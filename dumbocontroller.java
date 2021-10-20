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

		int randno, direction;

		// Select a random number
		randno = (int) Math.round(Math.random()*3);

		// Convert this to a direction
		direction = switch (randno) {
			case 0 -> IRobot.LEFT;
			case 1 -> IRobot.RIGHT;
			case 2 -> IRobot.BEHIND;
			default -> IRobot.AHEAD;
		};

		robot.face(direction); /* Face the robot in this direction */ 
	}

}