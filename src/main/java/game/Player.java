package game;

public class Player {
    
    private Account account;
    private int oldPosition;
    private int currentPosition;
    private int[] properties = new int[30];
    private final String NAME;


    public Player(String NAME, int startBalance) {
        account = new Account(startBalance);
        this.NAME = NAME;
    }
}
