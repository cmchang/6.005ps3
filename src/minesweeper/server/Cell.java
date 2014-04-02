package minesweeper.server;
/**
 * 
 * This class is not thread safe.
 *
 */
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
        if(state == "untouched"){
            isDug = true;
            state = "dug";
            if(isBomb){
                isBomb = false;
                return true;
            }
        }
        return false;
    }

    public void flag(){
        if(!isDug){
            state = "flagged";
        }
    }
    
    public void deflag(){
        if(!isDug){
            state = "untouched";
        }
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
