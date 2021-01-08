package chance.card;

public class CashFromPlayer extends ChanceCard {

    public final int AMOUNT;


    /**
     * @param CARD_TEXT
     * @param amount
     */

    public CashFromPlayer(String CARD_TEXT, int amount){
        super(CARD_TEXT);
        AMOUNT = amount;
    }

    public CardInstruction executeCard(){
        return new CardInstruction(getClass().getSimpleName(),CARD_TEXT,AMOUNT);
    }
}

