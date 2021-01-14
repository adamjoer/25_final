package game.controller;

import game.Player;

public class PlayerController {

    private Player[] players;
    private final int[] getOutOfJailTries;


    /**
     * Make a list of players, with their given names and set their bank account balance
     *
     * @param playerNames  : A list of playerNames, also used to determine how many players are in the game
     * @param startBalance : The starting balance for the account of each player
     */
    public PlayerController(String[] playerNames, int startBalance) {
        players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i], startBalance, i);
        }

        getOutOfJailTries = new int[players.length];
    }

    public Player[] removePlayer(int player){
        Player[] tempArr = players;
        players = new Player[players.length-1];

        System.arraycopy(tempArr, 0, players, 0, player);
        System.arraycopy(tempArr, player + 1, players, player, players.length - player);
        return players;
    }

    /**
     * Make a transaction from a player to the bank or vise versa
     *
     * @param player : Which player to make the transaction with
     * @param amount : The amount to transfer/withdraw
     * @return True if successful transaction, otherwise false
     */
    public boolean makeTransaction(int amount, int player) {
        return players[player].makeTransaction(amount);
    }


    /**
     * Method to send money between money
     *
     * @param sender   : The player who sends money
     * @param receiver : The player who receives money
     * @param amount   : The amount to transfer
     * @return True if the transaction was a success, false if the sender doesn't have enough money
     */
    public boolean makeTransaction(int amount, int sender, int receiver) {
        boolean success = players[sender].makeTransaction(-amount);
        players[receiver].makeTransaction(amount);
        return success;
    }


    /**
     * Method to move a player on the board
     *
     * @param player    : Which player to move
     * @param increment : How much to move the player ahead
     */
    public boolean movePlayer(int player, int increment) {
        players[player].movePlayer(increment);
        if(players[player].getCurrentPosition() < players[player].getPreviousPosition() && increment > 0){
            players[player].makeTransaction(4000);
            return true;
        }
        return false;
    }

    /**
     * Method to assign a property to a player
     *
     * @param player : Which player to give a property
     * @param place  : Which index on the board-1 does the property have
     */
    public void addProperty(int player, int place) {
        players[player].addProperty(place);
    }

    /**
     * Remove a property from a player
     *
     * @param player : Which player to remove a property from
     * @param place  : Which index on the board-1 does the property have
     */
    public void removeProperty(int player, int place) {
        players[player].removeProperty(place);
    }


    //Relevant getters
    public int getId(int player) { return players[player].getId(); }
    public String getName(int player){ return players[player].getName(); }
    public Player[] getPlayers(){ return players; }
    public int[] getProperties(int player) { return players[player].getProperties(); }
    public int getPlayerPosition(int player) { return players[player].getCurrentPosition(); }
    public int getPreviousPlayerPosition(int player) { return players[player].getPreviousPosition(); }
    public int getPlayerBalance(int player) { return players[player].getBalance(); }
    public boolean hasOutOfJailCard(int player){ return players[player].hasOutOfJailCard(); }
    public int getGetOutOfJailTries(int player) { return getOutOfJailTries[player]; }

    //Relevant setters
    public void setPlayerPosition(int player, int position) { players[player].setCurrentPosition(position); }
    public void setPlayerBalance(int player, int balance) { players[player].setBalance(balance); }
    public void setPlayerOutOfJailCards(int player, int cards){ players[player].setOutOfJailCards(cards); }
    public void setGetOutOfJailTries(int player, int tries) { getOutOfJailTries[player] = tries; }
    public void incrementGetOutOfJailTries(int player) { getOutOfJailTries[player]++; }
}
