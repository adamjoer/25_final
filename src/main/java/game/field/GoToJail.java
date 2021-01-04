package game.field;

public class GoToJail extends Field {

    private final int jailPosition;

    public GoToJail(String title, String subText, String description, int position, int jailPosition) {
        super(title, subText, description, position);
        this.jailPosition = jailPosition;
    }

    public void fieldAction() {
        // TODO: Implement going to jail
    }

    public int getJailPosition() {
        return jailPosition;
    }

    public String toString() {
        return super.toString() +
               "\n\t[jailPosition=" + jailPosition + ']';
    }
}
