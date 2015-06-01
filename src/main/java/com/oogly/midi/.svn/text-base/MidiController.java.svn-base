package com.oogly.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiDevice.Info;

/**
 * MidiController provides a simple interface to a specific midi device. This
 * was tested using an EMU 404 and a Korg R3.
 * 
 * @author Jonathan Holloway.
 */
public class MidiController {

	private MidiDevice midiDevice = null;
	private int midiChannel = 0;

	/**
	 * MidiController constructor.
	 * 
	 * @param requiredDeviceName
	 * @param requiredDeviceDescription
	 */
	public MidiController(final String requiredDeviceName,
			final String requiredDeviceDescription) {

		System.out.println("MIDI Devices:");

		Info midiDeviceInfo = getMidiDevice(requiredDeviceName,
				requiredDeviceDescription);

		midiDevice = openMidiDevice(midiDeviceInfo);

		if (midiDevice.getMicrosecondPosition() != -1) {
			System.out.println("Midi device supports timestamping: "
					+ midiDevice.getMicrosecondPosition());
		}
	}

	public void close() {
		if (midiDevice.isOpen()) {
			midiDevice.close();
		}
	}

	/**
	 * Send a midi message on channel 0.
	 * 
	 * @param midiDevice
	 * @param number
	 *            is the number of notes to transmit
	 * @param note
	 *            is the note to send
	 * @param velocity
	 *            is the velocity to send
	 */
	public void sendMidiMessage(final int number, final int note,
			final int velocity) {

		for (int i = 0; i < number; i++) {
			sendNoteMessage(note, velocity);
		}
	}

	/**
	 * Stop midi message.
	 */
	public void stopMidiMessage(final int note, final int velocity) {

		sendGenericMessage(ShortMessage.NOTE_OFF, note, velocity);
	}

	/**
	 * Send a continuous midi message on channel 0.
	 * 
	 * @param midiDevice
	 * @param number
	 *            is the number of notes to transmit
	 * @param note
	 *            is the note to send
	 * @param velocity
	 *            is the velocity to send
	 */
	public void sendContinuousMidiMessage(final int note, final int velocity) {
				
		try {
			while (true) {			
				
				sendNoteMessage(note, velocity);				
			}					
		} finally {
			this.close();
		}
	}

	private void sendNoteMessage(final int note, final int velocity) {
		sendGenericMessage(ShortMessage.NOTE_ON, note, velocity);
		System.out.println("Sent note " + note + " with velocity " + velocity);
	}

	private void sendGenericMessage(int messageType, final int note,
			final int velocity) {

		ShortMessage shortMessage = new ShortMessage();

		try {
			shortMessage.setMessage(messageType, midiChannel, note, velocity);

			Receiver receiver = midiDevice.getReceiver();
			receiver.send(shortMessage, -1);

		} catch (InvalidMidiDataException e) {
			System.out.println("Invalid MIDI Data" + e);
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			System.out.println("MIDI Unavailable " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Open a midi device.
	 * 
	 * @param midiDeviceInfo
	 * @param midiDevice
	 * @return
	 */
	private MidiDevice openMidiDevice(final Info midiDeviceInfo) {
		try {
			if (midiDeviceInfo == null) {
				
				System.out.println("Could not access MIDI device, please specify one of the following:");
				
				for(Info info: MidiSystem.getMidiDeviceInfo()) {
					System.out.println("Device Info " + info.getName() + " - " + info.getDescription());
				}
				throw new IllegalArgumentException("Midi device info is null");
			}

			midiDevice = MidiSystem.getMidiDevice(midiDeviceInfo);

			if (!midiDevice.isOpen())
				midiDevice.open();

			System.out.println("\nUsing Midi Device: "
					+ midiDevice.getDeviceInfo().getName() + " "
					+ midiDevice.getDeviceInfo().getDescription());
			
		} catch (MidiUnavailableException mue) {
			System.out.println("MIDI Unavailable " + mue);
		}
		return midiDevice;
	}

	/**
	 * Get a specific midi device.
	 * 
	 * @param requiredDeviceName
	 * @param requiredDeviceDescription
	 * @return
	 */
	private Info getMidiDevice(String requiredDeviceName,
			String requiredDeviceDescription) {

		for (int i = 0; i < MidiSystem.getMidiDeviceInfo().length; i++) {

			Info[] devices = MidiSystem.getMidiDeviceInfo();
			Info currentDeviceInfo = devices[i];
			
			if (currentDeviceInfo.getName().equals(requiredDeviceName)
					&& currentDeviceInfo.getDescription().equals(
							requiredDeviceDescription)) {
				return currentDeviceInfo;
			}
		}
		return null;
	}

	public static void main(String[] args) {

		//String requiredDeviceName = "E-MU 0404 | USB";
		String requiredDeviceName = "USB [hw:1,0]";		
		String requiredDeviceDescription = "External MIDI Port";		
		
		MidiController midiController = new MidiController(requiredDeviceName,
				requiredDeviceDescription);
		midiController.sendContinuousMidiMessage(60, 83);
	}

	public MidiDevice getMidiDevice() {
		return midiDevice;
	}

	public void setMidiDevice(MidiDevice midiDevice) {
		this.midiDevice = midiDevice;
	}
}
