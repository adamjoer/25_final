package game.field;

import java.awt.Color;

public class Shipping extends Property {

    // In Shipping, the array rentLevels represents the cost of rent at different levels:
    //      0: Base rent
    //      1: With two properties owned in the group
    //      2: With three properties owned in the group
    //      3: With four properties owned in the group
    // Which level the property is at, is shown with the propertyLevel attribute, which is just the index in this array

    public Shipping(String title, String subText, String description, int position, Color color, int cost, int pawnValue, int[] rentLevels, int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position, color, cost, pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
    }
}
