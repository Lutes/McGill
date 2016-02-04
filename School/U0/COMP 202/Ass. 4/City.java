import java.util.Random;

public class City {
	
//Array of prefixes	
	private static String[] namePrefixes = { "Chiguiro", "Maracas", "Raccoon",
		"Glass", "Iron", "Spring", "Winter", "Autumn", "Summer", "Godel",
		"Recursion", "Escher", "PowPow", "LOL", "Cheese", "MasterRoshi", "Pants", "Dork",
		"Cat", "Liszt", "Hysteria", "Cool", "Ennui", "Tortoise", "Mudkip", "Tonkatsu", 
		"Rainy", "Slump", "Tortilla", "Rodizio", "Ajiaco", "Sunny" };

//Array of Suffixes
	private static String[] nameSuffixes = { "ville", "vale", "_City", "town", "ton",
			"hill", "field", "land", "ia", "furt", "grad", "lia", "stadt", "stan" };

	//Class variable
	private String name;
	private Vector2D pos;
	private City[] neighbours; 
	
	private boolean explored = false;

	//Constructor form a random name and random posistion in grid
	public City() {
		int gridmax = 150;
		 name = namePrefixes [(int)(Math.random() * namePrefixes.length)] + nameSuffixes [(int)(Math.random() * nameSuffixes.length)];	
		 pos = new Vector2D ((int)(Math.random() *gridmax),(int)(Math.random() *gridmax));
	}
	
	//maxDist is the further distance apart two cities can be and still be connected.
	//Return the number of neighbours found
	//If there are i neighbours, then the last length-i spots of neighbours should be null
	//And the first i elements should be city objects
	public void setNeighbours(double maxDist, City[] cities)
	{
		int count = 0;
		neighbours = new City [cities.length -1]; 
		
		for (int i = 0; i < cities.length;i++){
		
			if (pos.distance(cities[i].getPos())  < maxDist && cities[i] != this){
				neighbours[count] = cities[i];
				count++;
			}
		
		}
	
	}
	
	//Searches to see which cities are connected to the current city.
	//If a city can be reached, its boolean 'explore' value will be true after this method is called
	//Otherwise, it will be false.
	public void explore() {
		explored = true;
		
		for (City v : neighbours){
			if (v != null && v.getExplored()){
				v.explore();
			}
		}
	}

	//Gets pos.
	public Vector2D getPos()
	{
		return pos;
	}

	//Gets name
	public String getName() {
		return name;
	}
	
	//Gets neighbours
	public City[] getNeighbours(){
		return neighbours;
	}

	//Gets explored
	public boolean getExplored() {
		return explored;
	}
}
	


