package game.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHandlerTest {
    StringHandler stringHandler = new StringHandler("stringRefsDA.xml");

    @Test
    void testStringReturn(){
        assertEquals("De er landet på skat.",stringHandler.getString("tax"));
    }
}
