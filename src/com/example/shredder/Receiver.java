package com.example.shredder;

import java.io.IOException;

import android.app.Activity;
import android.util.Log;


public class Receiver implements Runnable {
	
	private static final String TAG = "Reciever";
	
	MainActivity activity;
	
	public Receiver(Activity activity) {
		this.activity = (MainActivity)activity;
	}
	
	public void run() {
		try {
			String line = activity.getSockIn().readLine();
			while (line != null) {
				Log.d(TAG, "adding new line: " + line);
				activity.addMessage(line.trim());
				activity.runOnUiThread(new Runnable() {
					public void run() {
						activity.updateText();
					}
				}); 
				line = activity.getSockIn().readLine();
			}
		} catch (IOException ioex) {
			Log.d(TAG, "IOException");
			activity.addMessage("<-- Disconnected -->");
			activity.runOnUiThread(new Runnable() {
				public void run() {
					activity.updateText();
				}
			});
			activity.setConnected(false);
		}
	}

}
