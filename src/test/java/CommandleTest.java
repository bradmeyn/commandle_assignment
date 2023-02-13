
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CommandleTest {
    // See https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println and
    // https://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing
    // for more information on how to test with system input & output
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private String getOutput() {
        return testOut.toString();
    }

    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }

    @Test
    void gameCanBeWon() throws IOException {
        // set up a one-word list for easy testing
        String target = "brave";
        wordList.add(target);

        // provide the correct guess, and then followed by "N" to signal not wanting to play again
        provideInput(target + "\nN");

        // simulate the gameplay start
        Commandle.start(System.in, System.out, wordList);

        // get output
        String result = getOutput();

        // verify that the output contains the word "won"
        assertTrue(result.contains("won"), "You won");
    }



}