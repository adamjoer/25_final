package game.field;

public class Go extends Field {

    private final int reward;

    public Go(String title, String subText, String description, int position, int reward) {
        super(title, subText, description, position);
        this.reward = reward;
    }

    public void fieldAction() {
        // TODO: should this field do anything or will the 'Pass Go' reward be handled elsewhere?
    }

    public int getReward() {
        return reward;
    }

    public String toString() {
        return super.toString() +
               "\n\t[reward=" + reward + ']';
    }
}
