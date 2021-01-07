package game.field;

import game.*;

import java.awt.Color;

public class TaxField extends Field {

    private final int fine;

    public TaxField(String title, String subText, String description, int position, Color color, int fine) {
        super(title, subText, description, position, color);
        this.fine = fine;
    }

    public boolean fieldAction(PlayerController playerController, GUIController guiController, int diceSum) {
        // TODO: Implement whatever happens on this field
        return false;
    }

    //Relevant getters
    public int getFine() {
        return fine;
    }

    public String toString() {
        return super.toString() +
               "\n\t[fine=" + fine + ']';
    }
}
