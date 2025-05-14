package com.BoardGameFinder.BoardGameFinder.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NumPlayers {

    SOLO("1 player"),
    ONE_TO_FOUR("1 to 4 players"),
    TWO_PLAYERS("2 players"),
    TWO_TO_FOUR("2 to 4 players"),
    THREE_PLAYERS("3 players"),
    THREE_OR_MORE("3 or more players"),
    FOUR_PLAYERS("4 players"),
    FOUR_OR_MORE("4 or more players"),
    FIVE_PLAYERS("5 players"),
    FIVE_OR_MORE("5 or more players"),
    SIX_OR_MORE("6 or more players");

    private final String label;

    NumPlayers(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
    
    @JsonCreator
    public static NumPlayers fromLabel(String label) {
    	for(NumPlayers e: values()) {
    		if(e.label.equalsIgnoreCase(label)) {
    			return e;
    		}
    	}
    	throw new IllegalArgumentException("Unknown number of players: " + label);
    }
}
