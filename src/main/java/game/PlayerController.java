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
}
