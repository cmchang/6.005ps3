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
        System.out.println("Create Board " + sizeX + ", " + sizeY);
        for(int i = 0; i < sizeX; i++){
            List<Cell> column = Collections.synchronizedList(new LinkedList<Cell>());
            for(int j = 0; j < sizeY; j++){
                column.add(new Cell(addRandomizedBomb()));
            }
            Board.add(column);
        }
    }
    
    private void createBoard(File file){
        System.out.println("Create Board with File "+ sizeX + ", " + sizeY);

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
                    for(char curChar: lineOfChars){ //REDO: BOARD is rotated
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
        int randNum = randNumGenerator.nextInt(4); //generates random integer from 0 (inclusive) to 4 (exclusive)
        if(randNum == 0){ // 1/4 chance of being 0
            return true;
        }
        return false;
    }
    
    public String look(){
        String board = "";
        String cellState;
        for(int j = 0; j < sizeY; j++){
            for(int i = 0; i < sizeX; i++){
                cellState = Board.get(i).get(j).look();
                switch(cellState){
                case "untouched": board += "- ";
                    break;
                case "flagged":   board += "F ";
                    break;
                case "dug":       board += getNeighboringBombNum(i,j) + " ";
                    break;
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
                case 0: board += "- ";
                    break;
                case 1:   board += "B ";
                    break;
                }
            }
            board +="\r\n";
        }
        return board;
    }
    
    public synchronized boolean isValidPoint(int i, int j) {
        return (i >= 0 && j >= 0 && i < sizeX && j < sizeY);

    }
    
    private int getNeighboringBombNum(int i, int j){
        int bombCount = 0;

        for(int x = i-1; x<= i+1; x++){
            for(int y = j-1; y <= j+1; y++){
                if(isValidPoint(x, y)){
                    bombCount += Board.get(x).get(y).isBomb();
                }
            }
        }
        return bombCount;
    }
 
    public String flag (int i, int j){
        if(i >= 0 && i <= sizeX && j >= 0 && j <= sizeY){
            Board.get(i).get(j).flag();
        }
        
        return look();
    }
    
    public String deflag (int i, int j){
        if(i >= 0 && i <= sizeX && j >= 0 && j <= sizeY){
            Board.get(i).get(j).deflag();
        }
        return look();
    }
    
    public String dig (int i, int j){
        boolean explosion = false;
        
        if(isValidPoint(i, j) ){
            Cell curCell = Board.get(i).get(j);
            if(curCell.isUntouched()){
                explosion = curCell.dig();
                
                System.out.println(i + ", "+ j);
                if(getNeighboringBombNum(i,j) == 0){
                    System.out.println("digging neighbors");
                    digNeighbors(i,j);
                }
                
                if(explosion){
                    return "BOOM!\n";
                }
            }
        }
        return look();
    }
    
    //if called, guaranteed that no neighboring bombs (b/c placement in dig method)
    public void digNeighbors(int i, int j){
        for(int x = i-1; x<= i+1; x++){
            for(int y = j-1; y <= j+1; y++){
                System.out.println("dig: " + x + ", " + y);
                dig(x,y);
            }
        }
        
    }
}
