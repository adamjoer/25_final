package game.chance.card;

import game.*;
import java.lang.Math;

public class ChanceCardController {


    private final ChanceCard[] BASE_DECK;
    private ChanceCard[] drawPile;
    private int pilePosition = 0;


    public ChanceCardController(){
        BASE_DECK = Utility.chanceCardGenerator("src/main/java/resources/chanceCards.xml");
        this.drawPile = new ChanceCard[BASE_DECK.length];
        shuffleDeck();
    }

    private int[] appendIntArray(int[] array, int n){
        int[] result = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) { result[i] = array[i]; }
        result[array.length] = n;
        return result;
    }

    private int[] removeUniqueElementFromArray(int[] array, int n){
        int[] result = new int[array.length - 1];
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != n) {
                result[j] = array[i];
                j++;
            }
        }
        return result;
    }

    private int[] shuffleIntArray(int[] array){
        if (array.length == 1){
            return array;
        } else {
            int drawNumber = (int) (Math.random() * array.length);
            int[] arrayWithoutNumber = removeUniqueElementFromArray(array,drawNumber);
            return appendIntArray(shuffleIntArray(arrayWithoutNumber),drawNumber);
        }
    }

    private void shuffleDeck(){
        int k = BASE_DECK.length;
        int[] orderedArray = new int[k];
        for (int i = 0; i < k; i++) { orderedArray[i] = i; }
        int[] shuffleReference = shuffleIntArray(orderedArray);
        for (int i = 0; i < k; i++) {
            drawPile[i] = BASE_DECK[shuffleReference[i]];
        }
    }

    public String drawCard(){
        if (pilePosition == drawPile.length - 1){ shuffleDeck(); }
        pilePosition = (pilePosition + 1) % drawPile.length;
        return getCurrentCardType();
    }


    public String getCurrentCardType(){
        return this.drawPile[pilePosition].getClass().getSimpleName();
    }

    public String getCardText(){ return this.drawPile[this.pilePosition].getCardText(); }
    public int getAmount(){ return this.drawPile[this.pilePosition].getAmount(); }
    public int getHouseTax(){ return this.drawPile[this.pilePosition].getHouseTax(); }
    public int getHotelTax(){ return this.drawPile[this.pilePosition].getHotelTax(); }
    public int getThreshold(){ return this.drawPile[this.pilePosition].getThreshold(); }
    public int getIncrement(){ return this.drawPile[this.pilePosition].getIncrement(); }
    public int getDestination(){ return this.drawPile[this.pilePosition].getDestination(); }
}
