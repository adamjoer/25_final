package game.field;

import java.awt.Color;

public class Street extends Property {

    // Attributes
    private final int buildingCost;

    // In Street, the array rentLevels represents the cost of rent at different levels:
    //      0: Base rent
    //      1: Double rent
    //      2: With one house built on it
    //      3: With two houses built on it
    //      4: With three houses built on it
    //      5: With four houses built on it
    //      6: With a hotel built on it
    // Which level the property is at, is shown with the propertyLevel attribute, which is just the index in this array


    public Street(String title, String subText, String description, int position, Color color, int cost, int pawnValue, int[] rentLevels, int relatedProperties, int nextRelatedProperty, int buildingCost) {
        super(title, subText, description, position, color, cost, pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
        this.buildingCost = buildingCost;
    }

    // Relevant getters
    public int getNumberOfBuildings() {
        return switch (propertyLevel) {
            case 0, 1 -> 0;
            case 2, 3, 4, 5 -> propertyLevel - 1;
            case 6 -> 1;
            default -> throw new IllegalArgumentException("propertyLevel has invalid value: " + propertyLevel);
        };
    }

    public int getHouses() {
        if (propertyLevel < 6) return getNumberOfBuildings();
        return 0;
    }

    public int getHotel() {
        if (propertyLevel == 6) return 1;
        return 0;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public String toString() {
        return super.toString() +
                "\n\t[buildingCost=" + buildingCost + ']';
    }
}
