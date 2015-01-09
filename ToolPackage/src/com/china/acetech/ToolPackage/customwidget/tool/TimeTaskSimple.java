package com.china.acetech.ToolPackage.customwidget.tool;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.debug.DebugTool;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Android TimeTask的使用实例
 */
public class TimeTaskSimple {

	private static TimeTaskSimple instance;
	public static TimeTaskSimple getInstance(){
		if ( instance == null )
			instance = new TimeTaskSimple();
		
		return instance;
	}
	
	private TimeTaskSimple(){
		
	}
	
	public void setIsAutoSync(boolean isChecked){
	}
	
	
	public void onCreate(){
		if ( MyApplication.getRegisterable() != null ){
			MyApplication.getRegisterable().addListener(PropertyRegisterable.BLE_CONNECT_STATE, propertyListener);
			MyApplication.getRegisterable().addListener(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE, propertyListener);
			MyApplication.getRegisterable().addListener(PropertyRegisterable.BLE_SYNC_OVER, propertyListener);
		}
		reset();
	}
	
	public void onDestroy(){
		if ( MyApplication.getRegisterable() != null ){
			MyApplication.getRegisterable().removeListener(PropertyRegisterable.BLE_CONNECT_STATE, propertyListener);
			MyApplication.getRegisterable().removeListener(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE, propertyListener);
			MyApplication.getRegisterable().removeListener(PropertyRegisterable.BLE_SYNC_OVER, propertyListener);
		}
		cancel();
	}
	
	Timer tim = new Timer();
    TimerTask mTask = new MyTimerTask();
	boolean isSchedule = false;
	
	public void cancel() {
		if ( tim != null )
			tim.cancel();
		if ( mTask != null )
			mTask.cancel();
		
		tim = null;
		mTask = null;
		
		isSchedule = false;
	}


	public void reset() {
		if ( isSchedule ) return;
		
		if ( tim == null ){
			tim = new Timer();
			mTask = new MyTimerTask();
		}
			
		tim.schedule(mTask, 5000, 1000*60*15);
		
		isSchedule = true;
	}
	
	private class MyTimerTask extends TimerTask{

		@Override
		public void run() {
			try{
//				if ( MySavedState.SaveFlagOrInfo.isAutoSync() ){
//					if( !BLETool.getInstance().isBluetoothEnabled() ){
//						InfoSyncManager.syncBaseInfo();//這裡的sync需要在有looper的線程中創建
//						return;
//					}
//
//					ConnectivityManager connectMgr = (ConnectivityManager) SBApplication.getTopApp()
//					        .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//					NetworkInfo info = connectMgr.getActiveNetworkInfo();
//
//					if ( info != null && info.getType() == ConnectivityManager.TYPE_WIFI ){
//
//					}
//					 //如果需要在wifi環境下進行同步的話需要把代碼移動至上方
//					if ( SBApplication.getRegisterable() != null ){
//						SBApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_START_SYNC, null, PropertyRegisterable.START_SYNC_ALL);
//					}
//
//					mIsSendSyncMessage = true;
//				}
//				else{
//					InfoSyncManager.syncBaseInfo();
//				}
				DebugTool.show("");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	
	private boolean mIsSendSyncMessage = false;
	
	private PropertyChangeListener propertyListener = new PropertyChangeListener(){

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if ( !mIsSendSyncMessage ) return;
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE) ){
				mIsSendSyncMessage = false;
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_SYNC_OVER)){
				mIsSendSyncMessage = false;
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_CONNECT_STATE)){
				if ( event.getNewValue().equals(PropertyRegisterable.BLE_DISCONNECTED)){
					mIsSendSyncMessage = false;
				}
			}
		}
		
	};
}
