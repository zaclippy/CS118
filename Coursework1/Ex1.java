/*
* This robot avoids hitting a wall by choosing a direction at random, then looks in that direction and keeps on choosing until there is no wall in that direction, using a do-while loop to avoid the extra two lines of code.
* It then looks in all directions finding out how many walls there are, and prints out which direction the robot is going in as well as whether it is a dead-end, corridor, junction or crossroads from the number of walls
* Then it moves in the chosen random direction.
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex1 {

	public void controlRobot(IRobot robot) {
		int walls = 0;
		int direction;

		// If direction is towards a wall, keep choosing new direction until there is not going to be a collision
		do {
			// random direction is chosen in a separate function randomDirection()
			direction = randomDirection();
		} while (robot.look(direction) == IRobot.WALL);

		// For loop to count how many walls there are by checking every direction clockwise.
		for (int i = 0; i<4; i++){
			if (robot.look(IRobot.AHEAD + i) == IRobot.WALL) {
				walls += 1;
			}
		}

		// Move in the direction and prints the direction the robot is going in.
		System.out.println("I'm going " + logDirection(direction) + " at " + logSquareType(walls));
		robot.face(direction);
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

	// Function to return a string which describes the direction in the direction parameter
	private String logDirection(int direction) {
		switch (direction) {
			case IRobot.LEFT : return "left";
			case IRobot.RIGHT : return "right";
			case IRobot.BEHIND : return "backwards";
			default: return "forward";
		}
	}

	// Function to return whether the robot is at a dead-end, in a corridor, at a junction or at a crossroads from the number of walls around it.
	private String logSquareType(int walls) {
		switch(walls) {
			case 0 : return "crossroads";
			case 1 : return "junction";
			case 2 : return "corridor";
			default : return "dead end";
		}
	}
}