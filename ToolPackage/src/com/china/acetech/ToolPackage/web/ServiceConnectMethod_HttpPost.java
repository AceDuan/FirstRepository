package com.china.acetech.ToolPackage.web;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;


public class ServiceConnectMethod_HttpPost extends ServiceConnectMethod{

    private static final String METHOD_GetNewestVersionOfBracelet = "getNewestVersionOfBracelet";
    private static final String METHOD_DownloadCourseInfo = "DownloadFavoriteSelfTrainingCourse";
    private static final String METHOD_DownloadUserImageData = "DownloadUserImageData";



    public int DownloadCustomCourseInfo(String UserName){
        List<PropertyInfo> info = new ArrayList<PropertyInfo>();
        //PropertyInfo pro = DataTranslate.UserInfo.DB2Soap(entity);
        PropertyInfo username = DataTranslate.makeSoapEntity("UserName", UserName);
        info.add(username);
        return connect_Soap(METHOD_DownloadCourseInfo, info);
    }

    public int DownloadUserImageData(String userName){
        List<PropertyInfo> info = new ArrayList<PropertyInfo>();
        PropertyInfo temp;
        temp = DataTranslate.makeSoapEntity("UserName", userName);
        info.add(temp);
        return connect_Soap(METHOD_DownloadUserImageData, info);
    }

    public int GetNewestVersionOfBracelet(){
        List<PropertyInfo> info = new ArrayList<PropertyInfo>();
        return connect_Soap(METHOD_GetNewestVersionOfBracelet, info);
    }

}
