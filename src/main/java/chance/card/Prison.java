package chance.card;

public class Prison extends ChanceCard {

    public final int amount;
    public final int cardnumber;
    public static int increment;
    public static int priceIncrease;
    public static int destination;
    public static boolean prison;

    public static Instructions instructions;


    /**
     * @param CARD_TEXT
     * @param prison
     * @param cardnumber
     */
    public Prison(String CARD_TEXT, boolean prison, int cardnumber){
        super(CARD_TEXT);
        this.amount = 0;
        this.cardnumber = cardnumber;
        this.increment = 0;
        this.priceIncrease = 0;
        this.prison = prison;

    }

    public Instructions executeCard(){
        instructions = new Instructions(amount, cardnumber, increment, priceIncrease, destination, prison);
        return instructions;
    }
}
