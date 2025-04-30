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

public class AccusationPanel extends JPanel{
    private JComboBox<String> weaponBox;
    private JComboBox<String> suspectBox;
    private JButton accuseButton;
    private JButton suggestButton;
    private JButton endTurnButton;
    private JButton rollDiceButton;
    private JButton showCardsButton;
    private JLabel diceResults;
    private BoardPanel boardPanel;
    private ClueGUI clueGUI;
    private boolean gameOver = false;
    private GUIPlayer winner = null;
    private JTextArea textLog;
    private JScrollPane logScrollPane;
    public int movesRemaining = 0;
    private boolean hasRolledThisTurn = false;
    private boolean hasMadeSuggestionThisTurn = false;

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public boolean isHasRolledThisTurn() {
        return hasRolledThisTurn;
    }
    public JLabel getDiceResults() {
        return diceResults;
    }

    public AccusationPanel(ClueGUI clueGUI) {
        this.clueGUI = clueGUI;
        setLayout(new BorderLayout());
        setBackground(new Color(200, 180, 160));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create panel for accusations
        JPanel accusePanel = new JPanel();
        accusePanel.setLayout(new GridLayout(11, 1, 5, 10));
        setBackground(new Color(200, 180, 160));

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
        showCardsButton = new JButton("Show My Cards");

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
            rollDice();
            boardPanel.requestFocusInWindow();
        });
        showCardsButton.addActionListener(e -> {
            showPlayerCards();
            boardPanel.requestFocusInWindow();
        });

        // Ensure buttons don't keep focus
        accuseButton.setFocusable(false);
        suggestButton.setFocusable(false);
        endTurnButton.setFocusable(false);
        rollDiceButton.setFocusable(false);
        showCardsButton.setFocusable(false);

        // Add compononents to our panel
        accusePanel.add(new JLabel("Make an accusation:"));
        accusePanel.add(suspectLabel);
        accusePanel.add(suspectBox);
        accusePanel.add(weaponLabel);
        accusePanel.add(weaponBox);
        accusePanel.add(accuseButton);
        accusePanel.add(suggestButton);
        accusePanel.add(endTurnButton);
        accusePanel.add(rollDiceButton);
        accusePanel.add(diceResults);
        accusePanel.add(showCardsButton);

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
    * Show the current player's cards
    */
    private void showPlayerCards() {
        GUIPlayer currentPlayer = clueGUI.getCurrentPlayer();
        if (currentPlayer == null) return;
        
        if (gameOver) {
            addToGameLog("Game is over! No need to check cards.");
            return;
        }
        
        // Create a text area to show cards
        JTextArea cardsText = new JTextArea(20, 30);
        cardsText.setEditable(false);
        
        // Add the player's own cards
        cardsText.append("YOUR CARDS:\n" + currentPlayer.getCardList() + "\n\n");
        
        // Add cards shown by other players for easier tracking of information
        cardsText.append("CARDS SHOWN TO YOU:\n");
        boolean hasSeenCards = false;
        
        for (GUIPlayer otherPlayer : clueGUI.getPlayers()) {
            if (otherPlayer == currentPlayer) continue;
            
            // Get cards shown by this player if any
            Set<Card> shownCards = otherPlayer.getCardsShownToPlayers().getOrDefault(currentPlayer, new HashSet<>());
            
            if (!shownCards.isEmpty()) {
                hasSeenCards = true;
                cardsText.append("\nFrom " + otherPlayer.getName() + " (" + otherPlayer.getCharacter() + "):\n");
                
                for (Card card : shownCards) {
                    cardsText.append("- " + card.getName() + " (" + card.getType() + ")\n");
                }
            }
        }
        
        if (!hasSeenCards) {
            cardsText.append("No cards have been shown to you yet.\n");
        }
        
        JScrollPane scrollPane = new JScrollPane(cardsText);
        JOptionPane.showMessageDialog(this, scrollPane, 
                                     currentPlayer.getName() + "'s Cards", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }

    /* 
    * Make an accusation with the selected options by player
    */
    private void makeAccusation() {
        GUIPlayer currentPlayer = clueGUI.getCurrentPlayer();
        if (currentPlayer == null) return;
        
        if (gameOver) {
            addToGameLog("Game is already over!");
            return;
        }
        
        String room = currentPlayer.getCurrentRoom();
        String suspect = (String) suspectBox.getSelectedItem();
        String weapon = (String) weaponBox.getSelectedItem();
        
        // This line is for testing:
        // addToGameLog(("answer" + clueGUI.getSolutionSuspect().getName() + clueGUI.getSolutionWeapon().getName() + clueGUI.getSolutionRoom().getName()));

        // Ask for confirmation with player since accusation is final
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to make this accusation?\n" +
            "Suspect: " + suspect + "\n" +
            "Weapon: " + weapon + "\n" +
            "Room: " + room + "\n\n" +
            "If wrong, you will be eliminated from the game!",
            "Confirm Accusation", JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        String accusation = currentPlayer.getName() + " (" + currentPlayer.getCharacter() + ") accuses: " + 
                           suspect + " killed Mr. Boddy in the " + 
                           room + " with the " + weapon + ".";
        
        addToGameLog(accusation);
        addToGameLog("Checking accusation...");
        
        // Check if accusation is correct
        boolean isCorrect = clueGUI.getSolutionSuspect().getName().equals(suspect) &&
                           clueGUI.getSolutionWeapon().getName().equals(weapon) &&
                           clueGUI.getSolutionRoom().getName().equals(room);
        
        if (isCorrect) {
            addToGameLog("CORRECT! " + currentPlayer.getName() + " has won the game!");
            addToGameLog("The solution was: " + suspect + " in the " + room + " with the " + weapon + ".");
            
            // End the game
            gameOver = true;
            winner = currentPlayer;
            
            // Say our congratulations
            JOptionPane.showMessageDialog(this,
                "Congratulations! You've solved the case!\n\n" +
                "The solution was: " + suspect + " in the " + room + " with the " + weapon + ".",
                "You Win!", JOptionPane.INFORMATION_MESSAGE);

            // Add a small delay before closing game to ensure the dialog is fully closed
            SwingUtilities.invokeLater(() -> {
                try {
                    // Small delay to ensure dialog is closed
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Exit the application
                System.exit(0);
            });
        } else {
            addToGameLog("INCORRECT! " + currentPlayer.getName() + " is now out of the game!");
            
            eliminateCurrentPlayer(currentPlayer);

                // 2) KICK THEIR TOKEN OFF THE BOARD
                boardPanel.repaint();

                // 3) IMMEDIATE GAME OVER if only one left
                if (clueGUI.getPlayers().size() <= 1) {
                    GUIPlayer survivor = clueGUI.getPlayers().get(0);
                    addToGameLog(survivor.getName() + " is the last detective standing! Game Over.");
                    gameOver = true;
                    disableGameControls();
                    JOptionPane.showMessageDialog(this,
                        survivor.getName() + " wins— all others eliminated!",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    return;   // stop right here
                }
            // Check which part was wrong but don't reveal the solution
            if (!clueGUI.getSolutionSuspect().getName().equals(suspect)) {
                addToGameLog("The murderer was not " + suspect + ".");
            }
            if (!clueGUI.getSolutionWeapon().getName().equals(weapon)) {
                addToGameLog("The murder weapon was not the " + weapon + ".");
            }
            if (!clueGUI.getSolutionRoom().getName().equals(room)) {
                addToGameLog("The murder did not occur in the " + room + ".");
            }
            
            // Check if this was the last player
            if (clueGUI.getPlayers().size() <= 1) {
                addToGameLog("All players have been eliminated! Game Over!");
                addToGameLog("The solution was: " + clueGUI.getSolutionSuspect().getName() + " in the " + 
                clueGUI.getSolutionWeapon().getName() + " with the " + clueGUI.getSolutionRoom().getName() + ".");
                gameOver = true;
                
                JOptionPane.showMessageDialog(this,
                    "All players have been eliminated!\n\n" +
                    "The solution was: " + clueGUI.getSolutionSuspect().getName() + " in the " + 
                    clueGUI.getSolutionWeapon().getName() + " with the " + clueGUI.getSolutionRoom().getName() + ".",
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Proceed to next player
                endTurn();
            }
        }
    }
    
    /*
     * Make a suggestion with the selected options
     * Other players must try to disprove the suggestion
     */
    private void makeSuggestion() {
        GUIPlayer currentPlayer = clueGUI.getCurrentPlayer();
        if (currentPlayer == null) return;
        
        if (gameOver) {
            addToGameLog("Game is already over!");
            return;
        }
        
        if (!currentPlayer.isInRoom()) {
            addToGameLog("You must be in a room to make a suggestion!");
            return;
        }else{
            showPassDeviceBuffer();

        }
        
        if (hasMadeSuggestionThisTurn) {
            addToGameLog("You've already made a suggestion this turn!");
            return;
        }
        
        // Use current room for suggestion
        String room = currentPlayer.getCurrentRoom();
        String suspect = (String) suspectBox.getSelectedItem();
        String weapon = (String) weaponBox.getSelectedItem();
        
        String suggestion = currentPlayer.getName() + " (" + currentPlayer.getCharacter() + 
                          ") suggests: It was " + suspect + 
                          " in the " + room + " with the " + weapon + ".";
        
        addToGameLog(suggestion);
        addToGameLog("Other players considering your suggestion...");
        
        // Try to disprove the suggestion by checking if other players have any of the cards
        boolean disproven = false;
        for (int i = 0; i < clueGUI.getPlayers().size(); i++) {
            // Skip current player
            if (i == clueGUI.getCurrentPlayerIndex()) continue;
            
            GUIPlayer otherPlayer = clueGUI.getPlayers().get(i);
            Card foundCard = null;
            
            // Check if other player has any of the suggested cards
            Card suspectCard = otherPlayer.checkCard(suspect);
            Card roomCard = otherPlayer.checkCard(room);
            Card weaponCard = otherPlayer.checkCard(weapon);
            
            // Create list of cards that could be shown
            List<Card> possibleCards = new ArrayList<>();
            if (suspectCard != null) possibleCards.add(suspectCard);
            if (roomCard != null) possibleCards.add(roomCard);
            if (weaponCard != null) possibleCards.add(weaponCard);
            
            if (!possibleCards.isEmpty()) {
                disproven = true;
                
                // Filter out cards that have already been shown to this player
                List<Card> availableCards = new ArrayList<>();
                for (Card card : possibleCards) {
                    if (!otherPlayer.hasShownCardTo(currentPlayer, card)) {
                        availableCards.add(card);
                    }
                }
                
                // If all cards have been shown before, use all possible cards
                if (availableCards.isEmpty()) {
                    availableCards = possibleCards;
                }
                
                // If only one card can disprove, show that one
                if (availableCards.size() == 1) {
                    foundCard = availableCards.get(0);
                } 
                // If multiple cards can disprove, let player choose which to show
                else if (availableCards.size() > 1) {
                    // Create a dialog box for card selection
                    String[] cardOptions = new String[availableCards.size()];
                    for (int j = 0; j < availableCards.size(); j++) {
                        Card card = availableCards.get(j);
                        // Mark previously shown cards
                        String shownMark = otherPlayer.hasShownCardTo(currentPlayer, card) ? " (shown before)" : "";
                        cardOptions[j] = card.getName() + " (" + card.getType() + ")" + shownMark;
                    }
                    
                    // Have other player choose which card to show
                    String message = otherPlayer.getName() + ", choose which card to show to " + currentPlayer.getName() + ":";
                    String selectedCard = (String) JOptionPane.showInputDialog(
                        this, message, "Choose Card to Show", JOptionPane.QUESTION_MESSAGE,
                        null, cardOptions, cardOptions[0]);
                    
                    // Find the selected card
                    if (selectedCard != null) {
                        for (int j = 0; j < availableCards.size(); j++) {
                            Card card = availableCards.get(j);
                            String cardOption = card.getName() + " (" + card.getType() + ")";
                            if (selectedCard.startsWith(cardOption)) {
                                foundCard = card;
                                break;
                            }
                        }
                    } else {
                        // Default to first card if dialog was cancelled
                        foundCard = availableCards.get(0);
                    }
                }
                
                // Show the card to the current player
                if (foundCard != null) {
                    // Record that this card has been shown
                    otherPlayer.recordShownCard(currentPlayer, foundCard);
                    
                    addToGameLog(otherPlayer.getName() + " shows a card to " + currentPlayer.getName() + ".");
                    
                    JOptionPane.showMessageDialog(this,
                        otherPlayer.getName() + " shows you:\n" + foundCard.getName() + " (" + foundCard.getType() + ")",
                        "Card Shown", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Break after first player disproves
                    break;
                }
            }
        }
        
        if (!disproven) {
            addToGameLog("No one could disprove the suggestion!");
        }
        
        hasMadeSuggestionThisTurn = true;
    }
    private void rollDice() {
        if (hasRolledThisTurn) {
            addToGameLog("You already rolled this turn.");
            return;
        }
        if (movesRemaining > 0) {
            addToGameLog("You still have moves left!");
            return;
        }
        movesRemaining = (int)(Math.random() * 6) + 1;
        diceResults.setText("Moves Left: " + movesRemaining);
        hasRolledThisTurn = true; 
        addToGameLog("You rolled a " + movesRemaining + "! Use arrow keys to move.");
    }
    
    /*
     * End the current player's turn and move to the next player
     */
    private void endTurn() {
        movesRemaining = 0;
        hasRolledThisTurn = false;
        hasMadeSuggestionThisTurn = false;
        diceResults.setText("Moves Left: 0");
        clueGUI.nextPlayerTurn();
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
    private void eliminateCurrentPlayer(GUIPlayer toRemove) {
        List<GUIPlayer> players = clueGUI.getPlayers();
        int removedIdx = players.indexOf(toRemove);
        if (removedIdx == -1) return;

        // 1) yank them out of the list
        players.remove(removedIdx);

        // 2) if ClueGUI is tracking its own currentPlayerIndex, shift it back
        //    so that nextPlayerTurn() won't skip—(you already use getCurrentPlayerIndex() in makeSuggestion)
        int currIdx = clueGUI.getCurrentPlayerIndex();
        if (removedIdx <= currIdx) {
            // assume there's a setter; if not, add one in ClueGUI:
            clueGUI.setCurrentPlayerIndex(currIdx - 1);
        }
    }
    private void disableGameControls() {
        accuseButton.setEnabled(false);
        suggestButton.setEnabled(false);
        endTurnButton.setEnabled(false);
        rollDiceButton.setEnabled(false);
        showCardsButton.setEnabled(false);
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
}
