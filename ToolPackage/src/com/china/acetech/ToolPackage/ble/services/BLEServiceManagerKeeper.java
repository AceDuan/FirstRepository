package com.china.acetech.ToolPackage.ble.services;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.MySavedState;
import com.china.acetech.ToolPackage.ble.attributes.BLEConfig;
import com.china.acetech.ToolPackage.ble.bletool.BLETool;
import com.china.acetech.ToolPackage.ble.web.BaseSportInfoSync;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;
import com.china.acetech.ToolPackage.debug.DebugTool;


public class BLEServiceManagerKeeper {
 
	//*************text code*******************
	public void writeData(byte[] order){
		//這裡要去判斷連接是否存在。
		BluetoothGattCharacteristic reveive = mRightSevice.getCharacteristic(BLEConfig.getReceiverUUID());
		mBluetoothLeService.writeDataToCharacteristic(reveive, order);
	}
	
	public void writeData(String order){
		BluetoothGattCharacteristic reveive = mRightSevice.getCharacteristic(BLEConfig.getReceiverUUID());
		mBluetoothLeService.writeDataToCharacteristic(reveive, order);
	}
	//****************************************
	
	//only used for match key window
	public BLEServiceConnection getConnection(){
		return mServiceConnection;
	}
	public BluetoothLeService getBLEService(){
		return mBluetoothLeService;
	}
	public void ClearQueue(){
		mRequestQueue.clearQueue();
	}
	
	
	protected Context mContext;
	
	/********************************************************************/
	/****************     BLE service logic start      ******************/
	/********************************************************************/
	
	
	protected BroadcastReceiver mGattUpdateReceiver;
	protected BluetoothLeService mBluetoothLeService;
	protected BLEServiceConnection mServiceConnection;
	
	
	protected BluetoothGattService mRightSevice;
	
	protected BroadcastSender mRequestProcesser;
	protected DeviceScanProcsser mScanedProcesser;
	
	protected boolean mIsNeedScanWhenCreate;
	
	protected BLERequestQueue mRequestQueue;

	protected BaseSportInfoSync.ConnectedReturn mBaseSportsyncListener;

