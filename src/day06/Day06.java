package day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day06 {
    private static List<long[]> partOneRaces;
    private static long[] partTwoRace;

    private static void setupPartOneRaces(List<String> lines) {
        String[] line1 = lines.get(0).split(" +");
        String[] line2 = lines.get(1).split(" +");
        partOneRaces = new ArrayList<>();
        for (int i = 1; i < line1.length; i++) {
            partOneRaces.add(new long[]{Long.parseLong(line1[i]), Long.parseLong(line2[i])});
        }
    }

    private static void setupPartTwoRace(List<String> lines) {
        partTwoRace = new long[2];
        partTwoRace[0] = Long.parseLong(lines.get(0).split(": +")[1].replace(" ", ""));
        partTwoRace[1] = Long.parseLong(lines.get(1).split(": +")[1].replace(" ", ""));
    }

    private static long possibleWinsInARace(long[] race) {
        long totalTime = race[0];
        long minimumDistance = race[1];
        long minimumButtonHoldTime = -1;
        long distanceTravelled = 0;
        while (distanceTravelled <= minimumDistance) {
            minimumButtonHoldTime++;
            distanceTravelled = minimumButtonHoldTime * (totalTime - minimumButtonHoldTime);
        }
        long maximumButtonHoldTime = totalTime - minimumButtonHoldTime;
        return maximumButtonHoldTime - minimumButtonHoldTime + 1;
    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/day06/day06input.txt");
        List<String> lines = Files.readAllLines(path);

        setupPartOneRaces(lines);
        setupPartTwoRace(lines);

        long result1 = partOneRaces.stream().mapToLong(Day06::possibleWinsInARace).reduce(1, (a, b) -> a * b);
        long result2 = possibleWinsInARace(partTwoRace);

        System.out.printf("Part one solution: %d\n", result1);
        System.out.printf("Part two solution: %d\n", result2);
    }
}
