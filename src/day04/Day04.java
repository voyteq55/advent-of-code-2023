package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day04 {
    private static long numberOfMatchingCards(String line) {
        String[] parts = line.split(" *[:|] +");
        HashSet<String> winningNumbers = new HashSet<>(List.of(parts[1].split(" +")));
        return Arrays.stream(parts[2].split(" +")).filter(winningNumbers::contains).count();
    }
    private static int valueOfScratchcard(String line) {
        return (int) Math.pow(2, numberOfMatchingCards(line) - 1);
    }

    private static int numberOfScratchcards(List<String> lines){
        List<Integer> copiesOfScratchcard = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            copiesOfScratchcard.add(1);
        }
        for (int i = 0; i < copiesOfScratchcard.size(); i++) {
            long matchingScratchcards = numberOfMatchingCards(lines.get(i));
            for (int j = i + 1; j <= i + matchingScratchcards && j < copiesOfScratchcard.size(); j++) {
                copiesOfScratchcard.set(j, copiesOfScratchcard.get(j) + copiesOfScratchcard.get(i));
            }
        }
        return copiesOfScratchcard.stream().reduce(0, Integer::sum);
    }

    public static void main(String[] args) throws IOException {
        Path filePath = Paths.get("src/day04/day04input.txt");
        List<String> lines = Files.readAllLines(filePath);
        int result1 = lines.stream().mapToInt(Day04::valueOfScratchcard).sum();
        int result2 = numberOfScratchcards(lines);
        System.out.printf("Part one result: %d\n", result1);
        System.out.printf("Part two result: %d\n", result2);
    }
}
