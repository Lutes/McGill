import java.util.Scanner;

public class Question1 {

	public static void main(String[] args) {
		// Prompts user to input to input binary number and calls GetInput()
		System.out.print("Enter a binary number:");
		String input = GetInput();

		// if CheckInputCorrect(input) is false then the user is asked to re
		// input a number

		while (!CheckInputCorrect(input)) {
			System.out.println(input + " is not a binary number");
			System.out.print("Enter a binary number:");
			input = GetInput();
		}

		// Once it is verified that the number that was input was binary the
		// input is stored in the variable numberInput. Then BinaryToNumber() is
		// called.
		String numberInput = input;
		// BinaryToNumber(numberInput);
		System.out.println("The base ten form of " + input + " is "
				+ BinaryToNumber(numberInput));

	}

	// This method is called in order to get input from the user
	// The method creates a scanner and takes in an input which is returned by
	// the method
	public static String GetInput() {
		// Scanner is created and a value is set for input
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		return input;

	}

	// This method checks to see if the input is a binary number, it does this
	// by checking each char in input to see if it is a 1 or 0, if it is "true"
	// is returned. As soon as a non 1 or 0 is reconized a false is returned.
	public static boolean CheckInputCorrect(String input) {
		boolean binary_tautology = false;

		// every char is passed through to evaluate if it is a 1 or 0.
		for (int i = 0; i < input.length(); i++) {
			int z = Character.digit(input.charAt(i), 10);

			if ((z == 0) || (z == 1)) {
				binary_tautology = true;
			} else {
				return false;
			}
		}
		return binary_tautology;
	}

	// This method converts the binary number to the base 10 number. It does so
	// by utilizing the property of binary numbers. An index i is created and
	// set to zero, and a index j is created and set to the length of the
	// string. If there is a 1 in place value n then temp is assigned the value
	// (2^(n-1)). binary_number is then set to binary_number + temp. Finally
	// once every char has passed through the if statement to method returns x
	// which is passed its value from binary_number
	public static int BinaryToNumber(String numberInput) {

		//Varibales are given values.
		double temp = 0, binary_number = 0;
		int j = numberInput.length();
		int i = 0;
		
		//Every Char is cycled through
		while (i < numberInput.length()) {
			int z = Character.digit(numberInput.charAt(i), 10);

			//If there is a 1 it is translated into a base 10 value
			if (z == 1) {
				temp = Math.pow(2, j - 1.0);
				binary_number += temp;
			}

			i++;
			j--;
		}
		//the final base 10 value is passed into x and returned. 
		int x = (int) binary_number;
		return x;
	}
}