#include <stdio.h>

// Returns String length.
int MyStringLength(char array[]);

// Copies source to endlocation. This is done by copying char by char until null.
void MyStringCopy(char source[], char destination[]);

// Compares strings so and st by alphabetical order.
int MyStringCompare(char so[], char st[]);


// Testing above methods
int main() {
	// Creates and defines strings 
	char so[] = "Bob";	
	char st[] = "Billie";
	char sr[] = "Shannon";
	int compare;
	
	printf("\nCopys string %s\n", so);
	char copyArray[10];
	MyStringCopy(so, copyArray);
	
	// Compares so and copyArray
	printf("Compare %s and copied string %s\n", so, copyArray);
	printf("First String: %s ~~ Length of String: %d Second String: %s ~~ Length of String: %d\n",
		so, MyStringLength(so), copyArray, MyStringLength(copyArray));
	compare = MyStringCompare(so, copyArray);
	if (compare < 0){
		printf("%s is less than %s\n\n", so, copyArray);
	}	

	else if (compare > 0) {
		printf("%s is greater than %s\n\n", so, copyArray);
	}

	else {
		printf("%s is equal to %s\n\n", so, copyArray);
	}
	
	// Compares st and copyArray
	printf("Compare %s and the copied string %s\n", st, copyArray);
	printf("First String: %s ~~ Length of String: %d Second String: %s ~~ Length of String: %d\n",
		st, MyStringLength(st), copyArray, MyStringLength(copyArray));
	compare = MyStringCompare(st, copyArray);
	if (compare < 0){
		 printf("%s is less than %s\n\n", st, copyArray);
	}

	else if(compare > 0){ 
		printf("%s is greater than %s\n\n", st, copyArray);
	}	
	
	else{
	printf("%s is equal to %s\n\n", st, copyArray);
	}

	// Compares sr and copyArray
	printf("Compare %s and the copied string %s\n", sr, copyArray); 
	printf("First String: %s ~~ Length of String: %d Second String: %s ~~ Length of String: %d\n",
		sr, MyStringLength(sr), copyArray, MyStringLength(copyArray));
	compare = MyStringCompare(sr, copyArray);
	if (compare < 0) {
		printf("%s is less than %s\n\n", sr, copyArray);
	}	

	else if (compare > 0) {
		printf("%s is greater than %s\n\n", sr, copyArray);
	}
	
	else {
	printf("%s is equal to %s\n\n", sr, copyArray);
	}
}

int MyStringLength(char array[]) {
	// if array is 0; 

	if (sizeof(array) == 0) {
		return -1;
	}
	// While there are still chars, increment i
	int i = 0;
	char current = array[i];	
	
	while (current != '\0') {
		i++;
		current = array[i];
	}
	return i;
}


void MyStringCopy(char source[], char destination[]) {
	int p;
	// Checks location of null char in source
	for (p = 0; p <= MyStringLength(source); p++) {
		destination[p] = source[p]; 
	}
}

 
int MyStringCompare(char so[], char st[]) {
	int p;
	for (p = 0; p <= MyStringLength(so); p++) {
		
		// If chars identical, skip. 
		if (so[p] == st[p]){
			continue;
		}		
		return so[p] - st[p];		
	}
	return 0;
}

