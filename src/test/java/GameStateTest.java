import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    private GameBoard gameBoard;

    private Commandle commandle;

    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        commandle = new Commandle();
        gameBoard = new GameBoard(List.of( "start", "guess", "ghost", "cyber", "smart", "smirk", "joint", "shoot", "scene", "sheet"));
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




    @Test // Tests that the main game loop uses 6 turns by default
    void TestMaxGuessDefault() {

        // Set the games target word
        gameBoard.setTarget("smart");

        // Provide 6 incorrect guesses & correct guess on 7th attempt
        Scanner scanner = new Scanner("scene\n" + "start\n" + "guess\n" +  "ghost\n" + "cyber\n" + "shoot\n" + "smart\n" );

        // Use commandles default 'MAX_TRIES' value for the round count
        Boolean success =  commandle.playOneGame(System.out, commandle.MAX_TRIES, gameBoard,scanner );

        String result = getOutput();

        // Guess outputs
        assertTrue(result.contains("1: scene"));
        assertTrue(result.contains("2: start"));
        assertTrue(result.contains("3: guess"));
        assertTrue(result.contains("4: ghost"));
        assertTrue(result.contains("5: cyber"));
        assertTrue(result.contains("6: shoot"));

        // 7th guess should not register
        assertFalse(result.contains("7: smart"));

        // If round count is exceeded, entering correct guess after should not register
        assertFalse(success);
    }


    @Test // Tests that the main game loop ends once the max amount of valid guesses occurs
    void TestMaxGuessExceeded() {

        // Set the games target word
        gameBoard.setTarget("sheet");

        // Provide 6 incorrect guesses
        Scanner scanner = new Scanner("scene\n" + "start\n" + "guess\n" +  "ghost\n" + "cyber\n" + "shoot\n" );
        Boolean success =  commandle.playOneGame(System.out, commandle.MAX_TRIES, gameBoard,scanner );

        String result = getOutput();

        // Guess outputs
        assertTrue(result.contains("1: scene"));
        assertTrue(result.contains("2: start"));
        assertTrue(result.contains("3: guess"));
        assertTrue(result.contains("4: ghost"));
        assertTrue(result.contains("5: cyber"));
        assertTrue(result.contains("6: shoot"));

        // If round count is exceeded & target is not guesses, false is returned from playOneGame method
        assertFalse(success);
//        System.setOut(systemOut);
//        System.out.println(success);
//        System.out.println( result);
    }

    @Test
    void TestSuccessfulGuess() {

        // Set the games target word
        gameBoard.setTarget("guess");

        // Provide 2 incorrect guesses & correct guess on 3rd turn
        Scanner scanner = new Scanner("scene\n" + "start\n" + "guess\n" +  "ghost\n" + "cyber\n" + "shoot\n" );
        Boolean success =  commandle.playOneGame(System.out, commandle.MAX_TRIES, gameBoard,scanner );

        String result = getOutput();

        // Guess outputs
        assertTrue(result.contains("1: scene"));
        assertTrue(result.contains("2: start"));
        assertTrue(result.contains("3: guess"));

        // These should not be displayed
        assertFalse(result.contains("4: ghost"));
        assertFalse(result.contains("5: cyber"));
        assertFalse(result.contains("6: shoot"));

        // If correct word is guessed, main gameplay loop ends & true value is returned from playOneGame
        assertTrue(success);
//        System.setOut(systemOut);
//        System.out.println(success);
//        System.out.println( result);
    }
}
