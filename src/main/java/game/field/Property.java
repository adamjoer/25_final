package game.field;

import java.awt.Color;
import java.util.Arrays;

public abstract class Property extends Field {

    // Attributes
    protected final int cost;
    protected final int pawnValue;
    protected final int[] rentLevels;
    protected int propertyLevel = 0;
    protected final int relatedProperties;
    protected final int nextRelatedProperty;
    protected int owner = -1; // -1 means the property is owned by the bank

    // The array rentLevels represents the cost of rent at different levels:
    //      0: Base rent
    //      1: Double rent
    //      2: With one house built on it
    //      3: With two houses built on it
    //      4: With three houses built on it
    //      5: With four houses built on it
    //      6: With a hotel built on it

    // Constructor
    public Property(String title, String subText, String description, int position, Color color, int cost, int pawnValue, int[] rentLevels, int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position, color);
        this.cost = cost;
        this.pawnValue = pawnValue;
        this.rentLevels = rentLevels;
        this.relatedProperties = relatedProperties;
        this.nextRelatedProperty = nextRelatedProperty;
    }

    public FieldInstruction fieldAction() {
        return new FieldInstruction(getField(), getOwner(), getCurrentRent(), getCost());
    }

    // Relevant getters
    public int getCurrentRent() {
        return rentLevels[propertyLevel];
    }

    public int getCost() {
        return cost;
    }

    public int getPawnValue() {
        return pawnValue;
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
                "]\n\t[pawnValue=" + pawnValue +
                "]\n\t[rentLevels=" + Arrays.toString(rentLevels) +
                "]\n\t[propertyLevel=" + propertyLevel +
                "]\n\t[relatedProperties=" + relatedProperties +
                "]\n\t[nexRelatedProperty=" + nextRelatedProperty +
                "]\n\t[owner=" + owner + ']';
    }
}
