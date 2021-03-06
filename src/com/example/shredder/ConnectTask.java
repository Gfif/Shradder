package com.example.shredder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


public class ConnectTask extends AsyncTask<Object, Void, Void> {

	private MainActivity activity;
	private Socket sock;
	private BufferedReader sockIn;
	private PrintWriter sockOut;
	private ProgressDialog dialog;
		
	public ConnectTask(Activity activity) {
		super();
		this.activity = (MainActivity)activity;
	}
	
	public MainActivity getActivity() {
		return this.activity;
	}
	
	@Override
	protected Void doInBackground(Object... params) {
		String host = (String)params[0];
		int port = ((Integer)params[1]).intValue();
		reconnectSocket(host, port);
		return null;
	}
	
	private boolean checkNetwork() {
		ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null || ! activeNetwork.isConnectedOrConnecting()) { 
			return false;
		}
		return true;
	}
	
	private ProgressDialog createWaitDialog() {
		return ProgressDialog.show(activity, "", "Call to X...", true);
	}
	
	private void reconnectSocket(final String host, final int port) {
		if (!checkNetwork()) {
			activity.runOnUiThread(new ToastMaker("Please, enable networking...",
					activity.getApplicationContext(), 3000));
		}
		else {
			activity.setConnected(true);
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog = createWaitDialog();
				}
			});
			try {
				sock = new Socket(host, port);
				sock.setSoTimeout(5 * 1000 * 10);
				sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				sockOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
			} 
			catch (UnknownHostException uhex) {
				activity.runOnUiThread(new ToastMaker("Unknown Host",
						activity.getApplicationContext(), 3000));
				activity.setConnected(false);
			}
			catch (IOException ioex) {
				activity.runOnUiThread(new ToastMaker("Connection to server is lost",
						activity.getApplicationContext(), 3000));
				activity.setConnected(false);
			}
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
				}
			});
		}
	}
	
	@Override
	protected void onPostExecute(Void none) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (activity.isConnected()) {
					activity.addSystemMessage("\n--Connected--");
					activity.runOnUiThread(new Runnable() {
						public void run() {
							activity.updateText();
						}
					});
					activity.setSocket(sock);
					activity.setSockIn(sockIn);
					activity.setSockOut(sockOut);
					new Thread(new Receiver(activity)).start();
				}
			}
		});
	}

}
