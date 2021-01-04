package game.field;

public class Jail extends Field{

    private final int goToJailPosition;

    public Jail(String title, String subText, String description, int position, int goToJailPosition) {
        super(title, subText, description, position);
        this.goToJailPosition = goToJailPosition;
    }

    public void fieldAction() {
        //TODO: Implement getting out of jail
    }
}
