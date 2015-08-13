package com.besta.app.SportBracele.Luncher;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.besta.app.SportBracele.R;
import com.besta.app.SportBracele.EnableBluetooth.EnableBluetooth;
import com.besta.app.SportBracele.EnableBluetooth.EnableBluetoothHandler.ActionCallback;
import com.besta.app.SportBracele.adapter.LeDeviceListAdapter;
import com.besta.app.SportBracele.tool.Debug;

public class MainActivity extends Activity 
	implements AdapterView.OnItemClickListener{

	
	
	private BluetoothAdapter mBluetoothAdapter;
	
	private Handler mHandler;
	private boolean mScanning;
	private BluetoothAdapter.LeScanCallback mLeScanCallback;
	
	private LeDeviceListAdapter mLeDeviceListAdapter;
	
	
	
	public static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 10000;		// 10√Î∫ÛÕ£÷π≤È’“À—À˜.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setTitle(R.string.title_devices);
        

		testForIssupportBLE();
        
        
        initField();
        
	}
	
	
	private boolean testForIssupportBLE(){
		
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        
        return true;
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
		//BluetoothTool.Enable(this);
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        scanLeDevice(true);
        // Initializes list view adapter.
       
        //setListAdapter(mLeDeviceListAdapter);

		
        
//		Button but = (Button)findViewById(R.id.loginbutton);
//		but.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//				MainActivity.this.startActivity(intent);
//			}
//		});
	}
	
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mLeDeviceListAdapter.clear();
		scanLeDevice(false);
	}




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}




	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BluetoothDevice device = this.mLeDeviceListAdapter.getDevice(position);
	    if (device == null)
	      return;
	    Intent intent = new Intent(this, ServiceActivity.class);
	    System.out.println("device.getName() :" + device.getName());
	    intent.putExtra("DEVICE_NAME", device.getName());
	    intent.putExtra("DEVICE_ADDRESS", device.getAddress());
	    if (this.mScanning)
	      this.mScanning = false;
	    startActivity(intent);
	};



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if ( mScanning ){
			menu.findItem(R.id.menu_scan).setVisible(false);
			menu.findItem(R.id.menu_stop).setVisible(true);
			menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
			
		}
		else{
			menu.findItem(R.id.menu_scan).setVisible(true);
			menu.findItem(R.id.menu_stop).setVisible(false);
			menu.findItem(R.id.menu_refresh).setActionView(null);
		}
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        case R.id.menu_scan:
            mLeDeviceListAdapter.clear();
            
            ActionCallback callback = new ActionCallback() {
    			
    			@Override
    			public Context getContext() {
    				// TODO Auto-generated method stub
    				return MainActivity.this;
    			}
    			
    			@Override
    			public void clear() {
    				// TODO Auto-generated method stub
    			}
    			
    			@Override
    			public void ActionAfterEnableBluetooth() {
    				// TODO Auto-generated method stub
    				scanLeDevice(true);
    			}
    		};
    		
            EnableBluetooth.enableBluetooth(this, callback);
            
            //scanLeDevice(true);
            break;
        case R.id.menu_stop:
            scanLeDevice(false);
            break;
		}
		return super.onOptionsItemSelected(item);
	}




	private void initField(){
		mHandler = new Handler();
		mScanning = false;
		mLeScanCallback = new MyLeScanCallback();
		mLeDeviceListAdapter = new LeDeviceListAdapter(this);
		
		ListView list = (ListView)findViewById(R.id.mlistView);
        list.setAdapter(mLeDeviceListAdapter);
        list.setOnItemClickListener(this);
	}
	
	
	private void scanLeDevice(boolean isenable){
		if ( isenable ){
			
			mHandler.postDelayed(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					invalidateOptionsMenu();
				}
				
			}, SCAN_PERIOD);
			
			mScanning = true;
			new Thread(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mBluetoothAdapter.startLeScan(mLeScanCallback);
				}
				
			}.start();
//			ImageView v = (ImageView)findViewById(R.id.testimage);
//			Animation animation=AnimationUtils.loadAnimation(this, R.anim.testfortrans);
//			v.startAnimation(animation);
//			animation.setAnimationListener(new AnimationListener() {
//				
//				@Override
//				public void onAnimationStart(Animation animation) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationRepeat(Animation animation) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onAnimationEnd(Animation animation) {
//					// TODO Auto-generated method stub
//					
//				}
//			});

		}
		else{
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		Debug.show("ispass here");
		invalidateOptionsMenu();
	}
	
	private class MyLeScanCallback implements BluetoothAdapter.LeScanCallback {
		
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			// TODO Auto-generated method stub
			MainActivity.this.runOnUiThread(new Runnable(){
				public void run(){

					mLeDeviceListAdapter.addDevice(device);
					mLeDeviceListAdapter.notifyDataSetChanged();
					Debug.show("LE scan success");
				}
			});
		}
	}

	
}
