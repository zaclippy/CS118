package Coursework1;/*
 * The robot does not always reach the target. However, it will always move in a direction which brings it closer to the target.
 * If possible, it moves in any direction which brings it closer to the target, as long as this direction doesn't lead it into a wall
 * and if there are two such directions, or no such direction then it should choose out of the available directions at random.
 * The robot would be improved if only homed towards the target once a junction was reached, since now it just moves back and forth in some corridors to try to move closer to the target.
 * I have done the final heading controller by making arrays with the directions towards the target (which should always be two) and the possible directions the robot can move in without hitting a wall (can be 1-4 of these)
 * This makes it easier to compare them and choose a random direction from the elements that match by looping through the two arrays.
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.util.ArrayList;

public class C1Ex3 {
    // Main method to control the robot. Sets the robot to head in the direction chosen by the heading controller and moves ahead.
    public void controlRobot(IRobot robot) {
        int heading = headingController(robot);
        ControlTest.test(heading, robot);
        robot.setHeading(heading);
        robot.face(IRobot.AHEAD);/* Face the direction */
    }

    public void reset() {
        ControlTest.printResults();
    }
    
    private byte isTargetNorth(IRobot robot) {
        // check if y coordinate of target is less (i.e. NORTH) than the robot (less because starts from 1,1 at north-west.)
        if(robot.getTargetLocation().y < robot.getLocation().y) {
            return 1; // yes
        } else if (robot.getTargetLocation().y > robot.getLocation().y) {
            return -1; // no
        } else {
            return 0; // same y coordinate
        }
    }

    private byte isTargetEast(IRobot robot) {
        // check if x coordinate of target is greater (i.e. EAST) than the robot
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
        // Adding 1000 changes NORTH to AHEAD, EAST to RIGHT etc.
        // returns whatever type of square is in the parameter direction.
        return robot.look(direction+1000);
    }

    private int headingController(IRobot robot) {
        int noOfDirections = 0;
        // Make an array of to represent the possible directions [N, E, S, W] with 0 for wall.
        int[] allDirections = new int[4];
        // Look in all directions and store in this array.
        for (byte i = 0; i < 4; i++) {
            if (lookHeading(i + 1000, robot) != IRobot.WALL){
                noOfDirections += 1;
                // i+1000 represents the compass directions (1001 is IRobot.EAST)
                allDirections[i] = i+1000;
            }
        }
        // array with just the possible directions
        int[] possDirections = new int[noOfDirections];
        int i = 0;
        // put all the available directions in this array
        for (int dir:allDirections) {
            if (dir != 0) {
                possDirections[i] = dir;
                i++;
            }
        }
        // Make an array of  to represent the target directions [N, E, S, W] with direction if the target is in that direction.
        int[] targetDirections = new int[4];
        if (isTargetNorth(robot) == 1) targetDirections[0] = IRobot.NORTH;
        else if (isTargetNorth(robot) == -1) targetDirections[2] = IRobot.SOUTH;
        if (isTargetEast(robot) == 1) targetDirections[1] = IRobot.EAST;
        else if (isTargetEast(robot) == -1) targetDirections[3] = IRobot.WEST;

        // if there is only one available direction then go that direction
        if (noOfDirections == 1)
            return possDirections[0];
        else {
            // Make an array list which shows all directions which both head towards the target and aren't walls (maximum size is 2)
            ArrayList<Integer> availableTargetDirections = new ArrayList<Integer>();
            for (byte j=0; j<4; j++) {
                if (targetDirections[j]!= 0 && allDirections[j] !=0) {
                    availableTargetDirections.add(targetDirections[j]);
                };
            }
            // if true then the robot can go in either of the directions towards the target, choosing at random
            if (availableTargetDirections.size() == 2)
                return availableTargetDirections.get((int) (Math.random() * noOfDirections));
            // if there is one non-wall direction towards the target, just move towards it
            else if (availableTargetDirections.size() == 1)
                return availableTargetDirections.get(0);
            // otherwise, just move in a random direction which isn't a wall.
            else
                return possDirections[(int) (Math.random() * noOfDirections)];
        }
    }
}
