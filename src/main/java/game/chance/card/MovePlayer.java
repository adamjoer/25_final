package game.chance.card;

public class MovePlayer extends ChanceCard {

    private final int INCREMENT;


    /**
     * @param CARD_TEXT The card text for the card. For output in the GUI.
     * @param increment The number of tiles the player has to be moved.
     */

    public MovePlayer(String CARD_TEXT, int increment){
        super(CARD_TEXT);
        INCREMENT = increment;
    }

    public String getSuccessText(){ return ""; }
    public String getFailText(){ return ""; }
    public int[] getShippingLocations(){ return new int[]{}; }
    public int getAmount(){ return 0; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return INCREMENT; }
    public int getDestination(){ return 0; }
    public int getJailPosition() { return 0; }
    public boolean getDoubleRent() { return false; }
    public boolean getForward() { return false; }
}


