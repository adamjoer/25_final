package game.field;

import java.awt.Color;

/**
 * Prototype for all fields on board
 */
public abstract class Field {

    protected final String title;
    protected final String subText;
    protected final String description;
    protected final int position;
    protected final Color color;

    public Field(String title, String subText, String description, int position, Color color) {
        this.title = title;
        this.subText = subText;
        this.description = description;
        this.position = position;
        this.color = color;
    }

    public abstract FieldInstruction fieldAction();

    public String getField() {
        return getClass().getSimpleName();
    }

    public String getTitle() {
        return title;
    }

    public String getSubText() {
        return subText;
    }

    public String getDescription() {
        return description;
    }

    public int getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return getField() +
               "\n\t[title=\"" + title +
               "\"]\n\t[subText=\"" + subText +
               "\"]\n\t[description=\"" + description +
               "\"]\n\t[position=" + position +
               "]\n\t[color=\"" + color.toString() + "\"]";
    }
}
