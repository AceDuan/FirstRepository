package com.china.acetech.ToolPackage.ble.services;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.china.acetech.ToolPackage.debug.DebugTool;

public class BluetoothLeReceiver extends BroadcastReceiver{

	TargetFragment mTargetActivity;
	public BluetoothLeReceiver( TargetFragment activity ){
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

		DebugTool.show(mTargetActivity.getClass().getSimpleName() + " 's ");
		DebugTool.show(action);
		
		//Receiver暫時沒有其他特殊處理，只負責消息發送，所有收到的消息全部進行轉發
		mTargetActivity.handleStateMessage(intent);
//		if ( BluetoothLeService.ACTION_GATT_CONNECTED.equals(action) ){
//			;
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action) ){
//			;
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action) ){
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action) ){
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_GATT_ON_CHARACTERISTIC_WRITE.equals(action) ){
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_GATT_READ_REMO_RSSI.equals(action)){
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_DATA_AVAILABLE_FAIL.equals(action)){
//			//not using. wait for add.
//			//ServiceActivity.this.handleStateMessage("com.billy.bluetooth.le.ACTION_DATA_AVAILABLE_FAIL");
//		}
//		else if ( BluetoothLeService.ACTION_GATT_NOTIFITION_SETTED.equals(action)){
//			mTargetActivity.handleStateMessage(intent);
//		}
//		else if ( BluetoothLeService.ACTION_GATT_BATTARY_READ.equals(action) ){
//			mTargetActivity.handleStateMessage(intent);
//		}
	}
	public static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_ON_CHARACTERISTIC_WRITE);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_READ_REMO_RSSI);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_NOTIFITION_SETTED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE_FAIL);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_BATTARY_READ);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DESCRIPTOR_WRITE);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED_FAIL);
		
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_FACTORY_INFO_READ);
		
		return intentFilter;
	}

	public static interface TargetFragment{
		public void handleStateMessage(Intent intent);
	}
	
	protected boolean mIsWorking = true;
	public void setWorkFlag(boolean isWorking){
		mIsWorking = isWorking;
	}
}
