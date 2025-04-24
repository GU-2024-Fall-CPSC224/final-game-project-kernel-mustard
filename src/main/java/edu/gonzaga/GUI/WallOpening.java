package edu.gonzaga.GUI;
import edu.gonzaga.GUI.*;

import java.awt.*;

public class WallOpening {
    Point roomPoint; // This holds the point coordinate inside the room (for entering)
    Point hallwayPoint; // This holds the point coordinate in the hallway (for exiting)
    int wallSide; // 0=top, 1=right, 2=bottom, 3=left
    
    public WallOpening(Point roomPoint, Point hallwayPoint, int wallSide) {
        this.roomPoint = roomPoint;
        this.hallwayPoint = hallwayPoint;
        this.wallSide = wallSide;
    }
}
