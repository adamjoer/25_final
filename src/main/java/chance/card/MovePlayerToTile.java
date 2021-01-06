package chance.card;

public class MovePlayerToTile extends ChanceCard{

    public final int amount;
    public final int cardnumber;
    public static int increment;
    public static int houseTax;
    public static int destination;
    public static int player;
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
        this.houseTax = 0;
        this.destination = destination;
        this.player = 0;
        this.prison = false;

    }

    public Instructions executeCard(){
        instructions = new Instructions(cardnumber, amount, increment, destination, houseTax, player, prison);
        return instructions;
    }
}
