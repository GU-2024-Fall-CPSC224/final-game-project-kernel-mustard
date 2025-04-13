package edu.gonzaga;

public class Suggestion {
    private String suggestedRoom;
    private String suggestedSuspect;
    private String suggestedWeapon;

    // Constructor
    public Suggestion(String room, String suspect, String weapon) {
        this.suggestedRoom = room;
        this.suggestedSuspect = suspect;
        this.suggestedWeapon = weapon;
    }

    // Method to make a suggestion
    public void makeSuggestion() {
        System.out.println("Suggestion: " + suggestedSuspect + " in the " + suggestedRoom + " with the " + suggestedWeapon);
        
        // Logic to check if any player can disprove the suggestion (maybe other players have cards matching the suggestion)
        // If another player disproves the suggestion, it would be handled here
    }

    // Getters
    public String getSuggestedRoom() {
        return suggestedRoom;
    }

    public String getSuggestedSuspect() {
        return suggestedSuspect;
    }

    public String getSuggestedWeapon() {
        return suggestedWeapon;
    }

    public void setSuggestedRoom(String suggestedRoom) {
        this.suggestedRoom = suggestedRoom;
    }

    public void setSuggestedWeapon(String suggestedWeapon) {
        this.suggestedWeapon = suggestedWeapon;
    }

    public void setSuggestedSuspect(String suggestedSuspect) {
        this.suggestedSuspect = suggestedSuspect;
    }
}
