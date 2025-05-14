package com.BoardGameFinder.BoardGameFinder.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstTime {
    LESS_THAN_15("Less than 15 minutes"),
    FROM_15_TO_30("15 to 30 minutes"),
    FROM_30_TO_45("30 to 45 minutes"),
    FROM_45_TO_60("45 to 60 minutes"),
    FROM_60_TO_90("60 to 90 minutes"),
    FROM_90_TO_120("90 to 120 minutes"),
    MORE_THAN_120("More than 2 hours");

    private final String label;

    EstTime(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static EstTime fromLabel(String label) {
        for (EstTime e : values()) {
            if (e.label.equalsIgnoreCase(label)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown EstTime: " + label);
    }
}
