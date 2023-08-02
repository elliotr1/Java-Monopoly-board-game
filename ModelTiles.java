import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;

public class ModelTiles {
    private final ArrayList<ModelHotel> hotelsList = new ArrayList<>();
    private final ArrayList<ModelTiles> tiles = new ArrayList<>();
    private String pieceName;
    private ModelHotel hotel;
    private final ArrayList<ModelPlayer> playersOnTile = new ArrayList<>();

    private ModelTiles(String pieceName, ModelHotel hotel) {
        this.pieceName = pieceName;
        this.hotel = hotel;
    }

    public ModelTiles() {
        //Calls the function that builds the hotels.
        buildHotels();
        //Creates a iterate for the hotel list object.
        //Builds the tiles using ascii and a counter.
        ListIterator<ModelHotel> itr;
        itr = hotelsList.listIterator();
        assert hotelsList.size() != 0 : "Error: The Hotel list is empty.";
        int charNumber = 65;
        String letter = String.valueOf((char) charNumber);
        int spaceCounter = 1;
        int letterCounter = 1;
        tiles.add(new ModelTiles("Go", null));
        while (!letter.equals("I")) {
            letter = String.valueOf((char) charNumber);
            if (spaceCounter == 2) {
                tiles.add(new ModelTiles("", null));
                spaceCounter++;
            } else if (spaceCounter == 0) {
                tiles.add(new ModelTiles("", null));
                spaceCounter++;
            } else if (spaceCounter < 4) {
                tiles.add(new ModelTiles(letter + letterCounter, itr.next()));
                letterCounter++;
                spaceCounter++;
            } else {
                tiles.add(new ModelTiles(letter + letterCounter, itr.next()));
                letterCounter = 1;
                spaceCounter = 0;
                charNumber++;
            }
        }
        assert tiles.size() != 0 : "Error: The tile list is empty.";
    }

    //Builds the hotels and adds them to a list.
    private void buildHotels() {
        int cost = 50;
        int counter = 0;
        for (int i = 0; i < 25; i++) {
            if (counter == 2) {
                cost += 20;
            } else if (counter == 3) {
                cost += 30;
                counter = 0;
            }
            hotelsList.add(new ModelHotel(cost));
            counter++;
        }
    }

    public String getPieceName() {
        return pieceName;
    }

    public ModelHotel getHotel() {
        return this.hotel;
    }

    public ModelTiles getTile(int index) {
        return tiles.get(index);
    }

    public ArrayList<ModelPlayer> getPlayersOnTile() {
        return playersOnTile;
    }

    public ArrayList<ModelTiles> getTiles() {
        return tiles;
    }


    public boolean checkIfPlayerOwnsATile(ModelPlayer player) {
        char character = pieceName.charAt(0);
        //Iterates over the tiles in the game and checks to see if the current player owns any of the hotels on the current block.
        for (ModelTiles tile : tiles) {
            if (tile.pieceName.charAt(0) == character && tile.hotel.getHotelOwner() == player) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfOpponentOwnsAllTiles(ModelPlayer opponent) {
        int counter = 0;
        //Iterates over the hotels and determines if the opponent owns all three hotels on the current block.
        char character = pieceName.charAt(0);
        for (ModelTiles tile : tiles) {
            if (tile.pieceName.charAt(0) == character && tile.hotel.getHotelOwner() == opponent) {
                counter++;
            }
        }
        return counter == 3;
    }

    //Updates the tiles in the game.
    public void updateTile(ModelTiles t) {
        for (int i = 0; i < tiles.size(); i++) {
            if (Objects.equals(tiles.get(i).pieceName, t.pieceName)) {
                this.tiles.set(i, t);
            }
        }
    }

    //Adds the players to start.
    public void placePlayersOnStart(LinkedList<ModelPlayer> players) {
        for (ModelPlayer p : players) {
            tiles.get(0).playersOnTile.add(p);
        }
    }

    //Moves the current player to their new location.
    public void movePlayer(int previousIndex, int newIndex, ModelPlayer player) {
        System.out.println(player.getPlayerID() + " was previously at: " + previousIndex);
        tiles.get(previousIndex).playersOnTile.remove(player);
        System.out.println(player.getPlayerID() + " is now at: " + newIndex);
        tiles.get(newIndex).playersOnTile.add(player);
    }

    //Get the space that the user is on.
    public int getTileIndexPlayerIsOn(ModelPlayer player) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).playersOnTile.contains(player)) {
                return i;
            }
        }
        return -1;
    }
}