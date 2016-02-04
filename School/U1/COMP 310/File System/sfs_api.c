//Daniel Lutes
//260609221
//NOV. 18 2015

#include "sfs_api.h"
#include "disk_emu.h"
#include <strings.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <regex.h>
#include <math.h>

//definitions
#define DISK_FILE "sfs_disk.disk"
#define BLOCK_SIZE 512
#define MAX_BLOCKS 300
#define MAX_INODES 111
#define MAX_FILES 110

//regex
#define REGEXDOT "(^[a-z0-9A-Z]{1,16})(\\.)([a-zA-Z0-9]{1,3})$" 
#define REGEX "^[a-z0-9A-Z]{1,16}$"

super_block_t sb;
dir_entry_t root_dir[MAX_FILES];
char buff [BLOCK_SIZE];
inode_t inode_table[MAX_INODES];
fd_table_t fd_table[MAX_FILES];
unsigned short free_blocks[MAX_BLOCKS];
int namecounter = 0;

//ctrl
void ctrl(char *name) {
	int i;
	for (i = 0; i <= MAXFILENAME; i++) if (name[i] == 1) name[i] = '\0';

}
//find free block
int free_block() {
	
	int i;
	for (i = 0; i < MAX_BLOCKS; i++) {
		if (free_blocks[i] == 0) {
			free_blocks[i] = 1;
			return i;
		}
	}
	return -1;
}

// find free inode
int free_inode() {

	int i;
	for (i = 0; i < MAX_INODES; i++) {
		if (inode_table[i].mode == NULL) {
      			inode_table[i].mode =  0x755;
      			inode_table[i].link_cnt = 1;
      			inode_table[i].uid = 0;
      			inode_table[i].gid = 0;				
			return i;
		}
	}
	return -1;
}


void init_superblock() {

        sb.magic = 1234;
        sb.block_size = BLOCK_SIZE;
        sb.fs_size = MAX_BLOCKS*BLOCK_SIZE;
        sb.inode_table_len = MAX_INODES;
        sb.root_dir_inode = 0;
}

void add_root_dir_inode() {

        //  root node place in table
        inode_table[0].mode =  0x755;
        inode_table[0].link_cnt = 1;
        inode_table[0].uid = 0;
        inode_table[0].gid = 0;
        inode_table[0].size = 103;
	//root dir is stored in block 17
        inode_table[0].data_ptrs[0] = 17; 
}

void zero_everything() {

        bzero(&sb, sizeof(super_block_t));
        bzero(&fd_table[0], sizeof(fd_table_t)*MAX_FILES);
        bzero(&inode_table[0], sizeof(inode_t)*MAX_INODES);
        bzero(&root_dir, sizeof(dir_entry_t)*MAX_INODES);
        bzero(&free_blocks[0], sizeof(unsigned int)*MAX_BLOCKS);
	bzero(&buff[0], BLOCK_SIZE);
}



// valid name check 
int name_valid(char *file, int p) {

  	regex_t regex;
  	int r;
  	char buf[100];
  	
	if (strlen(file) > MAXFILENAME) return 100;

	// this there a dot
  	if(p == 1) { 
  		
		// compile regex
  		r = regcomp(&regex, REGEXDOT, REG_EXTENDED);
  		if (r) {
    			printf("Regex compile error");
    			return -1;
  		}

  		// look for match
  		regexec(&regex, file, 0, NULL, 0);
  		if (r == REG_NOMATCH) {
    			printf("'%s' Invalid name\n", file);
    			return -1;
  		}
		else if (r) {
    			regerror(r, &regex, buf, sizeof(buf));
    			fprintf(stderr, "error %s\n", buf);
    			return -1;
  		}
  		regfree(&regex);
  		return r;
  	}
  	else {
  		r = regcomp(&regex, REGEX, REG_EXTENDED);
  		if (r) {
    			printf("Regex compile error");
    			return -1;
  		}

  		regexec(&regex, file, 0, NULL, 0);
  		if (r == REG_NOMATCH) {
    			printf("'%s' Invalid name\n", file);
    			return -1;
  		}
  
		else if (r) {
    			regerror(r, &regex, buf, sizeof(buf));
    			fprintf(stderr, "error %s\n", buf);
    			return -1;
  		}
  		regfree(&regex);
  		return r;	
	}

}

