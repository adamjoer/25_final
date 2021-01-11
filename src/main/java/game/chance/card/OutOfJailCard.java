package game.chance.card;

public class OutOfJailCard extends ChanceCard {
    public OutOfJailCard(String CARD_TEXT){
        super(CARD_TEXT);
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
    public int getJailPosition() { return 0; }
    public boolean getDoubleRent() { return false; }
    public boolean getForward() { return false; }
}
