package com.china.acetech.ToolPackage.ble.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.ble.attributes.BluetoothGATTStatus;
import com.china.acetech.ToolPackage.ble.attributes.CharacteristicArrributes;
import com.china.acetech.ToolPackage.ble.attributes.DataFormatTransf;
import com.china.acetech.ToolPackage.ble.attributes.ServiceArrributes;
import com.china.acetech.ToolPackage.debug.DebugTool;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;


/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
public class BluetoothLeService extends Service{

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	private String mBluetoothDeviceAddress;
	
	private int mConnectionState = STATE_DISCONNECTED;

	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;
	
	
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public static final String ACTION_GATT_SERVICES_DISCOVERED_FAIL = "com.billy.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED_FAIL";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_DATA_AVAILABLE_FAIL = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE_FAIL";
	public final static String ACTION_GATT_CONNECT_FAIL = "com.example.bluetooth.le.ACTION_GATT_CONNECT_FAIL";
	public static final String ACTION_GATT_READ_REMO_RSSI = "com.billy.bluetooth.le.ACTION_GATT_READ_REMO_RSSI";
	public static final String ACTION_GATT_ON_CHARACTERISTIC_WRITE = "com.billy.bluetooth.le.ACTION_GATT_ON_CHARACTERISTIC_WRITE";
	
	public static final String ACTION_GATT_NOTIFITION_SETTED = "com.billy.bluetooth.le.ACTION_GATT_NOTIFITION_SETTED";
	public static final String ACTION_GATT_BATTARY_READ = "com.billy.bluetooth.le.ACTION_BATTARY_READ";
	
	public static final String ACTION_GATT_FACTORY_INFO_READ = "com.billy.bluetooth.le.ACTION_GATT_FACTORY_INFO_READ";
	
	public static final String ACTION_GATT_DESCRIPTOR_WRITE = "com.billy.bluetooth.le.ACTION_GATT_DESCRIPTOR_WRITE";
	
	public static final String EXTRA_ASCII = "com.billy.bluetooth.le.EXTRA_ASCII";
	public static final String EXTRA_DATA = "com.billy.bluetooth.le.EXTRA_DATA";
	public static final String EXTRA_DECIMAL = "com.billy.bluetooth.le.EXTRA_DECIMAL";
	public static final String EXTRA_HEX = "com.billy.bluetooth.le.EXTRA_HEX";
	public static final String EXTRA_RSSI = "com.billy.bluetooth.le.EXTRA_RSSI";
	public static final String EXTRA_STATUS = "com.billy.bluetooth.le.EXTRA_STATUS";
	
	
	private static final int DISCOVER_UNUSED = -1;
	private static final int DISCOVER_START = 1;
	private static final int DISCOVER_END = 2;
	private int serviceDiscoverFlag = DISCOVER_UNUSED;
	
	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		DebugTool.show("service is bind");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
		DebugTool.show("service is unbind");
		close();
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();
	
	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		BluetoothManager mBluetoothManager = null;
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				DebugTool.show("Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			DebugTool.show("Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}
	
	public int getConnectionState(){
		return mConnectionState;
	}
	
	public String getDeviceAddress(){
		return mBluetoothDeviceAddress;
	}
	
	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			DebugTool.show("BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect. (珂蟀諉腔扢掘﹝ 郭彸笭陔蟀諉)
		
		//由於連接上一次舊設備會導致消息缺失，所以不再進行复連，以後可以對消息序列進行改進
		//复連時執行mBluetoothGatt.connect()後並未有BLE消息發送，也無法找到需要的Service。
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
//			DebugTool.show("Trying to use an existing mBluetoothGatt for connection.");
//			if (mBluetoothGatt.connect()) {
//				mConnectionState = STATE_CONNECTING;
//				broadcastUpdate(ACTION_GATT_CONNECTED);
//				return true;
//			} else {
//				return false;
//			}
//			
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			DebugTool.show("Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		DebugTool.show("Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		if ( MyApplication.getRegisterable() != null ){
			MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_CONNECT_STATE, null, PropertyRegisterable.BLE_CONNECTING);
		}
		return true;
	}
	
	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect(){
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			DebugTool.show("BluetoothAdapter not initialized");
			return;
		}
		DebugTool.show("BluetoothGatt is disconnected");
		mBluetoothGatt.disconnect();
		if ( getConnectState() == STATE_CONNECTING ){
			broadcastUpdate(ACTION_GATT_DISCONNECTED);
		}
		mConnectionState = STATE_DISCONNECTED;
		
