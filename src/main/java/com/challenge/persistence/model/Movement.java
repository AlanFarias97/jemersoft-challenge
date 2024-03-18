package com.challenge.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MOVEMENT")
public class Movement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NM_MOVEMENT_ID", nullable = false)
    private Long movementId;

    @Column(name = "VA_NAME", length = 250, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "pokemonMovements")
    private Set<Pokemon> pokemon;

}
