import javax.swing.*;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.*;

public class View extends JFrame, IObserved {
    private final JLabel instruction = new JLabel("");
    private ArrayList<TextAreaTile> rectangles = new ArrayList<>();
    private final JLabel playersTurn = new JLabel();
    ArrayList<IObservers> observers;
    
    private final JButton yes = new JButton("Yes");
    private final JButton no = new JButton("No");
    private final JButton dice = new JButton("Roll dice");
    private final JButton cheatMode = new JButton("Cheat mode");
    private final TextField cheatModeNumber = new TextField("");
    private final JLabel player1Balance = new JLabel("");
    private final JLabel player2Balance = new JLabel("");
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();


    private @Override void appendListener(IObserver obs){
        observer.add(obs);
    }

    private @Override void removeListener(IObserver obs){
        observer.remove(osb);
    }


    private @Override void void notify(){
        for(IObserver observer: observers){
            observer.update();
        }
    }

    public View() {
        observer = new ArrayList<IObserver>();
        
        JFrame f = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());

        yes.setBackground(Color.WHITE);
        no.setBackground(Color.WHITE);
        dice.setBackground(Color.WHITE);
        cheatMode.setBackground(Color.white);
        topPanel.setBackground(Color.white);
        bottomPanel.setBackground(Color.gray);
        bottomPanel.setLayout(new FlowLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        int height = (int) screenSize.getHeight();
        int topPanelHeight = (int) (height * 0.78);
        int bottomPanelHeight = (int) (height * 0.22);

        topPanel.setSize(new Dimension(100, topPanelHeight));
        bottomPanel.setSize(new Dimension(100, bottomPanelHeight));

        Dimension size = dice.getPreferredSize();
        size.setSize(70, 70);

        player1Balance.setForeground(new Color(93, 70, 226));
        player2Balance.setForeground(new Color(255, 31, 31));

        dice.setPreferredSize(size);
        yes.setPreferredSize(size);
        no.setPreferredSize(size);
        cheatMode.setPreferredSize(size);
        cheatModeNumber.setPreferredSize(new Dimension(40, 20));
        cheatModeNumber.setEnabled(false);
        yes.setEnabled(false);
        no.setEnabled(false);


        bottomPanel.add(playersTurn);
        bottomPanel.add(player1Balance);
        bottomPanel.add(instruction);
        bottomPanel.add(dice);
        bottomPanel.add(cheatMode);
        bottomPanel.add(cheatModeNumber);
        bottomPanel.add(yes);
        bottomPanel.add(no);
        bottomPanel.add(player2Balance);

        bottomPanel.setBackground(new Color(224, 238, 255));
        topPanel.setBackground(new Color(200, 200, 200));
        //Splits the panel into two.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(0.75);
        splitPane.setOneTouchExpandable(false);
        splitPane.setEnabled(false);
        splitPane.setDividerLocation(topPanelHeight);
        splitPane.setResizeWeight(1.0);
        panel.add(splitPane, BorderLayout.CENTER);

        f.setContentPane(panel);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1000, 800);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    //Update's the current player's balance in the GUI.
    public void setPlayerBalance(ModelPlayer player) {
        if (player.getPlayerID() == 1) {
            player1Balance.setText("Player 1's balance is: £" + player.getPlayersBalance());
            player1Balance.setVisible(true);
        } else {
            player2Balance.setText("Player 2's balance is: £" + player.getPlayersBalance());
            player2Balance.setVisible(true);
        }
    }

    //Update's the current player's turn indicator.
    public void currentPlayersTurn(ModelPlayer player) {
        playersTurn.setText("Current players turn: " + player.getPlayerID());
        playersTurn.setVisible(true);
        instruction.setVisible(false);
    }

    //Enables the yes and no buy/upgrade buttons.
    public void enableBuyAndUpgradeYesAndNoButtonsAndDisableDiceAndCheatModeButtons(String text, ModelPlayer currentPlayersTurn) {
        if (currentPlayersTurn.getPlayerID() == 1) {
            player2Balance.setVisible(false);
            player1Balance.setText("Your balance is: £" + currentPlayersTurn.getPlayersBalance());
        } else {
            player1Balance.setVisible(false);
            player2Balance.setText("Your balance is: £" + currentPlayersTurn.getPlayersBalance());
        }
        playersTurn.setVisible(false);
        cheatMode.setEnabled(false);
        dice.setEnabled(false);
        yes.setEnabled(true);
        no.setEnabled(true);
        instruction.setVisible(true);
        instruction.setText(text);
    }

    //Updates the players locations in the world.
    public void setAndUpdatePlayersLocationInTheGUIWorld(ArrayList<ModelTiles> tileLocations) {
        for (ModelTiles tile : tileLocations) {
            for (TextAreaTile rect : rectangles) {
                if (Objects.equals(rect.pieceName, tile.getPieceName())) {
                    rect.setPlayersOnPieces(tile.getPlayersOnTile());
                }
            }
        }
    }

    public void disableYesAndNoButtonsAndEnableDiceAndCheatMode() {
        yes.setEnabled(false);
        no.setEnabled(false);
        dice.setEnabled(true);
        cheatMode.setEnabled(true);
    }

    //Builds the object tiles in the world and adds the prices and ratings.
    private TextAreaTile buildTile(int x, int y, int width, int height, ModelTiles tile) {
        TextAreaTile piece = new TextAreaTile(x, y, width, height, tile.getPieceName());
        if (tile.getHotel() != null) {
            piece.setPriceAndStarRating(tile.getHotel().getHotelPrice(), 0);
        }
        return piece;
    }

