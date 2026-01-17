package com.yo1000;

import java.io.InputStream;
import java.io.PrintStream;

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
            out.print("1=Rock, 2=Paper, 3=Scissors... [1/2/3]: ");

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
