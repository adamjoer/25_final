package game;

import game.field.*;

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
     * Method for telling if a certain player owns all the properties in a certain group.
     * The method goes over each property in the group and checks who the owner of it is.
     * If any of the properties isn't owned by the player, false is returned.
     *
     * @param player           Value representing a specific player
     * @param propertyPosition Value representing the position of a property which belongs to a specific group
     * @return True if the player owns all the properties in the group, false otherwise
     */
    public boolean ownsAllPropertiesInGroup(int player, int propertyPosition) {

        // Find the specified property
        Property property = (Property) fields[propertyPosition];

        // Go over each property in the group
        // (the number of properties in the group is represented with the relatedProperties attribute)
        for (int i = 0, n = property.getRelatedProperties(); i < n; i++) {

            // Find the next property in the group
            property = (Property) fields[property.getNextRelatedProperty()];

            // If the property isn't owned by the player, return false
            if (property.getOwner() != player) return false;
        }

        // Ensure that we've ended up at the same property again, if we haven't, something is wrong
        assert property == fields[propertyPosition];

        // If we've gone through all the properties and all of them are owned by the player, return true
        return true;
    }

    /**
     * Method for checking whether a player has passed the Go field,
     * and is therefore eligible for the 'pass go' reward.
     * This is based entirely upon the assumption that the Go field's position is zero
     * on the board. It won't work otherwise.
     *
     * @param previousPosition The player's previous position
     * @param currentPosition  The player's current position
     * @return True if player has passed, or is on the 'Go' field, false otherwise
     */
    public boolean hasPassedStart(int previousPosition, int currentPosition) {

        // If start field position is zero, player will have passed start if their position has overflowed to a smaller value,
        // i.e. their previous position is larger than their current position
        return previousPosition > currentPosition;
    }

    /**
     * Method for getting the cost of rent on a specific property on bard
     * The cost of rent can fluctuate depending on different factors
     *
     * @param propertyPosition The position on board of specific property
     * @return The current cost of rent on the specified property
     */
    public int getCurrentRent(int propertyPosition) {

        // FIXME: This should be changed when Shipping and Brewery classed are added
        return ((Property) fields[propertyPosition]).getCurrentRent();
    }

    // Relevant getters
    public Field[] getFields() {
        return fields;
    }
}
