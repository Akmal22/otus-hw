package ru.Otus;

import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {
    private final Deque<Customer> customersDeque = new LinkedList<>();

    public void add(Customer customer) {
        customersDeque.add(customer);
    }

    public Customer take() {
        return customersDeque.pollLast();
    }
}
