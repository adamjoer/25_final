package game.field;

import java.awt.Color;

public class Jail extends Field{

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

        // Copy existing prisoner array into temporary array
        int[] temp = prisoners;

        // Increase
        prisoners = new int[prisoners.length + 1];

        // Copy temporary array into new array
        System.arraycopy(temp, 0, prisoners, 0, temp.length);

        // Add specified player to end of new array
        prisoners[prisoners.length - 1] = player;
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

        // Copy existing array into temporary array
        int[] temp = prisoners;

        // Change array length
        prisoners = new int[prisoners.length - 1];

        // Copy temporary array into new array, leaving out the prisoner at specified playerPlacement
        System.arraycopy(temp, 0, prisoners, 0, playerPlacement);
        System.arraycopy(temp, playerPlacement + 1, prisoners, playerPlacement, prisoners.length - playerPlacement);
    }

    public boolean isInJail(int player) {
        for (int prisoner : prisoners) {
            if (prisoner == player) return true;
        }
        return false;
    }

    public String toString() {
        return super.toString() +
               "\n\t[bail=" + bail + ']';
    }
}
