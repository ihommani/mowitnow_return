package mowit.mower;

import lombok.Data;

/**
 * Created on 14/05/17.<br/>
 */
@Data
public class Position {

    private Coordinates coordinates;
    private Orientation orientation;

    public Position(Coordinates coordinates, Orientation orientation) {
        this.coordinates = coordinates;
        this.orientation = orientation;
    }
}
