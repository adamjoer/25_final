package chance.card;

public class Instructions {

    int cardnumber;
    int amount;
    int increment;
    int priceIncrease;
    int destiantion;
    boolean prison;


    /**
     *
     * @param cardnumber
     * @param amount
     * @param increment
     * @param destiantion
     * @param priceIncrease
     * @param prison
     */
    public Instructions (int cardnumber, int amount, int increment, int destiantion, int priceIncrease, boolean prison){

        this.cardnumber = cardnumber;
        this.amount = amount;
        this.increment = increment;
        this.prison = prison;
        this.priceIncrease = priceIncrease;
        this.destiantion = destiantion;


    }

    /**
     * cases for different types of chancecards
     */
    public void choices(){

        switch(cardnumber){
            case 1: players[player].adjustBalance(amount); break; /* get from bank */

            case 2: players[player].adjustBalance(-amount); break; /* give to bank */

            case 3: players[player].giftPlayer(amount); break; /* get from other players */

            case 4: players[player].adjustbalance(amount*priceIncrease); break; /* pay price pr house */

            case 5: player[player].movePlayer(increment); break; /* moves player increments forward or backwards */

            case 6: players[player].setDestination(destiantion); break; /* moves player to a specific location */

            case 7: players[player].goToPrison(prison); break; /* sends player to prison */
        }


    }
}
