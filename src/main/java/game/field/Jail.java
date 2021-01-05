package game.field;

public class Jail extends Field{

    private final int bail;
    private final int goToJailPosition;

    public Jail(String title, String subText, String description, int position, int bail, int goToJailPosition) {
        super(title, subText, description, position);
        this.goToJailPosition = goToJailPosition;
        this.bail = bail;
    }

    public void fieldAction() {
        //TODO: Implement getting out of jail
    }
}
