#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void getSubstring(char *, char *, char *);
int login(char *, char *, char *);
int check(char *, char *);

void main(){
	
	char username[50];
	char password[50];
	char form[200];

	//getting length of form
	int n = atoi(getenv("CONTENT_LENGTH"));
	//1 is go back to login page, 0 is continue onto topics page
	int goback = 1;

	//identifiers for form parameters
	char *user = "username=";
	char *pass = "password=";

	//get the form
	fgets(form,n+1,stdin);

	//get the parameters corresponding to the identifiers
	getSubstring(user, form, username);
	getSubstring(pass, form, password);

	//was getting errors with an extra char on the username, to this just deletes the last char from the username
	int i;
	for (i = 0; i < 50; i++) {
		if (username[i] == '\0') {
			username[i-1] = '\0';
			break;
		}
	}

	//for printing html
	printf("Content-Type:text/html \n\n");
		
	//open members.csv and read line by line
	FILE *fp;
	fp = fopen("members.csv","r");
	char line[1024];
	while (fgets(line, 1024, fp)) {
		//checks if the username and passwords match on the form and members.csv
		goback = login(line, username, password);
		if (goback == 0) break;
	}

	//If no match
	if (goback == 1) {
		printf("Login failed... Click to go back to Login page.");
		//Button that redirects to login page
		printf("<form action=\"http://cs.mcgill.ca/~hkoivu/web/comp206/welcome.html\" method=\"POST\"><input type=\"submit\" value=\"Continue\"></form>"); 
	}
}

//Gets the parameter that comes after the string "parameter", ending with the & symbol
void getSubstring(char *parameter, char *form, char *tofill) {
	
	char *param = parameter;
	char *whole = form;
	char *output = tofill;

	int start = 0;
	int end = 0;
	int paramcounter = 0;

	//finds the starting char
	int i;
	for (i = 0; i < strlen(whole); i++) {
		if (whole[i] == param[paramcounter]) {
			paramcounter++;
			if (paramcounter == strlen(param)) break;
		}
		else {
			paramcounter = 0;
		}
		start++;
	}

	// if the start is at the end then there was no match
	if (start >= strlen(whole) - 1) return;
	
	//finds the end char
	end = start;
	for (i = start; i < strlen(whole); i++) {
		if (whole[i] != '&') {
			 end++;
		}
		else {
			end--;
			break;
		}
	}
	
	//finds the total length of the string
	int length = end - start;

	//copies the correct substring into the output string, with null char appended
	for (i = 0; i < length; i++) {
		output[i] = whole[start + 1 + i];
	}
	i++;
	output[i] = '\0';
}

//checks to see if two strings are equal (strcmp wasn't working because strtok doesn't append null chars)
int check(char *complete, char *token) {
	
	int i;
	for (i = 0; i < strlen(complete); i++) {
		if (complete[i] != token[i]) return 1;
	}
	return 0;
}

//checks if the username and pass match with a line in members.csv
int login(char *line, char *username, char *password) {

	char *tmp;
	char *uname;
	char *pass;
	//get first in line
	tmp = strtok(line, " ");
	//get second in line (username)
	uname = strtok(NULL, " ");
	//get third in line (pass)
	pass = strtok(NULL, " ");
	//was there a match? if so, boolean = 1
	int boolean = 0;
	if (check(username, uname) == 0 && check(password, pass) == 0) boolean = 1;

	if (boolean == 1) {
		printf("Login successful... Click to go to the Topics page.");
		//creates a button that links to the topics page
		printf("<form action=\"MyFacebookPage.py\" method=\"POST\"> <input type=\"hidden\" name=\"me\" value=\"%s\"/> <input type=\"submit\" value=\"Go to my Page, VROOM\"></form>", username);
		return 0;
	}
	else {
		return 1;
	}

}
