package com.challenge.core.event;


import com.challenge.config.event.IEventType;
import com.challenge.config.event.QueueGeneric;

public enum EventType implements IEventType {

    LOAD_INITIAL_DATA("LoadInitialData", "LOAD_DATA", QueueGeneric.POKEMON, false ),

    ;

    EventType(String name, String operationType, QueueGeneric queueGeneric, boolean transactional) {
        this.name = name;
        this.operationType = operationType;
        this.queueGeneric = queueGeneric;
        this.transactional = transactional;
    }

    String name;
    String operationType;
    QueueGeneric queueGeneric;
    boolean transactional;
    @Override
    public String findName() {
        return queueGeneric + "-" + operationType +  "-" + name;

    }

    @Override
    public QueueGeneric getQueue() {
        return queueGeneric;
    }

    @Override
    public boolean isTransactional() {
        return transactional;
    }

}
