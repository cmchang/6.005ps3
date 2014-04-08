package minesweeper.server;
/**
 * 
 * This class is not thread safe.
 *
 */
public class Cell {
    private String state = "untouched";
    private boolean isBomb;
    
    public Cell(boolean isBomb){
        this.isBomb = isBomb;
    }
    
    public String look(){
        return state;
    }
    
    public boolean dig(){
        if(state == "untouched"){
            state = "dug";
            if(isBomb){
                isBomb = false;
                return true;
            }
        }
        return false;
    }

    public void flag(){
        if(state != "dug"){
            state = "flagged";
        }
    }
    
    public void deflag(){
        if(state != "dug"){
            state = "untouched";
        }
    }
    
    //Note: this method returns a 1 if it is a bomb and 0 if it is not a bomb
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
