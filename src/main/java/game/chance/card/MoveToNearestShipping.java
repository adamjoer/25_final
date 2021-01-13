package game.chance.card;

public class MoveToNearestShipping extends ChanceCard {
    private final int[] SHIPPING_LOCATIONS;
    private final boolean FORWARD;
    private final boolean DOUBLE_RENT;

    public MoveToNearestShipping(String CARD_TEXT, int[] shippingLocations, boolean forward, boolean doubleRent){
        super(CARD_TEXT);
        SHIPPING_LOCATIONS = shippingLocations;
        FORWARD = forward;
        DOUBLE_RENT = doubleRent;
    }
    public String getSuccessText(){ return ""; }
    public String getFailText(){ return ""; }
    public int[] getShippingLocations(){ return SHIPPING_LOCATIONS; }
    public int getAmount(){ return 0; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }
    public int getJailPosition() { return 0; }
    public boolean getDoubleRent() { return DOUBLE_RENT; }
    public boolean getForward() { return FORWARD; }
}
