package game;

import gui_fields.*;
import gui_main.GUI;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class GUIController {

    private final GUI gui;
    private final int MAX_PLAYER_AMOUNT = 4;
    private final int playerAmount; // The actual number of players in the game
    private final GUI_Player[] guiPlayerList;
    private final String[] playerNames;
    private final StringHandler stringHandler;

    public GUIController() {
        // TODO: Initialise the class's attributes
    }

    /**
     * Places buttons on the board with a message, and wait for the button pressed
     *
     * @param msg  : The message that the player will see
     * @param btn1 : The first btn a text string, that the player will see on the btn
     * @param btn2 : The 2nd btn a text string, that the player will see on the btn
     * @return returns a text string with the button pressed
     */
    public String getUserButton(String msg, String btn1, String btn2) {
        return gui.getUserButtonPressed(
                msg,
                btn1, btn2
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

        String[] names = new String[MAX_PLAYER_AMOUNT];
        while (true) {
            boolean btnPressed = getUserLeftButtonPressed(getString("createPlayer"), getString("yes"), getString("no"));
            if (btnPressed) {
                if (i == 4) {
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
                        i++;
                    }
                }
            } else {
                if (i < 2) {
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
     * @return Array of the player names that has been entered from the GUI
     */
    public String[] returnPlayerNames() {
        return playerNames;
    }

    /**
     * Check if a GUI player object has a car on a specified field position
     *
     * @param player        : Gui player object whose car should be on the field
     * @param fieldPosition : The field which the players car should be on
     * @return : boolean, true if car is on the field otherwise false
     */
    public boolean hasCarGuiPlayer(GUI_Player player, int fieldPosition) {
        return gui.getFields()[fieldPosition].hasCar(player);
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
     * Places the dice on the board
     *
     * @param faceValue1 : Face value of the first dice to be shown on the board
     * @param faceValue2 : Seconds face of the second dice to be shown on the board
     */
    public void setDiceGui(int faceValue1, int faceValue2) {
        gui.setDice(faceValue1, faceValue2);
    }

    /**
     * Shows the face value of a singular dice
     *
     * @param faceValue : Face value of the dice, to be shown on the board
     */
    public void setDiceGui(int faceValue) {
        gui.setDie(faceValue);
    }

    /**
     * Sets a field to be owned by a player, can be done for the following types of fields:
     * - GUI_Street
     * - GUI_Brewery
     * - GUI_Shipping
     *
     * @param subtext       : Subtext on field
     * @param playerName    : Name of the player who owns the field
     * @param rent          : Rent of the field
     * @param fieldColor    : Change of color to indicate change of ownership
     * @param fieldPosition : The position of the field that changes ownership (array position), not actual position
     */
    public void fieldOwnable(String subtext, String playerName, int rent, Color fieldColor, int fieldPosition) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setSubText(subtext);
        ownable.setOwnerName(playerName);
        ownable.setRent(Integer.toString(rent));
        ownable.setBackGroundColor(fieldColor);
    }

    /**
     * Removes ownership of field
     *
     * @param fieldPosition : The position of the field (array position), not actual position
     */
    public void fieldOwnable(int fieldPosition) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setOwnerName(null);
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
        street.setHotel(setHotel);
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

    public GUI_Field[] getGuiFields() {
        return gui.getFields();
    }

    /**
     * Closes the GUI window.
     */
    public void close() {
        gui.close();
    }

    private String getString(String reference) {
        return stringHandler.getString(reference);
    }
}
