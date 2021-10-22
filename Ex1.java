/*
* File: dumbocontroller.java
* Created: 17 September 2002, 00:34
* Author: Stephen Jarvis
*/

// javac -classpath maze-environment.jar dumbocontroller.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

/** @noinspection unused*/
public class Ex1 {

	public void controlRobot(IRobot robot) {
		int walls = 0;
		// Get a random direction
		int direction = randomDirection();

		// If direction is towards a wall, keep choosing new direction until not
		while (robot.look(direction) == IRobot.WALL) {
			direction = randomDirection();
		}

		for (int i = 0; i<4; i++){
			if (robot.look(i+2000) == IRobot.WALL) {
				walls += 1;
			}
		}

		// Move
		robot.face(direction);
		System.out.println("I'm going " + logDirection(direction) + " " + logSquareType(walls));
	}

	private int randomDirection() {
		// Select random number 0->3
		int randno = (int) Math.round(Math.random()*3);
		// Return a direction from the random number above:
		if (randno == 0) return IRobot.LEFT;
		else if (randno == 1) return IRobot.RIGHT;
		else if (randno == 2) return IRobot.BEHIND;
		else return IRobot.AHEAD;
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
			case 0 -> "at a crossroads";
			case 1 -> "at a junction";
			case 2 -> "down a corridor";
			case 3 -> "at a dead end";
			default -> "?";
		};
	}
}