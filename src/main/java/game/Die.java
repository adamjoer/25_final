package game;
import java.lang.Math;

public class Die {
    // Variables
    private int faceValue;
    final private int dieMax;

    // Constructor, setting the highest face value of the dice, e.g. a D6 or D20.
    public Die (int maxValue) {
        dieMax = maxValue;
    }

    //Roll the dice, and return the value
    public void roll(){
        this.faceValue = (int)(Math.random()*dieMax) + 1;
    }

    //Get the current value of the dice
    public int getFaceValue(){
        return this.faceValue;
    }
}