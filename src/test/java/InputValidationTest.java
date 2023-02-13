import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InputValidationTest {

    /**
     * Requirements tested:
     *
     * The game only accepts valid, 5-letter words as input for each guess. The game needs to check for validity.
     *      - Guesses with numerical characters should be invalid
     *      - Guesses with special characters should be invalid
     *
     * Guesses that are 5 letters, but not allowed words must be invalid
     *
     * The game should only accept letters in the alphabet (regardless of their case), but not others.
     *      - Guesses with numerical characters should be invalid
     *      - Guesses with special characters should be invalid
     *
     * The same word cannot be guessed more than once in a game.

     */

        private final InputStream systemIn = System.in;
        private final PrintStream systemOut = System.out;
        private Set<String> guesses;
        private GameBoard gameBoard;
        private ByteArrayOutputStream testOut;

        private String errorMessage;

        @BeforeEach
        void setUp() {
            guesses = new HashSet<>();
            gameBoard = new GameBoard(List.of( "start", "$tart", "8tart", "smar", "smartt"));
            testOut = new ByteArrayOutputStream();
            System.setErr(new PrintStream(testOut));

        }

        @AfterEach
        void tearDown() {
            System.setIn(systemIn);
            System.setOut(systemOut);
            System.out.println(errorMessage);
        }



        @Test // Requirement 1.1: Test whether guesses less than 5 characters are invalid
        void testWordLengthTooShort() {

            // Guess with only 4 characters
            String shortGuess = "smar";

            // Input invalid guess, followed by valid guess
            Scanner scanner = new Scanner(shortGuess + "\n" + "start" );
            String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

            // Capture any generated error message
            errorMessage = testOut.toString();

            // Test passes if method produces error message, invalid guess is skipped &  valid guess is the 2nd input value ("start")
            assertTrue(validGuess.equals("start"));
            assertTrue(errorMessage.contains("Words less than 5 letters are invalid. Please enter a valid word: "));
        }

        @Test // Requirement 1.2: Test whether guesses greater than 5 characters are invalid
        void testWordLengthTooLong() {

            // Guess with 6 characters
            String longGuess = "smartt";

            // Input invalid guess, followed by valid guess
            Scanner scanner = new Scanner(longGuess + "\n" + "start" );
            String validGuess =  Commandle.getNextValidGuess(scanner, guesses, gameBoard);

            // Capture any generated error message
            errorMessage = testOut.toString();

            // Test passes if method produces error message, invalid guess is skipped &  valid guess is the 2nd input value ("start")
            assertTrue(validGuess.equals("start"));
            assertTrue(errorMessage.contains("Words greater than 5 letters are invalid. Please enter a valid word: "));
        }

        @Test // Requirement 2.1: Test whether blank guesses are invalid
        void testBlankInput() {

            // Blank guess
            Scanner scanner = new Scanner("\n" + "start" );
            String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

            // Capture any generated error message
            errorMessage = testOut.toString();

            // Test passes if method produces error message, blank input is skipped &  valid guess is the 2nd input value ("start")
            assertTrue(validGuess.equals("start"));
            assertTrue(errorMessage.contains("Words less than 5 letters are invalid. Please enter a valid word: "));

        }

        @Test // Requirement
        void testRepeatValidGuess() {

            // Add previous guess to the guesses Set
            String previousGuess = "smart";
            guesses.add(previousGuess);
            Scanner scanner = new Scanner(previousGuess + "\n" + "start" );
            String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

            errorMessage = testOut.toString();

            // Test passes if method throws an error with the message below & validGuess variable returned is not the repeated guess ("smart")
            assertTrue(!validGuess.equals(previousGuess));
            assertTrue(validGuess.equals("start"));
            assertTrue(errorMessage.contains("You have already used that word this game. Please enter a new word: "));
        }

        @Test // Requirement 2.2: Guesses containing numerical characters must not be accepted
        void testNumericalCharacters() {

            // Guess with number 8 in it should not be treated as a valid guess
            String numericalGuess = "8tart";
            Scanner scanner = new Scanner(numericalGuess + "\n" + "start" );
            String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);
            errorMessage = testOut.toString();

            // Test passes if error message is created, guess with number is skipped & second input ("start") is returned
            assertFalse(validGuess.equals(numericalGuess));
            assertFalse(validGuess.equals("start"));
            assertFalse(errorMessage.isBlank());
        }

    @Test // Requirement 2.3: Guesses containing special characters must not be accepted
    void testSpecialCharacters() {

        // Guess with a special character $ in it should not be treated as a valid guess
        String specialCharGuess = "$tart";
        Scanner scanner = new Scanner(specialCharGuess + "\n" + "start" );
        String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);
        errorMessage = testOut.toString();

        // Test passes if error message is created, guess with special character is skipped & second input ("start") is returned
        assertFalse(validGuess.equals(specialCharGuess));
        assertFalse(validGuess.equals("start"));
        assertFalse(errorMessage.isBlank());
    }

    @Test // Requirement 2.4: Capitliased letters must be accepted as valid when part of a valid guess
    void testUpperCase() {

        // Guess in uppercase format should be valid, with the getNextValidGuess returning the entered guess
        String upperCaseGuess = "START";

        Scanner scanner = new Scanner(upperCaseGuess + "\n");
        errorMessage = testOut.toString();
        String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Test passes if the valid guess variable returned equals the uppercase guess & there is no error message
        assertTrue(errorMessage.isBlank());
        assertTrue(validGuess != null);
        assertTrue(validGuess.equalsIgnoreCase(upperCaseGuess));
    }

    @Test // Requirement 2.5: Mixed case guesses must be accepted if the word is a valid guess
    void testMixedCase() {

        // Guess in camel case format
        String camelCaseGuess = "sTaRt";
        Scanner scanner = new Scanner(camelCaseGuess + "\n");
        errorMessage = testOut.toString();
        String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Test passes if the method's valid guess return equals the input guess and there is no error messages
        assertTrue(errorMessage.isBlank());
        assertTrue(validGuess != null);
        assertTrue(validGuess.equalsIgnoreCase(camelCaseGuess));
    }

    @Test // Requirement 2.6: Guesses with trailing spaces should be allowed if they are valid words
    void testLeadingSpace() {

        // Valid guess ("start") with leading blank spaces
        String leadingSpaceGuess = "   start";

        Scanner scanner = new Scanner(leadingSpaceGuess + "\n");
        errorMessage = testOut.toString();
        String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // If there is no error message, and the returned guess equals the entered guess, test passes
        assertTrue(errorMessage.isBlank());
        assertTrue(validGuess != null);
        assertTrue(validGuess.equals(leadingSpaceGuess.trim()));
    }

    @Test // Requirement 2.7: Guesses with leading spaces should be allowed if they are valid words
    void testTrailingSpace() {

        // Valid guess ("start") with trailing blank spaces
        String trailingSpaceGuess = "start   ";

        Scanner scanner = new Scanner(trailingSpaceGuess + "\n");
        errorMessage = testOut.toString();
        String validGuess = Commandle.getNextValidGuess(scanner, guesses, gameBoard);

        // Test passes if there is no error message, and the returned valid guess equals the entered guess ("start")
        assertTrue(errorMessage.isBlank());
        assertTrue(validGuess != null);
        assertTrue(validGuess.equals(trailingSpaceGuess.trim()));
    }


    }

