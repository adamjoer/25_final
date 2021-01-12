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

    /**
     * Method for assigning a specific property as owned by a specific player.
     * The method also changes the propertyLevel attribute depending on the
     * property type and whether the player owns any of the other properties
     * in the specified property's group.
     *
     * @param player           Value representing a specific player
     * @param propertyPosition The position of a specific property which the specified player will own
     * @return True if the propertyLevel/rent in the specified property's group has changed (for updating GUI), false otherwise
     */
    public boolean buyProperty(int player, int propertyPosition) {

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

                    return true;
                }
                return false;

            case "Shipping":
                // Set propertyLevel to the number of properties owned in the group minus one
                int group = getPropertyGroupIndex(propertyPosition),
                        owned = getNumberOfPropertiesOwnedInGroup(player, propertyPosition);

                for (int i = 0; i < properties[group].length; i++) {
                    if (properties[group][i].getOwner() == player) properties[group][i].setPropertyLevel(owned - 1);
                }

                return true;

            case "Brewery":
                // If the player owns all the properties in the group, change propertyLevel to 1
                if (ownsAllPropertiesInGroup(player, propertyPosition)) {
                    setPropertyLevelForGroup(propertyPosition, 1);

                    return true;
                }
                return false;
        }

        return false;
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

    /**
     * Method for setting the propertyLevel for a specific group to a specific level.
     * The method finds the group to which the specified property belongs, and assigns
     * the propertyLevel attribute the specified value
     *
     * @param propertyPosition The position of a property in a specific group
     * @param level            The level which every property in the group will be assigned
     */
    public void setPropertyLevelForGroup(int propertyPosition, int level) {

        // Get the index of the group to which the property belongs
        int group = getPropertyGroupIndex(propertyPosition);

        // Go over each property in the group, and change their propertyLevel attribute to the specified value
        for (int i = 0; i < properties[group].length; i++) properties[group][i].setPropertyLevel(level);
    }

    /**
     * Method to get the index in the 2d Property array 'properties' that correspond
     * with the group to which the specified property belongs.
     *
     * @param propertyPosition The position of a specific property
     * @return The index in the properties attribute that correspond to this property's group
     */
    public int getPropertyGroupIndex(int propertyPosition) {

        // Go over each group in the array
        for (int i = 0; i < properties.length; i++) {

            // If the color of the property matches with the color of the group, that is it's group
            if (properties[i][0].getColor().hashCode() == fields[propertyPosition].getColor().hashCode())
                return i;
        }

        // Error: the property doesn't match any group in the array, is either bad parameter, or bad data
        throw new IllegalArgumentException("Cannot recognise group belonging to property with position " + propertyPosition);
    }

    /**
     * Method to get all the streets that a player is able to put buildings on.
     * The method returns any streets that the player has permission and money
     * to build something on. This method takes into account that the player has
     * to spread their buildings evenly across the property group.
     *
     * @param player        Value representing a specific player
     * @param playerBalance The amount of money the player has
     * @return An array of pointers to Street objects that the specified player can put buildings on
     */
    public Street[] getBuildableStreets(int player, int playerBalance) {

        // Initialize empty Street array
        Street[] houseProperties = new Street[0];

        // Go over every property group
        for (Property[] group : properties) {

            // If the group doesn't contain streets continue to the next group
            if (!(group[0] instanceof Street)) continue;

            // If all streets aren't owned by the player, continue to the next group
            if (!ownsAllPropertiesInGroup(player, group[0].getPosition())) continue;

            // Calculate what the max. building level in this group is
            int maxLevel = 0;
            for (int i = 0; i < group.length - 1; i++) {

                // If there is a street with higher building level than the others, that is the max. number building level
                if (group[i].getPropertyLevel() == group[i + 1].getPropertyLevel()) {
                    maxLevel = group[i].getPropertyLevel() + 1;

                } else {
                    maxLevel = Math.max(group[i].getPropertyLevel(), group[i + 1].getPropertyLevel());
                    break;
                }
            }

            // Add streets that meets the criteria
            for (Property property : group) {

                // If the player can afford to build on it, and there isn't a hotel on it, and the building level is below the max, add it to the array
                if (((Street) property).getBuildingCost() <= playerBalance && property.getPropertyLevel() < 6 && property.getPropertyLevel() < maxLevel) {
                    houseProperties = Utility.addToArray(houseProperties, (Street) property);
                }
            }
        }
        return houseProperties;
    }

    public boolean canPlayerBuyHouses(int player) {
        return whoCanBuyHouses[player];
    }

    // Methods related to jail
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

    /**
     * Method for getting the combined worth of all the properties a specific player owns.
     * The value of a property is the cost of it, and if it is a Street, the worth of
     * the buildings on it. This is defined as the combined cost of each building on it.
     *
     * @param player Value representing a specific player
     * @return The worth of all the properties the specified user owns, in kr.
     */
    public int getCombinedPropertyWorth(int player) {

        int worth = 0;

        // Go over every property
        for (Property[] group : properties) {
            for (Property property : group) {

                // If property is not owned by player, continue to next property
                if (property.getOwner() != player) continue;

                // Add cost of property, and of potential buildings on it to worth
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

    public Property[] getPropertyGroup(int groupIndex) {
        return properties[groupIndex];
    }

}
