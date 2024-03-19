package ru.otus.model;

import java.util.ArrayList;

public class MessageMemento {
    private final long id;
    private final String field1;
    private final String field2;
    private final String field3;
    private final String field4;
    private final String field5;
    private final String field6;
    private final String field7;
    private final String field8;
    private final String field9;
    private final String field10;
    private final String field11;
    private final String field12;
    private final ObjectForMessage field13;

    public MessageMemento(Message message) {
        this.id = message.getId();
        this.field1 = message.getField1();
        this.field2 = message.getField2();
        this.field3 = message.getField3();
        this.field4 = message.getField4();
        this.field5 = message.getField5();
        this.field6 = message.getField6();
        this.field7 = message.getField7();
        this.field8 = message.getField8();
        this.field9 = message.getField9();
        this.field10 = message.getField10();
        this.field11 = message.getField11();
        this.field12 = message.getField12();
        ObjectForMessage objectForMessage = null;
        if (message.getField13() != null) {
            objectForMessage = new ObjectForMessage();
            if (message.getField13().getData() != null) {
                objectForMessage.setData(new ArrayList<>(message.getField13().getData()));
            }
        }
        this.field13 = objectForMessage;
    }

    public long getId() {
        return id;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }

    public String getField4() {
        return field4;
    }

    public String getField5() {
        return field5;
    }

    public String getField6() {
        return field6;
    }

    public String getField7() {
        return field7;
    }

    public String getField8() {
        return field8;
    }

    public String getField9() {
        return field9;
    }

    public String getField10() {
        return field10;
    }

    public String getField11() {
        return field11;
    }

    public String getField12() {
        return field12;
    }

    public ObjectForMessage getField13() {
        return field13;
    }
}
