package edu.gonzaga;

import java.util.Scanner;

public class PlayGame {
    private Rooms rooms;
    private Suspects suspects;
    private Weapons weapons;
    private String currentPlayer;

    // Constructor to initialize game
    public PlayGame() {
        rooms = new Rooms();
        suspects = new Suspects();
        weapons = new Weapons();
        rooms.setMurderRoom(); // Randomize the room where the murder occurred
        suspects.setCulprit(); // Randomize the murderer
        weapons.setMurderWeapon(); // Randomize the murder weapon
    }

    // Start a new game
    public void startGame() {
        System.out.println("Starting the game...\n");
        System.out.println("Welcome to Clue!\n");

        try (
                Scanner scanner = new Scanner(System.in)) {
            boolean gameInProgress = true;
            
            GameOutcome gameOutcome = new GameOutcome();
            
            while (gameInProgress) {
                System.out.println("It's " + currentPlayer + "'s turn!");
                // Ask the player for an accusation
                System.out.println("Make an accusation (enter suspect, room, and weapon):");
                
                // Take user input for accusation (simple example, in a real game this would be more complex)
                String accusedSuspect = scanner.nextLine();
                String accusedRoom = scanner.nextLine();
                String accusedWeapon = scanner.nextLine();
                
                // Make an accusation
                gameInProgress = !gameOutcome.makeAccusation(accusedSuspect, accusedRoom, accusedWeapon, suspects, rooms, weapons);
                
                // Switch to the next player's turn (you can implement player management logic here)
                // currentPlayer = (currentPlayer.equals(player1) ? player2 : player1);
            }
        }
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public Weapons getWeapons() {
        return weapons;
    }

    public void setWeapons(Weapons weapons) {
        this.weapons = weapons;
    }

    public Suspects getSuspects() {
        return suspects;
    }

    public void setSuspects(Suspects suspects) {
        this.suspects = suspects;
    }
}
