package game.controller;

import game.Player;
import game.Utility;
import game.field.*;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class GUIController {

    private final GUI gui;
    private final int MAX_PLAYER_AMOUNT = 6;
    private final int MIN_PLAYER_AMOUNT = 2;
    private final int playerAmount; // The actual number of players in the game
    private GUI_Player[] guiPlayerList;
    private String[] playerNames;
    private final GUI_Car[] guiCars;
    private Color[] colorsAvailable = new Color[]{Color.MAGENTA, Color.GRAY, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN}; //Color.decode("#3E0D0C")
    private final StringHandler stringHandler = new StringHandler("src/main/resources/stringRefsDA.xml");
    private String[] colorChoices = new String[]{
            "Magenta",
            stringHandler.getString("gray"),
            stringHandler.getString("red"),
            stringHandler.getString("yellow"),
            stringHandler.getString("green"),
            stringHandler.getString("cyan")
    };

    public GUIController(Field[] fields) {
        GUI_Field[] guiFields = new GUI_Field[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            switch (field.getField()) {
                case "Start":
                    guiFields[i] = new GUI_Start(field.getTitle(), field.getSubText(), field.getDescription(), Color.red, Color.BLACK);
                    break;
                case "Street":
                    Property property = (Property) fields[i];
                    guiFields[i] = new GUI_Street(property.getTitle(), property.getSubText(), property.getDescription(),
                            String.valueOf(property.getCost()), property.getColor(), Color.BLACK);
                    break;
                case "Shipping":
                    Shipping shipping = (Shipping) fields[i];
                    guiFields[i] = new GUI_Shipping("default", shipping.getTitle(), shipping.getSubText(), shipping.getDescription(),
                            String.valueOf(shipping.getCurrentRent()), Color.WHITE, Color.BLACK);
                    break;
                case "TaxField":
                    TaxField tax = (TaxField) fields[i];
                    guiFields[i] = new GUI_Tax(tax.getTitle(), tax.getSubText(), tax.getDescription(), Color.GRAY, Color.BLACK);
                    break;
                case "Brewery":
                    Brewery brewery = (Brewery) fields[i];
                    guiFields[i] = new GUI_Brewery("default", brewery.getTitle(), brewery.getSubText(), brewery.getDescription(),
                            String.valueOf(brewery.getCurrentRent()), Color.WHITE, Color.BLACK);
                    break;
                case "GoToJail":
                case "Jail":
                    guiFields[i] = new GUI_Jail("default", field.getTitle(), field.getTitle(), field.getTitle(),
                            new Color(125, 125, 125), Color.BLACK);
                    break;
                case "Parking":
                    guiFields[i] = new GUI_Refuge("default", field.getTitle(), field.getSubText(), field.getDescription(),
                            Color.white, Color.black);
                    break;
                case "Chance":
                    guiFields[i] = new GUI_Chance(field.getTitle(), field.getSubText(), field.getDescription(),
                            Color.white, Color.black);
                    break;
                // Error: Field name not recognised
                default:
                    throw new IllegalArgumentException();
            }
        }
        gui = new GUI(guiFields, Color.PINK);

        guiCars = new GUI_Car[MAX_PLAYER_AMOUNT];
        playerNames = askForPlayerNames();
        playerAmount = playerNames.length;
        this.guiPlayerList = new GUI_Player[playerAmount];
    }

    public String stringHandlerMessage(String stringHandlerRef, boolean showMsg) {
        String msg = stringHandler.getString(stringHandlerRef);
        if (showMsg) {
            showMessage(msg);
        }
        return msg;
    }

    public void stringHandlerMessage(String stringHandlerRef, boolean showMsg, String msg) {
        String stringHandlerMsg = stringHandler.getString(stringHandlerRef);
        if (showMsg) {
            showMessage(stringHandlerMsg + msg);
        }
    }

    /**
     * Places buttons on the board with a message, and wait for the button pressed
     *
     * @param msg : The message that the player will see
     * @param btn : multiple argumented buttons
     * @return returns a text string with the button pressed
     */
    public String getUserButton(String msg, String... btn) {
        return gui.getUserButtonPressed(
                msg,
                btn
        );
    }

    /**
     * laces buttons on the board with a message, and wait for the button pressed
     *
     * @param msg      : The message that the player will see
     * @param btnLeft  : If the player presses this button, the method will return true
     * @param btnRight : If the player presses this button, the method will return false
     * @return boolean
     */
    public boolean getUserLeftButtonPressed(String msg, String btnLeft, String btnRight) {
        return gui.getUserLeftButtonPressed(msg, btnLeft, btnRight);
    }

    /**
     * Ask the player to write something and returns it
     *
     * @param msg : The message that the player will see
     * @return the text what the player writes
     */
    public String getUserString(String msg) {
        return gui.getUserString(msg);
    }


    /**
     * Ask the players for their names on the board.
     * Checks for the max. and min. amount of players.
     * Checks also if the name is already in use.
     */
    private String[] askForPlayerNames() {
        String userInputName;
        int i = 0;
        GUI_Car.Type carType = GUI_Car.Type.CAR;

        String[] names = new String[MAX_PLAYER_AMOUNT];
        while (true) {
            boolean btnPressed = getUserLeftButtonPressed(getString("createPlayer"), getString("yes"), getString("no"));
            if (btnPressed) {
                if (i == MAX_PLAYER_AMOUNT) {
                    String userBtn = getUserButton(getString("maxPlayerReachedPrompt"),
                            getString("closeGameOption"), getString("continueOption"));
                    if (userBtn.equals(getString("closeGameOption"))) {
                        close();
                    }
                    break;
                } else {
                    userInputName = getUserString(getString("inputNamePrompt")).toLowerCase();
                    userInputName = userInputName.substring(0, 1).toUpperCase() + userInputName.substring(1);

                    if (Arrays.asList(names).contains(userInputName)) {
                        showMessage(getString("nameNotUniqueError"));
                    } else {
                        names[i] = (userInputName);
                        String vehicleType = gui.getUserSelection(stringHandlerMessage("vehicleType", false), "Bil", "Traktor", "Racerbil", "UFO");
                        switch (vehicleType) {
                            case "Bil" -> carType = GUI_Car.Type.CAR;
                            case "Traktor" -> carType = GUI_Car.Type.TRACTOR;
                            case "Racerbil" -> carType = GUI_Car.Type.RACECAR;
                            case "UFO" -> carType = GUI_Car.Type.UFO;
                        }
                        String vehicleColor = gui.getUserSelection(stringHandlerMessage("vehicleColor", false), colorChoices);

                        int tempColorIndex = -1;
                        for (int j = 0; j < colorChoices.length; j++) {
                            if (colorChoices[j].equals(vehicleColor)) {
                                tempColorIndex = j;
                                break;
                            }
                        }
                        guiCars[i] = new GUI_Car(colorsAvailable[tempColorIndex], null, carType, GUI_Car.Pattern.FILL);
                        colorChoices = Utility.removeFromArray(colorChoices, tempColorIndex);
                        colorsAvailable = Utility.removeFromArray(colorsAvailable, tempColorIndex);

                        i++;
                    }
                }
            } else {
                if (i < MIN_PLAYER_AMOUNT) {
                    showMessage(getString("tooFewPlayersError"));
                } else {
                    break;
                }
            }
        }

        String[] tempNames = new String[i];
        System.arraycopy(names, 0, tempNames, 0, i);
        return tempNames;
    }

    /**
     * Adds all players to the board
     *
     * @param players : players needs to be created before sending to GUI controller
     * @return true if the players are created otherwise false, can also return false if the player array size is over 4
     */
    public void addPlayers(Player[] players) {
        for (int i = 0; i < playerAmount; i++) {
            GUI_Player player = new GUI_Player(players[i].getName(), players[i].getBalance(), guiCars[i]);
            guiPlayerList[i] = player;
            setCar(i, true, 0);
        }
    }

    /**
     * @return Array of the player names that has been entered from the GUI
     */
    public String[] returnPlayerNames() {
        return playerNames;
    }

    /**
     * Set the players balance on the gui
     *
     * @param balance : balance to be set
     * @param player  : integer which correlates to the player
     */
    public void setBalance(int balance, int player) {
        //set balance
        Objects.requireNonNull(getGuiPlayer(player)).setBalance(balance);
    }

    /**
     * Makes a transaction on the player in the GUI
     *
     * @param amount : the amount to be transferred
     * @param player : integer which correlates to the player
     */
    public void makeTransaction(int amount, int player) {
        GUI_Player guiPlayer = getGuiPlayer(player);
        assert guiPlayer != null;
        guiPlayer.setBalance(amount + guiPlayer.getBalance());
    }

    public void removeGuiPlayer(int player, int fieldPlacement) {
        // TODO: finish this
        if (guiPlayerList.length == 1) {
            showMessage("" + getGuiPlayer(player).getName());
        } else {
            showMessage(getString("bankruptPlayer"));
            setCar(player, false, fieldPlacement);

            GUI_Player[] tempArr = guiPlayerList;
            guiPlayerList = new GUI_Player[guiPlayerList.length - 1];

            System.arraycopy(tempArr, 0, guiPlayerList, 0, player);
            System.arraycopy(tempArr, player + 1, guiPlayerList, player, guiPlayerList.length - player);

            String[] tempNameArr = playerNames;
            playerNames = new String[playerNames.length - 1];

            System.arraycopy(tempNameArr, 0, playerNames, 0, player);
            System.arraycopy(tempNameArr, player + 1, playerNames, player, playerNames.length - player);
        }
    }

    /**
     * Set the visibility of a players car
     *
     * @param player     : A player from the game
     * @param visibility : Whether the car is shown on the board or not
     */
    public void setCar(int player, boolean visibility, int fieldPlacement) {
        gui.getFields()[fieldPlacement].drawCar(getGuiPlayer(player), visibility);
    }

    /**
     * Moves a players car
     *
     * @param player            : A player from the game
     * @param fieldPlacement    : Position of the player (array position) not actual position
     * @param newFieldPlacement : The position where the player should be moved to (array position)
     */
    public void setCarPlacement(int player, int fieldPlacement, int newFieldPlacement) {
        gui.getFields()[fieldPlacement].drawCar(getGuiPlayer(player), false);
        gui.getFields()[newFieldPlacement].drawCar(getGuiPlayer(player), true);
    }

    /**
     * Displays two dice on the board with the given values and a fixed rotation,
     * at a random position on the board.
     * If the dice value is not between 1-6, the dice won't be displayed.
     * The method replaces the existing die/dice.
     *
     * @param faceValue1 : Value of first dice int [1:6]
     * @param rotation1  : Rotation of the first die, in degrees int [0:360]
     * @param faceValue2 : Value of second dice int [1:6]
     * @param rotation2  :  Rotation of the first die, in degrees int [0:360]
     */
    public void setDiceGui(int faceValue1, int rotation1, int faceValue2, int rotation2) {
        gui.setDice(faceValue1, rotation1, faceValue2, rotation2);
    }

    /**
     * Sets a field to be owned by a player, can be done for the following types of fields:
     * - GUI_Street
     * - GUI_Brewery
     * - GUI_Shipping
     *
     * @param player        : The player that will own the field (int of player)
     * @param rent          : Rent of the field
     * @param fieldPosition : The position of the field that changes ownership (array position), not actual position
     */
    public void fieldOwnable(int fieldPosition, int player, int rent) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setOwnerName(guiPlayerList[player].getName());
        ownable.setRent(Integer.toString(rent));
        ownable.setBorder(guiPlayerList[player].getCar().getPrimaryColor());
    }

    /**
     * Removes ownership of field and removes the rent of the field
     *
     * @param fieldPosition : The position of the field (array position), not actual position
     */
    public void removeRentOwnership(int fieldPosition) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setOwnerName(null);
        ownable.setRent(null);
        ownable.setBorder(Color.BLACK);
    }

    /**
     * Choose which property to be prompted
     *
     * @param propertyPositions : array of field positions which the player owns
     * @param reference         : String reference for StringHandler
     * @return : position of field
     */
    public int choosePropertyPrompt(int[] propertyPositions, String reference) {
        String[] properties = new String[propertyPositions.length];
        for (int i = 0; i < properties.length; i++) {
            properties[i] = gui.getFields()[propertyPositions[i]].getTitle();
        }
        String userSelection = gui.getUserSelection(getString(reference), properties);

        for (int i = 0; i < properties.length; i++) {
            if (gui.getFields()[propertyPositions[i]].getTitle().equals(userSelection)) {
                return propertyPositions[i];
            }
        }

        return 0;
    }

    /**
     * Prompts the user for which real estate to sell
     *
     * @param stringRefs : references of the options which the player can sell
     * @return : position in the array of the option chosen
     */
    public String sellRealEstatePrompt(String[] stringRefs) {
        String[] options = new String[stringRefs.length];
        if (options.length > 0) {
            for (int i = 0; i < options.length; i++) {
                options[i] = getString(stringRefs[i]);
            }
            String userSelection = gui.getUserSelection(getString("chooseRealEstate"), options);

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(userSelection)) {
                    return stringRefs[i];
                }
            }
        }
        return "";
    }

    /**
     * Sets the description for a field
     *
     * @param position  : position of the field
     * @param reference : String reference for the description
     */
    public void setDescription(int position, String reference) {
        gui.getFields()[position].setDescription(getString(reference));
    }

    /**
     * Sets the rent of a field
     *
     * @param fieldPosition : The position of the field (array position), not the actual position
     * @param rent          : the rent that the field will get
     */
    public void setRent(int fieldPosition, int rent) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setRent(Integer.toString(rent));
    }

    /**
     * Sets houses or a hotel on a street.
     * - The max number of houses is 4, so if number is >4 it will just be set to 4 houses
     *
     * @param fieldPosition : The position of the field (array position), not actual position
     * @param houseAmount   : Amount of houses to be put on the field
     * @param setHotel      : Boolean (true if hotel should be on the street otherwise false)
     */
    public void setHouseOrHotelStreet(int fieldPosition, int houseAmount, boolean setHotel) {
        GUI_Street street = (GUI_Street) gui.getFields()[fieldPosition];
        if (houseAmount > 4) {
            houseAmount = 4;
        }
        street.setHouses(houseAmount);
        if (setHotel) {
            street.setHotel(true);
        }
    }

    /**
     * Displays a message to the user, along with an 'OK'-button.
     * The program stops/hangs at the method call until the button
     * is pressed.
     *
     * @param msg The message to print
     */
    public void showMessage(String msg) {
        gui.showMessage(msg);
    }

    /**
     * Sets the text for a chance card and shows it on the board
     *
     * @param msg : Text that will be seen on the chance card
     */
    public void displayChanceCard(String msg) {
        gui.displayChanceCard(msg);
    }

    /**
     * Display the current chance card text in the center
     */
    public void displayChanceCard() {
        gui.displayChanceCard();
    }

    /**
     * Closes the GUI window.
     */
    public void close() {
        gui.close();
    }

    /**
     * Takes a player from the game and retrieves it from board
     *
     * @param player : a player from the main game
     * @return a GUI_Player
     */
    private GUI_Player getGuiPlayer(int player) {

        for (GUI_Player p : guiPlayerList) {
            if (playerNames[player].equals(p.getName())) {
                return p;
            }
        }
        return null;
    }

    private String getString(String reference) {
        return stringHandler.getString(reference);
    }
}