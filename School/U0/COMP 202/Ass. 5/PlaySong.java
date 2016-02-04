import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class PlaySong{
    public static void main( String[] args){
    	
    	//File path is created
    	String file_name = "Bonus.txt";
        String song_file_path = "./data/" + file_name;
        //song object created
        Song mySong = new Song ();
        
        //Tries to find the song file
        try {
			mySong.loadSongFromFile(song_file_path);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
        //Creates the MusicInterpreter object
        MusicInterpreter mi = new MusicInterpreter ();
        // Load the Song and play it
        mi.loadSong (mySong);
        mi.play ();
        // close the player so that your program terminates
        mi.close ();

    }
}