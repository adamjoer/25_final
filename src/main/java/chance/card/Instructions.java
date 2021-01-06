package chance.card;

public class Instructions {

    int cardnumber;
    int amount;
    int increment;
    int priceIncrease;
    int destiantion;
    int playerNumber;
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
            case 1: players[player].adjustBalance(amount); break; /* transaction with bank */

            case 2: players[player].giftPlayer(amount); break; /* get from other players */

            case 3: players[player].adjustbalance(amount*priceIncrease); break; /* pay price pr house */

            case 4: player[player].movePlayer(increment); break; /* moves player increments forward or backwards */

            case 5: players[player].setDestination(destiantion); break; /* moves player to a specific location */

            case 6: players[player].goToPrison(prison); break; /* sends player to prison */

            case 7: if(players.getPlayerBalance(player) ==15000 || players.getPlayerBalance(player)>15000)
                        {players.makeTransaction(amount, playerNumber);} break;


        }


    }
}
