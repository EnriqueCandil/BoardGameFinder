package com.BoardGameFinder.BoardGameFinder.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Editorial {
    DEVIR("Devir"),
    EDGE("Edge Entertainment"),
    MALDITO_GAMES("Maldito Games"),
    TRANJIS("Tranjis Games"),
    ASMODEE("Asmodee"),
    GDM("GDM Games"),
    ZACATRUS("Zacatrus"),
    LUDONOVA("Ludonova"),
    MERCURIO("Mercurio"),
    GEN_X("Gen X Games");

    private final String label;

    Editorial(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Editorial fromLabel(String label) {
        for (Editorial e : values()) {
            if (e.label.equalsIgnoreCase(label)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown Editorial: " + label);
    }
}
