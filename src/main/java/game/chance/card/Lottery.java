package game.chance.card;

public class Lottery extends ChanceCard {

    private final int AMOUNT;
    private final int THRESHOLD;


    /**
     * @param CARD_TEXT The card text for the card. For output in the GUI.
     * @param amount The amount the player must receive from the bank, if they meet the condition.
     * @param threshold The maximum threshold of values (cash, properties and buildings) the player must have.
     */

    public Lottery(String CARD_TEXT, int amount, int threshold){
        super(CARD_TEXT);
        AMOUNT = amount;
        THRESHOLD = threshold;
    }

    public int getAmount(){ return AMOUNT; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return THRESHOLD; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }
}