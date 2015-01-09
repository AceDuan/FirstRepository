package com.china.acetech.ToolPackage.ble.services;

import android.content.Context;
import android.os.Handler;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.ble.bletool.EnableBluetooth;
import com.china.acetech.ToolPackage.ble.bletool.EnableBluetoothHandler;
import com.china.acetech.ToolPackage.debug.DebugTool;


public class BLEServiceManagerWorker extends BLEServiceManagerKeeper{

	/********************************************************************/
	/****************     BLE service logic start     *******************/
	/********************************************************************/

	private boolean isRegisterReceiver;

	public BLEServiceManagerWorker( Context context, boolean isNeedScanWhenCreate ){

		mContext = context;
		mIsNeedScanWhenCreate = isNeedScanWhenCreate;
		
		mServiceConnection = new CustomConnection();
		EnableBluetoothHandler.ActionCallback callback = new EnableBluetoothHandler.ActionCallback() {
			
			@Override
			public Context getContext() {
				return mContext;
			}
			
			@Override
			public void clear() {
			}
			
			@Override
			public void ActionAfterEnableBluetooth() {
				mServiceConnection.startBindService(mContext);
			}
		};
		
		if ( !DebugTool.isDebug() )
        	EnableBluetooth.enableBluetooth(callback);
		
		
		mRequestProcesser = new BroadcastSender();
		mScanedProcesser = new DeviceScanProcsser();
		mGattUpdateReceiver = new BluetoothLeReceiver(mRequestProcesser);
		
		mRequestQueue = new BLERequestQueue();
		mRequestQueue.setProcesser(new RequestProcesser());
		
		mBaseSportsyncListener = new BaseSportSyncListener();

		//这个在会注册到sync管理类中，这个没有定义，但是使用过程很简单，不做赘述
		//InfoSyncManager.getBaseSportSyncManager().setConnectReturnListener(mBaseSportsyncListener);
		
		isRegisterReceiver = false;
	}
	
	public void init(){//這個和create生命週期不太一樣，進入以後才有可能會初始化
		mContext.registerReceiver(mGattUpdateReceiver, 
				BluetoothLeReceiver.makeGattUpdateIntentFilter());
		isRegisterReceiver = true;
		
		if ( MyApplication.getRegisterable() != null ){
			MyApplication.getRegisterable().addListener(propertyListener);
		}
		
		//第一次進入時對時序進行初始化，但是由於進入時同時要進行網絡同步，如果兩個一起開始會起衝突，解決辦法是進入後10秒再開始設置時序相關的‘
		
		
//		new Handler().postDelayed(new Runnable() {
//			public void run() {
//				AutoSyncManager.getInstance().onCreate();
//			}
//		}, 1000L);
		
	}
	
	public void onResume(){
		if ( !isRegisterReceiver ){
		mContext.registerReceiver(mGattUpdateReceiver, 
				BluetoothLeReceiver.makeGattUpdateIntentFilter());
		isRegisterReceiver = true;
		}
	}
	
	public void onPause(){
		if ( isRegisterReceiver ){
			mContext.unregisterReceiver(mGattUpdateReceiver);
			isRegisterReceiver = false;
		}
	}

	public void onDestroy(){
		
		if ( mBluetoothLeService != null ){
			mContext.unbindService(mServiceConnection);
			mBluetoothLeService = null;
		}
		
		mContext.unregisterReceiver(mGattUpdateReceiver);
		
		mContext = null;
		
		if ( MyApplication.getRegisterable() != null ){
			MyApplication.getRegisterable().removeListener(propertyListener);
		}
		
		//AutoSyncManager.getInstance().onDestroy();
	}
	
	 
	public void setWorkFlag(boolean isWorking){
		if ( mGattUpdateReceiver != null ){
			((BluetoothLeReceiver)mGattUpdateReceiver).setWorkFlag(isWorking);
		}
		
	}
	
}
