package chance.card;

public class Instructions {

    int cardnumber;
    int amount;
    int increment;
    int houseTax;
    int destination;
    int player;
    boolean prison;


    /**
     *
     * @param cardnumber
     * @param amount
     * @param increment
     * @param destination
     * @param houseTax
     * @param prison
     */
    public Instructions (int cardnumber, int amount, int increment, int destination, int houseTax, int player, boolean prison){

        this.cardnumber = cardnumber;
        this.amount = amount;
        this.increment = increment;
        this.prison = prison;
        this.houseTax = houseTax;
        this.player = player;
        this.destination = destination;


    }

    /**
     * cases for different types of chancecards
     */
    public void choices(){

        switch(cardnumber){
            case 1: players.makeTransaction(amount, player); break; /* transaction with bank */

            case 2: players[player].giftPlayer(amount); break; /* get from other players */

            case 3: amount = amount*houseTax;
                    players.makeTransaction(amount, player); break; /* pay price pr house ... skal lige fikses med hensyn til huse*/

            case 4: players[player].movePlayer(increment); break; /* moves player increments forward or backwards */

            case 5: players[player].setDestination(destination); break; /* moves player to a specific location */

            case 6: players[player].goToPrison(prison); break; /* sends player to prison */

            case 7: if(players.getPlayerBalance(player)==15000 || players.getPlayerBalance(player)>15000)
                        {players.makeTransaction(amount, player);} break;


        }


    }
}
