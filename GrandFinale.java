/*
* GrandFinale preamble:
*
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.awt.*;
import java.util.Stack;

public class GrandFinale {
    private int explorerMode = 1;  // 1 = explore, 0 = backtrack
    private int pollRun = 0;     // Incremented after each pass
    private RobotDataGF robotData; // Data store for junctions

    public void controlRobot(IRobot robot) {
        // when one away from the target, move toward the target and mark as finished
        if (oneSquareAwayFromTarget(robot)) {
            if (robot.getTargetLocation().x == robot.getLocation().x) {
                if (robot.getTargetLocation().y < robot.getLocation().y) robot.setHeading(IRobot.NORTH);
                else robot.setHeading(IRobot.SOUTH);
            } else {
                if (robot.getTargetLocation().x > robot.getLocation().x) robot.setHeading(IRobot.EAST);
                else robot.setHeading(IRobot.WEST);
            }
            System.out.println("target is one square " + RobotDataGF.directionToString(robot.getHeading()));
            robotData.finishedMaze();
            robot.face(IRobot.AHEAD);
        }

        if (robot.getRuns() == 0 || !robotData.hasReachedTarget) {
            if (pollRun == 0) {
                explorerMode = 1;
                robotData = new RobotDataGF(); //reset the data store (first move of first run)
            }
            if (explorerMode == 1) exploreControl(robot);
            else backtrackControl(robot);
        } else /* not first run */ {
            if (pollRun == 0) {
                System.out.println("in int form: saved route = " + robotData.savedRoute);
                System.out.print("Junction directions: [");
                for (int i = 0; i< robotData.savedRoute.size(); i++) System.out.print(RobotDataGF.directionToString(robotData.savedRoute.get(i)) + ", ");
                System.out.println("] \n");
            }
            if (nonWallExits(robot) == 1) robot.face(deadEnd(robot));
            else if (nonWallExits(robot) == 2) robot.face(corridor(robot));
            else {
                int dir = robotData.getSavedJunction();
                System.out.println("going " + RobotDataGF.directionToString(dir));
                robot.face(dir);
            }
        }
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
        int direction;
        if (exits > 2 /* i.e. junction or crossroads */) {
            if (passageExits(robot) == 0) {
                // exit opposite way to which it first entered the junction
                int originalDir = robotData.searchJunction();
                if (originalDir >= IRobot.SOUTH)
                    robot.setHeading(originalDir - 2); // anticlockwise round compass (done this way to make it more concise rather than going through all the compass directions)
                else
                    robot.setHeading(originalDir + 2); // clockwise
                System.out.println("backtracking " + RobotData2.directionToString(robot.getHeading()));
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
        // do not crash into walls and do not reverse direction and go back on itself
        // only one direction out of ahead, left and right ever satisfies these criteria, so go to whichever one is not a wall.
        if (robot.look(IRobot.AHEAD) != IRobot.WALL) {
            return IRobot.AHEAD;
        } else if (robot.look(IRobot.LEFT) != IRobot.WALL) {
            return IRobot.LEFT;
        } else return IRobot.RIGHT;
    }

    private boolean oneSquareAwayFromTarget(IRobot robot) {
        Point R = robot.getLocation();
        Point T = robot.getTargetLocation();
        // either the x's are the same and the y's are 1 apart, or the y's are the same and the x's are 1 apart
        return ((R.x == T.x) && (Math.abs(R.y - T.y) == 1)) || ((R.y == T.y) && (Math.abs(R.x - T.x) == 1));
    }

    // FOR JUNCTIONS AND CROSSROADS:
    private int junction(IRobot robot, int exits /* number of exits */) {
        int passageExits = passageExits(robot);
        int finalDir;
        if (passageExits == 0) {
            finalDir = 2000 + (int) (Math.random() * exits);
        } else {
            int[] passages = new int[passageExits];
            short i = 0;
            for (int direction = IRobot.AHEAD; direction <= IRobot.LEFT; direction++)
                if (robot.look(direction) == IRobot.PASSAGE) {
                    passages[i] = direction;
                    i++;
                }
            finalDir = passages[(int) (Math.random() * passageExits)];
        }
        // Store the data
        if (beenbeforeExits(robot) <= 1) /* if new junction */ {
            robotData.recordHeading(robot.getHeading());
            robotData.recordLeaving(finalDir);
        } else {
            robotData.replaceJunctionLeaving(finalDir, robot.getHeading());
        }
        // print the data and return the direction
        robotData.printLastJunctionHeading();
        return finalDir;
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
        System.out.println("target " + (robotData.hasReachedTarget ? "reached" : "not reached"));
        robotData.resetJunctionCounter();
        pollRun = 0;
    }


}

