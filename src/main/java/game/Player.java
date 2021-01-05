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


    public boolean makeTransaction(int amount){
        if(getBalance() >= -amount){
            setBalance(getBalance() + amount);
            return true;
        }
        else{
            return false;
        }
    }

    public void addProperty(int place){

    }


    public int getBalance() {
        return account.getBalance();
    }

    public int[] getProperties(){
        return properties;
    }

    public void setBalance(int amount){
        account.setBalance(amount);
    }




}
