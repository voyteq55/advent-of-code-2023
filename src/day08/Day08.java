package day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class Day08 {
    private static char[] directions;
    private static HashMap<String, String[]> nodeGraph;

    private static void setupNodeGraph(List<String> lines) {
        List<String> inputLines = new ArrayList<>(lines);
        inputLines.remove(0);
        inputLines.remove(0);

        nodeGraph = new HashMap<>();
        for (String line : inputLines) {
            String[] lineParts = line.split(" = \\(|, |\\)");
            nodeGraph.put(lineParts[0], new String[]{lineParts[1], lineParts[2]});
        }
    }

    private static long calculateNumberOfSteps(String start, Predicate<String> goalCondition) {
        long currentStepNumber = 0;
        String currentElement = start;
        boolean foundGoal = false;
        while (!foundGoal) {
            currentElement = getNextElement(currentElement, currentStepNumber);
            if (goalCondition.test(currentElement)) {
                foundGoal = true;
            }
            currentStepNumber++;
        }
        return currentStepNumber;
    }

    private static long calculateNumberOfGhostSteps() {
        List<String> startElements = nodeGraph.keySet().stream().filter(n -> n.charAt(2) == 'A').toList();
        return startElements.stream()
                .mapToLong(startElement -> calculateNumberOfSteps(startElement, (s) -> s.charAt(2) == 'Z'))
                .reduce(1, Day08::leastCommonMultiplier);
    }

    private static long leastCommonMultiplier(long number1, long number2) {
        return (number1 * number2) / greatestCommonDivisor(number1, number2);
    }

    private static long greatestCommonDivisor(long number1, long number2) {
        while (number2 > 0) {
            long temp = number2;
            number2 = number1 % number2;
            number1 = temp;
        }
        return number1;
    }

    private static String getNextElement(String currentElement, long currentStepNumber) {
        char currentDirection = directions[(int) (currentStepNumber % directions.length)];
        if (currentDirection == 'L') {
            return nodeGraph.get(currentElement)[0];
        } else if (currentDirection == 'R') {
            return nodeGraph.get(currentElement)[1];
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Path pathName = Paths.get("src/day08/day08input.txt");
        List<String> lines = Files.readAllLines(pathName);

        directions = lines.get(0).toCharArray();
        setupNodeGraph(lines);

        long result1 = calculateNumberOfSteps("AAA", (s) -> s.equals("ZZZ"));
        long result2 = calculateNumberOfGhostSteps();

        System.out.printf("Part one solution: %d\n", result1);
        System.out.printf("Part two solution: %d\n", result2);
    }
}
