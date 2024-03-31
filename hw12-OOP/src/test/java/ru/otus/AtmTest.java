package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.Nominal._100;
import static ru.otus.Nominal._50;
import static ru.otus.Nominal._500;

@DisplayName("Atm should ")
public class AtmTest {
    @DisplayName("return specified amount, and subtract returned money from money in ATM")
    @Test
    void atmSuccessScenario() {
        // Given
        Map<Nominal, Integer> initialMoney = new HashMap<>();
        initialMoney.put(_50, 10);
        initialMoney.put(_100, 10);
        initialMoney.put(_500, 10);
        Atm atm = new SimpleAtm(initialMoney);

        // When
        Map<Nominal, Integer> cash = atm.giveOutAmount(650);

        // Then
        assertEquals(cash.get(_50), 1);
        assertEquals(cash.get(_100), 1);
        assertEquals(cash.get(_500), 1);
        Map<Nominal, Integer> restCash = atm.giveOutRest();
        assertEquals(restCash.get(_50), 9);
        assertEquals(restCash.get(_100), 9);
        assertEquals(restCash.get(_500), 9);
    }

    @Test
    @DisplayName("throw NotProcessableAmountException if there is not enough banknotes for specified amount")
    void atmNotProcessableAmountError() {
        // Given
        Map<Nominal, Integer> initialMoney = new HashMap<>();
        initialMoney.put(_50, 10);
        initialMoney.put(_100, 10);
        initialMoney.put(_500, 10);
        Atm atm = new SimpleAtm(initialMoney);

        // When
        assertThatThrownBy(() -> atm.giveOutAmount(575)).isInstanceOf(NotProcessableAmountException.class);


        // Then
        Map<Nominal, Integer> restMoney = atm.giveOutRest();
        assertEquals(10, restMoney.get(_50));
        assertEquals(10, restMoney.get(_100));
        assertEquals(10, restMoney.get(_500));
    }

    @Test
    @DisplayName("throw NotProcessableAmountException if there is not enough money for specified amount")
    void atmNotEnoughMoney() {
        // Given
        Map<Nominal, Integer> initialMoney = new HashMap<>();
        initialMoney.put(_50, 10);
        Atm atm = new SimpleAtm(initialMoney);

        // When
        assertThatThrownBy(() -> atm.giveOutAmount(600)).isInstanceOf(NotProcessableAmountException.class);

        // Then
        Map<Nominal, Integer> restMoney = atm.giveOutRest();
        assertEquals(10, restMoney.get(_50));
    }
}
