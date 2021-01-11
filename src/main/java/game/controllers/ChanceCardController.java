package game.controllers;

import game.*;
import game.chance.card.ChanceCard;

import java.lang.Math;

public class ChanceCardController {

    private final String XML_FILEPATH = "src/main/java/resources/chanceCards.xml";
    private final ChanceCard[] BASE_DECK;
    private final int BASE_DECK_LENGTH;
    private ChanceCard[] drawPile;
    private int pilePosition = -1;

    /**
     * Constructor. Executes chanceCardGenerator in game.Utility to create a base deck.
     * Proceeds to make a drawPile with references to the original deck in random order.
     */

    public ChanceCardController(){
        BASE_DECK = Utility.chanceCardGenerator(XML_FILEPATH);
        BASE_DECK_LENGTH = BASE_DECK.length;
        this.drawPile = new ChanceCard[BASE_DECK.length];
        shuffleDeck();
    }

    /**
     * appendIntArray creates a new int array with one more place and fills in that place with a given integer.
     * @param array The array to add a place to.
     * @param n The value to assign to the created place.
     * @return The array with the appended int place (placed at the last index).
     */

    private int[] appendIntArray(int[] array, int n){
        int[] result = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) { result[i] = array[i]; }
        result[array.length] = n;
        return result;
    }

    /**
     * removeIntArrayByIndex creates a new array with one less place than the input array. Specified index is removed.
     * @param array The int array from which a place is to be removed.
     * @param index The index of the place to be removed.
     * @return An int array with one less place - the specified index removed.
     */
    private int[] removeIntArrayByIndex(int[] array, int index){
        int[] result = new int[array.length - 1];
        for (int i = 0; i < index; i++) { result[i] = array[i]; }
        for (int i = index + 1; i < array.length; i++) { result[i-1] = array[i]; }
        return result;
    }

    /**
     * shuffleIntArray utilizes removeIntArrayByIndex to remove a random element from the int array given and
     * appendIntArray to apply the same random element to the end of the array.
     * The method is recursive, such that it keeps on removing elements in a random order, until there is only one
     * element left in the initial array. It then appends the single element arrays in the random order they were
     * extracted. This guarantees a shuffled array. As the method picks out by index rather than by value, it ensures
     * that the recursion is well defined.
     * @param array The int array to shuffle
     * @return A shuffled int array with the same elements as the argument given, in a random order.
     */

    private int[] shuffleIntArray(int[] array){
        if (array.length == 1){
            return array;
        } else {
            int index = (int) (Math.random() * (array.length - 1));
            int[] arrayWithoutIndex = removeIntArrayByIndex(array,index);
            return appendIntArray(shuffleIntArray(arrayWithoutIndex),array[index]);
        }
    }

    /**
     * shuffleDeck creates an int array with the same length as BASE_DECK and makes it so the each element of the int
     * array corresponds to its index. The int array is then shuffled with shuffleIntArray and used as a shuffle
     * reference for the drawPile so that each element in the drawPile has a reference to a different element in the
     * BASE_DECK.
     */

    private void shuffleDeck(){
        int[] orderedArray = new int[BASE_DECK_LENGTH];
        for (int i = 0; i < BASE_DECK_LENGTH; i++) { orderedArray[i] = i; }
        int[] shuffleReference = shuffleIntArray(orderedArray);
        for (int i = 0; i < BASE_DECK_LENGTH; i++) {
            drawPile[i] = BASE_DECK[shuffleReference[i]];
        }
    }

    /**
     * drawCard increases the value of pilePosition by 1 and then returns the String of the corresponding card in the
     * drawPile. If the last card in the drawPile is reached before the increment of pilePosition, the drawPile is
     * shuffled and pilePosition is reset to 0.
     * @return The String identifying the card type of the current ChanceCard.
     */

    public String drawCard(){
        if (pilePosition == BASE_DECK_LENGTH - 1){ shuffleDeck(); }
        pilePosition = (pilePosition + 1) % BASE_DECK_LENGTH;
        return getCardText();
    }

    // Relevant getters.

    public String getCurrentCardType(){ return this.drawPile[pilePosition].getClass().getSimpleName(); }
    public String getCardText(){ return this.drawPile[this.pilePosition].getCardText(); }
    public String getFailText(){ return this.drawPile[this.pilePosition].getFailText(); }
    public String getSuccessText(){ return this.drawPile[this.pilePosition].getSuccessText(); }
    public int getAmount(){ return this.drawPile[this.pilePosition].getAmount(); }
    public int getHouseTax(){ return this.drawPile[this.pilePosition].getHouseTax(); }
    public int getHotelTax(){ return this.drawPile[this.pilePosition].getHotelTax(); }
    public int getThreshold(){ return this.drawPile[this.pilePosition].getThreshold(); }
    public int getIncrement(){ return this.drawPile[this.pilePosition].getIncrement(); }
    public int getDestination(){ return this.drawPile[this.pilePosition].getDestination(); }
}
