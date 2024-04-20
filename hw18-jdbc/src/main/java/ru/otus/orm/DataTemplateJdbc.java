package ru.otus.orm;

import ru.otus.repository.DataTemplate;
import ru.otus.repository.DataTemplateException;
import ru.otus.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return getObjectFromResultSet(rs);
                }

                return null;
            } catch (Exception exc) {
                throw new DataTemplateException(exc);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                List<T> resultList = new ArrayList<>();
                while (rs.next()) {
                    resultList.add(getObjectFromResultSet(rs));
                }

                return resultList;
            } catch (Exception exc) {
                throw new DataTemplateException(exc);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> getFieldValue(f, entity))
                .collect(Collectors.toList()));

    }

    @Override
    public void update(Connection connection, T entity) {
        List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> getFieldValue(f, entity))
                .collect(Collectors.toList());

        params.add(getFieldValue(entityClassMetaData.getIdField(), entity));
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }

    private Object getFieldValue(Field field, T entity) {
        try {
            return field.get(entity);
        } catch (IllegalArgumentException | IllegalAccessException exc) {
            throw new DataTemplateException(exc);
        }
    }

    private T getObjectFromResultSet(ResultSet rs) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        T t = entityClassMetaData.getConstructor().newInstance();
        entityClassMetaData.getAllFields().forEach(f -> {
            try {
                Object fieldValue = rs.getObject(f.getName(), f.getType());
                f.set(t, fieldValue);
            } catch (Exception exc) {
                throw new DataTemplateException(exc);
            }
        });

        return t;
    }
}
