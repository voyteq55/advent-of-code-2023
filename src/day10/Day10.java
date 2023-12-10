package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day10 {
    private static char[][] pipeField;
    private static Map<Character, Set<Integer>> directionChart;
    private static final Integer NORTH = 100;
    private static final Integer EAST = 200;
    private static final Integer WEST = -EAST;
    private static final Integer SOUTH = -NORTH;
    private static final char START = 'S';

    private static void setupPipeField() throws IOException {
        Path pathName = Paths.get("src/day10/day10input.txt");
        List<String> lines = Files.readAllLines(pathName);
        pipeField = new char[lines.size()][];
        for (int i = 0; i < pipeField.length; i++) {
            pipeField[i] = lines.get(i).toCharArray();
        }
    }

    private static void setupDirectionChart() {
        directionChart = new HashMap<>();
        directionChart.put('|', new HashSet<>(List.of(NORTH, SOUTH)));
        directionChart.put('-', new HashSet<>(List.of(WEST, EAST)));
        directionChart.put('L', new HashSet<>(List.of(NORTH, EAST)));
        directionChart.put('J', new HashSet<>(List.of(NORTH, WEST)));
        directionChart.put('F', new HashSet<>(List.of(SOUTH, EAST)));
        directionChart.put('7', new HashSet<>(List.of(SOUTH, WEST)));
        directionChart.put('.', new HashSet<>());
    }

    private static int[] getStartCoordinates() {
        for (int i = 0; i < pipeField.length; i++) {
            for (int j = 0; j < pipeField[i].length; j++) {
                if (pipeField[i][j] == START) return new int[]{i, j};
            }
        }
        return new int[]{-1, -1};
    }

    private static int getNextDirection(int currentDirection, char nextPipe) {
        Set<Integer> possibleDirections = directionChart.get(nextPipe);
        int reversedCurrentDirection = -currentDirection;
        for (int possibleDirection : possibleDirections) {
            if (possibleDirection != reversedCurrentDirection) {
                return possibleDirection;
            }
        }
        return 0;
    }

    private static int calculateLoopLength() {
        int[] startCoordinates = getStartCoordinates();
        int rowIndex = startCoordinates[0];
        int columnIndex = startCoordinates[1];
        int currentDirection = EAST;
        int numberOfSteps = 0;
        while (true) {
            numberOfSteps++;
            if (currentDirection == EAST) columnIndex++;
            if (currentDirection == WEST) columnIndex--;
            if (currentDirection == NORTH) rowIndex--;
            if (currentDirection == SOUTH) rowIndex++;

            char nextPipe = pipeField[rowIndex][columnIndex];
            if (nextPipe == START) {
                return numberOfSteps;
            }
            currentDirection = getNextDirection(currentDirection, nextPipe);
        }
    }

    public static void main(String[] args) throws IOException {
        setupPipeField();
        setupDirectionChart();
        int result1 = calculateLoopLength() / 2;
        System.out.printf("Part one solution: %d\n", result1);
    }
}
