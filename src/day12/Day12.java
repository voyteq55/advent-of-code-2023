package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {
    private static final char BROKEN = '#';
    private static final char UNKNOWN = '?';
    private static List<String> rows;
    private static List<List<Integer>> brokenGroupsList;

    private static void setupInput() throws IOException {
        Path pathName = Paths.get("src/day12/day12input.txt");
        List<String> lines = Files.readAllLines(pathName);

        rows = new ArrayList<>();
        brokenGroupsList = new ArrayList<>();
        for (String line : lines) {
            String[] lineParts = line.split(" ");
            rows.add(lineParts[0]);
            brokenGroupsList.add(Arrays.stream(lineParts[1].split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
        }
    }

    private static int calculatePossibleRowArrangements(String row, List<Integer> brokenGroups) {
        int foundPossibleRowArrangements = 0;
        List<Integer> indexesOfUnknownSprings = getIndexesOfUnknownSprings(row);
        int possibleVariations = (int) Math.pow(2, indexesOfUnknownSprings.size());

        for (int i = 0; i < possibleVariations; i++) {
            String currentNewRow = getPotentialRowVariation(row, i, indexesOfUnknownSprings);
            if (getBrokenGroupsFromRow(currentNewRow).equals(brokenGroups)) {
                foundPossibleRowArrangements++;
            }
        }
        return foundPossibleRowArrangements;
    }

    private static List<Integer> getIndexesOfUnknownSprings(String row) {
        List<Integer> indexesOfUnknownSprings = new ArrayList<>();
        for (int i = 0; i < row.length(); i++) {
            if (row.charAt(i) == UNKNOWN) {
                indexesOfUnknownSprings.add(i);
            }
        }
        return indexesOfUnknownSprings;
    }

    private static String getPotentialRowVariation(String row, int variationIndex, List<Integer> indexesOfUnknownSprings) {
        StringBuilder currentNewRow = new StringBuilder(row);
        for (Integer indexOfUnknownSpring : indexesOfUnknownSprings) {
            if (variationIndex % 2 == 0) {
                currentNewRow.setCharAt(indexOfUnknownSpring, BROKEN);
            }
            variationIndex = variationIndex / 2;
        }
        return currentNewRow.toString();
    }

    private static List<Integer> getBrokenGroupsFromRow(String row) {
        ArrayList<Integer> brokenGroups = new ArrayList<>();
        int currentGroupLength = 0;
        boolean isProcessingBrokenGroup = false;
        for (char spring : row.toCharArray()) {
            if (spring == BROKEN) {
                currentGroupLength++;
                isProcessingBrokenGroup = true;
            } else if (isProcessingBrokenGroup) {
                brokenGroups.add(currentGroupLength);
                currentGroupLength = 0;
                isProcessingBrokenGroup = false;
            }
        }
        if (isProcessingBrokenGroup) {
            brokenGroups.add(currentGroupLength);
        }
        return brokenGroups;
    }

    public static void main(String[] args) throws IOException {
        setupInput();

        int result1 = 0;
        for (int i = 0; i < rows.size(); i++) {
            result1 += calculatePossibleRowArrangements(rows.get(i), brokenGroupsList.get(i));
        }
        System.out.printf("Part one solution: %d\n", result1);
    }
}
