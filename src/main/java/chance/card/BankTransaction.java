package chance.card;

public class BankTransaction extends ChanceCard {

    public final int AMOUNT;


    /**
     * @param CARD_TEXT
     * @param amount
     */

    public BankTransaction(String CARD_TEXT, int amount){
        super(CARD_TEXT);
        AMOUNT = amount;
    }

    public CardInstruction executeCard(){
        CardInstruction instruction = new CardInstruction(getClass().getSimpleName(),AMOUNT);
        return instruction;
    }
}