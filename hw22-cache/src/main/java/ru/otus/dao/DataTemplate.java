package ru.otus.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface DataTemplate<T> {
    Optional<T> findById(Connection connection, long id);

    List<T> findAll(Connection connection);

    void update(Connection connection, T t);

    long insert(Connection connection, T t);
}
