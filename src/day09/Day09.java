package day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day09 {
    private static List<List<Long>> histories;

    private static void setupHistories(List<String> lines) {
        histories = new ArrayList<>();
        lines.forEach(line -> histories.add(
                Arrays.stream(line.split(" "))
                        .mapToLong(Long::parseLong)
                        .boxed()
                        .collect(Collectors.toList())
        ));
    }

    private static Optional<List<Long>> calculateDifferencesSequence(List<Long> history) {
        List<Long> differencesSequence = new ArrayList<>();
        boolean encounteredAnyNonZeros = false;
        for (int i = 0; i + 1 < history.size(); i++) {
            long difference = history.get(i + 1) - history.get(i);
            differencesSequence.add(difference);
            if (difference != 0) encounteredAnyNonZeros = true;
        }
        return encounteredAnyNonZeros ? Optional.of(differencesSequence) : Optional.empty();
    }

    private static long calculateNextValue(List<Long> history) {
        Optional<List<Long>> differencesSequence = calculateDifferencesSequence(history);
        long lastValueOfCurrentHistory = history.get(history.size() - 1);
        return differencesSequence.map(sequence -> lastValueOfCurrentHistory + calculateNextValue(sequence)).orElse(lastValueOfCurrentHistory);
    }

    private static long calculatePreviousValue(List<Long> history) {
        Optional<List<Long>> differencesSequence = calculateDifferencesSequence(history);
        long firstValueOfCurrentHistory = history.get(0);
        return differencesSequence.map(sequence -> firstValueOfCurrentHistory - calculatePreviousValue(sequence)).orElse(firstValueOfCurrentHistory);
    }

    public static void main(String[] args) throws IOException {
        Path pathName = Paths.get("src/day09/day09input.txt");
        List<String> lines = Files.readAllLines(pathName);
        setupHistories(lines);

        long result1 = histories.stream().mapToLong(Day09::calculateNextValue).sum();
        long result2 = histories.stream().mapToLong(Day09::calculatePreviousValue).sum();

        System.out.printf("Part one solution: %d\n", result1);
        System.out.printf("Part two solution: %d\n", result2);
    }
}
