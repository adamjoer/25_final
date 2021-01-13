package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    PlayerController pc = new PlayerController(new String[]{"John", "Elise", "Jane", "Carsten", "Bo", "Jeff"}, 30000);

    @Test
    void makeTransaction() {
        assertTrue(pc.makeTransaction(1000, 1));

        assertTrue(pc.makeTransaction(-1000, 1));

        assertFalse(pc.makeTransaction(-30001, 2));

        assertFalse(pc.makeTransaction(-3000, 2));

        assertTrue(pc.makeTransaction(1000, 0, 3));
        assertEquals(31000, pc.getPlayerBalance(3));

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
        assertEquals(0, pc.getProperties(0).length);

        pc.addProperty(0, 28);

        assertEquals(1, pc.getProperties(0).length);

        pc.addProperty(0, 26);
        pc.addProperty(0, 23);

        assertEquals(3, pc.getProperties(0).length);

    }

    @Test
    void removeProperty() {
        assertEquals(0, pc.getProperties(1).length);

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
        for (Player p : pc.getPlayers()) {
            p.setBalance(30000);
        }


        assertTrue(pc.giftPlayer(100, 1));

        assertEquals(30500, pc.getPlayerBalance(1));
        assertEquals(29900, pc.getPlayerBalance(0));
        assertEquals(29900, pc.getPlayerBalance(2));
        assertEquals(29900, pc.getPlayerBalance(3));

        assertFalse(pc.giftPlayer(30000, 1));

    }

    @Test
    void removePlayer(){

        assertEquals(5, pc.removePlayer(0).length);
    }
}