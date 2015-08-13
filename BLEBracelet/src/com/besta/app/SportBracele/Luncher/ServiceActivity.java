package com.besta.app.SportBracele.Luncher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.besta.app.SportBracele.R;
import com.besta.app.SportBracele.BLE.BluetoothLeReceiver;
import com.besta.app.SportBracele.BLE.BluetoothLeService;
import com.besta.app.SportBracele.adapter.ServiceListAdapter;
import com.besta.app.SportBracele.attributes.BluetoothGATTStatus;
import com.besta.app.SportBracele.tool.Debug;
import com.besta.app.SportBracele.tool.UsingWidget;
import com.besta.app.SportBracele.view.LayoutGroupHolder;

public class ServiceActivity extends Activity {

	
	private String mDeviceName;
	private String mDeviceAddress;
	
	
	private TextView stateView;
	private ExpandableListView mExpandableListView;
	private LinearLayout linearlayout_Group;
	private boolean mConnected;
	//private boolean mDiscoverService;
	
	private TextView rssiView;
	private int getRSSIInterval = 10000;
	private Timer getRSSITimer = new Timer(true);
	private int mrssi;
	
	private BluetoothLeService mBluetoothLeService;
	//private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
	
	private BroadcastReceiver mGattUpdateReceiver;
	private boolean isRegisterReceiver;
	
