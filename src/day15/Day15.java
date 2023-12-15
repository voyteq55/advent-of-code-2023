package day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15 {
    private static final char REMOVE_LENS_SYMBOL = '-';
    private static final String ADD_LENS_SYMBOL = "=";
    private List<List<Lens>> allBoxes;
    private class Lens {
        private final String label;
        private int focalLength;

        public Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        public String getLabel() {
            return label;
        }

        public int getFocalLength() {
            return focalLength;
        }

        public void setFocalLength(int focalLength) {
            this.focalLength = focalLength;
        }
    }

    private String[] getInputWords() throws IOException {
        Path pathName = Paths.get("src/day15/day15input.txt");
        String input = Files.readString(pathName);
        return input.strip().split(",");
    }

    private int hashAlgorithm(String word) {
        int currentValue = 0;
        for (char character : word.toCharArray()) {
            currentValue += character;
            currentValue *= 17;
            currentValue = currentValue % 256;
        }
        return currentValue;
    }

    private void setupBoxes() {
        allBoxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            allBoxes.add(new ArrayList<>());
        }
    }

    private boolean isLensPresentWithLabel(String lensLabel, int boxIndex) {
        for (Lens lens : allBoxes.get(boxIndex)) {
            if (lens.getLabel().equals(lensLabel)) {
                return true;
            }
        }
        return false;
    }

    private void addNewLens(String lensLabel, int boxIndex, int newFocalLength) {
        allBoxes.get(boxIndex).add(new Lens(lensLabel, newFocalLength));
    }

    private void replaceFocalLengthOfLens(String lensLabel, int boxIndex, int newFocalLength) {
        for (Lens lens : allBoxes.get(boxIndex)) {
            if (lens.getLabel().equals(lensLabel)) {
                lens.setFocalLength(newFocalLength);
                return;
            }
        }
    }

    private void assignLens(String lensLabel, int focalLength) {
        int boxIndex = hashAlgorithm(lensLabel);
        if (isLensPresentWithLabel(lensLabel, boxIndex)) {
            replaceFocalLengthOfLens(lensLabel, boxIndex, focalLength);
        } else {
            addNewLens(lensLabel, boxIndex, focalLength);
        }
    }

    private void removeLens(String lensLabel) {
        int boxIndex = hashAlgorithm(lensLabel);
        List<Lens> box = allBoxes.get(boxIndex);
        for (int i = 0; i < box.size(); i++) {
            if (box.get(i).getLabel().equals(lensLabel)) {
                allBoxes.get(boxIndex).remove(i);
                return;
            }
        }
    }

    private int calculateTotalFocusingPower() {
        int currentTotalFocusingPower = 0;
        for (int boxIndex = 0; boxIndex < allBoxes.size(); boxIndex++) {
            List<Lens> box = allBoxes.get(boxIndex);
            for (int lensIndex = 0; lensIndex < box.size(); lensIndex++) {
                currentTotalFocusingPower += (boxIndex + 1) * (lensIndex + 1) * box.get(lensIndex).getFocalLength();
            }
        }
        return currentTotalFocusingPower;
    }

    private void performLensArrangement(String[] words) {
        for (String word : words) {
            if (word.charAt(word.length() - 1) == REMOVE_LENS_SYMBOL) {
                removeLens(word.substring(0, word.length() - 1));
            } else {
                String[] wordParts = word.split(ADD_LENS_SYMBOL);
                assignLens(wordParts[0], Integer.parseInt(wordParts[1]));
            }
        }
    }

    private void start() throws IOException {
        String[] words = getInputWords();
        int result1 = Arrays.stream(words).mapToInt(this::hashAlgorithm).sum();
        System.out.printf("Part one solution: %d\n", result1);

        setupBoxes();
        performLensArrangement(words);
        System.out.printf("Part two solution: %d\n", calculateTotalFocusingPower());
    }

    public static void main(String[] args) throws IOException {
        (new Day15()).start();
    }
}
