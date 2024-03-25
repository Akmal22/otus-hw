package ru.otus;

public class NotProcessableAmountException extends RuntimeException {
    public NotProcessableAmountException(String message) {
        super(message);
    }
}
