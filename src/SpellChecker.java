import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {

    private WordRecommender wordRecommender;

    // No-argument constructor that prompts for the dictionary file
    public SpellChecker() {
        Scanner scanner = new Scanner(System.in);
        File dictionaryFile = getFile(scanner, Util.DICTIONARY_PROMPT);
        wordRecommender = new WordRecommender(dictionaryFile.getAbsolutePath());
    }

    public SpellChecker(String dictionaryFile) {
        wordRecommender = new WordRecommender(dictionaryFile);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Prompt for spellcheck file
        File fileToCheck = getFile(scanner, Util.FILENAME_PROMPT);
        if (fileToCheck == null) return;

        // Step 2: Output file name
        String outputFileName = fileToCheck.getName().replace(".txt", "_chk.txt");

        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            Scanner fileScanner = new Scanner(fileToCheck);

            // Step 3: Process file word by word
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
            System.out.println(Util.FILE_SUCCESS_NOTIFICATION);
        } catch (FileNotFoundException e) {
            System.out.println("Error in writing output file.");
        }
    }

    private File getFile(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String filename = scanner.nextLine();
            File file = new File(filename);
            if (file.exists() && !file.isDirectory()) {
                System.out.println(Util.DICTIONARY_SUCCESS_NOTIFICATION);
                return file;
            } else {
                System.out.println(Util.FILE_OPENING_ERROR);
            }
        }
    }

    private String getReplacement(Scanner scanner, String word, ArrayList<String> suggestions) {
        System.out.printf(Util.MISSPELL_NOTIFICATION);
        for (int i = 0; i < suggestions.size(); i++) {
            System.out.println(Util.FOLLOWING_SUGGESTIONS);
            System.out.println(Util.SUGGESTION_ENTRY);
        }
        System.out.println(Util.THREE_OPTION_PROMPT);

        while (true) {
            String option = scanner.nextLine();
            if (option.equals("r")) {
                System.out.println(Util.AUTOMATIC_REPLACEMENT_PROMPT);
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice > 0 && choice <= suggestions.size()) {
                    return suggestions.get(choice - 1);
                } else {
                    System.out.println("Invalid choice.");
                }
            } else if (option.equals("a")) {
                return word;
            } else if (option.equals("t")) {
                System.out.println(Util.MANUAL_REPLACEMENT_PROMPT);
                return scanner.nextLine();
            } else {
                System.out.println(Util.INVALID_RESPONSE);
            }
        }
    }
}
