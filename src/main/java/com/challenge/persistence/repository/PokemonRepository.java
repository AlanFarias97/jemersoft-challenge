package com.challenge.persistence.repository;

import com.challenge.persistence.model.Pokemon;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonRepository extends CrudRepository<Pokemon, Long>, JpaSpecificationExecutor {

    @Override
    Optional<Pokemon> findById(Long id);
    boolean existsByName(String name);

    Optional<Pokemon> findByName(String name);
}
