package game.field;

import java.awt.Color;

public class Jail extends Field{

    private final int bail;

    public Jail(String title, String subText, String description, int position, Color color, int bail) {
        super(title, subText, description, position, color);
        this.bail = bail;
    }

    public void fieldAction() {
        //TODO: Implement getting out of jail
    }

    public String toString() {
        return super.toString() +
               "\n\t[bail=" + bail + ']';
    }
}