void mksfs(int fresh) {

	//Implement mksfs here	
	if (fresh == 1) {

                printf("Initalizing sfs\n");
		init_fresh_disk(DISK_FILE, BLOCK_SIZE, MAX_BLOCKS);
                zero_everything();

		// write superblock to the first block
                printf("Writing superblocks\n");
                init_superblock();
		write_blocks(0, 1, &sb);
	
		// write the inode table on block 2
                printf("Writing inode table\n");
                add_root_dir_inode();
		write_blocks(1, 16, &inode_table);

                // write root directory on block 17
                printf("Writing root dir\n");
                write_blocks(17,6, &root_dir);

                // end of disk for free blocks
                printf("Writing free blocks\n");
		int i;
		for (i = 0; i <= 23; i++) free_blocks[i] = 1;

		free_blocks[MAX_BLOCKS - 1] = 1;
		write_blocks(MAX_BLOCKS - 1, 1, &free_blocks);

	} else {

		init_disk(DISK_FILE, BLOCK_SIZE, MAX_BLOCKS);
	}
}
  
// get the next file name
int sfs_get_next_filename(char *fname) {
 
	int  i;
	if (namecounter == 0) {
		if (strcmp(fname, "\0")) {
			for (i = 0; i < MAX_FILES ; i++) {
				if (root_dir[i].name != NULL) {
					strcpy(fname, root_dir[i].name);
					namecounter = 1;
					return 1;
				}	
		
			}

		}
		namecounter = 1;
	}
	int j;
	for (i = 0; i < MAX_FILES ; i++) {
	
		if (strcmp(fname,root_dir[i].name) == 0){
			for (j = i + 1; j < MAX_FILES - i ; j++){
				if (root_dir[j].name != NULL){
				strcpy(fname, root_dir[j].name);
				printf("%s\n", fname);
				return 1;
				}
			}
		}	
	}
	return 0;

}

// get the size of the file by looking at all nodes
int get_file_size_index(int index) {
	
	int s = 0;
	while (inode_table[index].backwards_indirect_ptr != NULL) index = inode_table[index].backwards_indirect_ptr;
	s += inode_table[index].size;
	while (inode_table[index].indirect_ptr != NULL) {
		if(index > 109)break;
		s += inode_table[inode_table[index].indirect_ptr].size;
		index = inode_table[index].indirect_ptr;
	}
	return s;
}
 
// get the size of the file
int sfs_GetFileSize(const char* path) {

	int i = 0;
	int index, size;
	// find root
	while(i < MAX_FILES) {
		if (strcmp(root_dir[i].name, path) == 0) {
			index = root_dir[i].inode_idx;
			size = get_file_size_index(index);
			return size;
		}
		i++;
	}	
 	return -1;
}

