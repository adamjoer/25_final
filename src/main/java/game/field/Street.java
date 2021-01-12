package game.field;

import java.awt.Color;

public class Street extends Property {

    // Attributes
    private final int buildingCost;

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

    public int getBuildingCost() {
        return buildingCost;
    }

    public String toString() {
        return super.toString() +
               "\n\t[buildingCost=" + buildingCost + ']';
    }
}
