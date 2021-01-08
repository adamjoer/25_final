package game.field;

import java.awt.Color;

public class Parking extends Field {

    public Parking(String title, String subText, String description, int position, Color color) {
        super(title, subText, description, position, color);
    }

    public FieldInstruction fieldAction(int player) {
        return new FieldInstruction(getField());
    }
}
