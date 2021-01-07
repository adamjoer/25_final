package game.field;

import game.*;

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

    public abstract boolean fieldAction(PlayerController playerController, GUIController guiController, int diceSum);

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
        return getClass().getSimpleName() +
               "\n\t[title=\"" + title +
               "\"]\n\t[subText=\"" + subText +
               "\"]\n\t[description=\"" + description +
               "\"]\n\t[position=" + position + ']' +
               "]\n\t[Color=\"" + color.toString() + "\"]";
    }
}
