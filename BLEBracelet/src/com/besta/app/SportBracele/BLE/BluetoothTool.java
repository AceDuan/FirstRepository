package com.besta.app.SportBracele.BLE;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

public class BluetoothTool {

	public static boolean isSupportBLE(Activity context){
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        
	}
	
	public static boolean isSupportBluetooth(Activity context){
		BluetoothManager manager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE); 
		return manager.getAdapter() != null;
	}
	
	public static boolean isBluetoothEnabled(Activity context){
		BluetoothManager manager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE); 
		return manager.getAdapter().isEnabled();
	}
	
	
	
	
}


