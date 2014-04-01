package minesweeper.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This data structure represents a minesweeper board.
 */
public class Board {
    private List<List<Cell>> Board = Collections.synchronizedList(new LinkedList<List<Cell>>());
    private int sizeX;
    private int sizeY;
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
    
    public Board(boolean debug, File file){
        this.debug = debug;
        createBoard(file);
    }
    
    private void createBoard(){
        for(int i = 0; i < sizeX; i++){
            List<Cell> column = Collections.synchronizedList(new LinkedList<Cell>());
            for(int j = 0; j < sizeY; j++){
                column.add(new Cell(addRandomizedBomb()));
            }
            Board.add(column);
        }
    }
    
    private void createBoard(File file){
        
        //read in file
        ArrayList<String> linesInFile = new ArrayList<String>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
          //add all lines to linesInFile
            String line = reader.readLine();
            while (line != null) {
                linesInFile.add(line);
                line = reader.readLine();
            }
            reader.close();
            
            String firstLine = linesInFile.get(0);
            int spaceLoc = firstLine.indexOf(" ");
            sizeX = Integer.valueOf(firstLine.substring(spaceLoc));
            sizeY = Integer.valueOf(firstLine.substring(spaceLoc+1, firstLine.length()));
            linesInFile.remove(0); //now only the board contents remain

            //check number of rows
            if(linesInFile.size() != sizeY){
                throw new RuntimeException("File improperly formatted."); 
            }
            
            //build the board
            for(String curLine: linesInFile){
                String lineNoSpace = curLine.replace(" ", "");
                //check the size of the line
                if (lineNoSpace.length() != sizeX) {
                    throw new RuntimeException("File improperly formatted.");
                } else {
                    char[] lineOfChars = lineNoSpace.toCharArray();
                    for(char curChar: lineOfChars){
                        List<Cell> column = Collections.synchronizedList(new LinkedList<Cell>());
                        if(curChar == 1){
                            column.add(new Cell(true));
                        }else if(curChar == 0){
                            column.add(new Cell(false));
                        }else{
                            throw new RuntimeException("File improperly formatted. A char other than 0 or 1 is found in the board");
                        }
                        Board.add(column);   
                    }
                }
            }
            
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read in file.");
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
    
    public synchronized boolean isValidPoint(int i, int j) {
        return (i < 0 || j < 0 || i >= this.sizeX || j >= this.sizeY);

    }
    
    private int getNeighboringBombNum(int i, int j){
        int bombCount = 0;
        if(isValidPoint(i, j-1)){ // top neighbor
            bombCount += Board.get(i).get(j-1).isBomb();
        }else if(isValidPoint(i-1, j-1)){ //top-left corner neighbor
            bombCount += Board.get(i-1).get(j-1).isBomb();
        }else if(isValidPoint(i-1, j)){ // left neighbor
            bombCount += Board.get(i-1).get(j).isBomb();
        }else if(isValidPoint(i-1, j+1)){ //bottom-left corner neighbor
            bombCount += Board.get(i-1).get(j+1).isBomb();
        }else if(isValidPoint(i, j+1)){ //bottom corner neighbor
            bombCount += Board.get(i).get(j+1).isBomb();
        }else if(isValidPoint(i+1, j+1)){ //bottom-right corner neighbor
            bombCount += Board.get(i+1).get(j+1).isBomb();
        }else if(isValidPoint(i+1, j)){ //right neighbor
            bombCount += Board.get(i+1).get(j).isBomb();
        }else if(isValidPoint(i+1, j-1)){ //top-right corner neighbor
            bombCount += Board.get(i+1).get(j-11).isBomb();
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
    
    public String dig (int i, int j){
        boolean explosion = false;
        if(isValidPoint(i, j)){
            explosion = Board.get(i).get(j).dig();
            if(explosion){
                if(debug){
                    return "Boom!\n"; 
                }else{
                    return "Game Over!\n";
                }
            }else{ //not a bomb!
                if(getNeighboringBombNum(i,j) == 0){
                    digNeighbors(i,j);
                }
            }
        }
        return look();
    }
    
    public void digNeighbors(int i, int j){
        if(isValidPoint(i, j-1)){ // top neighbor
            if(getNeighboringBombNum(i,j-1) == 0) dig(i,j-1);
        }else if(isValidPoint(i, j+1)){ //bottom neighbor
            if(getNeighboringBombNum(i,j+1) == 0) dig(i,j+1);
        }else if(isValidPoint(i-1, j)){ //left neighbor
            if(getNeighboringBombNum(i-1,j) == 0) dig(i-1,j);
        }else if(isValidPoint(i+1, j)){ //right neighbor
            if(getNeighboringBombNum(i+1,j) == 0) dig(i+1,j);
        }
    }
}
