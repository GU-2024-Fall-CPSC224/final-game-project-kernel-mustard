package edu.gonzaga;

import java.util.ArrayList;
import java.util.Random;

public class Rooms {
    ArrayList<String> rooms;
    public String murderRoom; // This is our actual room in which the murder occurred

    // Constructor to initialize the list of rooms
    public Rooms() {
        rooms = new ArrayList<>();
        rooms.add("Ballroom");
        rooms.add("Conservatory");
        rooms.add("Billiard Room");
        rooms.add("Dining Room");
        rooms.add("Hall");
        rooms.add("Kitchen");
        rooms.add("Library");
        rooms.add("Lounge");
        rooms.add("Study");
    }

    // Get function for rooms
    public ArrayList<String> getRooms() {
        return rooms;
    }

    // Get function for our room in which the murder happened
    public String getMurderRoom() {
        return murderRoom;
    }

    // Method to print list of our rooms
    public void printRooms() {
        for (String room : rooms) {
            System.out.println(room);
        }
    }

    // Method to randomly choose a room, store/return it, and remove it from our rooms list
    public String randomizeRoom() {
        if (rooms.isEmpty()) {
            System.out.println("Error: No more rooms left in list");
            return "Error: No more rooms left in list";
        }

        Random rand = new Random();
        int index = rand.nextInt(rooms.size()); // Gets a random index from list
        String randomRoom = rooms.remove(index); // Remove and store room
        return randomRoom;
    }

    // Method to set our murderRoom
    public void setMurderRoom() {
        murderRoom = randomizeRoom();
    }
}
