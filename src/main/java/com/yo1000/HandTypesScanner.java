package com.yo1000;

import java.io.InputStream;
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
        return HandTypes.of(s);
    }
}
