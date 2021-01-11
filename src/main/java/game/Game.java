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

    private void gameLoop(){
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

    private void movePlayer(int player, int increment){
        playerController.movePlayer(player, increment);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
        if(playerController.getPlayerPosition(player) < playerController.getOldPlayerPosition(player) && increment > 0){
            setGuiBalance(playerController.getPlayerBalance(player),player);
        }
    }

    public boolean drawCard(){

        guiController.displayChanceCard(chanceCardController.drawCard());
        boolean success = true;
        switch (chanceCardController.getCurrentCardType()){
            case "BankTransaction":

                success = makeTransaction(chanceCardController.getAmount(), playerTurn);
                break;

            case "CashFromPlayer":

                success = giftPlayer(chanceCardController.getAmount(), playerTurn);
                break;
            /*
            case "HouseTax":

                // TODO: getPlayerHouses and getPlayer
                int houses = fieldController.getPlayerHouses(playerTurn);
                int hotels = fieldController.getPlayerHotels(playerTurn);
                int fine = houses * chanceCardController.getHouseTax() + hotels * chanceCardController.getHotelTax();
                success = makeTransaction(-fine, playerTurn);

                break;
            */

            case "Lottery":
                int threshold = chanceCardController.getThreshold();
                if (getPlayerTotalValue(playerTurn) <= threshold){
                    makeTransaction(chanceCardController.getAmount(),playerTurn);
                    guiController.showMessage(chanceCardController.getSuccessText());
                } else { guiController.showMessage(chanceCardController.getFailText()); }
                break;


            case "MovePlayer":
                movePlayer(playerTurn,chanceCardController.getIncrement());

                break;

            case "MovePlayerToTile":
                int delta = chanceCardController.getDestination() - playerController.getPlayerPosition(playerTurn);
                if (delta < 0){ delta += fieldController.getFields().length; }
                movePlayer(playerTurn,delta);

                break;

            case "OutOfJailCard":
                players[playerTurn].setOutOfJailCards(1);

                break;

            case "GoToJailCard":
                // TODO: Update with correct method name after merge to Dev.
                goToJail(playerTurn,chanceCardController.getJailPosition());

                break;

            case "MoveToNearestShipping":
                int[] shippingPositions = chanceCardController.getShippingLocations();
                boolean forward = chanceCardController.getForward();
                boolean doubleRent = chanceCardController.getDoubleRent();
                moveToNearestShipping(shippingPositions, forward, doubleRent);

                break;
        }
        return success;
    }

    private void moveToNearestShipping(int[] shippingLocations, boolean forward, boolean doubleRent){
        int currentPosition = players[playerTurn].getCurrentPosition();
        if (forward) {
            if (currentPosition > shippingLocations[3]){
                movePlayer(playerTurn, (5 - currentPosition) % fieldController.getFields().length);
            } else {
                for (int i = 0; i < 4; i++) {
                    if (shippingLocations[i] > currentPosition) {
                        movePlayer(playerTurn, shippingLocations[i] - currentPosition);
                        break;
                    }
                }
            }
        }
        else {
            int relativePositionToShipping = (currentPosition + 5) % 10;
            if (relativePositionToShipping < 5){
                movePlayer(playerTurn, -relativePositionToShipping);
            } else {
                movePlayer(playerTurn, 10 - relativePositionToShipping);
            }
        }
        currentPosition = players[playerTurn].getCurrentPosition();
        fieldAction(currentPosition);
        // TODO: Add second condition on this - if field is owned.
        if(doubleRent){
            fieldAction(currentPosition);
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

    private void fieldAction(int position){
    }

    private int getNextPlayerTurn(){
        playerTurn = (playerTurn + 1) % players.length;
        return playerTurn;
    }

    /*public int getPlayerTurn(){
        return 0;
    }

    public boolean hasWinner(){
        return false;
    }*/

    private int getPlayerTotalValue(int player){
        return playerController.getPlayerBalance(player) + fieldController.getCombinedPropertyWorth(player);
    }

    private boolean giftPlayer(int amount, int targetPlayer){
        boolean transactionSuccess = playerController.giftPlayer(amount,targetPlayer);
        for (int i = 0; i < players.length; i++) {
            updateGuiBalance(i);
        }
        return transactionSuccess;
    }

    private boolean makeTransaction(int amount, int player){
        boolean transactionSuccess = playerController.makeTransaction(amount, player);
        updateGuiBalance(player);
        return transactionSuccess;
    }

    private boolean makeTransaction(int amount, int sender, int receiver){
        boolean transactionSuccess = makeTransaction(-amount,sender);
        makeTransaction(amount,receiver);
        return transactionSuccess;
    }

    // TODO: Might be redundant later.
    private void updateGuiBalance(int player){
        setGuiBalance(players[player].getBalance(), player);
    }

    private void setGuiBalance(int amount, int player){
        guiController.setBalance(amount, player);
    }
}
