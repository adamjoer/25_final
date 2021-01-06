package chance.card;

public class MovePlayerToTile extends ChanceCard{

    public final int amount;
    public final int cardnumber;
    public static int increment;
    public static int priceIncrease;
    public static int destination;
    public static boolean prison;

    public static Instructions instructions;


    /**
     * @param CARD_TEXT
     * @param destination
     * @param cardnumber
     */
    public MovePlayerToTile(String CARD_TEXT, int destination, int cardnumber){
        super(CARD_TEXT);
        this.amount = 0;
        this.cardnumber = cardnumber;
        this.increment = 0;
        this.priceIncrease = 0;
        this.destination = destination;
        this.prison = false;

    }

    public Instructions executeCard(){
        instructions = new Instructions(amount, cardnumber, increment, priceIncrease, destination, prison);
        return instructions;
    }
}
