package chance.card;

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

    public CardInstruction executeCard(){
        return new CardInstruction(getClass().getSimpleName(),INCREMENT,CARD_TEXT);
    }
}


