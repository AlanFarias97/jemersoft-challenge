package com.challenge.config.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public abstract class EventData implements Serializable {

    protected Boolean isStandBy;
    protected Integer secondsStandBy;
    protected QueueGeneric queueGeneric;
}
