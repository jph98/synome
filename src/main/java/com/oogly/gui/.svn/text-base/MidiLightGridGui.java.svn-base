package com.oogly.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.oogly.midi.MidiController;

/**
 * Tinker application.
 * 
 * @author jon
 * 
 */
public class MidiLightGridGui {

	private JFrame frame;
	private MidiController midiController;

	public MidiLightGridGui() {

		// Setup midi controller
		String requiredDeviceName = "E-MU 0404 | USB";
		String requiredDeviceDescription = "External MIDI Port";
		midiController = new MidiController(requiredDeviceName,
				requiredDeviceDescription);

		setupGui();
	}

	private void setupGui() {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		GridLayout gridLayout = new GridLayout(10, 10);
		panel.setLayout(gridLayout);

		int baseNote = 1;

		while (baseNote < 128) {

			final Color buttonTextColor = new Color(178, 255, 0);
			final Color normalButtonColor = new Color(47, 55, 56);

			final int note = baseNote;

			final JButton gridButton = new JButton("" + note);
			gridButton.setFont(new Font(Font.SANS_SERIF, 1, 10));
			gridButton.setHorizontalAlignment(SwingConstants.LEFT);
			gridButton.setVerticalAlignment(SwingConstants.BOTTOM);
			gridButton.setForeground(buttonTextColor);
			gridButton.setBackground(normalButtonColor);

			gridButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent event) {
					new MidiWorker(note, gridButton).execute();
				}
			});

			panel.add(gridButton);
			baseNote = baseNote + 1;
		}

		JLabel label = new JLabel("Madoc");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setSize(200, 100);
		panel.add(label);
		
		frame.add(panel);
		frame.setTitle("Midi Controller GUI");
		frame.pack();
		frame.setSize(800, 600);
		frame.setVisible(true);

	}

	private void fadeColors(final JButton button, final Color targetColor,
			final Color origColor) {

		new SwingWorker<Object, Object>() {

			protected Object doInBackground() throws Exception {

				// Pick a reference point
				int inc = 4;

				int r = origColor.getRed();
				int g = origColor.getGreen();
				int b = origColor.getBlue();

				while (r >= targetColor.getRed() && g >= targetColor.getGreen()
						&& b >= targetColor.getBlue()) {

					if (r > targetColor.getRed())
						r -= inc;
					if (g > targetColor.getRed())
						g -= inc;
					if (b > targetColor.getRed())
						b -= inc;

					button.setBackground(new Color(r, g, b));

					Thread.sleep(10);
				}
				return null;
			}		

		}.execute();
	}

	public static void main(String[] args) {
		MidiLightGridGui midiLightGrid = new MidiLightGridGui();
	}

	public class MidiWorker extends SwingWorker<Object, Object> {

		private int note;
		private final int noteLength = 200;
		private final Color normalButtonColor = new Color(47, 55, 56);
		private final Color activeButtonColor = new Color(159, 188, 191);
		private JButton button;

		public MidiWorker(int note, JButton button) {
			this.note = note;
			this.button = button;
		}

		@Override
		protected Object doInBackground() throws Exception {
			button.setBackground(activeButtonColor);

			midiController.sendMidiMessage(1, note, 83);
			Thread.sleep(noteLength);
			midiController.stopMidiMessage(note, 83);

			return null;
		}

		@Override
		protected void done() {
			try {				

				// Fade back
				fadeColors(button, normalButtonColor, activeButtonColor);

			} catch (Exception ex) {
				ex.printStackTrace();
				if (ex instanceof java.lang.InterruptedException)
					return;
			}
		}
	}
}
