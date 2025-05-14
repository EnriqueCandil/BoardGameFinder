package com.BoardGameFinder.BoardGameFinder.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Age {
    AGE_3_PLUS("3+"),
    AGE_5_PLUS("5+"),
    AGE_7_PLUS("7+"),
    AGE_8_PLUS("8+"),
    AGE_10_PLUS("10+"),
    AGE_12_PLUS("12+"),
    AGE_14_PLUS("14+"),
    AGE_16_PLUS("16+"),
    AGE_18_PLUS("18+");

    private final String label;

    Age(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Age fromLabel(String label) {
        for (Age a : values()) {
            if (a.label.equalsIgnoreCase(label)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown Age: " + label);
    }
}
