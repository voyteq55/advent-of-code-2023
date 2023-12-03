package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day03 {
    private record PartNumber(int rowIndex, int startColumnIndex, int endColumnIndex) {
        public int getNumber() {
            int number = 0;
            for (int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++) {
                number *= 10;
                number += Character.getNumericValue(engineSchematic[rowIndex][columnIndex]);
            }
            return number;
        }
    }

    private static class PartNumberList extends ArrayList<PartNumber> {
        private final PartNumber[][] partNumbersReferencesTable = new PartNumber[engineSchematic.length][engineSchematic[0].length];
        @Override
        public boolean add(PartNumber partNumber) {
            for (int i = partNumber.startColumnIndex; i <= partNumber.endColumnIndex; i++) {
                partNumbersReferencesTable[partNumber.rowIndex][i] = partNumber;
            }
            return super.add(partNumber);
        }

        public PartNumber getPartNumberAtCoordinates(int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex >= engineSchematic.length) {
                return null;
            }
            if (columnIndex < 0 || columnIndex >= engineSchematic[rowIndex].length) {
                return null;
            }
            return partNumbersReferencesTable[rowIndex][columnIndex];
        }
    }

    private static char[][] engineSchematic;
    private static PartNumberList partNumbersList;

    private static void setupEngineSchematic() throws IOException {
        Path filePath = Paths.get("src/day03/day03input.txt");
        List<String> lines = Files.readAllLines(filePath);
        engineSchematic = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            engineSchematic[i] = lines.get(i).toCharArray();
        }
    }

    private static void fillOutPartNumbersList() {
        partNumbersList = new PartNumberList();
        for (int rowIndex = 0; rowIndex < engineSchematic.length; rowIndex++) {
            boolean wasProcessingDigit = false;
            int startColumnIndex = 0, endColumnIndex = 0;
            for (int columnIndex = 0; columnIndex < engineSchematic[rowIndex].length; columnIndex++) {
                if (Character.isDigit(engineSchematic[rowIndex][columnIndex])) {
                    if (!wasProcessingDigit) {
                        startColumnIndex = columnIndex;
                    }
                    endColumnIndex = columnIndex;
                    wasProcessingDigit = true;
                } else {
                    if (wasProcessingDigit && isPartNumber(rowIndex, startColumnIndex, endColumnIndex)) {
                        partNumbersList.add(new PartNumber(rowIndex, startColumnIndex, endColumnIndex));
                    }
                    wasProcessingDigit = false;
                }
            }
            if (wasProcessingDigit && isPartNumber(rowIndex, startColumnIndex, endColumnIndex)) {
                partNumbersList.add(new PartNumber(rowIndex, startColumnIndex, endColumnIndex));
            }
        }
    }

    private static int calculatePartNumbersSum() {
        return partNumbersList.stream().mapToInt(PartNumber::getNumber).sum();
    }

    private static int calculateGearRatioSum() {
        int gearRatioSum = 0;
        for (int rowIndex = 0; rowIndex < engineSchematic.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < engineSchematic[rowIndex].length; columnIndex++) {
                gearRatioSum += gearRatioIfGear(rowIndex, columnIndex);
            }
        }

        return gearRatioSum;
    }

    private static boolean isPartNumber(int rowIndex, int startColumnIndex, int endColumnIndex) {
        for (int i = startColumnIndex - 1; i <= endColumnIndex + 1; i++) {
            if (isSymbol(rowIndex - 1, i) || isSymbol(rowIndex + 1, i)) {
                return true;
            }
        }
        return isSymbol(rowIndex, startColumnIndex - 1) || isSymbol(rowIndex, endColumnIndex + 1);
    }

    private static boolean isSymbol(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= engineSchematic.length) {
            return false;
        }
        if (columnIndex < 0 || columnIndex >= engineSchematic[rowIndex].length) {
            return false;
        }
        return !Character.isDigit(engineSchematic[rowIndex][columnIndex]) && engineSchematic[rowIndex][columnIndex] != '.';
    }

    private static int gearRatioIfGear(int rowIndex, int columnIndex) {
        if (engineSchematic[rowIndex][columnIndex] != '*') {
            return 0;
        }

        Set<PartNumber> adjacentPartNumbers = new HashSet<>();
        for (int row = rowIndex - 1; row <= rowIndex + 1; row++) {
            for (int column = columnIndex - 1; column <= columnIndex + 1; column++) {
                PartNumber partNumber = partNumbersList.getPartNumberAtCoordinates(row, column);
                if (partNumber != null) {
                    adjacentPartNumbers.add(partNumber);
                }
            }
        }
        if (adjacentPartNumbers.size() == 2) {
            return adjacentPartNumbers.stream().mapToInt(PartNumber::getNumber).reduce(1, (a, b) -> a * b);
        }
        return 0;
    }

    public static void main(String[] args) throws IOException {
        setupEngineSchematic();
        fillOutPartNumbersList();
        System.out.printf("Part 1 solution: %d\n", calculatePartNumbersSum());
        System.out.printf("Part 2 solution: %d\n", calculateGearRatioSum());
    }
}
