package game.field;

import java.awt.Color;

public class Property extends Field {

    // Attributes
    private final int value;
    private final Color color;
    private final int relatedPropertyPosition;
    private int owner = -1;

    // Constructor
    public Property(String title, String subText, String description, int position, int value, Color color, int relatedPropertyPosition) {
        super(title, subText, description, position);
        this.value = value;
        this.color = color;
        this.relatedPropertyPosition = relatedPropertyPosition;
    }

    public void fieldAction() {
        // TODO: Implement buying property, or paying rent
    }

    // Relevant getters
    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getRelatedPropertyPosition() {
        return relatedPropertyPosition;
    }

    public int getOwner() {
        return owner;
    }

    // Relevant setters
    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String toString() {
        return super.toString() +
               "\n\t[value=" + value +
               "]\n\t[color=\"" + color.toString() +
               "\"]\n\t[relatedPropertyPosition=" + relatedPropertyPosition +
               "]\n\t[owner=" + owner + ']';
    }
}
