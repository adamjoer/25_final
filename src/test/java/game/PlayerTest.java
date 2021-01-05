package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player("John", 2000);

    @Test
    void makeTransaction() {

        player.getProperties();
        assertTrue(player.makeTransaction(10));
    }

    @Test
    void addProperty() {
    }
}