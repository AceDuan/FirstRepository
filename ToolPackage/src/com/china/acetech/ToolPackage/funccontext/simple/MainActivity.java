package com.china.acetech.ToolPackage.funccontext.simple;

import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.data.CustomDatabaseBuilder;
import com.china.acetech.ToolPackage.funccontext.AbsManuControlActivity;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;

/**
 * 主界面的外層Activity實例 (加入fragment的Activity的演示)
 * @author bxc2010011
 *
 */
public class MainActivity extends AbsManuControlActivity {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	//private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private static final String fragTag = MainActivity.class.getName() + ".fragment";
	private static final String viewIdTag = MainActivity.class.getName() + ".rootViewId";
	
	private int rootViewId;
	private Fragment mFragment;

	
	//resultCode 和requestCode後續需要全部整理一下，一個是要開始requestCode的使用，另一個resultCode沒有統一標準。有可能會出問題
	public static final int REPLACED = 0x1001;
	public static final int DONOTHING = 0x1002;
	
	public static final int RESULT_MATCHKEY_SUCCESS = 0x2001; 
	public static int getUUID() {
		return Math.abs(UUID.randomUUID().hashCode());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch ( item.getItemId()){
//		case android.R.id.home:
//			this.onBackPressed();
//			//this.finish();
//			break;
//		case R.id.action_log_out:
//			MySavedState.UserLoginInfo.setLoginState(false);
//			Intent intent = new Intent(this, LuncherActivity.class);
//			this.startActivity(intent);
//			this.finish();
//			break;
		case R.id.action_showing_all:
			;//MainInterfaceSetting.getInstance().showingAll();
		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int id = 0;
		
		//addShortcut();
		
		//从LuncherActivity移动此处理到这里，因为登录界面晚于Luncher且早于Main，并且有清除数据库的动作
		//最好的办法是只建立一次local db 但是对数据库操作修改过多，逻辑影响过大。所以采用此办法。
		CustomDatabaseBuilder.BuildDBIfNeed();//如果需要的話，建立食物數據信息數據庫
		
		if ( savedInstanceState != null ){
			id = savedInstanceState.getInt(viewIdTag, 0);
			rootViewId = id;
		}	
		FrameLayout layout = new FrameLayout(this);
		//layout.setBackgroundResource((R.drawable.background));
		if (rootViewId == 0)
			rootViewId = getUUID();
		layout.setId(rootViewId);
		setContentView(layout);
		
		mFragment = MainInterfaceFragment_.getMainInterfaceBuilder().create();
		getFragmentManager().beginTransaction().add(rootViewId, mFragment, fragTag).show(mFragment).commit();
		
		
		//setContentView(R.layout.f_main_info_showing);
		
		mRegister = new PropertyRegisterable();
		MyApplication.setRegisterable(mRegister);
		
		//每次進入的時候將電池信息進行重置
//		MySavedState.LocalSaveInfo.setBattaryInfo(-1);
//		getActionBar().setTitle(MySavedState.LocalSaveInfo.getFactoryInfo());
	}

	/**
	 * Backward-compatible version of {@link android.app.ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}
	
	PropertyRegisterable mRegister;
	public PropertyRegisterable getRegisterable(){
		return mRegister;
	}

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_showing_all);
//		long showList = MySavedState.LocalSaveInfo.getShowingList();
//		if ( showList < MySavedState.LocalSaveInfo.hiding_string_value ){//有隱藏數字少一位，必然比這個小
//			item.setVisible(true);
//		}
//		else{
//			item.setVisible(false);
//		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mRegister.removeAllListener();
		MyApplication.setRegisterable(null);
		clearSyncObject();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		mRegister.firePropertyChange(PropertyRegisterable.ACTIVITY_ACTIVE, 
				PropertyRegisterable.ACTIVE, 
				PropertyRegisterable.PAUSE);
	}

	private void clearSyncObject(){
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		mRegister.firePropertyChange(PropertyRegisterable.ACTIVITY_ACTIVE, 
				PropertyRegisterable.PAUSE, 
				PropertyRegisterable.ACTIVE);
		
		//InfoSyncManager.syncBaseInfo();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
//		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
//			getActionBar().setSelectedNavigationItem(
//					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
//		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
//		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
//				.getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFragment.onActivityResult(requestCode, resultCode, data);
	}



}
