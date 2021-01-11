package game;

import game.controllers.ChanceCardController;
import game.field.Field;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

public class Game {

    private PlayerController playerController;
    private GUIController guiController;
    private DiceController diceController;
    private FieldController fieldController;
    private ChanceCardController chanceCardController;
    private Player[] players;
    private int playerTurn;

    public static void main(String[] arg){
        Game game = new Game();
        game.gameLoop();
    }

    public Game(){
        Field[] fields = Utility.fieldGenerator("src/main/java/resources/fieldList.xml");
        guiController = new GUIController(fields);
        diceController = new DiceController(2,6);
        playerController = new PlayerController(guiController.returnPlayerNames(), 30000);
        chanceCardController = new ChanceCardController();
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

    public void drawCard(){

        guiController.displayChanceCard(chanceCardController.drawCard());

        switch (chanceCardController.getCurrentCardType()){
            case "BankTransaction":

                // TODO: Make use of Game.java's makeTransaction instead of the playerController's - bankruptcy + GUI update.

                playerController.makeTransaction(chanceCardController.getAmount(),playerTurn);
                break;

            case "CashFromPlayer":

                /* TODO: Re-write giftPlayer (so that all players make a transaction to target player - including the
                    target player.) Return a boolean array corresponding to the Player[] to check if transactions failed.
                  */

                playerController.giftPlayer(playerTurn,chanceCardController.getAmount());
                break;
            /*
            case "HouseTax":

                // TODO: Make use of Game.java's makeTransaction - bankruptcy + GUI update.
                int houses = fieldController.getPlayerHouses(playerTurn);
                int hotels = fieldController.getPlayerHotels(playerTurn);
                int fine = houses * chanceCardController.getHouseTax() + hotels * chanceCardController.getHotelTax();
                playerController.makeTransaction(-fine, playerTurn);

                break;
            */
            /*
            case "Lottery":
                // TODO: getPlayerTotalValue.
                int threshold = chanceCardController.getThreshold();
                if (getPlayerTotalValue(playerTurn)<threshold){
                    playerController.makeTransaction(chanceCardController.getAmount(),playerTurn);
                    guiController.showMessage(chanceCardController.getSuccessText());
                } else { guiController.showMessage(chanceCardController.getFailText()); }
                break;
            */
            /*
            case "MovePlayer":
                int increment = chanceCardController.getIncrement();
                if (increment < 0){
                    moveBackwards(playerTurn,increment);
                } else {
                    movePlayer(playerTurn,increment);
                }
                break;
            */
            case "MovePlayerToTile":
                int delta = chanceCardController.getDestination() - playerController.getPlayerPosition(playerTurn);
                if (delta < 0){ delta += fieldController.getFields().length; }
                movePlayer(playerTurn,delta);

                break;

            /*
             *  case "GetOutOfJailCard":
             *  case "GoToJailCard":
             *  case "MoveShipping":
             */
        }

    }

    /*public void setPlayerPosition(int player, int position){
        playerController.setPlayerPosition(player, position);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
    }

    public void checkStartPass(int player, int increment){
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
    }

    public boolean sellProperty(int player, int place){
        // TODO : make a check for if the property exists
        int[] properties = playerController.getProperties(player);
        if(Arrays.stream(properties).anyMatch(i -> i == place )){
            playerController.removeProperty(player, place);
            return true;
        }
        return false;
    }

    public boolean buyProperty(int player, int place){
        // TODO : make a check for if the property is not owned
        int[] properties = playerController.getProperties(player);
        if(Arrays.stream(properties).anyMatch(i -> i == place )){
            playerController.addProperty(player, place);
            return true;
        }
        return false;
    }

    public String[] getPlayerNames(){
        return guiController.returnPlayerNames();
    }*/

    public void fieldAction(int position){
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
