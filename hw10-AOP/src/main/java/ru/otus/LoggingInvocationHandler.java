package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class LoggingInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    private final Set<Method> loggingMethods;

    private final TestLoggingInterface proxiedObject;

    public LoggingInvocationHandler(TestLoggingInterface proxiedObject) {
        this.proxiedObject = proxiedObject;
        this.loggingMethods = new HashSet<>();
        for (Method method : proxiedObject.getClass().getMethods()) {
            if (Utils.logMethod(method)) {
                loggingMethods.add(method);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method interfaceMethod, Object[] args) throws Throwable {
        Method objectMethod = proxiedObject.getClass().getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
        if (loggingMethods.contains(objectMethod)) {
            logger.info("executed method: {}, params: {}", interfaceMethod.getName(), args);
        }

        return interfaceMethod.invoke(proxiedObject, args);
    }
}
