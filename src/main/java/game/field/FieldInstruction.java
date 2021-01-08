package game.field;

public class FieldInstruction {

    private final String fieldType;
    private int owner;
    private int rent;
    private int cost;
    private int propertyLevel;
    private int fineOrBailOrJailPosition;
    private final String SUB_TEXT;

    // Property
    public FieldInstruction(String fieldType, int owner, int rent, int cost, String SUB_TEXT) {
        this.fieldType = fieldType;
        this.owner = owner;
        this.rent = rent;
        this.cost = cost;
        this.SUB_TEXT = SUB_TEXT;
    }

    // Chance, Start, Parking,
    public FieldInstruction(String fieldType, String SUB_TEXT) {
        this.fieldType = fieldType;
        this.SUB_TEXT = SUB_TEXT;
    }

    // TaxField, Jail, GoToJail
    public FieldInstruction(String fieldType, int fineOrBailOrJailPosition, String SUB_TEXT) {
        this.fieldType = fieldType;
        this.fineOrBailOrJailPosition = fineOrBailOrJailPosition;
        this.SUB_TEXT = SUB_TEXT;
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

    public String getSubText(){
        return SUB_TEXT;
    }
}
