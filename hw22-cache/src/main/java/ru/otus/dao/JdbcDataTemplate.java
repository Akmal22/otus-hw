package ru.otus.dao;

import ru.otus.dao.orm.EntityClassMetaData;
import ru.otus.dao.orm.EntitySQLMetaData;
import ru.otus.dao.orm.executor.DBExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcDataTemplate<T> implements DataTemplate<T> {
    private final DBExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;


    public JdbcDataTemplate(DBExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
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
    public void update(Connection connection, T t) {
        List<Object> params = entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> getFieldValue(f, t))
                .collect(Collectors.toList());
        params.add(getFieldValue(entityClassMetaData.getIdField(), t));

        dbExecutor.executeStatement(connection, entitySQLMetaData.updateSql(), params);
    }

    @Override
    public long insert(Connection connection, T t) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.insertSql(), entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> getFieldValue(f, t))
                .collect(Collectors.toList()));
    }


    private T getObjectFromResultSet(ResultSet resultSet) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        T t = entityClassMetaData.getConstructor().newInstance();
        entityClassMetaData.getAllFields().forEach(f -> {
            try {
                var fieldValue = resultSet.getObject(f.getName(), f.getType());
                f.set(t, fieldValue);
            } catch (Exception exc) {
                throw new DataTemplateException(exc);
            }
        });

        return null;
    }

    private Object getFieldValue(Field field, T entity) {
        try {
            return field.get(entity);
        } catch (IllegalArgumentException | IllegalAccessException exc) {
            throw new DataTemplateException(exc);
        }
    }
}
