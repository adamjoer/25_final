package game;

import java.util.Arrays;

public class Player {
    
    private Account account;
    private int oldPosition;
    private int currentPosition;
    private int[] properties = new int[1];
    private final String NAME;


    public Player(String NAME, int startBalance) {
        account = new Account(startBalance);
        this.NAME = NAME;
    }


    /**
     * @param amount : Amount to transfer
     * @return : Return true if transfer was sucessfull, return false if player doesn't have anough money
     */
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
        if(properties[0] == 0){
            properties[0] = place;
        }
        else{
            int[] newList = new int[properties.length + 1];
            for(int i = 0; i < properties.length; i++){
                newList[i] = properties[i];
            }
            newList[properties.length] = place;

            properties = newList;
        }

    }

    public void removeProperty(int place){
        boolean owned = false;
        for(int i : properties){
            if(i == place){
                owned = true;
            }
        }
        if(owned){
            int[] newList = new int[properties.length - 1];
            for(int i = 0; i < properties.length - 1; i++){
                if(properties[i] == place){
                    properties[i] = properties[properties.length - 1];
                }
                newList[i] = properties[i];
            }
            properties = newList;
        }
    }

    public void movePlayer(int increment){
        oldPosition = currentPosition;
        currentPosition = (currentPosition + increment) % 40;
    }


    public int getBalance() {
        return account.getBalance();
    }

    public int[] getProperties(){
        return properties;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getOldPosition() {
        return oldPosition;
    }


    public void setBalance(int amount){
        account.setBalance(amount);
    }

    private void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.oldPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }
}
