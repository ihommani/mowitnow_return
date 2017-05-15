package mowit.mower;

import lombok.Data;
import lombok.Getter;

/**
 * Created on 13/05/17.<br/>
 */
@Data
@Getter
public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates incrementX(){
        this.x++;
        return this;
    }

    public Coordinates incrementY(){
        this.y++;
        return this;
    }

    public Coordinates decrementX(){
        this.x--;
        return this;
    }

    public Coordinates decrementY(){
        this.y--;
        return this;
    }
}
