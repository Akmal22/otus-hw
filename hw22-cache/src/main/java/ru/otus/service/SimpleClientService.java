package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.DataTemplate;
import ru.otus.dao.sessionmanager.TransactionManager;
import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public class SimpleClientService implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(SimpleClientService.class);
    private final DataTemplate<Client> dataTemplate;
    private final TransactionManager transactionManager;

    public SimpleClientService(DataTemplate<Client> dataTemplate, TransactionManager transactionManager) {
        this.dataTemplate = dataTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(connection -> {
            if (client.getId() == null) {
                long id = dataTemplate.insert(connection, client);
                client.setId(id);
                logger.info("saved client: {}", client);
                return client;
            }
            dataTemplate.update(connection, client);
            logger.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> findById(long id) {
        return transactionManager.doInTransaction(connection -> dataTemplate.findById(connection, id));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(dataTemplate::findAll);
    }
}
