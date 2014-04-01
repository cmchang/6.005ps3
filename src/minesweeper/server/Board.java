package minesweeper.server;

import java.io.File;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This data structure represents a minesweeper board.
 *
 */
public class Board {
    private List<List<Cell>> Board = Collections.synchronizedList(new LinkedList<List<Cell>>());
    private final int sizeX;
    private final int sizeY;
    private final boolean debug;
    
    /**
     * This constructs a minesweeper board of size sizeX*sizeY
     * where each cell has a 0.25% probability of containing a bomb
     * 
     * @param debug state
     * @param sizeX width of the board
     * @param sizeY height of the board
     */
    public Board(boolean debug, int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.debug = debug;
        
        createBoard();
    }
    
//    public Board(boolean debug, File file){
//        
//    }
    
    private void createBoard(){
        for(int i = 0; i < sizeX; i++){
            List<Cell> columns = Collections.synchronizedList(new LinkedList<Cell>());
            for(int j = 0; j < sizeY; j++){
                columns.add(new Cell(addRandomizedBomb()));
            }
            Board.add(columns);
        }
    }
    
    private boolean addRandomizedBomb(){
        Random randNumGenerator = new Random(); 
        int randNum = randNumGenerator.nextInt(3); //generates random integer from 0 to 3
        if(randNum == 0){ // 1/4 chance of being 0
            return true;
        }
        return false;
    }
    
    public String look(){
        String board = "";
        String cellState;
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                cellState = Board.get(i).get(j).look();
                switch(cellState){
                case "untouched": board += "-";
                case "flagged":   board += "F";
                case "dug":       board += getNeighboringBombNum(i,j);
                }
            }
            board +="\r\n";
        }
        board = board.replace("0", " ");
        return board;
    }
    
    public String showBombs(){
        String board = "";
        int isCellBomb;
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                isCellBomb = Board.get(i).get(j).isBomb();
                switch(isCellBomb){
                case 0: board += "-";
                case 1:   board += "B";
                }
            }
            board +="\r\n";
        }
        return board;
    }
    
    private int getNeighboringBombNum(int i, int j){
        int bombCount = 0;
        if(j-1 >= 0){ // top neighbor
            bombCount += Board.get(i).get(j-1).isBomb();
        }else if(i-1 >= 0 && j-1 >= 0){ //top-left corner neighbor
            bombCount += Board.get(i-1).get(j-1).isBomb();
        }else if(i-1 >= 0){ // left neighbor
            bombCount += Board.get(i-1).get(j).isBomb();
        }else if(i-1 >= 0 && j+1 >= 0){ //bottom-left corner neighbor
            bombCount += Board.get(i-1).get(j+1).isBomb();
        }else if(j+1 >= 0){ //bottom corner neighbor
            bombCount += Board.get(i).get(j+1).isBomb();
        }else if(i+1 >= 0 && j+1 >= 0){ //bottom-right corner neighbor
            bombCount += Board.get(i+1).get(j+1).isBomb();
        }else if(i+1 >= 0){ //right neighbor
            bombCount += Board.get(i+1).get(j).isBomb();
        }else if(i+1 >= 0 && j+1 >= 0){ //top-right corner neighbor
            bombCount += Board.get(i+1).get(j+1).isBomb();
        }
        return bombCount;
    }
 
    public void flag (int i, int j){
        if(i >= 0 && i <= sizeX && j >= 0 && j <= sizeY){
            Board.get(i).get(j).flag();
        }
    }
    
    public void deflag (int i, int j){
        if(i >= 0 && i <= sizeX && j >= 0 && j <= sizeY){
            Board.get(i).get(j).deflag();
        }
    }
    
    public void dig (int i, int j){
        boolean explosion = false;
        if(i >= 0 && i <= sizeX && j >= 0 && j <= sizeY){
            explosion = Board.get(i).get(j).dig();
            if(explosion){
                //message BOOM!!!
                //TODO: recursively dig neighbors!  
                digNeighbors(i,j);
            }else{ //not a bomb!
                if(getNeighboringBombNum(i,j) == 0){
                    digNeighbors(i,j);
                }
            }
        }
    }
    
    public void digNeighbors(int i, int j){
        if(j-1 >= 0){ // top neighbor
            if(getNeighboringBombNum(i,j-1) == 0) dig(i,j-1);
        }else if(j+1 <= sizeY){ //bottom neighbor
            if(getNeighboringBombNum(i,j+1) == 0) dig(i,j+1);
        }else if(i-1 >= 0){ //left neighbor
            if(getNeighboringBombNum(i-1,j) == 0) dig(i-1,j);
        }else if(i+1 <= sizeX){ //right neighbor
            if(getNeighboringBombNum(i+1,j) == 0) dig(i+1,j);
        }
        
    }
}
