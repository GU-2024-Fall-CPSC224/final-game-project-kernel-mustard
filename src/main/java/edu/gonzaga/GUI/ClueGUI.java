package edu.gonzaga.GUI;
import edu.gonzaga.GUI.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
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

    

    // Game cards
    private List<Card> allCards = new ArrayList<>();
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

        // Initializes cards and game solution
        initializeCards();
        
        accusationPanel = new AccusationPanel();
        boardPanel = new BoardPanel(this, accusationPanel);
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, accusationPanel);
        splitPane.setResizeWeight(0.8);
        add(splitPane);
        
        boardPanel.setFocusable(true);
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        boardPanel.requestFocusInWindow();

        if(!players.isEmpty()) {
            accusationPanel.addToGameLog("Game started! " + players.get(currentPlayerIndex).getName() + 
                                        " (" + players.get(currentPlayerIndex).getCharacter() + ") goes first.");
            accusationPanel.addToGameLog("The murder envelope has been set. Correctly identify the suspect, weapon, and room!");
        }
    }

    /*
     * Initialize the cards and create the game solution
     */
    private void initializeCards() {
        // Get references to the constants
        ClueConstants constants = new ClueConstants();
        List<String> suspects = constants.getSuspects();
        List<String> weapons = constants.getWeapons();
        List<String> rooms = constants.getRooms();
        
        // Initialize allCards as a new ArrayList
        allCards = new ArrayList<>();
        
        // Create all cards
        for (String suspect : suspects) {
            allCards.add(new Card(suspect, Card.CardType.SUSPECT));
        }
        
        for (String weapon : weapons) {
            allCards.add(new Card(weapon, Card.CardType.WEAPON));
        }
        
        for (String room : rooms) {
            allCards.add(new Card(room, Card.CardType.ROOM));
        }
        
        // Shuffle cards randomly
        Collections.shuffle(allCards);
        
        // Create the solution by selecting one card of each type
        List<Card> suspectCards = new ArrayList<>();
        List<Card> weaponCards = new ArrayList<>();
        List<Card> roomCards = new ArrayList<>();
        
        for (Card card : allCards) {
            if (card.getType() == Card.CardType.SUSPECT) {
                suspectCards.add(card);
            } else if (card.getType() == Card.CardType.WEAPON) {
                weaponCards.add(card);
            } else if (card.getType() == Card.CardType.ROOM) {
                roomCards.add(card);
            }
        }
        
        // Randomly select one card of each type for the solution
        Random random = new Random();
        solutionSuspect = suspectCards.remove(random.nextInt(suspectCards.size()));
        solutionWeapon = weaponCards.remove(random.nextInt(weaponCards.size()));
        solutionRoom = roomCards.remove(random.nextInt(roomCards.size()));
        
        // Remove solution cards from the deck so players don't get them
        allCards.remove(solutionSuspect);
        allCards.remove(solutionWeapon);
        allCards.remove(solutionRoom);
        
        // Shuffle each of the remaining cards
        Collections.shuffle(allCards);
        
        // Deal all the remaining cards to players
        int currentPlayer = 0;
        for (Card card : allCards) {
            players.get(currentPlayer).addCard(card);
            currentPlayer = (currentPlayer + 1) % players.size();
        }
    }

    /*
     * Dialog box to set up players at the start of the game
     */
    private void setupPlayers() {
        ClueConstants constants = new ClueConstants();
        List<String> characters = constants.getSuspects();
        
        // Convert List to array for JComboBox
        String[] characterArray = characters.toArray(new String[0]);
    
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
            List<JComboBox<String>> characterBoxes = new ArrayList<>();
    
            // Add header
            playerSetupPanel.add(new JLabel("Player Name:"));
            playerSetupPanel.add(new JLabel("Character:"));
    
            for (int i = 0; i < numPlayers; i++) {
                nameLabels[i] = new JLabel("Player " + (i + 1) + ":");
                nameFields[i] = new JTextField("Player " + (i + 1), 15);
                JComboBox<String> comboBox = new JComboBox<>(characterArray);
                comboBox.setSelectedIndex(i % characterArray.length);
                characterBoxes.add(comboBox);
    
                playerSetupPanel.add(nameFields[i]);
                playerSetupPanel.add(comboBox);
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
                    String character = (String) characterBoxes.get(i).getSelectedItem();
    
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
                    String character = (String) characterBoxes.get(i).getSelectedItem();
    
                    Point startPos = ClueConstants.getCharacterStartPositions().get(character);
                    Color color = ClueConstants.getCharacterColors().get(character);
    
                    GUIPlayer player = new GUIPlayer(name, character, color, startPos.y, startPos.x);
                    players.add(player);
                }
                break;
            } else {
                // User hit cancel, fallback to defaults
                String[] defaultNames = {"Player 1", "Player 2", "Player 3"};
                String[] defaultChars = characterArray.length >= 3 ? 
                                        new String[]{characterArray[0], characterArray[1], characterArray[2]} :
                                        new String[]{"Colonel Mustard", "Professor Plum", "Miss Scarlet"};
    
                for (int i = 0; i < 3; i++) {
                    Point startPos = ClueConstants.getCharacterStartPositions().get(defaultChars[i]);
                    Color color = ClueConstants.getCharacterColors().get(defaultChars[i]);
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

}