package mowit;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import mowit.cli.Arguments;
import mowit.mower.Field;
import mowit.mower.Instruction;
import mowit.mower.Mower;
import mowit.util.InstructionFileParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created on 14/05/17.<br/>
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        Arguments arguments = new Arguments();
        try {
            JCommander.newBuilder()
                    .addObject(arguments)
                    .build()
                    .parse(args);

        } catch (ParameterException e) {
            return;
        }

        if (arguments.isHelp()) {
            displayHelp();
            return;
        }

        Path instructionsLocation = arguments.getInstructions();

        InstructionFileParser instructionFileParser = new InstructionFileParser(instructionsLocation);

        Pair<Integer, Integer> fieldDimension = instructionFileParser.getFieldDimension();

        instructionFileParser.getInitialPositionWithInstructions().stream()
                .forEach(positionListEntry -> {
                    Mower mower = Mower.builder()
                            .defineField(new Field(fieldDimension.getKey(), fieldDimension.getValue()))
                            .setCoordinates(positionListEntry.getKey().getCoordinates())
                            .setOrientation(positionListEntry.getKey().getOrientation())
                            .build();

                    List<Instruction> instructions = positionListEntry.getValue();
                    instructions.stream().forEach(mower::move);

                    log.info("{} {} {}", mower.getCoordinates().getX(), mower.getCoordinates().getY(), mower.getOrientation());

                });
    }

    private static void displayHelp() {
        System.out.println("Small utility to pilot mower into a field.\n" +
                "Usage: mowitnow --instruction [path]\n" +
                "Please follow the README for more information about the format.\n");
    }
}
