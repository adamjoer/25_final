package game.field;

import game.*;

import java.awt.Color;

public class Parking extends Field {

    public Parking(String title, String subText, String description, int position, Color color) {
        super(title, subText, description, position, color);
    }

    public boolean fieldAction(int player, PlayerController playerController, GUIController guiController, int diceSum) {
        // Do nothing
        return false;
    }
}
