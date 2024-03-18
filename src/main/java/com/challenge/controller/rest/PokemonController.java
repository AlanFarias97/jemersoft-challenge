package com.challenge.controller.rest;

import com.challenge.config.event.EventFactory;
import com.challenge.controller.response.PokemonResponse;
import com.challenge.core.PokemonService;
import com.challenge.core.event.EventType;
import com.challenge.dataInit.OnLoadInitialDataEv;
import com.challenge.persistence.criteria.PokemonSpecification;
import com.challenge.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/challenge/v1/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping(path = "/list")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getPokemonList(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "-pokemonId") String sort,
            @RequestParam(defaultValue = "") String filter)  {
        PokemonSpecification.PokemonCriteria pokemonRequest = filter.isEmpty() ? null : (PokemonSpecification.PokemonCriteria) JsonUtils.base64ToObjectJson(filter, PokemonSpecification.PokemonCriteria.class.getName());
        Pageable paging = PageRequest.of(offset, limit, Sort.by(sort.charAt(0) == '-' ? Sort.Direction.ASC : Sort.Direction.ASC, sort.replace("-", "")));
        return ResponseEntity.ok(pokemonService.getPokemonList(pokemonRequest, paging));
    }



    @GetMapping(path = "/find/{pokemonId}")
    public ResponseEntity<PokemonResponse> findById(@NotNull @PathVariable("pokemonId") Long pokemonId) {
        return ResponseEntity.ok(pokemonService.findPokemonById(pokemonId));
    }

    @PostMapping(path = "/dataload")
    public ResponseEntity<String> loadDataInitial(){
        EventFactory.getEvent(EventType.LOAD_INITIAL_DATA).publishEvent(
                UUID.randomUUID().toString(),
                new OnLoadInitialDataEv.Data(LocalDateTime.now())
        );
        return ResponseEntity.ok("Loading initial information!!");
    }

}
