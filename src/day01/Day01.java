package day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 {
    private static final HashMap<String, Integer> stringToDigitMap = new HashMap<>();
    public static void calculateCalibrationNumberSum() {
        Path filePath = Paths.get("src/day01/day01input.txt");
        try {
            List<String> inputStrings = Files.readAllLines(filePath);
            int resultPartOne = inputStrings.stream().mapToInt(Day01::getCalibrationNumberPart1).sum();
            int resultPartTwo = inputStrings.stream().mapToInt(Day01::getCalibrationNumberPart2).sum();
            System.out.printf("Part 1 solution: %d\n", resultPartOne);
            System.out.printf("Part 2 solution: %d\n", resultPartTwo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getCalibrationNumberPart1(String inputString) {
        int result = 0, startIndex = 0, endIndex = inputString.length() - 1;
        boolean foundFirstDigit = false, foundSecondDigit = false;
        while (!foundFirstDigit && startIndex < inputString.length()) {
            if (Character.isDigit(inputString.charAt(startIndex))) {
                result = 10 * Character.getNumericValue(inputString.charAt(startIndex));
                foundFirstDigit = true;
            }
            startIndex++;
        }

        while (!foundSecondDigit && endIndex >= 0) {
            if (Character.isDigit(inputString.charAt(endIndex))) {
                result += Character.getNumericValue(inputString.charAt(endIndex));
                foundSecondDigit = true;
            }
            endIndex--;
        }

        return result;
    }

    private static int getCalibrationNumberPart2(String inputString) {
        Pattern pattern = Pattern.compile("([0-9]|one|two|three|four|five|six|seven|eight|nine)");
        Matcher matcher = pattern.matcher(inputString);
        String firstDigitString = "", lastDigitString = "";
        if (matcher.find()) {
            firstDigitString = matcher.group();
            lastDigitString = firstDigitString;
            while (matcher.find(matcher.start() + 1)) {
                lastDigitString = matcher.group();
            }
        }
        int firstDigit = stringToDigitMap.get(firstDigitString);
        int lastDigit = stringToDigitMap.get(lastDigitString);

        return 10 * firstDigit + lastDigit;
    }

    private static void setupStringToDigitMap() {
        for (int i = 0; i < 10; i++) {
            stringToDigitMap.put(String.valueOf(i), i);
        }
        stringToDigitMap.put("one", 1);
        stringToDigitMap.put("two", 2);
        stringToDigitMap.put("three", 3);
        stringToDigitMap.put("four", 4);
        stringToDigitMap.put("five", 5);
        stringToDigitMap.put("six", 6);
        stringToDigitMap.put("seven", 7);
        stringToDigitMap.put("eight", 8);
        stringToDigitMap.put("nine", 9);
    }

    public static void main(String[] args) {
        setupStringToDigitMap();
        calculateCalibrationNumberSum();
    }
}
