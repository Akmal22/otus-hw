package ru.otus.orm;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final String SELECT_ALL_SQL = "select * from %s";
    private static final String SELECT_BY_ID_SQL = "select * from %s where %s = ?";
    private static final String INSERT_SQL = "insert into %s (%s) values (%s)";
    private static final String UPDATE_SQL = "update %s set %s where %s = ?";

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
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
    public String getInsertSql() {
        return String.format(INSERT_SQL, entityClassMetaData.getName(), getFieldsNamesWithoutId(), getAllFieldsValueSubstitution());
    }

    @Override
    public String getUpdateSql() {
        return String.format(UPDATE_SQL, entityClassMetaData.getName(), getAllFieldsUpdateStatement(), entityClassMetaData.getIdField().getName());
    }

    private String getAllFieldsUpdateStatement() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> String.format("%s = ?", f.getName()))
                .collect(Collectors.joining(","));
    }

    private String getFieldsNamesWithoutId() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private String getAllFieldsValueSubstitution() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(f -> "?")
                .collect(Collectors.joining(","));
    }
}
