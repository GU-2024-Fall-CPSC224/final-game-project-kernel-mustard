/*
 * Final Project: Clue
 * CPSC 224 - Spring 2025
 * 
 * 
 * Project Description:
 * This game is based on the popular board game Clue. The goal of the game 
 * is to solve a murder mystery by figuring out which suspect committed the 
 * murder, in which room, and with what weapon. Players will make suggestions 
 * and accusations, and the game will check if the player's guesses are correct.
 * The game will randomly select the murderer, weapon, and room at the beginning. 
 * Players will take turns making accusations, and if they are correct, the game 
 * will declare them the winner. If their accusation is wrong, they are eliminated 
 * from the game.
 * 
 * 
 * Contributors:
 * Charles Serafin
 * Samuel Allen
 * Waylan Parsell
 */

package edu.gonzaga;

public class MainGame {
    public static void main(String[] args) {
        System.out.println("Welcome to Clue!\n");

        // Create and set up the game
        PlayGame game = new PlayGame();

        // Start the game
        game.startGame();
    }
}
