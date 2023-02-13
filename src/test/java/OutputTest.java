
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OutputTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    private Set<String> guesses;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
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

    @Test // Requirement 4.1: If a letter in the guess is not in the target word, it is replaced with a ‘#’ in the output displayed to the user
    void testIncorrectOutput(){

       // Set the games target word
        gameBoard.setTarget("cyber");

        String guess = "smart";

        Scanner scanner = new Scanner(guess + "\n" + "start" );
        Commandle.playOneGame(System.out, 1, gameBoard,scanner );

        String result = getOutput();

        assertTrue(result.contains("1: smart  1: ###?#"));
//        System.setOut(systemOut);
//        System.out.println( "Full result: === " + result);
    }

    @Test // Requirement 4.2: If a letter in the guess is in the target word, but in a different position, it replaced with a ‘?’ in the output displayed to the user
    void testPartiallyCorrectOutput(){

        // Set the games target word
        gameBoard.setTarget("trial");

        String guess = "smart";

        Scanner scanner = new Scanner(guess + "\n" + "start" );
        Commandle.playOneGame(System.out, 1, gameBoard,scanner );

        String result = getOutput();

        assertTrue(result.contains("1: smart  1: ##???"));
    }

    @Test // Requirement 4.3: If a letter in the guess is in the target word and it’s positioning is correct, it remains in it’s original form in the output displayed to the user
    void testCorrectOutput(){

        // Set the games target word
        gameBoard.setTarget("smirk");

        String guess = "smart";

        Scanner scanner = new Scanner(guess + "\n" + "start" );
        Commandle.playOneGame(System.out, 1, gameBoard,scanner );

        String result = getOutput();

        assertTrue(result.contains("1: smart  1: sm#r#"));
    }

    @Test // Tests whether letters that only appear once in a target are indicated as such
    void testDuplicateGuessLetterOutput(){

        // Set the games target word
        gameBoard.setTarget("joint");

        String guess = "shoot";

        Scanner scanner = new Scanner(guess + "\n" + "start" );
        Commandle.playOneGame(System.out, 1, gameBoard,scanner );

        String result = getOutput();

        assertTrue(result.contains("1: shoot  1: ##?#t"));
        // Fail actual output is 1: shoot  1: ##??t
    }

    @Test // Tests whether letters that appear twice in a target are indicated as such
    void testDuplicateTargetLetterOutput(){

        // Set the games target word
        gameBoard.setTarget("sheet");

        String guess = "scene";

        Scanner scanner = new Scanner(guess + "\n" + "start" );
        Commandle.playOneGame(System.out, 1, gameBoard,scanner );

        String result = getOutput();

        assertTrue(result.contains("1: scene  1: s#e#?"));
    }
}
