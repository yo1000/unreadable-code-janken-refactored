package com.yo1000;

import java.util.Random;

public enum HandTypes {
    UNDECIDED(0, ""),
    ROCK(1, "1=Rock"),
    PAPER(2, "2=Paper"),
    SCISSORS(3, "3=Scissors"),
    ;

    private static final Random random = new Random();

    private final int value;
    private final String text;

    HandTypes(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public boolean isDrawn(HandTypes other) {
        return this == other;
    }

    public boolean isWon(HandTypes other) {
        return (value - other.getValue() + 3) % 3 == 1;
    }

    public static HandTypes of(String value) {
        return of(Integer.parseInt(value));
    }

    public static HandTypes of(int value) {
        return switch (value) {
            case 1 -> ROCK;
            case 2 -> PAPER;
            case 3 -> SCISSORS;
            default -> UNDECIDED;
        };
    }

    public static HandTypes random() {
        return of(random.nextInt(3) + 1);
    }
}
