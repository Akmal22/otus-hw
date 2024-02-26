package ru.otus;

import java.lang.reflect.Method;

public class Utils {
    public static Integer getMethodHashCode(Method method, int argsSize) {
        return method.hashCode() + argsSize;
    }
}
