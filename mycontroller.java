/*
* File: dumbocontroller.java
* Created: 17 September 2002, 00:34
* Author: Stephen Jarvis
*/

// javac -classpath maze-environment.jar dumbocontroller.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

/** @noinspection unused*/
public class mycontroller {

	public void controlRobot(IRobot robot) {
		int walls = 0;
		boolean[] wallArr = new boolean[4];
		int direction;
		// Clockwise array from Ahead to Left
		for (int i = 0; i<4; i++){
			int look = robot.look(i+2000);
			// The first if statement forces movement in undiscovered direction.
			if (look == IRobot.PASSAGE) {
				robot.face(i+2000);
				System.out.println("moving " + logDirection(i+2000));
				return;
			} else if (robot.look(i+2000) == IRobot.WALL) {
				walls += 1;
				wallArr[i] = true;
			} else wallArr[i] = false;
		}
		int[] possDirections = new int[4-walls];
		int dirCount = 0;

		// Add all the directions without a wall to 'possible 	directions' array
		for (int i = 0; i<4; i++){
			if (wallArr[i] == false) {
				possDirections[dirCount] = 2000+i;
				dirCount+=1;
			}
		}

		direction = possDirections[(int)Math.floor(Math.random()*dirCount)];

		// Move
		robot.face(direction);
		System.out.println("I'm going " + logDirection(direction) + "at " + logSquareType(walls));

	}

	private String logDirection(int direction) {
        switch (direction) {
        case IRobot.LEFT:
            return "left";
        case IRobot.RIGHT:
            return "right";
        case IRobot.BEHIND:
            return "backwards";
        case IRobot.AHEAD:
            return "forwards";
        default:
            return "\b\b\b\b\b\boops  ";
        }
    }

    private String logSquareType(int walls) {
        switch (walls) {
        case 0:
            return "crossroads";
        case 1:
            return "junction";
        case 2:
            return "corridor";
        case 3:
            return "dead end";
        default:
            return "?";
        }
    }
}