package ru.otus;

public enum Nominal {
    _50(50),
    _100(100),
    _200(200),
    _500(500),
    _1000(1000);

    private long amount;

    Nominal(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }
}
