package game.field;

import java.awt.Color;

public class Start extends Field {

    public Start(String title, String subText, String description, int position, Color color) {
        super(title, subText, description, position, color);
    }

    public FieldInstruction fieldAction() {
        return new FieldInstruction(getField());
    }
}
