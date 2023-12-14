package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day14 {
    private static final char ROUNDED_ROCK = 'O';
    private static final char EMPTY_SPACE = '.';
    private static final long NUMBER_OF_CYCLES = 1000000000L;
    private static char[][] platform;

    private static void setupPlatform() throws IOException {
        Path pathName = Paths.get("src/day14/day14input.txt");
        List<String> lines = Files.readAllLines(pathName);
        platform = new char[lines.size()][];
        for (int i = 0; i < platform.length; i++) {
            platform[i] = lines.get(i).toCharArray();
        }
    }

    private static void tiltPlatformNorth() {
        for (int rowIndex = 1; rowIndex < platform.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < platform[rowIndex].length; columnIndex++) {
                moveRockNorth(rowIndex, columnIndex);
            }
        }
    }

    private static void tiltPlatformSouth() {
        for (int rowIndex = platform.length - 2; rowIndex >= 0; rowIndex--) {
            for (int columnIndex = 0; columnIndex < platform[rowIndex].length; columnIndex++) {
                moveRockSouth(rowIndex, columnIndex);
            }
        }
    }

    private static void tiltPlatformWest() {
        for (int columnIndex = 1; columnIndex < platform[0].length; columnIndex++) {
            for (int rowIndex = 0; rowIndex < platform.length; rowIndex++) {
                moveRockWest(rowIndex, columnIndex);
            }
        }
    }

    private static void tiltPlatformEast() {
        for (int columnIndex = platform[0].length - 2; columnIndex >= 0; columnIndex--) {
            for (int rowIndex = 0; rowIndex < platform.length; rowIndex++) {
                moveRockEast(rowIndex, columnIndex);
            }
        }
    }

    private static long calculateTotalLoad() {
        long calculatedTotalLoad = 0;
        int heightOfPlatform = platform.length;
        for (int rowIndex = 0; rowIndex < platform.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < platform[rowIndex].length; columnIndex++) {
                if (platform[rowIndex][columnIndex] == ROUNDED_ROCK) {
                    calculatedTotalLoad += heightOfPlatform - rowIndex;
                }
            }
        }
        return calculatedTotalLoad;
    }

    private static void moveRockNorth(int rowIndex, int columnIndex) {
        if (platform[rowIndex][columnIndex] != ROUNDED_ROCK) {
            return;
        }
        while (rowIndex >= 1 && platform[rowIndex - 1][columnIndex] == EMPTY_SPACE) {
            platform[rowIndex - 1][columnIndex] = platform[rowIndex][columnIndex];
            platform[rowIndex][columnIndex] = EMPTY_SPACE;
            rowIndex--;
        }
    }


    private static void moveRockSouth(int rowIndex, int columnIndex) {
        if (platform[rowIndex][columnIndex] != ROUNDED_ROCK) {
            return;
        }
        while (rowIndex < platform.length - 1 && platform[rowIndex + 1][columnIndex] == EMPTY_SPACE) {
            platform[rowIndex + 1][columnIndex] = platform[rowIndex][columnIndex];
            platform[rowIndex][columnIndex] = EMPTY_SPACE;
            rowIndex++;
        }
    }

    private static void moveRockWest(int rowIndex, int columnIndex) {
        if (platform[rowIndex][columnIndex] != ROUNDED_ROCK) {
            return;
        }
        while (columnIndex >= 1 && platform[rowIndex][columnIndex - 1] == EMPTY_SPACE) {
            platform[rowIndex][columnIndex - 1] = platform[rowIndex][columnIndex];
            platform[rowIndex][columnIndex] = EMPTY_SPACE;
            columnIndex--;
        }
    }

    private static void moveRockEast(int rowIndex, int columnIndex) {
        if (platform[rowIndex][columnIndex] != ROUNDED_ROCK) {
            return;
        }
        while (columnIndex < platform[rowIndex].length - 1 && platform[rowIndex][columnIndex + 1] == EMPTY_SPACE) {
            platform[rowIndex][columnIndex + 1] = platform[rowIndex][columnIndex];
            platform[rowIndex][columnIndex] = EMPTY_SPACE;
            columnIndex++;
        }
    }

    private static void makeTiltCycle() {
        tiltPlatformNorth();
        tiltPlatformWest();
        tiltPlatformSouth();
        tiltPlatformEast();
    }

    public static void main(String[] args) throws IOException {
        setupPlatform();
        tiltPlatformNorth();

        System.out.printf("Part one solution: %d\n", calculateTotalLoad());

        setupPlatform();
        for (int i = 1; i <= 200; i++) {
            makeTiltCycle();
            System.out.printf("%d: %d, ", i, calculateTotalLoad());
        }
        System.out.printf("\n%d mod 36: %d", NUMBER_OF_CYCLES, NUMBER_OF_CYCLES % 36);

        /*
        When analyzing the output after each cycle, it can bee seen that after about 100 cycles there is a recurring
        pattern which repeats every 36 cycles (at least for my input data). After calculating modulo 36 of one billion
        (1000000000), there can be found a different cycle number with the same value of modulo 36. Assuming that the
        repetitiveness of the pattern continues, the total load after both of those cycles is the same.
         */
    }
}
