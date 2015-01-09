package com.china.acetech.ToolPackage.ble.web;

import java.util.ArrayList;
import java.util.List;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;
import com.china.acetech.ToolPackage.web.AbsInfoSync;

/**
 * 借用已做好的同步過程，但是只進行上傳基礎數據的操作。
 * @author bxc2010011
 *
 */
public class BaseSportInfoSync extends AbsInfoSync {

	public List<SportInfo_AP> DataSyncList = new ArrayList<SportInfo_AP>();
	public ConnectedReturn mListener;
	
	@Override
	public void syncDataInfo() {
		if ( mThreadMap.size() != 0 ) return;
		if ( syncFlag )	return;
		
		
		int ID = mConnected.uploadBaseSportData(DataSyncList);
		mThreadMap.append(ID, GET_SYNC_FLAG);
		//這裡不再執行抓取同步標記的動作。直接進行數據同步
	}

	public void setConnectReturnListener( ConnectedReturn listener){
		mListener = listener;
	}
	@Override
	protected void refreshShowing() {
		if ( MyApplication.getRegisterable() != null )
			MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.ACTIVITY_REFRESH, null, PropertyRegisterable.REFRESH_MAINLIST);
	}

	@Override
	protected void updateData(Object object) {
		
	}

	@Override
	protected void downLoadData(Object object) {
		
	}

	@Override
	protected int getSyncType(Object object) {
		//這裡不再判斷同步標記，直接對返回值進行分析。
		

		String message = "";
		if ( object instanceof String )
			message = (String)object;
		
		//無論數據是否上傳成功，通知藍牙同步隊列進行下一步處理。那就是說，這裡需要存放藍牙部分的回調。
		//if ( message.equals(ReturnCodeGroup.SUCCESSFUL) ){
		if ( message.equals("1") ){
			mListener.processForReturn(true);
		}
		else{
			mListener.processForReturn(false);
		}
		
		RefreshShowingInMainThread();
		
		return EQUAL;
	}

	public static interface ConnectedReturn{
		public void processForReturn(boolean isSuccessful);
	}
}
