package game;

import game.field.*;

public class FieldController {

    private final String XML_FILEPATH = "src/main/resources/fieldList.xml";
    private final Field[] fields;
    private final Property[][] properties;
    private Jail jail;
    private final boolean[] whoCanBuyHouses = new boolean[6];

    public FieldController() {

        // Generate fields from XML-file
        fields = Utility.fieldGenerator(XML_FILEPATH);

        // Array for keeping track of groups
        Property[] groups = new Property[0];

        // Go over each field in fields array
        for (Field field : fields) {

            // Check if field is a property
            if (!(field instanceof Property)) {

                // If field is jail, assign it to jail attribute
                if (field instanceof Jail) {
                    jail = (Jail) field;
                }

                // Don't do anything else with this field
                continue;
            }

            // Check if we've already registered this group
            boolean groupIsFound = false;
            for (Property property : groups) {

                // If they have the same color they are in the same group
                if (property.getColor().hashCode() == field.getColor().hashCode()) {

                    // this property is part of a group we've already registered
                    groupIsFound = true;
                    break;
                }
            }

            // If this is a new group, add property to array
            if (!groupIsFound)
                groups = Utility.addToArray(groups, (Property) field);
        }

        // The properties attribute acts as an array of groups (subarrays)
        // Each subarray contains the properties in that group
        properties = new Property[groups.length][];

        // Go over each group
        for (int i = 0; i < properties.length; i++) {

            // give subarray right length (Number of properties in group is shown in relatedProperties)
            properties[i] = new Property[groups[i].getRelatedProperties()];

            // Temporary property for going over each property in group
            Property property = groups[i];

            // Go over each property in group
            for (int j = 0; j < properties[i].length; j++) {

                // Put property into group
                properties[i][j] = property;

                // Get next property in group
                property = (Property) fields[property.getNextRelatedProperty()];
            }
        }
    }

    public FieldInstruction fieldAction(int position) {
        return fields[position].fieldAction();
    }

    public void buyProperty(int player, int propertyPosition) {

        // Get the property
        Property property = (Property) fields[propertyPosition];

        // Change the owner to player
        property.setOwner(player);

        // If necessary, change propertyLevel, based on what the property type is
        switch (property.getField()) {

            case "Street":

                // If the player owns all the properties in the group, change propertyLevel to 1
                if (ownsAllPropertiesInGroup(player, propertyPosition)) {

                    setPropertyLevelForGroup(propertyPosition, 1);
                    whoCanBuyHouses[player] = true;
                }
                break;


            case "Shipping":
                // Set propertyLevel to the number of properties owned in the group minus one
                int group = getPropertyGroupIndex(propertyPosition),
                        owned = getNumberOfPropertiesOwnedInGroup(player, propertyPosition);

                for (int i = 0; i < properties[group].length; i++) {
                    if (properties[group][i].getOwner() == player) properties[group][i].setPropertyLevel(owned - 1);
                }

                break;

            case "Brewery":
                // If the player owns all the properties in the group, change propertyLevel to 1
                if (ownsAllPropertiesInGroup(player, propertyPosition)) {
                    setPropertyLevelForGroup(propertyPosition, 1);
                }
                break;
        }
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

        // Get the index for the g
        int group = getPropertyGroupIndex(propertyPosition);

        // Go over each property in the group
        for (int i = 0; i < properties[group].length; i++) {

            // If the property isn't owned by the player, return false
            if (properties[group][i].getOwner() != player) return false;
        }

        // If we've gone through all the properties in the group and all of them are owned by the player, return true
        return true;
    }

    /**
     * Method for telling how many properties a certain player owns in a certain group.
     * The method goes over each property in the group,
     * and counts how many are owned by the specified player.
     *
     * @param player           Value representing a specific player
     * @param propertyPosition Value representing the position of a property which belongs to a specific group
     * @return The number of properties owned by the specific player in the property's group
     */
    public int getNumberOfPropertiesOwnedInGroup(int player, int propertyPosition) {

        // Variables for finding the group this property belongs to and counting owned properties
        int group = getPropertyGroupIndex(propertyPosition),
                count = 0;

        // Go over each property in the group
        for (int i = 0; i < properties[group].length; i++) {

            // If the property isn owned by the player, increment count
            if (properties[group][i].getOwner() == player) count++;
        }

        return count;
    }

    public void setPropertyLevelForGroup(int propertyPosition, int level) {
        int group = getPropertyGroupIndex(propertyPosition);
        for (int i = 0; i < properties[group].length; i++) properties[group][i].setPropertyLevel(level);
    }

    public int getPropertyGroupIndex(int propertyPosition) {

        int group = -1;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i][0].getColor().hashCode() == fields[propertyPosition].getColor().hashCode()) {
                group = i;
                break;
            }
        }

        return group;
    }

    public int getJailPosition() {
        return jail.getPosition();
    }

    public boolean isInJail(int player) {
        return jail.isInJail(player);
    }

    public void incarcerate(int player) {
        jail.incarcerate(player);
    }

    public void free(int player) {
        jail.free(player);
    }


    public boolean canPlayerBuyHouses(int player) {
        return whoCanBuyHouses[player];
    }

    public void setPropertyLevel(int fieldPosition, int level) {
        if (fields[fieldPosition].getField() == "Street") {
            ((Property) fields[fieldPosition]).setPropertyLevel(level);
        }
    }

    public Street[] allOwnedStreetsByPlayer(int player, int playerBalance) {
        // TODO: This method needs comments

        Street[] houseProperties = new Street[0];
        for (int i = 0; i < properties.length; i++) {
            for (int j = 0; j < properties[i].length; j++) {
                if (ownsAllPropertiesInGroup(player, properties[i][j].getPosition()) && properties[i][j] instanceof Street) {
                    int maxHouses = 0;
                    for (int y = 0; y < properties[i][j].getRelatedProperties() - 1; y++) {
                        if (properties[i][y].getPropertyLevel() == properties[i][y + 1].getPropertyLevel()) {
                            maxHouses = properties[i][y].getPropertyLevel() + 1;
                        } else {
                            maxHouses = Math.max(properties[i][y].getPropertyLevel(), properties[i][y + 1].getPropertyLevel());
                            break;
                        }
                    }
                    if (((Street) properties[i][j]).getBuildingCost() <= playerBalance && properties[i][j].getPropertyLevel() < 6 && properties[i][j].getPropertyLevel() < maxHouses) {
                        houseProperties = Utility.addToArray(houseProperties, (Street) properties[i][j]);
                    }
                }
            }
        }
        return houseProperties;
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
     * Method for getting the cost of rent on a specific property
     * The cost of rent can fluctuate depending on different factors
     *
     * @param propertyPosition The position on board of specific property
     * @return The current cost of rent on the specified property
     */
    public int getCurrentRent(int propertyPosition) {
        return ((Property) fields[propertyPosition]).getCurrentRent();
    }

    public int getCombinedPropertyWorth(int player) {

        int worth = 0;

        // Go over every property
        for (Property[] group : properties) {
            for (Property property : group) {

                // If property is not owned by player, continue to next property
                if (property.getOwner() != player) continue;

                // Add cost of property, of potential buildings on it to worth
                worth += property.getCost();
                if (property instanceof Street)
                    worth += (((Street) property).getBuildingCost() / 2) * ((Street) property).getNumberOfBuildings();
            }
        }

        return worth;
    }

    // Relevant getters
    public Field[] getFields() {
        return fields;
    }

}