	protected PropertyChangeListener propertyListener = new PropertyChangeListener(){

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if ( mBluetoothLeService == null )
				return;
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE) ){
				mBluetoothLeService.disconnect();
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_START_SYNC)){
				String value = (String)event.getNewValue();
				if ( value.equals(PropertyRegisterable.START_SYNC_ALL ) )
					startSyncToBLEDevice();
				else
					startSyncToBLEDevice();
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_CLOSED_CONNECT)){
				if ( mBluetoothLeService != null ){
					mBluetoothLeService.disconnect();
					//mBluetoothLeService.close();
				}
				
				BLETool.getInstance().stopScanBLEDevice();
				
				if (MyApplication.getRegisterable() != null ){
					MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_CONNECT_STATE, null, PropertyRegisterable.BLE_DISCONNECTED);
				}
			}
		}
		
	};
	
	public Context getContext(){
		return mContext;
	}
	
	public boolean isConnected(){
		if ( mBluetoothLeService == null )
			return false;
		return (mBluetoothLeService.getConnectionState() == BluetoothLeService.STATE_CONNECTED);
	}
	
	public int getConnectedState(){
		if ( mBluetoothLeService == null )
			return BluetoothLeService.STATE_DISCONNECTED;
		return mBluetoothLeService.getConnectionState();
	}
		
	public void saveMatchDeviceAddress(){
		String address = mBluetoothLeService.getDeviceAddress();
		//MySavedState.LocalSaveFlagInfo.setDeviceAddress(address);
	}
	
	/**
	 * 根據所有資料的狀態來生成消息隊列，並發送第一個消息到
	 */
	public void startSyncToBLEDevice(){
		if ( !mRequestQueue.isEmpty() )
			return;
		
//		mRequestQueue.addRequestToQueue(new DataInfoOrder());
//		
//		
//		if ( MySavedState.SaveFlagOrInfo.isUserInfoReplaced() ){
//			mRequestQueue.addRequestToQueue(new UserInfoOrder());
//		}
//		
//		if ( MySavedState.SaveFlagOrInfo.isAlarmListReplaced() ){
//			mRequestQueue.addRequestToQueue(new AlarmClockOrder());
//		}
//		else if ( MySavedState.SaveFlagOrInfo.isTimeZoneReplaced() ){
//			mRequestQueue.addRequestToQueue(new AlarmClockOrder());
//		}
//		
//		if ( MySavedState.SaveFlagOrInfo.isDeviceSettingReplaced() ){
//			mRequestQueue.addRequestToQueue(new DeviceSettingOrder());
//		}
//		
//		//只有在有網絡連接的時候才可以點同步
//		ConnectivityManager connectMgr = (ConnectivityManager) MyApplication.getTopApp()
//		        .getSystemService(Context.CONNECTIVITY_SERVICE);
//		 
//		NetworkInfo info = connectMgr.getActiveNetworkInfo();
//		if ( info != null )
//			mRequestQueue.addRequestToQueue(new ReadRequestOrder());
		
		if ( !isConnected() ){//如果與藍牙設備並未建立連接，則先掃描設備
			mServiceConnection.startScanDevice();
			//mRequestQueue.addRequestToQueue(new MatchKeyOrder(), 0);
		}
		else{
			mRequestQueue.process(null);
		}
	}
	
	public void connectBLEDevice(){
		if ( mBluetoothLeService == null )
			return;
		
		if ( !isConnected() )
			mServiceConnection.startScanDevice();
	}
	
	protected class RequestProcesser implements BLERequestQueue.RequestProcess{

		@Override
		public void process(byte[] order) {
			
			mTimeOutController.startWork(order);
			
			if ( mRightSevice == null ){
				mRightSevice = mBluetoothLeService.getSupportedGattService(BLEConfig.getSBServiceUUID());
				if ( mRightSevice == null )
					return;
			}
			BluetoothGattCharacteristic reveive = mRightSevice.getCharacteristic(BLEConfig.getReceiverUUID());
			mBluetoothLeService.writeDataToCharacteristic(reveive, order);
			StringBuffer buffer = new StringBuffer();
			for ( int  l=0; l < order.length; l++){
				buffer.append("  "+Integer.toHexString(order[l]&0xFF));
			}
			DebugTool.show("Write to deivce: " + buffer.toString());
		}
		
	}
	
	protected class BroadcastSender implements BluetoothLeReceiver.TargetFragment{
		@Override
		public void handleStateMessage(Intent intent){//以後有很大機率需要把這個處理也抽象出來
			String action = intent.getAction();
			
			if ( mBluetoothLeService == null )
				return;
			
			if ( action.equals(BluetoothLeService.ACTION_GATT_CONNECTED)){//連接成功後打開手環的notify開關，並發送第一次匹配的消息
				
			}
			else if ( action.equals(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED_FAIL) ){
				mBluetoothLeService.refreshDiscoverFlag();
				
				if ( MyApplication.getRegisterable() != null )
					MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_SYNC_OVER, 
						null, null);
				
				if ( MyApplication.getRegisterable() != null )
					MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE, null, null);
			}
			else if ( action.equals(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)){
				mBluetoothLeService.refreshDiscoverFlag();
				
				UUID sb_service = BLEConfig.getSBServiceUUID();
				mRightSevice = mBluetoothLeService.getSupportedGattService(sb_service);
				
				if ( mRightSevice == null ){
					DebugTool.show("can't find service");
					mBluetoothLeService.disconnect();
				}
				else{
					BluetoothGattCharacteristic character = mRightSevice.getCharacteristic(BLEConfig.getSenderUUID());
					//mBluetoothLeService.setCharacteristicNotification(character, true);
					mBluetoothLeService.setCharacteristicNotificationByDes(character, true);
				}
			}
			else if ( action.equals(BluetoothLeService.ACTION_GATT_NOTIFITION_SETTED)){
				if (MyApplication.getRegisterable() != null ){
					MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_CONNECT_STATE, null, PropertyRegisterable.BLE_CONNECTED);
				}
				
