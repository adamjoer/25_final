package chance.card;

public class BankTransaction extends ChanceCard {

    private final int AMOUNT;


    /**
     * @param CARD_TEXT
     * @param amount
     */

    public BankTransaction(String CARD_TEXT, int amount){
        super(CARD_TEXT);
        AMOUNT = amount;
    }

    public CardInstruction executeCard(){
        return new CardInstruction(getClass().getSimpleName(),CARD_TEXT,AMOUNT);
    }
}