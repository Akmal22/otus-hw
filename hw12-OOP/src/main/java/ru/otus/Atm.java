package ru.otus;

import java.util.Map;

public interface Atm<T extends BankNote> {
    void insert(T bankNote);

    Map<T, Integer> giveOutAmount(Double amount);

    Map<T, Integer> giveOutRest();
}
