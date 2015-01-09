package com.china.acetech.ToolPackage.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.MySavedState;
import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.SportInfoGreenDaoRepository;
import com.china.acetech.ToolPackage.java.CalendarToolForSync;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * 運動目標的數據同步暫定為只刷新數據庫，不刷新界面，也就是說，如果其他設備進行修改以後，另外的設備第一次進入時
 * 顯示的結果會有問題。
 * @author bxc2010011
 *
 */
public class SportInfoSync extends AbsInfoSync {

	
	private static final int HOUR_STATISTICS_RECORD = 11;
	private static final int DAY_STATISTICS_RECORD = 12;
	private static final int MONTH_STATISTICS_RECORD = 13;
	
	private static final String method_hourly = "DownloadMultiHourStatisticsTable";
	private static final String method_daily = "DownloadMultiDayStatisticsTable";
	private static final String method_monthly = "DownloadMultiMonthStatisticsTable";
	
	public static String getMethodName(int staLevel){
		if ( staLevel == HOUR_STATISTICS_RECORD )
			return method_hourly;
		else if ( staLevel == DAY_STATISTICS_RECORD )
			return method_daily;
		else if ( staLevel == MONTH_STATISTICS_RECORD )
			return method_monthly;
		else
			return null;
	}
	
	
	@Override
	protected void connectedForChild(int threadID, Object object) {
		
		if ( object instanceof String ){
			return;
		}
		
		int timeStyle = 0;
		switch(mThreadMap.get(threadID)){
		case HOUR_STATISTICS_RECORD:
			timeStyle = SportInfo_AP.HOUR_STATUSTIC;
			break;
		case DAY_STATISTICS_RECORD:
			timeStyle = SportInfo_AP.DAY_STATUSTIC;
			break;
		case MONTH_STATISTICS_RECORD:
			timeStyle = SportInfo_AP.MONTH_STATUSTIC;
			break;
		default:
			break;
		}
		
		if ( DataTranslate.isElementEmpty(DataTranslate.SportInfo.DownLoad_Data_Name, (Element)object)){
			return ;
		}
		
		List<SportInfo_AP> entityList = new ArrayList<SportInfo_AP>();
		DataTranslate.SportInfo.NodeSet2DBList(entityList, timeStyle, (Node)object);
		//RepositoryManager.getInstance().getSportInfo().addAll(entityList);

		if ( mThreadMap.get(threadID) == DAY_STATISTICS_RECORD )
			RefreshShowingInMainThread();
		syncFlag = false;
	}



	@Override
	public void syncDataInfo(){
		if ( mThreadMap.size() != 0 ) return;
		if ( syncFlag )	return;
		 
		//服務器端存儲的是以秒為單位的時間。
		//分析判斷那些數據需要下載，然後依次發送給服務器。
		Calendar lastDate = CalendarToolForSync.getZeroOfToday();
		Calendar startDate = (Calendar) lastDate.clone();
		startDate.add(Calendar.DATE, -15);
		lastDate.add(Calendar.DATE, 1);
		lastDate.add(Calendar.HOUR, -1);
		int ID; 
		ID = mConnected.getSportData(MySavedState.UserLoginInfo.getLoginName(),
				DataTranslate.C2STime(startDate.getTimeInMillis()), 
				DataTranslate.C2STime(lastDate.getTimeInMillis()), 
				HOUR_STATISTICS_RECORD);
		mThreadMap.append(ID, HOUR_STATISTICS_RECORD);
		
		ID = mConnected.getSportData(MySavedState.UserLoginInfo.getLoginName(), 
				DataTranslate.C2STime(startDate.getTimeInMillis()), 
				DataTranslate.C2STime(lastDate.getTimeInMillis()), 
				DAY_STATISTICS_RECORD);
		mThreadMap.append(ID, DAY_STATISTICS_RECORD);
		
		ID = mConnected.getSportData(MySavedState.UserLoginInfo.getLoginName(), 
				DataTranslate.C2STime(startDate.getTimeInMillis()), 
				DataTranslate.C2STime(lastDate.getTimeInMillis()), 
				MONTH_STATISTICS_RECORD);
		mThreadMap.append(ID, MONTH_STATISTICS_RECORD);
		
		syncFlag = true;
	}
	
//	public void syncDataInfo(Calendar date){
//		if ( syncFlag )	return;
//
//		long Second = date.getTimeInMillis()/1000;
//		int ID = mConnected.getSportData(MySavedState.UserLoginInfo.getLoginName(), Second);
//		mThreadMap.append(ID, GET_SYNC_FLAG);
//		mDateMap.append(ID, Second);
//		syncFlag = true;
//	}
	
	@Override
	protected void refreshShowing(){
		if ( MyApplication.getRegisterable() != null )
			MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.ACTIVITY_REFRESH,
				null, PropertyRegisterable.REFRESH_STATISTIC);
	}

	@Override
	protected void downLoadData(Object object){
		SportInfo_AP entity = new SportInfo_AP();
		DataTranslate.SportInfo.Node2DB(entity, (Node)object);
			
		RefreshShowingInMainThread();
		syncFlag = false;
	}
	
	//updateData可以不用做了，因為不可能會有上傳基礎運動數據的情況。
	@Override
	protected void updateData(Object object){
		Date spDate = new Date( DataTranslate.SportInfo.getDateOfSportData((Node)object) );
		//SportInfo_AP entity = RepositoryManager.getInstance().getSportInfo().getDailySportInfo(spDate);
		SportInfo_AP entity = new SportInfoGreenDaoRepository().getDailySportInfo(spDate);
		mConnected.modifySportData(entity);
		//mConnected.modifySportDatas(MySavedState.UserLoginInfo.getLoginName(), entityList);
		syncFlag = false;
	}

	@Override
	protected int getSyncType(Object object) {
		boolean test = true;
		if ( test )
			return EQUAL;
		if ( object instanceof String ){
			return EQUAL;
		}
		
		if ( DataTranslate.isElementEmpty(DataTranslate.SportInfo.DATA_NAME, (Element)object)){
			return UPDATE;
		}
		Date spDate = new Date( DataTranslate.SportInfo.getDateOfSportData((Node)object) );
		SportInfo_AP entity = new SportInfoGreenDaoRepository().getDailySportInfo(spDate);
		
		Date apSync = entity.getLastsynctime();
		Date webSync = DataTranslate.getLastSyncTime((Node)object);
		
		if ( apSync.equals(webSync) )
			return EQUAL;
		else if ( apSync.before(webSync))
			return DOWNLOAD;
		else
			return UPDATE;
	}
}
