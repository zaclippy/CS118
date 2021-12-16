/*
*
 */

// javac -classpath maze-environment.jar Ex2.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Stack;

public class Ex1 {
    private int explorerMode = 1;  // 1 = explore, 0 = backtrack
    private int pollRun = 0;     // Incremented after each pass
    private RobotData1 robotData; // Data store for junctions

    public void controlRobot(IRobot robot) {
        // maximum number of steps
        if (pollRun > 5000) {
            reset();
        }
        // On the first move of the first run of a g new maze
        if ((robot.getRuns() == 0) && (pollRun == 0)) {
            robotData = new RobotData1(); //reset the data store
        } if (pollRun == 0) explorerMode = 1;
        if (explorerMode == 1) exploreControl(robot);
        else backtrackControl(robot);
        pollRun++; // Increment pollRun so that the data is not reset each time the robot moves
    }

    private void exploreControl(IRobot robot) {
        int exits = nonWallExits(robot);
        int direction;
        if (exits == 1) {
            direction = deadEnd(robot);
            if (pollRun != 0) explorerMode = 0; // sets to backtrack mode UNLESS ITS THE FIRST ONE
        } else if (exits == 2) {
            direction = corridor(robot);
        } else  /* (either will be a junction or a crossroads) */ {
            direction = junction(robot, exits);
        }
        robot.face(direction);
    }

    private void backtrackControl(IRobot robot) {
        // calculate no of non wall exits in relation to the robot position
        int exits = nonWallExits(robot);
        int direction = 1000;
        if (exits > 2 /* i.e. junction or crossroads */) {
            if (passageExits(robot) == 0) {
                // exit opposite way to which it first entered the junction
                int originalDir = robotData.searchJunction(robot.getLocation().x, robot.getLocation().y); // temporary direction
                if (originalDir >= IRobot.SOUTH)
                    robot.setHeading(originalDir - 2); // anticlockwise round compass (done this way to make it more concise rather than going through all the compass directions)
                else
                    robot.setHeading(originalDir + 2); // clockwise
                direction = IRobot.AHEAD;
            } else {
                // there are passage exits so switch back to explorer mode and pick a random one
                direction = junction(robot, exits); /* The junction method will choose a random passage direction in this case. */
                explorerMode = 1;
            }
        } else if (exits == 2) {/* Corridor */
            direction = corridor(robot);
        } else direction = deadEnd(robot);
        robot.face(direction);
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

    // FOR JUNCTIONS AND CROSSROADS:
    private int junction(IRobot robot, int exits /* number of exits */) {
        int passageExits = passageExits(robot);
        if (passageExits == 0) {
            return 2000 + (int) (Math.random() * exits);
        } else {
            if (beenbeforeExits(robot) <= 1) /* if new junction */ {
                // store the data
                robotData.recordJunction(robot.getLocation().x, robot.getLocation().y, robot.getHeading());
            }
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

class RobotData1 {
    int[] juncX = new int[10000];
    int[] juncY = new int[10000];
    private int[] arrived = new int [10000]; // heading the robot first appeared from
    int junctionCounter = 0;

    public void recordJunction(int x, int y, int heading) {
        juncX[junctionCounter] = x; // store the x coordinate
        juncY[junctionCounter] = y; // store the y coordinate
        arrived[junctionCounter] = heading;
        junctionCounter++; // increment the counter
    }

    public void resetJunctionCounter() {
        junctionCounter = 0;
    }

    // get the direction the robot was traveling when it originally found this junction
    public int searchJunction(int x, int y) {
        // iterates from the most recent one to save time.
        for (int i = junctionCounter; i>=0; i--) {
            if (juncX[i] == x && juncY[i] == y) {
                return arrived[i];
            }
        }
        return -1; // just for compiler - in reality a heading from the arrived[] array will be returned.
    }

    public void printLastJunction() {
        // replace heading with words
        String heading;
        if (arrived[junctionCounter] == IRobot.NORTH) heading = "NORTH";
        else if (arrived[junctionCounter] == IRobot.EAST) heading = "EAST";
        else if (arrived[junctionCounter] == IRobot.SOUTH) heading = "SOUTH";
        else heading = "WEST";
        System.out.println("Junction " + junctionCounter + " (x=" + juncX[junctionCounter] + ",y=" + juncY[junctionCounter] + ") original heading: " + heading);
    }
}