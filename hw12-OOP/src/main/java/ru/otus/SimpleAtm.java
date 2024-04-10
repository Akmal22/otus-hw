package ru.otus;

import java.util.Map;

public class SimpleAtm implements Atm {
    private static final Nominal MIN_NOMINAL = Nominal._50;
    private final MoneyStorage moneyStorage;

    public SimpleAtm() {
        this.moneyStorage = new MoneyStorage();
    }

    public SimpleAtm(Map<Nominal, Integer> initialMoney) {
        moneyStorage = new MoneyStorage(initialMoney);
    }

    public void insert(Nominal nominal) {
        moneyStorage.insert(nominal);
    }

    public Map<Nominal, Integer> giveOutAmount(long amount) {
        if (amount < MIN_NOMINAL.getAmount()) {
            throw new IllegalArgumentException("Illegal amount");
        }

        return moneyStorage.getMoney(amount);
    }

    @Override
    public Map<Nominal, Integer> giveOutRest() {
        return moneyStorage.getAllMoney();
    }
}
