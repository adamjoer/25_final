package game.chance.card;

import game.*;
import java.lang.Math;

public class ChanceCardController {

    private final String XML_FILEPATH = "src/main/java/resources/chanceCards.xml";
    private final ChanceCard[] BASE_DECK;
    private final int BASE_DECK_LENGTH;
    private ChanceCard[] drawPile;
    private int pilePosition = -1;
/*
    public static void main(String[] arg){
        ChanceCardController chanceCardController = new ChanceCardController();
    }
*/

    /**
     *
     */

    public ChanceCardController(){
        BASE_DECK = Utility.chanceCardGenerator(XML_FILEPATH);
        BASE_DECK_LENGTH = BASE_DECK.length;
        this.drawPile = new ChanceCard[BASE_DECK.length];
        shuffleDeck();
    }

    /**
     *
     * @param array
     * @param n
     * @return
     */

    private int[] appendIntArray(int[] array, int n){
        int[] result = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) { result[i] = array[i]; }
        result[array.length] = n;
        return result;
    }

    /**
     *
     * @param array
     * @param index
     * @return
     */
    private int[] removeIntArrayByIndex(int[] array, int index){
        int[] result = new int[array.length - 1];
        for (int i = 0; i < index; i++) { result[i] = array[i]; }
        for (int i = index + 1; i < array.length; i++) { result[i-1] = array[i]; }
        return result;
    }

    /**
     *
     * @param array
     * @return
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
     *
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
     *
     * @return
     */

    public String drawCard(){
        if (pilePosition == BASE_DECK_LENGTH - 1){ shuffleDeck(); }
        pilePosition = (pilePosition + 1) % BASE_DECK_LENGTH;
        return getCurrentCardType();
    }

    // Relevant getters.

    public String getCurrentCardType(){ return this.drawPile[pilePosition].getClass().getSimpleName(); }
    public String getCardText(){ return this.drawPile[this.pilePosition].getCardText(); }
    public int getAmount(){ return this.drawPile[this.pilePosition].getAmount(); }
    public int getHouseTax(){ return this.drawPile[this.pilePosition].getHouseTax(); }
    public int getHotelTax(){ return this.drawPile[this.pilePosition].getHotelTax(); }
    public int getThreshold(){ return this.drawPile[this.pilePosition].getThreshold(); }
    public int getIncrement(){ return this.drawPile[this.pilePosition].getIncrement(); }
    public int getDestination(){ return this.drawPile[this.pilePosition].getDestination(); }
}
