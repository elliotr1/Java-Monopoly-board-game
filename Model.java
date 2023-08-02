import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Model {
    private final Scanner scanner = new Scanner(System.in);
    private final ModelTiles modelTiles = new ModelTiles();
    private final LinkedList<ModelPlayer> players = new LinkedList<>();
    private ModelPlayer currentPlayer;

    public ArrayList<ModelTiles> getModelTilesObjectArrayList(){
        return modelTiles.getTiles();
    }

    public ModelPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public LinkedList<ModelPlayer> getPlayers() {
        return players;
    }

    public Model() {
        //Adds the players into the world and set player 1 as the active player.
        assert modelTiles.getTiles().size() != 0 : "Error: The tile list is empty.";
        for (int i = 1; i < 3; i++) {
            ModelPlayer player = new ModelPlayer(i);
            players.add(player);
        }
        modelTiles.placePlayersOnStart(players);
        currentPlayer = players.get(0);
    }

    public String yesNoValidation(String message) {
        String value = "";
        System.out.println(message);
        //While the user has not entered either "y" (Yes) or "n" (No)
        while (!(value.equals("y") || value.equals("n"))) {
            try {
                //gets the next line.
                value = scanner.nextLine();
                //Converts the string to lower case.
                value = value.toLowerCase();
                //Validates the at entered value is not longer than 1.
                if (value.length()>1) {
                    System.out.println("Input is too long. Please enter a valid input: ");
                    value = "";
                } else if (!(value.equals("y") || value.equals("n"))) {
                    System.out.println("Please enter a valid input: ");
                }
            } catch (Exception e) {
                scanner.reset();
                System.out.println("Error: Out of line bounds.");

            }
        }
        return value;
    }

    public void switchToOtherPlayer(){
        //CHange's player to the next person in the list.
        currentPlayer = currentPlayer.getPlayerID() == 1 ? players.get(1) : players.get(0);
    }

    public ModelTiles cheatMode(int value) {
        assert currentPlayer != null : "Error: Player object is null";
        assert value >= 0 && value <= 12 : "Error: Value is out of range";
        //If the user is using the cli get the users input from the console.
        if (value == 0) {
            System.out.println("Please select from a number between 1-12: ");
            return playGame(intValidator());
        }
        //else if the inputs from the GUI pass the value in.
        return playGame(value);
    }

    public int intValidator() {
        //This function checks to see if the user has entered a valid integer.
        int value = -1;
        while (value < 1 || value > 12) {
            String selectedOption = scanner.nextLine();
            try {
                value = Integer.parseInt(selectedOption);
            } catch (NumberFormatException e) {
                System.out.println("Error: you have entered an invalid value.");
            }
            if (value > 12) {
                System.out.println("Error: the number you entered is too high.\nPlease try again: ");
            } else if (value < 1) {
                System.out.println("Error: the number you entered is too low.\nPlease try again: ");
            }
        }
        return value;
    }


    private int playersNewLocation(int rolledNumber, ModelPlayer player) {
        //Gets the player's current location.
        int playersLocation = modelTiles.getTileIndexPlayerIsOn(player);
        assert playersLocation >= 0 && playersLocation <= 41 : "Error: The player is out of range.";
        //Calculates the new location.
        int newPosition = (playersLocation + rolledNumber) % modelTiles.getTiles().size();
        assert newPosition >= 0 && newPosition <= 41 : "Error new position is out of range.";
        //Moves the player from their old position to their new position.
        modelTiles.movePlayer(playersLocation, newPosition, player);
        //If players go's past go collect £200
        if (playersLocation > newPosition) {
            System.out.println(player.getPlayerID() + " has gone past go. collect £200.");
            player.addCashToPlayerAccount(200);
        }
        return newPosition;
    }

    public ModelTiles playGame(int rolledNumber) {
        //Rolls the dice if the user has not used to GUI.
        assert modelTiles.getTiles().size() != 0 : "Error: Tile list is empty.";
        Random rand = new Random();
        if (rolledNumber == 0) {
            System.out.println("Please press any button to roll the dice");
            scanner.nextLine();
            rolledNumber = rand.nextInt(1, 12 + 1);
        }
        ModelTiles tile = modelTiles.getTile(playersNewLocation(rolledNumber, currentPlayer));
        assert tile != null : "Error: Tile is a null value.";
        //Returns the function that checks to see if user can buy or upgrade a hotel or if the hotel is owned payout to the opponent.
        return payoutOrReturnTileForUpgradingOrBuying(tile);
    }



    private ModelTiles payoutOrReturnTileForUpgradingOrBuying(ModelTiles tile) {
        //Gets the hotel on the tile.
        ModelHotel hotel = tile.getHotel();
        if (hotel != null) {
            int price = hotel.getHotelPrice();
            int level = hotel.getHotelLevel();
            System.out.printf("\ntile name: %s price: %d tile level: %d%n", tile.getPieceName(), price, level);
            //Checks to see if the hotel is not owned or if it is owned by the user return the tile.
            if ((hotel.getHotelOwner() == null || hotel.getHotelOwner() == currentPlayer) && price <= getCurrentPlayer().getPlayersBalance()){
                return tile;
            } else {
                //Else payout the other player.
                for (ModelPlayer opponent : players) {
                    if (opponent != currentPlayer) {
                        if (hotel.getHotelOwner() == opponent) {
                            assert opponent != null : "Error: Player object is null";
                            //Calls the function that pays the opponent/
                            currentPlayer.payOpponent(currentPlayer, opponent, hotel, tile.checkIfOpponentOwnsAllTiles(opponent), tile.checkIfPlayerOwnsATile(currentPlayer));
                        }
                    }
                }
            }
        }
        return null;
    }

    public void upgradeHotel(ModelTiles tile) {
        tile.getHotel().upgradeHotel(currentPlayer);
        this.modelTiles.updateTile(tile);
    }

    public void buyHotel(ModelTiles tile) {
        tile.getHotel().buyHotel(currentPlayer);
        this.modelTiles.updateTile(tile);
    }

    public ArrayList<ModelTiles> getTiles() {
        return modelTiles.getTiles();
    }
}
