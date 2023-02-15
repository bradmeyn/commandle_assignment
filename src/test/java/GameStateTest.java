import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

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




    @Test // Requirement 5:  For each game, the player can have a maximum of 6 guesses
    void testMaxGuessDefault() {

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


    @Test // Requirement 6: A game is won when the player correctly guesses the word within the maximum number of allowed guesses (e.g. 6), otherwise, the game is lost.
    void testMaxGuessExceeded() {

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

    @Test // Requirement 6: A game is won when the player correctly guesses the word within the maximum number of allowed guesses (e.g. 6), otherwise, the game is lost.
    void testSuccessfulGuess() {

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

    @Test // Requirement 7: The same word cannot be guessed more than once in a game.
    void testRepeatedGuess() {
        gameBoard.setTarget("guess");
        System.setErr(new PrintStream(testOut));
        HashSet guesses = new HashSet<>();

        // Add 'smart' to list of guesses
        guesses.add("smart");

        // Guess with number 8 in it should not be treated as a valid guess
        String repeatedGuess = "smart";
        Scanner scanner = new Scanner(repeatedGuess + "\n" + "start" );
        String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);
        String errorMessage = testOut.toString();

        // Test passes if error message is created warning about duplicate guess and valid guess returned from method is second guess input
        assertFalse(validGuess.equals(repeatedGuess));
        assertTrue(validGuess.equals("start"));
        assertTrue(errorMessage.contains("You have already used that word this game. Please enter a new word."));
    }
}
