package ru.otus.service;

import ru.otus.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

public class ClientServiceWrapper implements ClientService {
    private final ClientService clientService;
    private final WeakHashMap<Long, Client> cache;

    public ClientServiceWrapper(ClientService clientService) {
        this.clientService = clientService;
        this.cache = new WeakHashMap<>();
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = clientService.saveClient(client);
        cache.put(getCacheKey(savedClient.getId()), savedClient);
        return savedClient;
    }

    @Override
    public Optional<Client> findById(long id) {
        return Optional.ofNullable(cache.get(getCacheKey(id)))
                .or(() -> {
                    Optional<Client> optionalClient = clientService.findById(id);
                    if (optionalClient.isPresent()) {
                        Client client = optionalClient.get();
                        cache.put(client.getId().longValue(), client);
                    }
                    return optionalClient;
                });
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(cache.values());
    }

    private Long getCacheKey(long id) {
        return 128 + id;
    }

    public int getCacheSize() {
        return cache.size();
    }
}
