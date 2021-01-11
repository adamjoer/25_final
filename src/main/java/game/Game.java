package game;

import game.controllers.ChanceCardController;
import game.field.Field;
import game.field.FieldInstruction;
import game.field.TaxField;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

public class Game {

    private final PlayerController playerController;
    private final GUIController guiController;
    private final DiceController diceController;
    private final FieldController fieldController;
    private final ChanceCardController chanceCardController;
    private Player[] players;
    private int playerTurn;
    private int playerTurnIndex; // look at setPlayerTurn for info
    private final int[] getOutOfJailTries;
    private final StringHandler stringHandler;

    public Game(){
        fieldController = new FieldController();
        guiController = new GUIController(fieldController.getFields());
        diceController = new DiceController(2,6);
        playerController = new PlayerController(guiController.returnPlayerNames(), 30000);
        chanceCardController = new ChanceCardController();
        players = playerController.getPlayers();
        playerTurn = (int) (Math.random() * (players.length - 1));
        playerTurnIndex = playerTurn;
        getOutOfJailTries = new int[players.length];
        stringHandler = new StringHandler("src/main/resources/stringRefs.xml");
    }

    public void gameLoop() {
        boolean stop = false;

        guiController.addPlayers(players);

        do {
            guiController.showMessage(stringHandler.getString("playerTurn") + playerController.getName(playerTurn));

            // Check if player is on jail field
            if (playerController.getPlayerPosition(playerTurn) == fieldController.getJailPosition()) {

                // Check whether player is actually in prison or just visiting
                if (fieldController.isInJail(playerTurn)) {
                    if (!getOutOfJail()) {
                        playerTurn = getNextPlayerTurn();
                        continue;
                    }
                }
            }

            // Cast dice from dice controller
            guiController.getUserButton(stringHandler.getString("rollDice"), "OK");
            diceController.roll();

            guiController.setDiceGui(diceController.getFaceValue(0), (int) (Math.random() * 360), diceController.getFaceValue(1), ((int) (Math.random() * 360)));

            movePlayer(playerTurnIndex, diceController.getSum());

            stop = !fieldAction(playerController.getPlayerPosition(playerTurn), playerTurn);

            if (!diceController.isIdentical()) {
                playerTurn = getNextPlayerTurn();

            } else {
                guiController.showMessage(stringHandler.getString("extraTurnIdenticalDice"));
            }

        } while (!stop);
        guiController.close();
    }

    private boolean getOutOfJail() {
        //TODO: Take into account 'get out of jail free' card when it's implemented

        guiController.showMessage(stringHandler.getString("isInJail"));

        if (getOutOfJailTries[playerTurn] == 3) {
            getOutOfJailTries[playerTurn] = 0;
            guiController.showMessage(stringHandler.getString("jailTriesUsed"));
            return makeTransaction(-1000, playerTurn);
        }

        String returnBtn = guiController.getUserButton(stringHandler.getString("inJailOptions"), "1", "2");
        if (returnBtn.equals("1")) {

            guiController.getUserButton(stringHandler.getString("rollDice"), "OK");
            diceController.roll();

            guiController.setDiceGui(diceController.getFaceValue(0), (int) (Math.random() * 360), diceController.getFaceValue(1), ((int) (Math.random() * 360)));

            if (diceController.isIdentical()) {
                guiController.showMessage(stringHandler.getString("jailIdenticalDice"));
                fieldController.free(playerTurn);
                getOutOfJailTries[playerTurn] = 0;
                return true;

            } else {
                guiController.showMessage(stringHandler.getString("jailNotIdenticalDice"));
                getOutOfJailTries[playerTurn]++;
                return false;
            }

        } else {
            return makeTransaction(-1000, playerTurn);
        }
    }

    private void movePlayer(int player, int increment) {
        playerController.movePlayer(player, increment);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());

