package game.field;

import java.awt.Color;

public class Brewery extends Property {

    // In Brewery, the array rentLevels represents the multiplier which will be used to calculate the cost of rent at different levels:
    // (rent = multiplier * sum of dice)
    //      0: With one property owned in the group
    //      1: With two properties owned in the group
    // Which level the property is at, is shown with the propertyLevel attribute, which is just the index in this array

    public Brewery(String title, String subText, String description, int position, Color color, int cost, int pawnValue, int[] rentLevels, int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position, color, cost, pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
    }

}
