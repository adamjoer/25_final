package game;

import game.field.Field;
import game.field.FieldInstruction;
import game.field.TaxField;
import game.field.Street;
import org.apache.commons.lang.ArrayUtils;

import java.io.FileInputStream;
import java.util.Arrays;

public class Game {

    private final PlayerController playerController;
    private final GUIController guiController;
    private final DiceController diceController;
    private final FieldController fieldController;
    //private ChanceCardController chanceCardController;
    private Player[] players;
    private int playerTurn;
    private int playerTurnIndex; // look at setPlayerTurn for info
    private final StringHandler stringHandler = new StringHandler("src/main/java/resources/stringRefs.xml");
    private Field[] fields;


    public Game(){
        fields = Utility.fieldGenerator("src/main/java/resources/fieldList.xml");
        fieldController = new FieldController();
        guiController = new GUIController(fields);
        diceController = new DiceController(2, 6);
        playerController = new PlayerController(guiController.returnPlayerNames(), 30000);
        players = playerController.getPlayers();
        playerTurn = (int) (Math.random() * (players.length - 1));
    }

    public void gameLoop() {
        boolean stop = false;
        boolean isDieIdentical;

        guiController.addPlayers(players);

        while (!stop) {
            playerTurn = getNextPlayerTurn();

            //Check if a player can buy houses
            if (fieldController.canPlayerBuyHouses(playerTurn)) {
                //Ask if the player want to buy houses
                while (guiController.getUserButton(playerController.getName(playerTurn) + " " + stringHandler.getString("askBuyHouse"), "Ja", "Nej") == "Ja") {

                    //Get the streets which the player can buy houses on
                    Street[] streets = fieldController.allOwnedStreetsByPlayer(playerTurn);

                    //Get an array of strings, which shows the name of the street and the price to put a house on it
                    String[] houseCostButtons = getHouseCostButtons(streets);

                    String streetToBuyHouse = guiController.getUserButton(stringHandler.getString("whereToBuyHouse"), houseCostButtons);


                    //Find the street that the user wants to buy a house on
                    for (Street s : streets) {
                        String temp = s.getTitle() + ": " + s.getBuildingCost() + " kr.";
                        if (temp.equals(streetToBuyHouse)) {

                            //Check if they have money for it
                            if (playerController.makeTransaction(-s.getBuildingCost(), playerTurn)) {

                                //Increase the streets propertyLevel and update the gui with the new player balance, and the house
                                s.setPropertyLevel(s.getPropertyLevel() + 1);
                                setGuiBalance(playerTurn, playerController.getPlayerBalance(playerTurn));

                                //Check if the street is going to have a hotel
                                if (s.getPropertyLevel() == 6) {
                                    guiController.setHouseOrHotelStreet(s.getPosition(), 0, true);
                                } else {
                                    guiController.setHouseOrHotelStreet(s.getPosition(), s.getPropertyLevel() - 1, false);
                                }
                            }

                            //If player can't afford a house, show it to them in the gui
                            else {
                                guiController.showMessage(stringHandler.getString("noMoney"));
                            }
                            break;
                        }
                    }
                }
            }

            guiController.showMessage("Player " + playerController.getName(playerTurn) + " turn to roll the dice");

            // Check if player is on jail field
            if (playerController.getPlayerPosition(playerTurn) == fieldController.getJailPosition()) {

                // Check if player is actually in prison or just visiting
                if (fieldController.isInJail(playerTurn)) {

                    guiController.showMessage(stringHandler.getString("isInJail"));
                    // TODO: Implement whatever needs to be done to get out of jail

                    fieldController.free(playerTurn);
                }
            }

            // cast dice from dice controller
            guiController.getUserButton("Roll the dice", "Roll");
            diceController.roll();
            isDieIdentical = diceController.isIdentical();
            int dice1 = diceController.getFaceValue(0);
            int dice2 = diceController.getFaceValue(1);

            guiController.setDiceGui(dice1, (int) (Math.random() * 360), dice2, ((int) (Math.random() * 360)));

            movePlayer(playerTurnIndex, diceController.getSum());

            fieldController.fieldAction(playerController.getPlayerPosition(playerTurnIndex));
            fieldAction(playerController.getPlayerPosition(playerTurnIndex), playerTurnIndex);

            while (isDieIdentical) {
                guiController.showMessage("You got identical dies, roll the dice again!");
                guiController.getUserButton("Roll the dice", "Roll");
                diceController.roll();
                isDieIdentical = diceController.isIdentical();
                dice1 = diceController.getFaceValue(0);
                dice2 = diceController.getFaceValue(1);
                guiController.setDiceGui(dice1, (int) (Math.random() * 360), dice2, ((int) (Math.random() * 360)));
                movePlayer(playerTurnIndex, diceController.getSum());
                fieldController.fieldAction(playerController.getPlayerPosition(playerTurnIndex));
                fieldAction(playerController.getPlayerPosition(playerTurnIndex), playerTurnIndex);
            }

            if (players.length <= 1) {
                stop = true;
            }
        }
        guiController.close();
    }

