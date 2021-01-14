package game;

import game.controller.StringHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHandlerTest {
    StringHandler stringHandler = new StringHandler("src/main/resources/stringRefs.xml");

    @Test
    void testStringReturn(){
        assertEquals("Du er landet på skat.",stringHandler.getString("tax"));
    }
}
