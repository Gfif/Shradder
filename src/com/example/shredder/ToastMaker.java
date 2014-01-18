package com.example.shredder;

import android.content.Context;
import android.widget.Toast;


public class ToastMaker implements Runnable {
	private String msg;
	private Context cntx;
	private int len;
	
	
	public ToastMaker(String msg, Context cntx, int len) {
		this.msg = msg;
		this.cntx = cntx;
		this.len = len;
	}
	
	@Override
	public void run() {
		Toast toast = Toast.makeText(cntx, msg, len);
		toast.show();
	}

}