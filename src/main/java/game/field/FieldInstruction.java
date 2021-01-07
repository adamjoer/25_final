package game.field;

public class FieldInstruction {

    private final String fieldType;
    private boolean ownedByPlayer;
    private int rent;
    private int cost;
    private int propertyLevel;
    private int fineOrBailOrJailPosition;

    // Property
    public FieldInstruction(String fieldType, boolean ownedByPlayer, int rent, int cost, int propertyLevel) {
        this.fieldType = fieldType;
        this.ownedByPlayer = ownedByPlayer;
        this.rent = rent;
        this.cost = cost;
        this.propertyLevel = propertyLevel;
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

    public boolean isOwnedByPlayer() {
        return ownedByPlayer;
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
