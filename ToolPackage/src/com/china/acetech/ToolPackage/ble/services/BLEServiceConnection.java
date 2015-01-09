package com.china.acetech.ToolPackage.ble.services;

import java.util.Timer;
import java.util.TimerTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.china.acetech.ToolPackage.debug.DebugTool;


/**
 * 用來建立BLE連接的工具類
 * @author bxc2010011
 *
 */
public abstract class BLEServiceConnection implements ServiceConnection{
	
	public void startBindService(final Context context ){
		
//		new Handler().postDelayed(new Runnable() {
//			public void run() {
//				Intent getServiceIntent = new Intent(context,
//						BluetoothLeService.class);
//				boolean isbind = context.bindService(getServiceIntent,
//						BLEServiceConnection.this, Context.BIND_AUTO_CREATE);
//				DebugTool.show("" + isbind);
//			}
//		}, 2000L);
		
		new Timer().schedule(new TimerTask(){

			@Override
			public void run() {
				Intent getServiceIntent = new Intent(context,
						BluetoothLeService.class);
				boolean isbind = context.bindService(getServiceIntent,
						BLEServiceConnection.this, Context.BIND_AUTO_CREATE);
				DebugTool.show("" + isbind);
			}
			
		}, 2000L);
	}
	
	
	BluetoothLeService ble_service;
	@Override
	public void onServiceConnected(ComponentName componentName,
			IBinder service) {
		ble_service = ((BluetoothLeService.LocalBinder) service)
				.getService();
		if (!ble_service.initialize()) {
			DebugTool.show("Unable to initialize Bluetooth");
		}
		// Automatically connects to the device upon successful start-up
		// initialization.
		DebugTool.show("service is started");
		//mBluetoothLeService.connect("");
		setBLEService(ble_service);
		
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
		DebugTool.show("service is stoped");
		ble_service.disconnect();
		ble_service.close();
		ble_service = null;
	}
	
	/**
	 * 將初始化好的service交給使用者
	 * @param ble_service
	 */
	public abstract void setBLEService(BluetoothLeService ble_service);
	public abstract void startScanDevice();
	
	private boolean mIsMatchKeyWindow = false;
	public boolean isMatchKeyWindow(){
		return mIsMatchKeyWindow;
	}
	public void setIsMatchKeyWindow(boolean isWindow){
		mIsMatchKeyWindow = isWindow;
	}
}
