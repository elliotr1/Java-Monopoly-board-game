import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.Random;

public class ModelTest {
    ModelTiles tiles = new ModelTiles();

    private LinkedList<ModelPlayer> players = new LinkedList<>();

    @Test
    public void placePlayersOnStartTest() {
        for (int i = 1; i < 3; i++) {
            ModelPlayer player = new ModelPlayer(i);
            players.add(player);
        }
        tiles.placePlayersOnStart(players);
        for (ModelPlayer p : players) {
            assertSame(tiles.getTile(tiles.getTileIndexPlayerIsOn(p)), tiles.getTile(0));
        }
    }

    public ModelTest() {
        for (int i = 1; i < 3; i++) {
            ModelPlayer player = new ModelPlayer(i);
            players.add(player);
        }
        tiles.placePlayersOnStart(players);
    }


    @ParameterizedTest
    @ValueSource(ints = {12, 32, 1, 3, 4, 5, 6, 0, 0, -1, -15, 40})
    public void movePlayerTest(int number) {
        ModelPlayer player = players.get(0);
        assertNotNull(tiles);
        Random rand = new Random();
        if (number == 0) {
            System.out.println("Please press any button to roll the dice");
            number = rand.nextInt(1, 12 + 1);
        }
        assertTrue(number > 0 && number < 13);
        int playersLocation = tiles.getTileIndexPlayerIsOn(player);
        assertTrue(playersLocation >= 0 && playersLocation <= 41);
        int newPosition = (playersLocation + number) % tiles.getTiles().size();
        assertTrue(newPosition >= 0 && newPosition <= 41);
        tiles.movePlayer(playersLocation, newPosition, player);
        assertEquals(tiles.getTileIndexPlayerIsOn(player), newPosition);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, -10, 30, 31, -5, 50, 60})
    public void goPastGoTest(int number) {
        ModelPlayer player = players.get(0);
        assertNotNull(tiles);
        assertTrue(number > 0);
        int playersLocation = tiles.getTileIndexPlayerIsOn(player);
        tiles.movePlayer(playersLocation, 12, player);
        playersLocation = tiles.getTileIndexPlayerIsOn(player);
        assertTrue(playersLocation >= 0 && playersLocation <= 41);
        int newPosition = (playersLocation + number) % tiles.getTiles().size();
        assertTrue(newPosition >= 0 && newPosition <= 41);
        tiles.movePlayer(playersLocation, newPosition, player);
        assertTrue(playersLocation > newPosition);
    }


    @Test
    public void buyHotel() {
        int[] playerIndex = {1};
        int[] tileIndex = {6};
        for (int i = 0; i < tileIndex.length; i++) {
            tiles.getTile(tileIndex[i]).getHotel().buyHotel(players.get(playerIndex[i]));
            assertNotNull(tiles.getTile(tileIndex[i]).getHotel().getHotelOwner());
            this.tiles.updateTile(tiles.getTile(tileIndex[i]));
        }
    }
}