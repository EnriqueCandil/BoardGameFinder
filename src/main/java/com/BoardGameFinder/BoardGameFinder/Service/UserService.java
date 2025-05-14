package com.BoardGameFinder.BoardGameFinder.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BoardGameFinder.BoardGameFinder.Exception.BadRequestException;
import com.BoardGameFinder.BoardGameFinder.Model.BoardGame;
import com.BoardGameFinder.BoardGameFinder.Model.Role;
import com.BoardGameFinder.BoardGameFinder.Model.User;
import com.BoardGameFinder.BoardGameFinder.Repository.BoardGameRepository;
import com.BoardGameFinder.BoardGameFinder.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BoardGameRepository boardGameRepository;
	
	// Crea un nuevo usuario, validamos que el usuario no este en la base de datos y que el email no este repetido
	public User saveUser(User user) {
		
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new BadRequestException("Email already exists");
		}
		
		// Por defecto los usuarios creados son USER, ya que no se pueden crear Admins desde la pagina web
		user.setRole(Role.USER);
		
		return userRepository.save(user);
	}
	
	public User getByEmail(String email) {
		
		if(!userRepository.existsByEmail(email)) {
			throw new BadRequestException("Incorrect email");
		}
		
		return userRepository.findByEmail(email).get();
	}
	
	// Busca todos los usuarios
	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if(users.isEmpty()) {
			throw new BadRequestException("User list is empty");
		}
		return users;
	}
	
	// Busca un usuario por su identificador
	public User getUserById(Long id){
		return userRepository.findById(id).orElseThrow(() -> new BadRequestException("User does not exist"));
	}
	
	// Elimina un usuario por su identificador
	public void deleteUser(Long id) {
		
		if(!userRepository.existsById(id)) {
			throw new BadRequestException("Id not found");
		}
		userRepository.deleteById(id);
	}
	
	// Actualiza un usuario con validaciones, menos la lista de juegos de mesa favoritos
	public User updateUser(User user) {
		
		if(userRepository.existsById(user.getIdUser())) {
			User currentUser = userRepository.findById(user.getIdUser()).get();
			
			if(user.getName()!= null && !user.getName().isBlank()) {
				currentUser.setName(user.getName());
			}
			if(user.getEmail()!= null && !user.getEmail().isBlank()) {
				currentUser.setEmail(user.getEmail());
			}
			if(user.getPassword()!= null && !user.getPassword().isBlank()) {
				currentUser.setPassword(user.getPassword());
			}
			return userRepository.save(currentUser);
		}else {
			throw new BadRequestException("User does not exist");
		}
	}
	
	// Actualiza la lista de juegos de mesa favoritos de un usuario
	public User updateFavouriteBoardGames(Long idUser, Long idBoardGame) {
		
		BoardGame newFav = boardGameRepository.findById(idBoardGame).orElseThrow(() -> new BadRequestException("Board game not found"));
		User user = userRepository.findById(idUser).orElseThrow(() -> new BadRequestException("User does not exist"));
		
		if(user.getFavouriteBoardGames().stream().anyMatch(favGame -> favGame.getIdBoardGame().equals(idBoardGame))) {
			throw new BadRequestException("Board game already in the list");
		}
		user.getFavouriteBoardGames().add(newFav);
		return userRepository.save(user);
	}
	
	// Elimina un juego de mesa del usuario y actualiza la informacion
	public String deleteFavouriteBoardGame(Long idUser, Long idBoardGame) {
		
		BoardGame boardGame = boardGameRepository.findById(idBoardGame).orElseThrow(() -> new BadRequestException("Board game not found"));
		User user = userRepository.findById(idUser).orElseThrow(() -> new BadRequestException("User does not exist"));
		
		if(!user.getFavouriteBoardGames().stream().anyMatch(game -> game.getIdBoardGame().equals(idBoardGame))) {
			throw new BadRequestException("Board game is not  on the list");
		}
		
		user.getFavouriteBoardGames().remove(boardGame);
		
		userRepository.save(user);
		
		return "BoardGame eliminado";
	}
}
