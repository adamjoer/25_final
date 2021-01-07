package game.field;

import game.*;

import java.awt.Color;

public class Brewery extends Property {

    public Brewery(String title, String subText, String description, int position, Color color, int cost, int pawnValue, int[] rentLevels, int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position, color, cost, pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
    }

    public boolean fieldAction(int player, PlayerController playerController, GUIController guiController, int diceSum) {
        // TODO: Implement whatever happens on this field
        return false;
    }
}
