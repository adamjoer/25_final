package game.field;

public class Start extends Field {

    public Start(String title, String subText, String description, int position) {
        super(title, subText, description, position);
    }

    public void fieldAction() {
        // Do nothing, 'pass go' reward will be handled in Board
    }
}
