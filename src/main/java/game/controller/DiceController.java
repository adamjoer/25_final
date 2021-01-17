package game.controller;

import game.Die;

public class DiceController {

    // Variables
    final private Die[] dice;
    private boolean hasRolled = false;

    // Constructor: Takes number of die in beaker as argument, and the max face value of the dice.
    public DiceController(int n, int dieMax) {

        // Assign arrays with right length
        this.dice = new Die[n];

        // Make new instance of die for each number of dice
        for (int i = 0; i < n; i++) {
            this.dice[i] = new Die(dieMax);
        }
    }

    // Rolls each die in beaker using the roll function
    public void roll() {

        // Roll each die
        for (Die die : this.dice) {
            die.roll();
        }

        // Set hasRolled to true
        this.hasRolled = true;
    }

    // Returns true if all the rolled results are identical, otherwise return false
    public boolean isIdentical() {

        // Return false if dice hasn't been rolled
        if (!hasRolled) {
            return false;
        }

        // Go over each result starting at #2 and compare it to the one before it
        for (int i = 1; i < this.dice.length; i++) {

            // If the two are not equal, return false
            if (this.dice[i].getFaceValue() != this.dice[i - 1].getFaceValue()) {
                return false;
            }
        }

        // If we have gotten to this point, all the results must be identical; return true
        return true;
    }

    // Returns the face of the dice at index in the array
    public int getFaceValue(int index) {

        try {
            return this.dice[index].getFaceValue();

        } catch (Exception e) {

            // If the face value is not defined, or index is out of bounds, return zero
            return 0;
        }
    }

    // Returns the sum of all the face values if dice have been rolled, otherwise returns zero
    public int getSum() {

        // Return zero if dice hasn't been rolled
        if (!hasRolled) {
            return 0;
        }

        // Sum up all face values
        int sum = 0;
        for (Die die : this.dice) {
            sum += die.getFaceValue();
        }

        // Return sum of face values
        return sum;
    }
}
