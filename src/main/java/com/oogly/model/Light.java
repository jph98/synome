package com.oogly.model;


public class Light {

	public static final int OFF = 0;
	public static final int ON = 1;	
	
	private int note = 0;	
	private int state;

	public Light() {
		setState(OFF);
	}
	
	public int getState() {
		return state;
	}

	public void setState(int lightState) {
		this.state = lightState;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}
	
	public void flipStatus() {
		if (this.state == ON) {
			this.state = OFF;
		} else {
			this.state = ON;
		}
	}
	
	public String toString() {
		return this.note + " "
		       + this.state;
	}	
}
