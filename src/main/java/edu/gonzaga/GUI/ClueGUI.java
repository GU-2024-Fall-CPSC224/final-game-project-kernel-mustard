package edu.gonzaga.GUI;
import edu.gonzaga.GUI.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClueGUI extends JFrame {
    private BoardPanel boardPanel;
    private AccusationPanel accusationPanel;
    private JSplitPane splitPane;
    private List<GUIPlayer> players = new ArrayList<>();
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private GUIPlayer currentPlayer;


    public List<GUIPlayer> getPlayers() {
        return players;
    }

    private int currentPlayerIndex = 0;
    
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Game solution member variables
    private Card solutionSuspect;
    private Card solutionWeapon;
    private Card solutionRoom;

    public Card getSolutionSuspect() {
        return solutionSuspect;
    }

    public Card getSolutionWeapon() {
        return solutionWeapon;
    }

    public Card getSolutionRoom() {
        return solutionRoom;
    }

    public void setCurrentPlayerIndex(int idx) {
        // wrap negative indices to the end
        if (idx < 0) {
            idx = players.size() - 1;
        }
        // clamp above to last player
        else if (idx >= players.size()) {
            idx = idx % players.size();
        }
        this.currentPlayerIndex = idx;

        // sync the currentPlayer reference
        this.currentPlayer = players.get(currentPlayerIndex);

        // if you have a method to highlight the new player on the board, call it:
        // highlightCurrentPlayerOnBoard();
    }

    // Game cards
    private List<Card> allCards = new ArrayList<>();
    /*
     * Constructor for our main game window
     * Creates frame, sets window properties, and initializes our board.
     */
    public ClueGUI() {
        setTitle("Clue Board Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);


        TitleScreenPanel titleScreen = new TitleScreenPanel();
        mainPanel.add(titleScreen, "Title");

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*
     * Game Setup
     */

    private class TitleScreenPanel extends JPanel {
        public TitleScreenPanel() {
            setLayout(new BorderLayout());

            URL logoUrl = getClass().getClassLoader().getResource("edu/gonzaga/GUI/CLUE_logo.png");
            ImageIcon logoIcon = new ImageIcon(logoUrl);
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(logoLabel, BorderLayout.CENTER);

            JButton startButton = new JButton("Start Game");
            startButton.setFont(new Font("Serif", Font.PLAIN, 36));
            startButton.addActionListener(e -> openPlayerSetup());
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(startButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private void showPassDeviceBuffer() {
        Window win = SwingUtilities.getWindowAncestor(this);
        final JDialog dialog = new JDialog(win, "Pass Device", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 200));
        dialog.setLayout(new BorderLayout());

        JLabel msg = new JLabel(
            "<html><div style='text-align:center; color:white;'>PASS TO NEXT PLAYER<br><br>"
          + "<span style='font-size:12px;'>(press SPACE to continue)</span>"
          + "</div></html>", SwingConstants.CENTER);
        msg.setFont(msg.getFont().deriveFont(24f));
        dialog.add(msg, BorderLayout.CENTER);

        JComponent content = (JComponent) dialog.getContentPane();
        content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
               .put(KeyStroke.getKeyStroke("SPACE"), "dismiss");
        content.getActionMap()
               .put("dismiss", new AbstractAction() {
                   public void actionPerformed(ActionEvent e) {
                       dialog.dispose();
                   }
               });

        dialog.setSize(win.getSize());
        dialog.setLocationRelativeTo(win);
        dialog.setVisible(true);
    }

    private void openPlayerSetup() {
        PlayerSetupPanel setupPanel = new PlayerSetupPanel();
        mainPanel.add(setupPanel, "Setup");
        cardLayout.show(mainPanel, "Setup");
    }

    private class PlayerSetupPanel extends JPanel {
        private List<JTextField> nameFields = new ArrayList<>();
        private List<JComboBox<String>> characterBoxes = new ArrayList<>();
        private JComboBox<Integer> playerCountBox;
        private JPanel inputPanel;
        private JButton startGameButton;

        public PlayerSetupPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(Color.DARK_GRAY);

            // --- Top Panel: player count selection ---
            JPanel topPanel = new JPanel();
            topPanel.setBackground(Color.DARK_GRAY);
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
            
            JLabel prompt = new JLabel("How many players? (2-6)", SwingConstants.CENTER);
            prompt.setFont(new Font("Serif", Font.BOLD, 28));
            prompt.setForeground(Color.WHITE);
            prompt.setAlignmentX(Component.CENTER_ALIGNMENT);

            playerCountBox = new JComboBox<>(new Integer[]{2, 3, 4, 5, 6});
            playerCountBox.setMaximumSize(new Dimension(100, 30));
            playerCountBox.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton confirmButton = new JButton("Confirm");
            confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            confirmButton.addActionListener(e -> createPlayerInputs());

            topPanel.add(Box.createVerticalStrut(20));
            topPanel.add(prompt);
            topPanel.add(Box.createVerticalStrut(10));
            topPanel.add(playerCountBox);
            topPanel.add(Box.createVerticalStrut(10));
            topPanel.add(confirmButton);
            topPanel.add(Box.createVerticalStrut(10));

            add(topPanel, BorderLayout.NORTH);

            // --- Middle Panel: Player Inputs ---
            inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setBackground(Color.GRAY);
            JScrollPane scrollPane = new JScrollPane(inputPanel);
            add(scrollPane, BorderLayout.CENTER);

            // --- Bottom Panel: Start Game button ---
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.DARK_GRAY);
            startGameButton = new JButton("Start Game");
            startGameButton.setEnabled(false);
            startGameButton.setFont(new Font("Serif", Font.BOLD, 24));
            startGameButton.addActionListener(e -> finishSetup());
            bottomPanel.add(startGameButton);

            add(bottomPanel, BorderLayout.SOUTH);
        }

        private void createPlayerInputs() {
            inputPanel.removeAll();
            nameFields.clear();
            characterBoxes.clear();

            int numPlayers = (int) playerCountBox.getSelectedItem();

            List<String> characters = new ClueConstants().getSuspects();
            String[] characterArray = characters.toArray(new String[0]);

            for (int i = 0; i < numPlayers; i++) {
                JPanel row = new JPanel();
                row.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
                row.setBackground(Color.GRAY);

                JTextField nameField = new JTextField(10);
                JComboBox<String> characterBox = new JComboBox<>(characterArray);

                nameFields.add(nameField);
                characterBoxes.add(characterBox);

                row.add(new JLabel("Player " + (i + 1) + " Name:"));
                row.add(nameField);
                row.add(new JLabel("Character:"));
                row.add(characterBox);

                inputPanel.add(row);
            }

            startGameButton.setEnabled(true);
            revalidate();
            repaint();
        }

        private void finishSetup() {
            Set<String> usedNames = new HashSet<>();
            Set<String> usedCharacters = new HashSet<>();
            players.clear();

            boolean invalidInput = false;

            for (int i = 0; i < nameFields.size(); i++) {
                String name = nameFields.get(i).getText().trim();
                String character = (String) characterBoxes.get(i).getSelectedItem();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Player " + (i + 1) + " must enter a name.");
                    invalidInput = true;
                    break;
                }

                if (usedNames.contains(name)) {
                    JOptionPane.showMessageDialog(this, "Duplicate player names are not allowed.");
                    invalidInput = true;
                    break;
                }
                if (usedCharacters.contains(character)) {
                    JOptionPane.showMessageDialog(this, "Duplicate characters are not allowed.");
                    invalidInput = true;
                    break;
                }

                usedNames.add(name);
                usedCharacters.add(character);

                Point startPos = ClueConstants.getCharacterStartPositions().get(character);
                Color color = ClueConstants.getCharacterColors().get(character);
                players.add(new GUIPlayer(name, character, color, startPos.y, startPos.x));
            }

            if (!invalidInput && players.size() >= 2) {
                initializeGame();
            }
        }
    }

    private void initializeGame() {
        initializeCards();

        accusationPanel = new AccusationPanel(this);
        boardPanel = new BoardPanel(this, accusationPanel);
        accusationPanel.setBoardPanel(boardPanel);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, accusationPanel);
        splitPane.setResizeWeight(0.8);
        
        mainPanel.add(splitPane, "Game");
        cardLayout.show(mainPanel, "Game");

        boardPanel.setFocusable(true);
        boardPanel.requestFocusInWindow();

        if (!players.isEmpty()) {
            accusationPanel.addToGameLog("Game started! " + players.get(currentPlayerIndex).getName() +
                    " (" + players.get(currentPlayerIndex).getCharacter() + ") goes first.");
            accusationPanel.addToGameLog("The murder envelope has been set. Correctly identify the suspect, weapon, and room!");
        }
    }

    /*
     * Initialize the cards and create the game solution
     */
    private void initializeCards() {
        ClueConstants constants = new ClueConstants();
        List<String> suspects = constants.getSuspects();
        List<String> weapons = constants.getWeapons();
        List<String> rooms = constants.getRooms();

        allCards.clear();

        for (String suspect : suspects) {
            allCards.add(new Card(suspect, Card.CardType.SUSPECT));
        }
        for (String weapon : weapons) {
            allCards.add(new Card(weapon, Card.CardType.WEAPON));
        }
        for (String room : rooms) {
            allCards.add(new Card(room, Card.CardType.ROOM));
        }

        Collections.shuffle(allCards);

        List<Card> suspectCards = new ArrayList<>();
        List<Card> weaponCards = new ArrayList<>();
        List<Card> roomCards = new ArrayList<>();

        for (Card card : allCards) {
            if (card.getType() == Card.CardType.SUSPECT) suspectCards.add(card);
            if (card.getType() == Card.CardType.WEAPON) weaponCards.add(card);
            if (card.getType() == Card.CardType.ROOM) roomCards.add(card);
        }

        Random random = new Random();
        solutionSuspect = suspectCards.remove(random.nextInt(suspectCards.size()));
        solutionWeapon = weaponCards.remove(random.nextInt(weaponCards.size()));
        solutionRoom = roomCards.remove(random.nextInt(roomCards.size()));

        allCards.removeAll(Arrays.asList(solutionSuspect, solutionWeapon, solutionRoom));
        Collections.shuffle(allCards);

        int currentPlayer = 0;
        for (Card card : allCards) {
            players.get(currentPlayer).addCard(card);
            currentPlayer = (currentPlayer + 1) % players.size();
        }
    }

    
    /*
     * Get the current player whose turn it is
     */
    public GUIPlayer getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }
    
    /*
     * Advance to the next player's turn
     */
    public void nextPlayerTurn() {
        currentPlayerIndex = (getCurrentPlayerIndex() + 1) % players.size();
        accusationPanel.addToGameLog("\n" + players.get(currentPlayerIndex).getName() + 
                                  " (" + players.get(currentPlayerIndex).getCharacter() + 
                                  ") it's your turn.");
    }
    
    /*
     * Here is our main entry into the program.
     * This creates a new instance of ClueGUI on the Event Dispatch Thread (Basically means a thread that runs in an infinite loop, processing events as we play)
     * 
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClueGUI());
    } 

}
