package com.BoardGameFinder.BoardGameFinder.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BoardGameFinder.BoardGameFinder.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);

}