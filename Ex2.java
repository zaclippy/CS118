/*
* Here the robot carries on going straight until it is in front of a wall, and then it chooses a random directon from all those directions which are not a wall.
* but this means it will sometimes move between two dead ends forever, so there is a 1/8 chance of choosing a random direction instead of just going forward every time.
* This is implemented by choosing a random integer from 0 to 7 each time and if that number is 1 (1/8 chance of this) then it follows the else statement instead and chooses a random direction/
*/

// javac -classpath maze-environment.jar Ex2.java
// java -jar maze-environment.jar &

import uk.ac.warwick.dcs.maze.logic.IRobot;

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
		switch (direction) {
			case IRobot.LEFT : return "left";
			case IRobot.RIGHT : return "right";
			case IRobot.BEHIND : return "backwards";
			case IRobot.AHEAD : return "forwards";
			default : return "\b\b\b\b\b\boops  ";
		}
	}

	private String logSquareType(int walls) {
		switch(walls) {
			case 0 : return "crossroads";
			case 1 : return "junction";
			case 2 : return "corridor";
			case 3 : return "dead end";
			default : return "?";
		}
	}
}