package com.challenge.persistence.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NM_EVENT_ID", unique = true)
    private Long eventId;

    @Column(name = "VA_OPERATION_ID")
    private String operationId;

    @Column(name = "VA_TRANSACTION_ID")
    private String transactionId;

    @Column(columnDefinition = "jsonb", nullable = false, updatable = true, name = "JSON_DATA")
    @Type(JsonBinaryType.class)
    private Map<String, Serializable> data = new HashMap<>();


    @Column(name = "VA_CLASS_NAME_DATA", nullable = false, columnDefinition = "text")
    private String classNameData;


    @Column(name = "VA_EVENT_TYPE_NAME", nullable = false, columnDefinition = "text")
    private String eventTypeName;

    @Column(name = "VA_QUEUE_NAME", nullable = false, columnDefinition = "text")
    private String queueName;

    @Column(name = "BO_PROCESSING", nullable = false)
    private Boolean isProcessing;

    @Column(name = "BO_ERROR", nullable = false)
    private Boolean isError;

    @Column(name = "BO_STAND_BY", nullable = false)
    private Boolean isStandBy;

    /**
     * Cuando un evento queda en stand by, se setea cuantos minutos se desea que permanezca en este estado
     * de esta manera, cuando el Timer este levantando este tipo de eventos, lo va levantar dps que haya pasado X minutos
     * desde que se volvio a grabar en la tabla
     */
    @Column(name = "FE_START_PROCESS_STAND_BY")
    private LocalDateTime atStartProcessStandBy;

    @Column(name = "BO_FINALIZED", nullable = false)
    private Boolean isFinalized;

    @Column(name = "FE_CREATE")
    private LocalDateTime atCreate;
    @Column(name = "FE_PROCESSING")
    private LocalDateTime atProcessing;
    @Column(name = "FE_FINALIZED")
    private LocalDateTime atFinalized;


    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<EventError> errors = new HashSet<>();

    @Column(name = "NM_RETRIES_CURRENT")
    private Integer retriesCurrent;

    @Column(name = "VA_FINAL_STATE")
    private String finalState;
    @Column(name = "NM_DURATION_MILLISECONDS")
    private Integer durationMilliseconds;

    @Column(name = "NM_DURATION_COMPLETE_MILLISECONDS")
    private Integer durationCompleteMilliseconds;


//    @Column(name = "BO_TRANSACCIONAL", nullable = false, columnDefinition = "integer default 0")
//    @Type(type = "org.hibernate.type.NumericBooleanType")
//    private boolean transaccional;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "ART_EVENTS_ERROR")
    public static class EventError implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(unique = true, name = "NM_EVENT_ERROR_ID")
        private Long eventErrorId;

        @ManyToOne
        @JoinColumn(name = "NM_EVENT_ID", nullable = false, referencedColumnName = "NM_EVENT_ID"
                , foreignKey = @ForeignKey(name = "fk_EventError_Event"))
        private Event event;

        @Column(name = "FE_CREATE")
        private LocalDateTime atCreate;

        @Column(name = "FE_FINALIZED")
        private LocalDateTime atFinalized;

        @Column(name = "VA_ERROR_CODE", nullable = true)
        private String errorCode;

        @Column(name = "VA_ERROR_MSG", nullable = true, columnDefinition = "text")
        private String errorMsg;

        @Column(name = "VA_ERROR_DETAIL", nullable = true, columnDefinition = "text")
        private String errorDetail;
    }
}
