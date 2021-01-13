package game;

import game.controllers.*;
import game.field.*;

import java.util.Arrays;

public class Game {

    private final PlayerController playerController;
    private final GUIController guiController;
    private final DiceController diceController;
    private final FieldController fieldController;
    private final ChanceCardController chanceCardController;
    private final int playerCount;
    private int playerTurn;
    private int playerTurnIndex; // look at setPlayerTurn for info
    private final StringHandler stringHandler;

    public Game() {
        fieldController = new FieldController();
        guiController = new GUIController(fieldController.getFields());
        diceController = new DiceController(2, 6);
        playerController = new PlayerController(guiController.returnPlayerNames(), 30000);
        chanceCardController = new ChanceCardController();
        playerCount = playerController.getPlayers().length;
        playerTurn = (int) Math.round((Math.random() * (playerCount - 1)));
        playerTurnIndex = playerTurn;
        stringHandler = new StringHandler("src/main/resources/stringRefs.xml");
    }

    public void gameLoop() {

        // Variable for keeping track of whether a winner has been found
        boolean stop = false;

        // Ask users for player info
        guiController.addPlayers(playerController.getPlayers());

        do {

            // Announce whose turn it is
            guiController.showMessage(stringHandler.getString("playerTurn") + playerController.getName(playerTurn));

            // If the player has permission to build on their properties, give them the opportunity
            if (fieldController.canPlayerBuyHouses(playerTurn)) buildOnStreets();

            // Check if player is on jail field
            if (playerController.getPlayerPosition(playerTurn) == fieldController.getJailPosition()) {

                // Check whether player is actually in prison or just visiting
                if (fieldController.isInJail(playerTurn)) {

                    // Give them the opportunity to get out of jail
                    if (!getOutOfJail()) {

                        // If they didn't succeed, continue to the next player
                        getNextPlayerTurn();
                        continue;
                    }
                }
            }

            // Roll the dice and move the resulting number of fields forward
            rollDice();
            movePlayer(playerTurnIndex, diceController.getSum());

            // Execute the fieldAction of that field
            stop = !fieldAction(playerController.getPlayerPosition(playerTurn), playerTurn);

            // If the player rolled two identical dice, they get an extra turn
            if (diceController.isIdentical()) {
                guiController.showMessage(stringHandler.getString("extraTurnIdenticalDice"));

            } else { // Otherwise move on to the next player
                getNextPlayerTurn();
            }

            // Keep playing until a winner is found
        } while (!stop);

        // Show message announcing winner
        guiController.showMessage(stringHandler.getString("winnerFound") + playerController.getName(0));

        // Close the window when the game is over
        guiController.close();

        // Stop the program with exit code 0 (OK)
        System.exit(0);
    }

    // Methods related to fields.
    private boolean fieldAction(int position, int player) {
        FieldInstruction instructions = fieldController.fieldAction(position);

        switch (instructions.getFieldType()) {

            case "Brewery":
            case "Street":
            case "Shipping":
                return propertyFieldAction(position, player, instructions);

            case "Chance":
                drawCard();
                break;

            case "GoToJail":
                return goToJailFieldAction(player, instructions.getJailPosition());

            case "Jail":
                guiController.showMessage(stringHandler.getString("justVisitingJail"));
                break;

            case "Parking":
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

    private boolean buyProperty(int player, int place, int rent) {
        boolean propertyLevelChanged = fieldController.buyProperty(player, place);
        playerController.addProperty(player, place);
        guiController.fieldOwnable(place, player, rent);

        if (propertyLevelChanged) {

            int groupIndex = fieldController.getPropertyGroupIndex(place);
            Property[] group = fieldController.getPropertyGroup(groupIndex);

            for (Property property : group) {
                if (property.getOwner() == player)
                    guiController.setRent(property.getPosition(), property.getCurrentRent());
            }
        }
        return true;
    }

    private boolean sellProperty(int player, int place) {
        // TODO : make a check for if the property exists
        int[] properties = playerController.getProperties(player);
        if (Arrays.stream(properties).anyMatch(i -> i == place)) {
            playerController.removeProperty(player, place);
            makeTransaction(((Street) fieldController.getFields()[place]).getCost(), player);
            guiController.removeRentOwnership(place);
            fieldController.sellPropety(place);
            return true;
        }
        return false;
    }

    private boolean goToJailFieldAction(int player, int jailPosition) {

        guiController.showMessage(stringHandler.getString("goToJail"));
        playerController.setPlayerPosition(player, jailPosition);
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

                int subtract = (playerValue / 10);
                guiController.showMessage(stringHandler.getString("playerValue") + subtract + " kr.");

                playerController.makeTransaction(-subtract, player);
                guiController.makeTransaction(-subtract, player);
            }
        }
        return true;
    }

