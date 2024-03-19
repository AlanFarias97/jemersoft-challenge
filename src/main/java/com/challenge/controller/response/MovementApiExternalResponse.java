package com.challenge.controller.response;

import com.challenge.persistence.model.Movement;
import com.challenge.persistence.model.Pokemon;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementApiExternalResponse {
    private List<Movement> results;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MovementExternalDTO{
        private List<Pokemon> learned_by_pokemon;
    }
}
