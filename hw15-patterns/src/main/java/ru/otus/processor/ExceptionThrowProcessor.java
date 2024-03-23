package ru.otus.processor;

import ru.otus.model.Message;

public class ExceptionThrowProcessor implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ExceptionThrowProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new RuntimeException("Second has even value");
        }

        return message;
    }
}
