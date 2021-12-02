// For Cw2 Ex1

/*
* The robot should never reverse direction, except at dead ends.
* At corners it should turn left or right so as to avoid collisions.
* At junctions it should, if possible, turn so as to move into a square that it has not previously been explored, choosing randomly if there are more than one. If this is not possible it should randomly choose a direction that doesn’t cause a collision.
* Similar behaviour to junctions should be exhibited at crossroads: the robot should select an unexplored exit if possible, selecting randomly between the exits if more than one is possible. If there are no unexplored exits then the robot should randomly choose a direction that doesn’t cause a collision.
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.util.ArrayList;

public class Explorer {

    private int pollRun = 0;     // Incremented after each pass
    private RobotData robotData; // Data store for junctions
    public void controlRobot(IRobot robot) {
        // On the first move of the first run of a new maze
        if ((robot.getRuns() == 0) && (pollRun == 0))
            robotData = new RobotData(); //reset the data store
        System.out.println(robot.getLocation());
        int exits = nonWallExits(robot);
        int direction;
        if (exits == 2) {
            direction = corridor(robot);
        } else if (exits == 3) {
            // Add the junction to the junctionCounter elements
            robotData.recordJunction(robot.getLocation().x, robot.getLocation().y, robot.getHeading());
            direction = junction(robot);
        } else if (exits == 4) {
            // Add the crossroads (junction and crossroads are basically the same) to the junctionCounter elements
            robotData.recordJunction(robot.getLocation().x, robot.getLocation().y, robot.getHeading());
            direction = crossroads(robot);
        } else {
            direction = deadEnd(robot);
        }
        robot.face(direction);
        pollRun++; // Increment pollRun so that the data is not reset each time the robot moves
    }

    private int deadEnd(IRobot robot) {
        // If robot has been in the only possible direction, then move back down the direction it just came from (but if robot is at the start, move ahead)
        int dir=IRobot.AHEAD;
        while (dir <= IRobot.LEFT) {
            if (robot.look(dir) != IRobot.WALL) break;
            dir++;
        }
        return dir;
    }


    private int corridor(IRobot robot) {
        // do not crash into walls and do not rseverse direction and go back on itself
        // only one direction out of ahead, left and right ever satisfies these criteria, so go to whichever one is not a wall.
        if (robot.look(IRobot.AHEAD) != IRobot.WALL) {
            return IRobot.AHEAD;
        } else if (robot.look(IRobot.LEFT) != IRobot.WALL) {
            return IRobot.LEFT;
        } else return IRobot.RIGHT;
    }

    private int junction(IRobot robot) {
        int passageExits = passageExits(robot);
        if (passageExits == 0) {
            return 2000 + (int) (Math.random() * 3);
        } else {
            int[] passages = new int[passageExits];
            short i = 0;
            for (int direction = IRobot.AHEAD; direction <= IRobot.LEFT; direction++)
                if (robot.look(direction) == IRobot.PASSAGE) {
                    passages[i] = direction;
                    i++;
                }
            return passages[(int) (Math.random() * passageExits)];
        }
    }


    private int crossroads(IRobot robot) {
        // If there is one+ passage exit, the controller will choose one of the passages randomly
        // if there are no passage exits then the controller will choose randomly between all of the non wall directions.
        int passageExits = passageExits(robot);
        if (passageExits == 0) {
            return 2000 + (int) (Math.random() * 4);
        } else {
            int[] passages = new int[passageExits];
            short i = 0;
            for (int direction = IRobot.AHEAD; direction <= IRobot.LEFT; direction++)
                if (robot.look(direction) == IRobot.PASSAGE) {
                    passages[i] = direction;
                    i++;
                }
            return passages[(int) (Math.random() * passageExits)];
        }
    }

    // returns the number of passage exit squares adjacent to the robot's current square
    private int passageExits(IRobot robot) {
        int passageExits = 0;
        for (int look = IRobot.AHEAD; look<=IRobot.LEFT; look++)
            if(robot.look(look) == IRobot.PASSAGE) passageExits++;
        return passageExits;
    }

    // returns the number of been before exit squares adjacent to the robot's current square
    private int beenbeforeExits(IRobot robot) {
        int beenbeforeExits = 0;
        for (int look = IRobot.AHEAD; look<=IRobot.LEFT; look++)
            if(robot.look(look) == IRobot.BEENBEFORE) beenbeforeExits++;
        return beenbeforeExits;
    }

    // returns the number of non wall squares adjacent to the robot's current square
    private int nonWallExits(IRobot robot) {
        int noOfExits = 0;
        // loop through all directions and if that direction does not face a wall, add one to the noOfExits variable which will be returned at the end of the loop
        for (int look = IRobot.AHEAD; look <= IRobot.LEFT; look++)
            if (robot.look(look) != IRobot.WALL) noOfExits++;
        return noOfExits;
    }

    public void reset() {
        robotData.resetJunctionCounter();
    }

}

class RobotData {
    private static int maxJunctions =  10000; // Max number likely to occur
    private static int junctionCounter; // No. of junctions for which information has been recorded - i.e. the i for the arrays of the elements.
    private int[] juncX = new int[maxJunctions]; // x coordinates of the junctions
    private int[] juncY = new int[maxJunctions]; // y coordinates of the junction
    private int[] arrived; // heading the robot first appeared from

    public void recordJunction(int x, int y, int heading) {
        juncX[junctionCounter] = x; // store the x coordinate
        juncY[junctionCounter] = y; // store the y coordinate
        junctionCounter++; // increment the counter
    }

    public void resetJunctionCounter() {
        junctionCounter = 0;
    }
}