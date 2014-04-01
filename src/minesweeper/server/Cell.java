package minesweeper.server;

public class Cell {
    private String state = "untouched";
    private boolean isBomb;
    private boolean isDug = false;
    
    public Cell(boolean isBomb){
        this.isBomb = isBomb;
    }
    
    public String look(){
        return state;
    }
    
    public boolean dig(){
        isDug = true;
        state = "dug";
        if(isBomb){
            isBomb = false;
            return true;
        }
        return false;
    }

    public void flag(){
        state = "flagged";
    }
    
    public void deflag(){
        state = "untouched";
    }
    
    public int isBomb(){
        if(isBomb){
            return 1;
        }
        return 0;
    }
    
    public boolean isUntouched(){
        return (state.equals("untouched"));
    }
}
