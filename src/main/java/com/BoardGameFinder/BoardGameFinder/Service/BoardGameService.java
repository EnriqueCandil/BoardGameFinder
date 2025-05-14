package com.BoardGameFinder.BoardGameFinder.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.BoardGameFinder.BoardGameFinder.Exception.BadRequestException;
import com.BoardGameFinder.BoardGameFinder.Model.Age;
import com.BoardGameFinder.BoardGameFinder.Model.BoardGame;
import com.BoardGameFinder.BoardGameFinder.Model.BoardGameSpecification;
import com.BoardGameFinder.BoardGameFinder.Model.Category;
import com.BoardGameFinder.BoardGameFinder.Model.Editorial;
import com.BoardGameFinder.BoardGameFinder.Model.EstTime;
import com.BoardGameFinder.BoardGameFinder.Model.NumPlayers;
import com.BoardGameFinder.BoardGameFinder.Model.User;
import com.BoardGameFinder.BoardGameFinder.Repository.BoardGameRepository;
import com.BoardGameFinder.BoardGameFinder.Repository.UserRepository;

@Service
public class BoardGameService {

	@Autowired
	private BoardGameRepository boardGameRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// Crea un nuevo juego de mesa
	public BoardGame saveBoardGame(BoardGame boardGame) {
		
		if(boardGame.getIdBoardGame() == null) {
			return boardGameRepository.save(boardGame);
		}else {
			throw new BadRequestException("Board game already exists");
		}
	}
	
	// Busca todos los juegos de mesa
	public List<BoardGame> getAllBoardGames() {
		
		if(boardGameRepository.findAll().isEmpty()) {
			throw new BadRequestException("Boad game list is empty");
		}
		return boardGameRepository.findAll();
	}
	
	// Busca un juego de mesa por su identificador
	public BoardGame getBoardGameById(Long id){
		
		return boardGameRepository.findById(id).orElseThrow(() -> new BadRequestException("Board game does not exist"));
	}
	
	// Elimina un juego de mesa por su identificador
	public void deleteBoardGame(Long id) {
		if(!boardGameRepository.existsById(id)) {
			throw new BadRequestException("Board game does not exist");
		}
		BoardGame boardGame = boardGameRepository.findById(id).get();
		List<User> users = boardGame.getUsers();
		for(User user:users) {
			user.getFavouriteBoardGames().remove(boardGame);
		}
		userRepository.saveAll(users);
		boardGameRepository.deleteById(id);
	}
	
	public BoardGame updateBoardGame(BoardGame boardGame) {
		
		if(boardGameRepository.existsById(boardGame.getIdBoardGame())) {
			
			BoardGame currentBoardGame = boardGameRepository.findById(boardGame.getIdBoardGame()).get();
			
			
			// Comprobamos que los campos que nos llegan no estan vacios, no quiero actualizar datos vacios o nulos a la base de datos
			// La unica excepcion es la url de la imagen
			
			if(boardGame.getAge() != null) {
				currentBoardGame.setAge(boardGame.getAge());
			}
			
			if(boardGame.getEstTime() != null) {
				currentBoardGame.setEstTime(boardGame.getEstTime());
			}
			
			if(boardGame.getDescription() != null && !boardGame.getDescription().isBlank()) {
				currentBoardGame.setDescription(boardGame.getDescription());
			}
			
			if(boardGame.getEditorial() != null) {
				currentBoardGame.setEditorial(boardGame.getEditorial());
			}
			
			if(boardGame.getName() != null && !boardGame.getName().isBlank()) {
				currentBoardGame.setName(boardGame.getName());
			}
			
			if(boardGame.getNumPlayers() != null) {
				currentBoardGame.setNumPlayers(boardGame.getNumPlayers());
			}
			
			if(!boardGame.getCategories().isEmpty()) {
				currentBoardGame.setCategories(boardGame.getCategories());
			}
			return boardGameRepository.save(currentBoardGame);
		}else {
			throw new BadRequestException("Board game does not exist");
		}
	}
	
	
	public List<BoardGame> searchBoardGames(
			String name,
	        NumPlayers numPlayers,
	        EstTime estTime,
	        Editorial editorial,
	        Age age,
	        List<Category> categories
			){
		
		// Para poder hacer una vista compleja en nuestra base de datos uso Specification, que me permite usar variables nulas
		// Ignora los campos nulos de nuestra query automaticamente lo que hace que sea una solucion muy limpia
		
		Specification<BoardGame> spec = Specification
				.where(BoardGameSpecification.hasName(name))
				.and(BoardGameSpecification.hasNumPlayers(numPlayers))
				.and(BoardGameSpecification.hasEstTime(estTime))
				.and(BoardGameSpecification.hasEditorial(editorial))
				.and(BoardGameSpecification.hasAge(age))
				.and(BoardGameSpecification.hasAllCategories(categories));
		
		return boardGameRepository.findAll(spec);
	}
}
