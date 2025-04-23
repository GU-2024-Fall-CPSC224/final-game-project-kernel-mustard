package edu.gonzaga;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class ClueGUI extends JFrame {
    private BoardPanel boardPanel;
    private AccusationPanel accusationPanel;
    private JSplitPane splitPane;
    private List<GUIPlayer> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    // Map of starting positions for each character
    private static final Map<String, Point> CHARACTER_START_POSITIONS = new HashMap<>();
    static {
        CHARACTER_START_POSITIONS.put("Colonel Mustard", new Point(0, 13)); // Yellow, starts near Lounge
        CHARACTER_START_POSITIONS.put("Professor Plum", new Point(18, 14)); // Purple, starts near Study
        CHARACTER_START_POSITIONS.put("Miss Scarlet", new Point(5, 0)); // Red, starts near Kitchen
        CHARACTER_START_POSITIONS.put("Reverend Green", new Point(18, 4)); // Green, starts near Conservatory
        CHARACTER_START_POSITIONS.put("Mrs. White", new Point(4, 17)); // White, starts near Lounge
        CHARACTER_START_POSITIONS.put("Mrs. Peacock", new Point(12, 17)); // Blue, starts near Library
    }
    
    // Map character names to colors
    private static final Map<String, Color> CHARACTER_COLORS = new HashMap<>();
    static {
        CHARACTER_COLORS.put("Colonel Mustard", new Color(255, 215, 0));
        CHARACTER_COLORS.put("Professor Plum", new Color(128, 0, 128));
        CHARACTER_COLORS.put("Miss Scarlet", new Color(220, 20, 60));
        CHARACTER_COLORS.put("Reverend Green", new Color(34, 139, 34));
        CHARACTER_COLORS.put("Mrs. White", Color.WHITE);
        CHARACTER_COLORS.put("Mrs. Peacock", new Color(65, 105, 225));
    }
    
    /*
     * Constructor for our main game window
     * Creates frame, sets window properties, and initializes our board.
     */
    public ClueGUI() {
        setTitle("Clue Board Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        // Calls player setup windows before initializing game
        setupPlayers();
        
        boardPanel = new BoardPanel();
        accusationPanel = new AccusationPanel();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, accusationPanel);
        splitPane.setResizeWeight(0.8);
        // splitPane.setDebugGraphicsOptions(800);
        add(splitPane);
        
        boardPanel.setFocusable(true);
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        boardPanel.requestFocusInWindow();

        if(!players.isEmpty()) {
            accusationPanel.addToGameLog("Game started! " + players.get(currentPlayerIndex).getName() + 
                                        " (" + players.get(currentPlayerIndex).getCharacter() + ") goes first.");
        }
    }

        /*
     * Dialog to set up players at the start of the game
     */
    
    private void setupPlayers() {
        // List of available characters
        String[] characters = {"Colonel Mustard", "Professor Plum", "Miss Scarlet",
                             "Reverend Green", "Mrs. White", "Mrs. Peacock"};

        // Show dialog to get number of players
        String input = JOptionPane.showInputDialog(this, "Enter number of players (2-6):", "Player Setup", JOptionPane.QUESTION_MESSAGE);

        // Validate input
        int numPlayers = 3; // Default
        try {
            numPlayers = Integer.parseInt(input);
            if (numPlayers < 2) numPlayers = 2;
            if (numPlayers > 6) numPlayers = 6;
        } catch (NumberFormatException e) {
            // Default to 3 if input is invalid
            JOptionPane.showMessageDialog(this,
                    "Invalid input. Defaulting to 3 players.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

        while (true) {
            // Create player setup panel
            JPanel playerSetupPanel = new JPanel(new GridLayout(numPlayers + 1, 2, 10, 10));
            JLabel[] nameLabels = new JLabel[numPlayers];
            JTextField[] nameFields = new JTextField[numPlayers];
            JComboBox<String>[] characterBoxes = new JComboBox[numPlayers];

            // Add header
            playerSetupPanel.add(new JLabel("Player Name:"));
            playerSetupPanel.add(new JLabel("Character:"));

            for (int i = 0; i < numPlayers; i++) {
                nameLabels[i] = new JLabel("Player " + (i + 1) + ":");
                nameFields[i] = new JTextField("Player " + (i + 1), 15);
                characterBoxes[i] = new JComboBox<>(characters);
                characterBoxes[i].setSelectedIndex(i % characters.length);

                playerSetupPanel.add(nameFields[i]);
                playerSetupPanel.add(characterBoxes[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, playerSetupPanel,
                    "Enter Player Details",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Set<String> usedNames = new HashSet<>();
                Set<String> usedCharacters = new HashSet<>();
                boolean hasDuplicates = false;

                for (int i = 0; i < numPlayers; i++) {
                    String name = nameFields[i].getText().trim();
                    if (name.isEmpty()) name = "Player " + (i + 1);

                    String character = (String) characterBoxes[i].getSelectedItem();

                    if (usedNames.contains(name) || usedCharacters.contains(character)) {
                        hasDuplicates = true;
                        break;
                    }

                    usedNames.add(name);
                    usedCharacters.add(character);
                }

                if (hasDuplicates) {
                    JOptionPane.showMessageDialog(this,
                            "Duplicate names or characters detected. Please enter unique values.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    continue; // Loop again
                }

                // No duplicates, finalize players
                for (int i = 0; i < numPlayers; i++) {
                    String name = nameFields[i].getText().trim();
                    if (name.isEmpty()) name = "Player " + (i + 1);
                    String character = (String) characterBoxes[i].getSelectedItem();

                    Point startPos = CHARACTER_START_POSITIONS.get(character);
                    Color color = CHARACTER_COLORS.get(character);

                    GUIPlayer player = new GUIPlayer(name, character, color, startPos.y, startPos.x);
                    players.add(player);
                }
                break;
            } else {
                // User hit cancel, fallback to defaults
                String[] defaultNames = {"Player 1", "Player 2", "Player 3"};
                String[] defaultChars = {"Colonel Mustard", "Professor Plum", "Miss Scarlet"};

                for (int i = 0; i < 3; i++) {
                    Point startPos = CHARACTER_START_POSITIONS.get(defaultChars[i]);
                    Color color = CHARACTER_COLORS.get(defaultChars[i]);
                    GUIPlayer player = new GUIPlayer(defaultNames[i], defaultChars[i], color, startPos.y, startPos.x);
                    players.add(player);
                }
                break;
            }
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
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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

    /*
     * Panel for making accusations with drop-down (combo boxes) menus for rooms, weapons, and suspects
     */
    private class AccusationPanel extends JPanel {
        private JComboBox<String> roomBox;
        private JComboBox<String> weaponBox;
        private JComboBox<String> suspectBox;
        private JButton accuseButton;
        private JButton suggestButton;
        private JButton endTurnButton;
        private JButton rollDiceButton;
        private JLabel diceResults;
        private JTextArea textLog;
        private JScrollPane logScrollPane;
        public int movesRemaining = 0;

        public AccusationPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(200, 180, 160));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Create panel for accusations
            JPanel accusePanel = new JPanel();
            accusePanel.setLayout(new GridLayout(11, 1, 5, 10));
            setBackground(new Color(200, 180, 160));

            // Room selection
            String[] rooms = {"Ballroom", "Conservatory", "Billiard Room", "Dining Room", "Hall", "Kitchen", "Library", "Lounge", "Study"};
            JLabel roomLabel = new JLabel("Room:");
            roomBox = new JComboBox<>(rooms);

            // Weapon selection
            String[] weapons = {"Rope", "Lead Pipe", "Knife", "Wrench", "Candlestick", "Revolver"};
            JLabel weaponLabel = new JLabel("Weapon:");
            weaponBox = new JComboBox<>(weapons);

            // Suspect selection
            String[] suspects = {"Colonel Mustard", "Professor Plum", "Miss Scarlet", "Reverend Green", "Mrs. White", "Mrs. Peacock"};
            JLabel suspectLabel = new JLabel("Suspect:");
            suspectBox = new JComboBox<>(suspects);

            // Add buttons
            accuseButton = new JButton("Make Accusation");
            suggestButton = new JButton("Make Suggestion");
            endTurnButton = new JButton("End Turn");
            rollDiceButton = new JButton("Roll Dice");
            diceResults = new JLabel("Moves Left: 0");

            // Setup actions for our buttons
            accuseButton.addActionListener(e -> {
                makeAccusation();
                boardPanel.requestFocusInWindow();
            });
            suggestButton.addActionListener(e -> {
                makeSuggestion();
                boardPanel.requestFocusInWindow();
            });
            endTurnButton.addActionListener(e -> {
                endTurn();
                boardPanel.requestFocusInWindow();
            });
            rollDiceButton.addActionListener(e -> {
                int diceRoll = (int) (Math.random() * 6) + 1;
                movesRemaining = diceRoll;
                diceResults.setText("Moves Left: " + movesRemaining);
                addToGameLog(getCurrentPlayer().getName() + " rolled a " + diceRoll + ".");
                boardPanel.requestFocusInWindow();
            });

            // Ensure buttons don't keep focus
            accuseButton.setFocusable(false);
            suggestButton.setFocusable(false);
            endTurnButton.setFocusable(false);
            rollDiceButton.setFocusable(false);

            // Add compononents to our panel
            accusePanel.add(new JLabel("Make an accusation:"));
            accusePanel.add(roomLabel);
            accusePanel.add(roomBox);
            accusePanel.add(suspectLabel);
            accusePanel.add(suspectBox);
            accusePanel.add(weaponLabel);
            accusePanel.add(weaponBox);
            accusePanel.add(accuseButton);
            accusePanel.add(suggestButton);
            accusePanel.add(endTurnButton);
            accusePanel.add(rollDiceButton);
            accusePanel.add(diceResults);

            // Setup a chat log
            textLog = new JTextArea(10, 20);
            textLog.setEditable(false);
            textLog.setFont(new Font("Times New Roman", Font.PLAIN, 12));
            textLog.setText("Game started. Use arrow keys to move.\n");
            logScrollPane = new JScrollPane(textLog);
            logScrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));

            add(accusePanel, BorderLayout.NORTH);
            add(logScrollPane, BorderLayout.CENTER);
        }

        /* 
         * Make an accusation with the selected options
         */
        private void makeAccusation() {
            GUIPlayer currentPlayer = getCurrentPlayer();
            if (currentPlayer == null) return;
            
            String room = (String) roomBox.getSelectedItem();
            String suspect = (String) suspectBox.getSelectedItem();
            String weapon = (String) weaponBox.getSelectedItem();
            
            String accusation = currentPlayer.getName() + " (" + currentPlayer.getCharacter() + ") accuses: " + 
                               suspect + " killed Mr. Boddy in the " + 
                               room + " with the " + weapon + ".";
            
            addToGameLog(accusation);
            
            // TODO: Add code to check if accusation is correct
            // This will need to connect to your game logic
            
            // Sample response - in real game this would check against solution
            addToGameLog("Checking accusation...");
        }
        
        /*
         * Make a suggestion with the selected options
         */
        private void makeSuggestion() {
            GUIPlayer currentPlayer = getCurrentPlayer();
            if (currentPlayer == null) return;
            
            if (!currentPlayer.isInRoom()) {
                addToGameLog("You must be in a room to make a suggestion!");
                return;
            }
            
            String room = currentPlayer.getCurrentRoom();
            String suspect = (String) suspectBox.getSelectedItem();
            String weapon = (String) weaponBox.getSelectedItem();
            
            String suggestion = currentPlayer.getName() + " (" + currentPlayer.getCharacter() + 
                              ") suggests: It was " + suspect + 
                              " in the " + room + " with the " + weapon + ".";
            
            addToGameLog(suggestion);
            
            // TODO: Add code to process suggestion
            // This would typically involve other players trying to disprove suggestion
            addToGameLog("Other players considering your suggestion...");
        }
        
        /*
         * End the current player's turn and move to the next player
         */
        private void endTurn() {
            movesRemaining = 0;
            diceResults.setText("Moves Left: 0");
            nextPlayerTurn();
            boardPanel.repaint();
        }
        
        /*
         * Add a message to the game log
         */
        public void addToGameLog(String message) {
            textLog.append(message + "\n");
            // Auto-scroll to bottom
            textLog.setCaretPosition(textLog.getDocument().getLength());
        }
    }
    
    private class BoardPanel extends JPanel {
        // Set room coordinates and sizes
        private Map<String, Rectangle> rooms = new HashMap<>();
        private Map<String, Color> roomColors = new HashMap<>();
        private Map<String, List<WallOpening>> wallOpenings = new HashMap<>();
        
        // Set grid size and player position
        private final int GRID_SIZE = 40;
        private final int ROWS = 18;
        private final int COLS = 19;
        
        // Movement statuses
        private boolean[][] validMovementTiles;
        
        /*
         * This inner class represents our doorways for entering/exiting a room.
         * Stores coords for both inside/outside the room, and which wall the doorway is on.
         */
        private class WallOpening {
            Point roomPoint; // This holds the point coordinate inside the room (for entering)
            Point hallwayPoint; // This holds the point coordinate in the hallway (for exiting)
            int wallSide; // 0=top, 1=right, 2=bottom, 3=left
            
            public WallOpening(Point roomPoint, Point hallwayPoint, int wallSide) {
                this.roomPoint = roomPoint;
                this.hallwayPoint = hallwayPoint;
                this.wallSide = wallSide;
            }
        }
        
        /*
         * Constructor for our game baord panel.
         * Initializes rooms, doorways, movement grid, and sets up keyboard input
         */
        public BoardPanel() {
            // Initialize rooms
            setupRooms();
            
            // Initialize wall openings
            setupWallOpenings();
            
            // Create valid movement grid
            setupValidMovementTiles();
            
            // Add key listener for player movement
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    movePlayer(e.getKeyCode());
                }
            });
        }
        
        /*
         * The following function is lengthy. Essentially just sets up each of our doorways for all the rooms.
         * I would definitely reccommend minimizing this function so you don't have to scroll past this every time. 
         */
        private void setupWallOpenings() {
            // Create wall openings for each room
            wallOpenings.put("Kitchen", new ArrayList<>());
            wallOpenings.get("Kitchen").add(new WallOpening(
                new Point(3, 3), // Inside point
                new Point(3, 4), // Outside point
                2 // Right wall
            ));
            
            wallOpenings.put("Ballroom", new ArrayList<>());
            wallOpenings.get("Ballroom").add(new WallOpening(
                new Point(9, 4),  // Inside point
                new Point(9, 5),  // Outside point
                2 // Bottom wall
            ));
            wallOpenings.get("Ballroom").add(new WallOpening(
                new Point(11, 4),  // Inside point
                new Point(11, 5),  // Outside point
                2 // Bottom wall
            ));
            
            wallOpenings.put("Conservatory", new ArrayList<>());
            wallOpenings.get("Conservatory").add(new WallOpening(
                new Point(15, 2),  // Inside point
                new Point(14, 2),  // Outside point
                3 // Left wall
            ));
            
            wallOpenings.put("Dining Room", new ArrayList<>());
            wallOpenings.get("Dining Room").add(new WallOpening(
                new Point(5, 9),  // Inside point
                new Point(6, 9),  // Outside point
                1 // Right wall
            ));
            wallOpenings.get("Dining Room").add(new WallOpening(
                new Point(3, 7),  // Inside point
                new Point(3, 6),  // Outside point
                0 // Top wall
            ));
            wallOpenings.get("Dining Room").add(new WallOpening(
                new Point(6, 9),  // Inside point
                new Point(7, 8),  // Outside point
                0 // Top wall
            ));
            
            wallOpenings.put("Lounge", new ArrayList<>());
            wallOpenings.get("Lounge").add(new WallOpening(
                new Point(3, 15),  // Inside point
                new Point(4, 15),  // Outside point
                1 // Right wall
            ));
            
            wallOpenings.put("Hall", new ArrayList<>());
            wallOpenings.get("Hall").add(new WallOpening(
                new Point(7, 13),  // Inside point
                new Point(7, 12),  // Outside point
                0 // Top wall
            ));
            wallOpenings.get("Hall").add(new WallOpening(
                new Point(11, 13),  // Inside point
                new Point(11, 12),  // Outside point
                0 // Top wall
            ));
            
            wallOpenings.put("Study", new ArrayList<>());
            wallOpenings.get("Study").add(new WallOpening(
                new Point(16, 15),  // Inside point
                new Point(16, 14),  // Outside point
                0 // Top wall
            ));
            
            wallOpenings.put("Library", new ArrayList<>());
            wallOpenings.get("Library").add(new WallOpening(
                new Point(15, 10),  // Inside point
                new Point(14, 10),  // Outside point
                3 // Left wall
            ));
            wallOpenings.get("Library").add(new WallOpening(
                new Point(17, 12),  // Inside point
                new Point(17, 13),  // Outside point
                2 // Bottom wall
            ));
            
            wallOpenings.put("Billiard Room", new ArrayList<>());
            wallOpenings.get("Billiard Room").add(new WallOpening(
                new Point(14, 6),  // Inside point
                new Point(13, 6),  // Outside point
                3 // Left wall
            ));
            wallOpenings.get("Billiard Room").add(new WallOpening(
                new Point(17, 6),  // Inside point
                new Point(17, 7),  // Outside point
                2 // Bottom wall
            ));
            wallOpenings.put("CLUE", new ArrayList<>());
        }
        
        /*
         * Creates a boolean grid that tracks which tiles are valid for player to move in.
         * Marks hallways as valid and rooms and invalid for direct movement.
         * TODO: Need to make it so a user from inside a room can exit from either doorway if there is more than one.
         * Currently a user can only exit from the door they entered from.
         */
        private void setupValidMovementTiles() {
            validMovementTiles = new boolean[ROWS][COLS];
            
            // Initially we will mark all tiles as valid
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    validMovementTiles[row][col] = true;
                }
            }
            
            // Then we mark tiles in a room as invalid for movement
            for (Rectangle room : rooms.values()) {
                int startRow = room.y / GRID_SIZE;
                int startCol = room.x / GRID_SIZE;
                int endRow = (room.y + room.height) / GRID_SIZE;
                int endCol = (room.x + room.width) / GRID_SIZE;
                
                for (int row = startRow; row < endRow; row++) {
                    for (int col = startCol; col < endCol; col++) {
                        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                            validMovementTiles[row][col] = false;
                        }
                    }
                }
            }
            
            // Add doorway points as valid movement tiles
            for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
                for (WallOpening opening : entry.getValue()) {
                    validMovementTiles[opening.hallwayPoint.y][opening.hallwayPoint.x] = true;
                }
            }
        }
        
        /*
         * Handles player movement based upon keyboard input
         */
        private void movePlayer(int keyCode) {
            GUIPlayer currentPlayer = getCurrentPlayer();
            if (currentPlayer == null) return;

            if (accusationPanel.movesRemaining <= 0 && keyCode != KeyEvent.VK_ENTER) {
                accusationPanel.addToGameLog("You have no moves left. Roll the dice to continue.");
                return;
            }
            
            int newRow = currentPlayer.getRow();
            int newCol = currentPlayer.getCol();
            
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    newRow--;
                    break;
                case KeyEvent.VK_DOWN:
                    newRow++;
                    break;
                case KeyEvent.VK_LEFT:
                    newCol--;
                    break;
                case KeyEvent.VK_RIGHT:
                    newCol++;
                    break;
                case KeyEvent.VK_ENTER:
                    // Handle room entry/exit with 'Enter' key
                    handleRoomEntryExit(currentPlayer);
                    return;
                default:
                    return; // Ignore other keys
            }
            
            // Check if player is in a room
            if (currentPlayer.isInRoom()) {
                // If in room, can only move to wall opening hallway points
                boolean isExit = false;
                for (WallOpening opening : wallOpenings.get(currentPlayer.getCurrentRoom())) {
                    if (opening.hallwayPoint.y == newRow && opening.hallwayPoint.x == newCol) {
                        isExit = true;
                        break;
                    }
                }
                
                if (isExit) {
                    currentPlayer.setRow(newRow);
                    currentPlayer.setCol(newCol);
                    currentPlayer.setInRoom(false);
                    currentPlayer.setCurrentRoom(null);
                    repaint();
                }
            } 
            // Player is in hallway
            else {
                // Check if new position is valid for movement
                if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && 
                    validMovementTiles[newRow][newCol]) {
                    
                    // Check if another player is already on this tile
                    boolean tileOccupied = false;
                    for (GUIPlayer p : players) {
                        if (p != currentPlayer && p.getRow() == newRow && p.getCol() == newCol && !p.isInRoom()) {
                            tileOccupied = true;
                            break;
                        }
                    }
                    
                    if (!tileOccupied) {
                        currentPlayer.setRow(newRow);
                        currentPlayer.setCol(newCol);

                        accusationPanel.movesRemaining--;
                        accusationPanel.diceResults.setText("Moves Left: " + accusationPanel.movesRemaining);
                        
                        // Check if player moved to a wall opening
                        checkIfAtWallOpening(currentPlayer);
                        
                        repaint();
                    }
                }
            }
        }
        
        /*
         * This function manages entrance and exit to/from rooms when a player presses the 'Enter' key
         */
        private void handleRoomEntryExit(GUIPlayer player) {
            if (!player.isInRoom()) {
                // Check if player is standing next to a wall opening
                for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
                    String roomName = entry.getKey();
                    List<WallOpening> openings = entry.getValue();
                    
                    for (WallOpening opening : openings) {
                        if (opening.hallwayPoint.y == player.getRow() && opening.hallwayPoint.x == player.getCol()) {
                            // Enter room
                            player.setInRoom(true);
                            player.setCurrentRoom(roomName);
                            
                            // Move player inside room
                            player.setRow(opening.roomPoint.y);
                            player.setCol(opening.roomPoint.x);

                            accusationPanel.addToGameLog(player.getName() + " entered the " + roomName);
                            
                            repaint();
                            return;
                        }
                    }
                }
            }
        }
        
        /*
         * Checks if a user is standing next to a doorway. We call this after each time the player moves and just use it
         * to tell the user they can push 'enter' to go into a room.
         */
        private void checkIfAtWallOpening(GUIPlayer player) {
            // Check if player is standing at a wall opening
            for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
                for (WallOpening opening : entry.getValue()) {
                    if (opening.hallwayPoint.y == player.getRow() && opening.hallwayPoint.x == player.getCol()) {
                        repaint();
                        return;
                    }
                }
            }
        }
        
        /*
         * Initializes game rooms with their specific positions, colors, and sizes.
         */
        private void setupRooms() {
            // Set room rectangles (x, y, width, height)
            rooms.put("Kitchen", new Rectangle(0, 0, 200, 160));
            rooms.put("Ballroom", new Rectangle(280, 0, 200, 200));
            rooms.put("Conservatory", new Rectangle(600, 0, 160, 160));
            rooms.put("Dining Room", new Rectangle(0, 280, 240, 160));
            rooms.put("Hall", new Rectangle(280, 520, 200, 200));
            rooms.put("Lounge", new Rectangle(0, 560, 160, 160));
            rooms.put("Library", new Rectangle(600, 360, 160, 160));
            rooms.put("Study", new Rectangle(560, 600, 200, 120));
            rooms.put("Billiard Room", new Rectangle(560, 200, 200, 80));
            rooms.put("CLUE", new Rectangle(320, 280, 120, 160));
            
            // Set room colors
            roomColors.put("Kitchen", new Color(220, 220, 220));
            roomColors.put("Ballroom", new Color(235, 199, 158));
            roomColors.put("Conservatory", new Color(180, 220, 180));
            roomColors.put("Dining Room", new Color(220, 180, 160));
            roomColors.put("Hall", new Color(200, 200, 220));
            roomColors.put("Lounge", new Color(200, 180, 160));
            roomColors.put("Library", new Color(220, 180, 180));
            roomColors.put("Study", new Color(180, 200, 220));
            roomColors.put("Billiard Room", new Color(180, 220, 220));
            roomColors.put("CLUE", new Color(230, 230, 230));
        }
        
        /*
         * This is the main drawing method that renders our game board
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Draw the grid
            drawGrid(g2d);
            
            // Draw rooms with wall openings
            drawRoomsWithOpenings(g2d);
            
            // Draw player token
            drawPlayers(g2d);
            
            // Draw status information
            drawStatus(g2d);
        }
        
        /*
         * Draws the background grid for the game board
         */
        private void drawGrid(Graphics2D g2d) {
            // Fill background
            g2d.setColor(new Color(200, 150, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw the checkered pattern for the hallways
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    int x = col * GRID_SIZE;
                    int y = row * GRID_SIZE;
                    
                    if (validMovementTiles[row][col]) {
                    
                        g2d.setColor(new Color(190, 180, 180));
                        g2d.fillRect(x, y, GRID_SIZE, GRID_SIZE);
                        
                        // Grid lines
                        g2d.setColor(Color.BLACK);
                        g2d.drawRect(x, y, GRID_SIZE, GRID_SIZE);
                    }
                }
            }
        }
        
        /*
         * This draws all the rooms on the game board, including their walls, doorways, and labels.
         */
        private void drawRoomsWithOpenings(Graphics2D g2d) {
            // First draw room interiors
            for (Map.Entry<String, Rectangle> room : rooms.entrySet()) {
                String roomName = room.getKey();
                Rectangle rect = room.getValue();
                
                // Fill room
                g2d.setColor(roomColors.get(roomName));
                g2d.fill(rect);
            }
            
            // Then draw room walls w/ doorways
            for (Map.Entry<String, Rectangle> room : rooms.entrySet()) {
                String roomName = room.getKey();
                Rectangle rect = room.getValue();
                List<WallOpening> openings = wallOpenings.get(roomName);
                
                // Draw each wall segment, skipping doorways
                drawWallWithOpenings(g2d, rect, openings, roomName);
                
                // Draw room name
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Times New Roman", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(roomName);
                int textHeight = fm.getHeight();
                
                int x = rect.x + (rect.width - textWidth) / 2;
                int y = rect.y + (rect.height - textHeight) / 2 + fm.getAscent();
                
                // Draw text
                g2d.drawString(roomName, x, y);
            }
        }
        
        /*
         * Draws the walls for a specific room, with gaps for our doorways
         */
        private void drawWallWithOpenings(Graphics2D g2d, Rectangle rect, List<WallOpening> openings, String roomName) {
            g2d.setColor(Color.ORANGE);
            g2d.setStroke(new BasicStroke(3));
            
            int roomStartRow = rect.y / GRID_SIZE;
            int roomStartCol = rect.x / GRID_SIZE;
            int roomEndRow = (rect.y + rect.height) / GRID_SIZE;
            int roomEndCol = (rect.x + rect.width) / GRID_SIZE;
            
            // Draw top wall
            for (int col = roomStartCol; col < roomEndCol; col++) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check if this is where the opening should be on the top wall
                    if (opening.wallSide == 0 && opening.roomPoint.x == col) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    g2d.drawLine(col * GRID_SIZE, roomStartRow * GRID_SIZE, 
                                (col + 1) * GRID_SIZE, roomStartRow * GRID_SIZE);
                }
            }
            
            // Draw right wall
            for (int row = roomStartRow; row < roomEndRow; row++) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check if this is where the opening should be on the right wall
                    if (opening.wallSide == 1 && opening.roomPoint.y == row) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    g2d.drawLine(roomEndCol * GRID_SIZE, row * GRID_SIZE, 
                                roomEndCol * GRID_SIZE, (row + 1) * GRID_SIZE);
                }
            }
            
            // Draw bottom wall
            for (int col = roomStartCol; col < roomEndCol; col++) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check if this is where the opening should be on the bottom wall
                    if (opening.wallSide == 2 && opening.roomPoint.x == col) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    g2d.drawLine(col * GRID_SIZE, roomEndRow * GRID_SIZE, 
                                (col + 1) * GRID_SIZE, roomEndRow * GRID_SIZE);
                }
            }
            
            // Draw left wall
            for (int row = roomStartRow; row < roomEndRow; row++) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check if this is where the opening should be on the left wall
                    if (opening.wallSide == 3 && opening.roomPoint.y == row) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    g2d.drawLine(roomStartCol * GRID_SIZE, row * GRID_SIZE, 
                                roomStartCol * GRID_SIZE, (row + 1) * GRID_SIZE);
                }
            }
        }
        
        /*
         * Draws all player tokens on the board
         */
        private void drawPlayers(Graphics2D g2d) {
            // Draw each player token
            for (GUIPlayer player : players) {
                // Get player position
                int playerX, playerY;
                
                if (player.isInRoom()) {
                    // If player is in a room, calculate position within room
                    playerX = player.getCol() * GRID_SIZE;
                    playerY = player.getRow() * GRID_SIZE;
                } else {
                    // If player is in hallway, use grid coordinates
                    playerX = player.getCol() * GRID_SIZE;
                    playerY = player.getRow() * GRID_SIZE;
                }
                
                // Draw player token
                g2d.setColor(player.getColor());
                g2d.fillOval(playerX + 5, playerY + 5, GRID_SIZE - 10, GRID_SIZE - 10);
                
                // Draw token outline
                g2d.setColor(Color.BLACK);
                g2d.drawOval(playerX + 5, playerY + 5, GRID_SIZE - 10, GRID_SIZE - 10);
                
                // Draw first letter of character name
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.setColor(Color.BLACK);
                String initial = player.getCharacter().substring(0, 1);
                FontMetrics metrics = g2d.getFontMetrics();
                int textWidth = metrics.stringWidth(initial);
                int textHeight = metrics.getHeight();
                g2d.drawString(initial, playerX + (GRID_SIZE - textWidth) / 2, 
                              playerY + (GRID_SIZE + textHeight) / 2 - 2);
                
                // Highlight current player so it's easier to tell who's turn it is
                if (player == getCurrentPlayer()) {
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
                    g2d.drawOval(playerX + 2, playerY + 2, GRID_SIZE - 4, GRID_SIZE - 4);
                    g2d.setStroke(new BasicStroke(1));
                }
            }
        }
        
       /*
         * Draws game status information on the board
         */
        private void drawStatus(Graphics2D g2d) {
            GUIPlayer currentPlayer = getCurrentPlayer();
            if (currentPlayer == null) return;
            
            // Draw current player info
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getCharacter() + ")", 10, getHeight() - 40);
            
            // Draw player position info
            String posInfo;
            if (currentPlayer.isInRoom()) {
                posInfo = "In the " + currentPlayer.getCurrentRoom();
            } else {
                // Check if player is at doorway
                String doorwayInfo = "";
                for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
                    for (WallOpening opening : entry.getValue()) {
                        if (opening.hallwayPoint.y == currentPlayer.getRow() && 
                            opening.hallwayPoint.x == currentPlayer.getCol()) {
                            doorwayInfo = " - Press Enter to enter " + entry.getKey();
                            break;
                        }
                    }
                    if (!doorwayInfo.isEmpty()) break;
                }
                posInfo = "In hallway" + doorwayInfo;
            }
            g2d.drawString(posInfo, 10, getHeight() - 20);
            
            // Draw navigation instructions
            g2d.drawString("Use arrow keys to move, Enter to enter/exit rooms", getWidth() - 400, getHeight() - 20);
        }
    }
}