// open a file or create it and place it in table
int sfs_fopen(char *name) {

	int a, b, size, loc, fd, index;
	int  t = 0;

	// does the file exist
	for(a = 0; a < MAX_FILES; a++) {
		if(strcmp(root_dir[a].name, name) == 0) {
			t = 1; 
			loc = a;	
			break;
		}  
	}

	if (t == 0) {
		
		int i;
		int p = 0;
		
		// regex
		for(i = 0; i < 20; i++) {
			if(name[i] == '.'){
				p = 1;
			}
		}		
		if (name_valid(name, p) != 0){
			printf("Invalid name \n");
			return -1;
		}
	
		// get an open block for the file 
		int free_block_ptr = free_block();
		if (free_block_ptr == -1) {
			printf("No open blocks");
			exit(0);
		}

		int j,k,l;

		// look for space in inodes
		for (j = 0; j < MAX_INODES; j++) {
			if(inode_table[j].mode == NULL) {
				// create inode
				index = j;
				inode_table[j].mode =  0x755;
        			inode_table[j].link_cnt = 1;
        			inode_table[j].uid = 0;
        			inode_table[j].gid = 0;
        			inode_table[j].size = 0;
        			inode_table[j].data_ptrs[0] = free_block_ptr; 
				break;
			}
		}

		// place inode in root dir
		for (k = 0; k < MAX_INODES; k++) {
			if (strcmp(root_dir[k].name, "") == 0) {
				strcpy(root_dir[k].name, name);
				ctrl(root_dir[k].name);
				root_dir[k].inode_idx = index;
				break;
			}
		}
		
		// place file in fd table
		for(l = 0; l < MAX_FILES; l++) {
			if(fd_table[l].inode_idx == NULL){
				fd = l;
				fd_table[l].inode_idx = index;
				fd_table[l].rd_write_ptr = 0;
				break;
			}	
		}
		// return fd posiotion
		return fd;
	}
	// if the file already exists
	if (t == 1) { 
		// place the file in table
		index = root_dir[loc].inode_idx;
		for (b = 0; b < MAX_FILES; b++) {
			// is the file there already?
			if (fd_table[b].inode_idx == index) return b;
			else if (fd_table[b].inode_idx == NULL) {
				fd_table[b].inode_idx = index;
				//read write ptr update
				fd_table[b].rd_write_ptr = get_file_size_index(index);
				return b;
			}
		}		
		return fd;
	}	

}

// remove file from fd table
int sfs_fclose(int fileID){

	if (fd_table[fileID].inode_idx != NULL) {
		fd_table[fileID].inode_idx = NULL;
		fd_table[fileID].rd_write_ptr = NULL;
		return 0;
	} 	
	return 1;
}

// read a file into a buffer
int sfs_fread(int fileID, char *buf, int length) {

	//Implement sfs_fread here	
	if (length < 0 | fd_table[fileID].inode_idx == NULL) return 0; 
		
        char buffer[BLOCK_SIZE];
	int read = 0;
	int offset = 0;
	int remainder;

        int index = fd_table[fileID].inode_idx;
        int cur = fd_table[fileID].rd_write_ptr;
        inode_t node;        
	int start_block = (int) cur/BLOCK_SIZE;
	int start_position = cur % BLOCK_SIZE;

        //go to end of file
        if (cur + length > get_file_size_index(index)) {
		read = get_file_size_index(index) - cur;
	}
	else {
		read = length;
	}

	// every 512 bytes loop
	while (read > 0) {		

		int last = 0;
		int start_node = (int) floor((double)start_block/(double)12); 
		int j;
		for (j = 0; j < start_node; j++) index = node.indirect_ptr;
		start_block = start_block % 12;
		node = inode_table[index];

		if (read >= BLOCK_SIZE) {
			remainder = BLOCK_SIZE - start_position;
		} else {
			// if there is only one loop change
			remainder = read;
		}

		read_blocks(node.data_ptrs[start_block], 1, &buffer[0]);
		int i;
		for (i = 0; i < remainder; i++) {
			buf[offset+i] = buffer[start_position+i];
		}

		offset += remainder;
		read -= remainder;
		fd_table[fileID].rd_write_ptr += remainder;
		start_block++;
		start_position = fd_table[fileID].rd_write_ptr % BLOCK_SIZE;

	}
	return offset;
}

