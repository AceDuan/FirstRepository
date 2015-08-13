package com.besta.app.SportBracele.BLE;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class SearchDevice {
	
	private Activity mcontext;
	
	public SearchDevice(Activity context){
		mcontext = context;
	}

	public static BluetoothAdapter testDeviceUsable(Activity context){
		

        // ��鵱ǰ�ֻ��Ƿ�֧��ble ����,�����֧���˳�����
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
            context.finish();
            return null;
        }

        // ��ʼ�� Bluetooth adapter, ͨ�������������õ�һ���ο�����������(API����������android4.3�����ϺͰ汾)
		BluetoothAdapter adapter = ((BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE))
										.getAdapter();
		if (adapter == null)
	    {
	      Toast.makeText(context, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
	      context.finish();
	      return null;
	    }
		
		return adapter;
	}
	
	public static final int REQUEST_ENABLE_BT = 1;
	// 10���ֹͣ��������.
	public static final long SCAN_PERIOD = 10000;
	
	public static void enableBluetooth(Activity context){
		BluetoothAdapter adapter = ((BluetoothManager)context.
							getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
		
		if (!adapter.isEnabled()) {
            if (!adapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
	}
	

	
	public void scanLEDevice(boolean isenable){
		BluetoothAdapter adapter = ((BluetoothManager)mcontext.
				getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
		
		if ( isenable ){
//			handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    invalidateOptionsMenu();
//                }
//            }, SCAN_PERIOD);
			
			adapter.startLeScan(mLeScanCallback);
		}
		else{
			adapter.stopLeScan(mLeScanCallback);
		}
	}
	
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        	
//            mcontext.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mLeDeviceListAdapter.addDevice(device);
//                    mLeDeviceListAdapter.notifyDataSetChanged();
//                }
//            });
            
        }
    };
}
