package chance.card;

public class Prison extends ChanceCard {

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
     * @param prison
     * @param cardnumber
     */
    public Prison(String CARD_TEXT, boolean prison, int cardnumber){
        super(CARD_TEXT);
        this.amount = 0;
        this.cardnumber = cardnumber;
        this.increment = 0;
        this.houseTax = 0;
        this.player = 0;
        this.prison = prison;

    }

    public Instructions executeCard(){
        instructions = new Instructions(cardnumber, amount, increment, destination, houseTax, player, prison);
        return instructions;
    }
}
