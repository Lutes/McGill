import java.util.Arrays;

public class MergeSort {

	// Here is a test array. You may use it or change it to test with other
	// examples.
	public static void main(String[] args) {
		String[] a = { "apple", "orange", "banana", "pear", "banana",
				"grapefruit" };
		System.out.println(Arrays.toString(a));
		a = mergeSort(a);
		System.out.println(Arrays.toString(a));
	}

	/*
	 * This is the recursive sorting method, mergeSort. Your task is to
	 * implement the merge method described below.
	 */
	public static String[] mergeSort(String[] a) {
		if (a.length < 2)
			return a;
		int middle = a.length / 2;
		String[] left = new String[middle];
		String[] right = new String[a.length - middle];
		int i = 0;
		for (i = 0; i < middle; i++)
			left[i] = a[i];
		for (int j = 0; j < right.length; j++) {
			right[j] = a[i];
			i++;
		}

		left = mergeSort(left);
		right = mergeSort(right);
		String[] result = merge(left, right);
		return result;
	}

	// This method merges two sorted arrays.
	public static String[] merge(String[] l, String[] r) {
		// Create a new string array which is the size of the left and right
		// string arrays combined.
		String[] result = new String[l.length + r.length];

		// Index created for the three string arrays.
		int li = 0;
		int ri = 0;
		int resi = 0;

		// While the new string is still in the bounds.
		while ((li + ri) < ((l.length + r.length))) {

			// If the left array is empty. Place the next digit from the right
			// array into the new array.
			if (li == l.length) {
				result[resi] = r[ri];
				ri++;
				resi++;

				// If the right array is empty. Place the next digit from the
				// left
				// array into the new array.
			} else if (ri == r.length) {
				result[resi] = l[li];
				li++;
				resi++;

				// if the left is alphabetically superior then it is place in
				// the new array.
			} else if (l[li].compareTo(r[ri]) < 0) {

				result[resi] = l[li];
				li++;
				resi++;

				// if the right is alphabetically superior then it is place in
				// the new array.
			} else if (l[li].compareTo(r[ri]) >= 0) {

				result[resi] = r[ri];
				ri++;
				resi++;

				// if the comparison is between two of the same word, then the
				// word on the right is arbitrarily placed in the new array
			} else {

				result[resi] = r[ri];
				ri++;
				resi++;
			}
		}

		return result;
	}
}
