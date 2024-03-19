package com.challenge.controller.response;

import com.challenge.persistence.model.Pokemon;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PokemonResponse {
    private Long pokemonId;
    private String name;
    private String url;
    private List<String> type;
    private String description;
    private int weight;
    private List<PokemonListResponse.AbilityDTO> abilities;
    private List<MovementDTO> movements;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MovementDTO{
        private String movementName;
    }


    public static PokemonResponse convertTo(Pokemon p) {
        List<PokemonListResponse.AbilityDTO> abilitiesList = new ArrayList<>();
        p.getPokemonAbilities().stream().forEach(pa ->{
            abilitiesList.add(PokemonListResponse.AbilityDTO.builder()
                    .abilityId(pa.getAbilityId())
                    .abilityName(pa.getName())
                    .build());
        });
        List<PokemonResponse.MovementDTO> movementsList = new ArrayList<>();
        p.getPokemonMovements().stream().forEach(pm ->{
            movementsList.add(PokemonResponse.MovementDTO.builder()
                    .movementName(pm.getName())
                    .build());
        });

        return PokemonResponse.builder()
                .pokemonId(p.getPokemonId())
                .name(p.getName())
                .url(p.getUrl())
                .type(p.getType())
                .weight(p.getWeight())
                .abilities(abilitiesList)
                .movements(movementsList)
                .build();
    }
}
