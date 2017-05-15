package mowit;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import lombok.Getter;

import java.nio.file.Path;

/**
 * Created on 14/05/17.<br/>
 */
@Getter
public class Arguments {
    @Parameter(description = "A file containing instructions to pilot the mower into a field",
            names = "--instruction",
            required = true,
            converter = PathConverter.class,
            validateWith = InstructionFileValidator.class)
    private Path instructions;

    @Parameter(names = "--help", help = true)
    private boolean help;
}
