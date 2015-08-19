package com.china.acetech.ToolPackage.web;

import java.io.File;
import java.util.concurrent.Future;

import android.os.Environment;


public abstract class DownloadResourceSync extends AbsInfoSync{

	
	public static final String DIRECTORY_MOV = Environment.getExternalStorageDirectory().getPath() + "/Besta/HealthCloud/res1/";
	public static final String DIRECTORY_PIC = Environment.getExternalStorageDirectory().getPath() + "/Besta/HealthCloud/res2/";
	public static final String DIRECTORY_BRACELETDATA = Environment.getExternalStorageDirectory().getPath() + "/Besta/HealthCloud/res3/";
	//public static final String BRACELETDATA_NAME = "Caf05.hex";
	
	public static final String DIRECTORY_MALE = "1/";
	public static final String DIRECTORY_FEMALE = "2/";
	
	public static final int VALUE_MOV = 1;
	public static final int VALUE_PIC = 2;
	public static final int VALUE_BRACELETDATA = 3;
	
	//private static final String PATH_MOV = "/mov/480";
	private static final String PATH_MOV = "/mov";
	private static final String PATH_PIC = "/pic";
	
	public static final int VALUE_MALE = 1;
	public static final int VALUE_FEMALE = 2;
	
//	private static final String PATH_MALE = "/1";
//	private static final String PATH_FEMALE = "/2";
	private static final String PATH_MALE = "/Male";
	private static final String PATH_FEMALE = "/Female";
	
	//private static final String baseAddress = "http://10.180.3.132:8080/BestaHealthCloud/services/regedit";
	//private static final String baseAddress = "http://www.5dkg.com/BestaHealthCloud/services/regedit";
	private static final String baseAddress;
	public static final String braceletDataAddress; 
	
	static{
		baseAddress = "http://health.5dkg.com/BestaHealthCloud";

		braceletDataAddress = "http://health.5dkg.com/BestaHealthCloud/bracelet/";
	}
	
	//以下的值其實都是臨時變量，只是因為為了採用syncDataInfo的接口才放在這裡，其實完全可以
	//使用另外一個接口的。這樣這些成員就不必以這樣容易產生誤解的方式放在這裡了

	
	public static int getVideoSexValue(){
		return VALUE_MALE;
	}
	
	public static int getVideoSexValue(String sexChoose){
			return getVideoSexValue();
	}
	
	public static String getResourcePath(String resourceName, int resType, int sexType){
		StringBuilder builder = new StringBuilder();
		if ( resType == VALUE_MOV )
			builder.append(DIRECTORY_MOV);
		else
			builder.append(DIRECTORY_PIC);
		
		if ( sexType == VALUE_FEMALE )
			builder.append(DIRECTORY_FEMALE);
		else
			builder.append(DIRECTORY_MALE);
		
		builder.append(resourceName);
		
		return builder.toString();
	}
	public void syncDataInfo(int flagID, String resourceName, int resType, int sexType){
		if ( resType == VALUE_MOV )
			syncDataOfMov(flagID, resourceName, resType, sexType);
		else
			syncDataOfPic(flagID, resourceName, resType, sexType);
	}
	
	
	private void syncDataOfMov( int flagID, String resourceName, int resType, int sexType ){
		StringBuilder builder = new StringBuilder();
		builder.append(baseAddress);
		
		builder.append(PATH_MOV);
		
		if ( sexType == VALUE_MALE )
			builder.append(PATH_MALE);
		else
			builder.append(PATH_FEMALE);
		
		String folder;
		folder = resourceName;
		
		builder.append("/" + folder);
		builder.append("/" + resourceName);
		
		builder.append("_480.mp4");
		
		String dictory = DIRECTORY_MOV;
		if ( sexType == VALUE_MALE )
			dictory += DIRECTORY_MALE;
		else
			dictory += DIRECTORY_FEMALE;
		
		int ID = mConnected.connect_HttpMulGet("", builder.toString(), resourceName, dictory, resType, downloadController);
		
		mThreadMap.append(ID, flagID);
	}
	
	private void syncDataOfPic( int flagID, String resourceName, int resType, int sexType ){
		StringBuilder builder = new StringBuilder();
		builder.append(baseAddress);
		
		builder.append(PATH_PIC);
		

		if ( resourceName.endsWith(".jpg") ){
			builder.append("/Common/");
			builder.append(resourceName);
		}
		else{
			if ( sexType == VALUE_MALE )
				builder.append(PATH_MALE);
			else
				builder.append(PATH_FEMALE);
			
			String folder;
			if ( resourceName == null || resourceName.length() == 0 ){
				folder = "error";
			}
			else{
				if ( resourceName.contains("_") )
					folder =  resourceName.substring(0, resourceName.lastIndexOf("_"));
				else
					folder = resourceName;
					
			}
				
			builder.append("/" + folder);
			builder.append("/" + resourceName);
			builder.append(".jpg");
		}
		
		
		String dictory = DIRECTORY_PIC;	
		if ( sexType == VALUE_MALE )
			dictory += DIRECTORY_MALE;
		else
			dictory += DIRECTORY_FEMALE;
		
		//增加緩衝機制，如果資源在本地已經存在，則不再從服務器抓取
		//這個方法不是太好，還得走一次線程的流程，後續再想辦法直接讀取圖片set到
		//相應位置，需要考慮一個問題。在沒有進行view的繪製的情況下，如何獲取view的寬高
		File file = new File(dictory + resourceName);
		
		int ID = mConnected.connect_HttpGet("", builder.toString(), resourceName, dictory, resType);
		
		mThreadMap.append(ID, flagID);
	}

	protected int getFlagID(int threadID){
		return mThreadMap.get(threadID);
	}
	
	/**
	 * 當退出視頻播放界面時，用於終止視頻下載線程
	 */
	public void interrupt(){
		if ( downloadController.res != null )
			downloadController.res.cancel(true);
	}
	
	private Package downloadController = new Package();
	public static class Package{
		public Future<?> res;
		
	}
}
