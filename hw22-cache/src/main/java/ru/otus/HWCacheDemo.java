package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.JdbcDataTemplate;
import ru.otus.dao.datasource.DataSourceManager;
import ru.otus.dao.orm.EntityClassMetaData;
import ru.otus.dao.orm.EntityClassMetaDataImpl;
import ru.otus.dao.orm.EntitySQLMetaData;
import ru.otus.dao.orm.EntitySqlMetaDataImpl;
import ru.otus.dao.orm.executor.SimpleDBExecutor;
import ru.otus.dao.sessionmanager.SimpleTransactionManager;
import ru.otus.model.Client;
import ru.otus.service.ClientServiceWrapper;
import ru.otus.service.SimpleClientService;

import javax.sql.DataSource;

public class HWCacheDemo {
    private static final Logger log = LoggerFactory.getLogger(HWCacheDemo.class);

    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException, InterruptedException {
        new HWCacheDemo().demo();
    }

    private void demo() throws NoSuchMethodException, NoSuchFieldException, InterruptedException {
        // Общая часть
        var dataSource = new DataSourceManager(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new SimpleTransactionManager(dataSource);
        var dbExecutor = new SimpleDBExecutor();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySqlMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplate = new JdbcDataTemplate<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); // реализация DataTemplate, универсальная

        var clientService = new SimpleClientService(dataTemplate, transactionRunner);
        ClientServiceWrapper clientServiceWrapper = new ClientServiceWrapper(clientService);

        for (int i = 0; i < 1000; i++) {
            clientServiceWrapper.saveClient(new Client("client" + i));
        }

        log.info("Cache size before GC: {}", clientServiceWrapper.getCacheSize());

        System.gc();
        Thread.sleep(1000);
        log.info("Cache size after GC: {}", clientServiceWrapper.getCacheSize());
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
