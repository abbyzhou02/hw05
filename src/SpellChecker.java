import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class SpellChecker {
    private static ArrayList<String> dictionary = new ArrayList<>();

    public SpellChecker() {
      // TODO: You can modify the body of this constructor,
      // or you can leave it blank. You must keep the signature, however.
    }
  
    public static void start() {
      // TODO: You can modify the body of this method,
      // or you can leave it blank. You must keep the signature, however.
        Scanner scanner = new Scanner (System.in);

        try {
            String dictionaryFileName = promptForDictionary(scanner);
            System.out.printf(Util.FILENAME_PROMPT);
            String textFileName = scanner.nextLine();
            checkSpelling(textFileName);
        } catch (IOException e) {
            System.out.printf(Util.FILE_OPENING_ERROR);
        } finally {
            scanner.close();
        }
    }

    private static String promptForDictionary(Scanner scanner) throws FileNotFoundException {
        while (true) {
            System.out.printf(Util.DICTIONARY_PROMPT);
            String fileName = scanner.nextLine();

            FileInputStream inputStream = new FileInputStream(fileName);
            System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, fileName);
            try {
                loadDictionary(inputStream);
            } catch (IOException e) {
                System.out.printf(Util.FILE_OPENING_ERROR);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Failed to close the file stream.");
                }
            }
            return fileName;
        }
    }

    private static void loadDictionary(FileInputStream inputStream) throws IOException {
        Scanner fileScanner = new Scanner(inputStream);
        while (fileScanner.hasNextLine()) {
            String word = fileScanner.nextLine().trim().toLowerCase();
            if (!dictionary.contains(word)) {
                dictionary.add(word);
            }
        }
        fileScanner.close();
    }
    private static void checkSpelling(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileName);
        Scanner fileScanner = new Scanner(inputStream);

        System.out.println("Misspelled words:");
        while (fileScanner.hasNextLine()) {
            String[] words = fileScanner.nextLine().toLowerCase().split(" ");
            for (String word : words) {
                if (!dictionary.contains(word)) {
                    System.out.printf(Util.MISSPELL_NOTIFICATION, word);
                }
            }
        }

        fileScanner.close();
        inputStream.close();
    }

    public static boolean isWordInDictionary(String word) {
        return dictionary.contains(word);
    }
    // You can of course write other methods as well.
  }