        if (playerController.getPlayerPosition(player) < playerController.getPreviousPlayerPosition(player) && increment > 0) {
            setGuiBalance(playerController.getPlayerBalance(player), player);
        }
    }

    public void removePlayer(int player, int fieldPlacement){
        players = playerController.removePlayer(player);

        guiController.removeGuiPlayer(playerTurnIndex, fieldPlacement);
        playerTurn = playerTurn-1;
    }

    private boolean drawCard() {

        guiController.displayChanceCard(chanceCardController.drawCard());
        boolean success = true;
        switch (chanceCardController.getCurrentCardType()) {
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
            /*
            case "Lottery":
                // TODO: getPlayerTotalValue.
                int threshold = chanceCardController.getThreshold();
                if (getPlayerTotalValue(playerTurn)<threshold){
                    makeTransaction(chanceCardController.getAmount(),playerTurn);
                    guiController.showMessage(chanceCardController.getSuccessText());
                } else { guiController.showMessage(chanceCardController.getFailText()); }
                break;
            */

            case "MovePlayer":
                movePlayer(playerTurn, chanceCardController.getIncrement());

                break;

            case "MovePlayerToTile":
                int delta = chanceCardController.getDestination() - playerController.getPlayerPosition(playerTurn);
                if (delta < 0) delta += fieldController.getFields().length;
                movePlayer(playerTurn, delta);

                break;

            /*
             *  case "GetOutOfJailCard":
             *  case "GoToJailCard":
             *  case "MoveShipping":
             */
        }
        return success;
    }

    /*private void setPlayerPosition(int player, int position){
        playerController.setPlayerPosition(player, position);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
    }

    private void checkStartPass(int player, int increment){
        if (playerController.getPlayerPosition(player) > playerController.getCurrentPosition(player) + increment){
            giveStartPassReward(player);
        }
    }

    private boolean giveStartPassReward(int player){
        if (fieldController.hasPassedGo(playerController.getOldPlayerPosition(player), playerController.getPlayerPosition(player))){
            playerController.setPlayerBalance(player, playerController.makeTransaction(player, 4000));
            return true;
        }
        return false;
    }

    private void removePlayer(int player){
        // When a player becomes broke, the player needs to be removed from the game
        guiController.showMessage("%s has been removed from the game", players[player].getName());
        players = ArrayUtils.removeElement(players, players[player]);
        guiController.setCar(player, false);
    }*/

    private boolean sellProperty(int player, int place) {
        // TODO : make a check for if the property exists
        int[] properties = playerController.getProperties(player);
        if (Arrays.stream(properties).anyMatch(i -> i == place)) {
            playerController.removeProperty(player, place);
            return true;
        }
        return false;
    }

    private boolean buyProperty(int player, int place, int rent) {
        // TODO : make a check for if the property is not owned
        //int[] properties = playerController.getProperties(player);
        //if(Arrays.stream(properties).anyMatch(i -> i == place )){
        fieldController.buyProperty(player, place);
        playerController.addProperty(player, place);
        guiController.fieldOwnable(place, player, rent);
        return true;
        //}
        //return false;
    }

    private String[] getPlayerNames() {
        return guiController.returnPlayerNames();
    }

    private boolean fieldAction(int position, int player) {
        FieldInstruction instructions = fieldController.fieldAction(position);

        switch (instructions.getFieldType()) {

            case "Brewery":
            case "Street":
            case "Shipping":
                return propertyFieldAction(position, player, instructions);

            case "Chance":
                break;

            case "GoToJail":
                 return goToJailFieldAction(player, instructions);

            case "Jail":
                guiController.showMessage(stringHandler.getString("justVisitingJail"));
                break;

            case "Parking":
                break;

            case "Start":
                break;

            case "TaxField":
                return taxFieldAction(position, player);

            default:
                throw new IllegalArgumentException("Field type '" + instructions.getFieldType() + "' not recognised");

        }
        return true;
    }

    private boolean propertyFieldAction(int position, int player, FieldInstruction instructions) {

        //Check if the field is owned by the player
        if (player == instructions.getOwner()) {
            guiController.showMessage(stringHandler.getString("ownField"));
            return true;
        }

        //Check if the field is owned by the bank
        else if (instructions.getOwner() == -1) {

            //If field is owned by the bank, ask player if they want to buy it
            if (guiController.getUserButton(stringHandler.getString("buyField"), "Ja", "Nej").equals("Ja")) {

                //If they want to buy it, check if they have money for it
                if (playerController.makeTransaction(-instructions.getCost(), player)) {
                    buyProperty(player, position, instructions.getRent());
                    guiController.setBalance(playerController.getPlayerBalance(player), player);
                } else {
                    guiController.showMessage(stringHandler.getString("noMoney"));
                }
            }
            return true;
        }

        //Field is owned by another player, so they have to pay rent
        else {
            int owner = instructions.getOwner();
            guiController.showMessage(stringHandler.getString("payRent") + playerController.getName(owner));

            //Make transaction from the current player to the owner of the field
            boolean successfulRent = playerController.makeTransaction(instructions.getRent(), player, owner);

            //Set the balance of both players in the GUI
            guiController.setBalance(playerController.getPlayerBalance(player), player);
            guiController.setBalance(playerController.getPlayerBalance(owner), owner);

            return successfulRent;
        }
    }

    private boolean goToJailFieldAction(int player, FieldInstruction instructions) {

        guiController.showMessage(stringHandler.getString("goToJail"));
        playerController.setPlayerPosition(player, instructions.getJailPosition());
        guiController.setCarPlacement(player, playerController.getPreviousPlayerPosition(player), playerController.getPlayerPosition(player));
        fieldController.incarcerate(player);
        return true;
    }

    private boolean taxFieldAction(int position, int player) {
        TaxField currentField = (TaxField) fieldController.getFields()[position];
        int fine = currentField.getFine();
        int playerValue = fieldController.getCombinedPropertyWorth(player) + playerController.getPlayerBalance(player);
        if (currentField.getTitle().equals("Statsskat")) {
            guiController.showMessage(stringHandler.getString("stateTax"));
            playerController.makeTransaction(-currentField.getFine(), player);
            guiController.makeTransaction(-currentField.getFine(), player);
        } else {
            guiController.showMessage(stringHandler.getString("tax"));
            String playerChoice = guiController.getUserButton(stringHandler.getString("taxOptions"),
                    "1", "2");
            if (playerChoice.equals("1")) {
                playerController.makeTransaction(-fine, player);
                guiController.makeTransaction(-fine, player);
            } else {

                int subtract = (int) (playerValue * 0.10);
                guiController.showMessage(stringHandler.getString("playerValue") + subtract);

                playerController.makeTransaction(-subtract, player);
                guiController.makeTransaction(-subtract, player);
            }
        }
        return true;
    }

    private int getNextPlayerTurn() {
        playerTurn = (playerTurn + 1) % players.length;
        setPlayerTurn();
        return playerTurn;
    }

    /*private int getPlayerTurn(){
        return 0;
    }

    private boolean hasWinner(){
        return false;
    }*/

    private boolean giftPlayer(int amount, int targetPlayer) {
        boolean transactionSuccess = playerController.giftPlayer(amount, targetPlayer);
        for (int i = 0; i < players.length; i++) {
            updateGuiBalance(i);
        }
        return transactionSuccess;
    }

    private boolean makeTransaction(int amount, int player) {
        boolean transactionSuccess = playerController.makeTransaction(amount, player);
        updateGuiBalance(player);
        return transactionSuccess;
    }

    private boolean makeTransaction(int amount, int sender, int receiver) {
        boolean transactionSuccess = makeTransaction(-amount, sender);
        makeTransaction(amount, receiver);
        return transactionSuccess;
    }

    // TODO: Might be redundant later.
    private void updateGuiBalance(int player) {
        setGuiBalance(players[player].getBalance(), player);
    }

    private void setGuiBalance(int amount, int player) {
        guiController.setBalance(amount, player);
    }

    /**
     * This method is used, to get the players index after removal of players in the original array
     */
    private void setPlayerTurn(){
        for(int i=0;i<players.length;i++){
            if(players[i].getId() == playerTurn){
                playerTurnIndex = i;
            }
        }
    }
}
