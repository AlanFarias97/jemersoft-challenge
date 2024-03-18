package com.challenge.persistence.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "POKEMON")
public class Pokemon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NM_POKEMON_ID", length = 25, nullable = false)
    private Long pokemonId;

    @Column(name = "VA_NAME", length = 250, nullable = false)
    private String name;

    @Column(name = "VA_URL", length = 1250, nullable = true)
    private String url;

    @Column(name = "VA_TYPE", length = 250, nullable = true, columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private List<String> type;

    @Column(name = "VA_DESCRIPTION", length = 650, nullable = true)
    private String description;

    @Column(name = "NM_WEIGHT", nullable = true)
    private int weight;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "POKEMON_ABILITY",
            joinColumns = @JoinColumn(name = "NM_POKEMON_ID"),
            inverseJoinColumns = @JoinColumn(name = "NM_ABILITY_ID"),
            foreignKey = @ForeignKey(name = "fk_Pokemon_Ability")
    )
    private Set<Ability> pokemonAbilities = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "POKEMON_MOVEMENT",
            joinColumns = @JoinColumn(name = "NM_POKEMON_ID"),
            inverseJoinColumns = @JoinColumn(name = "NM_MOVEMENT_ID"),
            foreignKey = @ForeignKey(name = "fk_Pokemon_Movement")
    )
    private Set<Movement> pokemonMovements = new HashSet<>();

}
