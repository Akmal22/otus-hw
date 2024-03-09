package ru.otus;

import java.lang.reflect.Method;

public class Utils {
    public static <T> boolean logMethod(Method method) {
        return method.getDeclaredAnnotations().length > 0
                && method.isAnnotationPresent(Log.class);
    }
}
