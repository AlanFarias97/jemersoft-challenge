package com.challenge.config.event;

import com.challenge.exception.BusinessException;
import com.challenge.exception.MessageCode;
import com.challenge.persistence.model.Event;
import com.challenge.persistence.repository.EventRepository;
import com.challenge.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EventBUS implements ApplicationListener<ContextClosedEvent> {
    private  final List<EventGeneric<EventData>> eventListen = new ArrayList<>();

    private final EventRepository eventRepository;

    private final AmqpTemplate rabbitTemplate;
    private final EventExecutor eventExecutor;

    private volatile boolean contextClosed = false;

    public EventBUS(EventRepository eventRepository, AmqpTemplate rabbitTemplate, EventExecutor eventExecutor) {
        this.eventRepository = eventRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.eventExecutor = eventExecutor;
    }

    public void subscribe(EventGeneric<EventData> eventGenericConsumerGeneric) {
        eventListen.add(eventGenericConsumerGeneric);
    }

    public <D extends EventData> void publish(String operationId, D eventData, IEventType eventType, Integer delaySeconds) {
        this.publish(eventType.getQueue(), operationId, eventData, eventType, delaySeconds);
    }


    public <D extends EventData> void publish(QueueGeneric queueGeneric, String operationId, D eventData, IEventType eventType, Integer delaySeconds) {
        Event eventGeneric = new Event();
        try {
            eventGeneric.setClassNameData(eventData.getClass().getName());
           // eventGeneric.setData(JsonUtils.convertObjectToMapSerializable(eventData));
            eventGeneric.setAtCreate(LocalDateTime.now());
            eventGeneric.setIsProcessing(false);
            eventGeneric.setIsFinalized(false);
            eventGeneric.setIsError(false);
            eventGeneric.setIsStandBy(false);
            eventGeneric.setOperationId(operationId);
            eventGeneric.setTransactionId(UUID.randomUUID().toString());
            eventGeneric.setEventTypeName(eventType.findName());
            //eventGeneric.setTransaccional(eventType.isTransaccional());
            eventGeneric.setRetriesCurrent(0);
            eventGeneric.setFinalState("");
            eventGeneric.setQueueName(queueGeneric.name());
            if (delaySeconds == 0) {
                saveOrUpdateEvent(eventGeneric);
                Map<String, String> eventDataReq = new HashMap<>();
                eventDataReq.put("id", eventGeneric.getEventId().toString());
                eventDataReq.put(operationId, eventGeneric.getOperationId());
                rabbitTemplate.convertAndSend(queueGeneric.name(), eventDataReq);
                //partitionedQueue.send(queueArtico.name(), eventDataReq, eventGeneric.getOperationId().hashCode(), eventGeneric.getEventId());
            } else {
                eventGeneric.setFinalState("STAND_BY");
                eventGeneric.setIsStandBy(true);
                eventGeneric.setAtStartProcessStandBy(LocalDateTime.now().plusSeconds(delaySeconds));
                saveOrUpdateEvent(eventGeneric);
            }

        } catch (Exception e) {
            log.error("No se ha podido emitir el evento a la cola {}. ", queueGeneric.name(), e);
        }
    }

    public void saveOrUpdateEvent(Event event) {
            eventRepository.save(event);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        this.contextClosed = true;
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    public void processQueueIntern(Event event) {
        if (event == null) {
            return;
        }
        var eventConsumerList = eventListen.stream().filter(eventConsumerGeneric -> eventConsumerGeneric.eventListen().findName().equalsIgnoreCase(
                event.getEventTypeName()
        )).toList();
        eventRepository.updateIsProcessingAndAtProcessingByEventId(true, LocalDateTime.now(), event.getEventId());
        for (var eventConsumerGeneric : eventConsumerList) {
            eventExecutor.run(event, eventConsumerGeneric);
        }
    }

    @RabbitListener(queues = "POKEMON")
    public void handleMessageProposal(String jsonStr) {
        handleMessage(jsonStr, "POKEMON");
    }

    public void handleMessage(String jsonStr, String queue) {
        try {
            log.info("Mensaje recibido en handleMessageFrom  {}:  {}", queue, jsonStr);
            Event event = getEvent(jsonStr, "Se terminó de procesar evento de cola " + queue + ". attr {}={}");
            if (event == null) return;
            processQueueIntern(event);
            log.info("Se terminó de procesar evento de cola " + queue + ". attr {}={}", "id", event.getEventId());
        } catch (Exception e) {
            log.error("No se ha podido procesar el evento a la cola {}. ", queue, e);
        }
    }
    public Event getEvent(String jsonStr, String s) throws Exception {
        Map<String, Object> message = JsonUtils.convertStringJsonToMap(jsonStr);
        var eventResult = eventRepository.findById(Long.parseLong(message.get("id").toString()));
        int reintentos = 0;
        while (eventResult.isEmpty() && reintentos < 30) {
            TimeUnit.SECONDS.sleep(1);
            eventResult = eventRepository.findById(Long.parseLong(message.get("id").toString()));
            reintentos++;
        }
        if (this.contextClosed) {
            log.info("Se detecta que contexto se está cerrando");
            throw new BusinessException(MessageCode.ERROR_AT_LOADING_DATA .CONTEXT_CLOSED);
        }
        Event event = eventResult.orElseThrow(() -> new BusinessException(MessageCode.EVENT_NOT_FOUND));
        if (Boolean.TRUE.equals(event.getIsFinalized())) {
            log.info(s, "id", event.getEventId());
            return null;
        }
        return event;
    }
}
