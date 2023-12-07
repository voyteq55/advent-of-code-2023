package day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day07 {
    private static List<CamelHand> handList;

    private static void setupCamelHands(List<String> lines) {
        handList = new ArrayList<>();
        for (String line : lines) {
            String[] lineArgs = line.split(" ");
            handList.add(new CamelHand(lineArgs[0], Integer.parseInt(lineArgs[1])));
        }
    }

    private static int calculateTotalWinnings(Comparator<CamelHand> comparator) {
        int totalWinnings = 0;
        handList.sort(comparator);
        for (int i = 0; i < handList.size(); i++) {
            totalWinnings += handList.get(i).getBid() * (i + 1);
        }
        return totalWinnings;
    }

    public static void main(String[] args) throws IOException {
        Path pathName = Paths.get("src/day07/day07input.txt");
        List<String> lines = Files.readAllLines(pathName);

        setupCamelHands(lines);

        int result1 = calculateTotalWinnings(new CamelHand.StandardHandComparator());
        int result2 = calculateTotalWinnings(new CamelHand.JokerHandComparator());

        System.out.printf("Part one solution: %d\n", result1);
        System.out.printf("Part two solution: %d\n", result2);
    }

}
