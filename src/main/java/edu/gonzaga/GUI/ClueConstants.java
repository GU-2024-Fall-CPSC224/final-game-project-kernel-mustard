package edu.gonzaga.GUI;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueConstants {
    // Game cards
    private static final List<String> SUSPECTS = Arrays.asList(
        "Colonel Mustard", "Professor Plum", "Miss Scarlet",
        "Reverend Green", "Mrs. White", "Mrs. Peacock"
    );
    
    private static final List<String> WEAPONS = Arrays.asList(
        "Rope", "Lead Pipe", "Knife", "Wrench", "Candlestick", "Revolver"
    );
    
    private static final List<String> ROOMS = Arrays.asList(
        "Ballroom", "Conservatory", "Billiard Room", "Dining Room", "Hall",
        "Kitchen", "Library", "Lounge", "Study"
    );
   
    // Map of starting positions for each character
    private static final Map<String, Point> CHARACTER_START_POSITIONS = new HashMap<>();
    
    // Map character names to colors
    private static final Map<String, Color> CHARACTER_COLORS = new HashMap<>();
    
    static {
        CHARACTER_START_POSITIONS.put("Colonel Mustard", new Point(23, 7)); // Yellow
        CHARACTER_START_POSITIONS.put("Professor Plum", new Point(0, 5)); // Purple
        CHARACTER_START_POSITIONS.put("Miss Scarlet", new Point(16, 0)); // Red
        CHARACTER_START_POSITIONS.put("Reverend Green", new Point(9, 24)); // Green
        CHARACTER_START_POSITIONS.put("Mrs. White", new Point(14, 24)); // White
        CHARACTER_START_POSITIONS.put("Mrs. Peacock", new Point(0, 18)); // Blue
        
        CHARACTER_COLORS.put("Colonel Mustard", new Color(255, 215, 0));
        CHARACTER_COLORS.put("Professor Plum", new Color(128, 0, 128));
        CHARACTER_COLORS.put("Miss Scarlet", new Color(220, 20, 60));
        CHARACTER_COLORS.put("Reverend Green", new Color(34, 139, 34));
        CHARACTER_COLORS.put("Mrs. White", Color.WHITE);
        CHARACTER_COLORS.put("Mrs. Peacock", new Color(65, 105, 225));
    }
    
    // Static getters
    public static List<String> getSuspects() {
        return SUSPECTS;
    }
    
    public static List<String> getWeapons() {
        return WEAPONS;
    }
    
    public static List<String> getRooms() {
        return ROOMS;
    }
    
    public static Map<String, Point> getCharacterStartPositions() {
        return CHARACTER_START_POSITIONS;
    }
    
    public static Map<String, Color> getCharacterColors() {
        return CHARACTER_COLORS;
    }
}