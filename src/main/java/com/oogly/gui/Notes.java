package com.oogly.gui;

public class Notes {

	public static void main(String[] args) {

		for (int initialNote = 0; initialNote < 128; initialNote++) {
			String[] noteString = new String[] { "C", "C#", "D", "D#", "E",
					"F", "F#", "G", "G#", "A", "A#", "B" };

			int octave = (initialNote / 12) - 1;
			int noteIndex = (initialNote % 12);
			String note = noteString[noteIndex];
			
			System.out.println("Note is " + note + " octave "
					+ octave + " for " + initialNote);
		}
	}
}
