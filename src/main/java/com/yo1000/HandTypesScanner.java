package com.yo1000;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class HandTypesScanner {
    private final Scanner scanner;

    public HandTypesScanner(InputStream source) {
        this.scanner = new Scanner(source);
    }

    public HandTypes nextHandTypesUntilValid() {
        HandTypes handTypes = HandTypes.UNDECIDED;
        while (handTypes == HandTypes.UNDECIDED) {
            handTypes = nextHandTypes();
        }
        return handTypes;
    }

    public HandTypes nextHandTypes() {
        String s = scanner.next();

        if (Arrays.stream(HandTypes.values())
                .filter(handTypes -> handTypes != HandTypes.UNDECIDED)
                .map(HandTypes::getValue)
                .map(String::valueOf)
                .toList().contains(s)) {
            return HandTypes.of(s);
        } else {
            return HandTypes.UNDECIDED;
        }
    }
}
