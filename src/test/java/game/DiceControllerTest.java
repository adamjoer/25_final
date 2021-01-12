package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceControllerTest {
    

    int dies = 2;
    int dieFaces = 6;
    int tests = 1000;

    DiceController dc = new DiceController(dies, dieFaces);


    //test if the isIdentical returns the correct boolean
    @org.junit.jupiter.api.Test
    void isIdentical() {
        for (int j = 0; j < tests; j++) {
            dc.roll();
            boolean identical = true;

            //find the expected value
            for (int i = 1; i < dies; i++) {
                if (dc.getFaceValue(i) == dc.getFaceValue(i - 1)) {
                    identical = true;
                } else {
                    identical = false;
                    break;
                }
            }

            assertEquals(identical, dc.isIdentical());
        }

    }
    
    @Test
    void getSum() {
        for (int j = 0; j < tests; j++) {
            dc.roll();

            int sum = 0;

            for (int i = 0; i < dies; i++) {
                sum += dc.getFaceValue(i);
            }

            assertEquals(sum, dc.getSum());
        }

    }
}