package chance.card;

public class Lottery extends ChanceCard {

    private final int AMOUNT;
    private final int THRESHOLD;


    /**
     * @param CARD_TEXT
     * @param amount
     * @param threshold
     */

    public Lottery(String CARD_TEXT, int amount, int threshold){
        super(CARD_TEXT);
        AMOUNT = amount;
        THRESHOLD = threshold;
    }

    public CardInstruction executeCard(){
        return new CardInstruction(AMOUNT,THRESHOLD,getClass().getSimpleName(),CARD_TEXT);
    }
}