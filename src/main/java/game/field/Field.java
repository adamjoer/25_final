package game.field;

/**
 * Prototype for all fields on board
 */
public abstract class Field {

    protected final String title;
    protected final String subText;
    protected final String description;
    protected final int position;

    public Field(String title, String subText, String description, int position) {
        this.title = title;
        this.subText = subText;
        this.description = description;
        this.position = position;
    }

    public abstract void fieldAction();

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

    public String toString() {
        return getClass().getSimpleName() +
               "\n\t[title=\"" + title +
               "\"]\n\t[subText=\"" + subText +
               "\"]\n\t[description=\"" + description +
               "\"]\n\t[position=" + position + ']';
    }
}