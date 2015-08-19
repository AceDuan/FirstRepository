package com.china.acetech.ToolPackage.web;

import com.china.acetech.ToolPackage.MySavedState;
import com.china.acetech.ToolPackage.data.domain.UserImageData_AP;
import com.china.acetech.ToolPackage.data.repo.manager.RepositoryManager;
import org.ksoap2.serialization.SoapObject;




public class UserImageDataSync extends AbsInfoSync{

	public static final int DownloadData = 2;
	
	public UserImageDataSync(){}

	public void downLoadDataInfo(){
		if ( MySavedState.UserLoginInfo.isAlreadyLogin()
				&& MySavedState.UserLoginInfo.isNeedDownloadHeadImage() ){
			int ID = mConnected.DownloadUserImageData(MySavedState.UserLoginInfo.getLoginName());
			mThreadMap.append(ID, DownloadData);
		}
	}
	
	@Override
	protected void refreshShowing(Object object){}
	
	@Override
	protected void connectedForChild(int threadID, Object object){
		if ( mThreadMap.get(threadID) == DownloadData ){
			SoapObject soap = (SoapObject)object;
			UserImageData_AP entity = RepositoryManager.getInstance().getUserImageData().getEntity();
			
			DataTranslate.UserImageData.Soap2DB(entity, soap);
			if ( RepositoryManager.getInstance().getUserImageData().getById(UserImageData_AP.INFO_ID) == null )
				RepositoryManager.getInstance().getUserImageData().add(entity);
			else
				RepositoryManager.getInstance().getUserImageData().save(entity);
			
			MySavedState.UserLoginInfo.setDownloadHeadImageFlag(false);
		}
	}
	
}
