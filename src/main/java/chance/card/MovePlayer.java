package chance.card;

public class MovePlayer extends ChanceCard {

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
     * @param increment
     * @param cardnumber
     */
    public MovePlayer(String CARD_TEXT, int increment, int cardnumber){
        super(CARD_TEXT);
        this.amount = 0;
        this.cardnumber = cardnumber;
        this.increment = increment;
        this.houseTax = 0;
        this.player = 0;
        this.destination = 0;
        this.prison = false;

    }

    public Instructions executeCard(){
        instructions = new Instructions(cardnumber, amount, increment, destination, houseTax, player, prison);
        return instructions;
    }

}


