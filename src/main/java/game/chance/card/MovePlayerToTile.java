package game.chance.card;

public class MovePlayerToTile extends ChanceCard {

    private final int DESTINATION;


    /**
     * @param CARD_TEXT The card text for the card. For output in the GUI.
     * @param destination The destination field to move the player to.
     */

    public MovePlayerToTile(String CARD_TEXT, int destination){
        super(CARD_TEXT);
        DESTINATION = destination;
    }

    public int getAmount(){ return 0; }
    public int getHouseTax(){ return 0; }
    public int getHotelTax(){ return 0; }
    public int getThreshold(){ return 0; }
    public int getIncrement(){ return 0; }
    public int getDestination(){ return DESTINATION; }
}