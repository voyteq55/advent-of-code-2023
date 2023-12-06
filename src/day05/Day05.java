package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day05 {
    private static List<List<long[]>> allMaps;
    private static List<Long> seeds;

    private static void setupMaps(List<String> lines) {
        boolean isProcessingNumbers = false;
        allMaps = new ArrayList<>();
        List<long[]> currentMap = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty() && Character.isDigit(line.charAt(0))) {
                long[] values = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();
                if (!isProcessingNumbers) {
                    isProcessingNumbers = true;
                    currentMap = new ArrayList<>();
                    allMaps.add(currentMap);
                }
                currentMap.add(values);
            } else {
                isProcessingNumbers = false;
            }
        }
    }

    private static long findLocationForSeed(long seed) {
        long currentNumber = seed;
        for (List<long[]> map : allMaps) {
            Iterator<long[]> iterator = map.iterator();
            boolean foundNextNumber = false;
            while (!foundNextNumber && iterator.hasNext()) {
                long[] values = iterator.next();
                long destinationRangeStart = values[0];
                long sourceRangeStart = values[1];
                long sourceRangeLength = values[2];
                if (currentNumber >= sourceRangeStart && currentNumber < sourceRangeStart + sourceRangeLength) {
                    currentNumber = destinationRangeStart + (currentNumber - sourceRangeStart);
                    foundNextNumber = true;
                }
            }
        }
        return currentNumber;
    }

    private static long calculateSmallestLocationFromRanges() {
        long smallestLocation = Long.MAX_VALUE;
        for (int i = 0; i + 1 < seeds.size(); i += 2) {
            long seedRangeStart = seeds.get(i);
            long seedRangeLength = seeds.get(i + 1);
            for (long seed = seedRangeStart; seed < seedRangeStart + seedRangeLength; seed++) {
                long location = findLocationForSeed(seed);
                if (location < smallestLocation) {
                    smallestLocation = location;
                }
            }
        }
        return smallestLocation;
    }

    public static void main(String[] args) throws IOException {
        Path pathName = Paths.get("src/day05/day05input.txt");
        List<String> lines = Files.readAllLines(pathName);
        seeds = Stream.of(lines.get(0).split(": ")[1].split(" ")).map(Long::parseLong).toList();

        setupMaps(lines);
        long result1 = seeds.stream().map(Day05::findLocationForSeed).min(Long::compareTo).orElseThrow();

        System.out.printf("Part one result: %d\n", result1);
        System.out.printf("Part two result: %d\n", calculateSmallestLocationFromRanges());
    }
}
