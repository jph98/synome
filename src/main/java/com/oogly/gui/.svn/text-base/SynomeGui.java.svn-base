package com.oogly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.oogly.midi.MidiController;
import com.oogly.model.Light;

/**
 * Synome: MIDI Sequencer application.
 * 
 * @author Jonathan Holloway
 * 
 * Set velocity on the notes with mouse up and down Set note length via right or
 * left move Fix midi double note issue MIDI Note Numbers down left hand side
 * Save/Load Patch Mouse Listener for dragging action when button pressed
 * Start/Stop button for the main daemon loop
 * Clear screen button
 */
public class SynomeGui implements WindowListener {

	private JLabel coord;
	private static final int numberOfButtonRows = 12;
	private static final int numberOfControlButtons = 8;
	private String tempo = "180";
	private final int sleepTime = 60000 / Integer.parseInt(tempo);	

	private JFrame frame;
	private MidiController midiController;
	private final Color buttonTextColor = new Color(178, 255, 0);
	private final Color normalButtonColor = new Color(34, 34, 34);
	private final Color activeButtonColor = new Color(90, 117, 122);

	private boolean daemonRunningFlag = true;
	private JPanel gridPanel;

	private String[] noteString = new String[] { "C", "C#", "D", "D#", "E",
			"F", "F#", "G", "G#", "A", "A#", "B" };

	private final ArrayList<ControlButton> controlButtons = new ArrayList<ControlButton>();

	private int dragState = Light.ON;

	private MouseMotionListener mouseMotionListener;
	private MouseListener mouseListener;
	
	private JButton startButton; 
	private JButton stopButton;

	public SynomeGui() {

		// Setup midi controller
		String requiredDeviceName = "E-MU 0404 | USB";
		String requiredDeviceDescription = "External MIDI Port";

		midiController = new MidiController(requiredDeviceName,
							                requiredDeviceDescription);

		setupGui();

		new MainDaemonWorker().execute();	
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
	}
	
	private boolean isLightButtonsRowOn(int row) {
		
		for (ControlButton controlButton: controlButtons) {
			
			if (controlButton.getLightButton(row).getLight().getState() == Light.OFF
				&& controlButtons.indexOf(controlButton) != 0) {
								
				return false;
			}
		}
		
		return true;
	}

	private void setupGui() {

		GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
	    GraphicsConfiguration graphicConf = graphDevice.getDefaultConfiguration();
		 			 
		frame = new JFrame(graphicConf);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int width = screenSize.width;
		int height = screenSize.height;
		
		if (graphDevice.isFullScreenSupported()) {
			frame.setUndecorated(true);
			DisplayMode disMode = new DisplayMode( width, height,32, 60);					
			graphDevice.setFullScreenWindow(frame);
			graphDevice.setDisplayMode(disMode);
		}
		
		frame.setBounds(0,0,width, height);
		frame.setSize(width, height);
		
		gridPanel = new JPanel();
		gridPanel.setBackground(normalButtonColor);
		
		mouseMotionListener = new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent evt) {
				double x = evt.getLocationOnScreen().getX();
				double y = evt.getLocationOnScreen().getY();

				Point componentPoint = new Point();
				componentPoint.setLocation(x, y);
				Component component = gridPanel.getComponentAt(componentPoint);
				
				if (component instanceof LightButton) {
					LightButton lightButton = (LightButton) component;
					int state = lightButton.getLight().getState();
										
					if (dragState == Light.ON && state == Light.OFF) {
						new LightButtonMidiWorker(lightButton, false).execute();
					}
					
					if (dragState == Light.OFF && state == Light.ON) {
						new LightButtonMidiWorker(lightButton, false).execute();
					}
				}			
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		};

		mouseListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent evt) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent evt) {

//				// This doesn't translate to the right component
//				int x = evt.getXOnScreen();
//				int y = evt.getYOnScreen();
//				
//				Component component = frame.getComponentAt(x,y);
//				
//				if (component instanceof LightButton) {
//					LightButton lightButton = (LightButton) component;													
//					new LightButtonMidiWorker(lightButton, true).execute();
//				}				
			}

			@Override
			public void mouseReleased(MouseEvent evt) {
				
			}
		};

		GridLayout gridLayout = new GridLayout();

		// extra one for control buttons
		gridLayout.setRows(numberOfButtonRows + 1);
		gridLayout.setColumns(numberOfControlButtons + 1);
		gridPanel.setLayout(gridLayout);

		final ControlButton noteButton = new ControlButton("");			
		noteButton.setHorizontalAlignment(SwingConstants.CENTER);
		noteButton.setVerticalAlignment(SwingConstants.CENTER);
		noteButton.setForeground(buttonTextColor);
		noteButton.setBackground(normalButtonColor);
		addNoteColumn(noteButton.getLightButtonList());

		// Add the control button to the list
		controlButtons.add(noteButton);

		// Add our control button to our panel
		gridPanel.add(noteButton);

		// Create 10 control buttons
		for (int i = 0; i < numberOfControlButtons; i++) {

			final ControlButton newControlButton = new ControlButton();
			newControlButton.setText("" + (i + 1));
			newControlButton.setFont(new Font(Font.SANS_SERIF, 1, 18));
			newControlButton.setHorizontalAlignment(SwingConstants.CENTER);
			newControlButton.setVerticalAlignment(SwingConstants.CENTER);
			newControlButton.setForeground(buttonTextColor);
			newControlButton.setBackground(normalButtonColor);

			newControlButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent event) {
					new ControlButtonWorker(newControlButton).execute();
				}
			});

			// Add column of lights (12 in total) to the control button
			ArrayList<LightButton> lightButtonColumn = newControlButton
					.getLightButtonList();
			addLightColumn(lightButtonColumn);

			// Add the control button to the list
			controlButtons.add(newControlButton);

			// Add our control button to our panel
			gridPanel.add(newControlButton);
		}

		// Add our light buttons to our panel
		for (int n = 0; n < numberOfButtonRows; n++) {
			for (int i = 0; i < controlButtons.size(); i++) {

				ControlButton controlButton = controlButtons.get(i);
				gridPanel.add(controlButton.getLightButton(n));
			}
		}

		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new FlowLayout());
		displayPanel.setBackground(new Color(100, 118, 120));
		JLabel tempoPanel = new JLabel("Synome Tempo: " + tempo);
		tempoPanel.setForeground(Color.WHITE);
		displayPanel.add(tempoPanel);

		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!daemonRunningFlag) {
					new MainDaemonWorker().execute();
					daemonRunningFlag = true;
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
				}
				
			}
			
		});
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (daemonRunningFlag) {
					daemonRunningFlag = false;
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
				}
				
			}
			
		});
		
		JButton clearButton = new JButton("Clear");		
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				for (ControlButton controlButton: controlButtons) {
					for (LightButton lightButton: controlButton.getLightButtonList()) {
						lightButton.getLight().setState(Light.OFF);
						lightButton.setBackground(normalButtonColor);
					}
				}
				
			}
			
		});
		
		displayPanel.add(startButton);
		displayPanel.add(stopButton);
		displayPanel.add(clearButton);
		coord = new JLabel("");
		displayPanel.add(coord);
		
		frame.setLayout(new BorderLayout());
		frame.add(displayPanel, BorderLayout.NORTH);
		frame.add(gridPanel, BorderLayout.CENTER);
		frame.setTitle("Synome");			
		frame.setVisible(true);
		frame.setIgnoreRepaint(true);

		//frame.addMouseListener(mouseListener);
	}

	private void addNoteColumn(ArrayList<LightButton> lightButtonColumn) {

		int initialNote = 60;

		for (int buttonNumber = 0; buttonNumber < numberOfButtonRows; buttonNumber++) {
			Light light = new Light();
			light.setState(Light.OFF);
			light.setNote(initialNote);

			//int octave = (initialNote / 12) - 1;
			int noteIndex = (initialNote % 12);
		
			String noteText = noteString[noteIndex];

			final LightButton currentNoteButton = new LightButton();
			currentNoteButton.setLight(light);
			currentNoteButton.setText(noteText);
			currentNoteButton.setFont(new Font(Font.SANS_SERIF, 1,18));
			currentNoteButton.setHorizontalAlignment(SwingConstants.CENTER);
			currentNoteButton.setVerticalAlignment(SwingConstants.CENTER);
			currentNoteButton.setForeground(buttonTextColor);
			currentNoteButton.setBackground(normalButtonColor);
			
			final int currentButtonNumber = buttonNumber;
			
			currentNoteButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (isLightButtonsRowOn(currentButtonNumber)) {
						
						for (ControlButton controlButton: controlButtons) {
							
							if (controlButtons.indexOf(controlButton) != 0) {
								LightButton currentLightButton = controlButton.getLightButton(currentButtonNumber);
								currentLightButton.getLight().setState(Light.OFF);
								currentLightButton.setBackground(normalButtonColor);
							}
						}											
						
					} else {
						
						for (ControlButton controlButton: controlButtons) {	
							if (controlButtons.indexOf(controlButton) != 0) {
								LightButton currentLightButton = controlButton.getLightButton(currentButtonNumber);
								currentLightButton.getLight().setState(Light.ON);
								currentLightButton.setBackground(activeButtonColor);
							}
						}
					}															
					
				}			
			});

			// Add the new button to the column of lights
			lightButtonColumn.add(currentNoteButton);
			initialNote = initialNote + 1;
		}
	}

	/**
	 * @param lightButtonColumn
	 */
	/**
	 * @param lightButtonColumn
	 */
	private void addLightColumn(ArrayList<LightButton> lightButtonColumn) {

		int initialNote = 60;

		for (int numButtons = 0; numButtons < numberOfButtonRows; numButtons++) {
			Light light = new Light();
			light.setState(Light.OFF);
			light.setNote(initialNote);

			final LightButton currentLightButton = new LightButton();
			currentLightButton.setLight(light);
			currentLightButton.setText("");
			currentLightButton.setFont(new Font(Font.SANS_SERIF, 1, 10));
			currentLightButton.setHorizontalAlignment(SwingConstants.LEFT);
			currentLightButton.setVerticalAlignment(SwingConstants.BOTTOM);
			currentLightButton.setForeground(buttonTextColor);
			currentLightButton.setBackground(normalButtonColor);		

			currentLightButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent event) {
					new LightButtonMidiWorker(currentLightButton, true).execute();
				}
			});

			// Disabled post for x, y issues
			//currentLightButton.addMouseMotionListener(mouseMotionListener);
			currentLightButton.addMouseListener(mouseListener);

			// Add the new button to the column of lights
			lightButtonColumn.add(currentLightButton);
			initialNote = initialNote + 1;
		}
	}

	public class LightButtonMidiWorker extends SwingWorker<Object, Object> {

		private LightButton button;
		private boolean fade;
		
		public LightButtonMidiWorker(LightButton button, boolean fade) {
			this.button = button;
			this.fade = fade;
		}

		@Override
		protected Object doInBackground() throws Exception {

			if (button.getLight().getState() == Light.ON) {
				if (fade) fadeColors(button, normalButtonColor, activeButtonColor, 6);
				else button.setBackground(normalButtonColor);
			} else {
				if (fade) fadeColors(button, activeButtonColor, normalButtonColor, 6);
				else button.setBackground(activeButtonColor); 
			}

			button.getLight().flipStatus();

			return null;
		}

	}

	public class ControlButtonWorker extends SwingWorker<Object, Object> {

		private ControlButton button;

		public ControlButtonWorker(ControlButton controlButton) {
			this.button = controlButton;
		}

		@Override
		protected Object doInBackground() throws Exception {

			// If all on turn off, else turn on
			if (button.isAllLightsOn()) {
				for (LightButton lightButton : button.getLightButtonList()) {
					lightButton.getLight().setState(Light.OFF);
					fadeColors(lightButton, normalButtonColor,
							activeButtonColor, 0);
				}
			} else {
				for (LightButton lightButton : button.getLightButtonList()) {
					lightButton.getLight().setState(Light.ON);
					fadeColors(lightButton, activeButtonColor,
							normalButtonColor, 0);
				}
			}

			return null;
		}
	}

	public class MainDaemonWorker extends SwingWorker<Object, Object> {

		public MainDaemonWorker() {
		}

		@Override
		protected Object doInBackground() throws Exception {

			ControlButton controlButton = null;

			// If all on turn off, else turn on
			while (daemonRunningFlag) {

				for (int i = 1; i < controlButtons.size(); i++) {

					controlButton = controlButtons.get(i);
					// controlButton.setBackground(Color.BLACK);
					controlButton.setBackground(buttonTextColor);
					controlButton.setForeground(Color.BLACK);

					// Check all light buttons, optimize?
					for (LightButton lightButton : controlButton
							.getLightButtonList()) {
						if (lightButton.getLight().getState() == Light.ON) {
							int note = lightButton.getLight().getNote();
							if (note != 0) {
								System.out.println("Note " + note);
							}

							// TOOD: Find out why this is a such a short sound
							// and we need to send two notes!!!
							midiController.sendMidiMessage(2, note, 100);
							// Thread.sleep(noteLength);
							midiController.stopMidiMessage(note, 100);
						}
					}

					Thread.sleep(sleepTime);
					controlButton.setBackground(normalButtonColor);
					controlButton.setForeground(buttonTextColor);
					
					if (!daemonRunningFlag) {
						break;
					}
				}
			}
			
			return null;
		}
	}

	private void fadeColors(final JButton button, final Color targetColor,
			final Color origColor, final int fadeSleep) {

		new SwingWorker<Object, Object>() {

			protected Object doInBackground() throws Exception {

				// Pick a reference point
				int inc = 1;

				int r = origColor.getRed();
				int g = origColor.getGreen();
				int b = origColor.getBlue();

				Color finalColor = null;

				int tr = targetColor.getRed();
				int tg = targetColor.getGreen();
				int tb = targetColor.getBlue();

				while (r != tr || g != tg || b != tb) {

					// which direction
					if (r > targetColor.getRed())
						r = r - inc;
					if (r < targetColor.getRed())
						r = r + inc;
					if (g > targetColor.getGreen())
						g = g - inc;
					if (g < targetColor.getGreen())
						g = g + inc;
					if (b > targetColor.getBlue())
						b = b - inc;
					if (b < targetColor.getBlue())
						b = b + inc;
					
					finalColor = new Color(r, g, b);
					button.setBackground(finalColor);

					if (fadeSleep != 0)
						Thread.sleep(fadeSleep);
				}

				finalColor.toString();

				return null;
			}

		}.execute();
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
		midiController.close();
	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {
		SynomeGui midiLightGrid = new SynomeGui();
	}
}
