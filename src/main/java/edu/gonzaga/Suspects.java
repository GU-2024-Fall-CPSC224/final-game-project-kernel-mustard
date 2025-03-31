package edu.gonzaga;

import java.util.ArrayList;
import java.util.Random;

public class Suspects {
    ArrayList<String> suspects;
    public String culprit; // This is our actual murderer

    // Constructor to initialize the list of suspects
    public Suspects() {
        suspects = new ArrayList<>();
        suspects.add("Colonel Mustard");
        suspects.add("Professor Plum");
        suspects.add("Miss Scarlet");
        suspects.add("Reverend Green");
        suspects.add("Mrs. White");
        suspects.add("Mrs. Peacock");
    }

    // Get function for suspects
    public ArrayList<String> getSuspects() {
        return suspects;
    }

    // Get function for our murderer (culprit)
    public String getCulprit() {
        return culprit;
    }

    // Method to print list of our suspects
    public void printSuspects() {
        for (String suspect : suspects) {
            System.out.println(suspect);
        }
    }

    // Method to randomly choose a suspect, store/return it, and remove it from our suspects list
    public String randomizeSuspect() {
        if (suspects.isEmpty()) {
            System.out.println("Error: No more suspects left in list");
            return "Error: No more suspects left in list";
        }

        Random rand = new Random();
        int index = rand.nextInt(suspects.size()); // Gets a random index from list
        String randomSuspect = suspects.remove(index); // Remove and store suspect
        return randomSuspect;
    }

    // Method to set our culprit / Murderer
    public void setCulprit() {
        culprit = randomizeSuspect();
    }
}
