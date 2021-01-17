package game;

import game.chance.card.ChanceCard;
import game.field.Field;
import game.field.Street;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void fieldGenerator() {
        Field[] fields = Utility.fieldGenerator("fieldListDA.xml");
        // Checks that Strings are correctly imported from the XML file. In this case the class name.
        assertEquals(fields[10].getField(), "Jail");
        // Checks that ints and int arrays are correctly imported AND that the last place is filled in the array.
        // Also checks that the correct class has been instantiated for the particular field through typecasting.
        int[] actualRentLevels = new int[]{1000, 2000, 4000, 12000, 28000, 34000, 40000};
        int[] importedRentLevels = ((Street) fields[39]).getRentLevels();
        for (int i = 0; i < importedRentLevels.length; i++) {
            assertEquals(importedRentLevels[i], actualRentLevels[i]);
        }
        // Checks that the correct array length has been assigned.
        assertEquals(fields.length, 40);
    }

    @Test
    void chanceCardGenerator() {
        ChanceCard[] deck = Utility.chanceCardGenerator("testFiles/chanceCardTest.xml");
        // Checks that the length of the array is correct (differs from fieldGenerator, in that it accounts for duplicates.
        assertEquals(deck.length, 15);
        // Checks that the last spot in the array is filled with an OutOfJailCard as expected.
        assertEquals(deck[14].getClass().getSimpleName(), "OutOfJailCard");
        // Checks that the correct number of duplicates with cardText "BankTransactionTest2" has been created.
        int duplicateCounter = 0;
        for (ChanceCard card : deck) {
            if (card.getCardText().equals("BankTransactionTest2")) duplicateCounter++;
        }
        assertEquals(duplicateCounter, 3);
    }

    @Test
    void stringRefGenerator() {
        StringRef[] stringRefs = Utility.stringRefGenerator("stringRefsDA.xml");
        // Checks that the 6th element in the array has the correct reference and then checks that the output String corresponds.
        assertTrue(stringRefs[5].checkReference("maxPlayerReachedPrompt"));
        assertEquals(stringRefs[5].getString(), "Det maksimale antal spillere er nået, vil De:");
        // Checks that the last element in the array isn't null and is what we expect it to be.
        assertTrue(stringRefs[stringRefs.length - 1].checkReference("pawnedOrOwnerInJail"));
    }

    @Test
    void shuffleIntArray() {
        int[] orderedArray = new int[40];
        for (int i = 0; i < 40; i++) {
            orderedArray[i] = i;
        }
        int[] shuffledArray = Utility.shuffleIntArray(orderedArray);
        // See if the shuffled array is the same length as the ordered array.
        assertEquals(orderedArray.length, shuffledArray.length);
        // Make sure the shuffled array is actually shuffled.
        assertNotSame(orderedArray, shuffledArray);
        // Make sure every element of the ordered array is represented in the shuffled array.
        boolean originalElementMissing = false;
        for (int i = 0; i < 40; i++) {
            boolean matchFound = false;
            for (int j = 0; j < 40; j++) {
                if (i == shuffledArray[j]) {
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                originalElementMissing = true;
            }
        }
        assertFalse(originalElementMissing);

    }

    // All removeFromArray methods are virtually identical except for parameter types and return type.
    @Test
    void removeFromArray() {

        // Integer arrays
        int[] ints = new int[]{1, 2, 5, 4};
        int[] noTwo = Utility.removeFromArray(ints, 1);
        // Assertions for the new int array with index 1 removed.
        assertNotEquals(noTwo[1], ints[1]);
        assertEquals(noTwo[1], 5);
        assertEquals(noTwo.length, ints.length - 1);

        // String arrays
        String[] strings = new String[]{"me", "count", "so", "poor"};
        String[] noMe = Utility.removeFromArray(strings, 0);
        // Assertions for the new string array with index 0 removed.
        assertNotEquals(noMe[0], strings[0]);
        assertEquals(noMe[0], "count");
        assertEquals(noMe.length, strings.length - 1);
    }

    // All addToArray methods are virtually identical except for parameter types and return type.
    @Test
    void addToArray() {
        // Integer arrays
        int[] ints = new int[]{1, 2, 9};
        int[] withTen = Utility.addToArray(ints, 10);
        // Assertions for the new int array with 10 added in the last place.
        for (int i = 0; i < ints.length; i++) {
            assertEquals(withTen[i], ints[i]);
        }
        assertEquals(withTen[3], 10);
        assertEquals(withTen.length, ints.length + 1);

        // String arrays
        String[] strings = new String[]{"lost", "count", "again"};
        String[] withExtra = Utility.addToArray(strings, "gonna count, gonna count, gonna count now");
        // Assertions for the new int array with an extra string added in the last place.
        for (int i = 0; i < strings.length; i++) {
            assertEquals(withExtra[i], strings[i]);
        }
        assertEquals(withExtra[3], "gonna count, gonna count, gonna count now");
        assertEquals(withExtra.length, strings.length + 1);
    }
}