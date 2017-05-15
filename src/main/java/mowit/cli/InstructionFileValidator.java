package mowit.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created on 14/05/17.<br/>
 */
@Slf4j
public class InstructionFileValidator implements IParameterValidator {

    private static final Pattern fiedlPattern = Pattern.compile("^\\d+ \\d+$");
    private static final Pattern coordinatePattern = Pattern.compile("^\\d+ \\d+ [NEWS]$");
    private static final Pattern instructionPattern = Pattern.compile("^[GDA]+$");

    @Override
    public void validate(String name, String value) throws ParameterException {
        try {
            Path instructionsPath = Paths.get(value);
            Preconditions.checkState(Files.exists(instructionsPath));
            Preconditions.checkState(Files.isReadable(instructionsPath));

            List<String> instructions = Files.readAllLines(instructionsPath);
            Preconditions.checkState(instructions.size() > 0, "Instruction cannot be an empty file.");
            Preconditions.checkState(instructions.size() % 2 == 1, "Instruction should have an uneven number of lines.");

            Preconditions.checkState(fiedlPattern.matcher(instructions.remove(0)).matches(), "Field dimensions does not match the pattern X Y");

            IntStream.range(0, instructions.size())
                    .filter(index -> index % 2 == 0)
                    .mapToObj(instructions::get)
                    .forEach(coordinate ->
                            Preconditions.checkArgument(coordinatePattern.matcher(coordinate).matches(), "Coordinate " + coordinate + " is not valid."));

            IntStream.range(0, instructions.size())
                    .filter(index -> index % 2 == 1)
                    .mapToObj(instructions::get)
                    .forEach(instructionsRow ->
                            Preconditions.checkArgument(instructionPattern.matcher(instructionsRow).matches(), "Instruction " + instructionsRow + " is not valid."));
        } catch (IOException e) {
            throw new ParameterException("Error while getting the instruction file.", e);
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw new ParameterException(e.getMessage(), e);
        }
    }
}
