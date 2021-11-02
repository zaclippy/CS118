/*
* Here the robot carries on going straight until it is in front of a wall, and then it chooses a random directon from all those directions which are not a wall.
* but this means it will sometimes move between two dead ends forever, so there is a 1/8 chance of choosing a random direction instead of just going forward every time.
* This is implemented by choosing a random integer from 0 to 7 each time and if that number is 1 (1/8 chance of this) then it follows the else statement instead and chooses a random direction
* I did it this way to keep it simple - so that no extra code had to be added apart from a modification to the if condition.
* Even probabilities of random numbers are done by casting the random float to an int rather than rounding. This will always round it down, so it will give equal probabilities for each number within the range
* (The problem with the initial Math.round was that 0 is only rounded to if the random float is between 0 and 0.5, but 1 is given for any float 0.5 to 1.5 so 1 is more likely to appear than 0)
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

/** @noinspection unused*/
public class Ex2 {

	public void controlRobot(IRobot robot) {
		int walls = 0; // to store the number of walls
		boolean[] wallArr = new boolean[4]; // array with all 4 directions (clockwise) relative to the robot storing true if there is a wall in that direction
		int direction;
		// If there is no wall ahead, has a 7/8 chance of moving ahead
		int random8 = (int) (Math.random() * 8);
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

			// Create a new array to hold all the directions without a wall (so the ones which will not cause a collision)s
			int[] possDirections = new int[4-walls];
			int dirCount = 0;
			// here it loops through the wall array and add all directions which are not a wall to the new array
			for (int i = 0; i<4; i++){
				if (wallArr[i] == false) {
					possDirections[dirCount] = 2000+i;
					dirCount+=1;
				}
			}
			// Choose one of the possible directions at random. not rounded since rounding gives uneven probability. By converting from float to an integer it gives equal probability of every direction.
			direction = possDirections[(int) (Math.random() * dirCount)];
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
			default : return "forward";
		}
	}

	private String logSquareType(int walls) {
		switch(walls) {
			case 0 : return "crossroads";
			case 1 : return "junction";
			case 2 : return "corridor";
			default : return "dead end";
		}
	}
}