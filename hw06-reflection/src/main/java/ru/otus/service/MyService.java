package ru.otus.service;

public class MyService {
    public String shouldReturnString() {
        return "String";
    }

    public void failMethod() {
        throw new RuntimeException("Failed test");
    }
}
