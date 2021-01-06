package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    PlayerController pc = new PlayerController(new String[]{"John", "Elise", "Jane", "Carsten", "Bo", "Jeff"}, 30000);

    @Test
    void makeTransaction() {
        assertTrue(pc.makeTransaction(1, 1000));

        assertTrue(pc.makeTransaction(1, -1000));

        assertFalse(pc.makeTransaction(2, -30001));

        assertTrue(pc.makeTransaction(2, -30000));

        assertFalse(pc.makeTransaction(2, -3000));

    }

    @Test
    void makePlayerTransaction() {

        for (Player p : pc.players) {
            p.setBalance(30000);
        }

        assertTrue(pc.makePlayerTransaction(0, 2, 20000));

        assertEquals(10000, pc.getPlayerBalance(0));
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

    }

    @Test
    void removeProperty() {
        assertEquals(1, pc.getProperties(1).length);

        pc.addProperty(1, 11);
        pc.addProperty(1, 20);

        pc.removeProperty(1, 11);

        assertEquals(1, pc.getProperties(1).length);

        pc.addProperty(1, 12);
        pc.addProperty(1, 21);

        pc.removeProperty(1, 12);

        assertEquals(2, pc.getProperties(1).length);
    }

    @Test
    void giftPlayer() {
        for (Player p : pc.players) {
            p.setBalance(30000);
        }


        assertTrue(pc.giftPlayer(1, 100));

        assertEquals(30500, pc.getPlayerBalance(1));
        assertEquals(29900, pc.getPlayerBalance(0));
        assertEquals(29900, pc.getPlayerBalance(2));
        assertEquals(29900, pc.getPlayerBalance(3));

        assertFalse(pc.giftPlayer(1, 30000));

    }
}