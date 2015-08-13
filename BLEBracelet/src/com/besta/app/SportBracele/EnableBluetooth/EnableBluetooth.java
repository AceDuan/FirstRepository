package com.besta.app.SportBracele.EnableBluetooth;

import android.app.Activity;

import com.besta.app.SportBracele.BLE.BluetoothTool;
import com.besta.app.SportBracele.EnableBluetooth.EnableBluetoothHandler.ActionCallback;

public class EnableBluetooth {

	public static boolean enableBluetooth(Activity context, ActionCallback callback){
		if ( BluetoothTool.isBluetoothEnabled(context) ){
			
			callback.ActionAfterEnableBluetooth();
			return true;
		}			
		
		EnableBluetoothHandler handler = new EnableBluetoothHandler(callback);
		
		handler.sendEmptyMessage(EnableBluetoothHandler.SHOW_ENABLE_DIALOG);
		
		
		return false;
	}
	
}
