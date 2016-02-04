#include<stdio.h>
#include<string.h>
#include<stdlib.h>
void Draw(int size);

main()
{
	//creates 2 varible
	char input[4];
	int size = 0;
		
	//while input is less than 1 ask for input
	do{
		printf("Enter size of Triangle:");
		scanf("%d", &size);
	}while(size <=0);
	
	//Draws first triangle
	Draw(size);

	//Asks user if they want to make another
	printf("Would you like to make another triangle:");
	scanf("%s", input);

	//While input is true make new triangle
	while (strcmp(input, "YES") == 0 || strcmp(input, "yes") == 0 || strcmp(input, "Y") == 0 || strcmp(input, "y") == 0){
		do{
                printf("Enter size of Triangle:");
                scanf("%d", &size);
        	}while(size <=0);

        	Draw(size);

		//asks user again if they want to make triagnle
        	printf("Would you like to make another triangle:");
        	scanf("%s", input);
	}

}


void Draw(int size){
       
	//Creates variables
	 int i, p;
	//counts depth of triangle
	for(i=1;i<=size;i+=2){
		//counts width of square
		for(p=1; p<=size; p++){
			//if p is in the correct range for the coresponding row print *
			if((p>(size-i)/2)&&(p<=size-(size-i)/2)){
				printf("*");
			}
			else{
				printf(" ");
			}
		}
	//new line
	printf("\n");				
	}
}	

