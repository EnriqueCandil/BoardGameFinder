package com.BoardGameFinder.BoardGameFinder.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {

	ADVENTURE("Adventure"),
	CARRDS("Cards"),
	DICE("Dice"),
	EDUCATIONAL("Educational"),
	EXPLORATION("Exploration"),
	FANTASY("Fantasy"),
	MEMORY("Memory"),
	CASUAL("Casual"),
	RACING("Racing"),
	TRIVIA("Trivia"),
	DEDUCCION("Deduccion"),
	ECONOMY("Economy"),
	STRATEGY("Strategy"),
	HUMOR("Humor"),
	PUZZLE("Puzzle"),
	ZOMBIE("Zombie");
	
	private final String label;
	
	Category(String label){
		this.label = label;
	}
	
	@JsonValue
	public String getLabel() {
		return label;
	}
	
	@JsonCreator
	public static Category fromLabel(String label) {
		for(Category e: values()) {
			if(e.label.equalsIgnoreCase(label)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Unknown category" + label);
	}
}
