package mowit;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.stream.Stream;

import static mowit.Instruction.A;
import static mowit.Instruction.D;
import static mowit.Instruction.G;

/**
 * Created on 13/05/17.<br/>
 */
@Slf4j
public class MowerTest {


    @Test
    public void should_mow_the_field() {

        // given
        Mower mower = Mower.builder()
                .coordinates(new Coordinates(1, 2))
                .orientation(Orientation.N)
                .field(new Field(5,5))
                .build();


        // when
        Stream.of(G,A,G,A,G,A,G,A,A).forEach(mower::move);

        // then
        Assertions.assertThat(mower.getCoordinates()).isEqualTo(new Coordinates(1,3));
        Assertions.assertThat(mower.getOrientation()).isEqualTo(Orientation.N);
    }

    @Test
    public void should_mow_the_field2() {
        // given
        Mower mower = Mower.builder()
                .coordinates(new Coordinates(3, 3))
                .orientation(Orientation.E)
                .field(new Field(5,5))
                .build();


        // when
        Stream.of(A,A,D,A,A,D,A,D,D,A).forEach(mower::move);

        // then
        Assertions.assertThat(mower.getCoordinates()).isEqualTo(new Coordinates(5,1));
        Assertions.assertThat(mower.getOrientation()).isEqualTo(Orientation.E);
    }
}