public class Vector2D {

	//Creates two variables
	private double yVal;
	private double xVal;
	
	//Constructor that creates a  2D vector
	public Vector2D(double posx, double posy) {
		xVal = posx;
		yVal = posy;
	}
	
	//Calculates the distance between two differents variables
	double distance(Vector2D v) {
		double dis = (Math.sqrt((Math.pow(xVal - v.getX(), 2) + Math.pow(yVal - v.getY(), 2))));
		return dis;
	}

	//Gets the x value
	double getX() {
		return xVal;
	}
	//Gets the y value
	double getY(){
		return yVal;
	}
}