//				if ( MyApplication.getTopApp().getBondOperation() == AbsActivity.REQUEST_CODE_UPDATE_CONNECTION ){
//					new Receiver_DFUModeOrder().sendMessage();
//				}
//				else if ( MyApplication.getTopApp().getBondOperation() == AbsActivity.REQUEST_CODE_UNBOND_BRACELET ){
//					new Receiver_BondOrder().setBondOffMessgae();
//				}
//				else{
//				//並非只有bond_bracelet才會走這邊，正常流程也會這麼進行，具體分支處理在這個類進行
//					//new Receiver_BondOrder().setBondOnMessgae();
//					new Receiver_SerialNumberOrder().setMessage();
//				}
				
			}
			else if ( action.equals(BluetoothLeService.ACTION_GATT_DESCRIPTOR_WRITE)){
				int status  = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, -1);
				if ( status != BluetoothGatt.GATT_SUCCESS ){
					return;
				}
				
				Intent sendIntent = new Intent(BluetoothLeService.ACTION_GATT_NOTIFITION_SETTED);
				if ( mContext != null )
					mContext.sendBroadcast(sendIntent);
				
//				new Receiver_VersionOrder().sendMessage();
//				new Receiver_BattaryOrder().sendMessage();
			}
			else if ( action.equals(BluetoothLeService.ACTION_GATT_ON_CHARACTERISTIC_WRITE) ){//消息發送完畢
			}
			else if ( action.equals(BluetoothLeService.ACTION_DATA_AVAILABLE)){//收到手環返回的消息，表示手環已做好匹配準備。剩下的事由用戶進行操作。
				
				byte[] order = (intent.getByteArrayExtra(BluetoothLeService.EXTRA_ASCII));
				StringBuffer buffer = new StringBuffer();
				for ( int  l=0; l < order.length; l++){
					buffer.append("  "+Integer.toHexString(order[l]&0xFF));
				}
				DebugTool.show(buffer.toString());
				
				
//				if ( ReceiveDataOrder.isRealTimeOrder(order)){
//					;//如果是實時數據，在不保存的情況下刷新界面。需要注意的是用戶輸入的運動log會影響到顯示，所以實時數據在刷新時需要處理
//					
//					RealTimeDataProcesser.getInstance().processForOrder(order);
//					if (MyApplication.getRegisterable() != null ){
//						MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.ACTIVITY_REFRESH, null, PropertyRegisterable.REFRESH_MAINLIST);
//					}
//				}
//				else if ( ReceiveDataOrder.isBattaryOrder(order) ){
//					DebugTool.show("battary number:" + order[1]);
//					MySavedState.LocalSaveInfo.setBattaryInfo(order[1]);
//					if (MyApplication.getRegisterable() != null ){
//						MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_BATTARY_CALLBACK, 0, order[1]);
//					}
//				}
//				else
				{//如果不是實時數據，則交給隊列(或者說是隊首的order)來決定到底下一步該如何處理。

					mTimeOutController.endWork();//end必须放在process之前，因为process已经发送了下一条指令
					mRequestQueue.process(order);
					
					//mOrderListener.processForOrder(order);

				}				
			}
			else if (action.equals(BluetoothLeService.ACTION_GATT_DISCONNECTED)){
				
				if (mRequestQueue != null ){
					mRequestQueue.clearQueue();
				}
				if (MyApplication.getRegisterable() != null ){
					MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_CONNECT_STATE, null, PropertyRegisterable.BLE_DISCONNECTED);
				}
				
				//mBluetoothLeService.close();
			}
		}
	}
	
	//--------------------------------OrderListener-------------------------------
	//把order的處理轉移到另一個類，優化結構代碼
	//SenderOrderListener mOrderListener =  new SenderOrderListener();
	//----------------------------------------------------------------------------
	
	
	//----------------------------------------------------------------------------
	public int count = 0;
	private boolean mIsTryConnecting = false;
	public boolean isTryConnecting(){
		return mIsTryConnecting;
	}
	//----------------------------------------------------------------------------
	
	protected class DeviceScanProcsser implements BLETool.DeviceScan{
		//private Handler mHandler = new Handler();
		@Override
		public void ScanSucess(final String address){
			DebugTool.show("connect success");
			if ( mBluetoothLeService != null ){
				((Activity)mContext).runOnUiThread(new Runnable(){

					@Override
					public void run() {
						mBluetoothLeService.connect(address);
					}
					
				});
			}
			mIsTryConnecting = false;
			
		}
		@Override
		public void ScanFail(){
			mRequestQueue.clearQueue();
			DebugTool.show("connect fail");
			
			mIsTryConnecting = false;
			
			if (MyApplication.getRegisterable() != null ){
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_SYNC_OVER, null, null);
			}
		}
				
	}
	
	/**
	 * 服務重建完畢後的連接，一起其他相關處理動作
	 * @author bxc2010011
	 *
	 */
	protected class CustomConnection extends BLEServiceConnection {
		
		@Override
		public void setBLEService(BluetoothLeService ble_service) {
			mBluetoothLeService = ble_service;
			//新的要求是主界面最好能一進入就可以掃描手環。
			if ( mIsNeedScanWhenCreate )
				startScanDevice();
			else if ( BLETool.getInstance().isBluetoothEnabled() ){
				//當主界面藍牙處於連接狀態才去連接。自動同步功能已經有同步處理了，這裡可以不要
				startScanDevice();
			}
				
		}

		@Override
		public void startScanDevice() {	
			if ( mBluetoothLeService.getConnectionState() != BluetoothLeService.STATE_DISCONNECTED )
				return;
//			if ( !isMatchKeyWindow() ){
//				mRequestQueue.addRequestToQueue(new MatchKeyOrder(), 0);
//				mRequestQueue.addRequestToQueue(new DataInfoOrder());
//			}
			BLETool.getInstance().scanLeDevice(true);
			BLETool.getInstance().setListener(mScanedProcesser);
			
			if (MyApplication.getRegisterable() != null ){
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_CONNECTION, null, PropertyRegisterable.BLE_CONNECTION_START_SYNC);
			}
			mIsTryConnecting = true;
			
			if (MyApplication.getRegisterable() != null ){
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_CONNECT_STATE, null, PropertyRegisterable.BLE_CONNECTING);
			}
		}
	};
	
	protected TimeOut mTimeOutController = new TimeOut();
	
	protected class TimeOut{
		byte[] tempOrder;
		Timer timer;
		TimeOutTask mTask;
		
		public boolean isWork(){
			return timer != null;
		}
		
		public void close(){
			if ( timer != null ){
				timer.cancel();
			}
			mTask = null;
			
		}
		public void startWork(byte[] order ){
			DebugTool.show("TimeOut start");
			tempOrder = order.clone();
			if ( timer != null ){
				timer.cancel();
				timer = null;
				mTask = null;
			}
			
			timer = new Timer("BLE_TimeOut");
			mTask = new TimeOutTask();
			timer.schedule(mTask, 10000, 10000);
		}
		
		public void endWork(){
			DebugTool.show("TimeOut end");
			if ( timer != null ){
				timer.cancel();
				timer = null;
			}
			
		}
		
		public void resendOrder(){
			DebugTool.show("TimeOut resend order");
			if ( mRightSevice == null ){
				mRightSevice = mBluetoothLeService.getSupportedGattService(BLEConfig.getSBServiceUUID());
				if ( mRightSevice == null )
					return;
			}
			BluetoothGattCharacteristic reveive = mRightSevice.getCharacteristic(BLEConfig.getReceiverUUID());
			mBluetoothLeService.writeDataToCharacteristic(reveive, tempOrder);
		}
		
		private class TimeOutTask extends TimerTask{
			
			private int count = 0;
			
			@Override
			public void run() {
				if ( count == 3 ){
					DebugTool.show("resend fail, exit");
					if ( MyApplication.getRegisterable() != null )
						MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE, null, null);
					mTimeOutController.close();
				}
				resendOrder();
				count++;
			}
		}
	}




	/**
	 * 在数据同步过程中，需要中断并向网络发送同步请求，并根据结果来进行下一步动作
	 * 所以需要注册回调到网络调用
	 */
	protected class BaseSportSyncListener implements BaseSportInfoSync.ConnectedReturn {

		@Override
		public void processForReturn(boolean isSuccessful) {
			byte[] order = new byte[2];
//			order[0] = DataReceiveOrder.WEB_SYNC_FLAG;
//			order[1] = (byte)(isSuccessful ? DataReceiveOrder.WEB_SYNC_SUCCESS: DataReceiveOrder.WEB_SYNC_FAIL);
			mRequestQueue.process(order);
		}

	}
}
