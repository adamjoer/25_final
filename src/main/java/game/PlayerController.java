package game;

public class PlayerController {

    Player[] players;


    /**
     * Make a list of players, with their given names and set their bank account balance
     *
     * @param playerNames : A list of playernames, also used to determine how many players are in the game
     */
    public PlayerController(String[] playerNames, int startBalance) {
        players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i], startBalance);
        }
    }


    /**
     * Make a transaktion from a player to the bank or vise versa
     *
     * @param player : Which player to make the transaction with
     * @param amount : The amount to transfer/withdraw
     * @return True if successfull transaction, otherwise false
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
     * @return True if the transaction was a success, false if the sender doens't have enough money
     */
    public boolean makeTransaction(int amount, int sender, int receiver) {
        if (players[sender].makeTransaction(-amount)) {
            players[receiver].makeTransaction(amount);
            return true;
        } else {
            players[sender].setBalance(0);
            return false;
        }
    }


    /**
     * Method to move a player on the board
     *
     * @param player    : Which player to move
     * @param increment : How much to move the player ahead
     */
    public void movePlayer(int player, int increment) {
        players[player].movePlayer(increment);
        if(players[player].getCurrentPosition() < players[player].getPreviousPosition()){
            players[player].makeTransaction(4000);
        }
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

    /**
     * Give a player a gift from all other players
     *
     * @param player           : Which player to recieve gifts from other players
     * @param amountFromOthers : Amount each player have to gift
     * @return Return true if succesfull, otherwise return false
     */
    public boolean giftPlayer(int player, int amountFromOthers) {
        for (int i = 0; i < players.length; i++) {
            if (i == player) {
                players[player].makeTransaction(amountFromOthers * (players.length - 1));
            } else {
                if (players[i].makeTransaction(-amountFromOthers)) {
                    //do nothing
                } else {
                    players[i].setBalance(0);
                    return false;
                }
            }
        }
        return true;
    }


    //Relevant getters
    public String getName(int player){
        return players[player].getName();
    }

    public Player[] getPlayers(){
        return players;
    }

    public int[] getProperties(int player) {
        return players[player].getProperties();
    }

    public int getPlayerPosition(int player) {
        return players[player].getCurrentPosition();
    }

    public int getPreviousPlayerPosition(int player) {
        return players[player].getPreviousPosition();
    }

    public int getPlayerBalance(int player) {
        return players[player].getBalance();
    }

    //Relevant setters
    public void setPlayerPosition(int player, int position) {
        players[player].setCurrentPosition(position);
    }

    public void setPlayerBalance(int player, int balance) {
        players[player].setBalance(balance);
    }

}
