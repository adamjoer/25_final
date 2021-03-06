package game.controller;

import game.Utility;
import game.field.*;

public class FieldController {

    private final Field[] fields;
    private final Property[][] properties;
    private Jail jail;
    private final boolean[] whoCanBuyHouses = new boolean[6];

    public FieldController() {

        // Generate fields from XML-file
        fields = Utility.fieldGenerator("fieldListDA.xml");

        // Organise properties into groups by putting them into the properties attribute

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

            // Check if this property belongs to a group we've already registered

            // Go over each group already found, from back to front, for optimisation:
            // Properties in the same group are mostly grouped together on board,
            // and we go through the fields based on position.
            // The properties recently appended to the end of the 'groups' array are therefore more likely to be a match
            boolean groupIsFound = false;
            for (int i = groups.length - 1; i >= 0; i--) {

                // If they have the same color they are in the same group
                if (groups[i].getColor().hashCode() == field.getColor().hashCode()) {

                    // this property is part of a group we've already registered
                    groupIsFound = true;
                    break;
                }
            }

            // If this is a new group, add property to array
            if (!groupIsFound)
                groups = Utility.addToArray(groups, (Property) field);
        }

        // The properties attribute acts as an array of groups (arrays)
        // Each array contains properties that are in the same group
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

                if (owned != 1) {
                    for (int i = 0; i < properties[group].length; i++) {
                        if (properties[group][i].getOwner() == player) properties[group][i].setPropertyLevel(owned - 1);
                    }
                }

                return owned != 1;

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
     * disOwnProperty is the method called when it has been verified that a Property can be sold. It adjusts propertyLevel,
     * sets pawnValue and returns the appropriate value the Property is sold for.
     *
     * @param player : The player selling the Property.
     * @param place  : The position of the Property (for correcting propertyLevel of related Properties).
     * @return : The sell price of the Property.
     */
    public int disOwnProperty(int player, int place) {

        //Get the property
        Property property = (Property) fields[place];
        boolean pawned = property.getPawned();
        property.setPawned(false);

        switch (property.getField()) {

            //If it's a street, check if there's any houses on it
            case "Street":

                //If there's no houses, set the owner to be the bank, chance propertyLevel of the entire group to 0, and return the cost of the property
                if (property.getPropertyLevel() < 2) {
                    property.setOwner(-1);
                    setPropertyLevelForGroup(place, 0);
                    return (pawned) ? property.getCost() / 2 : property.getCost();
                } else {
                    return 0;
                }

            case "Shipping":
                // Set propertyLevel to the number of properties owned in the group minus one
                int group = getPropertyGroupIndex(place),
                        owned = getNumberOfPropertiesOwnedInGroup(player, place);

                for (int i = 0; i < properties[group].length; i++) {
                    if (properties[group][i].getOwner() == player) properties[group][i].setPropertyLevel(owned - 1);
                }
                property.setOwner(-1);

                return (pawned) ? property.getCost() / 2 : property.getCost();

            case "Brewery":
                //Since there's only 2 brewery fields, they are both going to be set to 0 if one is sold
                setPropertyLevelForGroup(place, 0);
                property.setOwner(-1);
                return (pawned) ? property.getCost() / 2 : property.getCost();
        }

        return 0;
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
    private void setPropertyLevelForGroup(int propertyPosition, int level) {

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
        if (isInJail(player)) {
            jail.free(player);
        }
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

                if (property.getPawned()) {
                    worth += property.getCost() / 2;
                } else {
                    worth += property.getCost();
                }
                if (property instanceof Street)
                    worth += (((Street) property).getBuildingCost() / 2) * ((Street) property).getNumberOfBuildings();
            }
        }

