package mowit;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 13/05/17.<br/>
 * <p>
 * Class representing a mow.
 */
@Builder
@Getter
@Slf4j
public class Mower {

    private static final List<Orientation> orientationLeft = Arrays.asList(Orientation.N, Orientation.E, Orientation.W, Orientation.S);
    private static final List<Orientation> orientationRight = Arrays.asList(Orientation.E, Orientation.S, Orientation.N, Orientation.W);

    private Coordinates coordinates;
    private Orientation orientation;

    private Field field;

    public void move(@NonNull Instruction instruction) {
        log.debug("Received instruction {}", instruction);
        computeNextPosition(instruction);
        log.debug("New position [{},{},{}]", this.coordinates.getX(), this.coordinates.getY(), this.orientation);
    }

    private void computeNextPosition(Instruction instruction) {
        switch (instruction) {
            case A:
                Coordinates newCoordinates = computeNewCoordinates();
                if (isAbleToMove(newCoordinates))
                    this.coordinates = newCoordinates;
                break;
            case D:
            case G:
                this.orientation = computeNewOrientation(instruction);
                break;
            default:
                log.warn("Unknown instruction [{}]. Passing to the next instruction.", instruction);
        }
    }

    private Orientation computeNewOrientation(Instruction instruction) {
        switch (instruction) {
            case D:
                return orientationRight.get(orientationLeft.indexOf(this.orientation));
            case G:
                return orientationLeft.get(orientationRight.indexOf(this.orientation));
            default:
                log.warn("{} is not an instruction to change orientation. Ignoring it.", instruction);
                return this.orientation;
        }
    }

    private Coordinates computeNewCoordinates() {
        switch (this.orientation) {
            case N:
                return this.coordinates.incrementY();
            case E:
                return this.coordinates.incrementX();
            case W:
                return this.coordinates.decrementX();
            case S:
                return this.coordinates.decrementY();
            default:
                return this.coordinates;
        }
    }

    private boolean isAbleToMove(Coordinates coordinates) {
        return coordinates.getX() >= 0 &&
                coordinates.getY() >= 0 &&
                coordinates.getX() <= field.getLength() &&
                coordinates.getY() <= field.getHeight();
    }


}
