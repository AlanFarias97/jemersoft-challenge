package com.challenge.config.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventFactory {

    private static final List<EventGeneric> events = new ArrayList<>();

    public static void subscribe(EventGeneric eventArtico) {
        events.add(eventArtico);
    }

    public static EventGeneric<EventData> getEvent(IEventType event) {
        return events.stream().filter(eventDataEventProducerGeneric ->
                eventDataEventProducerGeneric.eventListen().findName().equalsIgnoreCase(event.findName())).findFirst().orElse(null);
    }

}
