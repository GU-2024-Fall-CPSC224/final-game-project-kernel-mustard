package edu.gonzaga.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


// Player class to store player information
public class GUIPlayer {
    private String name;
    private String character;
    private Color color;
    private int row;
    private int col;
    private boolean inRoom;
    private String currentRoom;
    private List<Card> cards = new ArrayList<>();
    private Map<GUIPlayer, Set<Card>> cardsShownToPlayers = new HashMap<>();
    
    public GUIPlayer(String name, String character, Color color, int startRow, int startCol) {
        this.name = name;
        this.character = character;
        this.color = color;
        this.row = startRow;
        this.col = startCol;
        this.inRoom = false;
        this.currentRoom = null;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public String getCharacter() { return character; }
    public Color getColor() { return color; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isInRoom() { return inRoom; }
    public String getCurrentRoom() { return currentRoom; }
    
    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
    public void setInRoom(boolean inRoom) { this.inRoom = inRoom; }
    public void setCurrentRoom(String room) { this.currentRoom = room; }
    
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public List<Card> getCards() {
        return cards;
    }
    
    // Check if player has a card that matches the given name
    public Card checkCard(String cardName) {
        for (Card card : cards) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }
    
    public String getCardList() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append("- ").append(card.getName()).append(" (").append(card.getType()).append(")\n");
        }
        return sb.toString();
    }

    // Records shown cards to other players
    public void recordShownCard(GUIPlayer toPlayer, Card card) {
        cardsShownToPlayers.computeIfAbsent(toPlayer, k -> new HashSet<>()).add(card);
    }

    // Checks if a card has been shown to a player before
    public boolean hasShownCardTo(GUIPlayer toPlayer, Card card) {
        Set<Card> shownCards = cardsShownToPlayers.get(toPlayer);
        return shownCards != null && shownCards.contains(card);
    }

    public Map<GUIPlayer, Set<Card>> getCardsShownToPlayers() {
        return cardsShownToPlayers;
    }
}
