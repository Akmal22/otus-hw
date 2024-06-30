package ru.otus.dao.orm;

public interface EntitySQLMetaData {
    String getSelectAllSql();

    String getSelectByIdSql();

    String insertSql();

    String updateSql();
}