class RobotDataGF {
    Stack<Integer> arrived = new Stack<>(); // heading the robot first appeared from
    Stack<Integer> juncLeavingDirections = new Stack<>(); // heading the robot leaves each junction to
    Stack<Integer> savedRoute = new Stack<>(); // stack to save the route for use next time
    boolean hasReachedTarget = false;

    // coordinates no longer need to be stored, just the heading.
    public void recordHeading(int heading) {
        arrived.push(heading); // store the heading
    }

    public void recordLeaving(int leaving) {
        juncLeavingDirections.push(leaving);
    }

    public void saveDirections() {
        while (!juncLeavingDirections.empty()) {
            savedRoute.push(juncLeavingDirections.pop());
        }
    }


    public void replaceJunctionLeaving(int newleavingdirection, int currentheading) {
        /* The leaving direction will be given in relation to the current heading of the robot.
        * Before it is pushed to the stack it must be converted to the direction it would have been in relation to the original heading.
        * This is done by adding the difference between the new heading and the original heading. */
        int originalheading = arrived.peek();
        int dir = (currentheading - originalheading) + newleavingdirection;
        // The next line will correct any of the directions >LEFT or <AHEAD
        if (dir > 2003) {
            System.out.print("(taken away 4 from " + dir);
            dir -= 4;
            System.out.println("to get " + dir + ")");
        } else if (dir < 2000) { dir += 4; }
        juncLeavingDirections.pop();
        juncLeavingDirections.push(dir);
    }

    // take the most recent junction
    public int searchJunction() {
        // can be done with just popping it, since we have just backtracked to the most recent junction we don't need to search for it
        System.out.print("From junction " + arrived.size());
        juncLeavingDirections.pop();
        return arrived.pop();
    }

    public void printLastJunctionHeading() {
        // replace heading with words
        String heading = directionToString(arrived.peek());
        System.out.println("Junction " + arrived.size() + " original heading: " + heading + " ; left junction : " + directionToString(juncLeavingDirections.peek()));
    }

    public int getSavedJunction(){
        // add the most recent saved junction back to the juncLeavingDirections so the route will work in subsequent runs
        int savedJunc = savedRoute.pop();
        juncLeavingDirections.push(savedJunc);
        return savedJunc;
    }

    public static String directionToString(int direction) {
        switch (direction) {
            case IRobot.NORTH: return "NORTH";
            case IRobot.EAST: return "EAST";
            case IRobot.SOUTH: return "SOUTH";
            case IRobot.WEST: return "WEST";
            case IRobot.AHEAD: return "AHEAD";
            case IRobot.BEHIND: return "BEHIND";
            case IRobot.RIGHT: return "RIGHT";
            case IRobot.LEFT: return "LEFT";
            default: return Integer.toString(direction);
        }
    }

    public void finishedMaze() {
        hasReachedTarget = true;
    }

    public void resetJunctionCounter() {
        arrived.clear();
        saveDirections();
        if (hasReachedTarget) {
            System.out.println("robot reset without finishing");
            juncLeavingDirections.clear();
        } else System.out.println("remembering route... \n");
    }
}
