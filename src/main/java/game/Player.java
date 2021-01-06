package game;

import org.apache.commons.lang.ArrayUtils;

public class Player {

    private Account account;
    private int previousPosition;
    private int currentPosition;
    private int[] properties = new int[1];
    private final String NAME;
    private final int BOARD_LENGTH = 40;


    /**
     * @param NAME         : Name of the player
     * @param startBalance : The amount of money they start off with
     */
    public Player(String NAME, int startBalance) {
        account = new Account(startBalance);
        this.NAME = NAME;
    }


    /**
     * @param amount : Amount to transfer
     * @return : Return true if transfer was sucessfull, return false if player doesn't have anough money
     */
    public boolean makeTransaction(int amount) {
        if (getBalance() >= -amount) {
            setBalance(getBalance() + amount);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method for adding properties to a player
     *
     * @param place : The new place for the player to own
     */
    public void addProperty(int place) {
        if (properties[0] == 0) {
            properties[0] = place;
        } else {
            int[] newList = new int[properties.length + 1];
            System.arraycopy(properties, 0, newList, 0, properties.length);
            newList[properties.length] = place;

            properties = newList;
        }

    }


    /**
     * Method used to remove a property from a player
     *
     * @param place : The place to remove from the player
     */
    public void removeProperty(int place) {
        boolean owned = false;
        for (int i : properties) {
            if (i == place) {
                owned = true;
            }
        }
        if (owned) {
            int[] newList = new int[properties.length - 1];
            properties[ArrayUtils.indexOf(properties, place)] = properties[properties.length - 1];
            System.arraycopy(properties, 0, newList, 0, newList.length);
            properties = newList;
        }
    }

    /**
     * @param increment : The amount to increment the players position
     */
    public void movePlayer(int increment) {
        previousPosition = currentPosition;
        currentPosition = (currentPosition + increment) % BOARD_LENGTH;
    }


    public int getBalance() {
        return account.getBalance();
    }

    public int[] getProperties() {
        return properties;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setBalance(int amount) {
        account.setBalance(amount);
    }

    public void setCurrentPosition(int currentPosition) {
        this.previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }
}
