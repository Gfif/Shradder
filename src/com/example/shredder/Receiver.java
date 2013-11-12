package com.example.shredder;

import java.io.IOException;

public class Receiver implements Runnable {
	
	MainActivity activity;
	
	public Receiver(MainActivity activity) {
		this.activity = (MainActivity)activity;
	}
	
	public void run() {
		try {
			String line = activity.getSockIn().readLine();
			while (line != null) {
				activity.addMessage(line.trim());
				activity.runOnUiThread(new Runnable() {
					public void run() {
						activity.updateText();
					}
				});
				line = activity.getSockIn().readLine();
			}
		} catch (IOException ioex) {
			activity.connected = false;
		}
	}

}
