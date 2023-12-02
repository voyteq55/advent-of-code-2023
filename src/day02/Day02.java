package day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day02 {
    private static HashMap<String, Integer> minimumCubes;

    private static void setupPartOneMinimumCubesMap() {
        minimumCubes = new HashMap<>();
        minimumCubes.put("red", 12);
        minimumCubes.put("green", 13);
        minimumCubes.put("blue", 14);
    }
    private static void calculateSolutions() throws IOException {
        Path filePath = Paths.get("src/day02/day02input.txt");
        List<String> inputStrings = Files.readAllLines(filePath);
        int resultPartOne = inputStrings.stream().mapToInt(Day02::getIdOfPossibleGameOrZero).sum();
        int resultPartTwo = inputStrings.stream().mapToInt(Day02::getCubeSetPower).sum();
        System.out.printf("Part 1 solution: %d\n", resultPartOne);
        System.out.printf("Part 2 solution: %d\n", resultPartTwo);
    }

    private static List<Map<String, Integer>> getCurrentCubeSubsetMaps(String inputString) {
        String[] sets = inputString.split("[:;] ");
        List<Map<String, Integer>> subsetMaps = new ArrayList<>();

        for (int i = 1; i < sets.length; i++) {
            String[] oneColorCubeSubsets = sets[i].split(", ");
            HashMap<String, Integer> currentCubeSubsetsMap = new HashMap<>();
            Arrays.stream(oneColorCubeSubsets).map(s -> s.split(" ")).forEach(s -> currentCubeSubsetsMap.put(s[1], Integer.parseInt(s[0])));
            subsetMaps.add(currentCubeSubsetsMap);
        }

        return subsetMaps;
    }

    private static int getIdOfPossibleGameOrZero(String inputString) {
        for (Map<String, Integer> map : getCurrentCubeSubsetMaps(inputString)) {
            if (!isSubsetPossible(map)) return 0;
        }
        int gameId = Integer.parseInt(inputString.split(": ")[0].split(" ")[1]);
        return gameId;
    }

    private static boolean isSubsetPossible(Map<String, Integer> cubeSubsetsMap) {
        for (String color : cubeSubsetsMap.keySet()) {
            if (cubeSubsetsMap.get(color) > minimumCubes.get(color)) return false;
        }
        return true;
    }

    private static int getCubeSetPower(String inputString) {
        Map<String, Integer> minimalCubeSet = new HashMap<>();
        Arrays.stream(new String[]{"green", "blue", "red"}).forEach(s -> minimalCubeSet.put(s, 0));

        getCurrentCubeSubsetMaps(inputString).forEach(cubeSet -> cubeSet.keySet().forEach(color -> minimalCubeSet.put(color, Math.max(cubeSet.get(color), minimalCubeSet.get(color)))));
        int cubeSetPower = 1;
        for (String key : minimalCubeSet.keySet()) {
            cubeSetPower *= minimalCubeSet.get(key);
        }
        return cubeSetPower;
    }

    public static void main(String[] args) throws IOException{
        setupPartOneMinimumCubesMap();
        calculateSolutions();
    }
}