    public void buildBoard(ArrayList<ModelTiles> tiles) {
        int pieceIndex = 0;
        topPanel.setLayout(null);
        TextAreaTile corner = new TextAreaTile(100, 5, 70, 70, "Go");
        topPanel.add(corner);
        rectangles.add(corner);
        int x = 170;
        int y = 5;

        for (int i = 0; i < 9; i++) {
            pieceIndex++;
            ModelTiles tile = tiles.get(pieceIndex);
            TextAreaTile piece = buildTile(x, y, 50, 70, tile);
            topPanel.add(piece);
            rectangles.add(piece);
            x += 50;
        }
        pieceIndex++;

        corner = new TextAreaTile(x, y, 70, 70, tiles.get(pieceIndex).getPieceName());
        topPanel.add(corner);
        rectangles.add(corner);

        y += 70;

        for (int i = 0; i < 9; i++) {
            pieceIndex++;
            ModelTiles tile = tiles.get(pieceIndex);
            TextAreaTile piece = buildTile(x, y, 70, 50, tile);
            topPanel.add(piece);
            rectangles.add(piece);
            y += 50;
        }
        pieceIndex++;
        corner = new TextAreaTile(x, y, 70, 70, tiles.get(pieceIndex).getPieceName());
        topPanel.add(corner);
        rectangles.add(corner);
        x -= 50;

        for (int i = 0; i < 9; i++) {
            pieceIndex++;
            ModelTiles tile = tiles.get(pieceIndex);
            TextAreaTile piece = buildTile(x, y, 50, 70, tile);
            topPanel.add(piece);
            rectangles.add(piece);
            x -= 50;
        }
        x -= 20;
        pieceIndex++;
        corner = new TextAreaTile(x, y, 70, 70, tiles.get(pieceIndex).getPieceName());
        topPanel.add(corner);
        rectangles.add(corner);
        y -= 50;

        for (int i = 0; i < 9; i++) {
            pieceIndex++;
            ModelTiles tile = tiles.get(pieceIndex);
            TextAreaTile piece = buildTile(x, y, 70, 50, tile);
            topPanel.add(piece);
            rectangles.add(piece);
            y -= 50;
        }
        topPanel.revalidate();
        topPanel.repaint();
    }

    public void addActionListeners(ActionListener actionListener) {
        cheatMode.addActionListener(actionListener);
        dice.addActionListener(actionListener);
    }

    public void enableCheatMode() {
        if (cheatModeNumber.isEnabled()) {
            cheatModeNumber.setText("");
        }
        cheatModeNumber.setEnabled(!cheatModeNumber.isEnabled());

    }

    //Validates the users int input.
    public int cheatModeNumber() {
        String stringNum = cheatModeNumber.getText();
        try {
            int number = Integer.parseInt(stringNum);
            if (number > 0 && number < 13) {
                return number;
            }
        } catch (NumberFormatException exception) {
            System.out.println("User has entered an invalid int");
            instruction.setText("Please enter a valid number between 1 and 12.");
        }
        return -1;
    }

    public JButton getCheatMode() {
        return cheatMode;
    }

    public JButton getYes() {
        return yes;
    }

    public JButton getNo() {
        return no;
    }

    public JButton getDice() {
        return dice;
    }
    //Updates the prices in the world.
    public void updateHotelPricesAndOwnershipRating(ModelTiles tile) {
        for (TextAreaTile rectangle : rectangles) {
            if (Objects.equals(rectangle.pieceName, tile.getPieceName())) {
                rectangle.setPriceAndStarRating(tile.getHotel().getHotelPrice(), tile.getHotel().getHotelLevel());
                rectangle.playerOwnership(tile.getHotel().getHotelOwner());
            }
        }
    }


    private static class TextAreaTile extends JTextArea {
        private final String pieceName;
        private String text;
        private String priceAndID;

        private TextAreaTile(int x, int y, int width, int height, String pieceName) {
            super();
            this.setBackground(Color.white);
            this.setLineWrap(true);
            this.setWrapStyleWord(true);
            this.setEditable(false);
            this.setOpaque(false);
            this.pieceName = pieceName;
            this.priceAndID = pieceName;
            this.text = priceAndID;
            this.setBounds(x, y, width, height);
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setFont(new Font("Arial", Font.PLAIN, 9));
        }

        private void setPriceAndStarRating(int price, int starRating) {
            StringBuilder stringBuilder = new StringBuilder(pieceName);
            stringBuilder.append("\nPrice £").append(price);
            if (starRating > 0)
                stringBuilder.append("\nRating: ").append(starRating);
            this.priceAndID = stringBuilder.toString();
            this.setText(this.priceAndID);
            repaint();
        }

        private void setPlayersOnPieces(ArrayList<ModelPlayer> players) {
            StringBuilder playersOnPiece = new StringBuilder(priceAndID);
            for (ModelPlayer p : players) {
                playersOnPiece.append("\n player: ").append(p.getPlayerID());
            }
            text = playersOnPiece.toString();
            this.setText(this.text);
            repaint();
        }

        private void playerOwnership(ModelPlayer player) {
            if (player.getPlayerID() == 1) {
                this.setForeground(new Color(0, 0, 245));
            } else {
                this.setForeground(new Color(255, 0, 0));
            }
        }
    }

}
