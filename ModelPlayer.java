public class ModelPlayer {
    private int playerID;
    private int playersCash;

    public ModelPlayer(int playerID) {
        this.playerID = playerID;
        this.playersCash = 2000;
    }

    //Deducts n amount of cash from the users bank account.
    public void removeCashFromPlayer(int cash) {
        this.playersCash -= cash;
    }

    //Adds n amount of cash to the users bank account.
    public void addCashToPlayerAccount(int cash) {
        this.playersCash += cash;
    }


    public int getPlayersBalance() {
        return playersCash;
    }

    //Checks to see if the user can afford the hotel.
    public boolean checkIfPlayerCanAffordHotel(ModelHotel h, ModelPlayer p) {
        return p.getPlayersBalance() - h.getHotelPrice() >= 0;
    }

    //Pay the opponent based using the multiple factors.
    public void payOpponent(ModelPlayer currentPlayer, ModelPlayer opponent, ModelHotel h, boolean playerOwnsATile, boolean opponentOwnsAllTiles) {
        int cost;
        if (playerOwnsATile) {
            cost = ((int) (h.getHotelPrice() * .1) / 2) * h.getHotelLevel();
            currentPlayer.removeCashFromPlayer(cost);
            opponent.addCashToPlayerAccount(cost);
        } else if (opponentOwnsAllTiles) {
            cost = ((int) (h.getHotelPrice() * .1) * 2) * h.getHotelLevel();
            currentPlayer.removeCashFromPlayer(cost);
            opponent.addCashToPlayerAccount(cost);
        } else {
            cost = (int) (h.getHotelPrice() * .1) * h.getHotelLevel();
            currentPlayer.removeCashFromPlayer(cost);
            opponent.addCashToPlayerAccount(cost);
        }
        System.out.println(cost);
        if (cost > currentPlayer.playersCash) {
            System.out.println("Game over: " + currentPlayer.playerID + " has gone broke");
            System.exit(0);
        }
        System.out.println("Player: " + currentPlayer.playerID + " paid: " + opponent.playerID + " £" + cost);
        System.out.println(currentPlayer.getPlayerID() + " new balance is " + currentPlayer.getPlayersBalance());
    }

    public int getPlayerID() {
        return playerID;
    }
}
