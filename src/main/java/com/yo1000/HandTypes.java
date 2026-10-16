package com.yo1000;

import java.util.Arrays;
import java.util.Random;

public enum HandTypes {
    UNDECIDED(0, "") {
        @Override
        boolean isWon(HandTypes other) {
            throw new IllegalArgumentException("Not supported hand");
        }
    },
    ROCK(1, "1=Rock") {
        @Override
        boolean isWon(HandTypes other) {
            return switch (other) {
                case ROCK -> false;
                case PAPER -> false;
                case SCISSORS -> true;
                case JACKAL -> true;
                default -> throw new IllegalArgumentException("Not supported hand");
            };
        }
    },
    PAPER(2, "2=Paper") {
        @Override
        boolean isWon(HandTypes other) {
            return switch (other) {
                case ROCK -> true;
                case PAPER -> false;
                case SCISSORS -> false;
                case JACKAL -> false;
                default -> throw new IllegalArgumentException("Not supported hand");
            };
        }
    },
    SCISSORS(3, "3=Scissors") {
        @Override
        boolean isWon(HandTypes other) {
            return switch (other) {
                case ROCK -> false;
                case PAPER -> true;
                case SCISSORS -> false;
                case JACKAL -> false;
                default -> throw new IllegalArgumentException("Not supported hand");
            };
        }
    },
    JACKAL(4, "4=Jackal") {
        @Override
        boolean isWon(HandTypes other) {
            return switch (other) {
                case ROCK -> false;
                case PAPER -> true;
                case SCISSORS -> true;
                case JACKAL -> false;
                default -> throw new IllegalArgumentException("Not supported hand");
            };
        }
    },
    ;

    private static final Random random = new Random();

    private final int value;
    private final String text;

    HandTypes(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() { return value; }
    public String getText() {
        return text;
    }

    public boolean isDrawn(HandTypes other) {
        return this == other;
    }

    abstract boolean isWon(HandTypes other);

    public static HandTypes of(String value) {
        return of(Integer.parseInt(value));
    }

    public static HandTypes of(int value) {
        return Arrays.stream(values())
                .filter(handTypes -> handTypes.value == value)
                .findFirst()
                .orElse(UNDECIDED);
    }

    public static HandTypes random() {
        return of(random.nextInt(values().length - 1) + 1);
    }
}
