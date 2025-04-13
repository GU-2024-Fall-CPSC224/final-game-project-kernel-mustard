package edu.gonzaga;

public class GameOutcome {

    // Method to handle the accusation
    public boolean makeAccusation(String accusedSuspect, String accusedRoom, String accusedWeapon,
                                  Suspects suspects, Rooms rooms, Weapons weapons) {
        // Check if the accusation is correct
        if (accusedSuspect.equals(suspects.getCulprit()) && accusedRoom.equals(rooms.getMurderRoom()) && accusedWeapon.equals(weapons.getMurderWeapon())) {
            // If correct, declare the winner
            declareWinner(accusedSuspect, accusedRoom, accusedWeapon);
            return true; // Correct accusation, game ends
        } else {
            // If incorrect, player is out of the game
            System.out.println("The accusation is incorrect. You're out of the game!");
            return false; // Incorrect accusation
        }
    }

    // Method to declare the winner
    private void declareWinner(String accusedSuspect, String accusedRoom, String accusedWeapon) {
        System.out.println("The accusation is correct! " + accusedSuspect + " did it in the " + accusedRoom + " with the " + accusedWeapon + "!");
        // You can implement additional logic for handling the winner (e.g., stopping the game or announcing the winner)
    }
}
