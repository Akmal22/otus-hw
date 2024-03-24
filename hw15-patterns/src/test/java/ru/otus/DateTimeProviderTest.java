package ru.otus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.processor.DateTimeProvider;

import java.time.LocalDateTime;

public class DateTimeProviderTest {

    @Test
    void testDateTimeProvider() {
        DateTimeProvider dateTimeProvider = LocalDateTime::now;
        Assertions.assertEquals(LocalDateTime.now().getSecond(), dateTimeProvider.getDate().getSecond());
    }
}
