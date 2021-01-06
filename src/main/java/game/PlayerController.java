package game;

public class PlayerController {

    Player[] players;
    private final int startBalance = 30000;


    public PlayerController(String[] playerNames) {
        players = new Player[playerNames.length];

        for(int i = 0; i < playerNames.length; i++){
            players[i] = new Player(playerNames[i], startBalance);
        }
    }


    public boolean makeTransaction(int player, int amount){
        return players[player].makeTransaction(amount);
    }


    public void movePlayer(int player, int increment){
        players[player].movePlayer(increment);
    }

    public void addProperty(int player, int place){
        players[player].addProperty(place);
    }

    public void removeProperty(int player, int place){
        players[player].removeProperty(place);
    }

    public boolean giftPlayer(int player, int amountFromOthers){
        for(int i = 0; i < players.length; i++){
            if()
        }
    }

}
