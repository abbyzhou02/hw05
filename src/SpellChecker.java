import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {

    private WordRecommender wordRecommender;

    public SpellChecker() {
        Scanner scanner = new Scanner(System.in);
        File dictionaryFile = getFile(scanner, Util.DICTIONARY_PROMPT);
        this.wordRecommender = new WordRecommender(dictionaryFile.getAbsolutePath());
    }

    public SpellChecker(String dictionaryFile) {
        this.wordRecommender = new WordRecommender(dictionaryFile);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        File fileToCheck = getFile(scanner, Util.FILENAME_PROMPT);
        if (fileToCheck == null) return;

        String outputFileName = fileToCheck.getName().replace(".txt", "_chk.txt");

        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            Scanner fileScanner = new Scanner(fileToCheck);

            while (fileScanner.hasNext()) {
                String word = fileScanner.next();

                if (!wordRecommender.isWordInDictionary(word)) {
                    ArrayList<String> suggestions = wordRecommender.getWordSuggestions(word, 2, 0.5, 4);
                    String replacement = getReplacement(scanner, word, suggestions);
                    writer.print(replacement + " ");
                } else {
                    writer.print(word + " ");
                }
            }

            fileScanner.close();
            System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, fileToCheck.getName(), outputFileName);
        } catch (FileNotFoundException e) {
            System.out.printf("Error in writing output file.");
        }
    }

    private File getFile(Scanner scanner, String prompt) {
        while (true) {
            System.out.printf(prompt);
            String filename = scanner.nextLine();
            File file = new File(filename);
            if (file.exists() && !file.isDirectory()) {
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, filename);
                return file;
            } else {
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }

    private String getReplacement(Scanner scanner, String word, ArrayList<String> suggestions) {
        System.out.printf(Util.MISSPELL_NOTIFICATION, word);

        if (suggestions.isEmpty()) {
            System.out.printf(Util.NO_SUGGESTIONS);
            System.out.printf(Util.TWO_OPTION_PROMPT);
            while (true) {
                String option = scanner.nextLine();
                if (option.equals("a")) {
                    return word;
                } else if (option.equals("t")) {
                    System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                    return scanner.nextLine();
                } else {
                    System.out.printf(Util.INVALID_RESPONSE);
                }
            }
        } else {
            System.out.printf(Util.FOLLOWING_SUGGESTIONS);
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestions.get(i));
            }
            System.out.printf(Util.THREE_OPTION_PROMPT);

            while (true) {
                String option = scanner.nextLine();
                if (option.equals("r")) {
                    System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice > 0 && choice <= suggestions.size()) {
                        return suggestions.get(choice - 1);
                    } else {
                        System.out.printf(Util.INVALID_RESPONSE);
                    }
                } else if (option.equals("a")) {
                    return word;
                } else if (option.equals("t")) {
                    System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                    return scanner.nextLine();
                } else {
                    System.out.printf(Util.INVALID_RESPONSE);
                }
            }
        }
    }
}
