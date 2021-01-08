package game;

import game.field.Field;
import game.field.FieldInstruction;

import java.io.FileInputStream;
import java.util.Arrays;

public class Game {

    private PlayerController playerController;
    private GUIController guiController;
    private DiceController diceController;
    private FieldController fieldController;
    //private ChanceCardController chanceCardController;
    private Player[] players;
    private int playerTurn;

    public Game(){
        Field[] fields = Utility.fieldGenerator("src/main/java/resources/fieldList.xml");
        guiController = new GUIController(fields);
        diceController = new DiceController(2,6);
        playerController = new PlayerController(guiController.returnPlayerNames(), 30000);
        players = playerController.getPlayers();
        playerTurn = (int) (Math.random() * (players.length - 1));
    }

    public void gameLoop(){
        boolean stop = false;
        boolean isDieIdentical;

        guiController.addPlayers(players);

        while(!stop){
            playerTurn = getNextPlayerTurn();
            guiController.showMessage("Player "+ playerController.getName(playerTurn) + " turn to roll the dice" );
            // cast dice from dice controller
            guiController.getUserButton("Roll the dice", "Roll");
            diceController.roll();
            isDieIdentical = diceController.isIdentical();
            int dice1 = diceController.getFaceValue(0);
            int dice2 = diceController.getFaceValue(1);

            guiController.setDiceGui(dice1, (int) (Math.random() * 360), dice2, ((int) (Math.random() * 360)));

            movePlayer(playerTurn, diceController.getSum());
            while(isDieIdentical){
                guiController.showMessage("You got identical dies, roll the dice again!");
                guiController.getUserButton("Roll the dice", "Roll");
                diceController.roll();
                isDieIdentical = diceController.isIdentical();
                dice1 = diceController.getFaceValue(0);
                dice2 = diceController.getFaceValue(1);
                guiController.setDiceGui(dice1, (int) (Math.random() * 360), dice2, ((int) (Math.random() * 360)));
                movePlayer(playerTurn, diceController.getSum());
            }
            String userBtn = guiController.getUserButton("Continue or close game?",
                    "Close game", "Continuegame");
            if (userBtn.equals("Close game")) {
                stop = true;
            }
        }
        guiController.close();
    }

    public void movePlayer(int player, int increment){
        playerController.movePlayer(player, increment);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
        if(playerController.getPlayerPosition(player) < playerController.getOldPlayerPosition(player)){
            setGuiBalance(player, playerController.getPlayerBalance(player));
        }
    }

    public void setPlayerPosition(int player, int position){
        playerController.setPlayerPosition(player, position);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
    }

    /*public void checkStartPass(int player, int increment){
        if (playerController.getPlayerPosition(player) > playerController.getCurrentPosition(player) + increment){
            giveStartPassReward(player);
        }
    }

    public boolean giveStartPassReward(int player){
        if (fieldController.hasPassedGo(playerController.getOldPlayerPosition(player), playerController.getPlayerPosition(player))){
            playerController.setPlayerBalance(player, playerController.makeTransaction(player, 4000));
            return true;
        }
        return false;
    }

    public void removePlayer(int player){
        // When a player becomes broke, the player needs to be removed from the game
        guiController.showMessage("%s has been removed from the game", players[player].getName());
        players = ArrayUtils.removeElement(players, players[player]);
        guiController.setCar(player, false);
    }*/

    public boolean sellProperty(int player, int place){
        // TODO : make a check for if the property exists
        int[] properties = playerController.getProperties(player);
        if(Arrays.stream(properties).anyMatch(i -> i == place )){
            playerController.removeProperty(player, place);
            return true;
        }
        return false;
    }

    public boolean buyProperty(int player, int place, int rent){
        // TODO : make a check for if the property is not owned
        int[] properties = playerController.getProperties(player);
        if(Arrays.stream(properties).anyMatch(i -> i == place )){
            fieldController.buyProperty(player, place);
            playerController.addProperty(player, place);
            guiController.fieldOwnable(place, player, rent);
            return true;
        }
        return false;
    }

    public String[] getPlayerNames(){
        return guiController.returnPlayerNames();
    }

    public boolean fieldAction(int position, int player){
        FieldInstruction instructions = fieldController.fieldAction(position);

        switch(instructions.getFieldType()) {

            case "Brewery":
            case "Street":
            case "Shipping":

                //Check if the field is owned by the player
                if(player == instructions.getOwner()){
                    guiController.showMessage("Det er dit eget felt");
                    return true;
                }

                //Check if the field is owned by the bank
                else if(instructions.getOwner() == -1){

                    //If field is owned by the bank, ask player if they want to buy it
                    if(guiController.getUserButton("Ingen spillere ejer dette felt, vil du købe det?", "Ja", "Nej") == "Ja"){

                        //If they want to buy it, check if they have money for it
                        if(playerController.makeTransaction(instructions.getCost(), player)){
                            buyProperty(player, position, instructions.getRent());
                            guiController.setBalance(playerController.getPlayerBalance(player), player);
                        }
                        else{
                            guiController.showMessage("Du har ikke nok penge");
                        }
                    }
                    return true;
                }

                //Field is owned by another player, so they have to pay rent
                else{
                    guiController.showMessage("Feltet er eget af en anden spiller, du skal betale husleje");
                    int owner = instructions.getOwner();

                    //Make transaction from the current player to the owner of the field
                    boolean successfulRent = playerController.makeTransaction(instructions.getRent(), player, owner);

                    //Set the balance of both players in the GUI
                    guiController.setBalance(playerController.getPlayerBalance(player), player);
                    guiController.setBalance(playerController.getPlayerBalance(owner), owner);

                    return successfulRent;
                }

                break;

            case "Chance":
                break;

            case "GoToJail":
                break;

            case "Jail":
                break;

            case "Parking":
                break;

            case "Start":
                break;

            case "TaxField":
                break;

            default:
                throw new IllegalArgumentException("Field type '" + instructions.getFieldType() + "' not recognised");
        }
    }

    public int getNextPlayerTurn(){
        playerTurn = (playerTurn + 1) % players.length;
        return playerTurn;
    }

    /*public int getPlayerTurn(){
        return 0;
    }

    public boolean hasWinner(){
        return false;
    }*/

    public void makeTransaction(int receiver, int sender, int amount){
        playerController.makeTransaction(receiver, sender, amount);
    }

    public void setGuiBalance(int player, int amount){
        guiController.setBalance(amount, player);
    }
}
