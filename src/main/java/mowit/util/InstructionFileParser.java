package mowit.util;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Chars;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import mowit.mower.Coordinates;
import mowit.mower.Instruction;
import mowit.mower.Orientation;
import mowit.mower.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created on 15/05/17.<br/>
 */
@RequiredArgsConstructor
public class InstructionFileParser {

    private final Path instrutionFilePath;

    private static final Splitter SPACE_SPLITTER = Splitter.on(' ');

    @SneakyThrows(IOException.class)
    public Pair<Integer, Integer> getFieldDimension() {
        String fields = Files.readAllLines(instrutionFilePath).get(0);
        List<String> dimension = SPACE_SPLITTER.splitToList(fields);
        return new Pair<>(Integer.valueOf(dimension.get(0)), Integer.valueOf(dimension.get(1)));
    }

    @SneakyThrows(IOException.class)
    public List<Map.Entry<Position, List<Instruction>>> getInitialPositionWithInstructions() {
        List<String> instructions = Files.readAllLines(instrutionFilePath);
        instructions.remove(0);

        ImmutableList.Builder<Map.Entry<Position, List<Instruction>>> builder = ImmutableList.builder();

        List<Position> initialPositions = getInitialPositions(instructions);
        List<List<Instruction>> instructionsList = getInstructions(instructions);

        IntStream.range(0, initialPositions.size()).forEach(index -> builder.add(new HashMap.SimpleEntry(initialPositions.get(index), instructionsList.get(index))));

        return builder.build();
    }

    private List<List<Instruction>> getInstructions(List<String> instructions) {
        return IntStream.range(0, instructions.size())
                .filter(index -> index % 2 == 1)
                .mapToObj(instructions::get)
                .map(String::toCharArray)
                .map(Chars::asList)
                .map(chars -> chars.stream()
                        .map(String::valueOf)
                        .map(Instruction::valueOf)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<Position> getInitialPositions(List<String> instructions) {
        return IntStream.range(0, instructions.size())
                .filter(index -> index % 2 == 0)
                .mapToObj(instructions::get)
                .map(SPACE_SPLITTER::splitToList)
                .map(strings -> new Position(new Coordinates(Integer.valueOf(strings.get(0)), Integer.valueOf(strings.get(1))), Orientation.valueOf(strings.get(2))))
                .collect(Collectors.toList());
    }
}
