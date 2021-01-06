package game.field;

public class Jail extends Field{

    private final int bail;

    public Jail(String title, String subText, String description, int position, int bail) {
        super(title, subText, description, position);
        this.bail = bail;
    }

    public void fieldAction() {
        //TODO: Implement getting out of jail
    }

    public String toString() {
        return super.toString() +
               "\n\t[bail=" + bail + ']';
    }
}
