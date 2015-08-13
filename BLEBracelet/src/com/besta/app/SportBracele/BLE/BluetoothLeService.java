package com.besta.app.SportBracele.BLE;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.besta.app.SportBracele.attributes.BluetoothGATTStatus;
import com.besta.app.SportBracele.attributes.CharacteristicArrributes;
import com.besta.app.SportBracele.attributes.DataFormatTransf;
import com.besta.app.SportBracele.attributes.ServiceArrributes;
import com.besta.app.SportBracele.tool.Debug;

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
	public static final String ACTION_GATT_SERVICES_DISCOVERED_FAIL = "com.billy.bluetooth.le.ACTION_DATA_AVAILABLE_FAIL";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_DATA_AVAILABLE_FAIL = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE_FAIL";
	public final static String ACTION_GATT_CONNECT_FAIL = "com.example.bluetooth.le.ACTION_GATT_CONNECT_FAIL";
	public static final String ACTION_GATT_READ_REMO_RSSI = "com.billy.bluetooth.le.ACTION_GATT_READ_REMO_RSSI";
	public static final String ACTION_GATT_ON_CHARACTERISTIC_WRITE = "com.billy.bluetooth.le.ACTION_GATT_ON_CHARACTERISTIC_WRITE";
	
	
	public static final String EXTRA_ASCII = "com.billy.bluetooth.le.EXTRA_ASCII";
	public static final String EXTRA_DATA = "com.billy.bluetooth.le.EXTRA_DATA";
	public static final String EXTRA_DECIMAL = "com.billy.bluetooth.le.EXTRA_DECIMAL";
	public static final String EXTRA_HEX = "com.billy.bluetooth.le.EXTRA_HEX";
	public static final String EXTRA_RSSI = "com.billy.bluetooth.le.EXTRA_RSSI";
	
	
	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
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
				Debug.show("Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Debug.show("Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
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
			Debug.show("BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect. (��ǰ���ӵ��豸�� ������������)
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Debug.show("Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				return false;
			}
			
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Debug.show("Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Debug.show("Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
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
			Debug.show("BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
		if ( getConnectState() == STATE_CONNECTING ){
			broadcastUpdate(ACTION_GATT_DISCONNECTED);
		}
	}
	
	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close(){
		mBluetoothGatt.close();
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
			Debug.show("BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}
	
	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled ){
		
		if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null)){
			Debug.show("BluetoothAdapter not initialized");
			return false;
		}
		if ( mBluetoothGatt.setCharacteristicNotification(characteristic, enabled) ){
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CharacteristicArrributes.CLIENT_CHARACTERISTIC_CONFIG));
		    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		    mBluetoothGatt.writeDescriptor(descriptor);
		    
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
	
	
	public void writeDataToCharacteristic(BluetoothGattCharacteristic characteristic, String paramString){
		characteristic.setValue(DataFormatTransf.hexStringToByteArray2(paramString));
	    switch (characteristic.getProperties())
	    {
	    
	    case BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE:
	    	characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
	    case BluetoothGattCharacteristic.PROPERTY_WRITE:
	    	characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
	    case BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE:
	    	 characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_SIGNED);
	    default:
		      characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
	    }
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
			intent.putExtra(EXTRA_ASCII, new String(arrayOfByte));
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
	
	
	
	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			System.out.println("=======status:" + status);
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Debug.show("Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Debug.show("Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				Debug.show("Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			} 
			else {
				Debug.show("onServicesDiscovered received: " + status);
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED_FAIL);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			System.out.println("onCharacteristicRead");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}
		
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			System.out.println("--------write success----- status:" + status);
			System.out.println("CharacteristicWrite " + BluetoothGATTStatus.lookup(new StringBuilder().append(status).toString(), new StringBuilder().append(status).toString()));
		    broadcastUpdate(ACTION_GATT_ON_CHARACTERISTIC_WRITE, status);
		};
		
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			if (characteristic.getValue() != null) {

				System.out.println(characteristic.getStringValue(0));
			}
			System.out.println("--------onCharacteristicChanged-----");
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {

			System.out.println("onDescriptorWriteonDescriptorWrite = " + status
					+ ", descriptor =" + descriptor.getUuid().toString());
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			System.out.println("rssi = " + rssi);
			broadcastUpdate(ACTION_GATT_READ_REMO_RSSI, rssi);
		}

		
	};
}
