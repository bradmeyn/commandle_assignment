import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LetterPostionTest {

        private GameBoard gameBoard;
        private List<String> wordList;

        @BeforeEach
        void setUp() {
        }

        @AfterEach
        void tearDown() {
        }


        @Test // Requirement 3.1: The game should identify letters in the guess not in the target word
        void testWrongLetter() throws IOException {

            // Create a new GameBoard with a target word 'cyber'
            List<String> wordList = Arrays.asList("cyber");
            GameBoard gameBoard = new GameBoard(wordList);

            // Start a new game
            gameBoard.startGame();

            // Guess
            String guess = "smart";

            // Check the status of the guess
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toCharArray());

            // For the test to pass the letters should register as wrong, wrong, wrong, partial, wrong
            assertEquals(GameBoard.Status.wrong, result[0], "First letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.wrong, result[1], "Second letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.wrong, result[2], "Third letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.partial, result[3], "Fourth letter should be value of 'partial'");
            assertEquals(GameBoard.Status.wrong, result[4], "Fifth letter should be value of 'wrong'");
        }


        @Test // Requirement 3.2: The game should identify letters in guess that match those in the target but in a different position
        void testPartialCorrect() throws IOException {

            // create a new GameBoard with a target word 'cyber'
            List<String> wordList = Arrays.asList("tools");
            GameBoard gameBoard = new GameBoard(wordList);

            // start a new game
            gameBoard.startGame();

            // guess
            String guess = "smart";

            // check the status of the guess
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toCharArray());

            // For the test to pass the letters should register as partial, wrong, wrong, wrong, partial
            assertEquals(GameBoard.Status.partial, result[0], "First letter should be value of 'partial'");
            assertEquals(GameBoard.Status.wrong, result[1], "Second letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.wrong, result[2], "Third letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.wrong, result[3], "Fourth letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.partial, result[4], "Fifth letter should be value of 'partial'");
        }

        @Test // Requirement 3.3: The game should identify letters in the guess that correct in terms of letter and placement relative to the guess
        void testCorrect() throws IOException {

            // create a new GameBoard with a target word 'cyber'
            List<String> wordList = Arrays.asList("smirk");
            GameBoard gameBoard = new GameBoard(wordList);

            // start a new game
            gameBoard.startGame();

            // guess
            String guess = "smart";

            // check the status of the guess
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toCharArray());

            // For the test to pass the letters should register as correct, correct, wrong, correct, wrong
            assertEquals(GameBoard.Status.correct, result[0], "First letter should be value of 'correct'");
            assertEquals(GameBoard.Status.correct, result[1], "Second letter should be value of 'correct'");
            assertEquals(GameBoard.Status.wrong, result[2], "Third letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.correct, result[3], "Fourth letter should be value of 'correct'");
            assertEquals(GameBoard.Status.wrong, result[4], "Fifth letter should be value of 'wrong'");
        }

        @Test // Verify letters that only appear once in a target are indicated as such
        void testRepeatingLetterGuess() throws IOException {

            // create a new GameBoard with a target word 'cyber'
            List<String> wordList = Arrays.asList("joint");
            GameBoard gameBoard = new GameBoard(wordList);

            // start a new game
            gameBoard.startGame();

            // guess
            String guess = "shoot";

            // check the status of the guess
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toCharArray());

            // check that the output contains the expected results:
            assertEquals(GameBoard.Status.wrong, result[0], "First letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.wrong, result[1], "Second letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.partial, result[2], "Third letter should be value of 'partial'");
            assertEquals(GameBoard.Status.wrong, result[3], "Fourth letter should be value of 'wrong'");
            assertEquals(GameBoard.Status.correct, result[4], "Fifth letter should be value of 'correct'");
        }

        @Test // Verify letters that only appear once in a target are indicated as such
        void testRepeatingLetterTarget() throws IOException {

            // create a new GameBoard with a target word
            List<String> wordList = Arrays.asList("steve");
            GameBoard gameBoard = new GameBoard(wordList);

            // start a new game
            gameBoard.startGame();

            // guess
            String guess = "sleek";

            // check the status of the guess
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toCharArray());

            // check that the output contains the expected results
            assertEquals(GameBoard.Status.correct, result[0], "First letter 's' should be value of 'correct'");
            assertEquals(GameBoard.Status.wrong, result[1], "Second letter 'l' should be value of 'wrong'");
            assertEquals(GameBoard.Status.correct, result[2], "Third letter 'e' should be value of 'correct'");
            assertEquals(GameBoard.Status.partial, result[3], "Fourth letter 'e' should be value of 'partial'");
            assertEquals(GameBoard.Status.wrong, result[4], "Fifth letter 'k' should be value of 'wrong'");
        }

}
