import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

public class Song {
	String myName;
	int myBeatsPerMinute;
	String mySoundbank;
	ArrayList<MidiTrack> myTracks;

	// The constructor of this class
	public Song() {
		myTracks = new ArrayList<MidiTrack>();
		myBeatsPerMinute = 200;
		mySoundbank = "";
		myName = "Default_Name";
	}

	// GETTER METHODS

	public String getName() {
		return myName;
	}

	public String getSoundbank() {
		return mySoundbank;
	}

	public int getBPM() {
		return myBeatsPerMinute;
	}

	public ArrayList<MidiTrack> getTracks() {
		return myTracks;
	}

	public void loadSongFromFile(String file_path) throws IOException {

		// File reader and buffered reader are created
		FileReader fr = new FileReader(file_path);
		BufferedReader br = new BufferedReader(fr);

		String CurrentLine = br.readLine();

		// While there is still information on the line
		while (CurrentLine != null) {
			// Converts the whole line to lowercase
			CurrentLine = CurrentLine.toLowerCase();

			// If it starts will name then split at equals and set myName to
			// second half of split.
			if (CurrentLine.startsWith("name")) {
				String[] SplitCurrentLine = CurrentLine.split("=");
				myName = SplitCurrentLine[1].trim();
			}

			// If it starts with bpm split at equals and set myBeatsPerMinute to
			// second half of split.
			if (CurrentLine.startsWith("bpm")) {
				String[] SplitCurrentLine = CurrentLine.split("=");
				CurrentLine = SplitCurrentLine[1].trim();
				int bpm = Integer.parseInt(CurrentLine);
				myBeatsPerMinute = bpm;
			}

			// If it starts with sound bank split at equals and set mySoundbank
			// to second half of split.
			if (CurrentLine.startsWith("soundbank")) {
				String[] SplitCurrentLine = CurrentLine.split("=");
				mySoundbank = SplitCurrentLine[1].trim();
			}

			// If it starts with instrument split at equals and set instrumentid
			// to second half of split. go down a line get the notestring and add it to myTracks.
			if (CurrentLine.startsWith("instrument")) {
				String[] SplitCurrentLine = CurrentLine.split("=");
				CurrentLine = SplitCurrentLine[1].trim();
				int instrumentid = Integer.parseInt(CurrentLine);
				MidiTrack Track = new MidiTrack(instrumentid);

				CurrentLine = br.readLine();
				String[] SplitCurrentLine2 = CurrentLine.split("=");
				String notestring = SplitCurrentLine2[1].trim();
				Track.loadNoteString(notestring);
				myTracks.add(Track);
			}

			//After an operation is complete got to next line.
			CurrentLine = br.readLine();
		}

		//Buffered reader an file reader are closed.
		br.close();
		fr.close();
	}

	public void revert() {
		for (int i = 0; i < myTracks.size(); i++) {
			myTracks.get(i).revert();
		}
	}
}