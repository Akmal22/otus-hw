package ru.otus;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface testLogging = ProxiedObjectsFactory.getLogging();
        testLogging.calculation(6);
        testLogging.calculation(6, 4);
        testLogging.calculation(4, 5, 6);
    }
}