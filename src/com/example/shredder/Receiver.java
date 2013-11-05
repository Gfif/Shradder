package com.example.shredder;

import java.io.IOException;

import android.app.Activity;

public class Receiver implements Runnable {
	
	MainActivity activity;
	
	public Receiver(Activity activity) {
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
			// ...
		}
	}

}
