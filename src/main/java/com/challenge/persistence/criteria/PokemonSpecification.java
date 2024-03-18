package com.challenge.persistence.criteria;

import com.challenge.persistence.model.Pokemon;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class PokemonSpecification {
    public static Specification<Pokemon> findPokemonByCriteria(PokemonCriteria criteria) {
        return (pokemonRoot, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(criteria!=null) {

                if (criteria.name != null) {
                    predicates.add(cb.like(cb.lower(pokemonRoot.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
                }

            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PokemonCriteria {
        private String name;
    }
}
