package ru.otus.listener;

import ru.otus.model.Message;
import ru.otus.model.MessageMemento;
import ru.otus.model.ObjectForMessage;

import java.util.HashMap;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private HashMap<Long, MessageMemento> messageHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messageHistory.put(msg.getId(), new MessageMemento(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageHistory.get(id))
                .map(this::convertMessageMemento);
    }

    private Message convertMessageMemento(MessageMemento messageMemento) {
        Message.Builder builder = new Message.Builder(messageMemento.getId())
                .field1(messageMemento.getField1())
                .field2(messageMemento.getField2())
                .field3(messageMemento.getField3())
                .field4(messageMemento.getField4())
                .field5(messageMemento.getField5())
                .field6(messageMemento.getField6())
                .field7(messageMemento.getField7())
                .field8(messageMemento.getField8())
                .field9(messageMemento.getField9())
                .field10(messageMemento.getField10())
                .field11(messageMemento.getField11())
                .field12(messageMemento.getField12());
        ObjectForMessage objectForMessage = null;
        if (messageMemento.getField13() != null) {
            objectForMessage = new ObjectForMessage();
            if (messageMemento.getField13().getData() != null) {
                objectForMessage.setData(messageMemento.getField13().getData());
            }
        }

        builder.field13(objectForMessage);

        return builder.build();
    }
}
