public class Sorting {

	// The Compare Method compare two Strings and determines which string is
	// alphabetically "smaller"
	public static int Compare(String s1, String s2) {
		int x = 0;
		int least_Char;
		int a1 = 0, a2 = 0;
		// First the smallest string in terms of length is selected to avoid an
		// out of bounds reference in the future.
		if (s1.length() < s2.length()) {
			least_Char = s1.length();
		} else {
			least_Char = s2.length();
		}

		// For loops cycles through each character in each string.
		for (int i = 0; i < least_Char; i++) {

			// Converts Strings to individual chars. They char's ASCII value is
			// checked to see if it is a capital letter. If the char is a
			// Capital letter then the ASCII number is converted to a a lower
			// case letter ASCII.
			if ((s1.charAt(i) < 91) && ((s2.charAt(i) < 91))) {
				a1 = (s1.charAt(i) + 32);
				a2 = (s2.charAt(i) + 32);
			} else if ((s1.charAt(i) < 91)) {
				a1 = (s1.charAt(i) + 32);
				a2 = s2.charAt(i) + 0;
			}

			else if ((s2.charAt(i) < 91)) {
				a2 = (s2.charAt(i) + 32);
				a1 = s1.charAt(i) + 0;
			}

			else {
				a1 = s1.charAt(i) + 0;
				a2 = s2.charAt(i) + 0;
			}

			// These if statements are used to compare the values of each char
			// to the char in the same place in the other string. If neither of
			// the if statements are true the value x = 0 is returned meaning
			// both words are the same.
			if (a1 > a2) {
				x = 1;
				return x;
			} else if (a1 < a2) {
				x = -1;
				return x;
			}
		}
		return x;
	}

	// linearSearch checks in order a sorted array for a target string.
	public static int linearSearch(String[] a, String t) {
		int LinVal = -1;
		// The for loop cycles through every entry in the array and checks if
		// that entry is the same as the target String t. The Compare() method
		// is used to compare the Strings.
		for (int i = 0; i < a.length; i++) {
			if (Compare(a[i], t) == 0) {
				LinVal = i;
			}
		}
		return LinVal;
	}

	// binarySearch checks the midpoint between ever changing max or min values
	// for the target String.
	public static int binarySearch(String[] a, String t) {
		int BiVal = -1;
		int min = 0;
		int max = a.length;

		// While the min is still less than the max value.
		while (min < max) {
			int mid = midpoint(min, max);

			// The index i of the array a is checked against t.
			// if they are equal then position mid is returned. If not the mid
			// value is modified in order to get closer to the target.
			if (Compare(a[mid], t) == 0)
				return mid;
			else if (Compare(a[mid], t) == -1)
				min = mid + 1;
			else if ((Compare(a[mid], t) == 1)) {
				max = mid - 1;
			}
		}
		// If the search is not successful a -1 is returned.
		return BiVal;
	}

	// Sorts by comparing every string to every other string in the array with a
	// high index.
	public static int bubbleSort(String[] a) {
		int comparisons = 0;
		int n = a.length;
		boolean swapped = true;
		String temp = "";
		// while swapped is true.
		while (swapped) {
			swapped = false;
			// goes through every string in the array.
			for (int j = 1; j < n; j++) {
				// if two strings are not in order alphabetically they are
				// swapped.
				if (Compare(a[j - 1], a[j]) == 1) {
					temp = a[j - 1];
					a[j - 1] = a[j];
					a[j] = temp;
					swapped = true;

				}
				// Counts comparisons.
				comparisons++;
			}

		}
		
		return comparisons;
	}

	// Sorts by comparing two strings with gaped indexes.
	public static int combSort(String[] a) {
		int comparison = 0;
		int gap = a.length;
		boolean swapped = true;
		String temp = "";
		double shrink = 1.3;

		// While swapped is true or the gap isn't 1
		while ((swapped) || (gap != 1)) {

			// Gap size decreases
			gap = (int) ((double) gap / shrink);
			if (gap < 1) {
				gap = 1;
			}
			swapped = false;
			// Cycles through the strings in the array, split by the gap.
			for (int i = 0; i + gap < a.length; i++) {

				// if the Strings being compared are out of order then the
				// values are swapped.
				if (Compare(a[i], a[i + gap]) == 1) {
					temp = a[i];
					a[i] = a[i + gap];
					a[i + gap] = temp;
					swapped = true;

				}
				// The number of comparisons are counted.
				comparison++;
			}

		}
		return comparison;
	}