	private ArrayList<HashMap<String, Object>> mserviceList;
	private ArrayList<ArrayList<HashMap<String, Object>>> mchildList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_activity);
		
		Intent intent = getIntent();

		mDeviceName = intent.getStringExtra("DEVICE_NAME");
		mDeviceAddress = intent.getStringExtra("DEVICE_ADDRESS");
		
		if ((mDeviceName != null) && (mDeviceName.length() > 0))
		      getActionBar().setTitle(mDeviceName + " " + mDeviceAddress);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getWidgetFromLayout();
		
		stateView.setText("Connecting...");
		mConnected = false;
		//mDiscoverService = false;
		
		
	    final Intent getServiceIntent = new Intent(this, BluetoothLeService.class);
	    
	    new Handler().postDelayed(new Runnable()
	      {
	        public void run()
	        {
	        	boolean isbind = ServiceActivity.this.bindService(getServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	    	    Debug.show(""+isbind);
	        }
	      }
	      , 2000L);
	    
	    mGattUpdateReceiver = new BluetoothLeReceiver(this);
	    isRegisterReceiver = false;
	    
	    mserviceList = new ArrayList<HashMap<String, Object>>();
	    mchildList = new ArrayList<ArrayList<HashMap<String, Object>>>();
	    
	}
	
	private void getWidgetFromLayout(){
		
		stateView = (TextView)findViewById(R.id.peripheralState);
		rssiView = (TextView)findViewById(R.id.RssiNumber);
		linearlayout_Group = (LinearLayout)findViewById(R.id.linearlayout_Group);
		mExpandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
		
		
		
		
		linearlayout_Group.setVisibility(View.GONE);			
		LayoutGroupHolder layoutHolder = new LayoutGroupHolder(this, linearlayout_Group);
		linearlayout_Group.setTag(layoutHolder);
	}

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Debug.show("Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			Debug.show("start Service connect");
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if ( !isRegisterReceiver ){
			registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			isRegisterReceiver = true;
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if ( isRegisterReceiver ){
			unregisterReceiver(mGattUpdateReceiver);
			isRegisterReceiver = false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getRSSITimer.cancel();
	    getRSSITimer = null;
	    
	    if ( mBluetoothLeService != null ){
		mBluetoothLeService.disconnect();
		mBluetoothLeService.close();
		unbindService(mServiceConnection);
		mBluetoothLeService = null;
	    }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gatt_services, menu);
		if ( mBluetoothLeService == null){
			menu.findItem(R.id.menu_connect).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(false);
			menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
			
		}
		else if ( mBluetoothLeService.getConnectState() == BluetoothLeService.STATE_CONNECTING){
			menu.findItem(R.id.menu_connect).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(true);
			menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
		}
		else if (mBluetoothLeService.getConnectState() == BluetoothLeService.STATE_CONNECTED ){
			menu.findItem(R.id.menu_connect).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(true);
			menu.findItem(R.id.menu_refresh).setActionView(null);
		}
		else{
			menu.findItem(R.id.menu_connect).setVisible(true);
			menu.findItem(R.id.menu_disconnect).setVisible(false);
			menu.findItem(R.id.menu_refresh).setActionView(null);
		}
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		boolean test = true;
		if ( test){
			return true;
		}
		switch ( item.getItemId()){
		case R.id.menu_connect:
			mBluetoothLeService.connect(mDeviceAddress);
			invalidateOptionsMenu();
			break;
		case R.id.menu_disconnect:
			mBluetoothLeService.close();
			break;
		default:
			Debug.show("OptionsItemSelected error!");
			break;
		}
		
		return true;
	}
	
	public void displayService(){
		
		if ( !mBluetoothLeService.getServiceList(mserviceList, mchildList) )
			return;
		
		ExpandableListAdapter adapter = new ServiceListAdapter(getApplicationContext(), mserviceList, mchildList);
		ExpandableListListener listener = new ExpandableListListener();
	    
	    mExpandableListView.setAdapter(adapter);
    	mExpandableListView.setOnChildClickListener(listener);
    	mExpandableListView.setOnGroupExpandListener(listener);
    	mExpandableListView.setOnGroupClickListener(listener);
	    
	    
	    
	}
	
	
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_ON_CHARACTERISTIC_WRITE);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_READ_REMO_RSSI);
		return intentFilter;
	}
	
	public void setInfomation(Intent intent){
		LayoutGroupHolder holder = (LayoutGroupHolder)linearlayout_Group.getTag();
		holder.setInfomation(intent);
	}



	
	
	

	public void handleStateMessage(String paramString) {
		if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(paramString)) {
			System.out.println("Connected");
			this.stateView.setText("Looking for services");
			return;
		}
		else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
				.equals(paramString)){
			System.out.println("Disconnect");
			this.stateView.setText("Disconnect");
			this.linearlayout_Group.setVisibility(View.GONE);
			return;
		}
		else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
				.equals(paramString)){
			this.stateView.setText("Connected");
		}
		else if (BluetoothLeService.ACTION_DATA_AVAILABLE_FAIL
				.equals(paramString)){
			this.mBluetoothLeService.disconnect();
			this.stateView
					.setText("Discover service fail. Please restart bluetooth or restart the phone. Try again");
//			handleAlertMessage(
//					"Warning Message.",
//					"Discover service fail. Please restart bluetooth or restart the phone. Try again");
			this.linearlayout_Group.setVisibility(View.GONE);
			return;
		}
		else if (BluetoothLeService.ACTION_GATT_CONNECT_FAIL
				.equals(paramString)){
			//handleAlertMessage("Warning Message.", "Connect Fail. Please try again");
			this.stateView.setText("Connect Fail. Please try again");
			this.mBluetoothLeService.disconnect();
		}
		
	}
	
	public void handleStateMessage(Intent intent){
		String action = intent.getAction();
		if ( BluetoothLeService.ACTION_GATT_READ_REMO_RSSI.equals(action) ){
			String num = intent.getStringExtra(BluetoothLeService.EXTRA_RSSI);
			mrssi = Integer.parseInt(num);
			rssiView.setText(String.valueOf(mrssi));
		}
		else if ( BluetoothLeService.ACTION_GATT_ON_CHARACTERISTIC_WRITE.equals(action)){
			String status = intent.getStringExtra(BluetoothLeService.EXTRA_RSSI);
			UsingWidget.handleAlertMessage(this, "Warning", BluetoothGATTStatus.lookup(status, null));
		}
	}
	
	private class ExpandableListListener implements ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener{

		@Override
		public void onGroupExpand(final int groupPosition) {

		}

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			linearlayout_Group.setVisibility(View.GONE);
		    return false;
		}

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			linearlayout_Group.setVisibility(View.VISIBLE);
			LayoutGroupHolder groupHolder = (LayoutGroupHolder)linearlayout_Group.getTag();
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic)mchildList.get(groupPosition).get(childPosition).get("DEVICE_CHARACTERISTIC");
			groupHolder.show(mBluetoothLeService, characteristic);
			linearlayout_Group.setTag(groupHolder);
			

		    return false;
		}

	}
	
	public void disconnectedWithDevice(){
		if ( isRegisterReceiver ){
			unregisterReceiver(mGattUpdateReceiver);
			isRegisterReceiver = false;
		}
        if (mConnected)
        {
        	mConnected = false;
        	handleStateMessage(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        	((ServiceListAdapter)mExpandableListView.getAdapter()).cleanObject();
        	getRSSITimer.cancel();
        }
        mBluetoothLeService.close();
        return;
	}

	
	public void HandlerForRssi(String str){
		if ( str.equals(BluetoothLeService.ACTION_GATT_DISCONNECTED) ){
			getRSSITimer.cancel();
			mConnected = false;
		}
		else if ( str.equals(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)){
			getRSSITimer.schedule(raedRemoteRSSITask, 0L, getRSSIInterval);
		}
		else if ( str.equals(BluetoothLeService.ACTION_GATT_CONNECTED )){
			mConnected = true;
		}
	}
	
	TimerTask raedRemoteRSSITask = new TimerTask()
	  {
	    public void run()
	    {
	      if (!mConnected)
	        return;
	      new Thread(new Runnable()
	      {
	        public void run()
	        {
	          ServiceActivity.this.mBluetoothLeService.readRemoteRssi();
	        }
	      }).run();
	    }
	  };
}
