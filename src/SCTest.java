import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SCTest {

    @Test
    void testStartMethod() {
        String simulatedInput = "engDictionary.txt\ninputFile.txt\na\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        SpellChecker spellChecker = new SpellChecker("engDictionary.txt");
        spellChecker.start();

        String output = outputStream.toString();
        assertTrue(output.contains("Spell checking for"), "Expected output to contain success message");
    }
}
