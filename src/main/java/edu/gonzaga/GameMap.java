package edu.gonzaga;

import java.util.*;

public class GameMap {
    private final int rows = 25;
    private final int cols = 24;
    private String[][] grid;
    private Map<String, int[]> playerPositions;
    private Set<String> occupiedPositions;

    public GameMap() {
        grid = new String[rows][cols];
        playerPositions = new HashMap<>();
        occupiedPositions = new HashSet<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], ".");
        }

        // Place sample rooms (use abbreviations)
        placeRoom("Ba", 1, 5);   // Ballroom
        placeRoom("Ki", 21, 3);  // Kitchen
        placeRoom("Co", 3, 18);  // Conservatory
        placeRoom("Bi", 10, 10); // Billiard Room
        placeRoom("Li", 15, 18); // Library
        placeRoom("St", 22, 20); // Study
    }

    private void placeRoom(String abbreviation, int row, int col) {
        grid[row][col] = abbreviation;
    }

    public void addPlayer(String playerName, int startRow, int startCol) {
        if (grid[startRow][startCol].equals(".") && !occupiedPositions.contains(posKey(startRow, startCol))) {
            playerPositions.put(playerName, new int[]{startRow, startCol});
            occupiedPositions.add(posKey(startRow, startCol));
            grid[startRow][startCol] = playerName.substring(0, 1).toUpperCase();
        }
    }

    public boolean movePlayer(String playerName, int steps, List<String> directions) {
        int[] pos = playerPositions.get(playerName);
        Set<String> visited = new HashSet<>();
        visited.add(posKey(pos[0], pos[1]));

        for (int i = 0; i < steps; i++) {
            if (i >= directions.size()) break;

            int newRow = pos[0];
            int newCol = pos[1];

            String dir = directions.get(i).toLowerCase();
            if (dir.equals("up")) newRow--;
            else if (dir.equals("down")) newRow++;
            else if (dir.equals("left")) newCol--;
            else if (dir.equals("right")) newCol++;
            else continue;

            if (!isValidMove(newRow, newCol, visited)) {
                System.out.println("Invalid move: blocked or already visited.");
                return false;
            }

            // Check if the new tile is a room — if yes, stop there
            if (grid[newRow][newCol].length() == 2) {
                updatePlayerPosition(playerName, pos, newRow, newCol);
                break;
            }

            // Regular move
            updatePlayerPosition(playerName, pos, newRow, newCol);
            visited.add(posKey(newRow, newCol));
        }

        return true;
    }

    private void updatePlayerPosition(String playerName, int[] pos, int newRow, int newCol) {
        grid[pos[0]][pos[1]] = ".";
        occupiedPositions.remove(posKey(pos[0], pos[1]));

        pos[0] = newRow;
        pos[1] = newCol;

        grid[newRow][newCol] = playerName.substring(0, 1).toUpperCase();
        occupiedPositions.add(posKey(newRow, newCol));
    }

    private boolean isValidMove(int row, int col, Set<String> visited) {
        return row >= 0 && row < rows && col >= 0 && col < cols &&
                !occupiedPositions.contains(posKey(row, col)) &&
                !visited.contains(posKey(row, col));
    }

    private String posKey(int row, int col) {
        return row + "," + col;
    }

    public void printMap() {
        for (String[] row : grid) {
            for (String tile : row) {
                System.out.print(tile + " ");
            }
            System.out.println();
        }
    }

    public int[] getPlayerPosition(String playerName) {
        return playerPositions.getOrDefault(playerName, new int[]{-1, -1});
    }

    public boolean isPlayerInRoom(String playerName) {
        int[] pos = getPlayerPosition(playerName);
        return grid[pos[0]][pos[1]].length() == 2;
    }

    public String getRoomAtPosition(int row, int col) {
        String tile = grid[row][col];
        return tile.length() == 2 ? tile : null;
    }

    public void removePlayer(String playerName) {
        int[] pos = playerPositions.remove(playerName);
        if (pos != null) {
            occupiedPositions.remove(posKey(pos[0], pos[1]));
            if (grid[pos[0]][pos[1]].length() == 1) {
                grid[pos[0]][pos[1]] = ".";
            }
        }
    }

    public List<String> getValidDirections(String playerName, Set<String> visited) {
        List<String> valid = new ArrayList<>();
        int[] pos = getPlayerPosition(playerName);
        int row = pos[0];
        int col = pos[1];

        if (isValidMove(row - 1, col, visited)) valid.add("up");
        if (isValidMove(row + 1, col, visited)) valid.add("down");
        if (isValidMove(row, col - 1, visited)) valid.add("left");
        if (isValidMove(row, col + 1, visited)) valid.add("right");

        return valid;
    }

    public int rollDice() {
        Random rand = new Random();
        return rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
    }
}
