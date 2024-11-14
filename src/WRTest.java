import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WRTest {

    @Test
    void testIsWordInDictionary() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        assertTrue(recommender.isWordInDictionary("example"), "The word 'example' should be in the dictionary");
        assertFalse(recommender.isWordInDictionary("nonexistentword"), "The word 'nonexistentword' should not be in the dictionary");
    }

    @Test
    void testGetWordSuggestions() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        ArrayList<String> suggestions = recommender.getWordSuggestions("morbit", 2, 0.5, 4);

        assertNotNull(suggestions, "Suggestions should not be null");
        assertEquals(4, suggestions.size(), "Expected 4 suggestions to be returned");
        assertTrue(suggestions.contains("morbid"), "Suggestions should contain 'morbid'");
        assertTrue(suggestions.contains("hobbit"), "Suggestions should contain 'hobbit'");
    }

    @Test
    void testCalculateCommonPercent() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        double percent1 = recommender.calculateCommonPercent("committee", "comet");
        double percent2 = recommender.calculateCommonPercent("gardener", "nerdier");

        assertEquals(5.0 / 6.0, percent1, 0.01, "The common percent between 'committee' and 'comet' should be 5/6");
        assertEquals(4.0 / 7.0, percent2, 0.01, "The common percent between 'gardener' and 'nerdier' should be 4/7");
    }

    @Test
    void testGetSimilarity() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        double similarity1 = recommender.getSimilarity("aghast", "gross");
        double similarity2 = recommender.getSimilarity("oblige", "oblivion");

        assertEquals(1.5, similarity1, 0.01, "The left-right similarity between 'aghast' and 'gross' should be 1.5");
        assertEquals(2.5, similarity2, 0.01, "The left-right similarity between 'oblige' and 'oblivion' should be 2.5");
    }
}
