package ru.otus.dao.sessionmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class SimpleTransactionManager implements TransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTransactionManager.class);
    private final DataSource dataSource;

    public SimpleTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T doInTransaction(TransactionAction<T> action) {
        return wrapException(() -> {
            try (Connection connection = dataSource.getConnection()) {
                try {
                    var result = action.apply(connection);
                    connection.commit();
                    return result;
                } catch (SQLException exc) {
                    logger.error("Error while running action in transaction", exc);
                    throw new DataBaseOperationException("Error while running action in transaction", exc);
                }
            }
        });
    }

    private <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception exc) {
            throw new DataBaseOperationException("Error while running database action", exc);
        }
    }
}
