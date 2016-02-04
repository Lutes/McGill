public class Question2 {
	public static void main(String[] args) {
		// Variable are initialized max_size refers to the max size of the axis,
		// Coor is used to alter the axis orientation.
		int max_size = 21, Coor = 10;
		// LA and LB refer to the slope and y intercept respectively.
		double LA = 1, LB = 2;
		// PA PB and PC refer to the various modifiers of a parabola.
		double PA = 0.3, PB = 1, PC = -4;

		// The nested for loops are used to count across 20X20 grid
		for (int j = 0; j < max_size; j++) {
			for (int i = 0; i < max_size; i++) {

				// if DrawAxis() isnt returning a blank string then
				// DrawAxis(i,j) is called
				if (DrawAxis(i, j) != "") {
					System.out.print(DrawAxis(i, j));
				}
				// if DrawLine() isnt returning a blank string then DrawLine(i -
				// Coor, Coor - j, LA, LB) is called
				else if (DrawLine(i - Coor, Coor - j, LA, LB) != "") {
					System.out.print(DrawLine(i - Coor, Coor - j, LA, LB));
				}
				// if DrawParabola() isnt returning a blank string then
				// DrawParabolaF(i - Coor, Coor - j, LA, LB) is called
				else if (DrawParabola(i - Coor, Coor - j, PA, PB, PC) != "") {
					System.out.print(DrawParabola(i - Coor, Coor - j, PA, PB,
							PC));
				} else {
					System.out.print("");
				}

			}
			// When a full line of 20 char is printed a new line is started
			System.out.println("");
		}

	}

	// Draws the Axis, the order in which the if statements are placed is based
	// on precedence of character
	public static String DrawAxis(int i, int j) {
		// a dot on the (0,0)
		if (i == 10 && j == 10) {
			return ".";
		} 
		// Arrow up on top of y axis
		else if (i == 10 && j == 0) {
			return "^";
		} 
		// An arrow out on the x axis
		else if (i == 20 && j == 10) {
			return ">";
		} 
		//This forms the y axis
		else if (i == 10) {
			return "|";
		}
		//this forms the x axis.
		else if (j == 10) {
			return "-";
		}
		return "";

	}

	// the linear equation is evaluated and stored in lineY, then if the y value
	// (j) is within a threshold of 0.4 then a "*" is returned and printed.
	public static String DrawLine(int i, int j, double a, double b) {
		//Variables are given values
		double LineY = (a * (i) + b);
		double thick = 0.2;
		if (LineY - thick < j && j < LineY + thick) {
			return ("*");
		} else {
			return ("");
		}
	}

	// The quadratic equation is evaluated and stored in ParabolaY. Then if the
	// Y value (j) is equivalent to ParabolaY within a threshold of 3.8 a "#" is
	// returned to be printed
	public static String DrawParabola(int i, int j, double a, double b, double c) {
		//Variables are given values
		double ParabolaY = ((Math.pow(i, 2) * (a)) + i * (b) + (c));
		double thick = 1.9;
		if (ParabolaY - thick < j && j < ParabolaY + thick) {
			return ("#");
		}
		return " ";

	}
}
