package ru.otus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RubleAtm implements Atm<Ruble> {

    private final Map<Ruble, Integer> money = new HashMap<>();

    public RubleAtm() {
    }

    public RubleAtm(Map<Ruble, Integer> initialMoney) {
        this.money.putAll(initialMoney);
    }

    @Override
    public void insert(Ruble bankNote) {
        Integer bankNoteCount = money.get(bankNote);
        if (bankNoteCount == null) {
            money.put(bankNote, 1);
        } else {
            money.put(bankNote, ++bankNoteCount);
        }
    }

    @Override
    public Map<Ruble, Integer> giveOutAmount(Double amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount is null");
        }
        HashMap<Ruble, Integer> giveOutMoney = new HashMap<>();
        List<Ruble> bankNotes = this.money.keySet().stream()
                .sorted(Comparator.comparingDouble(Ruble::getAmount))
                .toList().reversed();
        Map<Ruble, Integer> tmpMoney = new HashMap<>(this.money);
        Double giveOutAmount = 0.0;
        for (Ruble ruble : bankNotes) {
            int giveOutBankNoteCount = 0;
            int bankNoteCount = money.get(ruble);
            while (giveOutAmount < amount && bankNoteCount > 0) {
                if (giveOutAmount + ruble.getAmount() > amount) {
                    break;
                }
                giveOutMoney.put(ruble, ++giveOutBankNoteCount);
                giveOutAmount += ruble.getAmount();
                bankNoteCount--;
            }
            tmpMoney.put(ruble, bankNoteCount);
        }

        if (!giveOutAmount.equals(amount)) {
            throw new NotProcessableAmountException("Inserted amount of money cannot be given out");
        }

        money.putAll(tmpMoney);

        return giveOutMoney;
    }

    @Override
    public Map<Ruble, Integer> giveOutRest() {
        HashMap<Ruble, Integer> giveOut = new HashMap<>(this.money);
        this.money.clear();

        return giveOut;
    }
}
