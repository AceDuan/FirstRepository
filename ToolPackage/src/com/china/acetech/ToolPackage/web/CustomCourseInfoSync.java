package com.china.acetech.ToolPackage.web;

import java.util.ArrayList;
import java.util.List;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.MySavedState;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;
import com.china.acetech.ToolPackage.data.domain.CustomCourseRelative_AP;
import com.china.acetech.ToolPackage.data.domain.CustomCourse_AP;
import com.china.acetech.ToolPackage.data.repo.manager.RepositoryManager;
import org.ksoap2.serialization.SoapObject;





public class CustomCourseInfoSync extends AbsInfoSync{

	public static final int DownloadData = 1;

	public void syncDataInfo(){
		if ( mThreadMap.size() != 0 ) return;
		if ( syncFlag )	return;
		 
		
		//int ID = mConnected.DownloadCustomCourseInfo(RepositoryManager.getInstance().getUserInfo().getEntity().getUserName());
		//int ID = mConnected.DownloadCustomCourseInfo(RepositoryManager.getInstance().getUserInfo().getEntity().getUserName());
		int ID = mConnected.DownloadCustomCourseInfo(MySavedState.UserLoginInfo.getLoginName());
		mThreadMap.append(ID, DownloadData);
		syncFlag = true;
	}
	
	@Override
	protected void refreshShowing(Object object){
		if (MyApplication.getRegisterable() != null ){
			MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.FAVORITE_FRAGMENT,
					null, PropertyRegisterable.FAVORITE_CUSTOMCOURSE);
		}
	}
	
	@Override
	protected void connectedForChild(int threadID, Object object){
		int threadValue = mThreadMap.get(threadID);
		if ( threadValue == DownloadData ){
			SoapObject soap = (SoapObject)object;		
			List<CustomCourse_AP> list = new ArrayList<CustomCourse_AP>();
			DataTranslate.CustomCourse.AllSoap2DB(list, soap);
			RepositoryManager.getInstance().getCustomCourse().clear(true);
			RepositoryManager.getInstance().getCustomCourse().addAll(list);
			
			List<CustomCourseRelative_AP> actionList = new ArrayList<CustomCourseRelative_AP>();
			for ( CustomCourse_AP entity : list){
				actionList.addAll(entity.getActionList());
			}
			RepositoryManager.getInstance().getCustomCourseRelative().clear(true);
			RepositoryManager.getInstance().getCustomCourseRelative().addAll(actionList);
		}
		
	}
}
