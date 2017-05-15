package mowit;

import com.beust.jcommander.ParameterException;
import com.google.common.io.Resources;
import mowit.cli.InstructionFileValidator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Paths;

/**
 * Created on 15/05/17.<br/>
 */
public class InstructionFileValidatorTest {

    @DataProvider(name = "wrongInstructionFile")
    public static Object[][] wrongInstructionFile() {
        return new Object[][]{
                {"instruction_with_no_line.txt"},
                {"instruction_with_wrong_number_of_line.txt"},
        };
    }


    @Test(dataProvider = "wrongInstructionFile", expectedExceptions = ParameterException.class)
    public void should_throw_parameter_exception_on_missing_line(String instuctionFile) {

        // given
        String valueParam = Paths.get(Resources.getResource(instuctionFile).getPath()).toString();
        // when
        // then
        new InstructionFileValidator().validate("file", valueParam);

    }

    @DataProvider(name = "instructionWithBadPattern")
    public static Object[][] instructionWithBadPattern() {
        return new Object[][]{
                {"instruction_with_wrong_coordinate_pattern.txt"},
                {"instruction_with_wrong_field_pattern.txt"},
                {"instruction_with_wrong_instruction_pattern.txt"},
        };

    }

    @Test(dataProvider = "instructionWithBadPattern", expectedExceptions = ParameterException.class)
    public void should_throw_parameter_exception_on_bad_patterns(String instructionFile) {

        // given
        String valueParam = Paths.get(Resources.getResource(instructionFile).getPath()).toString();
        // when
        // then
        new InstructionFileValidator().validate("file", valueParam);

    }

    @Test
    public void should_validate_valid_file() {

        // given
        String valueParam = Paths.get(Resources.getResource("valid_instruction_pattern.txt").getPath()).toString();
        // when
        // then
        new InstructionFileValidator().validate("file", valueParam);
    }
}