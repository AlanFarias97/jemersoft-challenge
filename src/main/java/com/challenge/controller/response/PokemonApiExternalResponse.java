package com.challenge.controller.response;

import com.challenge.persistence.model.Ability;
import com.challenge.persistence.model.Movement;
import com.challenge.persistence.model.Pokemon;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PokemonApiExternalResponse {
    private List<Pokemon> results;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PokemonExternalDTO{
    private List<PokemonApiExternalResponse.AbilityDTO> abilities;
    private List<PokemonApiExternalResponse.MovementDTO> moves;
    private Sprites sprites;
    private List<TypeListDTO> types;
    private int weight;
        public static Movement convertTo(MovementDTO p) {
            return Movement.builder()
                    .name(p.getMove().getName())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MovementDTO{

        private Movement move;


    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PokemonExternalURL{

        private List<Description> flavor_text_entries;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Description{
            private String flavor_text;
            private Language language;

            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public static class Language{
                private String name;
            }
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AbilityDTO{
        private Ability ability;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeListDTO{
        private Type type;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Type{
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sprites{
        private Other other;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Other{
        private Home home;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Home{
        private String front_default;
    }
}