	public static void plotBubbleSortTest(int N_MAX) {
		/*
		 * bubble_sort_results[N] = the number of comparisons made when sorting
		 * an array of size N.
		 */
		int[] bubble_sort_results = new int[N_MAX];

		/*
		 * For each array size between 1 (the smallest array size) and N_MAX (an
		 * upper limit passed to this method), we will: 1) create a random test
		 * array 2) sort it, and store the number of comparisons in
		 * bubble_sort_results MAKE SURE THAT YOUR METHOD IS ACTUALLY SORTING
		 * THE TEST ARRAY!!!!!!
		 */
		for (int i = 1; i < N_MAX; i++) {
			String[] test_array = ArrayUtilities.getRandomNamesArray(i);
			bubble_sort_results[i] = bubbleSort(test_array);
		}

		// create a plot window
		// The argument passed to PlotWindow is the title of the window
		PlotWindow pw = new PlotWindow("Bubble Sort!");

		// add a plot to the window using our results array
		/*
		 * The first argument for addPlot is a name for your data set The second
		 * argument for addPlot is an array of integers, In position "N" in the
		 * array, you should put the number of comparisons that your algorithm
		 * made, when sorting an array of size N. For example,
		 * bubble_sort_results[100] will contain the number of comparisons made
		 * for sorting an array of 100 elements
		 */
		pw.addPlot("BubbleSort", bubble_sort_results);
	}

	public static void plotCombSortTest(int N_MAX) {
		// the results array
		int[] comb_sort_results = new int[N_MAX];

		// test sorting for arrays from size 1 to N_MAX
		// MAKE SURE THAT YOUR METHOD IS ACTUALLY SORTING THE TEST ARRAY!!!!!!
		for (int i = 1; i < N_MAX; i++) {
			String[] test_array = ArrayUtilities.getRandomNamesArray(i);
			comb_sort_results[i] = combSort(test_array);
		}
		// create a plot window
		PlotWindow pw = new PlotWindow("Comb Sort!");
		// add a plot to the window using our results array
		pw.addPlot("CombSort", comb_sort_results);
	}

	// Plots both sort methods comparisons.
	public static void plotCombBubble(int N_MAX) {
		/*
		 * bubble_sort_results[N] and comb_sort_results[N] = the number of
		 * comparisons made when sorting an array of size N.
		 */
		int[] bubble_sort_results = new int[N_MAX];
		int[] comb_sort_results = new int[N_MAX];

		// For each array size the number of comparisons is stored.
		for (int i = 1; i < N_MAX; i++) {
			String[] test_array = ArrayUtilities.getRandomNamesArray(i);
			String[] test_array_clone = ArrayUtilities.getRandomNamesArray(i);

			// The array for each method is stored.
			comb_sort_results[i] = combSort(test_array_clone);
			bubble_sort_results[i] = bubbleSort(test_array);

		}

		// create a plot window
		// The argument passed to PlotWindow is the title of the window
		PlotWindow pw = new PlotWindow("Bubble Sort And Comb Sort");

		// both CombSort and BubbleSort are plotted in the same window. Comb
		// sort is much faster than bubble sort.
		pw.addPlot("CombSort", comb_sort_results);
		pw.addPlot("BubbleSort", bubble_sort_results);

	}

	// midpoint simply finds the midpoint between 2 variables: max and min.
	private static int midpoint(int min, int max) {
		// TODO Auto-generated method stub
		int x = (min + max) / 2;
		return x;
	}

	public static void main(String[] args) {
		// uncomment the following lines when you want to test your sorting
		// methods
		// plotBubbleSortTest(100);
		// plotCombSortTest(1000);
		plotCombBubble(100);
	}
}
