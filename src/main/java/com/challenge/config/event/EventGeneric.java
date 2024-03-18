package com.challenge.config.event;

public abstract class EventGeneric<D extends EventData> {


    private final EventBUS eventBUS;


    protected EventGeneric(EventBUS eventBUS) {
        this.eventBUS = eventBUS;
        eventBUS.subscribe((EventGeneric<EventData>)this);
        EventFactory.subscribe(this);
    }

    public void executeConsumer(D event) throws Exception {
        onEvent(event);
    }

    public void publishEvent(String operationId, D eventData) {
        try {
            preValidation(eventData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        eventBUS.publish(operationId, eventData, eventListen(),0);
    }

    public abstract void onEvent(D eventData) throws Exception;

    public abstract IEventType eventListen();

    public void preValidation(D eventData) throws Exception {
        // Puede ser sobrescrito por los eventos
    }
}
