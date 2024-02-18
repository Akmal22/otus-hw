package ru.otus;

import org.javatuples.Pair;
import ru.otus.test.MyServiceTest;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Pair<String, String>> testsResult = TestRunner.run(MyServiceTest.class);
        System.out.println("Tests result:");
        if (testsResult != null) {
            testsResult.forEach(testResult -> System.out.printf("%s: %s", testResult.getValue0(), testResult.getValue1()));
        } else {
            System.out.println("Test run error");
        }
    }
}