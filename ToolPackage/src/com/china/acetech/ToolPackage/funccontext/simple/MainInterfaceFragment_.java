package com.china.acetech.ToolPackage.funccontext.simple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.ble.services.BLEServiceManagerWorker;
import com.china.acetech.ToolPackage.ble.services.BluetoothLeService;
import com.china.acetech.ToolPackage.debug.DebugTool;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;


/**
 * 主界面的Fragment實例類
 * 行为层，创建、销毁以及生命周期过程的代码放在这里。
 * @author bxc2010011
 *
 */
public class MainInterfaceFragment_ extends MainInterfaceFragment{

private View mMainInterface;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainInterface = super.onCreateView(inflater, container, savedInstanceState);
	    if (mMainInterface == null)
	    	mMainInterface = inflater.inflate(R.layout.f_main_info_showing, container, false);
	    else{
	    	((ViewGroup)mMainInterface.getParent()).removeAllViewsInLayout();
	    }
	    return mMainInterface;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
		mBLEManager.init();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
		case MainActivity.RESULT_FIRST_USER:
//			getActivity().setResult(MainActivity.RESULT_FIRST_USER);
//			getActivity().finish();
			break;
		case MainActivity.REPLACED:
			initWeidgetValue();//重新刷新一次主界面的個人信息部分
			if ( MyApplication.getRegisterable() != null ){
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.ACTIVITY_REFRESH, null, PropertyRegisterable.REFRESH_MAINLIST);
			}
			break;
		case MainActivity.DONOTHING:
			break;
		default:
			break;
		}
	}
	
	@Override
	protected View getRootView() {
		return mMainInterface;
	}
	
	@Override
	public void onDestroy() {
		DebugTool.show("Destroy");
		super.onDestroy();
		mMainInterface = null;
		((MainActivity)getActivity()).getRegisterable().removeListener(PropertyRegisterable.ACTIVITY_REFRESH, mListener);
		
		((MainActivity)getActivity()).getRegisterable().removeListener(PropertyRegisterable.BLE_CONNECTION,mListener);
		((MainActivity)getActivity()).getRegisterable().removeListener(PropertyRegisterable.BLE_SYNC_OVER,mListener);
		((MainActivity)getActivity()).getRegisterable().removeListener(PropertyRegisterable.BLE_CONNECT_STATE,mListener);
		mBLEManager.onDestroy();
		MyApplication.setBLEManagerWorker(null);
		
//		if ( SBApplication.getRegisterable() != null ){
//			SBApplication.getRegisterable().removeListener(PropertyRegisterable.BLE_FACTORY_INFO_REFRESH, mListener);
//			deviceHolder.unRegisterProperty(SBApplication.getRegisterable(), listener);
//		}
	}

	@Override
	public void onPause() {
		DebugTool.show("pause");
		super.onPause();
		//mBLEManager.onPause();
	}

	@Override
	public void onStart() {
		DebugTool.show("start");
		super.onStart();
	}

	@Override
	public void onResume() {
		DebugTool.show("Resume");
		super.onResume();
		//if ( SBApplication.getBLEManagerWorker() != null )
		//	SBApplication.getBLEManagerWorker().connectBLEDevice();
		//mBLEManager.onResume();
	}
	
	@Override
	public boolean onBackPressed() {
		return false;
	}
	
	public void init(){
		initWeidgetLink();
		initWeidgetValue();
		
		
		((MainActivity)getActivity()).getRegisterable().addListener(PropertyRegisterable.ACTIVITY_REFRESH, mListener);
		
		((MainActivity)getActivity()).getRegisterable().addListener(PropertyRegisterable.BLE_CONNECTION,mListener);
		((MainActivity)getActivity()).getRegisterable().addListener(PropertyRegisterable.BLE_SYNC_OVER,mListener);
		((MainActivity)getActivity()).getRegisterable().addListener(PropertyRegisterable.BLE_CONNECT_STATE,mListener);
		mBLEManager = new BLEServiceManagerWorker(getActivity(), false);
		
		ImageView bt = (ImageView)findViewById(R.id.bt_share_in_deviceset);
//		bt.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ShareToOthers.showShare(getActivity(), false, null, false);
//			}
//		});
		
		ImageView setting = (ImageView)findViewById(R.id.bt_setting_in_deviceset);
//		setting.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), SettingActivity.class);
//				getActivity().startActivityForResult(intent, 0);
//
//			}
//		});
		
//		mDeviceState.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(final View v) {
//				ActionCallback callback = new ActionCallback() {
//
//	    			@Override
//	    			public Context getContext() {
//	    				return v.getContext();
//	    			}
//
//	    			@Override
//	    			public void clear() {
//	    			}
//
//	    			@Override
//	    			public void ActionAfterEnableBluetooth() {
//	    				if ( SBApplication.getBLEManagerWorker() == null ){
//	    					return;
//	    				}
//
//	    				if ( SBApplication.getBLEManagerWorker().isConnected() ){
//	    					;
//	    				}
//	    				else{
//	    					SBApplication.getBLEManagerWorker().connectBLEDevice();
//	    				}
//	    			}
//	    		};
//
//	            EnableBluetooth.enableBluetooth(callback);
//			}
//		});
		MyApplication.setBLEManagerWorker(mBLEManager);
	}
	

	private void initWeidgetLink(){
		mPager = (ViewPager)findViewById(R.id.main_info_list_pager);
		
		mHeadport = (ImageView)findViewById(R.id.person_info_headport);
		mName = (TextView)findViewById(R.id.person_info_name);
		mBirthday = (TextView)findViewById(R.id.person_info_birthday);
		mHeight = (TextView)findViewById(R.id.person_info_height);
		mWeight = (TextView)findViewById(R.id.person_info_weight);
		
		mDate = (TextView)findViewById(R.id.sport_info_date);
		
		mDeviceState = (ImageView)findViewById(R.id.device_connect_state);
		
//		deviceHolder = new ViewHolderSet.DeviceInfoHolder() ;
//		LinearLayout layout = (LinearLayout)findViewById(R.id.device_info_setting);
//		deviceHolder.initHolder(layout);
//		layout.setOnClickListener(deviceHolder.listener);
	}
	private void initWeidgetValue(){
		
//		mHeadport.setImageResource(MainInterfaceSetting.getInstance().getHeadPortImageId());
//		mName.setText(MainInterfaceSetting.getInstance().getUserName());
//		mBirthday.setText(MainInterfaceSetting.getInstance().getRegisterDate());
//		mBirthday.setTextColor(Color.GRAY);
//		mHeight.setText(MainInterfaceSetting.getInstance().getUserHeight());
//		mWeight.setText(MainInterfaceSetting.getInstance().getUserWeight());
		
//		deviceHolder.refreshDeviceInfo();
//		if ( SBApplication.getRegisterable() != null ){
//			SBApplication.getRegisterable().addListener(PropertyRegisterable.BLE_FACTORY_INFO_REFRESH, mListener);
//			deviceHolder.registerProperty(SBApplication.getRegisterable(), listener);
//		}
		
	}
	
	PropertyChangeListener mListener = new PropertyChangeListener(){

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if ( event.getPropertyName().equals(PropertyRegisterable.ACTIVITY_REFRESH )){

				Object o = event.getNewValue();
				if( ((String)o).equals(PropertyRegisterable.REFRESH_USERINFO) ){
					initWeidgetValue();
				}
				
				if ( ((String)o).equals(PropertyRegisterable.REFRESH_MAINLIST) ){
					if ( mPager != null && mPager.getAdapter() != null )
						mPager.getAdapter().notifyDataSetChanged();
				}
				
				if ( ((String)o).equals(PropertyRegisterable.REFRESH_SLEEPSTATISTIC) ){
					if ( mPager != null && mPager.getAdapter() != null )
						mPager.getAdapter().notifyDataSetChanged();
				}
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_CONNECTION )){
				final String value = (String)event.getNewValue();
				if ( getActivity() != null )
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							View connecting = mMainInterface.findViewById(R.id.device_connect_connecting);
							connecting.setVisibility(View.INVISIBLE);
							mDeviceState.setVisibility(View.VISIBLE);
							
							if ( value.equals(PropertyRegisterable.BLE_CONNECTION_START_SYNC )){
								mDeviceState.setVisibility(View.INVISIBLE);
								connecting.setVisibility(View.VISIBLE);
							}
							else{
								if ( MyApplication.getBLEManagerWorker() == null ){
									return;
								}
								
								if ( MyApplication.getBLEManagerWorker().isConnected() ){
									mDeviceState.setImageResource(R.drawable.connect);
								}
								else{
									mDeviceState.setImageResource(R.drawable.disconnect);
								}
							}
						}
					});
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_FACTORY_INFO_REFRESH)){
				if ( getActivity() != null )
					getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
//						String title = MySavedState.LocalSaveInfo.getFactoryInfo();
//						getActivity().getActionBar().setTitle(title);
					}
				});
			}
			
			if ( event.getPropertyName().equals(PropertyRegisterable.BLE_CONNECT_STATE) || event.getPropertyName().equals(PropertyRegisterable.BLE_SYNC_OVER)){
				if ( getActivity() != null )
					getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if ( MyApplication.getBLEManagerWorker() == null ){
							mDeviceState.setImageResource(R.drawable.disconnect);
							return;
						}
						
						if ( MyApplication.getBLEManagerWorker().isConnected() ){
							mDeviceState.setImageResource(R.drawable.connect);
							View connecting = mMainInterface.findViewById(R.id.device_connect_connecting);
							connecting.setVisibility(View.INVISIBLE);
							mDeviceState.setVisibility(View.VISIBLE);
						}
						else if ( MyApplication.getBLEManagerWorker().isTryConnecting() || MyApplication.getBLEManagerWorker().getConnectedState() == BluetoothLeService.STATE_CONNECTING ){
							//deviceState.setText(R.string.setting_device_connecting);
						}
						else{
							mDeviceState.setImageResource(R.drawable.disconnect);
							View connecting = mMainInterface.findViewById(R.id.device_connect_connecting);
							connecting.setVisibility(View.INVISIBLE);
							mDeviceState.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		}
		
	};
	
	public static MainInterfaceFragmentBuilder getMainInterfaceBuilder() {
		return new MainInterfaceFragmentBuilder();
	}
	
	public static class MainInterfaceFragmentBuilder
	  {
	    private Bundle bundle = new Bundle();
	    
	    private MainInterfaceFragmentBuilder(){
	    	;
	    }

	    public MainInterfaceFragment create()
	    {
	    	MainInterfaceFragment fragment = new MainInterfaceFragment_();
	      fragment.setArguments(bundle);
	      return fragment;
	    }

	    public MainInterfaceFragmentBuilder setIconId(int iconId)
	    {
	      bundle.putInt("icon", iconId);
	      return this;
	    }

	  }

}
