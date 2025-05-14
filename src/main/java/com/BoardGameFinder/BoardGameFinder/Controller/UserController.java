package com.BoardGameFinder.BoardGameFinder.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BoardGameFinder.BoardGameFinder.DTO.UserDTO;
import com.BoardGameFinder.BoardGameFinder.Model.User;
import com.BoardGameFinder.BoardGameFinder.Service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// Registrar a un usuario
	@PostMapping("/register")
	public ResponseEntity<User> createUser(@RequestBody User user){
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userService.saveUser(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	// Obtener datos del usuario actual
	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
		User user = userService.getByEmail(userDetails.getUsername());
		return ResponseEntity.ok(new UserDTO(user));
	}
	
	// Nos aseguramos que esta funcion actualice los datos del current user
	@PutMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody User user, @AuthenticationPrincipal UserDetails userDetails){
		User currentUser = userService.getByEmail(userDetails.getUsername());
		user.setIdUser(currentUser.getIdUser());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userService.updateUser(user);
		return ResponseEntity.ok(new UserDTO(user));
	}
	
	// Elimina el usuario actual
	@DeleteMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> deleteCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
		User currentUser = userService.getByEmail(userDetails.getUsername());
		userService.deleteUser(currentUser.getIdUser());
		return ResponseEntity.ok("User deleted");
	}
	
	@PostMapping("/me/fav/{gameId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserDTO> addFavouriteBoardGame(@PathVariable Long gameId, @AuthenticationPrincipal UserDetails userDetails){
		User currentUser = userService.getByEmail(userDetails.getUsername());
		User user = userService.updateFavouriteBoardGames(currentUser.getIdUser(), gameId);
		return ResponseEntity.ok(new UserDTO(user));
	}
	
	@DeleteMapping("/me/fav/{gameId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> deleteFavouriteBoardGame(@PathVariable Long gameId, @AuthenticationPrincipal UserDetails userDetails){
		User currentUser = userService.getByEmail(userDetails.getUsername());
		String message = userService.deleteFavouriteBoardGame(currentUser.getIdUser(), gameId);
		return ResponseEntity.ok(message);
	}
	
	// Eliminar un usuario, funcion solo para administradores
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUser (@PathVariable Long id){
		userService.deleteUser(id);
		return ResponseEntity.ok("User deleted");
	}
	
	// Encuentra un usuario por id, solo para admins
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
		User user = userService.getUserById(id);
		return ResponseEntity.ok(new UserDTO(user));
	}
	
	// Encuentra a todos los usuarios, solo para admins
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		List<User> users = userService.getAllUsers();
		List<UserDTO> usersDTO = new ArrayList<>();
		for (User user : users) {
			usersDTO.add(new UserDTO(user));
		}
		return ResponseEntity.ok(usersDTO);
	}
}