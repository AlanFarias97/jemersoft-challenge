package com.challenge.config.event;

import com.challenge.persistence.model.Event;
import com.challenge.persistence.repository.EventRepository;
import com.challenge.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
@Slf4j
public class EventExecutor {

    private final EventRepository eventRepository;


    private final PlatformTransactionManager transactionManager;

    public EventExecutor(EventRepository eventRepository, PlatformTransactionManager transactionManager) {
        this.eventRepository = eventRepository;
        this.transactionManager = transactionManager;
    }


    public void run(Event event, EventGeneric<EventData> eventConsumerGeneric) {


        LocalDateTime initial = LocalDateTime.now();
        TransactionStatus status = null;
        try {
            var data = Objects.requireNonNull(JsonUtils.convertMapSerializableToObject(event.getData(), event.getClassNameData()));
            data.setIsStandBy(false);
            data.setSecondsStandBy(0);
            eventConsumerGeneric.executeConsumer(data);

            eventFinOk(event, initial);

        } catch (Throwable ex) {
            log.info(ex.getMessage());
        }
    }



    public void eventFinOk(Event event, LocalDateTime initial) {
        event.setIsProcessing(false);
        event.setIsError(false);
        event.setIsFinalized(true);
        event.setIsStandBy(false);
        event.setAtProcessing(initial);
        event.setAtFinalized(LocalDateTime.now());
//        event.setDurationMilliseconds(DateTimeUtils.getMilliseconds(event.getAtProcessing(),event.getAtFinalized()));
//        event.setDurationCompleteMilliseconds(DateTimeUtils.getMilliseconds(event.getAtCreate(),event.getAtFinalized()));
        event.setRetriesCurrent(event.getRetriesCurrent() + 1);
        event.setFinalState("OK");
        saveOrUpdateEvent(event);
    }

    public void saveOrUpdateEvent(Event event) {
        eventRepository.save(event);

    }

}
