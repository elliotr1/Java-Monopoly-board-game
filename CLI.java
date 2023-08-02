import java.util.LinkedList;

public class CLI {
    CLI(Model model) {
        while (true) {
            ModelTiles tile;
            System.out.println("Player: " + model.getCurrentPlayer().getPlayerID() + " turn");
            //This calls a function which checks to see if a user would like to enter cheat mode if y then enter cheat-mode else call play game which rolls the dice.
            if (model.yesNoValidation("Would you like to do cheat mode? y or n").equals("y")) {
                System.out.println("Please enter a number between 1 and 12");
                tile = model.cheatMode(model.intValidator());
            } else {
                tile = model.playGame(0);
            }
            //Get the returned tile and check if there's a hotel in place.
            //If there is check to see if the user wants to buy the hotel if it's already not owned or ask them if they want to upgrade it if they own it.
            if (tile != null && tile.getHotel() != null) {
                if (tile.getHotel().getHotelOwner()==null) {
                    if (model.yesNoValidation("Would you like to buy this property for £" + tile.getHotel().getHotelPrice() + " (y/n) ?").equals("y")) {
                        model.buyHotel(tile);
                    }
                } else if (tile.getHotel().getHotelOwner()!=null) {
                    {
                        if (model.yesNoValidation("Would you like to upgrade this property for £" + tile.getHotel().getHotelPrice()/2 + " (y/n) ?").equals("y")) {
                            model.upgradeHotel(tile);
                        }
                    }
                }
            }
            //Switches to the next player.
            model.switchToOtherPlayer();
        }
    }
}