		serviceDiscoverFlag = DISCOVER_UNUSED;
	}
	
	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close(){
		if ( mBluetoothGatt != null)
			mBluetoothGatt.close();
		DebugTool.show("BluetoothGatt is closed");
		mConnectionState = STATE_DISCONNECTED;
	}
	
	
	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic){
		if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null)){
			DebugTool.show("BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}
	
	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enable
	 *            If true, enable notification. False otherwise.
	 */
	
	public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable ){
		return this.mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
	}
	
	//有官方的API可以使用。不用自己寫。不行，原生的有問題，設置不成功
	public boolean setCharacteristicNotificationByDes(BluetoothGattCharacteristic characteristic, boolean enable ){
		
		if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null)){
			DebugTool.show("BluetoothAdapter not initialized");
			return false;
		}
		if ( mBluetoothGatt.setCharacteristicNotification(characteristic, enable) ){
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CharacteristicArrributes.CLIENT_CHARACTERISTIC_CONFIG));
		    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		    DebugTool.show(characteristic.getUuid().toString() + " is notify: " + mBluetoothGatt.writeDescriptor(descriptor));
		    /*
			 * // This is specific to Heart Rate Measurement. if
			 * (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
			 * System
			 * .out.println("characteristic.getUuid() == "+characteristic.getUuid
			 * ()+", "); BluetoothGattDescriptor descriptor =
			 * characteristic.getDescriptor
			 * (UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			 * descriptor
			 * .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			 * mBluetoothGatt.writeDescriptor(descriptor); }
			 */
		}
		return true;
	}
	
	
	public void writeDataToCharacteristic(BluetoothGattCharacteristic characteristic, String hexString){
		writeDataToCharacteristic(characteristic, DataFormatTransf.hexStringToByteArray2(hexString));

	}
	
	public void writeDataToCharacteristic(BluetoothGattCharacteristic characteristic, byte[] order){
		characteristic.setValue(order);
//	    switch (characteristic.getProperties())
//	    {
//	    
//	    case BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE:
//	    	characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
//	    case BluetoothGattCharacteristic.PROPERTY_WRITE:
//	    	characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
//	    case BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE:
//	    	 characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_SIGNED);
//	    default:
//		      characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
//	    }
		characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
		this.mBluetoothGatt.writeCharacteristic(characteristic);

	}
	
	/**
	 * Read the RSSI for a connected remote device.
	 * */
	public Boolean readRemoteRssi()
	  {
	    return Boolean.valueOf(mBluetoothGatt.readRemoteRssi());
	  }
	
	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices()
	{
		if (this.mBluetoothGatt == null)
	    	return null;
	    return this.mBluetoothGatt.getServices();
	}
	
	/**
	 * Retrieve the GATT service by given UUID
	 * @param uuid
	 * @return
	 */
	public BluetoothGattService getSupportedGattService(UUID uuid)
	{
		if (this.mBluetoothGatt == null)
	    	return null;
	    return this.mBluetoothGatt.getService(uuid);
	}
	
	public int getConnectState(){
		return mConnectionState;
		
	}
	
	public boolean getServiceList(ArrayList<HashMap<String, Object>> serviceList,
			ArrayList<ArrayList<HashMap<String, Object>>> childList){
		List<BluetoothGattService> list = getSupportedGattServices();
		if ( list == null )
			return false;
		
    	
    	
	    for ( BluetoothGattService blueService: list){
		    HashMap<String,Object> tem_map = new HashMap<String, Object>();
		    String tem_uuid = blueService.getUuid().toString();
		    tem_map.put("DEVICE_UUID", tem_uuid);
		    tem_map.put("DEVICE_NAME", ServiceArrributes.lookup(tem_uuid, tem_uuid));
		    tem_map.put("DEVICE_SERVICE", blueService);
		    serviceList.add(tem_map);
		    
		    ArrayList<HashMap<String,Object>> child = new ArrayList<HashMap<String,Object>>();
		    
		    List<BluetoothGattCharacteristic> charlist = blueService.getCharacteristics();
		    for ( BluetoothGattCharacteristic blueCharacteristic: charlist){
		    	HashMap<String, Object> childmap = new HashMap<String, Object>();
		    	String child_uuid = blueCharacteristic.getUuid().toString();
		        childmap.put("DEVICE_NAME", CharacteristicArrributes.lookup(child_uuid, child_uuid));
		        childmap.put("DEVICE_UUID", child_uuid);
		        childmap.put("DEVICE_CHARACTERISTIC", blueCharacteristic);
		        childmap.put("DEVICE_PROPERTIES", Integer.valueOf(blueCharacteristic.getProperties()));
		        
		        child.add(childmap);
		    }
		    childList.add(child);
	    }
	    
	    return true;
	}

	
	private void broadcastUpdate(String message){
		final Intent intent = new Intent(message);
		sendBroadcast(intent);
	}
	private void broadcastUpdate(String message, 
			final BluetoothGattCharacteristic characteristic){
		Intent intent = new Intent(message);
	    byte[] arrayOfByte = characteristic.getValue();
	    StringBuilder buffer;
	    int i;
		if ((arrayOfByte != null) && (arrayOfByte.length > 0)) {
			buffer = new StringBuilder(arrayOfByte.length);
			i = arrayOfByte.length;
			for (int j = 0; j < i; j++){
				byte b = arrayOfByte[j];
				Object[] arrayOfObject = new Object[1];
				arrayOfObject[0] = Byte.valueOf(b);
				buffer.append(String.format("%02X ", arrayOfObject));
			}
			intent.putExtra(EXTRA_HEX, buffer.toString());
			//intent.putExtra(EXTRA_ASCII, new String(arrayOfByte));
			//只有電池電量才會走read過程。先強制給這裡增加一個標記來識別命令
			if ( arrayOfByte.length == 1 ){

				byte[] battary = new byte[arrayOfByte.length+1];
				battary[0] = (byte)0x50;
				for ( int l = 0; l < arrayOfByte.length; l++){
					battary[l+1] = arrayOfByte[l];
				}
				arrayOfByte = battary;
			}
			
			intent.putExtra(EXTRA_ASCII, arrayOfByte);
			try {
				int number = Integer.parseInt(buffer.toString().replaceAll("\\s+", ""), 16);
				intent.putExtra(EXTRA_DECIMAL, number);
			}catch (NumberFormatException e){
			}
			sendBroadcast(intent);
	    }	
	}
	private void broadcastUpdate(String message, int rssiOrstatus){
		Intent intent = new Intent(message);
		intent.putExtra(EXTRA_RSSI, String.valueOf(rssiOrstatus));
		sendBroadcast(intent);
	}
	
	private void broadcastUpdate(String message, BluetoothGattDescriptor descriptor, int status){
		Intent intent = new Intent(message);
		intent.putExtra(EXTRA_STATUS, status);
		sendBroadcast(intent);
	}
	
	
	
	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			DebugTool.show("=======status:" + status);
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				DebugTool.show("Connected to GATT server.");
				// Attempts to discover services after successful connection.
				DebugTool.show("Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());
				broadcastUpdate(intentAction);
				
				serviceDiscoverFlag = DISCOVER_START;
				serviceDiscoverSafelyTimeOut();

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				DebugTool.show("Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			} 
			else {
				DebugTool.show("onServicesDiscovered received: " + status);
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED_FAIL);
			}
			
			serviceDiscoverFlag = DISCOVER_END;
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			DebugTool.show("onCharacteristicRead");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}
		
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			DebugTool.show("--------write success----- status:" + status);
			DebugTool.show("CharacteristicWrite " + BluetoothGATTStatus.lookup(new StringBuilder().append(status).toString(), new StringBuilder().append(status).toString()));
		    //broadcastUpdate(ACTION_GATT_ON_CHARACTERISTIC_WRITE, status);
			broadcastUpdate(ACTION_GATT_ON_CHARACTERISTIC_WRITE);
		};
		
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			if (DebugTool.isDebug() && characteristic.getValue() != null) {

				//當前定義中，手環所返回的數據都是以byte數組形式存在，所以在打印信息時也採用byte形式。
				DebugTool.show("String is: " + characteristic.getStringValue(0));
				byte[] array = characteristic.getValue();
				StringBuffer buffer = new StringBuffer();
				for ( int  l=0; l < array.length; l++){
					buffer.append("  "+Integer.toHexString(array[l]&0xFF));
				}
				DebugTool.show("byte array is : " + buffer.toString());
			}
			DebugTool.show("--------onCharacteristicChanged-----");
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {

			DebugTool.show("onDescriptorWriteonDescriptorWrite = " + status
					+ ", descriptor =" + descriptor.getUuid().toString());
			broadcastUpdate(ACTION_GATT_DESCRIPTOR_WRITE, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			DebugTool.show("rssi = " + rssi);
			broadcastUpdate(ACTION_GATT_READ_REMO_RSSI, rssi);
		}

		
	};
	
	//part of discover service timer control
	public void refreshDiscoverFlag(){
		serviceDiscoverFlag = DISCOVER_UNUSED;
	}
	
	private void serviceDiscoverSafelyTimeOut(){
		new Timer().schedule(new TimerTask(){

			@Override
			public void run() {
				if ( serviceDiscoverFlag == DISCOVER_START ){
					broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED_FAIL);
				}
				
				serviceDiscoverFlag = DISCOVER_END;
			}
			
		}, 10*1000);
	}
}
