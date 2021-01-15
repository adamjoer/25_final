package game.controller;

import game.field.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldControllerTest {

    FieldController fieldController = new FieldController();

    Property[][] properties = fieldController.getProperties();

    int playerTurn = 0;

    @Test
    void buyProperty() {

        for (Property[] group : properties) {

            boolean groupIsShipping = group[0] instanceof Shipping,
                    expectedReturn = false;

            for (int i = 0; i < group.length; i++) {

                if (i == group.length - 1) expectedReturn = true;

                assertEquals(expectedReturn, fieldController.buyProperty(playerTurn, group[i].getPosition()));
                assertEquals(playerTurn, group[i].getOwner());

                if (groupIsShipping) expectedReturn = true;
            }
        }
        getNextPlayerTurn();

        for (Property[] group : properties) {

            for (Property property : group) {

                boolean randomBool = (int) Math.round(Math.random()) == 1;

                if (randomBool) fieldController.buyProperty(playerTurn, property.getPosition());

                assertEquals((randomBool) ? playerTurn : playerTurn - 1, property.getOwner());
            }
        }
        getNextPlayerTurn();
    }

    @Test
    void ownsAllPropertiesInGroup() {

        for (Property[] group : properties) {

            for (Property property : group) {
                fieldController.buyProperty(playerTurn, property.getPosition());
            }

            for (Property property : group) {
                assertTrue(fieldController.ownsAllPropertiesInGroup(playerTurn, property.getPosition()));
            }
        }

        getNextPlayerTurn();

        for (Property[] group : properties) {

            for (Property property : group) {
                fieldController.buyProperty(playerTurn, property.getPosition());
            }

            assertTrue(fieldController.ownsAllPropertiesInGroup(playerTurn, group[0].getPosition()));
        }
        getNextPlayerTurn();

        for (Property[] group : properties) {

            int count = 0;
            for (Property property : group) {
                int increment = (int) Math.round(Math.random());
                count += increment;

                if (increment == 1) fieldController.buyProperty(playerTurn, property.getPosition());
            }

            assertEquals(count == group.length, fieldController.ownsAllPropertiesInGroup(playerTurn, group[0].getPosition()));
        }
        getNextPlayerTurn();
    }

    @Test
    void getNumberOfPropertiesOwnedInGroup() {


        for (Property[] group : properties) {

            for (Property property : group) {
                fieldController.buyProperty(playerTurn, property.getPosition());
            }

            assertEquals(group.length, fieldController.getNumberOfPropertiesOwnedInGroup(playerTurn, group[0].getPosition()));
        }
        getNextPlayerTurn();

        for (Property[] group : properties) {

            int count = 0;

            for (Property property : group) {

                // Decide randomly if this property will be bought
                int increment = (int) Math.round(Math.random());

                // Add number of properties bought to count
                count += increment;

                // Buy if it was decided
                if (increment == 1) fieldController.buyProperty(playerTurn, property.getPosition());

            }

            assertEquals(count, fieldController.getNumberOfPropertiesOwnedInGroup(playerTurn, group[0].getPosition()));
        }

        getNextPlayerTurn();
    }

    @Test
    void getPropertyGroupIndex() {


        for (int i = 0; i < properties.length; i++) {

            for (int j = 0; j < properties[i].length; j++) {
                int randomPropertyIndexInGroup = (int) Math.round(Math.random() * (properties[i].length - 1));

                assertEquals(i, fieldController.getPropertyGroupIndex(properties[i][randomPropertyIndexInGroup].getPosition()));
            }
        }
    }

    @Test
    void buildings() {

        int houseCount = 0,
                hotelCount = 0;

        for (Property[] group : properties) {

            if (!(group[0] instanceof Street)) continue;

            for (Property property : group) {
                property.setOwner(playerTurn);

                int randomPropertyLevel = (int) Math.round(Math.random() * 4) + 2;
                property.setPropertyLevel(randomPropertyLevel);

                if (randomPropertyLevel == 6) hotelCount++;
                else houseCount += randomPropertyLevel - 1;

                assertEquals(houseCount, fieldController.getHouses(playerTurn));
                assertEquals(hotelCount, fieldController.getHotels(playerTurn));

                assertEquals(houseCount == 0 && hotelCount == 0, fieldController.propertyCanBePawned(property.getPosition()));
            }
        }
    }

    @Test
    void getCombinedPropertyWorth() {

        int worth = 0;
        assertEquals(worth, fieldController.getCombinedPropertyWorth(playerTurn));

        for (Property[] group : properties) {
            boolean groupIsStreet = group[0] instanceof Street;

            for (Property property : group) {
                property.setOwner(playerTurn);
                worth += property.getCost();

                if (groupIsStreet) {
                    property.setPropertyLevel((int) Math.round(Math.random() * 6));
                    worth += (((Street) property).getBuildingCost() / 2) * ((Street) property).getNumberOfBuildings();
                }

                assertEquals(worth, fieldController.getCombinedPropertyWorth(playerTurn));
            }
        }
        getNextPlayerTurn();
    }

    public void getNextPlayerTurn() {
        playerTurn = ++playerTurn % 6;
    }
}