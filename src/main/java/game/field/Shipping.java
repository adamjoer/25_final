package game.field;

import game.*;

import java.awt.Color;

public class Shipping extends Property {

    public Shipping(String title, String subText, String description, int position, Color color, int cost, int pawnValue, int[] rentLevels, int relatedProperties, int nextRelatedProperty) {
        super(title, subText, description, position, color, cost, pawnValue, rentLevels, relatedProperties, nextRelatedProperty);
    }

    public FieldInstruction fieldAction(int player) {
        boolean ownedByPlayer = owner == player;

        return new FieldInstruction("Shipping", ownedByPlayer, getCurrentRent(), getCost(), getPropertyLevel());
    }
}
