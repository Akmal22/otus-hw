package ru.Otus;

import java.util.LinkedList;

public class CustomerReverseOrder {
    private final LinkedList<Customer> collection = new LinkedList<>();

    public void add(Customer customer) {
        collection.add(customer);
    }

    public Customer take() {
        return collection.pollLast();
    }
}
