package chance.card;

public class CardInstructions {

    private final String CARD_TYPE, CARD_TEXT;
    private int amount, threshold, increment, houseTax, hotelTax, destination;




    public CardInstructions (String cardType, String cardText, int amount){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.amount = amount;
    }

    public CardInstructions (String cardType, String cardText, int houseTax, int hotelTax){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.houseTax = houseTax;
        this.hotelTax = hotelTax;
    }

    public CardInstructions (int amount, int threshold, String cardType, String cardText){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.amount = amount;
        this.threshold = threshold;
    }

    public CardInstructions (String cardType,int increment, String cardText){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.increment = increment;

    }

    public CardInstructions (int destination, String cardType, String cardText){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.destination = destination;

    }

    public String getCardType(){ return this.CARD_TYPE; }
    public String getCardText(){ return this.CARD_TEXT; }
    public int getAmount(){ return this.amount; }
    public int getThreshold(){ return this.threshold; }
    public int getIncrement(){ return this.increment; }
    public int getDestination(){ return this.destination; }
    public int getHouseTax(){ return this.houseTax; }
    public int getHotelTax(){ return this.hotelTax; }
}
