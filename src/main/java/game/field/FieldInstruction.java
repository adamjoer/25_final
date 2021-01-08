package game.field;

public class FieldInstruction {

    private final String fieldType;
    private int owner;
    private int rent;
    private int cost;
    private int propertyLevel;
    private int fineOrBailOrJailPosition;

    // Property
    public FieldInstruction(String fieldType, int owner, int rent, int cost) {
        this.fieldType = fieldType;
        this.owner = owner;
        this.rent = rent;
        this.cost = cost;
    }

    // Chance, Start, Parking,
    public FieldInstruction(String fieldType) {
        this.fieldType = fieldType;
    }

    // TaxField, Jail, GoToJail
    public FieldInstruction(String fieldType, int fineOrBailOrJailPosition) {
        this.fieldType = fieldType;
        this.fineOrBailOrJailPosition = fineOrBailOrJailPosition;
    }

    // Relevant getters
    public String getFieldType() {
        return fieldType;
    }

    public int getOwner() {
        return owner;
    }

    public int getRent() {
        return rent;
    }

    public int getCost() {
        return cost;
    }

    public int getPropertyLevel() {
        return propertyLevel;
    }

    public int getFine() {
        return fineOrBailOrJailPosition;
    }

    public int getBail() {
        return fineOrBailOrJailPosition;
    }

    public int getJailPosition() {
        return fineOrBailOrJailPosition;
    }
}
