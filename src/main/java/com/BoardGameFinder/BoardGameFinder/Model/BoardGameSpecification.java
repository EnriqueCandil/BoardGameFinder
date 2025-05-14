package com.BoardGameFinder.BoardGameFinder.Model;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class BoardGameSpecification {

	public static Specification<BoardGame> hasName(String name){
		return (root, query, cb) ->
			name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}
	
	 public static Specification<BoardGame> hasNumPlayers(NumPlayers numPlayers) {
	     return (root, query, cb) ->
            numPlayers == null ? null : cb.equal(root.get("numPlayers"), numPlayers);
	}

    public static Specification<BoardGame> hasEstTime(EstTime estTime) {
        return (root, query, cb) ->
            estTime == null ? null : cb.equal(root.get("estTime"), estTime);
    }

    public static Specification<BoardGame> hasEditorial(Editorial editorial) {
        return (root, query, cb) ->
            editorial == null ? null : cb.equal(root.get("editorial"),editorial);
    }

    public static Specification<BoardGame> hasAge(Age age) {
        return (root, query, cb) ->
            age == null ? null : cb.equal(root.get("age"), age);
    }
    
    public static Specification<BoardGame> hasAllCategories(List<Category> categories){
    	return (root,query,cb) -> {
    		
			if(categories == null || categories.isEmpty()) return null;
			
			 // Realizamos un JOIN con la relación de categorías
			Join<BoardGame, Category> join = root.join("categories");
			
			// Agrupamos por el id del juego de mesa
			query.groupBy(root.get("idBoardGame"));
			
			// Verificamos que el número de categorías del juego coincida con el tamaño de la lista de categorías que se pasa
			query.having(cb.equal(cb.countDistinct(join), categories.size()));
			
	        // Filtramos los juegos de mesa que contienen las categorías proporcionadas
			return join.in(categories);
    		
    	};
    }
}