        return worth;
    }

    /**
     * Gets the current number of houses a player owns across all Properties. It is used specifically for one type of ChanceCard.
     *
     * @param player : The player drawing the ChanceCard related to this.
     * @return : The number of houses the player owns.
     */
    public int getHouses(int player) {

        int houseCount = 0;
        for (Property[] group : properties) {

            if (!(group[0] instanceof Street)) continue;

            for (Property property : group) {
                if (property.getOwner() == player)
                    houseCount += ((Street) property).getHouses();
            }
        }
        return houseCount;
    }

    /**
     * Gets the current number of hotels a player owns across all Properties. It is used specifically for one type of ChanceCard.
     *
     * @param player : The player drawing the ChanceCard related to this.
     * @return : The number of hotels the player owns.
     */
    public int getHotels(int player) {

        int hotelCount = 0;
        for (Property[] group : properties) {

            if (!(group[0] instanceof Street)) continue;

            for (Property property : group) {
                if (property.getOwner() == player)
                    hotelCount += ((Street) property).getHotel();
            }
        }

        return hotelCount;
    }

    /**
     * After removal of a player, it sets all the owners with a new index to the new index
     *
     * @param player : player which index has been changed
     */
    public void setPropertyOwner(int player) {
        for (Property p : getPlayerProperties(player)) {
            p.setOwner(p.getOwner() - 1);
        }
    }

    /**
     * Simply checks if a Property group has buildings.
     *
     * @param position : The position of any Property in the group.
     * @return : true if there are buildings any Property the group.
     */
    private boolean groupHasBuildings(int position) {

        int groupIndex = getPropertyGroupIndex(position);

        for (Property property : getPropertyGroup(groupIndex)) {
            if (((Street) property).getNumberOfBuildings() > 0)
                return true;
        }
        return false;
    }

    /**
     * Checks if a property can be sold (i.e. no buildings on Property group, or not a Street)
     *
     * @param position : Property to check.
     * @return : true if Property can be sold.
     */
    private boolean propertyCanBeSold(int position) {
        if (fields[position].getField().equals("Street")) {
            return !groupHasBuildings(position);
        }
        return true;
    }

    /**
     * Goes through the Properties of the given Player and checks which are sellable.
     *
     * @param player : The owner whose Properties are requested.
     * @return : An int[] with the Property positions of the sellableProperties that belong to the given Player.
     */
    public int[] sellablePropertyPositions(int player) {
        int[] playerPropertyPositions = getPlayerPropertyPositions(player);
        int[] sellablePropertyPositions = new int[0];
        for (int position : playerPropertyPositions) {
            if (propertyCanBeSold(position)) {
                sellablePropertyPositions = Utility.addToArray(sellablePropertyPositions, position);
            }
        }
        return sellablePropertyPositions;
    }

    /**
     * Checks if there's a building on the Property, and whether there's more buildings on other Properties in the group.
     *
     * @param position : Position of the Property in question.
     * @return : true if a building can be sold on this Property, else false.
     */
    private boolean buildingCanBeSold(int position) {
        Property property = (Property) fields[position];
        int propertyLevel = property.getPropertyLevel();
        if (property instanceof Street && propertyLevel > 1) {
            Property nextProperty = (Property) fields[property.getNextRelatedProperty()];
            for (int i = 0; i < property.getRelatedProperties() - 1; i++) {
                if (nextProperty.getPropertyLevel() > propertyLevel) {
                    return false;
                }
                nextProperty = (Property) fields[nextProperty.getNextRelatedProperty()];
            }

        } else {
            return false;
        }
        return true;
    }

    /**
     * Gets an int[] of positions for all Properties with sellable buildings owned by the Player in question.
     *
     * @param player : Player in question.
     * @return : int[] of Property positions.
     */
    public int[] sellableBuildingPositions(int player) {
        int[] playerPropertyPositions = getPlayerPropertyPositions(player);
        int[] sellableBuildingPositions = new int[0];
        for (int position : playerPropertyPositions) {
            if (buildingCanBeSold(position)) {

                sellableBuildingPositions = Utility.addToArray(sellableBuildingPositions, position);
            }
        }
        return sellableBuildingPositions;
    }

    /**
     * Sells a building on the specified Property
     *
     * @param position : Position of Property.
     * @return : The reimbursement value.
     */
    public int sellBuilding(int position) {
        if (buildingCanBeSold(position)) {
            Street street = (Street) fields[position];
            street.setPropertyLevel(street.getPropertyLevel() - 1);
            return street.getBuildingCost() / 2;
        } else {
            return 0;
        }
    }

    /**
     * Used when a player is bankrupt, thus ignores propertyLevels. Sells all buildings and Properties for the Player.
     *
     * @param player : Player in question.
     * @return : The total reimbursement for the sales.
     */
    public int sellAllPlayerProperties(int player) {
        Property[] playerProperties = getPlayerProperties(player);
        int totalValue = 0;
        for (Property property : playerProperties) {
            if (property.getField().equals("Street")) {

                int propertyLevel = property.getPropertyLevel();
                if (propertyLevel > 1) {
                    Street street = (Street) property;
                    totalValue += (street.getBuildingCost() / 2) * (propertyLevel - 1);
                }
            }
            int baseValue = property.getCost();
            if (property.getPawned()) {
                totalValue += baseValue / 2;
            } else {
                totalValue += baseValue;
            }
            property.setPropertyLevel(0);
            property.setOwner(-1);
            property.setPawned(false);
        }
        return totalValue;
    }

    /**
     * Checks if a Property has buildings and whether it's already pawned.
     *
     * @param position : Position of Property.
     * @return : true if Property can be pawned.
     */
    public boolean propertyCanBePawned(int position) {
        if (fields[position] instanceof Street) {
            if (groupHasBuildings(position)) {
                return false;
            }
        }
        return !((Property) fields[position]).getPawned();
    }

    /**
     * Gets Property positions for all the Properties that the Player in question can pawn.
     *
     * @param player : Player in question
     * @return : int[] with positions of Properties that can be pawned by the Player.
     */
    public int[] pawnablePropertyPositions(int player) {
        int[] playerPropertyPositions = getPlayerPropertyPositions(player);
        int[] pawnablePropertyPositions = new int[0];
        for (int position : playerPropertyPositions) {
            if (propertyCanBePawned(position)) {
                pawnablePropertyPositions = Utility.addToArray(pawnablePropertyPositions, position);
            }
        }
        return pawnablePropertyPositions;
    }

    /**
     * Pawns the specified Property and adjusts propertyLevels accordingly.
     *
     * @param player   : The Player pawning the Property.
     * @param position : The position of the Property
     * @return : The pawn value of the property ( = getCost()/2 )
     */
    public int pawnProperty(int player, int position) {
        Property property = (Property) fields[position];
        int pawnValue = 0;
        if (propertyCanBePawned(position)) {
            property.setPawned(true);
            int propertyLevel = property.getPropertyLevel();
            pawnValue = property.getCost() / 2;

            Property[] properties = getPropertyGroup(getPropertyGroupIndex(position));

            // Adjust property rentLevels
            switch (property.getField()) {
                case "Street":
                    if (ownsAllPropertiesInGroup(player, position)) {
                        for (Property groupProperty : properties) {
                            groupProperty.setPropertyLevel(0);
                        }
                    }
                    break;

                case "Shipping":
                    for (Property groupProperty : properties) {
                        if (groupProperty.getOwner() == player && propertyLevel > 0) {
                            groupProperty.setPropertyLevel(propertyLevel - 1);
                        }
                    }
                    break;

                case "Brewery":
                    for (Property groupProperty : properties) {
                        groupProperty.setPropertyLevel(0);
                    }
            }
        }
        return pawnValue;
    }

    /**
     * Reclaims a pawned Property owned by the Player, and adjusts propertyLevels accordingly.
     *
     * @param player   : The Player reclaiming the Property.
     * @param position : The position of the Property.
     * @return : The price for reclaiming the Property.
     */
    public int reclaimProperty(int player, int position) {
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
        return property.getCost() / 2;
    }

    /**
     * Gets a Property[] of all Properties owned by the specified Player.
     *
     * @param player : The owner of the Properties.
     * @return : A Property[].
     */
    private Property[] getPlayerProperties(int player) {
        Property[] ownedProperties = new Property[0];
        for (Property[] group : properties) {
            for (Property property : group) {
                if (property.getOwner() == player) {
                    ownedProperties = Utility.addToArray(ownedProperties, property);
                }
            }
        }
        return ownedProperties;
    }

    /**
     * Gets all Property positions for the Properties of the specified Player.
     *
     * @param player : Player in question
     * @return : An int[] with Property positions.
     */
    public int[] getPlayerPropertyPositions(int player) {
        Property[] playerProperties = getPlayerProperties(player);
        int[] propertyPositions = new int[playerProperties.length];
        for (int i = 0; i < playerProperties.length; i++) {
            propertyPositions[i] = playerProperties[i].getPosition();
        }
        return propertyPositions;
    }

    /**
     * Gets all the positions of currently pawned Properties owned by the Player.
     *
     * @param player : Player in question.
     * @return : An int[] with pawned Properties owned by the Player.
     */
    private int[] getPlayerPawnedPropertyPositions(int player) {
        Property[] playerProperties = getPlayerProperties(player);
        int[] pawnedPropertyPositions = new int[0];
        for (Property property : playerProperties) {
            if (property.getPawned()) {
                pawnedPropertyPositions = Utility.addToArray(pawnedPropertyPositions, property.getPosition());
            }
        }
        return pawnedPropertyPositions;
    }

    /**
     * Gets a Property[] of pawned Properties that the Player can currently afford to reclaim.
     *
     * @param player        : Player in question.
     * @param playerBalance : Current playerBalance.
     * @return : A Property[].
     */
    public Property[] getReclaimableProperties(int player, int playerBalance) {
        int[] positions = getPlayerPawnedPropertyPositions(player);
        Property[] properties = new Property[0];
        for (int i : positions) {
            Property property = (Property) fields[i];
            if (property.getPawnValue() < playerBalance) {
                properties = Utility.addToArray(properties, property);
            }
        }
        return properties;
    }

    /**
     * Checks if any of the Properties owned by the Player are pawned.
     *
     * @param player : Player in question.
     * @return : true if the Player has pawned Properties.
     */
    public boolean playerHasPawnedProperties(int player) {
        return getPlayerPawnedPropertyPositions(player).length != 0;
    }

    /**
     * Checks if the owner of the specified Property is currently in jail or if the Property is pawned.
     *
     * @param position : Position of Property in question.
     * @return : true if Property isn't pawned and owner isn't in jail.
     */
    public boolean mustPayRent(int position) {
        if (!(fields[position] instanceof Property)) return false;
        Property property = (Property) fields[position];
        return !(property.getPawned() || isInJail(property.getOwner()));
    }

    // Relevant getters
    public Field[] getFields() {
        return fields;
    }

    public Property[][] getProperties() {
        return properties;
    }

    public Property[] getPropertyGroup(int groupIndex) {
        return properties[groupIndex];
    }

}
