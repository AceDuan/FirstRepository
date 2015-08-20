package com.china.acetech.ToolPackage.ble.bletool;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.customwidget.dialog.WaitingDialogBuilder;
import com.china.acetech.ToolPackage.debug.DebugTool;


public class EnableBluetoothHandler extends Handler implements DialogInterface.OnClickListener{

	protected static final int ENABLE_FINISHED	= 1;
	protected static final int ENABLE_START		= 2;
	protected static final int ENABLE_CANCEL	= 3;
	protected static final int ENABLE_ERROR		= 4;
	
	protected static final int SHOW_ENABLE_DIALOG = 5;
	
	AlertDialog mDialog;
	ActionCallback mCallback;
	
	EnableBluetoothHandler(ActionCallback callback ){
		mCallback = callback;
	}
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what ){
		case ENABLE_START:
			new Thread() {
				
				@Override
				public void run() {
					startEnableBluetooth();
				}
			}.start();
			
			mDialog = WaitingDialogBuilder.create(mCallback.getContext());
			mDialog.show();
			
			break;
		case ENABLE_FINISHED:
			new Thread() {
				
				@Override
				public void run() {
					try {
						sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mDialog.dismiss();
					mCallback.ActionAfterEnableBluetooth();
					clear();
				}
			}.start();
			
			
			break;
		case ENABLE_CANCEL:
			clear();
			break;
		case ENABLE_ERROR:
			clear();
		case SHOW_ENABLE_DIALOG:
			showEnableDialod();
			break;
		default:
			break;
		}
		
	}
	
	public void clear(){
		if ( mCallback != null )
			mCallback.clear();
		mCallback = null;
		
		mDialog = null;
	}
	
	private void showEnableDialod(){
		new AlertDialog.Builder(mCallback.getContext() )
		.setTitle(R.string.dialog_bluetooth_enable_title)
		.setMessage(R.string.dialog_bluetooth_enable_message)
		.setPositiveButton(R.string.dialog_yes,this)
		.setNegativeButton(R.string.dialog_no,this)
		.show();
	}
	
	private void startEnableBluetooth(){
		DebugTool.show("Start enable");
		LocalBluetoothAdapter localAdapter = LocalBluetoothAdapter.getInstance();
		if ( localAdapter == null ){
        	DebugTool.show("Error: there's a problem starting Bluetooth");
        }
		
		int btState = 0;

		//this is copied from android source code
        try {
            // TODO There's a better way.
            int retryCount = 30;
            do {
                btState = localAdapter.getBluetoothState();
                Thread.sleep(100);
                DebugTool.show(""+btState);
            } while (btState == BluetoothAdapter.STATE_TURNING_OFF && --retryCount > 0);
            
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            // don't care
        }

        
        if (btState == BluetoothAdapter.STATE_TURNING_ON
                || btState == BluetoothAdapter.STATE_ON
                || localAdapter.enable()) {
            this.sendEmptyMessage(EnableBluetoothHandler.ENABLE_FINISHED);
        } else {
            this.sendEmptyMessage(EnableBluetoothHandler.ENABLE_ERROR);
        }
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which){
		case DialogInterface.BUTTON_POSITIVE:
			
			sendEmptyMessage(EnableBluetoothHandler.ENABLE_START);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			
			sendEmptyMessage(EnableBluetoothHandler.ENABLE_CANCEL);
			break;
		default:
			break;
		}
	}
	
	public static interface ActionCallback {
		public void ActionAfterEnableBluetooth();
		
		public Context getContext();
				
		public void clear();
	}
}
