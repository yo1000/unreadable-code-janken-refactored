package com.yo1000;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Janken {
    private final InputStream in;
    private final PrintStream out;

    public Janken(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public void play() {
        HandTypes yourHand = HandTypes.UNDECIDED;
        HandTypes comHand = HandTypes.UNDECIDED;

        HandTypesScanner scanner = new HandTypesScanner(in);

        while (yourHand.isDrawn(comHand)) {
            out.print(Arrays.stream(HandTypes.values())
                    .filter(handTypes -> handTypes != HandTypes.UNDECIDED)
                    .map(HandTypes::getText)
                    .collect(Collectors.joining(", ")));

            out.print("... ");

            out.print("[" + Arrays.stream(HandTypes.values())
                    .filter(handTypes -> handTypes != HandTypes.UNDECIDED)
                    .map(HandTypes::getValue)
                    .map(String::valueOf)
                    .collect(Collectors.joining("/"))
                    + "]: ");

            yourHand = scanner.nextHandTypesUntilValid();
            out.println("Chose by You: " + yourHand.getText());

            comHand = HandTypes.random();
            out.println("Chose by COM: " + comHand.getText());

            out.println();
        }

        if (yourHand.isWon(comHand)) {
            out.println("You win!");
        } else {
            out.println("You lose.");
        }
    }
}
