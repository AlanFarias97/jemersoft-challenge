package com.challenge.controller.response;

import com.challenge.persistence.model.Pokemon;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PokemonListResponse {

    private Long pokemonId;
    private String name;
    private String url;
    private List<String> type;
    private int weight;
    private List<AbilityDTO> abilities;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AbilityDTO{
        private Long abilityId;
        private String abilityName;
    }
    public static PokemonListResponse convertTo(Pokemon p) {
        List<AbilityDTO> abilitiesList = new ArrayList<>();
        p.getPokemonAbilities().stream().forEach(pa ->{
            abilitiesList.add(AbilityDTO.builder()
                    .abilityId(pa.getAbilityId())
                    .abilityName(pa.getName())
                    .build());
        });
        return PokemonListResponse.builder()
                .pokemonId(p.getPokemonId())
                .name(p.getName())
                .url(p.getUrl())
                .type(p.getType())
                .weight(p.getWeight())
                .abilities(abilitiesList)
                .build();
    }
}
