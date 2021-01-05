package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player("John", 2000);

    @Test
    void makeTransaction() {

        assertTrue(player.makeTransaction(-500));

        assertTrue(player.makeTransaction(500));

        assertFalse(player.makeTransaction(-3000));

        assertFalse(player.makeTransaction(-2001));

        assertTrue(player.makeTransaction(100000));

    }

    @Test
    void addProperty() {

        player.addProperty(29);
        player.addProperty(22);
        player.addProperty(6);
        player.addProperty(3);

        assertEquals(4, player.getProperties().length);


    }

    @Test
    void removeProperty(){

        player.addProperty(29);
        player.addProperty(22);
        player.addProperty(6);
        player.addProperty(3);

        player.removeProperty(6);
        player.removeProperty(49);

        assertEquals(3, player.getProperties().length);
        assertEquals(3, player.getProperties()[2]);
    }
}