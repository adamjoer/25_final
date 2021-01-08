package game;

public class Account {

    private int balance;

    public Account(int startBalance){
        balance = startBalance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
