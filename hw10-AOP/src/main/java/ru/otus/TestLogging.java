package ru.otus;

public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public int calculation(int number) {
        return number;
    }

    @Log
    @Override
    public int calculation(int x, int y) {
        return x * y;
    }

    @Override
    public int calculation(int x, int y, int z) {
        return x * y * z;
    }
}
