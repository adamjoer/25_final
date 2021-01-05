package game;

import game.field.Field;

public class FieldController {

    private final String XML_FILEPATH = "src/main/resources/fields.xml";
    private final Field[] fields;

    public FieldController() {
        fields = Utility.fieldGenerator(XML_FILEPATH);
    }

    public void fieldAction(int position) {
        // TODO: This method needs the same parameters that Field.fieldAction() gets,
        //       which will then be passed to it here
        fields[position].fieldAction();
    }


    /**
     * Method for checking whether a player has passed the Go field,
     * and is therefore eligible for the 'pass go' reward.
     * This is based entirely upon the assumption that the Go field's
     * position is zero on the board. It won't work otherwise.
     *
     * @param previousPosition The player's previous position
     * @param currentPosition  The player's current position
     * @return True if player has passed, or is on the 'Go' field, false otherwise
     */
    public boolean hasPassedGo(int previousPosition, int currentPosition) {
        // FIXME: Should this just be placed in Board?

        // If start field position is zero, player will have passed start if their position has overflowed to a smaller value,
        // i.e. their previous position is larger than their current position
        return previousPosition > currentPosition;
    }

    // Relevant getters
    public Field[] getFields() {
        return fields;
    }
}
