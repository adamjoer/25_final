package game;

import org.apache.commons.lang.ArrayUtils;

public class Player {

    private final Account account;
    private int previousPosition;
    private int currentPosition;
    private int outOfJailCards = 0;
    private int[] properties = new int[0];
    private final String name;
    private final int BOARD_LENGTH = 40;
    private int id;


    /**
     * @param name         : Name of the player
     * @param startBalance : The amount of money they start off with
     */
    public Player(String name, int startBalance, int id) {
        account = new Account(startBalance);
        this.name = name;
        this.id = id;
    }


    /**
     * @param amount : Amount to transfer
     * @return : Return true if there was coverage. Return false, if balance is negative.
     */
    public boolean makeTransaction(int amount) {
        setBalance(getBalance() + amount);

        return account.getBalance() >= 0;
    }

    /**
     * Method for adding properties to a player
     *
     * @param place : The new place for the player to own
     */
    public void addProperty(int place) {
        int[] newList = new int[properties.length + 1];
        System.arraycopy(properties, 0, newList, 0, properties.length);
        newList[properties.length] = place;

        properties = newList;
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
                break;
            }
        }
        if (owned) {
            int[] newList = new int[properties.length - 1];
            if (newList.length != 0) {
                properties[ArrayUtils.indexOf(properties, place)] = properties[properties.length - 1];
                System.arraycopy(properties, 0, newList, 0, newList.length);
            }
            properties = newList;
        }
    }

    /**
     * @param increment : The amount to increment the players position
     */
    public void movePlayer(int increment) {
        previousPosition = currentPosition;
        currentPosition = (currentPosition + increment) % BOARD_LENGTH;
        while (currentPosition < 0) {
            currentPosition += BOARD_LENGTH;
        }
    }

    public int getOutOfJailCards(){return this.outOfJailCards;}

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
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

    public void setOutOfJailCards(int cards) {
        outOfJailCards = cards;
    }

    public void setBalance(int amount) {
        account.setBalance(amount);
    }

    public void setCurrentPosition(int currentPosition) {
        this.previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;
    }
}
