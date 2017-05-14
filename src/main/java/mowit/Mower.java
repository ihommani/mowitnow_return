package mowit;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 13/05/17.<br/>
 * <p>
 * Class representing a mow.
 */
@Builder
@Getter
@Slf4j
public class Mower {

    private static final BiMap<Orientation, Orientation> leftToRight = EnumBiMap.create(
            ImmutableMap.<Orientation, Orientation>builder()
                    .put(Orientation.N, Orientation.E)
                    .put(Orientation.E, Orientation.S)
                    .put(Orientation.W, Orientation.N)
                    .put(Orientation.S, Orientation.W)
                    .build()
    );

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
                return leftToRight.get(this.orientation);
            case G:
                return leftToRight.inverse().get(this.orientation);
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
