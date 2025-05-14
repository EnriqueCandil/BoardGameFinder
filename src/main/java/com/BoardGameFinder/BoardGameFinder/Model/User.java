package com.BoardGameFinder.BoardGameFinder.Model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private Long idUser;
	
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false, length = 200)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role" , nullable = false)
	private Role role;
	
	// Se crea una relacion N-N con BoardGame, ya que cada usuario va a tener una lista de juegos favoritos
	// Especificamos que el tipo de relacion es EAGER ya que vamos a querer tener la lista de juegos en el front-end
	// Si la app escala mucho tendria que ser una relacion LAZY y usar una query en el repositorio para conseguir la lista solo cuando sea necesario
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		      name = "user_favorite_boardgames", 
		      joinColumns = @JoinColumn(name = "user_id"), 
		      inverseJoinColumns = @JoinColumn(name = "board_game_id")
		    )
	private List<BoardGame> favouriteBoardGames;
}