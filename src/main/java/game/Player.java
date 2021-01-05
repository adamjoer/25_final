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
        if(Arrays.asList(properties).contains(place)){
            int[] newList = new int[properties.length - 1];
            for(int i = 0; i < properties.length - 1; i++){
                if(properties[i] == place){
                    properties[i] = properties[properties.length - 1];
                }
                else{
                    newList[i] = properties[i];
                }
            }
        }
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
