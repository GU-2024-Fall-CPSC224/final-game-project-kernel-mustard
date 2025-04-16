package edu.gonzaga;

public class Player {
    private String playerName;
    private String suspectName;
    private int row, col;
    private boolean isActive;
    private Cards hand;

    public Player(String playerName, String suspectName, int startRow, int startCol) {
        this.playerName = playerName;
        this.suspectName = suspectName;
        this.row = startRow;
        this.col = startCol;
        this.isActive = true;
        this.hand = new Cards();
    }

    // Basic info
    public String getPlayerName() { return playerName; }
    public String getSuspectName() { return suspectName; }
    public boolean isActive() { return isActive; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void setPosition(int r, int c) { row = r; col = c; }
    public void eliminate() { isActive = false; }

    // Card access
    public Cards getHand() { return hand; }

    // Print
    public void printStatus() {
        System.out.println(playerName + " (" + suspectName + ") at [" + row + ", " + col + "] — " +
                           (isActive ? "ACTIVE" : "ELIMINATED"));
    }

    public void showHand() {
        System.out.println(playerName + "'s cards:");
        hand.showCards();
    }
}
