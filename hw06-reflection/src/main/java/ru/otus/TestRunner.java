package ru.otus;

import org.javatuples.Pair;
import org.jetbrains.annotations.Nullable;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.exception.MethodTypeException;
import ru.otus.utils.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    private static final String SUCCESS = "Success";

    @Nullable
    public static <T> List<Pair<String, String>> run(Class<T> testClass) {
        List<Method> tests = new ArrayList<>();
        Method initMethod = null;
        Method tearDownMethod = null;

        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            try {
                MethodType methodType = validateAndGetMethodType(method);
                if (methodType != null) {
                    switch (methodType) {
                        case BEFORE -> initMethod = method;
                        case AFTER -> tearDownMethod = method;
                        case TEST -> tests.add(method);
                    }
                }

            } catch (MethodTypeException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        List<Pair<String, String>> testResult = new ArrayList<>();
        for (Method test : tests) {
            testResult.add(runTest(testClass, test, initMethod, tearDownMethod));
        }

        return testResult;
    }

    private static MethodType validateAndGetMethodType(Method method) throws MethodTypeException {
        boolean test = method.isAnnotationPresent(Test.class);
        boolean init = method.isAnnotationPresent(Before.class);
        boolean tearDown = method.isAnnotationPresent(After.class);
        if (test && (init || tearDown)) {
            String errorMessage = String.format("Error while launching test. Method [%s] cannot be annotated @Test and @Before or @After at the same time\n", method.getName());
            throw new MethodTypeException(errorMessage);
        }

        if (init && tearDown) {
            String errorMessage = String.format("Error while launching test. Method [%s] cannot be annotated @Before and @After at the same time\n", method.getName());
            throw new MethodTypeException(errorMessage);
        }

        if (init) {
            return MethodType.BEFORE;
        }

        if (tearDown) {
            return MethodType.AFTER;
        }

        if (test) {
            return MethodType.TEST;
        }

        return null;
    }

    private static <T> Pair<String, String> runTest(Class<T> testClass, Method test,
                                                    @Nullable Method initMethod, @Nullable Method tearDownMethod) {
        String testName = test.getName();
        try {
            T testClassObject = ReflectionHelper.instantiate(testClass);
            if (initMethod != null) {
                ReflectionHelper.callMethod(testClassObject, initMethod.getName());
            }
            ReflectionHelper.callMethod(testClassObject, testName);
            if (tearDownMethod != null) {
                ReflectionHelper.callMethod(testClassObject, tearDownMethod.getName());
            }
            return new Pair<>(testName, SUCCESS);
        } catch (Exception exc) {
            return new Pair<>(testName, exc.getMessage());
        }
    }

    private enum MethodType {
        TEST,
        BEFORE,
        AFTER
    }
}
