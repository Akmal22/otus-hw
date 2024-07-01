package ru.otus.service;

import ru.otus.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

public class DbServiceClientWrapper implements DBServiceClient {
    private final DBServiceClient dbServiceClient;
    private final WeakHashMap<Long, Client> cache;

    public DbServiceClientWrapper(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
        this.cache = new WeakHashMap<>();
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = dbServiceClient.saveClient(client);
        Long cacheId = savedClient.getId().longValue();
        cache.put(cacheId, client);

        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        return Optional.ofNullable(cache.get(id))
                .or(() -> {
                    Optional<Client> optionalClient = dbServiceClient.getClient(id);
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

    public int getCacheSize() {
        return cache.size();
    }
}
