//Daniel Lutes 
//Dec. 6 2015
//260609221
#include <sys/types.h>
#include <unistd.h> 
#include <stdio.h>

#define setup(x) (((((x) -1) >>2) <<2)+4)

void *base = NULL;
size_t allocated = 0;
size_t deallocated = 0;

char *my_malloc_error;

// first fit = 0, best fit = 1
int policy = 0;

//Structure for metadata
struct block {

	size_t block_size;
	struct block *link;
	struct block *back;
	int free;
	void *ptr;
	char data [1];
};
typedef struct block *chunk;

//Error code handling
void malloc_error(int error) {

	if (error == 1) my_malloc_error = "Error: Extension of heap failed.";
	if (error == 2) my_malloc_error = "Error: Initialization failed.";
	if (error == 3) my_malloc_error = "Error: Can't free when when no more blocks.";

	puts(my_malloc_error);	
}


//split up the the chunk to fit more info
void splice_chunk (chunk b, size_t s){
	
	//Create meta data	
	chunk new;
	//Assign meta data
	new = (chunk )(b->data + s);
	new->block_size = b->block_size - s;
	new->link = b->link;
	new->back = b;	
	new->free = 1;
	new->ptr = new ->data;
	b->block_size = s;
	b->link = new;
	if (new->link)
		new->link->back = new;
}

//Extend the heap into the free zone
chunk heap_extention(chunk last, size_t s) {
	
	//go to end of heap and extend
	long longb;
	chunk b;
	b = sbrk(0);
	longb = (long) sbrk(s);
	if (longb < 0) {
		malloc_error(1);
		return (NULL);
	}	
	b->block_size = s;
	b->link = NULL;
	b->back = last;
	b->ptr = b->data;
	if (last){
		last->link = b;
	}	
	b->free = 0;
	return b;
}


//First fit method of allocation
chunk first_fit(chunk *last, size_t block_size){
	
	chunk b = base;
	while (b && !(b->free && b->block_size >= block_size)) {
		*last = b;
		b = b->link;
	}
	return (b);
}

//Best fit method of allocation
chunk find_best_block(chunk *last, size_t block_size) {

	chunk b = base;
	chunk best = NULL;
	while ((b = b->link) != NULL) {
		if (b->free && b->block_size >= block_size) {
			if (best == NULL | best->block_size > b->block_size){ 
				best = b;	
			}
			*last = b;
		}
	}
	return best;
}


//Merges two chunks
chunk merge(chunk b) {
	
	if (b->link && b->link->free){
		b->block_size += b->block_size + b->link->block_size;
		b->link = b->link->link;
		if (b->link){
			b->link->back = b;
		}	
	}
	return (b);
}


//Searches for the largest free space. there is an error here
size_t free_space() {
	
	chunk b = base;
	size_t freesize = 0;
	while (b) {
		if(b->free) 
			freesize += (int) (b->block_size);
		b = b->link;
	}
	return freesize;
}

//Malloc comand
void * my_malloc(size_t block_size){
	
	allocated += block_size;
	chunk b, last;
	size_t s;
	s = setup(block_size);
	
	// first fit		
	if (base) {
		last = base;
		if (policy == 0) b = first_fit(&last, s);
		else b = find_best_block(&last, s);

		if (b) {
			if ((b->block_size - s) >= (b->block_size + 4)) splice_chunk(b, s);
			b->free = 0;
		} 
		else {
			//extend the heap
			b = heap_extention(last, s);
			if (!b) {
				malloc_error(1);			
				return(NULL);
			}		
		}
	} 
	else {
		//extend heap
		b = heap_extention(NULL, s);
		base = b;	
		if (!b) {
			malloc_error(2);
			return NULL;
		}
	}
	return b;
}

//free allocated memory
void my_free(void *p) {
	
	chunk b;
	if (1) {
		
		b = (chunk) p;
		deallocated += (int) b->block_size;
		b->free = 1;
		//merge if possibe
		if (b->back && b->back ->free) b = merge(b->back );
		if (b->link) merge(b);
		else {
			//Free up end of heap
			if (b->back) b->back->link = NULL;
			else {
				//out of blocks
				if (b != base) {
					malloc_error(3);
					base = NULL;
				}
			}			
			brk(b);
		}
	}		
}



void my_mallopt(int p){
	//First fit
	if (p == 0) {
		puts("First-fit policy set.\n");
		policy = 0;
	} 	
	//Best fit
	else if (p == 1) {
		puts("Best-fit policy set.\n");
		policy = 1;
	}
	// Error occurs
	else {
		puts("Error: argument not valid (must be 0 or 1).\n");
	}
}

void my_mallinfo() {

	//Bytes allocated
	char str[15];
	sprintf(str, "%d", (int) allocated);
	puts("Bytes allocated:");
	puts(str);
	//Bytes deallocated
	puts("Bytes deallocated:");
	sprintf(str, "%d", (int) deallocated);
	puts(str);
	//Bytes in use
	puts("Bytes in memory:");
	sprintf(str, "%d", (int) (allocated-deallocated));
	puts(str);
	//largest free space
	puts("Ammount of free space:");
	sprintf(str, "%d", (int) free_space());
	puts(str);
}


void main() {

	puts("\n***********************Starting Test***********************\n");

	// in first fit mode by default	
	puts("\nIn first-fit mode:");

	// saving 1 block of size 20
	puts("\nallocating 20 bytes...\n");
	void* a = my_malloc(20);
	my_mallinfo();
	puts("\n");

	// saving 3 more blocks of 50, 100, and 44. block size 214, largest 100. 
	puts("allocating 52 bytes...");
	puts("allocating 100 bytes...");
	puts("allocating 44 bytes...\n");
	void* b = my_malloc(52);
	void* c = my_malloc(100);
	void* d = my_malloc(44);
	
	my_mallinfo();
	puts("\n");

	// attempting to free the 50 byte block, largest now 44
	puts("removing b and c (52 and 100 bytes)...");
	my_free(b);
	my_free(c);

	my_mallinfo();
	puts("\n");


	// allocating one more block of memory (60 bytes)
	puts("allocating 30 bytes...\n");
	void *e = my_malloc(30);
	my_mallinfo();
	puts("\n");

	// Setting to best fit-policy
	my_mallopt(1);

	// saving 1 block of size 33
	puts("\nallocating 33 bytes...\n");
	void *f = my_malloc(33);
	my_mallinfo();
	puts("\n");
	
	puts("Freeing...\n");
	my_free(f);
	puts("Free!\n");
	puts("\n***********************Test Complete: Success***********************\n");
}


