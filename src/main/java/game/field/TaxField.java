package game.field;

public class TaxField extends Field {

    private final int fine;

    public TaxField(String title, String subText, String description, int position, int fine) {
        super(title, subText, description, position);
        this.fine = fine;
    }

    public void fieldAction() {
        // TODO: Implement whatever happens on this field
    }

    //Relevant getters
    public int getFine() {
        return fine;
    }

    public String toString() {
        return super.toString() +
               "\n\t[fine=" + fine + ']';
    }
}
