package com.yo1000;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class JankenTests {
    @Test
    void play_acceptsInputAndPrintsResult() {
        // [0]=Rock-win     | [1]=Rock-lose     | [2]=Rock-draw
        // [3]=Paper-win    | [4]=Paper-lose    | [5]=Paper-draw
        // [6]=Scissors-win | [7]=Scissors-lose | [8]=Scissors-draw
        // [9]=Jackal-win   | [a]=Jackal-lose   | [b]=Jackal=draw
        int[] winLoseFlags = new int[(HandTypes.values().length - 1) * 3];
        Arrays.fill(winLoseFlags, 0);

        int hand = 1; // 1=Rock, 2=Paper, 3=Scissors, 4=Jackal

        // `i < 1000` is a safety check to prevent infinite loops caused by test-side bugs
        for (int i = 0; i < 1000 && Arrays.stream(winLoseFlags).anyMatch(value -> value != 1); i++) {
            if (i > 0) System.out.println("----------------------------------------");
            System.out.println("Round=" + (i + 1) + ", Hand=" + hand);

            String input = (hand + "\n").repeat(1000);

            InputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(outBytes, true, StandardCharsets.UTF_8);

            Janken sut = new Janken(in, out);
            Assertions.assertDoesNotThrow(sut::play);

            String output = outBytes.toString(StandardCharsets.UTF_8);

            Assertions.assertTrue(output.contains("1=Rock, 2=Paper, 3=Scissors, 4=Jackal... [1/2/3/4]:"));
            Assertions.assertTrue(output.contains("Chose by You: " + hand));
            Assertions.assertTrue(output.contains("Chose by COM:"));
            Assertions.assertTrue(output.contains("You win!") || output.contains("You lose."));

            Pattern patternYou = Pattern.compile("Chose by You:\\s*(" + hand + "=.+)", Pattern.MULTILINE);
            Pattern patternCOM = Pattern.compile("Chose by COM:\\s*([1-" + (HandTypes.values().length - 1) + "]=.+)", Pattern.MULTILINE);

            Matcher matcherYou = patternYou.matcher(output);
            Matcher matcherCOM = patternCOM.matcher(output);

            boolean foundYou = matcherYou.find();
            boolean foundCOM = matcherCOM.find();

            Assertions.assertTrue(foundYou);
            Assertions.assertTrue(foundCOM);

            String choseByYou = matcherYou.group(1);
            String choseByCOM = matcherCOM.group(1);

            int drawCount = 0;

            // Continue the loop until it is no longer a draw
            while (choseByYou.equals(choseByCOM)) {
                // Assert draw
                Assertions.assertEquals(choseByYou, choseByCOM);
                drawCount++;

                foundYou = matcherYou.find();
                foundCOM = matcherCOM.find();

                Assertions.assertEquals(foundYou, foundCOM);

                if (foundYou && foundCOM) {
                    choseByYou = matcherYou.group(1);
                    choseByCOM = matcherCOM.group(1);
                } else {
                    break;
                }
            }

            Assertions.assertEquals(foundYou, foundCOM);

            // Assert not draw (It must be decided by win or loss)
            Assertions.assertNotEquals(choseByYou, choseByCOM);

            final String chosenByYou = choseByYou;
            final String chosenByCOM = choseByCOM;

            Assertions.assertTrue(Arrays.stream(HandTypes.values())
                    .filter(handTypes -> handTypes != HandTypes.UNDECIDED)
                    .map(HandTypes::getText)
                    .anyMatch(chosenByYou::equals));

            Assertions.assertTrue(Arrays.stream(HandTypes.values())
                    .filter(handTypes -> handTypes != HandTypes.UNDECIDED)
                    .map(HandTypes::getText)
                    .anyMatch(chosenByCOM::equals));

            // Win or lose is displayed only once at the end.
            Pattern patternResult = Pattern.compile("(You win!|You lose\\.)", Pattern.MULTILINE);
            Matcher matcherResult = patternResult.matcher(output);
            Assertions.assertTrue(matcherResult.find());
            String resultMessage = matcherResult.group(1);
            Assertions.assertFalse(matcherResult.find());

            final int DRAW = 0;
            final int WIN = 1;
            final int LOSE = 2;

            int result = -1;

            switch (choseByYou) {
                case "1=Rock" -> {
                    switch (choseByCOM) {
                        case "1=Rock" -> {
                            result = DRAW;
                        }
                        case "2=Paper" -> {
                            result = LOSE;
                            Assertions.assertEquals("You lose.", resultMessage);
                        }
                        case "3=Scissors" -> {
                            result = WIN;
                            Assertions.assertEquals("You win!", resultMessage);
                        }
                        case "4=Jackal" -> {
                            result = WIN;
                            Assertions.assertEquals("You win!", resultMessage);
                        }
                        default -> Assertions.fail();
                    }
                }
                case "2=Paper" -> {
                    switch (choseByCOM) {
                        case "1=Rock" -> {
                            result = WIN;
                            Assertions.assertEquals("You win!", resultMessage);
                        }
                        case "2=Paper" -> {
                            result = DRAW;
                        }
                        case "3=Scissors" -> {
                            result = LOSE;
                            Assertions.assertEquals("You lose.", resultMessage);
                        }
                        case "4=Jackal" -> {
                            result = LOSE;
                            Assertions.assertEquals("You lose.", resultMessage);
                        }
                        default -> Assertions.fail();
                    }
                }
                case "3=Scissors" -> {
                    switch (choseByCOM) {
                        case "1=Rock" -> {
                            result = LOSE;
                            Assertions.assertEquals("You lose.", resultMessage);
                        }
                        case "2=Paper" -> {
                            result = WIN;
                            Assertions.assertEquals("You win!", resultMessage);
                        }
                        case "3=Scissors" -> {
                            result = DRAW;
                        }
                        case "4=Jackal" -> {
                            result = LOSE;
                            Assertions.assertEquals("You lose.", resultMessage);
                        }
                        default -> Assertions.fail();
                    }
                }
                case "4=Jackal" -> {
                    switch (choseByCOM) {
                        case "1=Rock" -> {
                            result = LOSE;
                            Assertions.assertEquals("You lose.", resultMessage);
                        }
                        case "2=Paper" -> {
                            result = WIN;
                            Assertions.assertEquals("You win!", resultMessage);
                        }
                        case "3=Scissors" -> {
                            result = WIN;
                            Assertions.assertEquals("You win!", resultMessage);
                        }
                        case "4=Jackal" -> {
                            result = DRAW;
                        }
                        default -> Assertions.fail();
                    }
                }
                default -> Assertions.fail();
            }

            if (drawCount > 0) {
                winLoseFlags[(hand - 1) * 3 + DRAW] = 1;
            }

            winLoseFlags[(hand - 1) * 3 + result] = 1;

            if (winLoseFlags[(hand - 1) * 3 + DRAW] == 1
                    && winLoseFlags[(hand - 1) * 3 + WIN] == 1
                    && winLoseFlags[(hand - 1) * 3 + LOSE] == 1) {
                hand++;
            }

            System.out.println("Draw=" + drawCount);
            System.out.println(choseByYou);
            System.out.println(choseByCOM);
            System.out.println("Flags=" + Arrays.stream(winLoseFlags).mapToObj(String::valueOf).collect(Collectors.joining()));
        }

        Assertions.assertTrue(Arrays.stream(winLoseFlags).allMatch(value -> value == 1));
    }
}
