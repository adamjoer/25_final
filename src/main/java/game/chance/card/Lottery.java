package game.chance.card;

public class Lottery extends ChanceCard {

    private final int AMOUNT;
    private final int THRESHOLD;
    private final String SUCCESS_TEXT;
    private final String FAIL_TEXT;


    /**
     * @param CARD_TEXT The card text for the card. For output in the GUI.
     * @param amount The amount the player must receive from the bank, if they meet the condition.
     * @param threshold The maximum threshold of values (cash, properties and buildings) the player must have.
     */

    public Lottery(String CARD_TEXT, int amount, int threshold, String successText, String failText){
        super(CARD_TEXT);
        AMOUNT = amount;
        THRESHOLD = threshold;
        SUCCESS_TEXT = successText;
        FAIL_TEXT = failText;
    }

    public String getSuccessText(){ return SUCCESS_TEXT; }
    public String getFailText(){ return FAIL_TEXT; }
    public int[] getShippingLocations(){ return new int[]{}; }
    public int getAmount(){ return AMOUNT; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return THRESHOLD; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }
    public int getJailPosition() { return 0; }
    public boolean getDoubleRent() { return false; }
    public boolean getForward() { return false; }
}