    private void buildOnStreets() {

        // Get properties on which the player can build, and keep doing it until they don't have any left, or say no
        for (Street[] streets = getBuildableStreets(playerTurn); streets.length > 0; streets = getBuildableStreets(playerTurn)) {

            // Ask if the player want to buy houses
            if (guiController.getUserButton(stringHandler.getString("askBuyHouse"),
                    stringHandler.getString("yes"), stringHandler.getString("no"))
                    .equals(stringHandler.getString("no")))
                break;

            // Get an array of strings, which shows the name of the street and the price to put a house on it
            String[] houseCostButtons = getHouseCostButtons(streets);

            // Ask the player which street they want to build on
            String streetToBuyHouse = guiController.getUserButton(stringHandler.getString("whereToBuyHouse"), houseCostButtons);

            // Extract street name from button text by stripping text from the end
            // e.g. 'Rødovrevej: 1000 kr.' -> 'Rødovrevej'
            streetToBuyHouse = streetToBuyHouse.substring(0, streetToBuyHouse.length() - 10);

            // Find the street that the user wants to build a building on
            for (Street street : streets) {

                // If it isn't the street that player wants to build on, continue to next street
                if (!street.getTitle().equals(streetToBuyHouse)) continue;

                //Check if they have money for it
                if (playerController.makeTransaction(-street.getBuildingCost(), playerTurn)) {

                    //Increase the streets propertyLevel
                    street.setPropertyLevel(street.getPropertyLevel() + 1);

                    // Update the GUI with the new rent
                    guiController.setRent(street.getPosition(), street.getCurrentRent());

                    // Update the players balance in GUI
                    setGuiBalance(playerController.getPlayerBalance(playerTurn), playerTurn);

                    // Update the property with new buildings in GUI
                    if (street.getPropertyLevel() == 6) {
                        guiController.setHouseOrHotelStreet(street.getPosition(), 0, true);
                    } else {
                        guiController.setHouseOrHotelStreet(street.getPosition(), street.getPropertyLevel() - 1, false);
                    }
                }

                // Stop loop
                break;
            }
        }
    }

    private String[] getHouseCostButtons(Street[] properties) {
        String[] houseCostButtons = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            houseCostButtons[i] = properties[i].getTitle() + ": " + properties[i].getBuildingCost() + " kr.";
        }

