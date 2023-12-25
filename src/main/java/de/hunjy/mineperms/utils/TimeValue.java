package de.hunjy.mineperms.utils;

import java.util.Date;

public enum TimeValue {
    YEAR("Y", 31556952000L),
    MONTH("M", 2629746000L),
    WEEK("w", 604800000L),
    DAY("d", 86400000L),
    HOUR("h", 3600000L),
    MINUTE("m", 60000L),
    SECONDS("s", 1000L);

    private final String definitionChar;
    private final Long multiplicator;

    TimeValue(String definitionChar, Long multiplicator) {
        this.definitionChar = definitionChar;
        this.multiplicator = multiplicator;
    }

    public String getDefinitionChar() {
        return definitionChar;
    }

    public Long getMultiplicator() {
        return multiplicator;
    }

    public static TimeValue getTimeValue(String definitionChar) {
        for(TimeValue timeValues : values()) {
            if(timeValues.getDefinitionChar().equals(definitionChar)) {
                return timeValues;
            }
        }
        return null;
    }

    public static Date calcDate(long millis) {
        return new Date(millis);
    }
}
