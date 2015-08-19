package com.china.acetech.ToolPackage.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.MySavedState;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;
import com.china.acetech.ToolPackage.data.domain.BraceletInfo_AP;
import com.china.acetech.ToolPackage.data.repo.manager.RepositoryManager;
import com.china.acetech.ToolPackage.tool.MD5Vertify;
import org.ksoap2.serialization.SoapObject;




public class BraceletInfoSync extends AbsInfoSync{

	public static final int DownloadData = 2;
	public static final int DownloadUpdateData = 3;
	
	public BraceletInfoSync(){}

	public void downBraceletInfo(){
		int ID = mConnected.GetNewestVersionOfBracelet();
		mThreadMap.append(ID, DownloadData);
	}
	
//	/**
//	 * 用於測試的下載更新data接口。直接下載更新包，跳過檢測過程
//	 */
//	public void testDownBraceletData(){
//		MySavedState.LocalSaveFlagInfo.setDataRightFlag(false);
//		int ID = mConnected.connect_HttpGet("", DownloadResourceSync.braceletDataAddress, DownloadResourceSync.BRACELETDATA_NAME, DownloadResourceSync.DIRECTORY_BRACELETDATA, DownloadResourceSync.VALUE_BRACELETDATA);
//		
//		mThreadMap.append(ID, DownloadUpdateData);
//	}
	
	public void downBraceletData(){
		boolean res = RepositoryManager.getInstance().getBracelet().isNeedDownloadBraceletData();
		BraceletInfo_AP entity = RepositoryManager.getInstance().getBracelet().getEntity();
		if ( entity.getFileName().length() == 0 )
			return;
		
		if ( res ){
			if ( !MySavedState.LocalSaveFlagInfo.isDataRight() ){
				MySavedState.LocalSaveFlagInfo.setDataRightFlag(false);
				int ID = mConnected.connect_HttpGet("", DownloadResourceSync.braceletDataAddress+entity.getFileName(), entity.getFileName(), DownloadResourceSync.DIRECTORY_BRACELETDATA, DownloadResourceSync.VALUE_BRACELETDATA);
				
				mThreadMap.append(ID, DownloadUpdateData);
			}
			
		}
	}
	@Override
	protected void refreshShowing(Object object){}
	
	@Override
	protected void connectedForChild(int threadID, Object object){
		if ( mThreadMap.get(threadID) == DownloadData ){
			SoapObject soap = (SoapObject)object;
			BraceletInfo_AP entity = RepositoryManager.getInstance().getBracelet().getEntity();
			BraceletInfo_AP userEntity = RepositoryManager.getInstance().getBracelet().getUserEntity();
			
			List<BraceletInfo_AP> entityList = new ArrayList<BraceletInfo_AP>();
			DataTranslate.BraceletInfo.AllSoap2DB(entityList, soap);
			for ( BraceletInfo_AP temp : entityList ){
				if ( temp.getBracelet().equals(userEntity.getBracelet()) ){
					entity.setBracelet(temp.getBracelet());
					entity.setMD5(temp.getMD5());
					entity.setVersion(temp.getVersion());
					entity.setFileName(temp.getFileName());
				}
			}
			if ( RepositoryManager.getInstance().getBracelet().getById(BraceletInfo_AP.INFO_ID) == null )
				RepositoryManager.getInstance().getBracelet().add(entity);
			else
				RepositoryManager.getInstance().getBracelet().save(entity);
			
			if ( RepositoryManager.getInstance().getBracelet().isNeedDownloadBraceletData() 
					&&  !entity.getMD5().toUpperCase(Locale.US).equals(MD5Vertify.md5sum(DownloadResourceSync.DIRECTORY_BRACELETDATA + entity.getFileName()))){
				MySavedState.LocalSaveFlagInfo.setDataRightFlag(false);
			}
		}
		else if ( mThreadMap.get(threadID) == DownloadUpdateData ){
			BraceletInfo_AP entity = RepositoryManager.getInstance().getBracelet().getEntity();
			if ( entity.getMD5().toUpperCase(Locale.US).equals(MD5Vertify.md5sum(DownloadResourceSync.DIRECTORY_BRACELETDATA+entity.getFileName()))){
				//md5校驗成功，開始下載
				MySavedState.LocalSaveFlagInfo.setDataRightFlag(true);
				if ( MyApplication.getRegisterable() != null ){
					MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.UPDATE_BRACELET_DOWNLOAD_SUCCESS, null, null);
				}
			}
		}
	}
	
}
