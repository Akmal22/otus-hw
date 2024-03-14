package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static ru.otus.Utils.getMethodSignatureAsString;

public class LoggingInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);
    private final Set<String> loggingMethods;
    private final TestLoggingInterface proxiedObject;

    public LoggingInvocationHandler(TestLoggingInterface proxiedObject) {
        this.proxiedObject = proxiedObject;
        this.loggingMethods = new HashSet<>();
        for (Method method : proxiedObject.getClass().getMethods()) {
            if (Utils.logMethod(method)) {
                loggingMethods.add(getMethodSignatureAsString(method));
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (loggingMethods.contains(getMethodSignatureAsString(method))) {
            logger.info("executed method: {}, params: {}", method.getName(), args);
        }

        return method.invoke(proxiedObject, args);
    }
}
