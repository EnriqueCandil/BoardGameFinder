package com.BoardGameFinder.BoardGameFinder.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BoardGame")
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_board_game")
    private Long idBoardGame;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "num_players", length = 50)
    private NumPlayers numPlayers;

    @Enumerated(EnumType.STRING)
    @Column(name = "estimated_time", length = 50)
    private EstTime estTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Editorial editorial;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Age age;

    @Column(length = 500)
    private String description;

    @Column(name = "image_url", length = 255, nullable = true)
    private String imageUrl;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "boardgame_categories", joinColumns = @JoinColumn(name = "boardgame_id"))
    @Column(name = "category")
    private List<Category> categories;
	
    
    // Se define la relacion N-N con usuario, usando "mappedBy" para indicar que no es unidireccional y la relacion la maneja User
    
    @JsonIgnore
    @ManyToMany(mappedBy = "favouriteBoardGames")
    private List<User> users;
}