package ru.otus.dao.orm.executor;

import ru.otus.dao.sessionmanager.DataBaseOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SimpleDBExecutor implements DBExecutor {
    @Override
    public long executeStatement(Connection connection, String sql, List<Object> params) {
        try (PreparedStatement prs = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (var idx = 0; idx < params.size(); idx++) {
                prs.setObject(idx + 1, params.get(idx));
            }
            prs.executeUpdate();
            try (var rs = prs.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException exc) {
            throw new DataBaseOperationException("Error while executing insert statement", exc);
        }
    }

    @Override
    public <T> Optional<T> executeSelect(Connection connection, String sql, List<Object> params,
                                         Function<ResultSet, T> rsHandler) {
        try (PreparedStatement prs = connection.prepareStatement(sql)) {
            for (var idx = 0; idx < params.size(); idx++) {
                prs.setObject(idx + 1, params.get(idx));
            }

            try (var rs = prs.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        } catch (SQLException exc) {
            throw new DataBaseOperationException("Error while executing select statement", exc);
        }
    }
}
