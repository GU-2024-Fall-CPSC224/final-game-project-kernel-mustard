package edu.gonzaga;

import java.awt.*;


// Player class to store player information
public class GUIPlayer {
    private String name;
    private String character;
    private Color color;
    private int row;
    private int col;
    private boolean inRoom;
    private String currentRoom;
    
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
}
