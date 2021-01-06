package game.field;

import java.util.Arrays;

public class Shipping extends Field {

    private final int cost;
    private final int pawnValue;
    private final int[] rentLevels;
    private int propertyLevel = 0;
    private final int relatedProperties;
    private final int nextRelatedProperty;
    private int owner = -1; // -1 means the property is owned by the bank

    public Shipping(String title, String subText, String description, int position, int cost, int pawnValue, int[] rentLevels,
                    int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position);
        this.cost = cost;
        this.pawnValue = pawnValue;
        this.rentLevels = rentLevels;
        this.relatedProperties = relatedProperties;
        this.nextRelatedProperty = nextRelatedProperty;
    }

    public void fieldAction() {
        // TODO: Implement whatever happens on this field
    }

    // Relevant getters
    public int getCost() {
        return cost;
    }

    public int getPawnValue() {
        return pawnValue;
    }

    public int[] getRentLevels() {
        return rentLevels;
    }

    public int getRelatedProperties() {
        return relatedProperties;
    }

    public int getNextRelatedProperty() {
        return nextRelatedProperty;
    }

    //Relevant setters
    public void setPropertyLevel(int propertyLevel) {
        this.propertyLevel = propertyLevel;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String toString() {
        return super.toString() +
               "\n\t[cost=" + cost +
               "]\n\t[pawnValue=" + pawnValue +
               "]\n\t[rentLevels=" + Arrays.toString(rentLevels) +
               "]\n\t[propertyLevel=" + propertyLevel +
               "]\n\t[relatedProperties=" + relatedProperties +
               "]\n\t[nexRelatedProperty=" + nextRelatedProperty +
               "]\n\t[owner=" + owner + ']';
    }
}
