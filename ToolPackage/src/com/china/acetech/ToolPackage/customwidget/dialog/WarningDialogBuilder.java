package com.china.acetech.ToolPackage.customwidget.dialog;

import android.app.Activity;
import android.app.AlertDialog;

public class WarningDialogBuilder {

	Activity mContext;
	
	String mTitle;
	String mMessage;
	
	public WarningDialogBuilder(Activity context){
		
		mContext = context;
	}
	
	public WarningDialogBuilder setTitle(String title){
		mTitle = title;
		return this;
	}
	public WarningDialogBuilder setTitle(int titleID){
		mTitle = mContext.getResources().getString(titleID);
		return this;
	}
	
	public WarningDialogBuilder setMessage(String message){
		mMessage = message;
		return this;
	}
	public WarningDialogBuilder setMessage(int messageID){
		mMessage = mContext.getResources().getString(messageID);
		return this;
	}
	
	public void show(){
		mContext.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				new AlertDialog.Builder(mContext)
				.setTitle(mTitle)
				.setMessage(mMessage)
				.show();
			}
		});
	}
}
