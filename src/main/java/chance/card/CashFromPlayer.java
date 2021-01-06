package chance.card;

public class CashFromPlayer extends ChanceCard{

    public final int amount;
    public final int cardnumber;
    public static int increment;
    public static int priceIncrease;
    public static int destination;
    public static boolean prison;

    public static Instructions instructions;

    /**
     * @param CARD_TEXT
     * @param amount
     * @param cardnumber
     */
    public CashFromPlayer(String CARD_TEXT, int amount, int cardnumber){
        super(CARD_TEXT);
        this.amount = amount;
        this.cardnumber = cardnumber;
        this.increment = 0;
        this.priceIncrease = 0;
        this.prison = false;

    }

    public Instructions executeCard(){
        instructions = new Instructions(amount, cardnumber, increment, priceIncrease, destination, prison);
        return instructions;
    }

}


