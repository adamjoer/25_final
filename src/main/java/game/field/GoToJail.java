package game.field;

import java.awt.Color;

public class GoToJail extends Field {

    private final int jailPosition;

    public GoToJail(String title, String subText, String description, int position, Color color, int jailPosition) {
        super(title, subText, description, position, color);
        this.jailPosition = jailPosition;
    }

    public FieldInstruction fieldAction(int player) {
        // TODO: Implement going to jail
        return null;
    }

    public int getJailPosition() {
        return jailPosition;
    }

    public String toString() {
        return super.toString() +
               "\n\t[jailPosition=" + jailPosition + ']';
    }
}
