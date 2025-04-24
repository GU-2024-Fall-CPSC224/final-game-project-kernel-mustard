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
        CHARACTER_START_POSITIONS.put("Colonel Mustard", new Point(0, 13)); // Yellow, starts near Lounge
        CHARACTER_START_POSITIONS.put("Professor Plum", new Point(18, 14)); // Purple, starts near Study
        CHARACTER_START_POSITIONS.put("Miss Scarlet", new Point(5, 0)); // Red, starts near Kitchen
        CHARACTER_START_POSITIONS.put("Reverend Green", new Point(18, 4)); // Green, starts near Conservatory
        CHARACTER_START_POSITIONS.put("Mrs. White", new Point(4, 17)); // White, starts near Lounge
        CHARACTER_START_POSITIONS.put("Mrs. Peacock", new Point(12, 17)); // Blue, starts near Library
        
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