package game.chance.card;

public class HouseTax extends ChanceCard {

    public final int HOUSE_TAX;
    public final int HOTEL_TAX;


    /**
     * @param CARD_TEXT
     * @param houseTax
     * @param hotelTax
     */

    public HouseTax(String CARD_TEXT, int houseTax, int hotelTax){
        super(CARD_TEXT);
        HOUSE_TAX = houseTax;
        HOTEL_TAX = hotelTax;
    }

    public int getAmount(){ return 0; }
    public int getHouseTax(){ return HOUSE_TAX; }
    public int getHotelTax(){ return HOTEL_TAX; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return 0; }
}
