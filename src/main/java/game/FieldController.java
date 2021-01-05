package game;

import game.field.Field;

public class FieldController {

    private final String XML_FILEPATH = "src/main/resources/fields.xml";
    private final Field[] fields;

    public FieldController() {
        fields = Utility.fieldGenerator(XML_FILEPATH);
    }

    public void fieldAction(int position) {
        // TODO: This method needs the same parameters that the Field.fieldAction() gets,
        //       which will then be passed to it here
        fields[position].fieldAction();
    }

    // Relevant getters
    public Field[] getFields() {
        return fields;
    }
}
