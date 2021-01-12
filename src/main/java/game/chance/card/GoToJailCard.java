package game.chance.card;

public class GoToJailCard extends ChanceCard {
    private final int JAIL_POSITION;

    public GoToJailCard(String CARD_TEXT, int jailPosition){
        super(CARD_TEXT);
        JAIL_POSITION = jailPosition;

    }
    public String getSuccessText(){ return ""; }
    public String getFailText(){ return ""; }
    public int[] getShippingLocations(){ return new int[]{}; }
    public int getAmount(){ return 0; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }
    public int getJailPosition() { return JAIL_POSITION; }
    public boolean getDoubleRent() { return false; }
    public boolean getForward() { return false; }
}
