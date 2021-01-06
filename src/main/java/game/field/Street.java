package game.field;

import java.awt.Color;
import java.util.Arrays;

public class Street extends Field {

    // Attributes
    private final int cost;
    private final int buildingCost;
    private final int pawnValue;
    private final Color color;
    private final int[] rentLevels;
    private int propertyLevel = 0;
    private final int relatedProperties;
    private final int nextRelatedProperty;
    private int owner = -1; // -1 means the property is owned by the bank

    // The array rentLevels represents the cost of rent at different levels:
    //      0: Base rent
    //      1: Double rent
    //      2: With one house built on it
    //      3: With two houses built on it
    //      4: With three houses built on it
    //      5: With four houses built on it
    //      6: With a hotel built on it

    public Street(String title, String subText, String description, int position, int cost, int buildingCost, int pawnValue, Color color, int[] rent,
                  int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position);
        this.cost = cost;
        this.buildingCost = buildingCost;
        this.pawnValue = pawnValue;
        this.color = color;
        this.rentLevels = rent;
        this.relatedProperties = relatedProperties;
        this.nextRelatedProperty = nextRelatedProperty;
    }

    public void fieldAction() {
        // TODO: Implement buying property, or paying rent
    }

    // Relevant getters
    public int getCurrentRent() {
        return rentLevels[propertyLevel];
    }

    public int getNumberOfHouses() {
        return switch (propertyLevel) {
            case 0, 1, 6 -> 0;
            case 2, 3, 4, 5 -> propertyLevel - 1;
            default -> throw new IllegalArgumentException("propertyLevel has invalid value: " + propertyLevel);
        };
    }

    public int getNumberOfHotels() {
        if (propertyLevel == 6) return 1;
        return 0;
    }

    public int getCost() {
        return cost;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public int getPawnValue() {
        return pawnValue;
    }

    public Color getColor() {
        return color;
    }

    public int[] getRentLevels() {
        return rentLevels;
    }

    public int getPropertyLevel() {
        return propertyLevel;
    }

    public int getRelatedProperties() {
        return relatedProperties;
    }

    public int getNextRelatedProperty() {
        return nextRelatedProperty;
    }

    public int getOwner() {
        return owner;
    }

    // Relevant setters
    public void setPropertyLevel(int propertyLevel) {
        this.propertyLevel = propertyLevel;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String toString() {
        return super.toString() +
               "\n\t[cost=" + cost +
               "]\n\t[buildingCost=" + buildingCost +
               "]\n\t[pawnValue=" + pawnValue +
               "]\n\t[Color=\"" + color.toString() +
               "\"]\n\t[rentLevels=" + Arrays.toString(rentLevels) +
               "]\n\t[propertyLevel=" + propertyLevel +
               "]\n\t[relatedProperties=" + relatedProperties +
               "]\n\t[nexRelatedProperty=" + nextRelatedProperty +
               "]\n\t[owner=" + owner + ']';
    }
}
