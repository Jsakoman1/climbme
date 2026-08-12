package com.climbme.app.climbing;

import java.util.Arrays;

public enum FrenchGrade {
    GRADE_3A("3a"), GRADE_3B("3b"), GRADE_3C("3c"), GRADE_4A("4a"), GRADE_4B("4b"), GRADE_4C("4c"),
    GRADE_5A("5a"), GRADE_5B("5b"), GRADE_5C("5c"), GRADE_6A("6a"), GRADE_6A_PLUS("6a+"),
    GRADE_6B("6b"), GRADE_6B_PLUS("6b+"), GRADE_6C("6c"), GRADE_6C_PLUS("6c+"), GRADE_7A("7a"),
    GRADE_7A_PLUS("7a+"), GRADE_7B("7b"), GRADE_7B_PLUS("7b+"), GRADE_7C("7c"), GRADE_7C_PLUS("7c+"),
    GRADE_8A("8a"), GRADE_8A_PLUS("8a+"), GRADE_8B("8b"), GRADE_8B_PLUS("8b+"), GRADE_8C("8c"),
    GRADE_8C_PLUS("8c+"), GRADE_9A("9a"), GRADE_9A_PLUS("9a+"), GRADE_9B("9b"), GRADE_9B_PLUS("9b+"),
    GRADE_9C("9c");

    private final String label;

    FrenchGrade(String label) { this.label = label; }
    public String label() { return label; }
    public int rank() { return ordinal(); }

    public static FrenchGrade fromLabel(String value) {
        return Arrays.stream(values()).filter(grade -> grade.label.equalsIgnoreCase(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported French sport grade."));
    }
}