    public void movePlayer(int player, int increment) {
        playerController.movePlayer(player, increment);
        guiController.setCarPlacement(player, players[player].getPreviousPosition(), players[player].getCurrentPosition());
        if (playerController.getPlayerPosition(player) < playerController.getPreviousPlayerPosition(player)) {
            setGuiBalance(player, playerController.getPlayerBalance(player));
        }
    }

    public void removePlayer(int player, int fieldPlacement){
        players = playerController.removePlayer(player);

        guiController.removeGuiPlayer(playerTurnIndex, fieldPlacement);
        playerTurn = playerTurn-1;
    }

    public boolean sellProperty(int player, int place) {
        // TODO : make a check for if the property exists
        int[] properties = playerController.getProperties(player);
        if (Arrays.stream(properties).anyMatch(i -> i == place)) {
            playerController.removeProperty(player, place);
            return true;
        }
        return false;
    }

    public boolean buyProperty(int player, int place, int rent) {
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

    public String[] getPlayerNames() {
        return guiController.returnPlayerNames();
    }

    public boolean fieldAction(int position, int player) {
        FieldInstruction instructions = fieldController.fieldAction(position);

        switch (instructions.getFieldType()) {

            case "Brewery":
            case "Street":
            case "Shipping":

                //Check if the field is owned by the player
                if(player == instructions.getOwner()){
                    guiController.showMessage(stringHandler.getString("ownField") + " " + fieldController.getFields()[position].getTitle());
                    return true;
                }

                //Check if the field is owned by the bank
                else if (instructions.getOwner() == -1) {

                    //If field is owned by the bank, ask player if they want to buy it
                    if(guiController.getUserButton(fieldController.getFields()[position].getTitle() + " " + stringHandler.getString("buyField")
                            , "Ja", "Nej") == "Ja"){

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
                    guiController.showMessage(stringHandler.getString("payRent"));
                    int owner = instructions.getOwner();

                    //Make transaction from the current player to the owner of the field
                    boolean successfulRent = playerController.makeTransaction(instructions.getRent(), player, owner);

                    //Set the balance of both players in the GUI
                    guiController.setBalance(playerController.getPlayerBalance(player), player);
                    guiController.setBalance(playerController.getPlayerBalance(owner), owner);

                    return successfulRent;
                }

            case "Chance":
                break;

            case "GoToJail":
                guiController.showMessage(stringHandler.getString("goToJail"));
                playerController.setPlayerPosition(player, instructions.getJailPosition());
                guiController.setCarPlacement(player, playerController.getPreviousPlayerPosition(player), playerController.getPlayerPosition(player));
                fieldController.incarcerate(player);
                break;

            case "Jail":
                break;

            case "Parking":
                break;

            case "Start":
                break;

            case "TaxField":
                TaxField currentField = (TaxField) fieldController.getFields()[playerController.getPlayerPosition(playerTurnIndex)];
                int fine = currentField.getFine();
                int playerValue = fieldController.getCombinedPropertyWorth(playerTurnIndex);
                if (currentField.getTitle().equals("Statsskat")) {
                    guiController.showMessage(stringHandler.getString("statsSkat"));
                    playerController.makeTransaction(-currentField.getFine(), playerTurnIndex);
                    guiController.makeTransaction(-currentField.getFine(), playerTurnIndex);
                } else{
                    guiController.showMessage(stringHandler.getString("skat"));
                    String playerChoiceOne = "1";
                    String playerChoice = guiController.getUserButton(stringHandler.getString("skatOptions"),
                                                playerChoiceOne, "2");
                    if(playerChoice.equals(playerChoiceOne)){
                        playerController.makeTransaction(-fine, playerTurnIndex);
                        guiController.makeTransaction(-fine, playerTurnIndex);
                    } else{
                        playerController.makeTransaction(-playerValue, playerTurnIndex);
                        guiController.makeTransaction(-playerValue, playerTurnIndex);
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Field type '" + instructions.getFieldType() + "' not recognised");

        }
        return true;
    }

    public String[] getHouseCostButtons(Street[] properties) {
        String[] houseCostButtons = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            houseCostButtons[i] = properties[i].getTitle() + ": " + properties[i].getBuildingCost() + " kr.";
        }

        return houseCostButtons;
    }

    public int getNextPlayerTurn() {
        playerTurn = (playerTurn + 1) % players.length;
        setPlayerTurn();
        return playerTurn;
    }

    /*public boolean hasWinner(){
        return false;
    }*/

    public void makeTransaction(int receiver, int sender, int amount) {
        playerController.makeTransaction(receiver, sender, amount);
    }

    public void setGuiBalance(int player, int amount) {
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
