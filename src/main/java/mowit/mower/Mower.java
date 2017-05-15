package mowit.mower;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 13/05/17.<br/>
 * <p>
 * Class representing a mower.
 */
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

    private Mower() {
    }

    private Mower(Coordinates coordinates, Orientation orientation, Field field) {
        this.coordinates = coordinates;
        this.orientation = orientation;
        this.field = field;
    }

    public void move(@NonNull Instruction instruction) {
        log.debug("Received instruction {}", instruction);
        switch (instruction) {
            case A:
                Coordinates nextCoordinates = computeNextCoordinates();
                if (isAbleToMoveTo(nextCoordinates))
                    this.coordinates = nextCoordinates;
                else
                    log.debug("Cannot move to [{}]. Keeping the same coordinates", nextCoordinates);
                break;
            case D:
            case G:
                this.orientation = computeNewOrientation(instruction);
                break;
            default:
                log.warn("Unknown instruction [{}]. Passing to the next instruction.", instruction);
        }
        log.debug("New position [{},{},{}]", this.coordinates.getX(), this.coordinates.getY(), this.orientation);
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

    private Coordinates computeNextCoordinates() {
        Coordinates nextCoordinates = new Coordinates(this.coordinates.getX(), this.coordinates.getY());
        switch (this.orientation) {
            case N:
                return nextCoordinates.incrementY();
            case E:
                return nextCoordinates.incrementX();
            case W:
                return nextCoordinates.decrementX();
            case S:
                return nextCoordinates.decrementY();
            default:
                return nextCoordinates;
        }
    }

    private boolean isAbleToMoveTo(Coordinates coordinates) {
        return coordinates.getX() >= 0 &&
                coordinates.getY() >= 0 &&
                coordinates.getX() <= field.getLength() &&
                coordinates.getY() <= field.getHeight();
    }

    public static MowerBuilder builder() {
        return new MowerBuilder();
    }

    public static class MowerBuilder {
        private Coordinates coordinates;
        private Orientation orientation;

        private Field field;

        public MowerBuilder setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public MowerBuilder setOrientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public MowerBuilder defineField(Field field) {
            this.field = field;
            return this;
        }

        public Mower build() {

            Preconditions.checkNotNull(field, "The mower can't mows no field");
            Preconditions.checkNotNull(coordinates, "The mower should start at a position in the field.");
            Preconditions.checkNotNull(orientation, "The mower should have an initial orientation.");

            Preconditions.checkState(field.getHeight() > 0 && field.getLength() > 0, "Field cannot have negative value");

            Preconditions.checkState(coordinates.getX() >= 0 &&
                    coordinates.getY() >= 0 &&
                    coordinates.getX() <= field.getLength() &&
                    coordinates.getY() <= field.getHeight(), "The mower is outside the field. Please provide other coordinates.");

            return new Mower(coordinates, orientation, field);
        }
    }
}
