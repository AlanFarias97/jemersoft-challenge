package com.challenge.persistence.repository;

import com.challenge.persistence.model.Event;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query("update Event e set e.isProcessing = ?1, e.atProcessing = ?2 where e.eventId = ?3")
    int updateIsProcessingAndAtProcessingByEventId(Boolean isProcessing, LocalDateTime atProcessing, Long eventId);
}
