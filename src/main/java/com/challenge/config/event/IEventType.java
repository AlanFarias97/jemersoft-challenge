package com.challenge.config.event;

public interface IEventType {
    String findName();

    QueueGeneric getQueue();
    boolean isTransactional();
}
