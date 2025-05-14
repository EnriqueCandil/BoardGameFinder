package com.BoardGameFinder.BoardGameFinder.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BoardGameFinder.BoardGameFinder.Model.Age;
import com.BoardGameFinder.BoardGameFinder.Model.BoardGame;
import com.BoardGameFinder.BoardGameFinder.Model.Category;
import com.BoardGameFinder.BoardGameFinder.Model.Editorial;
import com.BoardGameFinder.BoardGameFinder.Model.EstTime;
import com.BoardGameFinder.BoardGameFinder.Model.NumPlayers;
import com.BoardGameFinder.BoardGameFinder.Service.BoardGameService;


@RestController
@RequestMapping("/api/boardgames")
public class BoardGameController {

	@Autowired
	private BoardGameService boardGameService;
	

	
	@GetMapping("/{id}")
	public ResponseEntity<BoardGame> getBoardGame(@PathVariable Long id){
		return ResponseEntity.ok(boardGameService.getBoardGameById(id));
	}
	
	@GetMapping("getall")
	public ResponseEntity<List<BoardGame>> getAllBoardGames(){
		return ResponseEntity.ok(boardGameService.getAllBoardGames());
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BoardGame> createBoardGame(@RequestBody BoardGame boardGame){
		
		System.out.println(boardGame.toString());
		boardGameService.saveBoardGame(boardGame);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(boardGame);
	}
	
	@DeleteMapping("delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteBoardGame(@PathVariable Long id){
		boardGameService.deleteBoardGame(id);
		return ResponseEntity.ok("Board game deleted");
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<BoardGame> updateBoardGame(@RequestBody BoardGame boardGame){
		boardGameService.updateBoardGame(boardGame);
		return ResponseEntity.ok(boardGame);
	}
	
	@GetMapping("/searchboardgames")
	public ResponseEntity<List<BoardGame>> searchBoardGames(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String numPlayers,
			@RequestParam(required = false) String estTime,
			@RequestParam(required = false) String editorial,
			@RequestParam(required = false) String age,
			@RequestParam(required = false) List<String> categories
			){
		
	    NumPlayers numPlayersEnum = numPlayers != null ? NumPlayers.fromLabel(numPlayers) : null;
	    EstTime estTimeEnum = estTime != null ? EstTime.fromLabel(estTime) : null;
	    Editorial editorialEnum = editorial != null ? Editorial.fromLabel(editorial) : null;
	    Age ageEnum = age != null ? Age.fromLabel(age) : null;
	    List<Category> categoryEnums = categories != null ? categories.stream()
	            .map(Category::fromLabel)
	            .collect(Collectors.toList()) : null;
		
		List<BoardGame> query = boardGameService.searchBoardGames(name, numPlayersEnum, estTimeEnum, editorialEnum, ageEnum, categoryEnums);
		return ResponseEntity.ok(query);
	}
}
