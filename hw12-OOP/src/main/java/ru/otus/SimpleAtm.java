package ru.otus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAtm implements Atm{
    private static final Nominal MIN_NOMINAL = Nominal._50;

    private final Map<Nominal, Integer> money = new HashMap<>();

    public SimpleAtm() {
    }

    public SimpleAtm(Map<Nominal, Integer> initialMoney) {
        this.money.putAll(initialMoney);
    }

    public void insert(Nominal nominal) {
        Integer nominalAmonut = money.get(nominal);
        if (nominalAmonut == null) {
            money.put(nominal, 1);
        } else {
            money.put(nominal, ++nominalAmonut);
        }
    }

    public Map<Nominal, Integer> giveOutAmount(long amount) {
        if (amount < MIN_NOMINAL.getAmount()) {
            throw new IllegalArgumentException("Illegal amount");
        }
        HashMap<Nominal, Integer> giveOutMoney = new HashMap<>();
        List<Nominal> nominals = this.money.keySet().stream()
                .sorted(Comparator.comparingLong(Nominal::getAmount))
                .toList().reversed();
        Map<Nominal, Integer> tmpMoney = new HashMap<>(this.money);
        Long giveOutAmount = 0L;
        for (Nominal nominal : nominals) {
            int giveoutNominalsCount = 0;
            int nominalCount = money.get(nominal);
            while (giveOutAmount < amount && nominalCount > 0) {
                if (giveOutAmount + nominal.getAmount() > amount) {
                    break;
                }
                giveOutMoney.put(nominal, ++giveoutNominalsCount);
                giveOutAmount += nominal.getAmount();
                nominalCount--;
            }
            tmpMoney.put(nominal, nominalCount);
        }

        if (!giveOutAmount.equals(amount)) {
            throw new NotProcessableAmountException("Inserted amount of money cannot be given out");
        }

        money.putAll(tmpMoney);

        return giveOutMoney;
    }

    @Override
    public Map<Nominal, Integer> giveOutRest() {
        HashMap<Nominal, Integer> giveOut = new HashMap<>(this.money);
        this.money.clear();

        return giveOut;
    }
}
