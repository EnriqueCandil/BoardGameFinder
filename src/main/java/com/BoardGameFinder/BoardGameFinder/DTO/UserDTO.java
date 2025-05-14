package com.BoardGameFinder.BoardGameFinder.DTO;

import java.util.List;

import com.BoardGameFinder.BoardGameFinder.Model.BoardGame;
import com.BoardGameFinder.BoardGameFinder.Model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long idUser;
	private String name;
	private String email;
	private List<BoardGame> favouriteBoardGames;
	
	public UserDTO(User user) {
		this.idUser = user.getIdUser();
		this.name = user.getName();
		this.email = user.getEmail();
		this.favouriteBoardGames = user.getFavouriteBoardGames();
	}
}
