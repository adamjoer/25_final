package game.chance.card;

public class HouseTax extends ChanceCard {

    public final int HOUSE_TAX;
    public final int HOTEL_TAX;


    /**
     * @param CARD_TEXT The card text for the card. For output in the GUI.
     * @param houseTax The penalty for each house the player owns.
     * @param hotelTax The penalty for each hotel the player owns.
     */

    public HouseTax(String CARD_TEXT, int houseTax, int hotelTax){
        super(CARD_TEXT);
        HOUSE_TAX = houseTax;
        HOTEL_TAX = hotelTax;
    }

    public String getSuccessText(){ return ""; }
    public String getFailText(){ return ""; }
    public int[] getShippingLocations(){ return new int[]{}; }
    public int getAmount(){ return 0; }
    public int getHouseTax(){ return HOUSE_TAX; }
    public int getHotelTax(){ return HOTEL_TAX; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }
    public int getJailPosition() { return 0; }
    public boolean getDoubleRent() { return false; }
    public boolean getForward() { return false; }
}
