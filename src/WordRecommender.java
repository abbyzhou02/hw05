import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordRecommender {
    private ArrayList<String> dictionary = new ArrayList<>();
    public WordRecommender(String dictionaryFile) {    
      // TODO: change this!
        try (FileInputStream inputStream = new FileInputStream(dictionaryFile);
             Scanner fileScanner = new Scanner(inputStream)) {
            while (fileScanner.hasNextLine()) {
                String word = fileScanner.nextLine().trim().toLowerCase();
                if (!dictionary.contains(word)) {
                    dictionary.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.printf(Util.FILE_OPENING_ERROR);
        } catch (IOException e) {
            System.out.printf(Util.FILE_OPENING_ERROR);
        }
    }
  
    public double getSimilarity(String word1, String word2) {
      // TODO: change this!
        int commonLetters = 0;
        int maxLen = Math.max(word1.length(), word2.length());

        for (int i = 0; i < Math.min(word1.length(), word2.length()); i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                commonLetters++;
            }
        }
        return (double) commonLetters / maxLen;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
        ArrayList<String> suggestions = new ArrayList<>();

        for (String dictWord : dictionary) {
            double similarity = getSimilarity(word, dictWord);
            if (similarity >= commonPercent) {
                suggestions.add(dictWord);
                if (suggestions.size() >= topN) {
                    break;  // Limit to top N suggestions
                }
            }
        }
        return suggestions;
    }
  
    
  }