// write file
int sfs_fwrite(int fileID, const char *buf, int length) {

	//Implement sfs_fwrite here

		
	int used_blocks = 0;
	int start_of_buff = 0;
	int writer;
	char buffer[BLOCK_SIZE];
	
	if (length == 0 | fileID < 0 | fileID >= MAX_FILES | fd_table[fileID].inode_idx == NULL){ 
return 0;
}

	int index = fd_table[fileID].inode_idx;
	int aptr_index = (int) floor((double)fd_table[fileID].rd_write_ptr/(double)BLOCK_SIZE);
	int total_blocks = (int) ceil((double)(fd_table[fileID].rd_write_ptr+length)/(double)BLOCK_SIZE);
	int blocks_cnt_down = total_blocks - aptr_index; 
	int aposition = fd_table[fileID].rd_write_ptr % BLOCK_SIZE;


	//write in chunk of 512
	do {	
		//create indirect ptr
		if (aptr_index > 11) {
			int numpointers = (int) aptr_index/12;
			int p;
			for (p = 0; p < numpointers; p++) {
				if (inode_table[index].indirect_ptr == NULL) {
					inode_table[index].indirect_ptr = free_inode();
					if (inode_table[index].indirect_ptr == -1) {
						printf("NO MORE SPACE\n");
						return -1;
					}
					inode_table[inode_table[index].indirect_ptr].backwards_indirect_ptr = index;
				}
				index = inode_table[index].indirect_ptr;
			}
			aptr_index = aptr_index % 12;			 
		}

		//if a new block is required allocate
		if (inode_table[index].data_ptrs[aptr_index] == NULL) {
			inode_table[index].data_ptrs[aptr_index] = free_block();
		}	

		if (blocks_cnt_down > 1) writer = BLOCK_SIZE - aposition;
		else writer = length - used_blocks;

		read_blocks(inode_table[index].data_ptrs[aptr_index], 1, &buffer[0]);
		int j;
		for (j = 0; j < writer; j++) {
			buffer[aposition+j] = buf[start_of_buff+j]; 
		}

		write_blocks(inode_table[index].data_ptrs[aptr_index], 1, &buffer[0]);


		start_of_buff += writer;
		aposition = 0;
		used_blocks += writer;
		inode_table[index].size += writer;
		fd_table[fileID].rd_write_ptr += writer;
		aptr_index++;
		blocks_cnt_down--;
		
	} while (blocks_cnt_down != 0); 
	return used_blocks;
}

// look for place based of read write ptr
int sfs_fseek(int fileID, int loc) {
        
        if (loc < 0 | loc > get_file_size_index(fd_table[fileID].inode_idx)) return 0;
        else fd_table[fileID].rd_write_ptr = loc;
	return 1;
}

// remove file
int sfs_remove(char *file) {
	
	int k = 0;
	int f = 0;	
	int index, i,j;


	//clean root
		for (i = 0;i < MAX_INODES; i++){
			if(strcmp(root_dir[i].name,file) == 0){		
				index = root_dir[i].inode_idx;
				int z;
				for (z = 0; z < sizeof(root_dir[i].name); z++) root_dir[i].name[z] = '\0';
				root_dir[i].inode_idx = NULL;
				f = 1;
				break;
			}
		}
	
	//no file f
	if (f == 0){
		printf("Couldn't find %s, exiting remove method", file);
		return -1;	
	}
	int next;

	do{
		//clean the inode
		inode_table[index].mode = NULL;
		inode_table[index].link_cnt = NULL;
        	inode_table[index].uid = NULL;
		inode_table[index].gid = NULL;
		inode_table[index].size = NULL;
	
		//clean inode array
		for (j = 0; j < 12; j++){
			
			if (inode_table[index].data_ptrs[j] != NULL){
				int bl = inode_table[index].data_ptrs[j];
				//if used write null
				write_blocks(bl,1,buff);
				//free block in arrray
				free_blocks[bl] =0;	
				inode_table[index].data_ptrs[j] = NULL;
			}		
		}

		next = inode_table[index].indirect_ptr;
		inode_table[index].indirect_ptr = NULL;
		inode_table[index].backwards_indirect_ptr = NULL;
		index = next;	
	}while(next != NULL);
	
	return 0;
}



