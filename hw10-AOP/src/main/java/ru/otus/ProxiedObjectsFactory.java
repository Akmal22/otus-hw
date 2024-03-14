package ru.otus;

import java.lang.reflect.Proxy;

public class ProxiedObjectsFactory {
    private ProxiedObjectsFactory() {
    }

    public static TestLoggingInterface getLogging() {
        LoggingInvocationHandler loggingInvocationHandler = new LoggingInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(TestLoggingInterface.class.getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, loggingInvocationHandler);
    }
}
