package suduko;

import java.util.*;
import java.io.*;

class Sudoku {
	/*
	 * SIZE is the size parameter of the Sudoku puzzle, and N is the square of
	 * the size. For a standard Sudoku puzzle, SIZE is 3 and N is 9.
	 */
	int SIZE, N;

	/*
	 * The grid contains all the numbers in the Sudoku puzzle. Numbers which
	 * have not yet been revealed are stored as 0.
	 */
	int Grid[][];
	int VGrid[][][];
	int unsolved;

	/*
	 * This program basically search for solution by looking for solo candidates
	 * - ie places where only one number can go. and looking for unique
	 * candidates - ie when there is only one possible solution because of the
	 * candidates in other squares. If these two methods dont work then simple
	 * recursive backtracking is used to brute force the puzzle
	 * 
	 */
	
	public void solve() {
		// calculate the number of unsolved squares
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (Grid[i][j] != 0) {
					unsolved--;
				}
			}
		}
		int temp = 0;

		// While there are unsolved squares
		while (unsolved > 0) {
			// if progress was made
			if (temp != unsolved) {
				temp = unsolved;
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (Grid[i][j] == 0) {
							int sol = getNumber(i, j);
							if (sol != 0) {
								Grid[i][j] = sol;
							}
							if (sol == 0) {
								Grid[i][j] = onlyOne(i, j);
							}
						}
						if (Grid[i][j] != 0) {
							for (int k = 0; k < N; k++) {
								VGrid[i][j][k] = 1;
							}
						}
					}
				}

			}
			// If no progress was made
			else {
				// Run recursive backtracking
				rsolve(Grid);
				// show that puzzle was solved
				unsolved = 0;

			}
		}
	}

	// This method determines possible candidates for each cell
	int getNumber(int r, int c) {
		// look through row for nots
		for (int i = 0; i < N; i++) {
			if (Grid[i][c] != 0) {
				VGrid[r][c][Grid[i][c] - 1] = 1;
			}
		}

		// looks through columns for nots
		for (int i = 0; i < N; i++) {
			if (Grid[r][i] != 0) {
				VGrid[r][c][Grid[r][i] - 1] = 1;
			}
		}

		// look through square for nots
		int x = (int) Math.floor((double) (r / SIZE)) * SIZE;
		int y = (int) Math.floor((double) (c / SIZE)) * SIZE;
		for (int j = x; j < SIZE + x; j++) {
			for (int k = y; k < SIZE + y; k++) {
				if (Grid[j][k] != 0)
					VGrid[r][c][Grid[j][k] - 1] = 1;
			}
		}
		// This returns a value if there is a possible solo candidate
		int a = 0;
		int index = 0;
		// cycle through grid
		for (int i = 0; i < N; i++) {
			if (VGrid[r][c][i] == 0) {
				a++;
				index = i + 1;
			}
			// if by the end there is only one candidate then assign value
			if (a == 1 && i == N - 1) {
				System.out.println("Assign:" + index + "(" + r + "," + c + ")");
				unsolved--;
				return index;
			}

		}

		return 0;
	}



	// Recursive backtracking brain
	boolean rsolve(int[][] a) {
		// cycle through through sudoku
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				// if the space is full then continue
				if (a[i][j] != 0) {
					continue;
				}
				// Cycle through possible values
				for (int number = 1; number <= N; number++) {
					// Is the the value valid
					if (isValid(number, i, j, a)) {
						// assign number if it valid
						a[i][j] = number;
						// recurse
						if (rsolve(a)) {
							return true;
						} else {
							a[i][j] = 0;
						}
					}
				}
				return false;
			}
		}
		return true;
	}

	// Checks if number is valid
	boolean isValid(int number, int i, int j, int[][] a) {
		// Cycles through rows and columns
		for (int z = 0; z < N; z++) {
			if (number == a[i][z])
				return false;
			if (number == a[z][j])
				return false;
		}

		// Checks the square to see if value is valid
		int x = (int) Math.floor((double) (i / SIZE)) * SIZE;
		int y = (int) Math.floor((double) (j / SIZE)) * SIZE;
		for (int h = x; h < SIZE + x; h++) {
			for (int k = y; k < SIZE + y; k++) {
				if (a[h][k] == number)
					return false;
			}
		}

		// if all the conditions are satisfied then number is valid
		return true;
	}

	// looks for unique candidate
	int onlyOne(int r, int c) {
		for (int i = 0; i < N; i++) {
			if (VGrid[r][c][i] == 0) {
				int cr = 0;
				int cc = 0;
				int cs = 0;
				for (int a = 0; a < N; a++) {
					// check row
					if (VGrid[r][a][i] == 0)
						cr++;

					// check column
					if (VGrid[a][c][i] == 0)
						cc++;

				}
				// if solvable then assign value
				if (cr == 1) {
					unsolved--;
					System.out.printf("Assign:%d (%d ,%d) \n", i + 1, r, c);
					return i + 1;
				}
				if (cc == 1) {
					unsolved--;
					System.out.printf("Assign:%d (%d ,%d) \n", i + 1, r, c);
					return i + 1;
				}

				// check Square
				int x = (int) Math.floor((double) (r / SIZE)) * SIZE;
				int y = (int) Math.floor((double) (c / SIZE)) * SIZE;
				for (int j = x; j < SIZE + x; j++) {
					for (int k = y; k < SIZE + y; k++) {
						if (VGrid[j][k][i] == 0) {
							cs++;
						}
					}
				}
				// if solvable then assign value
				if (cs == 1) {
					unsolved--;
					System.out.printf("Assign:%d (%d ,%d) \n", i + 1, r, c);
					return i + 1;
				}
			}
		}
		// Keep value zero if no solution found
		return 0;
	}

	/*****************************************************************************/
	/*
	 * NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE FUNCTIONS BELOW THIS LINE.
	 */
	/*****************************************************************************/

	/*
	 * Default constructor. This will initialize all positions to the default 0
	 * value. Use the read() function to load the Sudoku puzzle from a file or
	 * the standard input.
	 */
	public Sudoku(int size) {
		SIZE = size;
		N = size * size;
		unsolved = N * N;
		Grid = new int[N][N];
		VGrid = new int[N][N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				Grid[i][j] = 0;
				for (int k = 0; k < N; k++) {
					VGrid[i][j][k] = 0;
				}
			}

	}

	/*
	 * readInteger is a helper function for the reading of the input file. It
	 * reads words until it finds one that represents an integer. For
	 * convenience, it will also recognize the string "x" as equivalent to "0".
	 */
	static int readInteger(InputStream in) throws Exception {
		int result = 0;
		boolean success = false;

		while (!success) {
			String word = readWord(in);

			try {
				result = Integer.parseInt(word);
				success = true;
			} catch (Exception e) {
				// Convert 'x' words into 0's
				if (word.compareTo("x") == 0) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}

		return result;
	}

	/*
	 * readWord is a helper function that reads a word separated by white space.
	 */
	static String readWord(InputStream in) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while (whiteSpace.indexOf(currentChar) > -1) {
			currentChar = in.read();
		}

		// Read all characters until you reach white space
		while (whiteSpace.indexOf(currentChar) == -1) {
			result.append((char) currentChar);
			currentChar = in.read();
		}
		return result.toString();
	}

	/*
	 * This function reads a Sudoku puzzle from the input stream in. The Sudoku
	 * grid is filled in one row at at time, from left to right. All non-valid
	 * characters are ignored by this function and may be used in the Sudoku
	 * file to increase its legibility.
	 */
	public void read(InputStream in) throws Exception {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Grid[i][j] = readInteger(in);
			}
		}
	}

	/*
	 * Helper function for the printing of Sudoku puzzle. This function will
	 * print out text, preceded by enough ' ' characters to make sure that the
	 * printint out takes at least width characters.
	 */
	void printFixedWidth(String text, int width) {
		for (int i = 0; i < width - text.length(); i++)
			System.out.print(" ");
		System.out.print(text);
	}

	/*
	 * The print() function outputs the Sudoku grid to the standard output,
	 * using a bit of extra formatting to make the result clearly readable.
	 */
	public void print() {
		// Compute the number of digits necessary to print out each number in
		// the Sudoku puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

		// Create a dashed line to separate the boxes
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for (int lineInit = 0; lineInit < lineLength; lineInit++)
			line.append('-');

		// Go through the Grid, printing out its values separated by spaces
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				printFixedWidth(String.valueOf(Grid[i][j]), digits);
				// Print the vertical lines between boxes
				if ((j < N - 1) && ((j + 1) % SIZE == 0))
					System.out.print(" |");
				System.out.print(" ");
			}
			System.out.println();

			// Print the horizontal line between boxes
			if ((i < N - 1) && ((i + 1) % SIZE == 0))
				System.out.println(line.toString());
		}
	}

	/*
	 * The main function reads in a Sudoku puzzle from the standard input,
	 * unless a file name is provided as a run-time argument, in which case the
	 * Sudoku puzzle is loaded from that file. It then solves the puzzle, and
	 * outputs the completed puzzle to the standard output.
	 */
	public static void main(String args[]) throws Exception {
		InputStream in;
		if (args.length > 0)
			in = new FileInputStream(args[0]);
		else
			in = System.in;

		// The first number in all Sudoku files must represent the size of the
		// puzzle. See
		// the example files for the file format.
		int puzzleSize = readInteger(in);
		if (puzzleSize > 100 || puzzleSize < 1) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}

		Sudoku s = new Sudoku(puzzleSize);

		// read the rest of the Sudoku puzzle
		s.read(in);

		// Solve the puzzle. We don't currently check to verify that the puzzle
		// can be
		// successfully completed. You may add that check if you want to, but it
		// is not
		// necessary.

		s.solve();

		// Print out the (hopefully completed!) puzzle
		s.print();
	}
}
