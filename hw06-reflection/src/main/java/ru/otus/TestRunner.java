package ru.otus;

import org.jetbrains.annotations.Nullable;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.utils.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private static final String SUCCESS = "Success";

    @Nullable
    public static <T> Map<String, String> run(Class<T> testClass) {
        List<Method> tests = new ArrayList<>();
        Method initMethod = null;
        Method tearDownMethod = null;

        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            boolean test = method.isAnnotationPresent(Test.class);
            boolean init = method.isAnnotationPresent(Before.class);
            boolean tearDown = method.isAnnotationPresent(After.class);
            if (test && (init || tearDown)) {
                System.out.printf("Error while launching test for class [%s]. Method [%s] cannot be annotated @Test and @Before or @After at the same time\n", testClass.getCanonicalName(), method.getName());
                return null;
            }

            if (init && tearDown) {
                System.out.printf("Error while launching test for class [%s]. Method [%s] cannot be annotated @Before and @After at the same time\n", testClass.getCanonicalName(), method.getName());
                return null;
            }

            if (init) {
                initMethod = method;
            }

            if (tearDown) {
                tearDownMethod = method;
            }

            if (test) {
                tests.add(method);
            }
        }

        Map<String, String> testResult = new HashMap<>();
        for (Method test : tests) {
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
                testResult.put(testName, SUCCESS);
            } catch (Exception exc) {
                testResult.put(testName, exc.getMessage());
            }
        }

        return testResult;
    }
}
