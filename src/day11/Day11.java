package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 {
    private static final char GALAXY = '#';
    private static final char EMPTY_SPACE = '.';
    private static final long EMPTY_SPACE_EXPANSION_RATE = 1000000; // MODIFY THIS TO '2' FOR PART ONE SOLUTION
    private static List<List<Character>> universe;
    private static HashSet<Integer> emptyRowsIndexes;
    private static HashSet<Integer> emptyColumnsIndexes;
    private static List<int[]> galaxiesPositions;

    private static void setupGalaxy() throws IOException {
        Path pathName = Paths.get("src/day11/day11input.txt");
        List<String> lines = Files.readAllLines(pathName);
        universe = new ArrayList<>();
        for (String line : lines) {
            ArrayList<Character> galaxyLine = new ArrayList<>();
            for (char c : line.toCharArray()) {
                galaxyLine.add(c);
            }
            universe.add(galaxyLine);
        }
    }

    private static void markEmptyRowsAndColumns() {
        emptyRowsIndexes = new HashSet<>();
        emptyColumnsIndexes = new HashSet<>();
        for (int rowIndex = 0; rowIndex < universe.size(); rowIndex++) {
            if (universe.get(rowIndex).stream().allMatch(c -> c == EMPTY_SPACE)) {
                emptyRowsIndexes.add(rowIndex);
            }
        }
        for (int columnIndex = 0; columnIndex < universe.get(0).size(); columnIndex++) {
            if (isColumnEmpty(columnIndex)) {
                emptyColumnsIndexes.add(columnIndex);
            }
        }
    }

    private static boolean isColumnEmpty(int columnIndex) {
        for (List<Character> row : universe) {
            if (row.get(columnIndex) == GALAXY) {
                return false;
            }
        }
        return true;
    }

    private static void setupGalaxiesPositions() {
        galaxiesPositions = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < universe.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < universe.get(0).size(); columnIndex++) {
                if (universe.get(rowIndex).get(columnIndex) == GALAXY) {
                    galaxiesPositions.add(new int[]{rowIndex, columnIndex});
                }
            }
        }
    }

    private static long calculateGalaxiesDistancesSum() {
        long accumulatedDistancesSum = 0;
        for (int i = 0; i < galaxiesPositions.size(); i++) {
            for (int j = i + 1; j < galaxiesPositions.size(); j++) {
                accumulatedDistancesSum += calculateDistanceBetweenGalaxies(galaxiesPositions.get(i), galaxiesPositions.get(j));
            }
        }
        return accumulatedDistancesSum;
    }

    private static long getDistanceInOneDimension(int smallerIndex, int greaterIndex, Set<Integer> emptySpaceIndexes) {
        long accumulatedDistance = 0;
        for (int currentIndex = smallerIndex; currentIndex < greaterIndex; currentIndex++) {
            if (emptySpaceIndexes.contains(currentIndex)) {
                accumulatedDistance += EMPTY_SPACE_EXPANSION_RATE;
            } else {
                accumulatedDistance += 1;
            }
        }
        return accumulatedDistance;
    }

    private static long getVerticalDistance(int smallerRowIndex, int greaterRowIndex) {
        return getDistanceInOneDimension(smallerRowIndex, greaterRowIndex, emptyRowsIndexes);
    }

    private static long getHorizontalDistance(int smallerColumnIndex, int greaterColumnIndex) {
        return getDistanceInOneDimension(smallerColumnIndex, greaterColumnIndex, emptyColumnsIndexes);
    }

    private static long calculateDistanceBetweenGalaxies(int[] galaxy1, int[] galaxy2) {
        int smallerRowIndex = Math.min(galaxy1[0], galaxy2[0]);
        int greaterRowIndex = Math.max(galaxy1[0], galaxy2[0]);
        int smallerColumnIndex = Math.min(galaxy1[1], galaxy2[1]);
        int greaterColumnIndex = Math.max(galaxy1[1], galaxy2[1]);

        return getVerticalDistance(smallerRowIndex, greaterRowIndex) + getHorizontalDistance(smallerColumnIndex, greaterColumnIndex);
    }

    public static void main(String[] args) throws IOException {
        setupGalaxy();
        markEmptyRowsAndColumns();
        setupGalaxiesPositions();

        long result = calculateGalaxiesDistancesSum();
        System.out.printf("Solution for empty space expansion rate equal to %d: %d\n", EMPTY_SPACE_EXPANSION_RATE, result);
    }

}
