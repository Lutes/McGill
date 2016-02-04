import java.util.ArrayList;
import java.util.Hashtable;

public class MidiTrack {
	private Hashtable<Character, Integer> noteToPitch;

	private ArrayList<MidiNote> notes;
	private int instrumentId;

	// The constructor for this class
	public MidiTrack(int instrumentId) {
		notes = new ArrayList<MidiNote>();
		this.instrumentId = instrumentId;
		this.initPitchDictionary();
	}

	// This initialises the noteToPitch dictionary,
	// which will be used by you to convert note letters
	// to pitch numbers
	public void initPitchDictionary() {
		noteToPitch = new Hashtable<Character, Integer>();
		noteToPitch.put('C', 60);
		noteToPitch.put('D', 62);
		noteToPitch.put('E', 64);
		noteToPitch.put('F', 65);
		noteToPitch.put('G', 67);
		noteToPitch.put('A', 69);
		noteToPitch.put('B', 71);
	}

	// GETTER METHODS
	public ArrayList<MidiNote> getNotes() {
		return notes;
	}

	public int getInstrumentId() {
		return instrumentId;
	}

	// This method converts notestrings like
	// <<3E3P2E2GP2EPDP8C<8B>
	// to an ArrayList of MidiNote objects
	// ( the notes attribute of this class )
	public void loadNoteString(String notestring) {
		// convert the letters in the notestring to upper case
		notestring = notestring.toUpperCase();
		int length = notestring.length();
		int duration = 1;
		int pitch = 0;
		int octave = 0;
		int index = 0;

		// This loop cycles through all of the characters in the input string
		// and does something based on the character.
		for (int i = 0; i < length; i++) {
			// Creates a string that holds the char at index i.
			String CurrentString = String.valueOf(notestring.charAt(i));

			// If the CurrentString is an A then a new object is created with a
			// default length of 1, and a pitch corresponding to the char gotten
			// from the hash table. The 6 corresponding if statements complete
			// the
			// same task based on a different char.
			if (CurrentString.equals("A")) {
				pitch = noteToPitch.get('A') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;
			}
			if (CurrentString.equals("B")) {
				pitch = noteToPitch.get('B') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;

			}
			if (CurrentString.equals("C")) {
				pitch = noteToPitch.get('C') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;

			}
			if (CurrentString.equals("D")) {
				pitch = noteToPitch.get('D') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;

			}
			if (CurrentString.equals("E")) {
				pitch = noteToPitch.get('E') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;

			}
			if (CurrentString.equals("F")) {
				pitch = noteToPitch.get('F') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;

			}
			if (CurrentString.equals("G")) {
				pitch = noteToPitch.get('G') + octave;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				duration = 1;
				index++;

			}
			if (CurrentString.equals("P")) {
				MidiNote N = new MidiNote(60, duration);
				N.setSilent(true);
				notes.add(N);
				duration = 1;
				index++;

			}

			// The following 17 if statements change the duration of the note
			// based on the number preceding said note.
			if (notestring.charAt(i) == '2') {
				duration = 2;
			}
			if (notestring.charAt(i) == '3') {
				duration = 3;
			}
			if (notestring.charAt(i) == '4') {
				duration = 4;
			}
			if (notestring.charAt(i) == '5') {
				duration = 5;
			}
			if (notestring.charAt(i) == '6') {
				duration = 6;
			}
			if (notestring.charAt(i) == '7') {
				duration = 7;
			}
			if (notestring.charAt(i) == '8') {
				duration = 8;
			}
			if (notestring.charAt(i) == '9') {
				duration = 9;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '0')) {
				i++;
				duration = 10;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '1')) {
				
				i++;
				duration = 11;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '2')) {
				i++;
				duration = 12;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '3')) {
				i++;
				duration = 13;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '4')) {
				i++;
				duration = 14;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '5')) {
				i++;
				duration = 15;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '6')) {
				i++;
				duration = 16;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '7')) {
				i++;
				duration = 17;
			}
			if ((notestring.charAt(i) == '1')
					&& (notestring.charAt(i + 1) == '8')) {
				i++;
				duration = 18;
				
			}

			// The following 2 if statements checks if a note is a sharp or a
			// flat, and then creates a new note and places it where the non
			// modified note is. and removes the old incorrect note.
			if (CurrentString.equals("#")) {
				char Sharp = notestring.charAt(i - 1);
				duration = notes.get((index - 1)).getDuration();
				pitch = noteToPitch.get(Sharp) + octave + 1;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				
				notes.remove(index - 1);
			}

			if (CurrentString.equals("!")) {
				char Flat = notestring.charAt(i - 1);
				duration = notes.get((index - 1)).getDuration();
				pitch = noteToPitch.get(Flat) + octave - 1;
				MidiNote N = new MidiNote(pitch, duration);
				notes.add(N);
				notes.remove(index - 1);
			}

			// The following statements check if the user wishes to change the
			// octave, shifting the pitch up or down by 12.
			if (CurrentString.equals(">")) {
				octave += 12;
			}

			if (CurrentString.equals("<")) {
				octave -= 12;
			}


		}

	}

	public void revert() {
		ArrayList<MidiNote> reversedTrack = new ArrayList<MidiNote>();
		for (int i = notes.size() - 1; i >= 0; i--) {
			MidiNote oldNote = notes.get(i);
			// create a newNote
			MidiNote newNote = new MidiNote(oldNote.getPitch(),
					oldNote.getDuration());

			// check if the note was a pause
			if (oldNote.isSilent()) {
				newNote.setSilent(true);
			}

			// add the note to the new arraylist
			reversedTrack.add(newNote);
		}
		notes = reversedTrack;
	}

	// This will only be called if you try to run this file directly
	// You may use this to test your code.
	public static void main(String[] args) {
		//String notestring = "<<3E3P2E2GP2EPDP8C<8B>3E3P2E2GP2EPDP8C<8B>";
        String notestring = "18E18E!";
		int instrumentId = 0;
		MidiTrack newTrack = new MidiTrack(instrumentId);
		newTrack.loadNoteString(notestring);
		// Build a MusicInterpreter and set a playing speed
		MusicInterpreter mi = new MusicInterpreter();
		mi.setBPM(1200);
		// Load the track and play it
		mi.loadSingleTrack(newTrack);
		mi.play();
		// close the player so that your program terminates
		mi.close();
	}
}
