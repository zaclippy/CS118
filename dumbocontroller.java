/*
* File: dumbocontroller.java
* Created: 17 September 2002, 00:34
* Author: Stephen Jarvis
*/

// javac -classpath maze-environment.jar dumbocontroller.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

/** @noinspection unused*/
public class dumbocontroller {

	public void controlRobot(IRobot robot) {

		// Get a random direction
		int direction = randomDirection();

		// If direction is towards a wall, keep choosing new direction until not
		while (robot.look(direction) == IRobot.WALL) {
			direction = randomDirection();
		}

		// Move
		robot.face(direction);
		System.out.println("moving " + logDirection(direction));
	}

	private int randomDirection() {
		// Select random number 0->3
		int randno = (int) Math.round(Math.random()*3);
		// Return a direction from the random number above:
		if (randno == 0) {
			return IRobot.LEFT;
		} else if (randno == 1) {
			return IRobot.RIGHT;
		} else if (randno == 2) {
			return IRobot.BEHIND;
		} else
		return IRobot.AHEAD;
	}

	private String logDirection(int direction) {
		switch (direction) {
			case IRobot.LEFT : return "left";
			case IRobot.RIGHT : return "right";
			case IRobot.BEHIND : return "backwards";
			case IRobot.AHEAD : return "forwards";
			default : return "\b\b\b\b\b\boops  ";
		};
	}
}