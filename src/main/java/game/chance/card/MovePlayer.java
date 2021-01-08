package game.chance.card;

public class MovePlayer extends ChanceCard {

    private final int INCREMENT;


    /**
     * @param CARD_TEXT
     * @param increment
     */

    public MovePlayer(String CARD_TEXT, int increment){
        super(CARD_TEXT);
        INCREMENT = increment;
    }

    public int getAmount(){ return 0; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return INCREMENT; }
    public int getDestination(){ return 0; }
}


