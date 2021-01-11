package game.chance.card;

public class BankTransaction extends ChanceCard {

    private final int AMOUNT;


    /**
     * @param CARD_TEXT The card text for the card. For output in the GUI.
     * @param amount The amount the player must pay to (negative values)/receive from (positive values) the bank.
     */

    public BankTransaction(String CARD_TEXT, int amount){
        super(CARD_TEXT);
        AMOUNT = amount;
    }

    public String getSuccessText(){ return ""; }
    public String getFailText(){ return ""; }
    public int getAmount(){ return AMOUNT; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }

}