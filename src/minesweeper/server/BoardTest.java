package minesweeper.server;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class BoardTest {

    /**
     * Test for the Board Class:
     * (A) Creating a Board
     *      (A1) Using constructor given only the debug status, and dimensions of board
     *      (A2) Using constructor given the text file
     * (B) Adding/Removing Flags
     *      (B1) Adding a valid flag
     *      (B2) Adding an invalid flag (out of bounds point)
     *      (B3) Removing a valid flag
     *      (B4) Removing an invalid flag (out of bounds point)
     *      (B5) Adding an invalid flag (on a dug spot)
     * (C) Digs
     *      (C1) Digging a valid point
     *      (C2) Digging an invalid point (out of bounds point)
     *      (C3) Digging an invalid point (state of point is flagged)
     */
    
    //Tests A1
    @Test public void CreateBoardA1Test(){
        Board myBoard = new Board(true, 9, 4);
        String expectedAnswer = "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //couldn't figure out how to read the file in time, but this constructor 
    //worked when I tested it manually
    
    //Tests A2
    @Test public void CreateBoardA2Test(){
        Board myBoard = new Board(true, new File("src/minesweeper/server/boardFile.txt"));
        String expectedAnswer = "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test B1
    @Test public void AddValidFlagB1Test(){
        Board myBoard = new Board(true, 9, 4);
        myBoard.flag(0,0);
        String expectedAnswer = "F - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test B2
    @Test public void AddInvalidFlagB2Test(){
        Board myBoard = new Board(true, 9, 4);
        myBoard.flag(0,11);
        String expectedAnswer = "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test B3
    @Test public void RemoveValidFlagB1Test(){
        Board myBoard = new Board(true, 9, 4);
        myBoard.flag(0,0);
        myBoard.flag(1,1);
        myBoard.deflag(1,1);
        String expectedAnswer = "F - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test B4
    @Test public void RemoveInvalidFlagB1Test(){
        Board myBoard = new Board(true, 9, 4);
        myBoard.flag(0,0);
        myBoard.flag(1,1);
        myBoard.deflag(10, 10);
        String expectedAnswer = "F - - - - - - - -\r\n"
                + "- F - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test B5
    @Test public void InvalidFlagDugSpotB5Test(){
        Board myBoard = new Board(true, new File("src/minesweeper/server/boardFile.txt"));
        myBoard.dig(0,0);
        myBoard.flag(0,0);
        String expectedAnswer = "      1 - 1  \r\n"
                + "      1 - 1  \r\n"
                + "      1 1 1  \r\n"
                + "             \r\n"
                + "             \r\n"
                + "1 1          \r\n"
                + "- 1          \r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test C1
    @Test public void ValidDigC1Test(){
        Board myBoard = new Board(true, new File("src/minesweeper/server/boardFile.txt"));
        myBoard.dig(0,0);
        String expectedAnswer = "      1 - 1  \r\n"
                + "      1 - 1  \r\n"
                + "      1 1 1  \r\n"
                + "             \r\n"
                + "             \r\n"
                + "1 1          \r\n"
                + "- 1          \r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test C2
    @Test public void InvalidDigC2Test(){
        Board myBoard = new Board(true, new File("src/minesweeper/server/boardFile.txt"));
        myBoard.dig(10, 10);
        String expectedAnswer = "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n"
                + "- - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
    
    //Test C3
    @Test public void DigFlagStateC3Test(){
        Board myBoard = new Board(true, 9, 4);
        myBoard.flag(0,0);
        myBoard.dig(0,0);
        String expectedAnswer = "F - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n"
                + "- - - - - - - - -\r\n";
        assertEquals(myBoard.look(), expectedAnswer);
    }
}
