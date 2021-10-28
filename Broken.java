/*
 * File:    Broken	.java
 * Created: 7 September 2001
 * Author:  Stephen Jarvis
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Broken {

    public void controlRobot(IRobot robot) {

        int direction;
        int randno;

        do {
            randno = (int) Math.round(Math.random() * 3);

            if (randno == 0)
                direction = IRobot.LEFT;
            else if (randno == 1)
                direction = IRobot.RIGHT;
            else if (randno == 2)
                direction = IRobot.BEHIND;
            else
                direction = IRobot.AHEAD;
        } while (robot.look(direction) == IRobot.WALL);

        robot.face(direction);  /* Face the direction */

    }
    // The coordinates begin (1,1) at the top left-hand corner of the maze
    // and increase to (n,n) at the bottom right-hand corner (default 15x15).

    // return 1 for yes, -1 for no, 0 for same latitude
    private byte isTargetNorth(IRobot robot) {
        if(robot.getTargetLocation().y > robot.getLocation().y) {
            return 1;
        } else if (robot.getTargetLocation().y < robot.getLocation().y) {
            return -1;
        } else {
            return 0;
        }
    }
}
