package ru.otus.dao.orm;

import ru.otus.dao.orm.EntityClassMetaData;
import ru.otus.dao.orm.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySqlMetaDataImpl<T> implements EntitySQLMetaData {
    private static final String SELECT_ALL_SQL = "select * from %s";
    private static final String SELECT_BY_ID_SQL = "select * from %s where %s = ?";
    private static final String UPDATE_SQL = "update %s set %s where %s = ?";
    private static final String INSERT_SQL = "insert into %s (%s) values (%s)";

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySqlMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format(SELECT_ALL_SQL, entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(SELECT_BY_ID_SQL, entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String insertSql() {
        return String.format(INSERT_SQL, entityClassMetaData.getName(), getFieldsNamesWithoutId(), getFieldsWithoutIdSub());
    }

    @Override
    public String updateSql() {
        return String.format(UPDATE_SQL, entityClassMetaData.getName(), getAllFieldsUpdateStatement(), entityClassMetaData.getIdField().getName());
    }

    private String getFieldsNamesWithoutId() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private String getFieldsWithoutIdSub() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> "?")
                .collect(Collectors.joining(","));
    }

    private String getAllFieldsUpdateStatement() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> String.format("%s=?", f.getName()))
                .collect(Collectors.joining(","));
    }
}
