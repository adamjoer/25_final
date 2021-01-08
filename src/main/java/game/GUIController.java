package game;

import game.field.*;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class GUIController {

    private final GUI gui;
    private final int MAX_PLAYER_AMOUNT = 6;
    private final int playerAmount; // The actual number of players in the game
    private final GUI_Player[] guiPlayerList;
    private final String[] playerNames;
    private final GUI_Car.Type[] carType;
    private final StringHandler stringHandler = new StringHandler("src/main/java/resources/stringRefs.xml");;

    /*public static void main(String[] args){
        Field[] fieldsGen = Utility.fieldGenerator("src/main/java/resources/fieldList.xml");
        GUIController gui = new GUIController(fieldsGen);
        GUI_Field[] fields = gui.getFields();
        GUI_Field field_ownable = fields[3];
        ((GUI_Ownable) field_ownable).setOwnerName("Jens");
        ((GUI_Ownable) field_ownable).setRent("2000");
        ((GUI_Street) field_ownable).setHouses(4);
    }*/

    public GUIController(Field[] fields) {
        // TODO: Initialise the class's attributes
        GUI_Field[] guiFields = new GUI_Field[fields.length];
        for(int i = 0; i<fields.length; i++){
            Field field = fields[i];

            switch (field.getField()){
                case "Start":
                    //Opret start felt
                    guiFields[i] = new GUI_Start(field.getTitle(), field.getSubText(), field.getDescription(), Color.red, Color.BLACK);
                    break;
                case "Street":
                    Property property = (Property) fields[i];
                    //Color color = property.getColor();
                    guiFields[i] = new GUI_Street(property.getTitle(), property.getSubText(), property.getDescription(),
                            String.valueOf(property.getCost()), property.getColor(), Color.BLACK);
                    break;
                case "Shipping":
                    //
                    Shipping shipping = (Shipping) fields[i];
                    guiFields[i] = new GUI_Shipping("default",shipping.getTitle(), shipping.getSubText(), shipping.getDescription(),
                            String.valueOf(shipping.getCurrentRent()), Color.WHITE, Color.BLACK);
                    break;
                case "TaxField":
                    //
                    TaxField tax = (TaxField) fields[i];
                    guiFields[i] = new GUI_Tax(tax.getTitle(), tax.getSubText(), tax.getDescription(),Color.GRAY, Color.BLACK);
                    break;
                case "Brewery":
                    //
                    Brewery brewery = (Brewery) fields[i];
                    String title = brewery.getTitle();
                    guiFields[i] = new GUI_Brewery("default", brewery.getTitle(),brewery.getSubText(),brewery.getDescription(),
                            String.valueOf(brewery.getCurrentRent()), Color.WHITE, Color.BLACK);
                    break;
                case "GoToJail":
                case "Jail":
                    guiFields[i] = new GUI_Jail("default",field.getTitle(), field.getTitle(), field.getTitle(),
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


        //gui = new GUI();
        carType = new GUI_Car.Type[MAX_PLAYER_AMOUNT];
        playerNames = askForPlayerNames();
        playerAmount = playerNames.length;
        this.guiPlayerList = new GUI_Player[playerAmount];
    }

    public GUI_Field[] getFields(){
        return gui.getFields();
    }

    /**
     * Places buttons on the board with a message, and wait for the button pressed
     *
     * @param msg  : The message that the player will see
     * @param btn  : multiple argumented buttons
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

        String[] names = new String[MAX_PLAYER_AMOUNT];
        while (true) {
            boolean btnPressed = getUserLeftButtonPressed(getString("createPlayer"), getString("yes"), getString("no"));
            if (btnPressed) {
                if (i == 6) {
                    String userBtn = getUserButton(getString("maxPlayerReachedPrompt"),
                            getString("closeGameOption"), getString("continueOption"));
                    if (userBtn.equals(getString("closeGameOption"))) {
                        close();
                    }
                    break;
                } else {
                    userInputName = getUserString(getString("inputNamePrompt")).toLowerCase();
                    userInputName = userInputName.substring(0, 1).toUpperCase() + userInputName.substring(1);
                    String vehicleType = gui.getUserSelection("Vælg type af dit køretøj","Bil", "Traktor", "Racerbil", "UFO");
                    switch (vehicleType) {
                        case "Bil" -> carType[i] = GUI_Car.Type.CAR;
                        case "Traktor" -> carType[i] = GUI_Car.Type.TRACTOR;
                        case "Racerbil" -> carType[i] = GUI_Car.Type.RACECAR;
                        case "UFO" -> carType[i] = GUI_Car.Type.UFO;
                    }

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
     *  Adds all players to the board
     * @param players : players needs to be created before sending to GUI controller
     * @return true if the players are created otherwise false, can also return false if the player array size is over 4
     */
    public boolean addPlayers(Player[] players){
        Color[] color = new Color[]{Color.black, Color.blue, Color.red, Color.yellow, Color.GRAY, Color.CYAN};
        GUI_Car car;
        boolean playerCheck = false;
        if (players.length > 6 || players.length < 2){
            return false;
        }

        for(int i = 0; i < playerAmount; i++){
            car = new GUI_Car(color[i], null, carType[i], GUI_Car.Pattern.FILL);
            GUI_Player player = new GUI_Player(players[i].getName(),players[i].getBalance(), car);
            playerCheck = gui.addPlayer(player);
            guiPlayerList[i] = player;
            setCar(i, true, 0);
        }
        return playerCheck;
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

    public void setBalance(int balance, int player){
        //set balance
        Objects.requireNonNull(getGuiPlayer(player)).setBalance(balance);
    }

    /**
     * Set the visibility of a players car
     * @param player : A player from the game
     * @param visibility : Whether the car is shown on the board or not
     */
    public void setCar(int player, boolean visibility, int fieldPlacement){
        gui.getFields()[fieldPlacement].drawCar(getGuiPlayer(player), visibility);
    }

    /**
     * Moves a players car
     * @param player : A player from the game
     * @param fieldPlacement : Position of the player (array position) not actual position
     * @param newFieldPlacement : The position where the player should be moved to (array position)
     */
    public void setCarPlacement(int player, int fieldPlacement, int newFieldPlacement){
        gui.getFields()[fieldPlacement].drawCar(getGuiPlayer(player),false);
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
     * @param player        : The player that will own the field (int of player)
     * @param rent          : Rent of the field
     * @param fieldPosition : The position of the field that changes ownership (array position), not actual position
     */
    public void fieldOwnable(int fieldPosition, int player, int rent) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setOwnerName(guiPlayerList[player].getName());
        ownable.setRent(Integer.toString(rent));
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
    }

    /**
     * Sets the ownership of a field
     *
     * @param fieldPosition : The position of the field (array position), not the actual position
     * @param player : the number of the player
     */
    public void setOwnership(int fieldPosition, int player) {
        GUI_Ownable ownable = (GUI_Ownable) gui.getFields()[fieldPosition];
        ownable.setOwnerName(guiPlayerList[player].getName());
    }

    /**
     * Sets the rent of a field
     * @param fieldPosition : The position of the field (array position), not the actual position
     * @param rent : the rent that the field will get
     */
    public void setRent(int fieldPosition, int rent){
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

    /**
     * Takes a player from the game and retrieves it from board
     * @param player : a player from the main game
     * @return a GUI_Player
     */
    private GUI_Player getGuiPlayer(int player){

        for(GUI_Player p : guiPlayerList){
            if (playerNames[player].equals(p.getName())){
                return p;
            }
        }
        return null;
    }

    private String getString(String reference) {
        return stringHandler.getString(reference);
    }
}