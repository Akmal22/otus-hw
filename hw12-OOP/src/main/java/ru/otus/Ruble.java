package ru.otus;

import ru.otus.BankNote;

import java.util.Objects;

public class Ruble implements BankNote {
    private static final String currency = "Ruble";
    private final Double amount;

    public Ruble(Double amount) {
        this.amount = amount;
    }

    @Override
    public String currency() {
        return currency;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Ruble{" +
                "amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ruble ruble)) return false;
        return Objects.equals(amount, ruble.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
