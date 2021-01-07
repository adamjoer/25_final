package chance.card;

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

    public CardInstruction executeCard(){
        return new CardInstruction(getClass().getSimpleName(),CARD_TEXT,HOUSE_TAX,HOTEL_TAX);
    }
}
