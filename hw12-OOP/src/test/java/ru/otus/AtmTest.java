package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Atm should ")
public class AtmTest {
    @DisplayName("return specified amount, and subtract returned money from money in ATM")
    @Test
    void atmSuccessScenario() {
        Map<Ruble, Integer> initialMoney = new HashMap<>();
        initialMoney.put(new Ruble(50.0), 10);
        initialMoney.put(new Ruble(100.0), 10);
        initialMoney.put(new Ruble(500.0), 10);
        Atm<Ruble> atm = new RubleAtm(initialMoney);

        Map<Ruble, Integer> cash = atm.giveOutAmount(650.0);

        assertEquals(cash.get(new Ruble(50.0)), 1);
        assertEquals(cash.get(new Ruble(100.0)), 1);
        assertEquals(cash.get(new Ruble(500.0)), 1);
        Map<Ruble, Integer> restCash = atm.giveOutRest();
        assertEquals(restCash.get(new Ruble(50.0)), 9);
        assertEquals(restCash.get(new Ruble(100.0)), 9);
        assertEquals(restCash.get(new Ruble(500.0)), 9);
    }

    @Test
    @DisplayName("throw NotProcessableAmountException if there is not enough banknotes for specified amount")
    void atmNotProcessableAmountError() {
        // Given
        Map<Ruble, Integer> initialMoney = new HashMap<>();
        initialMoney.put(new Ruble(50.0), 10);
        initialMoney.put(new Ruble(100.0), 10);
        initialMoney.put(new Ruble(500.0), 10);
        Atm<Ruble> atm = new RubleAtm(initialMoney);

        // When
        assertThatThrownBy(() -> atm.giveOutAmount(575.0)).isInstanceOf(NotProcessableAmountException.class);


        // Then
        Map<Ruble, Integer> restMoney = atm.giveOutRest();
        assertEquals(10, restMoney.get(new Ruble(50.0)));
        assertEquals(10, restMoney.get(new Ruble(100.0)));
        assertEquals(10, restMoney.get(new Ruble(500.0)));
    }

    @Test
    @DisplayName("throw NotProcessableAmountException if there is not enough banknotes for specified amount")
    void atmNotEnoughMoney() {
        // Given
        Map<Ruble, Integer> initialMoney = new HashMap<>();
        initialMoney.put(new Ruble(50.0), 10);
        Atm<Ruble> atm = new RubleAtm(initialMoney);

        // When
        assertThatThrownBy(() -> atm.giveOutAmount(600.0)).isInstanceOf(NotProcessableAmountException.class);

        // Then
        Map<Ruble, Integer> restMoney = atm.giveOutRest();
        assertEquals(10, restMoney.get(new Ruble(50.0)));
    }
}
