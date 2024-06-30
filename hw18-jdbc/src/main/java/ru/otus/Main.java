package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.datasource.DriverManagerDataSource;
import ru.otus.model.Client;
import ru.otus.orm.DataTemplateJdbc;
import ru.otus.orm.EntityClassMetaData;
import ru.otus.orm.EntityClassMetaDataImpl;
import ru.otus.orm.EntitySQLMetaData;
import ru.otus.orm.EntitySQLMetaDataImpl;
import ru.otus.repository.executor.DbExecutorImpl;
import ru.otus.service.DbServiceClientImpl;
import ru.otus.service.DbServiceClientWrapper;
import ru.otus.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); // реализация DataTemplate, универсальная

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        DbServiceClientWrapper dbServiceClientWrapper = new DbServiceClientWrapper(dbServiceClient);

        for (int i = 0; i < 1000; i++) {
            dbServiceClientWrapper.saveClient(new Client("client" + i));
        }

        log.info("Cache size before GC: {}", dbServiceClientWrapper.getCacheSize());

        System.gc();
        Thread.sleep(1000);
        log.info("Cache size after GC: {}", dbServiceClientWrapper.getCacheSize());

//        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
//        var clientSecondSelected = dbServiceClient
//                .getClient(clientSecond.getId())
//                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
//        log.info("clientSecondSelected:{}", clientSecondSelected);
//
//        // Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)
//
//        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
//        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager);
//        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);
//
//        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
//        dbServiceManager.saveManager(new Manager("ManagerFirst"));
//
//        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
//        var managerSecondSelected = dbServiceManager
//                .getManager(managerSecond.getNo())
//                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
//        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}