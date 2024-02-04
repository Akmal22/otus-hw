package ru.otus.test;

import org.assertj.core.api.Assertions;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.service.MyService;

public class MyServiceTest {
    private MyService myService;

    @Before
    public void init() {
        myService = new MyService();
    }

    @Test
    public void testSuccessReturnString() {
        String str = myService.shouldReturnString();
        Assertions.assertThat(str).isEqualTo("String");
    }

    @Test
    public void testFailScenario() {
        myService.failMethod();
    }

    @After
    public void tearDown() {
        myService = null;
    }
}
