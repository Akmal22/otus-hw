package ru.otus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static boolean logMethod(Method method) {
        return method.getDeclaredAnnotations().length > 0
                && method.isAnnotationPresent(Log.class);
    }

    public static String getMethodSignatureAsString(Method method) {
        return method.getName() + "::" + Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining("::"));
    }
}
