import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

public class Controller {
    private final Model model;
    private final View view;
    private ModelTiles currentTile;
    private boolean cheatModeEnabled = false;


    public Controller(Model model, View view) {
        assert view != null : "Error: The view object is null";
        assert model != null : "Error: the model object is null";
        this.view = view;
        this.model = model;
        //Builds the board tiles in the view.
        this.view.buildBoard(model.getTiles());
        assert model.getPlayers() != null : "Error the player list function returned a  null object";
        //Adds the event listeners.
        addActionListeners();
        //Places the players on the board.
        updatePlayerViewComponents();
    }

    private void updatePlayerViewComponents() {
        //Updates the player balance on the board.
        for (ModelPlayer p : model.getPlayers()) {
            view.setPlayerBalance(p);
        }
        //Updates the players locations on the board.
        view.setAndUpdatePlayersLocationInTheGUIWorld(model.getModelTilesObjectArrayList());
        //Updates the current player on the board.
        view.currentPlayersTurn(model.getCurrentPlayer());
    }

    private void addActionListeners() {
        assert view.getDice() != null : "Error: The dice view button is null.";
        assert view.getYes() != null : "Error: The yes view button is null.";
        assert view.getNo() != null : "Error: The no view button is null";
        ActionListener listener = e -> {
            if (e.getSource() == view.getDice()) {
                //On click roll the dice/Get a random number.
                Random rand = new Random();
                int rolledNumber = rand.nextInt(1, 12 + 1);
                //Calls the function that runs the game.
                currentTile = model.playGame(rolledNumber);
                //Updates the current player's location.
                view.setAndUpdatePlayersLocationInTheGUIWorld(model.getTiles());
                //Calls the function that will display the upgrade options/buy if the hotel can be bought or upgraded.
                hotelUpgradeOption();
            }
            if (e.getSource() == view.getCheatMode()) {
                //Show the's input container.
                if (!cheatModeEnabled) {
                    view.enableCheatMode();
                    cheatModeEnabled = true;
                } else {
                    //Gets the users input.
                    int number = view.cheatModeNumber();
                    //If the user input is valid.
                    if (number != -1) {
                        //Hides the input container.
                        cheatModeEnabled = false;
                        //Disables cheatmode whilst the game is running.
                        view.enableCheatMode();
                        //Runs the cheat mode option.
                        currentTile = model.cheatMode(number);
                        //Updates players location.
                        view.setAndUpdatePlayersLocationInTheGUIWorld(model.getTiles());
                        hotelUpgradeOption();
                    }
                }


            }
        };
        //Adds the event listener.
        view.addActionListeners(listener);
    }

    private void hotelUpgradeOption() {
        //If the doesn't contain a hotel or the player has already paid out.
        if (currentTile != null && currentTile.getHotel() != null) {
            //Calls the function that displays the yes and no upgrade/buy buttons and waits until input is given.
            onTileAction(currentTile, currentTile.getHotel().getHotelOwner()!=null ? "upgrade" : "buy");
        } else {
            //Change turns.
            model.switchToOtherPlayer();
            updatePlayerViewComponents();
        }
    }

    private void onTileAction(ModelTiles tile, String buyOrUpgradeHotel) {
        switch (buyOrUpgradeHotel) {
            case "buy" -> {
                //Updates the text and enables buy/upgrade yes and no buttons.
                view.enableBuyAndUpgradeYesAndNoButtonsAndDisableDiceAndCheatModeButtons("Would you like to buy " + tile.getPieceName() + " property for £" +
                        tile.getHotel().getHotelPrice() + " ?", model.getCurrentPlayer());
                ActionListener yesListener = e -> {
                    if (tile.getHotel().getHotelOwner()==null) {
                        model.buyHotel(tile);
                        view.updateHotelPricesAndOwnershipRating(tile);
                        //Waits for a response.
                        SwingUtilities.invokeLater(view::disableYesAndNoButtonsAndEnableDiceAndCheatMode);
                        model.switchToOtherPlayer();
                        updatePlayerViewComponents();
                    }
                };
                updatePlayersState(yesListener);
            }
            case "upgrade" -> {
                view.enableBuyAndUpgradeYesAndNoButtonsAndDisableDiceAndCheatModeButtons("Would you like to upgrade " + tile.getPieceName() + " for £"
                        + tile.getHotel().getHotelPrice() + " ?", model.getCurrentPlayer());
                ActionListener yesListener = e -> {
                    model.upgradeHotel(tile);
                    //Updates the newly upgraded price and tier.
                    view.updateHotelPricesAndOwnershipRating(tile);
                    SwingUtilities.invokeLater(view::disableYesAndNoButtonsAndEnableDiceAndCheatMode);
                    model.switchToOtherPlayer();
                    updatePlayerViewComponents();
                };
                updatePlayersState(yesListener);
            }
        }
    }


    private void updatePlayersState(ActionListener yesListener) {
        view.getYes().addActionListener(yesListener);
        ActionListener noListener = e -> {
            SwingUtilities.invokeLater(view::disableYesAndNoButtonsAndEnableDiceAndCheatMode);
            model.switchToOtherPlayer();
            updatePlayerViewComponents();
        };
        view.getNo().addActionListener(noListener);
    }
}
