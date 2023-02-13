
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameBoardUnitTest {
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isInTarget() {
    }

    @Test
    void hasWon() {
        gameBoard = new GameBoard(new ArrayList<>());

        GameBoard.Status[] result = new GameBoard.Status[]{GameBoard.Status.correct, GameBoard.Status.correct, GameBoard.Status.correct, GameBoard.Status.correct, GameBoard.Status.correct};
        assertTrue(gameBoard.hasWon(result), "game is won");
    }

    @Test
    void containsCorrectWord() {
        ArrayList<String> list = new ArrayList<>();
        String word = "prone";
        list.add(word);
        gameBoard = new GameBoard(list);

        assertTrue(gameBoard.containsWord(word), "contains word");
    }


}