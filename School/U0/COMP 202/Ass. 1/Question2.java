
import java.util.Scanner;

public class Question2 {
		 
	public static void main(String[] args){
			
		 //Scanner is initialized and prompt is given to user. 
		 Scanner in=new Scanner(System.in);
		 System.out.println("Hal: How many laws of robotics are there according to Isaac Asimov?");
		 
		 //Input is saved as an int and scanner is closed.
		 int Laws=in.nextInt(); 
		 in.close();
			 
		 	//Checks if the user input input the proper number. 
		 	if (Laws == 2){
	 			//Output if user is correct
				System.out.println("Hal: Indeed there are 2 laws of robotics.");
		 	}
			 	
			 else {
				//Output if user is not correct
			 	System.out.println("Hal: There are not "+Laws+" laws of robotics. There are 2 laws of robotics.");
			 }
			 
		 }
	}


