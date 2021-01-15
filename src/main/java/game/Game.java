package game;

import game.controller.*;
import game.field.*;

import java.util.Arrays;

public class Game {

    private final PlayerController playerController;
    private final GUIController guiController;
    private final DiceController diceController;
    private final FieldController fieldController;
    private final ChanceCardController chanceCardController;
    private int playerCount;
    private int playerTurn;

    public Game() {
        fieldController = new FieldController();
        guiController = new GUIController(fieldController.getFields());
        diceController = new DiceController(2, 6);
        playerController = new PlayerController(guiController.returnPlayerNames(), 30000);
        chanceCardController = new ChanceCardController();
        playerCount = playerController.getPlayers().length;
        playerTurn = (int) Math.round((Math.random() * (playerCount - 1)));
    }

    public void gameLoop() {

        // Ask users for player info
        guiController.addPlayers(playerController.getPlayers());

        do {

            // Announce whose turn it is
            guiController.stringHandlerMessage("playerTurn", true, playerController.getName(playerTurn));

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
            if (fieldController.playerHasPawnedProperties(playerTurn)) reclaimProperties();
                // If the player has permission to build on their properties, give them the opportunity
            else if (fieldController.canPlayerBuyHouses(playerTurn)) buildOnStreets();

            // Roll the dice and move the resulting number of fields forward
            rollDice();

            if(playerController.getPlayers()[playerTurn].getName().equals("A") && playerController.getPlayers()[playerTurn].getBalance() >0){
                movePlayer(playerTurn, 30);
            } else{
                movePlayer(playerTurn, diceController.getSum());
            }

            // Execute the fieldAction of that field
            fieldAction(playerController.getPlayerPosition(playerTurn), playerTurn);

            // If the player rolled two identical dice, they get an extra turn
            if (diceController.isIdentical())
                guiController.stringHandlerMessage("extraTurnIdenticalDice", true);

                // Otherwise move on to the next player
            else getNextPlayerTurn();

            for(int i=0; i<playerController.getPlayers().length;i++){
                if(playerController.getPlayers()[i].getBalance() < 0){
                    //The following if check is so that, we dont have to make the for loop in isIdentical AND in the following else
                    if(playerTurn == 0){
                        playerTurn = playerCount-1; //-1 because it needs to be an index
                    } else{
                        playerTurn = playerTurn-1;
                    }
                    removePlayer(playerTurn, playerController.getPlayers()[i].getCurrentPosition());
                }
            }

        } while (playerCount>1); // Keep playing until a winner is found

        // Show message announcing winner
        guiController.stringHandlerMessage("winnerFound", true, playerController.getName(playerTurn));

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
                guiController.stringHandlerMessage("justVisitingJail", true);
                break;

            case "Parking":
            case "Start":
                break;

            case "TaxField":
                return taxFieldAction(player, instructions);

            default:
                throw new IllegalArgumentException("Field type '" + instructions.getFieldType() + "' not recognised");

        }
        return true;
    }

    private boolean propertyFieldAction(int position, int player, FieldInstruction instructions) {

        // Check if the field is owned by the player
        if (player == instructions.getOwner()) {
            guiController.stringHandlerMessage("ownField", true);
            return true;
        }

        // Check if the field is owned by the bank
        if (instructions.getOwner() == -1) {

            // If field is owned by the bank, ask player if they want to buy it
            String yesButton = guiController.stringHandlerMessage("yes", false);
            if (guiController.getUserButton(guiController.stringHandlerMessage("buyField", false),
                    yesButton, guiController.stringHandlerMessage("no", false))
                    .equals(yesButton)) {

                // If they want to buy it, check if they have money for it
                if (playerController.checkLiquidity(instructions.getCost(), player)) {
                    makeTransaction(-instructions.getCost(), player);
                    buyProperty(player, position, instructions.getRent());
                    updateGuiBalance(player);

                } else {
                    guiController.stringHandlerMessage("noMoney", true);
                }
            }
            return true;

        } else { // Field is owned by another player, so they have to pay rent

            int owner = instructions.getOwner();
            guiController.stringHandlerMessage("payRent", true, playerController.getName(owner));

            int rent = instructions.getRent();

            // If the property is a brewery, the rent needs to be multiplied by the sum of the dice
            if (instructions.getFieldType().equals("Brewery")) {

                // Calculate new rent
                rent = rent * diceController.getSum();

                // Announce new rent to player
                guiController.stringHandlerMessage("breweryRent", true, rent + " kr.");
            }

            // Make transaction from the current player to the owner of the field
            boolean successfulRent = makeTransaction(rent, player, owner);

            // Set the balance of both players in the GUI
            updateGuiBalance(player);
            updateGuiBalance(owner);

            return successfulRent;
        }
    }

    private void buyProperty(int player, int place, int rent) {
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
    }

    private void reclaimProperties() {
        String yesButton = guiController.stringHandlerMessage("yes", false),
                noButton = guiController.stringHandlerMessage("no", false),
                askText = guiController.stringHandlerMessage("askReclaimProperty", false);


        for (Property[] properties = getReclaimableProperties(playerTurn); properties.length > 0;
             properties = getReclaimableProperties(playerTurn)) {
            if (guiController.getUserButton(askText, yesButton, noButton)
                    .equals(noButton))
                break;

            String[] reclaimCostButtons = getReclaimCostButtons(properties);

            String propertyToReclaim = guiController.getUserButton(guiController.stringHandlerMessage
                    ("reclaimPropertyPrompt", false), reclaimCostButtons);

            propertyToReclaim = propertyToReclaim.substring(0, propertyToReclaim.length() - 10);

            for (Property property : properties) {
                if (!property.getTitle().equals(propertyToReclaim)) continue;
                else {
                    reclaimProperty(property.getPosition(), playerTurn);
                    updateGuiBalance(playerTurn);
                    break;
                }
            }
        }
    }

    private Property[] getReclaimableProperties(int player) {
        int playerBalance = playerController.getPlayerBalance(player);
        return fieldController.getReclaimableProperties(player, playerBalance);
    }

    private String[] getReclaimCostButtons(Property[] properties) {
        String[] reclaimCostButtons = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getPawnValue() < 1000) {
                reclaimCostButtons[i] = properties[i].getTitle() + ": " + properties[i].getPawnValue() + " kr. ";
            } else {
                reclaimCostButtons[i] = properties[i].getTitle() + ": " + properties[i].getPawnValue() + " kr.";
            }
        }
        return reclaimCostButtons;
    }

    private void reclaimProperty(int position, int player) {
        makeTransaction(-fieldController.reclaimProperty(player, position), player);
        guiController.setDescription(position, "propertyNotPawned");
        updateGuiRentForGroup(position);
    }

    private void sellRealEstate(int player) {
        int[] eligibleBuildings = fieldController.sellableBuildingPositions(player);
        int[] eligibleProperties = fieldController.sellablePropertyPositions(player);
        int[] eligiblePawns = fieldController.pawnablePropertyPositions(player);
        String[] guiOptions = new String[0];
        if (eligibleBuildings.length > 0) guiOptions = Utility.addToArray(guiOptions, "sellBuildingBtn");
        if (eligibleProperties.length > 0) guiOptions = Utility.addToArray(guiOptions, "sellPropertyBtn");
        if (eligiblePawns.length > 0)  guiOptions = Utility.addToArray(guiOptions, "pawnPropertyBtn");

        String selectedCase = guiController.sellRealEstatePrompt(guiOptions);
        switch (selectedCase) {
            case "sellBuildingBtn":
                sellBuilding(player, guiController.choosePropertyPrompt(eligibleBuildings, "sellBuildingPrompt"));
                break;
            case "sellPropertyBtn":
                sellProperty(player, guiController.choosePropertyPrompt(eligibleProperties, "sellPropertyPrompt"));
                break;
            case "pawnPropertyBtn":
                pawnProperty(player, guiController.choosePropertyPrompt(eligiblePawns, "pawnPropertyPrompt"));
                break;
            default:
                sellAllPlayerProperties(player);
        }
    }

    private void pawnProperty(int player, int position) {
        int pawnValue = fieldController.pawnProperty(player, position);
        guiController.setDescription(position, "propertyIsPawned");
        playerController.makeTransaction(pawnValue, player);
        updateGuiRentForGroup(position);
        updateGuiBalance(player);
    }

    private void sellBuilding(int player, int position) {
        int buildingValue = fieldController.sellBuilding(position);
        guiController.setHouseOrHotelStreet(position, fieldController.getHouses(player), false);
        playerController.makeTransaction(buildingValue, player);
        updateGuiRentForGroup(position);
        updateGuiBalance(player);
    }

    private boolean sellProperty(int player, int position) {
        int[] properties = fieldController.getPlayerPropertyPositions(player);
        if (Arrays.stream(properties).anyMatch(i -> i == position)) {
            int propertyCost = fieldController.disOwnProperty(player, position);
            if (propertyCost > 0) {
                playerController.makeTransaction(propertyCost, player);
                updateGuiBalance(player);
                guiController.removeRentOwnership(position);
                updateGuiRentForGroup(position);
                guiController.setDescription(position,"propertyNotPawned");
                return true;
            } else {
                guiController.showMessage(guiController.getUserString("stillHaveHouses"));
                return false;
            }

        }
        return false;
    }

    private void sellAllPlayerProperties(int player) {
        int valueOfProperties = fieldController.sellAllPlayerProperties(player);
        playerController.makeTransaction(valueOfProperties, player);
        updateGuiBalance(player);
    }

    private boolean goToJailFieldAction(int player, int jailPosition) {

        guiController.stringHandlerMessage("goToJail", true);
        playerController.setPlayerPosition(player, jailPosition);
        guiController.setCarPlacement(player, playerController.getPreviousPlayerPosition(player), playerController.getPlayerPosition(player));
        fieldController.incarcerate(player);
        return true;
    }

    private boolean taxFieldAction(int player, FieldInstruction instructions) {

        // Get tax
        int tax = instructions.getFine();

        // Check what kind of taxField it is
        if (instructions.getTitle().equals("Skat")) {

            // Give player options
            guiController.stringHandlerMessage("tax", true);
            String playerChoice = guiController.getUserButton(guiController.stringHandlerMessage("taxOptions", false), "1", "2");

            // If player chose to pay 10% of their worth, calculate it, and show the player
            if (playerChoice.equals("2")) {
                tax = (fieldController.getCombinedPropertyWorth(player) + playerController.getPlayerBalance(player)) / 10;
                guiController.stringHandlerMessage("playerValue", true, tax + " kr.");
            }

        }

        // Player doesn't have any options
        else guiController.stringHandlerMessage("stateTax", true);

        // Subtract tax from player balance
        boolean successfulFine = makeTransaction(-tax, player);
        updateGuiBalance(player);

        return successfulFine;
    }

    private void buildOnStreets() {

        String yesButton = guiController.stringHandlerMessage("yes", false),
                noButton = guiController.stringHandlerMessage("no", false),
                askText = guiController.stringHandlerMessage("askBuyHouse", false);

        // Get properties on which the player can build, and keep doing it until they don't have any left, or say no
        for (Street[] streets = getBuildableStreets(playerTurn); streets.length > 0; streets = getBuildableStreets(playerTurn)) {

            // Ask if the player want to buy houses
            if (guiController.getUserButton(askText, yesButton, noButton)
                    .equals(noButton))
                break;

            // Get an array of strings, which shows the name of the street and the price to put a house on it
            String[] houseCostButtons = getHouseCostButtons(streets);

            // Ask the player which street they want to build on
            String streetToBuyHouse = guiController.getUserButton(guiController.stringHandlerMessage("whereToBuyHouse", false), houseCostButtons);

            // Extract street name from button text by stripping text from the end
            // e.g. 'Strandvej: 1000 kr.' -> 'Strandvej'
            streetToBuyHouse = streetToBuyHouse.substring(0, streetToBuyHouse.length() - 10);

            // Find the street that the user wants to build a building on
            for (Street street : streets) {

                // If it isn't the street that player wants to build on, continue to next street
                if (!street.getTitle().equals(streetToBuyHouse)) continue;

                //Check if they have money for it
                if (playerController.checkLiquidity(street.getBuildingCost(), playerTurn)) {

                    //Pay the price of the building
                    makeTransaction(-street.getBuildingCost(), playerTurn);

                    //Increase the streets propertyLevel
                    street.setPropertyLevel(street.getPropertyLevel() + 1);

                    // Update the GUI with the new rent
                    guiController.setRent(street.getPosition(), street.getCurrentRent());

                    // Update the players balance in GUI
                    updateGuiBalance(playerTurn);

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

        // Announce that player is in prison
        guiController.stringHandlerMessage("isInJail", true);
        int getOutOfJailCards = playerController.getOutOfJailCards(playerTurn);
        // If player has 'get out of jail free' card, take it from them and free player
        if (getOutOfJailCards > 0) {
            guiController.stringHandlerMessage("hasOutOfJailCard", true);
            playerController.setPlayerOutOfJailCards(playerTurn, getOutOfJailCards - 1);
            chanceCardController.returnOutOfJailCard();
            return true;
        }

        // Get bail
        int bail = ((Jail) fieldController.getFields()[playerController.getPlayerPosition(playerTurn)]).getBail();

        // If the player has used all their tries, they have to pay the bail
        if (playerController.getGetOutOfJailTries(playerTurn) == 3) {
            playerController.setGetOutOfJailTries(playerTurn, 0);
            guiController.stringHandlerMessage("jailTriesUsed", true);
            boolean transaction = makeTransaction(-bail, playerTurn);
            System.out.print(transaction);
            if (!transaction){
                removePlayer(playerTurn, playerController.getPlayerPosition(playerTurn));
            }
            return transaction;
        }

        // Ask player if they want to pay bail, or try to roll two identical
        if (guiController.getUserButton(guiController.stringHandlerMessage("inJailOptions", false), "1", "2").equals("1")) {

            // Roll the dice
            rollDice();

            // If dice are identical, free player
            if (diceController.isIdentical()) {
                guiController.stringHandlerMessage("jailIdenticalDice", true);
                fieldController.free(playerTurn);
                playerController.setGetOutOfJailTries(playerTurn, 0);
                return true;

            } else { // If they aren't, the player has to stay in jail for the round
                guiController.stringHandlerMessage("jailNotIdenticalDice", true);
                playerController.incrementGetOutOfJailTries(playerTurn);
                return false;
            }

        } else { // Player wants to pay bail
            boolean transaction = makeTransaction(-bail, playerTurn);
            if (!transaction){
                removePlayer(playerTurn, playerController.getPlayerPosition(playerTurn));
            }
            return transaction;
        }
    }

    // Methods related to chanceCards.

    private boolean drawCard() {

        guiController.displayChanceCard(chanceCardController.drawCard());
        guiController.stringHandlerMessage("chanceCard", true);

        boolean success = true;
        switch (chanceCardController.getCurrentCardType()) {
            case "BankTransaction":

                success = makeTransaction(chanceCardController.getAmount(), playerTurn);
                break;

            case "CashFromPlayer":

                giftPlayer(chanceCardController.getAmount(), playerTurn);
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

                playerController.setPlayerOutOfJailCards(playerTurn, 1);

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
        int currentPosition = playerController.getPlayerPosition(playerTurn);
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
        currentPosition = playerController.getPlayerPosition(playerTurn);
        fieldAction(currentPosition, playerTurn);
        // TODO: Add second condition on this - if field is owned.
        if (doubleRent) {
            fieldAction(currentPosition, playerTurn);
        }
    }

    private void giftPlayer(int amount, int targetPlayer) {
        for (int i = 0; i < playerCount; i++) {
            makeTransaction(amount, i, targetPlayer);
        }
    }

    // Methods related to players.

    private boolean transactionFailed(int player) {

        boolean bankrupt = getPlayerTotalValue(player) < 0;
        if (bankrupt) {
            // Sell all buildings and properties automatically
            sellAllPlayerProperties(player);
        } else {
            // Let the player choose what real estate to sell and/or pawn to cover deficit.
            int deficit = -playerController.getPlayerBalance(player);
            while (deficit > 0) {
                sellRealEstate(player);
                deficit = -playerController.getPlayerBalance(player);
            }
        }
        return !bankrupt; //!bankrupt is return because is has to match makeTransaction which is basically the reverse.
    }

    private boolean makeTransaction(int amount, int player) {
        boolean transactionSuccess = playerController.makeTransaction(amount, player);
        updateGuiBalance(player);
        if (!transactionSuccess) {
            transactionSuccess = transactionFailed(player);
        }
        return transactionSuccess;
    }

    private boolean makeTransaction(int amount, int sender, int receiver) {
        boolean transactionSuccess = makeTransaction(-amount, sender);
        makeTransaction(amount, receiver);
        return transactionSuccess;
    }

    private void movePlayer(int player, int increment) {
        boolean hasGottenStartReward = playerController.movePlayer(player, increment);
        guiController.setCarPlacement(player, playerController.getPreviousPlayerPosition(player), playerController.getPlayerPosition(player));
        if (hasGottenStartReward) {
            guiController.stringHandlerMessage("hasPassedStart", true);
            updateGuiBalance(player);
        }
    }

    private void removePlayer(int player, int fieldPlacement) {
        guiController.removeGuiPlayer(playerTurn, fieldPlacement);
        //remove getOutOfJail chance cards
        int outOfJailCards = playerController.getOutOfJailCards(playerTurn);
        if (outOfJailCards > 0) {
            for (int i = 0; outOfJailCards > 0; i++) {
                chanceCardController.returnOutOfJailCard();
                outOfJailCards = playerController.getOutOfJailCards(playerTurn);
            }
        }
        if(playerTurn == playerCount-1 || playerTurn == 0){
            playerTurn = 0;
        } else{
            playerTurn = playerTurn-1;
        }
        playerCount = playerCount-1;
        playerController.removePlayer(player);
    }

    private int getPlayerTotalValue(int player) {
        return playerController.getPlayerBalance(player) + fieldController.getCombinedPropertyWorth(player);
    }

    private void getNextPlayerTurn() {
        playerTurn = ++playerTurn % playerCount;
    }

    // Miscellaneous.

    private void rollDice() {
        guiController.stringHandlerMessage("rollDice", true);
        diceController.roll();

        guiController.setDiceGui(diceController.getFaceValue(0), (int) (Math.random() * 360), diceController.getFaceValue(1), ((int) (Math.random() * 360)));
    }

    private void updateGuiRentForGroup(int position) {
        int groupIndex = fieldController.getPropertyGroupIndex(position);
        Property[] group = fieldController.getPropertyGroup(groupIndex);
        for (Property property : group) guiController.setRent(position, property.getCurrentRent());
    }

    private void updateGuiBalance(int player) {
        guiController.setBalance(playerController.getPlayerBalance(player), player);
    }

}
