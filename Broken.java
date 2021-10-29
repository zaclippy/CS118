/*
 * File:    Broken	.java
 * Created: 7 September 2001
 * Author:  Stephen Jarvis
 */

// javac -classpath maze-environment.jar Broken.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Broken {

    public void controlRobot(IRobot robot) {

        int direction;
        int randno;
        
        switch (isTargetNorth(robot)) {
            case 1: System.out.print("Target is north");
            break;
            case -1: System.out.print("Target is south");
            break;
            default: System.out.print("Target is ");
        }
        switch (isTargetEast(robot)) {
            case 1: System.out.println("east");
            break;
            case -1: System.out.println("west");
            break;
            default: System.out.println("");
        }


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
    
    private byte isTargetNorth(IRobot robot) {
        if(robot.getTargetLocation().y < robot.getLocation().y) {
            return 1; // yes
        } else if (robot.getTargetLocation().y > robot.getLocation().y) {
            return -1; // no
        } else {
            return 0; // same y coordinate
        }
    }

    private byte isTargetEast(IRobot robot) {
        if (robot.getTargetLocation().x > robot.getLocation().x) {
            return 1; // yes
        } else if (robot.getTargetLocation().x < robot.getLocation().x) {
            return -1; // no
        } else {
            return 0; // same x coordinate
        }
    }

    private int lookHeading(int direction, IRobot robot) {
        // set heading to north:
        robot.setHeading(IRobot.NORTH);
        // LOOK RIGHT FOR EAST etc.
        robot.look(direction);
    }
}
