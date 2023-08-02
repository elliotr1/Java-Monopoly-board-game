import java.util.ArrayList;

public class ModelHotel {
    private int hotelPrice;
    private ModelPlayer owner;
    private int hotelLevel;

    public ModelHotel(int hotelPrice) {
        this.hotelPrice = hotelPrice;
        this.owner = null;
        this.hotelLevel = 1;
    }
    public int getHotelLevel() {
        return hotelLevel;
    }
    public int getHotelPrice() {
        return this.hotelPrice;
    }
    public ModelPlayer getHotelOwner() {
        return this.owner;
    }
    //Assigns the player the hotel and removes it's cost from the users bank account.
    public void buyHotel(ModelPlayer p) {
        this.owner = p;
        p.removeCashFromPlayer(this.hotelPrice);
        System.out.println(p.getPlayerID() + " new balance is: £" + p.getPlayersBalance());
    }



    public void upgradeHotel(ModelPlayer p) {
        //Checks to see if the player can afford the hotel.
        if (p.checkIfPlayerCanAffordHotel(this, p)) {
            //if they can then upgrade the hotel.
            if (this.hotelLevel < 5) {
                this.hotelLevel += 1;
                p.removeCashFromPlayer(this.hotelPrice / 2);
            }
        }
        System.out.println(p.getPlayerID() + " new balance is " + p.getPlayersBalance());
    }



}
