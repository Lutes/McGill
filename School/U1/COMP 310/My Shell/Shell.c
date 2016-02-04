//Daniel Lutes
//260609221
//October 2 2015
//COMP 310
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>

int count = 1;
int c = 0;
char *history[100]; 
char *bh[100];
int bpid[100];


//Add elements to history
int addHistory(char *line){
	history[count] = line;
	count++;
}

//just the tokenize portion of getcmd
int toke (char *args[], char *line) {

        int i = 0;
        char *token;

	char *temp = malloc(sizeof(line));
        strtok(line, "\n");
        strcpy (temp, line);
        while ((token = strsep(&temp, " \t\n")) != NULL) {
		int j;
		for (j = 0; j < strlen(token); j++) {
			if (token[j] <= 32) token[j] = '\0';
                }
	if (strlen(token) > 0) args[i++] = token;
	if (strcmp(token, "&") == 0) args[i - 1] = NULL;
	args[i] = NULL;
        }
        free(temp);
}

int getcmd(char *prompt, char *args[], int *background){
	int length, i = 0;
	char *token, *loc;
	char *line;
	size_t linecap = 0;

	printf("%s", prompt);
	length = getline(&line, &linecap, stdin);
   
	if (length <= 0) {
        	exit(-1);
    	}
    	// Check if background is specified..
    	addHistory(line);
	printf("%s",history[1]);
    	if ((loc = index(line, '&')) != NULL) {
		*background = 1;
		*loc = ' ';
    		bh[c] = line;
		c++;
	
	}	 
	
	else
  	      *background = 0;

	char *temp = malloc(sizeof(line));
	strtok(line, "\n");
	strcpy (temp, line);

	while ((token = strsep(&temp, " \t\n")) != NULL) {
        	int j = 0;
		for (j < strlen(token); j++;)
            		if (token[j] <= 32)
                token[j] = '\0';
        	if (strlen(token) > 0)
            		args[i++] = token;
	    	args[i] = NULL;
	}

	free(temp);

	//if blank, place new prompt
	if (args[0] == NULL)
		return -1;

	//history command
	if (strcmp(args[0],"r") == 0){
        char *key = args[1];
	
	//If there is no history tell user
	if ((args[1] == NULL)&&(count == 2)){
		printf("You have yet to enter history, YOU FOOOL! \n");
		return  -1;
	}
	
	//if there is history and no args, run most recent
	if ((args[1] == NULL)&&(count != 2)){
		printf("run:%s", history[count - 2]);
		toke(args,history[count-2]);
	}

	//if there is an arg, cycle through 10 items.
	int i = 0;
        if (args[1]!= NULL){
		while ( (i < 10)&&((count - i)>0)){
        		if (key[0] == (history[count - i][0])){
				printf("run:%s", history[count-i]);
                		toke(args,history[(count-i)]);
                		break;
       			}
        	//if arg doesnt occur in array report
		if ((i == 9) || ((count - i == 1)))
                printf("No history match found, try again...");
        	
		i++;
        	}
     	}
	}    

	// change directory
	if (strcmp(args[0],"cd") == 0){
		
		//Checks for path
		if (args[1] == NULL){
			printf("no path specified \n");
			return -1;
		}
        	
		//Checks for too many args
        	if (args[2] != NULL){
			printf("Only one arg required \n");
			return -1;
		}
        	
		//Looks for real path
		if (chdir(args[1]) !=-1){ 
			return -1;
		}
		else {
			printf("Path failure");
			return -1;
		}	
	}

	//shows working dir
	if(strcmp(args[0], "pwd") == 0){
		//makes sure no args
		if (args[1] != NULL){
			printf("too may args");
			return -1;
		}
		
		printf("%s", getcwd(NULL, 0));	
	}

	//reads background processes and prints the ones that are running 
	if(strcmp(args[0],"jobs")==0){
		if(args[1] != NULL){
			printf("Too many args\n");
			return -1;
		}
		
		int offset = 0;
		int k = 0;
		int p = c;	
		
		//Checks for background procceses that are no longer running.
		while(k < p){

			if( 0 != (waitpid(bpid[k],0,WNOHANG))){ 
				int i = k;
				while (i < c){
					strcpy(bh[i] , bh[i + 1]);
                			bpid[i] = bpid[i+1];
					i++;
				}
				offset++;
			}	
			k++;
		}	
		c = c - offset;
	        	
		//Print running background jobs
		printf("# :: CMD  \n");
		int w = 0;
		while (w < c){ 
			printf("%d :: %s \n", w, bh[w]);	
			w++;	
		}


		return -1;
	}	
	
	//fg command
	if(strcmp(args[0],"fg") == 0){
		if ((args[1] == NULL)){
			printf("You need to enter an arg\n");
			return -1;
		}
		//wait for specified function
		int f = atoi(args[1]);
		waitpid(bpid[f], 0, NULL);
	}


	//Print history
	if(strcmp(args[0], "history") == 0){
		if (args[1] != NULL){
			printf("too many args\n");
			return -1;
		}

		int i = 1;
		while(i < count){
			printf("%d -- %s \n",i, history[i]);	
			i++;
		}
	}
	
	return i;
}

int main(){
	
	char *args[20];
	int bg;
	int j;
	for (j = 0; j<101; j++){ 
		history[j] = "\0";
    		bh[j] = "\0";
		bpid[j] = 0;
	}

	while(1){

  		int childid;
		int i = 0;
		int cnt = getcmd(">>  ", args, &bg);
		if (cnt == -1)
			continue;
	
		if (bg){
        		printf("\nBackground enabled..\n");
		}
		
		else
       			printf("\nBackground not enabled \n");

		childid = fork();
		if (childid == 0){  	                
			if (execvp(args[0],args) == -1)
				perror("execvp Error:");
		}
        
		else{
			if (bg == 0){
				waitpid(childid,NULL,0);
			}
			else {
				bpid[c - 1] = childid;
			}
			
		}
	}

}