        return houseCostButtons;
    }

    private Street[] getBuildableStreets(int player) {

        int playerBalance = playerController.getPlayerBalance(player);
        return fieldController.getBuildableStreets(player, playerBalance);
    }

    private boolean getOutOfJail() {
        //TODO: Take into account 'get out of jail free' card when it's implemented

        // Announce that player is in prison
        guiController.showMessage(stringHandler.getString("isInJail"));

        // Get bail
        int bail = ((Jail) fieldController.getFields()[playerController.getPlayerPosition(playerTurn)]).getBail();

        // If the player has used all their tries, they have to pay the bail
        if (playerController.getGetOutOfJailTries(playerTurn) == 3) {
            playerController.setGetOutOfJailTries(playerTurn, 0);
            guiController.showMessage(stringHandler.getString("jailTriesUsed"));
            return makeTransaction(-bail, playerTurn);
        }

        // Ask player if they want to pay bail, or try to roll two identical
        if (guiController.getUserButton(stringHandler.getString("inJailOptions"), "1", "2").equals("1")) {

            // Roll the dice
            rollDice();

            // If dice are identical, free player
            if (diceController.isIdentical()) {
                guiController.showMessage(stringHandler.getString("jailIdenticalDice"));
                fieldController.free(playerTurn);
                playerController.setGetOutOfJailTries(playerTurn, 0);
                return true;


            } else { // If they aren't, the player has to stay in jail for the round
                guiController.showMessage(stringHandler.getString("jailNotIdenticalDice"));
                playerController.incrementGetOutOfJailTries(playerTurn);
                return false;
            }

        } else { // Player wants to bay bail
            return makeTransaction(-bail, playerTurn);
        }
    }

    // Methods related to chanceCards.

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

            case "HouseTax":

                int houses = fieldController.getHouses(playerTurn);
                int hotels = fieldController.getHotels(playerTurn);
                int fine = houses * chanceCardController.getHouseTax() + hotels * chanceCardController.getHotelTax();
                success = makeTransaction(-fine, playerTurn);

                break;


            case "Lottery":

                int threshold = chanceCardController.getThreshold();
                if (getPlayerTotalValue(playerTurn) <= threshold) {
                    makeTransaction(chanceCardController.getAmount(), playerTurn);
                    guiController.showMessage(chanceCardController.getSuccessText());
                } else {
                    guiController.showMessage(chanceCardController.getFailText());
                }
                break;


            case "MovePlayer":

                movePlayer(playerTurn, chanceCardController.getIncrement());

                break;

            case "MovePlayerToTile":

                int delta = chanceCardController.getDestination() - playerController.getPlayerPosition(playerTurn);
                if (delta < 0) delta += fieldController.getFields().length;
                movePlayer(playerTurn, delta);

                break;

            case "OutOfJailCard":

                playerController.players[playerTurn].setOutOfJailCards(1);

                break;

            case "GoToJailCard":

                goToJailFieldAction(playerTurn, chanceCardController.getJailPosition());

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

    private void moveToNearestShipping(int[] shippingLocations, boolean forward, boolean doubleRent) {
        int currentPosition = playerController.players[playerTurn].getCurrentPosition();
        if (forward) {
            if (currentPosition > shippingLocations[3]) {
                movePlayer(playerTurn, (5 - currentPosition) % fieldController.getFields().length);
            } else {
                for (int i = 0; i < 4; i++) {
                    if (shippingLocations[i] > currentPosition) {
                        movePlayer(playerTurn, shippingLocations[i] - currentPosition);
                        break;
                    }
                }
            }
        } else {
            int relativePositionToShipping = (currentPosition + 5) % 10;
            if (relativePositionToShipping < 5) {
                movePlayer(playerTurn, -relativePositionToShipping);
            } else {
                movePlayer(playerTurn, 10 - relativePositionToShipping);
            }
        }
        currentPosition = playerController.players[playerTurn].getCurrentPosition();
        fieldAction(currentPosition, playerTurn);
        // TODO: Add second condition on this - if field is owned.
        if (doubleRent) {
            fieldAction(currentPosition, playerTurn);
        }
    }

    private boolean giftPlayer(int amount, int targetPlayer) {
        boolean transactionSuccess = playerController.giftPlayer(amount, targetPlayer);
        for (int i = 0; i < playerCount; i++) {
            updateGuiBalance(i);
        }
        return transactionSuccess;
    }


    // Methods related to players.

    private String[] getPlayerNames() {
        return guiController.returnPlayerNames();
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

    private void movePlayer(int player, int increment) {
        playerController.movePlayer(player, increment);
        guiController.setCarPlacement(player, playerController.getPreviousPlayerPosition(player), playerController.getPlayerPosition(player));
        if (playerController.getPlayerPosition(player) < playerController.getPreviousPlayerPosition(player) && increment > 0) {
            setGuiBalance(playerController.getPlayerBalance(player), player);
        }
    }

    private void removePlayer(int player, int fieldPlacement) {
        // TODO: Add setPlayers() in PlayerController
        // playerController.setPlayers(playerController.removePlayer(player));

        guiController.removeGuiPlayer(playerTurnIndex, fieldPlacement);
        playerTurn = playerTurn - 1;
    }

    private int getPlayerTotalValue(int player) {
        return playerController.getPlayerBalance(player) + fieldController.getCombinedPropertyWorth(player);
    }


    private void getNextPlayerTurn() {
        playerTurn = (playerTurn + 1) % playerCount;
        setPlayerTurn();
    }

    /**
     * This method is used, to get the players index after removal of players in the original array
     */
    private void setPlayerTurn() {
        for (int i = 0; i < playerCount; i++) {
            if (playerController.players[i].getId() == playerTurn) {
                playerTurnIndex = i;
            }
        }
    }

    /*
    public void setPlayerPosition(int player, int position){
        playerController.setPlayerPosition(player, position);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
    }
     */

    // Miscellaneous.

    private void rollDice() {
        guiController.showMessage(stringHandler.getString("rollDice"));
        diceController.roll();

        guiController.setDiceGui(diceController.getFaceValue(0), (int) (Math.random() * 360), diceController.getFaceValue(1), ((int) (Math.random() * 360)));
    }

    // TODO: Might be redundant later.
    private void updateGuiBalance(int player) {
        setGuiBalance(playerController.players[player].getBalance(), player);
    }

    private void setGuiBalance(int amount, int player) {
        guiController.setBalance(amount, player);
    }
}
