package game.chance.card;

public abstract class ChanceCard {

    protected final String CARD_TEXT;

    public ChanceCard(String CARD_TEXT) { this.CARD_TEXT = CARD_TEXT; }

    public String getCardText() { return CARD_TEXT; }

    public String getChanceCard() { return getClass().getSimpleName(); }
    public abstract boolean getDoubleRent();
    public abstract boolean getForward();
    public abstract int getJailPosition();
    public abstract int getAmount();
    public abstract int getHouseTax();
    public abstract int getHotelTax();
    public abstract int getThreshold();
    public abstract int getIncrement();
    public abstract int getDestination();
    public abstract int[] getShippingLocations();
    public abstract String getFailText();
    public abstract String getSuccessText();

}
