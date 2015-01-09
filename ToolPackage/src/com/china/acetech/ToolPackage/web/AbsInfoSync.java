package com.china.acetech.ToolPackage.web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseIntArray;

import com.china.acetech.ToolPackage.MyApplication;

/**
 * 所有同步數據對象的抽象父類。目的是封裝同步路徑的具體分支操作過程。
 * 根據抓取到的同步標記確定上傳還是下載。所有的具體動作由具體子類負責。
 * 
 * 由於最新做法是：下載所有數據，檢查是否需要本地上傳，若是，則上傳需要的數據。
 * 所以此部分封裝基本無效。後續如果有改進，可以繼續使用。
 * 
 * @author bxc2010011
 *
 */
public abstract class AbsInfoSync {

	protected static final int GET_SYNC_FLAG = 1;
	protected static final int UPDATE_DATA_TO_S = 2;
	protected static final int DOWNLOAD_DATA_FROM_S = 3;
	
	protected AbsInfoSync(){
		mConnected = new ServiceConnectMethod_HttpPost();
		mConnected.setConnectListener(new ConnectedListener());
		syncFlag = false;
		mThreadMap = new SparseIntArray();
	}
	
	protected ServiceConnectMethod_HttpPost mConnected;
	protected boolean syncFlag;
	protected SparseIntArray mThreadMap;
		
	

	/**
	 * 如果是第一次登入AP，由於本地並沒有數據庫，所以需要進行判空，如果為真直接下載數據。
	 */
	protected abstract void syncDataInfo();
	/**
	 * 提供給子Sync接口用於更新數據的操作。</br>注意！為了保證更新動作在主Thread里進行，
	 * 子Sync在刷新View的時候必須要調用{@link AbsInfoSync#RefreshShowingInMainThread}
	 */
	protected abstract void refreshShowing();
	protected abstract void updateData(Object object);
	protected abstract void downLoadData(Object object);
	protected abstract int getSyncType(Object object); 
	
	protected void connectedForChild(int threadID, Object object){
		
	}
	
	/**
	 * 子Sync需要調用此接口來更新數據。以保證其在主線程中
	 */
	protected void RefreshShowingInMainThread(){
		if (MyApplication.getBLEManagerWorker() != null ){
			((Activity)MyApplication.getBLEManagerWorker().getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run(){
					refreshShowing();
				}
			});
		}
	}
		
	protected static final int UPDATE = 1;
	protected static final int DOWNLOAD = 2;
	protected static final int EQUAL = 3;
	
	private void judgeAndSendOrder(Object object){		
		
		int order = getSyncType(object);
		switch ( order ){
		case EQUAL:
			syncFlag = false;
			break;
		case UPDATE:
			updateData(object);
			break;
		case DOWNLOAD:
			downLoadData(object);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 需要根據線程ID來確定當前運行到哪個階段，現在的做法是同步進程只能有一個。
	 */
	class ConnectedListener implements ServiceConnectMethod_HttpPost.ConnectListener{

		@Override
		public void onConnected(int threadID, Object object) {
			
			switch (mThreadMap.get(threadID)){
			case GET_SYNC_FLAG:
				judgeAndSendOrder(object);
				break;
			case UPDATE_DATA_TO_S:
				break;
			case DOWNLOAD_DATA_FROM_S:
				break;
			default:
				connectedForChild(threadID, object);
				break;
			}
			
			mThreadMap.delete(threadID);
		}		
	}
	
	public static final int CREATE = 0; 
	public static final int UN_UPDATE = 1;
	
	/**
	 * Create date list with start date and end date. you can set Flag to this map for your want  
	 * @param sDate  start Date
	 * @param eDate  end Date
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	public Map<Long, Integer> getFlagMap(Calendar sDate, Calendar eDate){
		Calendar temp = (Calendar)sDate.clone();
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		while ( !temp.after(eDate)){
			map.put(temp.getTimeInMillis(), CREATE);
			temp.add(Calendar.DATE, 1);
		}
		return map;
	}
	
}
