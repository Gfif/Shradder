package com.example.shredder;

import java.io.PrintWriter;

import android.app.Activity;
import android.os.AsyncTask;

public class SendTask extends AsyncTask<String, Void, Void> {
	
	private MainActivity activity;
	
	public SendTask(Activity activity) {
		super();
		this.activity = (MainActivity)activity;
	}
	
	@Override
	protected Void doInBackground(String... messages) {
		PrintWriter sockOut = activity.getSockOut();
		for (String message: messages) {
			sockOut.println(message);
			sockOut.flush();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void none) {
		// ...
	}

}
