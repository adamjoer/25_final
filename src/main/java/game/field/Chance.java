package game.field;

import java.awt.Color;

public class Chance extends Field {

    public Chance(String title, String subText, String description, int position, Color color) {
        super(title, subText, description, position, color);
    }

    public FieldInstruction fieldAction(int player) {
        // TODO: Implement drawing chance card and acting based on it
        return null;
    }
}
