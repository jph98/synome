package com.oogly.gui;

import javax.swing.JButton;

import com.oogly.model.Light;

public class LightButton extends JButton {

	private static final long serialVersionUID = 3188787962049608197L;	
	
	private Light light;

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}
}
