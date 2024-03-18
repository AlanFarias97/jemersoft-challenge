package com.challenge.controller.response;

import com.challenge.persistence.model.Ability;
import com.challenge.persistence.model.Pokemon;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbilityApiExternalResponse {
    private List<Ability> results;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AbilityExternalDTO{
        private List<PokemonExternalDTO> pokemon;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class PokemonExternalDTO{
            private PokemonDTO pokemon;
        }
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class PokemonDTO{
            private String name;
        }
    }
}
