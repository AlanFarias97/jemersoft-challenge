package com.challenge.core;

import com.challenge.controller.response.*;
import com.challenge.exception.BusinessException;
import com.challenge.exception.MessageCode;
import com.challenge.persistence.criteria.PokemonSpecification;
import com.challenge.persistence.model.Pokemon;
import com.challenge.persistence.repository.PokemonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PokemonService {

    private final PokemonRepository pokemonRepository;


    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public PokemonResponse findPokemonById(Long pokemonId) {
        var pokemon = pokemonRepository.findById(pokemonId).orElseThrow(()-> new BusinessException(MessageCode.POKEMON_NOT_FOUND));
        var response = PokemonResponse.convertTo(pokemon);
        return response;
    }

    public Map<String, Object> getPokemonList(PokemonSpecification.PokemonCriteria pokemonRequest, Pageable paging) {
        Page<Pokemon> result = pokemonRepository.findAll(PokemonSpecification.findPokemonByCriteria(pokemonRequest), paging);
        Map<String, Object> response = new HashMap<>();
        response.put("data", result.getContent().stream().map(PokemonListResponse::convertTo).collect(Collectors.toList()));
        response.put("currentPage", result.getNumber());
        response.put("totalItems", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        return response;
    }

}
