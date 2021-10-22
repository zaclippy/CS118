/*
* To avoid collisions, the robot looks around at all 4 surrounding squares first.
* It chooses a random direction from the adjacent squares which are not walls.
* This is to avoid having to keep choosing a random direction until it is not a wall. Only one random number has to be generated.
*/

// javac -classpath maze-environment.jar dumbocontroller.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.util.Arrays;

/** @noinspection unused*/
public class Ex2 {

	public void controlRobot(IRobot robot) {
		int walls = 0;
		boolean[] wallArr = new boolean[4];
		int direction;
		// If there is no wall ahead, has a 7/8 chance of moving ahead
		int random8 = (int) Math.floor(Math.random() * 8);
		if (robot.look(IRobot.AHEAD) != IRobot.WALL && random8 != 1) {
			direction = IRobot.AHEAD;
		} else {
			for (int i = 0; i<4; i++){
				// The first if statement forces movement in undiscovered direction.
				if (robot.look(i+2000) == IRobot.WALL) {
					walls += 1;
					wallArr[i] = true;
				} else wallArr[i] = false;
			}

			// Add all the directions without a wall to an array
			int[] possDirections = new int[4-walls];
			int dirCount = 0;
			for (int i = 0; i<4; i++){
				if (wallArr[i] == false) {
					possDirections[dirCount] = 2000+i;
					dirCount+=1;
				}
			}
			// Choose one of the possible directions at random
			direction = possDirections[(int)Math.floor(Math.random()*dirCount)];
		}
		// Move
		robot.face(direction);
		System.out.println("I'm going " + logDirection(direction) + " at " + logSquareType(walls));
	}

	private String logDirection(int direction) {
		return switch (direction) {
			case IRobot.LEFT -> "left";
			case IRobot.RIGHT -> "right";
			case IRobot.BEHIND -> "backwards";
			case IRobot.AHEAD -> "forwards";
			default -> "\b\b\b\b\b\boops  ";
		};
	}

	private String logSquareType(int walls) {
		return switch(walls) {
			case 0 -> "crossroads";
			case 1 -> "junction";
			case 2 -> "corridor";
			case 3 -> "dead end";
			default -> "?";
		};
	}
}