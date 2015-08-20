package com.china.acetech.ToolPackage.ble.bletool;


public class EnableBluetooth {

	public static boolean enableBluetooth(EnableBluetoothHandler.ActionCallback callback){
		if ( BLETool.getInstance().isBluetoothEnabled() ){
			
			callback.ActionAfterEnableBluetooth();
			return true;
		}			
		
		EnableBluetoothHandler handler = new EnableBluetoothHandler(callback);
		
		handler.sendEmptyMessage(EnableBluetoothHandler.SHOW_ENABLE_DIALOG);
		
		
		return false;
	}
	
	public static boolean enableBluetoothWithoutDialog(EnableBluetoothHandler.ActionCallback callback){
		if ( BLETool.getInstance().isBluetoothEnabled() ){
			
			callback.ActionAfterEnableBluetooth();
			return true;
		}			
		
		EnableBluetoothHandler handler = new EnableBluetoothHandler(callback);
		
		handler.sendEmptyMessage(EnableBluetoothHandler.ENABLE_START);
		
		
		return false;
	}
	
}
