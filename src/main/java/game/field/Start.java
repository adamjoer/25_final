package game.field;

import game.*;

import java.awt.Color;

public class Start extends Field {

    public Start(String title, String subText, String description, int position, Color color) {
        super(title, subText, description, position, color);
    }

    public boolean fieldAction(int player, PlayerController playerController, GUIController guiController, int diceSum) {

        // Do nothing, 'pass go' reward will be handled in Board
        return true;
    }
}
