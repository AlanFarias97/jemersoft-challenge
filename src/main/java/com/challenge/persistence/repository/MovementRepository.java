package com.challenge.persistence.repository;

import com.challenge.persistence.model.Movement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long>, JpaSpecificationExecutor {
    boolean existsByName(String name);


    Optional<Movement> findByName(String name);
}
