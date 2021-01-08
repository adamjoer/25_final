package chance.card;

public class MovePlayerToTile extends ChanceCard {

    private final int DESTINATION;


    /**
     * @param CARD_TEXT
     * @param destination
     */

    public MovePlayerToTile(String CARD_TEXT, int destination){
        super(CARD_TEXT);
        DESTINATION = destination;
    }

    public CardInstruction executeCard(){
        return new CardInstruction(DESTINATION,getClass().getSimpleName(),CARD_TEXT);
    }
}