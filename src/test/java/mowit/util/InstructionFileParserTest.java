package mowit.util;

import com.google.common.io.Resources;
import javafx.util.Pair;
import mowit.mower.Coordinates;
import mowit.mower.Instruction;
import mowit.mower.Orientation;
import mowit.mower.Position;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mowit.mower.Instruction.A;
import static mowit.mower.Instruction.D;
import static mowit.mower.Instruction.G;

/**
 * Created on 15/05/17.<br/>
 */
public class InstructionFileParserTest {

    private InstructionFileParser underTest = new InstructionFileParser(Paths.get(Resources.getResource("valid_instruction_pattern.txt").getPath()));


    @Test
    public void should_map_instructions_by_positions() {

        // given

        // when
        List<Map.Entry<Position, List<Instruction>>> instructionsByPosition = underTest.getInitialPositionWithInstructions();

        // then
        Assertions.assertThat(instructionsByPosition).hasSize(4);
        //Assertions.assertThat(instructionsByPosition.keySet()).containsExactly(new Position(new Coordinates(1, 2), Orientation.E),new Position(new Coordinates(1, 2), Orientation.E));
        Assertions.assertThat(instructionsByPosition).containsExactly(
                new HashMap.SimpleEntry(new Position(new Coordinates(1, 2), Orientation.E), Lists.newArrayList(A, D, D, G, G, A, A)),
                new HashMap.SimpleEntry(new Position(new Coordinates(1, 2), Orientation.E), Lists.newArrayList(A, D, D, G, G, A, A)),
                new HashMap.SimpleEntry(new Position(new Coordinates(1, 5), Orientation.N), Lists.newArrayList(A, D, D, G, G, A, A)),
                new HashMap.SimpleEntry(new Position(new Coordinates(1, 2), Orientation.S), Lists.newArrayList(A, D, D, G, G, A, A))
        );
    }

    @Test
    public void should_get_field_dimension() {

        // given
        // when
        Pair<Integer, Integer> fieldDimension = underTest.getFieldDimension();

        // then
        Assertions.assertThat(fieldDimension.getKey()).isEqualTo(5);
        Assertions.assertThat(fieldDimension.getValue()).isEqualTo(5);
    }
}