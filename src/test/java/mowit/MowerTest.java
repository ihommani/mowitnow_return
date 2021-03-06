package mowit;

import mowit.mower.Coordinates;
import mowit.mower.Field;
import mowit.mower.Instruction;
import mowit.mower.Mower;
import mowit.mower.Orientation;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static mowit.mower.Instruction.A;
import static mowit.mower.Instruction.D;
import static mowit.mower.Instruction.G;

/**
 * Created on 13/05/17.<br/>
 */
public class MowerTest {


    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "The mower can't mows no field")
    public void should_throw_NPE_when_no_field_is_defined() {
        // given
        // when
        // then
        Mower.builder()
                .setOrientation(Orientation.N)
                .setCoordinates(new Coordinates(2, 4))
                .build();
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "The mower should start at a position in the field.")
    public void should_throw_NPE_when_no_coordinate_is_defined() {

        // given
        // when
        // then
        Mower.builder()
                .defineField(new Field(3,4))
                .setOrientation(Orientation.N)
                .build();

    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "The mower should have an initial orientation.")
    public void should_throw_NPE_when_no_orientation_is_defined() {

        // given
        // when
        // then
        Mower.builder()
                .defineField(new Field(3,4))
                .setCoordinates(new Coordinates(2, 4))
                .build();
    }



    @DataProvider(name = "invalidInitPosition")
    public static Object[][] unvalideInitPosition() {
        return new Object[][]{
                {new Field(5, 5), new Coordinates(1, 6)},
                {new Field(5, 5), new Coordinates(6, 2)},
                {new Field(1, 1), new Coordinates(1, 2)},
                {new Field(1, 1), new Coordinates(1, -2)},
        };
    }

    @Test(dataProvider = "invalidInitPosition",
            expectedExceptions = IllegalStateException.class,
            expectedExceptionsMessageRegExp = "The mower is outside the field. Please provide other coordinates.")
    public void should_throw_illegal_state_exception_when_mower_begins_from_outside_the_field(Field fieldToMow, Coordinates initialMowCoordinate) {
        // given
        // when
        // then
        Mower.builder()
                .setCoordinates(initialMowCoordinate)
                .setOrientation(Orientation.N)
                .defineField(fieldToMow)
                .build();
    }

    @DataProvider(name = "invalidFields")
    public static Object[][] invalidFields() {
        return new Object[][]{
                {new Field(0, 0)},
                {new Field(0, 5)},
                {new Field(5, 0)},
                {new Field(-1, 2)},
                {new Field(1, -2)},
        };
    }

    @Test(dataProvider = "invalidFields", expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Field cannot have negative value")
    public void should_throw_illegal_state_exception_when_field_has_negative_dimensions(Field fieldToMow) {

        // given
        // when
        // then
        Mower.builder()
                .setCoordinates(new Coordinates(1,1))
                .setOrientation(Orientation.N)
                .defineField(fieldToMow)
                .build();

    }

    @DataProvider(name = "mowerScenarios")
    public static Object[][] mowerScenarios() {
        return new Object[][]{
                {new Field(5, 5), new Coordinates(1, 2), Orientation.N, Arrays.asList(G, A, G, A, G, A, G, A, A), new Coordinates(1, 3), Orientation.N},
                {new Field(5, 5), new Coordinates(3, 3), Orientation.E, Arrays.asList(A, A, D, A, A, D, A, D, D, A), new Coordinates(5, 1), Orientation.E},
                {new Field(3, 3), new Coordinates(3, 3), Orientation.E, Arrays.asList(A, A, D, A, A, D, A, D, D, A), new Coordinates(3, 1), Orientation.E},
                {new Field(10, 10), new Coordinates(3, 3), Orientation.E, Arrays.asList(A, A, D, A, A, D, A, D, D, A), new Coordinates(5, 1), Orientation.E},
                {new Field(10, 1), new Coordinates(0, 0), Orientation.N, Arrays.asList(A, A, A, A, A, A, A, A, A, A, A, A, A, A, A), new Coordinates(0, 10), Orientation.N}
        };
    }

    @Test(dataProvider = "mowerScenarios")
    public void should_mow_the_field(Field fieldToMow, Coordinates initialMowCoordinate, Orientation initialMowOrientation,
                                     List<Instruction> instructions, Coordinates finalCoordinates, Orientation finalOrientation) {
        // given
        Mower mower = Mower.builder()
                .setCoordinates(initialMowCoordinate)
                .setOrientation(initialMowOrientation)
                .defineField(fieldToMow)
                .build();

        // when
        instructions.forEach(mower::move);

        // then
        Assertions.assertThat(mower.getCoordinates()).isEqualTo(finalCoordinates);
        Assertions.assertThat(mower.getOrientation()).isEqualTo(finalOrientation);
    }
}