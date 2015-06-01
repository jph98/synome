package com.oogly.gui;

import java.util.ArrayList;

import javax.swing.JButton;

import com.oogly.model.Light;

public class ControlButton extends JButton {
	
	private static final long serialVersionUID = -3379181570790519662L;
	
	private ArrayList<LightButton> lightButtonColumn = new ArrayList<LightButton>(10);
	
	public ControlButton(String text) {
		super(text);
	}

	public ControlButton() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<LightButton> getLightButtonList() {
		return lightButtonColumn;
	}

	public void setLightButtonList(ArrayList<LightButton> lightButtonColumn) {
		this.lightButtonColumn = lightButtonColumn;
	}
	
	public LightButton getLightButton(int num) {
		return lightButtonColumn.get(num);
	}
	
	public LightButton getFirstLightButton() {
		return lightButtonColumn.get(0);
	}

	public boolean isAllLightsOn() {	
		
		// Look for negative light state
		for (LightButton lightButton: lightButtonColumn) {
			if (lightButton.getLight().getState() == Light.OFF) {
				return false;
			}
		}
		
		return true;
	}	
}
