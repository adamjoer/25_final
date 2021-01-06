package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    PlayerController pc = new PlayerController(new String[]{"John", "Elise", "Jane", "Carsten"});

    @Test
    void makeTransaction() {
        assertTrue(pc.makeTransaction(1, 1000));

        assertTrue(pc.makeTransaction(1, -1000));

        assertFalse(pc.makeTransaction(2, -30001));

        assertTrue(pc.makeTransaction(2, -30000));

        assertFalse(pc.makeTransaction(2, -3000));

    }

    @Test
    void movePlayer() {
        assertEquals(0, pc.getPlayerPosition(0));

        pc.movePlayer(0, 5);

        assertEquals(5, pc.getPlayerPosition(0));

        pc.movePlayer(0, 39);

        assertEquals(4, pc.getPlayerPosition(0));

        pc.movePlayer(3, 38);

        assertEquals(38, pc.getPlayerPosition(3));

    }

    @Test
    void addProperty() {
        assertEquals(1, pc.getProperties(0).length);

        pc.addProperty(0, 28);

        assertEquals(1, pc.getProperties(0).length);

        pc.addProperty(0, 26);
        pc.addProperty(0, 23);

        assertEquals(3, pc.getProperties(0).length);

        pc.removeProperty(0, 23);
        pc.removeProperty(0, 29);

        assertEquals(2, pc.getProperties(0).length);





    }

    @Test
    void removeProperty() {
    }

    @Test
    void giftPlayer() {
    }
}