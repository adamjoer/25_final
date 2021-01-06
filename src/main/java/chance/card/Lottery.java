package chance.card;

public class Lottery extends ChanceCard {

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
     * @param amount
     * @param cardnumber
     */
    public Lottery(String CARD_TEXT, int amount, int cardnumber){
        super(CARD_TEXT);
        this.amount = amount;
        this.cardnumber = cardnumber;
        this.increment = 0;
        this.player = 0;
        this.destination = 0;
        this.houseTax = 0;
        this.prison = false;

    }

    public Instructions executeCard(){
        instructions = new Instructions(cardnumber, amount, increment, destination, houseTax, player, prison);
        return instructions;
    }
}
