package edu.gonzaga;

import java.util.ArrayList;
import java.util.Random;

public class Weapons {
    ArrayList<String> weapons;
    public String murderWeapon; // This is our actual weapon used for killing

    // Constructor to initialize the list of weapons
    public Weapons() {
        weapons = new ArrayList<>();
        weapons.add("Rope");
        weapons.add("Lead Pipe");
        weapons.add("Knife");
        weapons.add("Wrench");
        weapons.add("Candlestick");
        weapons.add("Revolver");
    }

    // Get function for weapons
    public ArrayList<String> getWeapons() {
        return weapons;
    }

    // Get function for our murder weapon
    public String getMurderWeapon() {
        return murderWeapon;
    }

    // Method to print list of our weapons
    public void printWeapons() {
        for (String weapon : weapons) {
            System.out.println(weapon);
        }
    }

    // Method to randomly choose a weapon, store/return it, and remove it from our weapons list
    public String randomizeWeapon() {
        if (weapons.isEmpty()) {
            System.out.println("Error: No more weapons left in list");
            return "Error: No more weapons left in list";
        }

        Random rand = new Random();
        int index = rand.nextInt(weapons.size()); // Gets a random index from list
        String randomWeapon = weapons.remove(index); // Remove and store weapon
        return randomWeapon;
    }

    // Method to set our murderWeapon
    public void setMurderWeapon() {
        murderWeapon = randomizeWeapon();
    }
}
