package com.example.shredder;

//import android.view.View;
import java.io.PrintWriter;


public aspect crypto {
	private final int ID_LEN = 4;
	private int seqNumber;
	private String ident;
	
	pointcut sending(String text):
		within(SendTask) && args(text) && call(void PrintWriter.println(..));
	
	pointcut recieving(String msg):
		call(void addMessage(String)) && args(msg) && withincode(void Receiver.run());
	
	pointcut addingSelfMessage(String msg):
		call(void addMessage(String)) && args(msg) && !withincode(void Receiver.run());
	
	pointcut connecting(ConnectTask ct):
		target(ct) && call(* execute(*));
	
	void parseMessage() {
		//Message parsing, check type, verify message.
	}
		
	private String wrapMessage(String msg) {
		//adding message type, sequence number, message length.
		String wrappedMessage = new String();
		wrappedMessage += ident;
		wrappedMessage += msg;
		return wrappedMessage;
	}
	
	void around(String text): sending(text) {
		seqNumber++;
		proceed(text);
	}
	
	void around(String msg): recieving(msg) {
		proceed("Krang: " + msg);
	}
	void around(String msg): addingSelfMessage(msg) {
		proceed("Me: " + msg + " # SeqNumber: " + Integer.toString(seqNumber));
	}
	
	after(ConnectTask ct) : connecting(ct) {
		new SendTask(ct.getActivity()).execute("Hi, Krang, I'm Leonardo!");
	}
}