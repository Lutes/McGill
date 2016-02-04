import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SongWriter {
	private Hashtable<Integer, String> pitchToNote;

	// The constructor of this class
	public SongWriter() {
		this.initPitchToNoteDictionary();
	}

	// This initialises the pitchToNote dictionary,
	// which will be used by you to convert pitch numbers
	// to note letters
	public void initPitchToNoteDictionary() {
		pitchToNote = new Hashtable<Integer, String>();
		pitchToNote.put(60, "C");
		pitchToNote.put(61, "C#");
		pitchToNote.put(62, "D");
		pitchToNote.put(63, "D#");
		pitchToNote.put(64, "E");
		pitchToNote.put(65, "F");
		pitchToNote.put(66, "F#");
		pitchToNote.put(67, "G");
		pitchToNote.put(68, "G#");
		pitchToNote.put(69, "A");
		pitchToNote.put(70, "A#");
		pitchToNote.put(71, "B");
	}

	// This method converts a single MidiNote to its notestring representation
	public String noteToString(MidiNote note) {
		String result = "";
		String n = "";
		// The duration of the note is determined by the .getDuration method
		int duration = (note.getDuration());
		// The string n is set to the note value, this is determined by getting
		// the pitch and subtracting the octave variance. The note is then
		// retrieved from the pitchToNote hashtable
		n = pitchToNote.get(note.getPitch() - (note.getOctave() * 12));
		// If the note is silent then n = "P"
		if (note.isSilent()) {
			n = "P";
		}
		// If the duration is one then the numerical prefix is implied to be
		// one.
		if (duration == 1) {
			result = n;
			return result;
		}
		// the result is the duration of the note, followed by the note.
		result = duration + n;
		// the length of note and note is returned.
		return result;
	}

	// This method converts a MidiTrack to its notestring representation.
	// You should use the noteToString method here
	public String trackToString(MidiTrack track) {
		ArrayList<MidiNote> notes = track.getNotes();
		String result = "";
		int previous_octave = 0;
		int current_octave = 0;
		int octind = 0;
		MidiNote current_note;

		int Track_Length = notes.size();

		// The for loop iterates through every note.
		for (int i = 0; Track_Length > i; i++) {
			String octave_char = "";
			current_note = notes.get(i);
			current_octave = current_note.getOctave();
			octind = current_octave - previous_octave;

			// the octind variable is an index of the difference between the
			// current octave and the previous octave.
			// When octind is larger than zero the corresponding char for octave
			// change is added to the octave_char string.
			while (octind > 0) {
				octave_char += ">";
				octind--;
			}
			
			//When octind is less than zero the corresponding char for octave
			// change is added to the octave_char string.
			while (octind < 0) {
				octave_char += "<";
				octind++;
			}

			//previous_octave becomes current octave. 
			previous_octave = current_octave;
		
			result += (octave_char + noteToString(current_note));
		}

		return result;

	}

	public void writeToFile(Song s1, String file_path) throws IOException {

		//File writer and buffered writer are created.
		int Num_Track = s1.getTracks().size();
		FileWriter fw = new FileWriter(file_path);
		BufferedWriter bw = new BufferedWriter(fw);

		//Writes the properties of the song. ie name, bpm and soundbank.
		bw.write("name = " + s1.getName());
		bw.newLine();
		bw.write("bpm = " + s1.getBPM());
		bw.newLine();
		bw.write("soundbank = " + s1.getSoundbank());
		bw.newLine();

		//Iterates through every track in the arraylist.
		for (int i = 0; i < Num_Track; i++) {
			//writes instrument id and the track. 
			bw.write("instrument = " + s1.getTracks().get(i).getInstrumentId());
			bw.newLine();
			bw.write("track = " + trackToString(s1.getTracks().get(i)));
			bw.newLine();
		}

		//buffered writer and file writer are closed. 
		bw.close();
		fw.close();
		// TODO Q4.c.
		// Implement the void writeToFile( Song s1 , String file_path) method
		// This method writes the properties of the Song s1 object
		// and writes them into a file in the location specified by
		// file_path. This file should have the same format as the sample
		// files in the 'data/' folder.
	}

	public static void main(String[] args) {
		// Create a Song object
		Song s1 = new Song();
		String  type  = ".txt";
		String File_name = "Bonus";
		//tries to load a song file
		try {
			s1.loadSongFromFile("./data/" + File_name+type);
		} catch (IOException e1) {
			System.out.println(e1);
		}
		//Creates the file path
		String File_name_reverse = File_name + "_reverse" + type;
		String File_path = "./data/" + File_name_reverse;
		//reverts the object s1
		s1.revert();
		//new song writer object is created
		SongWriter sw = new SongWriter();

		//Tries to write the reverted file.
		try {
			sw.writeToFile(s1, File_path);
		} catch (IOException e) {
			System.out.println(e);
		}

		// Load text file using the given song_filename, remember to
		// catch the appropriate Exceptions, print meaningful messages!
		// e.g. if the file was not found, print
		// "The file FILENAME_HERE was not found"

		// call the revert method of the song object.

		// Create a SongWriter object here, and call its writeToFile( Song s,
		// String file_location) method.

	}
}