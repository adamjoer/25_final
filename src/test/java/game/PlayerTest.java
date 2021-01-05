package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player = new Player("John", 2000);

    @Test
    void makeTransaction() {

        System.out.println(player.getProperties().length);

        player.addProperty(29);
        player.addProperty(22);
        player.addProperty(6);
        player.addProperty(3);

        System.out.println(player.getProperties().length);

        assertTrue(true);
    }

    @Test
    void addProperty() {

    }
}