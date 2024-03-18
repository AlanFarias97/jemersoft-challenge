package com.challenge.dataInit;

import com.challenge.config.event.EventBUS;
import com.challenge.config.event.EventData;
import com.challenge.config.event.EventGeneric;
import com.challenge.config.event.IEventType;
import com.challenge.controller.response.PokemonApiExternalResponse;
import com.challenge.core.event.EventType;
import com.challenge.exception.BusinessException;
import com.challenge.exception.MessageCode;
import com.challenge.persistence.model.Ability;
import com.challenge.persistence.model.Movement;
import com.challenge.persistence.repository.AbilityRepository;
import com.challenge.persistence.repository.MovementRepository;
import com.challenge.persistence.repository.PokemonRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class OnLoadInitialDataEv extends EventGeneric<OnLoadInitialDataEv.Data> {

    @Value(" ${url-pokeapi}")
    private String urlBasePokeApi;

    private final PokemonRepository pokemonRepository;
    private final AbilityRepository abilityRepository;
    private final MovementRepository movementRepository;
    private final RestTemplate restTemplate;

    public OnLoadInitialDataEv(EventBUS eventBUS, PokemonRepository pokemonRepository, AbilityRepository abilityRepository, MovementRepository movementRepository, RestTemplate restTemplate) {
        super(eventBUS);
        this.pokemonRepository = pokemonRepository;
        this.abilityRepository = abilityRepository;
        this.movementRepository = movementRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public void onEvent(Data eventData) throws Exception {
            try {
                loadPokemonTable();
                searchPokemonMovementAndAbilities();
            }catch (Exception e){
                throw new BusinessException(MessageCode.ERROR_CALLING_POKE_API);
            }
    }

    private void loadPokemonTable() {
        String urlBaseApi  = this.urlBasePokeApi.trim();
        String urlFinal =  urlBaseApi+"/pokemon?offset0&limit=1302";
        HttpEntity request = new HttpEntity(urlFinal);
        log.info("Calling api pokeapi pokemon services with url: " + urlFinal);
        log.info("");
        long startTime = System.currentTimeMillis();
        ResponseEntity<PokemonApiExternalResponse> response = restTemplate.exchange(urlFinal, HttpMethod.GET, request,PokemonApiExternalResponse.class );
        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Time taken: " + urlFinal + " - " + timeTaken + " milliseconds.");

        if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new BusinessException(MessageCode.ERROR_CALLING_POKE_API);
        }

        //valido que no exista otro registro con el mismo nombre para evitar repetidos
        response.getBody().getResults().stream().forEach(p -> {
            if(!pokemonRepository.existsByName(p.getName())){
                pokemonRepository.save(p);
            }
        });
    }

    private void searchPokemonMovementAndAbilities() {
        var pokemons = pokemonRepository.findAll();
        pokemons.forEach(pokemon -> {
            try{
                if(pokemon.getDescription()==null){
                    var externalPokemon = findExternalPokemonByName(pokemon.getName());
                    List<String> types = new ArrayList<>();
                    externalPokemon.getTypes().stream().forEach(t->{
                        types.add(t.getType().getName());
                    });
                    var movements = convertToMovements(externalPokemon.getMoves());
                    var abilities = convertToAbilities(externalPokemon.getAbilities());
                    movements.stream().forEach(mov ->{
                        if(pokemon.getPokemonMovements().stream().noneMatch(pm -> pm.getName().equals(mov.getName()))){
                            if(movementRepository.existsByName(mov.getName())){
                                pokemon.getPokemonMovements().add(movementRepository.findByName(mov.getName()).get());
                            }else{
                                pokemon.getPokemonMovements().add(mov);
                            }
                        }
                    });
                    abilities.stream().forEach(ab ->{
                        if(pokemon.getPokemonAbilities().stream().noneMatch(pa -> pa.getName().equals(ab.getName()))) {
                            if (abilityRepository.existsByName(ab.getName())) {
                                pokemon.getPokemonAbilities().add(abilityRepository.findByName(ab.getName()).get());
                            } else {
                                pokemon.getPokemonAbilities().add(ab);
                            }
                        }
                    });
                    pokemon.setWeight(externalPokemon.getWeight());
                    pokemon.setType(types);
                    pokemon.setUrl(externalPokemon.getSprites().getOther().getHome().getFront_default());
                    pokemon.setDescription(findExternalURL(pokemon.getPokemonId()));
                    pokemonRepository.save(pokemon);
                }
            } catch (Exception e){
                throw new BusinessException(MessageCode.ERROR_AT_LOADING_DATA);
            }
        });
    }

    private PokemonApiExternalResponse.PokemonExternalDTO findExternalPokemonByName(String name) {
        String urlBaseApi  = this.urlBasePokeApi.trim();
        String urlFinal =  urlBaseApi+"/pokemon/"+name;
        HttpEntity request = new HttpEntity(urlFinal);
        ResponseEntity<PokemonApiExternalResponse.PokemonExternalDTO> response = restTemplate.exchange(urlFinal, HttpMethod.GET, request,PokemonApiExternalResponse.PokemonExternalDTO.class );
        if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new BusinessException(MessageCode.ERROR_CALLING_POKE_API);
        }
        return response.getBody();
    }

    private Set<Movement> convertToMovements(List<PokemonApiExternalResponse.MovementDTO> movements) {
        Set<Movement> movementSet = new HashSet<>();
        movements.stream().forEach(m->{
            movementSet.add(Movement.builder()
                    .name(m.getMove().getName())
                    .build());
        });
        return movementSet;
    }



    private Set<Ability> convertToAbilities(List<PokemonApiExternalResponse.AbilityDTO> movements) {
        Set<Ability> abilitiesSet = new HashSet<>();
        movements.stream().forEach(m->{
            abilitiesSet.add(Ability.builder()
                    .name(m.getAbility().getName())
                    .build());
        });
        return abilitiesSet;
    }

    private String findExternalURL(Long id) {
        String urlBaseApi  = this.urlBasePokeApi.trim();
        String urlFinal =  urlBaseApi+"/pokemon-species/"+id;
        HttpEntity request = new HttpEntity(urlFinal);
        ResponseEntity<PokemonApiExternalResponse.PokemonExternalURL> response = restTemplate.exchange(urlFinal, HttpMethod.GET, request,PokemonApiExternalResponse.PokemonExternalURL.class );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new BusinessException(MessageCode.ERROR_CALLING_POKE_API);
        }
        return response.getBody().getFlavor_text_entries().stream().filter(d -> d.getLanguage().getName().equals("es")).findFirst().get().getFlavor_text();
    }


    @Override
    public IEventType eventListen() {
        return EventType.LOAD_INITIAL_DATA;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Data extends EventData {
            private LocalDateTime initData;
    }
}
