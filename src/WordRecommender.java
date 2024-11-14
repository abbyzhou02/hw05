import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WordRecommender {
    private ArrayList<String> dictionary = new ArrayList<>();

    public WordRecommender(String dictionaryFile) {
        try {
            FileInputStream inputStream = new FileInputStream(dictionaryFile);
            Scanner fileScanner = new Scanner(inputStream);
            while (fileScanner.hasNextLine()) {
                String word = fileScanner.nextLine().trim().toLowerCase();
                if (!dictionary.contains(word)) {
                    dictionary.add(word);
                }
            }
            fileScanner.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.printf(Util.FILE_OPENING_ERROR);
        }
    }
    public boolean isWordInDictionary(String word) {
        return dictionary.contains(word);
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList<String> suggestions = new ArrayList<>();

        for (String potential : dictionary) {
            if (Math.abs(potential.length() - word.length()) <= tolerance &&
                    calculateCommonPercent(word, potential) >= commonPercent) {
                insertSuggestionBySimilarity(suggestions, potential, word, topN);
            }
        }
        return suggestions;
    }

    private void insertSuggestionBySimilarity(ArrayList<String> suggestions, String candidate, String word, int topN) {
        double similarity = getSimilarity(word, candidate);

        for (int i = 0; i < suggestions.size(); i++) {
            if (getSimilarity(word, suggestions.get(i)) < similarity) {
                suggestions.add(i, candidate);
                if (suggestions.size() > topN) {
                    suggestions.remove(suggestions.size() - 1);
                }
                return;
            }
        }

        if (suggestions.size() < topN) {
            suggestions.add(candidate);
        }
    }

    public double calculateCommonPercent(String word1, String word2) {
        int commonCount = 0;
        int uniqueCount = 0;

        for (int i = 0; i < word1.length(); i++) {
            char c1 = word1.charAt(i);
            if (word1.indexOf(c1) == i) {
                uniqueCount++;
            }
        }

        for (int i = 0; i < word2.length(); i++) {
            char c2 = word2.charAt(i);
            if (word1.indexOf(c2) != -1 && word2.indexOf(c2) == i) {
                commonCount++;
            }
            if (word1.indexOf(c2) == -1 && word2.indexOf(c2) == i) {
                uniqueCount++;
            }
        }

        return (double) commonCount / uniqueCount;
    }

    public double getSimilarity(String word1, String word2) {
        int leftSimilarity = calculateLeftSimilarity(word1, word2);
        int rightSimilarity = calculateRightSimilarity(word1, word2);
        return (leftSimilarity + rightSimilarity) / 2.0;
    }

    private int calculateLeftSimilarity(String word1, String word2) {
        int minLen = Math.min(word1.length(), word2.length());
        int similarity = 0;

        for (int i = 0; i < minLen; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                similarity++;
            }
        }
        return similarity;
    }


    private int calculateRightSimilarity(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int similarity = 0;

        for (int i = 1; i <= Math.min(len1, len2); i++) {
            if (word1.charAt(len1 - i) == word2.charAt(len2 - i)) {
                similarity++;
            }
        }
        return similarity;
    }
}
