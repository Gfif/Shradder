package com.example.shredder;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//import android.text.method.BaseMovementMethod;

public class MainActivity extends Activity {
	
	private List<String> messages;
	private Socket sock;
	private BufferedReader sockIn;
	private PrintWriter sockOut;
	private boolean connected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView textView = (TextView)findViewById(R.id.chat_area);
		textView.setMovementMethod(new ScrollingMovementMethod());
		messages = new LinkedList<String>();
		connected = false;
		new ConnectTask(this).execute("isc.tsu.ru", 1987);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public Socket getSocket() {
		return sock;
	}
	
	public void setSocket(Socket sock) {
		this.sock = sock;
	}
	
	public BufferedReader getSockIn() {
		return sockIn;
	}
	
	public void setSockIn(BufferedReader sockIn) {
		this.sockIn = sockIn;
	}
	
	public PrintWriter getSockOut() {
		return sockOut;
	}
	
	public void setSockOut(PrintWriter sockOut) {
		this.sockOut = sockOut;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isConnected() {
		return connected;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void updateText() {
		TextView textView = (TextView)findViewById(R.id.chat_area);
		StringBuffer text = new StringBuffer();
		LinkedList<String> oldmessages = new LinkedList<String>(
				Arrays.asList(textView.getText().toString().split("\n")));
		for (int i = 0; i < oldmessages.size() + messages.size() - 100; ++i) {
			oldmessages.remove();
		}
		
		for (String line: oldmessages) {
			text.append(line);
			text.append("\n");
		}
		
		synchronized(messages) {
			for (String line: messages) {
				processMessage(line);
				text.append(line);
				text.append("\n");
			}
			messages.clear();
		}
		textView.setText(text.toString());
		
	}
	
	public void processMessage(String msg) {
		if (msg.startsWith("\\topic")) {
			int index = msg.indexOf(' ');
			String topic = msg.substring(index + 1);
			TextView textView = (TextView)findViewById(R.id.topic_area);
			textView.setText(topic);
		}
	}
	
	public void addMessage(String msg) {
		synchronized(messages) {
			messages.add(msg);
		}
	}
	
	public void addSystemMessage(String msg) {
		synchronized(messages) {
			messages.add(msg);
		}
	}
	
	public void addErrorMessage(String err) {
		synchronized(messages) {
			messages.add(new String("ERRORORR: " + err));
		}
	}
	
	public boolean sendMessage(View view) {
		if (!connected) {
			new ToastMaker("Try to reconnect",
					getApplicationContext(), 1500).run(); 
			new ConnectTask(this).execute("isc.tsu.ru", 1987);
			return false;
		} else {
			EditText editText = (EditText)findViewById(R.id.input_area);
			String text = editText.getText().toString();
			addMessage(text);
			new SendTask(this).execute(text);
			updateText();
			editText.setText("");
			return true;
		}
	}

}
