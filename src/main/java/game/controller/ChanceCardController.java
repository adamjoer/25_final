package game.controller;

import game.*;
import game.chance.card.*;

public class ChanceCardController {

    private final String XML_FILEPATH = "src/main/resources/chanceCards.xml";
    private final ChanceCard[] BASE_DECK;
    private final int BASE_DECK_LENGTH;
    private int currentDeckLength;
    private ChanceCard[] drawPile;
    private int pilePosition = -1;
    private int outOfJailCardsDrawn = 0;

    /**
     * Constructor. Executes chanceCardGenerator in game.Utility to create a base deck.
     * Proceeds to make a drawPile with references to the original deck in random order.
     */

    public ChanceCardController() {
        BASE_DECK = Utility.chanceCardGenerator(XML_FILEPATH);
        BASE_DECK_LENGTH = BASE_DECK.length;
        shuffleDeck();
    }

    /**
     * shuffleDeck creates an int array with the same length as BASE_DECK and makes it so the each element of the int
     * array corresponds to its index. The int array is then shuffled with shuffleIntArray and used as a shuffle
     * reference for the drawPile so that each element in the drawPile has a reference to a different element in the
     * BASE_DECK.
     */
    private void shuffleDeck() {
        currentDeckLength = BASE_DECK_LENGTH - outOfJailCardsDrawn;
        drawPile = new ChanceCard[currentDeckLength];
        int[] orderedArray = new int[currentDeckLength];
        for (int i = 0; i < currentDeckLength; i++) {
            orderedArray[i] = i;
        }
        int[] shuffleReference = Utility.shuffleIntArray(orderedArray);
        for (int i = 0; i < currentDeckLength; i++) {
            drawPile[i] = BASE_DECK[shuffleReference[i]];
        }
    }

    /**
     * drawCard increases the value of pilePosition by 1 and then returns the String of the corresponding card in the
     * drawPile. If the last card in the drawPile is reached before the increment of pilePosition, the drawPile is
     * shuffled and pilePosition is reset to 0.
     *
     * @return The String identifying the card type of the current ChanceCard.
     */

    public String drawCard() {
        if (pilePosition == currentDeckLength - 1) {
            shuffleDeck();
        }
        pilePosition = (pilePosition + 1) % currentDeckLength;
        if (getCurrentCardType().equals("OutOfJailCard")) {
            outOfJailCardsDrawn += 1;
        }
        return getCardText();
    }

    public void returnOutOfJailCard() {
        outOfJailCardsDrawn -= 1;
    }

    // Relevant getters.
    public String getCurrentCardType() {
        return this.drawPile[pilePosition].getChanceCard();
    }

    public String getCardText() {
        return this.drawPile[this.pilePosition].getCardText();
    }

    public String getFailText() {
        return this.drawPile[this.pilePosition].getFailText();
    }

    public String getSuccessText() {
        return this.drawPile[this.pilePosition].getSuccessText();
    }

    public int[] getShippingLocations() {
        return this.drawPile[this.pilePosition].getShippingLocations();
    }

    public int getAmount() {
        return this.drawPile[this.pilePosition].getAmount();
    }

    public int getHouseTax() {
        return this.drawPile[this.pilePosition].getHouseTax();
    }

    public int getHotelTax() {
        return this.drawPile[this.pilePosition].getHotelTax();
    }

    public int getThreshold() {
        return this.drawPile[this.pilePosition].getThreshold();
    }

    public int getIncrement() {
        return this.drawPile[this.pilePosition].getIncrement();
    }

    public int getDestination() {
        return this.drawPile[this.pilePosition].getDestination();
    }

    public int getJailPosition() {
        return this.drawPile[this.pilePosition].getJailPosition();
    }

    public boolean getDoubleRent() {
        return this.drawPile[this.pilePosition].getDoubleRent();
    }

    public boolean getForward() {
        return this.drawPile[this.pilePosition].getForward();
    }
}
