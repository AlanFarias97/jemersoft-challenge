package com.challenge.persistence.repository;

import com.challenge.persistence.model.Ability;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbilityRepository extends CrudRepository<Ability, Long>, JpaSpecificationExecutor {
    boolean existsByName(String name);

    Optional<Ability> findByName(String name);
}
