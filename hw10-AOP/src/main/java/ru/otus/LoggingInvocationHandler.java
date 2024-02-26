package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoggingInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    private final Map<Integer, Method> loggingMethods = new HashMap<>();

    private final TestLoggingInterface proxiedObject;

    public LoggingInvocationHandler(TestLoggingInterface proxiedObject) {
        this.proxiedObject = proxiedObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (loggingMethods.containsKey(Utils.getMethodHashCode(method, args.length))) {
            logger.info("executed method: {}, params: {}", method.getName(), args);
        } else if (proxiedObject.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class)) {
            loggingMethods.put(Utils.getMethodHashCode(method, args.length), method);
            logger.info("executed method: {}, params: {}", method.getName(), args);
        }

        return method.invoke(proxiedObject, args);
    }
}
