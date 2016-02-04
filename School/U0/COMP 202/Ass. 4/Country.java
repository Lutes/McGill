public class Country {

	// Creates all varibles for the class
	private String name;
	private City[] citties;
	private boolean connected;

	// Contrstuctor takes the number of citties the max distance bewtween the
	// citties and the name of the country. The constructor creates array of
	// type City and fills it with random cities. Sets neighbours for each city.
	public Country(int n, int maxDist, String title) {

		//Sets values to class variables.
		name = title;
		citties = new City[n];

		//creates lists of citties
		for (int i = 0; i < n; i++) {
			citties[i] = new City();
		}

		//Creates a lits of neighbours
		for (int j = 0; j < n; j++) {
			citties[j].setNeighbours(maxDist, citties);
		}

	}

	// Checks to see the connectivity of every City, by iterating through each
	// City and using the explore method.
	public boolean setConnectivity() {

		for (int i = 0; i < citties.length; i++) {
			citties[0].explore();
			if (!citties[i].getExplored()) {
				connected = false;
			}

			else {
				connected = true;
				return connected;
			}

		}
		return connected;
	}

	//Gets citties;
	
	public City[] getCities() {
		return citties;
	}

	//Gets names
	public String getName() {
		return name;
	}

}
