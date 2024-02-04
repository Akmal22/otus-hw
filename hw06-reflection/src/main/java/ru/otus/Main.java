package ru.otus;

import ru.otus.test.MyServiceTest;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> testResult = TestRunner.run(MyServiceTest.class);
        System.out.println("Tests result:");
        if (testResult != null) {
            for (Map.Entry<String, String> entry : testResult.entrySet()) {
                System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
            }
        } else {
            System.out.println("Test run error");
        }
    }
}