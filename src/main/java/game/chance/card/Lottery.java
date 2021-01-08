package game.chance.card;

public class Lottery extends ChanceCard {

    private final int AMOUNT;
    private final int THRESHOLD;


    /**
     * @param CARD_TEXT
     * @param amount
     * @param threshold
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