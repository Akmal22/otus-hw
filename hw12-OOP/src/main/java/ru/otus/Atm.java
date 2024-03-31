package ru.otus;

import java.util.Map;

public interface Atm {
    void insert(Nominal nominal);

    Map<Nominal, Integer> giveOutAmount(long amount);

    Map<Nominal, Integer> giveOutRest();
}
