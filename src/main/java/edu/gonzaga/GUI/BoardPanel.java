package edu.gonzaga.GUI;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JPanel {
    // Set room coordinates and sizes
    private Map<String, Rectangle> rooms = new HashMap<>();
    private Map<String, Color> roomColors = new HashMap<>();
    private Map<String, List<WallOpening>> wallOpenings = new HashMap<>();
    private ClueGUI clueGUI;
    private AccusationPanel accusationPanel;
    private Image backgroundImage;
    
    // Set grid size and player position
    private final int GRID_SIZE = 40;
    private final int ROWS = 25;
    private final int COLS = 24;
    
    // Movement statuses
    private boolean[][] validMovementTiles;
    /*
    * Constructor for our game board panel.
    * Initializes rooms, doorways, movement grid, and sets up keyboard input
    */
    public BoardPanel(ClueGUI clueGUI, AccusationPanel accusationPanel) {
        this.clueGUI = clueGUI;
        this.accusationPanel = accusationPanel;
        backgroundImage = new ImageIcon(getClass().getResource("/edu/gonzaga/GUI/clueboard.jpg")).getImage();
        // Initialize rooms
        setupRooms();
        
        // Initialize wall openings
        setupWallOpenings();
        
        // Create valid movement grid
        setupValidMovementTiles();
        
        // Add key listener for player movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                movePlayer(e.getKeyCode());
            }
        });
    }

    /*
    * The following function is lengthy. Essentially just sets up each of our doorways for all the rooms.
    * I would definitely reccommend minimizing this function so you don't have to scroll past this every time. 
    */
    private void setupWallOpenings() {
        // Create wall openings for each room
        wallOpenings.put("Kitchen", new ArrayList<>());
        wallOpenings.get("Kitchen").add(new WallOpening(
            new Point(19, 18), // Inside point
            new Point(19, 17), // Outside point
            0 // Top wall
        ));
        
        wallOpenings.put("Ballroom", new ArrayList<>());
        wallOpenings.get("Ballroom").add(new WallOpening(
            new Point(9, 17),  // Inside point
            new Point(9, 16),  // Outside point
            0 // Top wall
        ));
        wallOpenings.get("Ballroom").add(new WallOpening(
            new Point(14, 17),  // Inside point
            new Point(14, 16),  // Outside point
            0 // Top wall
        ));
        wallOpenings.get("Ballroom").add(new WallOpening(
            new Point(15, 19),  // Inside point
            new Point(16, 19),  // Outside point
            1 // Right wall
        ));
        wallOpenings.get("Ballroom").add(new WallOpening(
            new Point(9, 19),  // Inside point
            new Point(7, 19),  // Outside point
            3 // Left wall
        ));
        
        wallOpenings.put("Conservatory", new ArrayList<>());
        wallOpenings.get("Conservatory").add(new WallOpening(
            new Point(4, 19),  // Inside point
            new Point(5, 19),  // Outside point
            1 // Right wall
        ));
        
        wallOpenings.put("Dining Room", new ArrayList<>());
        wallOpenings.get("Dining Room").add(new WallOpening(
            new Point(17, 9),  // Inside point
            new Point(17, 8),  // Outside point
            0 // Top wall
        ));
        wallOpenings.get("Dining Room").add(new WallOpening(
            new Point(17, 12),  // Inside point
            new Point(15, 12),  // Outside point
            3 // Left wall
        ));
        
        wallOpenings.put("Lounge", new ArrayList<>());
        wallOpenings.get("Lounge").add(new WallOpening(
            new Point(18, 5),  // Inside point
            new Point(17, 6),  // Outside point
            2 // Bottom wall
        ));
        
        wallOpenings.put("Hall", new ArrayList<>());
        wallOpenings.get("Hall").add(new WallOpening(
            new Point(10, 4),  // Inside point
            new Point(8, 4),  // Outside point
            3 // Left wall
        ));
        wallOpenings.get("Hall").add(new WallOpening(
            new Point(11, 5),  // Inside point
            new Point(11, 7),  // Outside point
            2 // Bottom wall
        ));
        wallOpenings.get("Hall").add(new WallOpening(
            new Point(12, 5),  // Inside point
            new Point(12, 7),  // Outside point
            2 // Bottom wall
        ));
        
        wallOpenings.put("Study", new ArrayList<>());
        wallOpenings.get("Study").add(new WallOpening(
            new Point(6, 3),  // Inside point
            new Point(6, 4),  // Outside point
            0 // Top wall
        ));
        
        wallOpenings.put("Library", new ArrayList<>());
        wallOpenings.get("Library").add(new WallOpening(
            new Point(5, 8),  // Inside point
            new Point(7, 8),  // Outside point
            1 // Right wall
        ));
        wallOpenings.get("Library").add(new WallOpening(
            new Point(3, 10),  // Inside point
            new Point(3, 11),  // Outside point
            2 // Bottom wall
        ));
        
        wallOpenings.put("Billiard Room", new ArrayList<>());
        wallOpenings.get("Billiard Room").add(new WallOpening(
            new Point(1, 12),  // Inside point
            new Point(1, 11),  // Outside point
            0 // Top wall
        ));
        wallOpenings.get("Billiard Room").add(new WallOpening(
            new Point(5, 15),  // Inside point
            new Point(6, 15),  // Outside point
            1 // Right wall
        ));
        wallOpenings.put("CLUE", new ArrayList<>());
    }
    
    /*
    * Creates a boolean grid that tracks which tiles are valid for player to move in.
    * Marks hallways as valid and rooms and invalid for direct movement.
    * TODO: Need to make it so a user from inside a room can exit from either doorway if there is more than one.
    * Currently a user can only exit from the door they entered from.
    */
    private void setupValidMovementTiles() {
        validMovementTiles = new boolean[ROWS][COLS];
        
        // Initially we will mark all tiles as valid
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                validMovementTiles[row][col] = true;
            }
        }
        
        // Then we mark tiles in a room as invalid for movement
        for (Rectangle room : rooms.values()) {
            int startRow = (room.y - 53)/ 39;
            int startCol = (room.x - 258) / GRID_SIZE;
            int endRow = (room.y + room.height - 55) / GRID_SIZE;
            int endCol = (room.x + room.width - 260) / GRID_SIZE;
            
            for (int row = startRow; row < endRow; row++) {
                for (int col = startCol; col < endCol; col++) {
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        validMovementTiles[row][col] = false;
                    }
                }
            }
        }
        
        // Add doorway points as valid movement tiles
        for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
            for (WallOpening opening : entry.getValue()) {
                validMovementTiles[opening.hallwayPoint.y][opening.hallwayPoint.x] = true;
            }
        }
        
        // These spots were weirdly enough not covered by the rooms even though they should've been. As a late-night fix
        // I have specifically set them to be false.

        // Section below and right of Study (works)
        for (int i = 1; i < 7; ++i) {
            validMovementTiles[3][i] = false;
        } 
        for (int j = 0; j < 3; ++j) {
            validMovementTiles[j][6] = false;
        }
        // Section below Lounge (works)
        for (int i = 17; i < 23; ++i) {
            validMovementTiles[5][i] = false;
        } 
        // Section below Hall (works)
        for (int i = 9; i < 15; ++i) {
            validMovementTiles[6][i] = false;
        }
        // Section below Library (works)
        for (int i = 0; i < 6; ++i) {
            validMovementTiles[10][i] = false;
        }
        // Section below Dining Room
        for (int i = 16; i < 20; ++i) {
            validMovementTiles[14][i] = false;
        }
        // Section below Clue
        for (int i = 9; i < 14; ++i) {
            validMovementTiles[14][i] = false;
        }
        // Section below Billiard Room
        for (int i = 0; i < 6; ++i) {
            validMovementTiles[16][i] = false;
        }
        // Section below Ballroom
        for (int i = 8; i < 16; ++i) {
            validMovementTiles[22][i] = false;
        }


        // Lastly, mark any out of bounds spots that are not covered by rectangular rooms
        validMovementTiles[4][0] = false;
        validMovementTiles[11][0] = false;
        validMovementTiles[17][0] = false;

        // Section right of library (including door)
        validMovementTiles[7][6] = false;
        validMovementTiles[8][6] = false;
        validMovementTiles[9][6] = false;

        // Section right of Conservatory 
        validMovementTiles[20][5] = false;
        validMovementTiles[21][5] = false;
        validMovementTiles[22][5] = false;
        validMovementTiles[23][5] = false;
        validMovementTiles[24][5] = false;

        validMovementTiles[23][6] = false;
        validMovementTiles[24][6] = false;

        validMovementTiles[24][7] = false;
        validMovementTiles[0][8] = false;

        // Tiny section left of Hall
        validMovementTiles[24][8] = false;

        // Section below Ballroom
        validMovementTiles[23][10] = false;
        validMovementTiles[23][11] = false;
        validMovementTiles[23][12] = false;
        validMovementTiles[23][13] = false;
        validMovementTiles[24][10] = false;
        validMovementTiles[24][11] = false;
        validMovementTiles[24][12] = false;
        validMovementTiles[24][13] = false;

        // Tiny section right of Hall
        validMovementTiles[0][15] = false;

        // Section left of Kitchen
        validMovementTiles[24][15] = false;
        validMovementTiles[24][16] = false; 
        validMovementTiles[23][17] = false;
        validMovementTiles[24][17] = false;

        // Section below Dining Room
        validMovementTiles[15][19] = false;
        validMovementTiles[15][20] = false;
        validMovementTiles[15][21] = false;
        validMovementTiles[15][22] = false;
        validMovementTiles[15][23] = false;
        validMovementTiles[16][23] = false;

        // Section between Dining Room and Lounge
        validMovementTiles[6][23] = false;
        validMovementTiles[8][23] = false;
    }
    
    /*
    * Handles player movement based upon keyboard input
    */
    private void movePlayer(int keyCode) {
        GUIPlayer currentPlayer = clueGUI.getCurrentPlayer();
        if (currentPlayer == null) return;

        if (accusationPanel.movesRemaining <= 0 && keyCode != KeyEvent.VK_ENTER && accusationPanel.isHasRolledThisTurn()) {
            accusationPanel.addToGameLog("You have no moves left!");
            return;
        }
        if (accusationPanel.movesRemaining <= 0 && keyCode != KeyEvent.VK_ENTER && !accusationPanel.isHasRolledThisTurn()) {
            accusationPanel.addToGameLog("You must roll the dice before moving!");
            return;
        }
        
        int newRow = currentPlayer.getRow();
        int newCol = currentPlayer.getCol();
        
        switch (keyCode) {
            case KeyEvent.VK_UP:
                newRow--;
                break;
            case KeyEvent.VK_DOWN:
                newRow++;
                break;
            case KeyEvent.VK_LEFT:
                newCol--;
                break;
            case KeyEvent.VK_RIGHT:
                newCol++;
                break;
            case KeyEvent.VK_ENTER:
                // Handle room entry/exit with 'Enter' key
                handleRoomEntryExit(currentPlayer);
                return;
            default:
                return; // Ignore other keys
        }
        
        // Check if player is in a room
        if (currentPlayer.isInRoom()) {
            // If in room, can only move to wall opening hallway points
            boolean isExit = false;
            for (WallOpening opening : wallOpenings.get(currentPlayer.getCurrentRoom())) {
                if (opening.hallwayPoint.y == newRow && opening.hallwayPoint.x == newCol) {
                    isExit = true;
                    break;
                }
            }
            
            if (isExit) {
                currentPlayer.setRow(newRow);
                currentPlayer.setCol(newCol);
                currentPlayer.setInRoom(false);
                currentPlayer.setCurrentRoom(null);
                repaint();
            }
        } 
        // Player is in hallway
        else {
            // Check if new position is valid for movement
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && 
                validMovementTiles[newRow][newCol]) {
                
                // Check if another player is already on this tile
                boolean tileOccupied = false;
                for (GUIPlayer p : clueGUI.getPlayers()) {
                    if (p != currentPlayer && p.getRow() == newRow && p.getCol() == newCol && !p.isInRoom()) {
                        tileOccupied = true;
                        break;
                    }
                }
                
                if (!tileOccupied) {
                    currentPlayer.setRow(newRow);
                    currentPlayer.setCol(newCol);

                    accusationPanel.movesRemaining--;
                    accusationPanel.getDiceResults().setText("Moves Left: " + accusationPanel.movesRemaining);
                    
                    // Check if player moved to a wall opening
                    checkIfAtWallOpening(currentPlayer);
                    
                    repaint();
                }
            }
        }
    }
        
    /*
    * This function manages entrance and exit to/from rooms when a player presses the 'Enter' key
    */
    private void handleRoomEntryExit(GUIPlayer player) {
        if (!player.isInRoom()) {
            // Check if player is standing next to a wall opening
            for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
                String roomName = entry.getKey();
                List<WallOpening> openings = entry.getValue();
                
                for (WallOpening opening : openings) {
                    if (opening.hallwayPoint.y == player.getRow() && opening.hallwayPoint.x == player.getCol()) {
                        // Enter room
                        player.setInRoom(true);
                        player.setCurrentRoom(roomName);
                        
                        // Move player inside room
                        player.setRow(opening.roomPoint.y);
                        player.setCol(opening.roomPoint.x);

                        accusationPanel.addToGameLog(player.getName() + " entered the " + roomName);
                        
                        repaint();
                        return;
                    }
                }
            }
        } else {
            // Player is in room, check if they can exit
            String currentRoom = player.getCurrentRoom();
            if (currentRoom != null && wallOpenings.containsKey(currentRoom)) {
                for (WallOpening opening : wallOpenings.get(currentRoom)) {
                    // Check if player is at the room point of any opening
                    if (opening.roomPoint.y == player.getRow() && opening.roomPoint.x == player.getCol()) {
                        // Exit room
                        player.setInRoom(false);
                        player.setCurrentRoom(null);
                        
                        // Move player to the hallway point
                        player.setRow(opening.hallwayPoint.y);
                        player.setCol(opening.hallwayPoint.x);
                        
                        accusationPanel.addToGameLog(player.getName() + " exited the " + currentRoom);
                        
                        repaint();
                        return;
                    }
                }
            }
        }
    }
    
    /*
    * Checks if a user is standing next to a doorway. We call this after each time the player moves and just use it
    * to tell the user they can push 'enter' to go into a room.
    */
    private void checkIfAtWallOpening(GUIPlayer player) {
        // Check if player is standing at a wall opening
        for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
            for (WallOpening opening : entry.getValue()) {
                if (opening.hallwayPoint.y == player.getRow() && opening.hallwayPoint.x == player.getCol()) {
                    repaint();
                    return;
                }
            }
        }
    }
    
    /*
    * Initializes game rooms with their specific positions, colors, and sizes.
    */
    private void setupRooms() {

        // Set room rectangles (x, y, width, height)
        rooms.put("Kitchen", new Rectangle(983, 760, 237, 280));
        rooms.put("Ballroom", new Rectangle(583, 723, 317, 230));
        rooms.put("Conservatory", new Rectangle(263, 799, 200, 231));
        rooms.put("Dining Room", new Rectangle(903, 410, 315, 235));
        rooms.put("Lounge", new Rectangle(940, 55, 278, 235));    
        rooms.put("Hall", new Rectangle(620, 55, 240, 270));           
        rooms.put("Study", new Rectangle(258, 53, 273, 160));           
        rooms.put("Library", new Rectangle(260, 290, 240, 196));      
        rooms.put("Billiard Room", new Rectangle(260, 523, 240, 196)); 
        rooms.put("CLUE", new Rectangle(623, 370, 200, 273));
        
        // Set room colors
        roomColors.put("Kitchen", new Color(220, 220, 220));
        roomColors.put("Ballroom", new Color(235, 199, 158));
        roomColors.put("Conservatory", new Color(180, 220, 180));
        roomColors.put("Dining Room", new Color(220, 180, 160));
        roomColors.put("Hall", new Color(200, 200, 220));
        roomColors.put("Lounge", new Color(200, 180, 160));
        roomColors.put("Library", new Color(220, 180, 180));
        roomColors.put("Study", new Color(180, 200, 220));
        roomColors.put("Billiard Room", new Color(180, 220, 220));
        roomColors.put("CLUE", new Color(230, 230, 230));
    }
    
    /*
    * This is the main drawing method that renders our game board
    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw the grid
        drawGrid(g2d);
        
        // Draw rooms with wall openings
        drawRoomsWithOpenings(g2d);
        
        // Temporarily draw image with translucency for matching grid
        float alpha = 1f; 

        Composite originalComposite = g2d.getComposite();

        AlphaComposite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        
        g2d.setComposite(alphaComp);

        // Delete above to get rid of translucency
        g2d.drawImage(backgroundImage, 200, 0, 1080, 1080, this);

        // Restore composite state (translucency)
        g2d.setComposite(originalComposite);

        // Draw player token
        drawPlayers(g2d);
        
        // Draw status information
        drawStatus(g2d);
    }
    
    /*
    * Draws the background grid for the game board
    */
    private void drawGrid(Graphics2D g2d) {
        // Fill background
        g2d.setColor(new Color(200, 150, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = 258 + col * GRID_SIZE;
                int y = 53 + row * 39;
                
                if (validMovementTiles[row][col]) {
                
                    g2d.setColor(new Color(190, 180, 180));
                    g2d.fillRect(x, y, GRID_SIZE, GRID_SIZE);
                    
                    // Grid lines
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, GRID_SIZE, GRID_SIZE);
                }
            }
        }
    }
    
    /*
    * This draws all the rooms on the game board, including their walls, doorways, and labels.
    */
    private void drawRoomsWithOpenings(Graphics2D g2d) {
        // First draw room interiors
        for (Map.Entry<String, Rectangle> room : rooms.entrySet()) {
            String roomName = room.getKey();
            Rectangle rect = room.getValue();
            
            // Fill room
            g2d.setColor(roomColors.get(roomName));
            g2d.fill(rect);
        }
        
        // Then draw room walls w/ doorways
        for (Map.Entry<String, Rectangle> room : rooms.entrySet()) {
            String roomName = room.getKey();
            Rectangle rect = room.getValue();
            List<WallOpening> openings = wallOpenings.get(roomName);
            
            // Draw each wall segment, skipping doorways
            drawWallWithOpenings(g2d, rect, openings, roomName);
            
            // Draw room name
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Times New Roman", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(roomName);
            int textHeight = fm.getHeight();
            
            int x = rect.x + (rect.width - textWidth) / 2;
            int y = rect.y + (rect.height - textHeight) / 2 + fm.getAscent();
            
            // Draw text
            g2d.drawString(roomName, x, y);
        }
    }
    
    /*
    * Draws the walls for a specific room, with gaps for each of our doorways
    */
    private void drawWallWithOpenings(Graphics2D g2d, Rectangle rect, List<WallOpening> openings, String roomName) {
        g2d.setColor(Color.ORANGE);
        g2d.setStroke(new BasicStroke(3));
        
        // Draw the rectangle border directly using the rectangle coordinates
        int x = rect.x;
        int y = rect.y;
        int width = rect.width;
        int height = rect.height;
        
        // Draw top wall
        if (openings != null) {
            // Draw top wall with openings (if needed)
            for (int i = 0; i < width; i += GRID_SIZE) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check if this segment should be an opening on the top wall
                    if (opening.wallSide == 0 && 
                        x + i >= x + opening.roomPoint.x * GRID_SIZE && 
                        x + i < x + (opening.roomPoint.x + 1) * GRID_SIZE) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    int segmentLength = Math.min(GRID_SIZE, width - i);
                    g2d.drawLine(x + i, y, x + i + segmentLength, y);
                }
            }
            
            // Draw right wall with openings (if needed)
            for (int i = 0; i < height; i += GRID_SIZE) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check for opening on right wall
                    if (opening.wallSide == 1 && 
                        y + i >= y + opening.roomPoint.y * GRID_SIZE && 
                        y + i < y + (opening.roomPoint.y + 1) * GRID_SIZE) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    int segmentLength = Math.min(GRID_SIZE, height - i);
                    g2d.drawLine(x + width, y + i, x + width, y + i + segmentLength);
                }
            }
            
            // Draw bottom wall with openings (if needed)
            for (int i = 0; i < width; i += GRID_SIZE) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check for opening on bottom wall
                    if (opening.wallSide == 2 && 
                        x + i >= x + opening.roomPoint.x * GRID_SIZE && 
                        x + i < x + (opening.roomPoint.x + 1) * GRID_SIZE) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    int segmentLength = Math.min(GRID_SIZE, width - i);
                    g2d.drawLine(x + i, y + height, x + i + segmentLength, y + height);
                }
            }
            
            // Draw left wall with openings (if needed)
            for (int i = 0; i < height; i += GRID_SIZE) {
                boolean isOpening = false;
                for (WallOpening opening : openings) {
                    // Check for opening on left wall
                    if (opening.wallSide == 3 && 
                        y + i >= y + opening.roomPoint.y * GRID_SIZE && 
                        y + i < y + (opening.roomPoint.y + 1) * GRID_SIZE) {
                        isOpening = true;
                        break;
                    }
                }
                
                if (!isOpening) {
                    int segmentLength = Math.min(GRID_SIZE, height - i);
                    g2d.drawLine(x, y + i, x, y + i + segmentLength);
                }
            }
        } else {
            // If no openings are defined, just draw the complete rectangle
            g2d.drawRect(x, y, width, height);
        }
    }
    
    /*
    * Draws all player tokens on the board
    */
    private void drawPlayers(Graphics2D g2d) {
        // Draw each player token
        for (GUIPlayer player : clueGUI.getPlayers()) {
            // Get player position
            int playerX, playerY;
            
            if (player.isInRoom()) {
                // If player is in a room, calculate position within room
                playerX = 258 + player.getCol() * 39;
                playerY = 53 + player.getRow() * GRID_SIZE;
            } else {
                // If player is in hallway, use grid coordinates
                playerX = 258 + player.getCol() * GRID_SIZE;
                playerY = 55 + player.getRow() * 39;
            }
            
            // Draw player token
            g2d.setColor(player.getColor());
            g2d.fillOval(playerX + 5, playerY + 5, GRID_SIZE - 10, GRID_SIZE - 10);
            
            // Draw token outline
            g2d.setColor(Color.BLACK);
            g2d.drawOval(playerX + 5, playerY + 5, GRID_SIZE - 10, GRID_SIZE - 10);
            
            // Draw first letter of character name
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.setColor(Color.BLACK);
            String initial = player.getCharacter().substring(0, 1);
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(initial);
            int textHeight = metrics.getHeight();
            g2d.drawString(initial, playerX + (GRID_SIZE - textWidth) / 2, 
                            playerY + (GRID_SIZE + textHeight) / 2 - 2);
            
            // Highlight current player so it's easier to tell who's turn it is
            if (player == clueGUI.getCurrentPlayer()) {
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
                g2d.drawOval(playerX + 2, playerY + 2, GRID_SIZE - 4, GRID_SIZE - 4);
                g2d.setStroke(new BasicStroke(1));
            }
        }
    }
    
    /*
    * Draws game status information on the board
    */
    private void drawStatus(Graphics2D g2d) {
        GUIPlayer currentPlayer = clueGUI.getCurrentPlayer();
        if (currentPlayer == null) return;
        
        // Draw current player info
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getCharacter() + ")", 10, getHeight() - 40);
        
        // Draw player position info
        String posInfo;
        if (currentPlayer.isInRoom()) {
            posInfo = "In the " + currentPlayer.getCurrentRoom();
        } else {
            // Check if player is at doorway
            String doorwayInfo = "";
            for (Map.Entry<String, List<WallOpening>> entry : wallOpenings.entrySet()) {
                for (WallOpening opening : entry.getValue()) {
                    if (opening.hallwayPoint.y == currentPlayer.getRow() && 
                        opening.hallwayPoint.x == currentPlayer.getCol()) {
                        doorwayInfo = " - Press Enter to enter " + entry.getKey();
                        break;
                    }
                }
                if (!doorwayInfo.isEmpty()) break;
            }
            posInfo = "In hallway" + doorwayInfo;
        }
        g2d.drawString(posInfo, 10, getHeight() - 20);
        
        // Draw navigation instructions
        g2d.drawString("Use arrow keys to move, Enter to enter/exit rooms", getWidth() - 400, getHeight() - 20);
    }
}
