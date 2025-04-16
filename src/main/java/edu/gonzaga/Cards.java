package edu.gonzaga;

import java.util.ArrayList;
import java.util.List;

public class Cards {
    private List<String> rooms;
    private List<String> weapons;
    private List<String> suspects;

    public Cards() {
        rooms = new ArrayList<>();
        weapons = new ArrayList<>();
        suspects = new ArrayList<>();
    }

    public void addRoom(String room) {
        rooms.add(room);
    }

    public void addWeapon(String weapon) {
        weapons.add(weapon);
    }

    public void addSuspect(String suspect) {
        suspects.add(suspect);
    }

    public boolean hasRoom(String room) {
        return rooms.contains(room);
    }

    public boolean hasWeapon(String weapon) {
        return weapons.contains(weapon);
    }

    public boolean hasSuspect(String suspect) {
        return suspects.contains(suspect);
    }

    public boolean disproves(String suspect, String weapon, String room) {
        return hasRoom(room) || hasWeapon(weapon) || hasSuspect(suspect);
    }

    public void showCards() {
        System.out.println("Rooms: " + rooms);
        System.out.println("Weapons: " + weapons);
        System.out.println("Suspects: " + suspects);
    }
}
