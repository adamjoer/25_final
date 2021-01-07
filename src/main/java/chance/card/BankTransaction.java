package chance.card;

public class BankTransaction extends ChanceCard {

    public final int amount;
    public static int player;


    /**
     * @param CARD_TEXT
     * @param amount
     */

    public BankTransaction(String CARD_TEXT, int amount){
        super(CARD_TEXT);
        this.amount = amount;
    }

    public CardInstruction executeCard(){
        CardInstruction instruction = new CardInstruction(getClass().getSimpleName(),amount);
    }
}
