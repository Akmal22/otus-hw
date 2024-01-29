package ru.Otus;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> customersMap;

    public CustomerService() {
        Comparator<Customer> comparator = (c1, c2) -> (int) (c1.getScores() - c2.getScores());
        this.customersMap = new TreeMap<>(comparator);
    }

    public Map.Entry<Customer, String> getSmallest() {
        return safeCopy(customersMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> foundEntry = customersMap.higherEntry(customer);
        return safeCopy(foundEntry);
    }

    public void add(Customer customer, String data) {
        customersMap.put(customer, data);
    }

    private Map.Entry<Customer, String> safeCopy(Map.Entry<Customer, String> entry) {
        return Optional.ofNullable(entry)
                .map(f -> {
                    Customer foundCustomer = f.getKey();
                    String data = f.getValue();
                    return Map.entry(new Customer(foundCustomer.getId(), foundCustomer.getName(), foundCustomer.getScores()), data);
                }).orElse(null);
    }
}
