package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class LoggingInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    private final Set<Method> loggingMethods = new HashSet<>();

    private final TestLoggingInterface proxiedObject;

    public LoggingInvocationHandler(TestLoggingInterface proxiedObject) {
        this.proxiedObject = proxiedObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (loggingMethods.contains(method)) {
            logger.info("executed method: {}, params: {}", method.getName(), args);
        } else if (Utils.logMethod(proxiedObject.getClass().getMethod(method.getName(), method.getParameterTypes()))) {
            loggingMethods.add(method);
            logger.info("executed method: {}, params: {}", method.getName(), args);
        }

        return method.invoke(proxiedObject, args);
    }
}
