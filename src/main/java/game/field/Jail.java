package game.field;

import game.Utility;

import java.awt.Color;
import java.util.Arrays;

public class Jail extends Field {

    private final int bail;
    private int[] prisoners;

    public Jail(String title, String subText, String description, int position, Color color, int bail) {
        super(title, subText, description, position, color);
        this.bail = bail;
        this.prisoners = new int[0];
    }

    public FieldInstruction fieldAction() {
        return new FieldInstruction(getField(), bail);
    }

    public void incarcerate(int player) {
        prisoners = Utility.addToArray(prisoners, player);
    }

    public void free(int player) {

        // Find the players placement in array
        int playerPlacement = -1;
        for (int i = 0; i < prisoners.length; i++) {
            if (prisoners[i] == player) {
                playerPlacement = i;
                break;
            }
        }

        // If player wasn't found, do nothing
//        if (playerPlacement == -1) return;

        prisoners = Utility.removeFromArray(prisoners, playerPlacement);
    }

    public boolean isInJail(int player) {

        for (int prisoner : prisoners)
            if (prisoner == player) return true;

        return false;
    }

    // Relevant getters
    public int getBail() {
        return bail;
    }

    public int[] getPrisoners() {
        return prisoners;
    }

    public String toString() {
        return super.toString() +
               "\n\t[bail=" + bail +
               "]\n\t[prisoners=" + Arrays.toString(prisoners) + ']';
    }
}
