package day07;

import java.util.Comparator;
import java.util.HashMap;

public class CamelHand {
    private static final int HIGH_CARD = 0, PAIR = 1, TWO_PAIRS = 2, THREE = 3, FULL_HOUSE = 4, FOUR = 5, FIVE = 6;
    private final String hand;
    private final int bid;
    private final int handStrength;
    private final int handStrengthWithJokers;
    private static final HashMap<Character, Integer> cardStrengths;
    private static final HashMap<Character, Integer> cardStrengthsWithJokers;

    static {
        cardStrengths = new HashMap<>();
        for (char c = '0'; c <= '9'; c++) {
            cardStrengths.put(c, c - '0');
        }
        cardStrengths.put('T', 10);
        cardStrengths.put('J', 11);
        cardStrengths.put('Q', 12);
        cardStrengths.put('K', 13);
        cardStrengths.put('A', 14);

        cardStrengthsWithJokers = new HashMap<>(cardStrengths);
        cardStrengthsWithJokers.put('J', 0);
    }

    CamelHand(String hand, int bid) {
        this.hand = hand;
        this.bid = bid;
        handStrength = initializeStrength(getCardOccurrences());
        handStrengthWithJokers = initializeStrength(getCardOccurrencesWithJokers());
    }

    @Override
    public String toString() {
        return hand + " " + bid;
    }

    public int getHandStrength() {
        return handStrength;
    }

    public int getBid() {
        return bid;
    }

    private int initializeStrength(HashMap<Character, Integer> cardOccurrences) {
        if (cardOccurrences.size() == 5) return HIGH_CARD;
        if (cardOccurrences.size() == 4) return PAIR;
        if (cardOccurrences.size() == 3) {
            if (cardOccurrences.containsValue(3)) return THREE;
            return TWO_PAIRS;
        }
        if (cardOccurrences.size() == 2) {
            if (cardOccurrences.containsValue(4)) return FOUR;
            return FULL_HOUSE;
        }
        return FIVE;
    }

    private HashMap<Character, Integer> getCardOccurrences() {
        HashMap<Character, Integer> cardOccurrences = new HashMap<>();
        for (char card : hand.toCharArray()) {
            if (cardOccurrences.containsKey(card)) {
                cardOccurrences.put(card, cardOccurrences.get(card) + 1);
            } else {
                cardOccurrences.put(card, 1);
            }
        }
        return cardOccurrences;
    }

    private HashMap<Character, Integer> getCardOccurrencesWithJokers() {
        HashMap<Character, Integer> cardOccurrences = getCardOccurrences();
        if (cardOccurrences.containsKey('J')) {
            int numberOfJokers = cardOccurrences.get('J');
            cardOccurrences.remove('J');
            char mostFrequentCard = ' ';
            int occurrencesOfMostFrequentCard = 0;
            for (char card : cardOccurrences.keySet()) {
                if (cardOccurrences.get(card) > occurrencesOfMostFrequentCard) {
                    mostFrequentCard = card;
                    occurrencesOfMostFrequentCard = cardOccurrences.get(card);
                }
            }
            cardOccurrences.put(mostFrequentCard, occurrencesOfMostFrequentCard + numberOfJokers);
        }
        return cardOccurrences;
    }

    static class StandardHandComparator implements Comparator<CamelHand> {
        @Override
        public int compare(CamelHand hand1, CamelHand hand2) {
            if (hand1.handStrength > hand2.handStrength) return 1;
            if (hand1.handStrength < hand2.handStrength) return -1;
            return compareEqualTypeHands(hand1, hand2, cardStrengths);
        }
    }

    static class JokerHandComparator implements Comparator<CamelHand> {
        @Override
        public int compare(CamelHand hand1, CamelHand hand2) {
            if (hand1.handStrengthWithJokers > hand2.handStrengthWithJokers) return 1;
            if (hand1.handStrengthWithJokers < hand2.handStrengthWithJokers) return -1;
            return compareEqualTypeHands(hand1, hand2, cardStrengthsWithJokers);
        }
    }

    private static int compareEqualTypeHands(CamelHand hand1, CamelHand hand2, HashMap<Character, Integer> cardStrengthsMap) {
        for (int i = 0; i < hand1.hand.length(); i++) {
            if (cardStrengthsMap.get(hand1.hand.charAt(i)) > cardStrengthsMap.get(hand2.hand.charAt(i))) return 1;
            if (cardStrengthsMap.get(hand1.hand.charAt(i)) < cardStrengthsMap.get(hand2.hand.charAt(i))) return -1;
        }
        return 0;
    }

}
