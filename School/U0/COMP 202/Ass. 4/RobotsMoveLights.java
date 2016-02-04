import java.awt.Color;
import becker.robots.*;

public class RobotsMoveLights {
	// Archibald Haddock (123456789)
	// COMP-202B, Section 0 (Winter 2010)
	// Instructor: Cuthbert Calculus
	// Assignment 4, Question 2

	// This method picks up flashers and places them in the diagonal.
	public static void moveToDiagonal(Robot robot) {
		getFlashers(robot);
		placeFlashers(robot);
	}

	// This Method turns the robot around by turning left twice.
	public static void turnAround(Robot robot) {
		robot.turnLeft();
		robot.turnLeft();
	}

	// This method turns the robot right by turning left and then turning
	// around.
	public static void turnRight(Robot robot) {
		turnAround(robot);
		robot.turnLeft();
	}

	// This method returns the robot to the (1,1);
	public static void Orgin(Robot robot) {
		// Finds the address of the robot/
		int street = robot.getStreet();
		int avenue = robot.getAvenue();
		// If there is something in the bag
		if (robot.countThingsInBackpack() != 0) {
			// Series of If statements use to turn the robot facing West.
			if (robot.getDirection() == Direction.SOUTH) {
				turnRight(robot);
			}
			if (robot.getDirection() == Direction.EAST) {
				turnAround(robot);
			}
			if (robot.getDirection() == Direction.NORTH) {
				robot.turnLeft();
			}

			// While they are not on avenue one, they move towards it until
			// they arrive.
			for (int i = avenue; i > 1; i--) {
				robot.move();
			}
		}

		// If there is something in the bag
		if (robot.countThingsInBackpack() != 0) {
			// Series of If statements use to turn the robot facing North.
			if (robot.getDirection() == Direction.SOUTH) {
				turnAround(robot);
			}
			if (robot.getDirection() == Direction.EAST) {
				robot.turnLeft();
			}
			if (robot.getDirection() == Direction.WEST) {
				turnRight(robot);
			}
			
			// While they are not on Street one, they move towards it until
			// they arrive.
			for (int i = street; i > 1; i--) {
				robot.move();
			}
			robot.turnLeft();
		}

		//Always leaves the robot facing East. 
		turnAround(robot);
	}

	// This method picks up the flashers from the street.
	public static void getFlashers(Robot robot) {
		boolean thing = true;
		robot.move();
		// while there is still a flasher to pick up, the robot picks up the
		// flasher and moves to the next position.
		while (thing) {
			if (robot.canPickThing()) {
				robot.pickThing();
				robot.move();
			} else {
				thing = false;
			}
		}
		// the robot returns to the point (1,1)
		Orgin(robot);
	}
	
	//Places flashers in diagonal structure.
	// This method places the flashers in a diagonal structure.
	public static void placeFlashers(Robot robot) {
		// Counts the number of items in backpack.
		int Flashers = robot.countThingsInBackpack();
		// This for loop places a flasher then moves to the position diagonal to
		// its position until there are no more flashers to put down.
		for (int i = Flashers; i > 0; i--) {
			robot.putThing();
			robot.move();
			turnRight(robot);
			robot.move();
			robot.turnLeft();

		}
	}

	//Main method
	public static void main(String[] args) {
		
		final int LIGHT_STREET = 1;
		final int LIGHT_AVENUE = 1;
		final int NUMBER_FLASHERS = 0;

		City montreal = new City(12, 12);

		Robot asimo = new Robot(montreal, LIGHT_STREET, LIGHT_AVENUE - 1,
				Direction.EAST);

		for (int i = 0; i < NUMBER_FLASHERS; i++) {
			new Flasher(montreal, LIGHT_STREET, LIGHT_AVENUE + i, true);
		}

		moveToDiagonal(asimo);
	}

}
