package game.chance.card;

public class CardInstruction {

    private final String CARD_TYPE, CARD_TEXT;
    private int amount, threshold, increment, houseTax, hotelTax, destination;


    /**
     * Constructor for BankTransaction Cardtype
     * @param cardType
     * @param cardText
     * @param amount
     */
    public CardInstruction(String cardType, String cardText, int amount){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.amount = amount;
    }

    /**
     * Constructor for HouseTax Cardtype
     * @param cardType
     * @param cardText
     * @param houseTax
     * @param hotelTax
     */
    public CardInstruction(String cardType, String cardText, int houseTax, int hotelTax){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.houseTax = houseTax;
        this.hotelTax = hotelTax;
    }

    /**
     * Constructor for Lottery Cardtype
     * @param amount
     * @param threshold
     * @param cardType
     * @param cardText
     */
    public CardInstruction(int amount, int threshold, String cardType, String cardText){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.amount = amount;
        this.threshold = threshold;
    }

    /**
     * Constructor for MovePlayer Cardtype
     * @param cardType
     * @param increment
     * @param cardText
     */
    public CardInstruction(String cardType, int increment, String cardText){
        CARD_TYPE = cardType;
        CARD_TEXT = cardText;
        this.increment = increment;

    }

    /**
     * Constructor for MovePlayerToTyle Cardtype
     * @param destination
     * @param cardType
     * @param cardText
     */
    public CardInstruction(int destination, String cardType, String cardText){
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
