package game;

import game.field.*;

public class FieldController {

    private final String XML_FILEPATH = "src/main/resources/fieldList.xml";
    private final Field[] fields;
    private final Property[][] properties;
    private Jail jail;
    private boolean[] whoCanBuyHouses = new boolean[6];

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

        // It shouldn't be possible to call this method
        assert property.getOwner() != player;

        // Change the owner to player
        property.setOwner(player);

        // If necessary, change propertyLevel, based on what the property type is
        switch (property.getField()) {

            case "Street":

                // If the player owns all the properties in the group, change propertyLevel to 1
                if (ownsAllPropertiesInGroup(player, propertyPosition)) {
                    int group = -1;
                    for (int i = 0; i < properties.length; i++) {
                        if (properties[i][0].getColor().hashCode() == fields[propertyPosition].getColor().hashCode()) {
                            group = i;
                            break;
                        }
                    }
                    for (int i = 0; i < properties[group].length; i++) properties[group][i].setPropertyLevel(1);
                }

                if (ownsAllPropertiesInGroup(player, propertyPosition)) {
                    whoCanBuyHouses[player] = true;
                    setPropertylevelForGroup(propertyPosition, 1);
                }
                break;


            case "Shipping":
                // Set propertyLevel to the number of properties owned in the group minus one
                property.setPropertyLevel(getNumberOfPropertiesOwnedInGroup(player, propertyPosition) - 1);
                for (int i = 1; i < getNumberOfPropertiesOwnedInGroup(player, propertyPosition); i++) {
                    property = (Property) fields[property.getNextRelatedProperty()];
                    if (property.getOwner() == player) i++;
                    property.setPropertyLevel(getNumberOfPropertiesOwnedInGroup(player, propertyPosition) - 1);
                }
                break;

            case "Brewery":
                // If the player owns all the properties in the group, change propertyLevel to 1
                if (ownsAllPropertiesInGroup(player, propertyPosition)) {
                    setPropertylevelForGroup(propertyPosition, 1);
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

        // Get the specified property
        Property property = (Property) fields[propertyPosition];

        // Variables for finding the group this property belongs to
        int color = property.getColor().hashCode(),
                group = -1;

        // Go over each property group
        for (int i = 0; i < properties.length; i++) {

            // Check if the property has the same color as the first property in the group
            if (properties[i][0].getColor().hashCode() == color) {

                // If they do, we've found the group this property belongs to
                group = i;
                break;
            }
        }

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

        // Get the specified property
        Property property = (Property) fields[propertyPosition];

        // Variables for finding the group this property belongs to and counting owned properties
        int color = property.getColor().hashCode(),
                count = 0,
                group = -1;

        // Go over each property group
        for (int i = 0; i < properties.length; i++) {

            // Check if the property has the same color as the first property in the group
            if (properties[i][0].getColor().hashCode() == color) {

                // If they do, we've found the group this property belongs to
                group = i;
                break;
            }
        }

        // Go over each property in the group
        for (int i = 0; i < properties[group].length; i++) {

            // If the property isn owned by the player, increment count
            if (properties[group][i].getOwner() == player) count++;
        }

        return count;
    }

    public void setPropertylevelForGroup(int propertyPosition, int level) {

        // Find the specified property
        Property property = (Property) fields[propertyPosition];

        property.setPropertyLevel(level);

        // Go over each property in the group
        // (the number of properties in the group is represented with the relatedProperties attribute)
        for (int i = 0, n = property.getRelatedProperties(); i < n; i++) {

            // Find the next property in the group
            property = (Property) fields[property.getNextRelatedProperty()];

            property.setPropertyLevel(level);
        }

        // Ensure that we've ended up at the same property again, if we haven't, something is wrong
        assert property == fields[propertyPosition];
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
        if (fields[fieldPosition].getField().equals("Street")) {
            ((Property) fields[fieldPosition]).setPropertyLevel(level);
        }
    }

    public Street[] allOwnedStreetsByPlayer(int player, int playerBalance) {
        Street[] houseProperties = new Street[0];
        for (int i = 0; i < properties.length; i++) {
            for (int j = 0; j < properties[i].length; j++) {
                if (ownsAllPropertiesInGroup(player, properties[i][j].getPosition()) && properties[i][j] instanceof Street) {
                    int maxHouses = 0;
                    boolean equalHouses = true;
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

    public int getHouses(int player) {
        int houseCount = 0;
        for (Field field : fields) {
            if (field instanceof Street) {
                if (((Property) field).getOwner() == player) houseCount += ((Street) field).getHouses();
            }
        }
        return houseCount;
    }

    public int getHotels(int player) {
        int hotelCount = 0;
        for (Field field : fields) {
            if (field instanceof Street) {
                if (((Property) field).getOwner() == player) hotelCount += ((Street) field).getHotel();
            }
        }
        return hotelCount;
    }

    private boolean existsBuildingsOnStreetGroup(int position) {
        Street referenceStreet = (Street) fields[position];
        boolean hasBuildings = false;
        int relatedProperties = referenceStreet.getRelatedProperties();
        int nextRelatedProperty = referenceStreet.getNextRelatedProperty();
        for (int i = 0; i < relatedProperties; i++) {
            Street street = (Street) fields[nextRelatedProperty];
            nextRelatedProperty = street.getNextRelatedProperty();
            if (street.getNumberOfBuildings() > 0) {
                hasBuildings = true;
            }
        }
        return hasBuildings;
    }

    public boolean propertyCanBePawned(int position) {
        if (!(fields[position] instanceof Property)) {
            return false;
        }
        if (fields[position] instanceof Street) {
            if (existsBuildingsOnStreetGroup(position)) {
                return false;
            }
        }
        return !((Property) fields[position]).getPawned();
    }

    public void pawnProperty(int player, int position) {
        Property property = (Property) fields[position];
        if (propertyCanBePawned(position)) {
            property.setPawned(true);
        }
        int relatedProperties = property.getRelatedProperties();
        int nextRelatedProperty = property.getNextRelatedProperty();
        int propertyLevel;

        // Adjust property rentLevels
        switch (property.getField()) {
            case "Street":
                if (ownsAllPropertiesInGroup(player, position)) {
                    for (int i = 0; i < relatedProperties; i++) {
                        Street nextStreet = (Street) fields[nextRelatedProperty];
                        setPropertyLevel(nextRelatedProperty, 0);
                        nextRelatedProperty = nextStreet.getNextRelatedProperty();
                    }
                }
                break;

            case "Shipping":
                for (int i = 0; i < relatedProperties; i++) {
                    Shipping nextShipping = (Shipping) fields[nextRelatedProperty];
                    propertyLevel = nextShipping.getPropertyLevel();
                    if (nextShipping.getOwner() == player && propertyLevel > 0) {
                        nextShipping.setPropertyLevel(propertyLevel - 1);
                    }
                    nextRelatedProperty = nextShipping.getNextRelatedProperty();
                }
                break;

            case "Brewery":
                for (int i = 0; i < relatedProperties; i++) {
                    Brewery nextBrewery = (Brewery) fields[nextRelatedProperty];
                    propertyLevel = nextBrewery.getPropertyLevel();
                    if (nextBrewery.getOwner() == player && propertyLevel > 0) {
                        nextBrewery.setPropertyLevel(propertyLevel - 1);
                    }
                    nextRelatedProperty = nextBrewery.getNextRelatedProperty();
                }
        }
    }

    public void reclaimProperty(int player, int position) {
        Property property = (Property) fields[position];

        property.setPawned(false);

        int relatedProperties = property.getRelatedProperties();
        int nextRelatedProperty = property.getNextRelatedProperty();
        int ownedNotPawnedPropertiesInGroup = 1;
        Property nextProperty = (Property) fields[nextRelatedProperty];
        for (int i = 0; i < relatedProperties - 1; i++) {
            if (!nextProperty.getPawned() && (nextProperty.getOwner() == player)) ownedNotPawnedPropertiesInGroup += 1;
            nextRelatedProperty = property.getNextRelatedProperty();
            nextProperty = (Property) fields[nextRelatedProperty];
        }

        // Adjust property rentLevels
        switch (property.getField()) {
            case "Street":
                if (ownedNotPawnedPropertiesInGroup == relatedProperties) {
                    for (int i = 0; i < relatedProperties; i++) {
                        Street nextStreet = (Street) fields[nextRelatedProperty];
                        nextStreet.setPropertyLevel(1);
                        nextRelatedProperty = nextStreet.getNextRelatedProperty();
                    }
                }
                break;

            case "Shipping":
                for (int i = 0; i < relatedProperties; i++) {
                    Shipping nextShipping = (Shipping) fields[nextRelatedProperty];
                    nextShipping.setPropertyLevel(ownedNotPawnedPropertiesInGroup - 1);
                    nextRelatedProperty = nextShipping.getNextRelatedProperty();
                }
                break;

            case "Brewery":
                for (int i = 0; i < relatedProperties; i++) {
                    Brewery nextBrewery = (Brewery) fields[nextRelatedProperty];
                    nextBrewery.setPropertyLevel(ownedNotPawnedPropertiesInGroup - 1);
                    nextRelatedProperty = nextBrewery.getNextRelatedProperty();
                }
        }
    }

    // Relevant getters
    public Field[] getFields() {
        return fields;
    }

}
