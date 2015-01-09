package com.china.acetech.ToolPackage.ble.bletool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.debug.DebugTool;

/**
 * 根据警告信息，在Android5.0的时候已经开始采用新的bleScan接口。有时间可以研究一下
 */
public class BLETool {

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothAdapter.LeScanCallback mLeScanCallback;
	private static final long SCAN_PERIOD = 10000;
	
	private BLETool(){
		initAdapter();
		mLeScanCallback = new MyLeScanCallback();
		isFindDevice = false;
	}
	private static BLETool instance;
	public static BLETool getInstance(){
		if ( instance == null )
			instance = new BLETool();
		return instance;
	}
	
	public static boolean testForIssupportBLE() {

		if (!MyApplication.getTopApp().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			// Toast.makeText(this, R.string.ble_not_supported,
			// Toast.LENGTH_SHORT).show();
			return false;
		}

		// final BluetoothManager bluetoothManager = (BluetoothManager)
		// SBApplication.getTopApp().getSystemService(Context.BLUETOOTH_SERVICE);
		// mBluetoothAdapter = bluetoothManager.getAdapter();
		//
		//
		// if (mBluetoothAdapter == null) {
		// Toast.makeText(this, R.string.bluetooth_not_supported,
		// Toast.LENGTH_SHORT).show();
		// finish();
		// return false;
		// }

		return true;
	}
	
	public static boolean isSupportBLE(Activity context){
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        
	}
	
	public static boolean isSupportBluetooth(Activity context){
//		BluetoothManager manager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE); 
//		return manager.getAdapter() != null;
		return true;
	}
	
	public boolean isBluetoothEnabled(){
		if ( mBluetoothAdapter == null )	initAdapter();
		
		return mBluetoothAdapter.isEnabled();

	}
	
	public void stopScanBLEDevice(){
		isSuccessAndStoped = true;
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}
	
	private boolean isFindDevice;
	private boolean isSuccessAndStoped;
	public void scanLeDevice(boolean isenable){
		if ( isenable ){
			isFindDevice = false;
			isSuccessAndStoped = false;
			new Thread(){

				@Override
				public void run() {
					boolean isStartScan = mBluetoothAdapter.startLeScan(mLeScanCallback);
					DebugTool.show("start le scan ");
					if ( !isStartScan )
						return;
					try {
						//sleep(SCAN_PERIOD);
						for( int i = 0; i < SCAN_PERIOD; i += 100){
							if ( isSuccessAndStoped ){
								return;
							}
							sleep(SCAN_PERIOD/100);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					DebugTool.show("stop le scan ");
					if ( !isFindDevice ){//未找到設備，需要通知此事件
						mlistener.ScanFail();
					}
				}
				
			}.start();

		}
		else{
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			DebugTool.show("stop le scan ");
			isSuccessAndStoped = true;
		}
	}
	
	private void initAdapter(){
		BluetoothManager manager = (BluetoothManager)MyApplication.getTopApp().getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = manager.getAdapter();
	}
	
	private class MyLeScanCallback implements BluetoothAdapter.LeScanCallback {
		
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			DebugTool.show("scaned device Address: " + device.getAddress() );
			DebugTool.show("scaned device Name: " + device.getName() );
			if ( device.getName() != null && device.getName().equals("AddOn SB-100")){
			//if ( device.getName().equals("Force")){
				if ( MyApplication.getBLEManagerWorker() != null && !MyApplication.getBLEManagerWorker().getConnection().isMatchKeyWindow() ){
//					if ( !MySavedState.LocalSaveInfo.getDeviceAddress().equals(device.getAddress()) ){
//						DebugTool.show("Address is different!");
//						return;
//					}
					
				}
				scanLeDevice(false);
				isFindDevice = true;
				//發送連接請求
				if ( mlistener != null ){
					mlistener.ScanSucess(device.getAddress());
				}
			}
		}
	}
	
	public void setListener(DeviceScan listener ){
		mlistener = listener;
	}
	private DeviceScan mlistener;
	public static interface DeviceScan{
		public void ScanSucess(String address);
		public void ScanFail();
	}
}
