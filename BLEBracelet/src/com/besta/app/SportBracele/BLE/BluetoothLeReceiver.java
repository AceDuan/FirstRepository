package com.besta.app.SportBracele.BLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.besta.app.SportBracele.Luncher.ServiceActivity;

public class BluetoothLeReceiver extends BroadcastReceiver{

	ServiceActivity mTargetActivity;
	public BluetoothLeReceiver( ServiceActivity activity ){
		mTargetActivity = activity;
	}
	
	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read
	// or notification operations.
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		if ( BluetoothLeService.ACTION_GATT_CONNECTED.equals(action) ){
			;
			mTargetActivity.invalidateOptionsMenu();
			mTargetActivity.handleStateMessage(action);
			mTargetActivity.HandlerForRssi(action);
		}
		else if ( BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action) ){
			;
			mTargetActivity.invalidateOptionsMenu();
			mTargetActivity.handleStateMessage(action);
			mTargetActivity.HandlerForRssi(action);
			
			mTargetActivity.disconnectedWithDevice();
		}
		else if ( BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action) ){
			mTargetActivity.handleStateMessage(action);
			mTargetActivity.displayService();
			mTargetActivity.HandlerForRssi(action);
		}
		else if ( BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action) ){
			mTargetActivity.setInfomation(intent);
		}
		else if ( BluetoothLeService.ACTION_GATT_ON_CHARACTERISTIC_WRITE.equals(action) ){
			mTargetActivity.handleStateMessage(intent);
		}
		else if ( BluetoothLeService.ACTION_GATT_READ_REMO_RSSI.equals(action)){
			mTargetActivity.handleStateMessage(intent);
		}
		else if ( BluetoothLeService.ACTION_DATA_AVAILABLE_FAIL.equals(action)){
			//not using. wait for add.
			//ServiceActivity.this.handleStateMessage("com.billy.bluetooth.le.ACTION_DATA_AVAILABLE_FAIL");
		}
	}

}
