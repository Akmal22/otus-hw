package ru.otus.listener;

import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final HashMap<Long, Message> messageHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messageHistory.put(msg.getId(), msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageHistory.get(id));
    }
}
