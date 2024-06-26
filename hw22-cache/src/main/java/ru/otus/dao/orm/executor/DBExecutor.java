package ru.otus.dao.orm.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DBExecutor {
    long executeStatement(Connection connection, String sql, List<Object> params);

    <T> Optional<T> executeSelect(Connection connection, String sql, List<Object> params,
                              Function<ResultSet, T> rsHandler);
}
