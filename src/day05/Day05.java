package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Day05 {
    private static List<List<long[]>> allMaps;
    private static List<List<long[]>> reversedMaps;
    private static List<Long> seeds;
    private static List<long[]> seedRanges;

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
        reversedMaps = new ArrayList<>(allMaps);
        Collections.reverse(reversedMaps);
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

    private static void setupSeedRanges() {
        seedRanges = new ArrayList<>();
        for (int i = 0; i + 1 < seeds.size(); i += 2) {
            seedRanges.add(new long[]{seeds.get(i), seeds.get(i + 1)});
        }
    }

    private static boolean isSeedInRange(long seed) {
        for (long[] seedRange : seedRanges) {
            if (seed >= seedRange[0] && seed < seedRange[0] + seedRange[1]) {
                return true;
            }
        }
        return false;
    }

    private static long findSeedFromLocation(long location) {
        long currentSeed = location;
        for (List<long[]> map : reversedMaps) {
            Iterator<long[]> iterator = map.iterator();
            boolean foundPreviousNumber = false;
            while (!foundPreviousNumber && iterator.hasNext()) {
                long[] values = iterator.next();
                long destinationRangeStart = values[0];
                long sourceRangeStart = values[1];
                long sourceRangeLength = values[2];
                if (currentSeed >= destinationRangeStart && currentSeed < destinationRangeStart + sourceRangeLength) {
                    currentSeed = sourceRangeStart + (currentSeed - destinationRangeStart);
                    foundPreviousNumber = true;
                }
            }
        }
        return currentSeed;
    }

    private static long calculateSmallestLocationFromRanges() {
        long checkedLocation = -1;
        while (!isSeedInRange(findSeedFromLocation(++checkedLocation)));
        return checkedLocation;
    }

    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();

        Path pathName = Paths.get("src/day05/day05input.txt");
        List<String> lines = Files.readAllLines(pathName);
        seeds = Stream.of(lines.get(0).split(": ")[1].split(" ")).map(Long::parseLong).toList();

        setupMaps(lines);
        long result1 = seeds.stream().map(Day05::findLocationForSeed).min(Long::compareTo).orElseThrow();
        setupSeedRanges();
        long result2 = calculateSmallestLocationFromRanges();
        Instant finish = Instant.now();

        long executionTime = Duration.between(start, finish).toMillis();

        System.out.printf("Part one result: %d\n", result1);
        System.out.printf("Part two result: %d\n", result2);
        System.out.printf("Execution time: %d ms", executionTime);
    }
}
