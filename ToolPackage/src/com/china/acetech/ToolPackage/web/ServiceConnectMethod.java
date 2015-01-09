package com.china.acetech.ToolPackage.web;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.china.acetech.ToolPackage.debug.DebugTool;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * AP與服務器連接的所有接口定義。<br/>
 * 所有上傳數據的接口的參數定義，除登錄註冊外均放在{@link DataTranslate}中<br/>
 * 所有的接口的返回值的定義統一放在{ReturnCodeGroup}
 * @author bxc2010011
 *
 */
public class ServiceConnectMethod{

	public static final String NAMESPACE = "http://wristband.besta.com";
	
	private static final String PARA_USERNAME = "UserName";
	private static final String PARA_PASSWORD = "Password";
	
	private static final String PARA_OLD_PASSWORD = "OldPassword";
	private static final String PARA_NEW_PASSWORD = "NewPassword";
	
	private static final String PARA_DATE_OF_INFO = "DateOfSportInfo";


	
	private static final String METHOD_Registration = "AccountRegistration";
	private static final String METHOD_AccountLogin = "LoginSystem";
	private static final String METHOD_ChangePassword = "ChangePassword";
	private static final String METHOD_ModifyPersonalInformation = "UploadAccountInfo";
	private static final String METHOD_GetPersonalInformation = "DownloadAccountInfo";
	
	//暫時還未開放的接口
	private static final String METHOD_ModifySportGoal = "ModifySportGoalInformation";
	private static final String METHOD_GetSportGoalInformation = "GetSportGoalInformation";
	private static final String METHOD_ModifySportData = "ModifySportDataInformation";
	private static final String METHOD_GetSportData = "GetSportDataInformation";
	
	public static final int NEVER_USE_ID_OF_THREAD = 0;
	private static final int START_USE_ID_OF_THREAD = 100;
	private static final int MAX_USE_ID_OF_THREAD = 10000;
	
	public int regeisterAccount(String userName, String password ){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(PARA_USERNAME, userName);
		map.put(PARA_PASSWORD, password);
		
		return connect(METHOD_Registration, map);
	}
	
	public int logInAccount(String userName, String password ){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(PARA_USERNAME, userName);
		map.put(PARA_PASSWORD, password);
		
		return connect(METHOD_AccountLogin, map);
	}
	
	public int ChangePassword(String userName, String oldpassword, String newpassword ){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(PARA_USERNAME, userName);
		map.put(PARA_OLD_PASSWORD, oldpassword);
		map.put(PARA_NEW_PASSWORD, newpassword);
		
		return connect(METHOD_ChangePassword, map);
	}
	
//	public int modifyPersonalInfomation(UserInfo_AP APEntity){
//		return modifyPersonalInfomation( APEntity.getAccount(),
//				APEntity.getUserName(),
//				APEntity.getSex(),
//				APEntity.getBirthday().toString(),
//				APEntity.getPersionHeight().toString(),
//				APEntity.getPersionWeight().toString(),
//				APEntity.getLengthUnit(),
//				APEntity.getWeightUnit(),
//				String.valueOf( APEntity.getRegisterDate().getTime() ),
//				String.valueOf( APEntity.getLastSyncTime().getTime() ) );
//	}
//	public int modifyPersonalInfomation(String userName, String Name,
//			String Sex, String Brithday, String Height, String Weight,
//			String Unit_Length, String Unit_Weight, String RegisterDate, String LastSyncTime){
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put(PARA_USERNAME, userName);
//		map.put(INFO_NAME, Name);
//		map.put(INFO_SEX, Sex);
//		map.put(INFO_BRITHDAY, Brithday);
//		map.put(INFO_HEIGHT, Height);
//		map.put(INFO_WEIGHT, Weight);
//		
//		return connect(METHOD_ModifyPersonalInformation, map);
//	}


//	public int modifyPersonalInfomation(UserInfo_AP APEntity){
//		List<PropertyInfo> proList = new ArrayList<PropertyInfo>();
//
//		PropertyInfo userName = new PropertyInfo();
//		userName.setName(PARA_USERNAME);
//		userName.setValue(MySavedState.UserLoginInfo.getLoginName());
//		proList.add(userName);
//
//		PropertyInfo structure = DataTranslate.UserInfo.DB2SoapMap(APEntity);
//		proList.add(structure);
//
//		return connect(METHOD_ModifyPersonalInformation, proList);
//
//	}
//
//	public int getPersonalInformation(String userName){
//		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		map.put(PARA_USERNAME, userName);
//
//		return connect(METHOD_GetPersonalInformation, map);
//	}
//
//
//	public int modifySportGoal(SportGoal_AP APEntity){
//		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		DataTranslate.SportGoal.DB2WebMap(map, APEntity);
//
//		return connect(METHOD_ModifySportGoal, map);
//	}
//
//	public int getSportGoal(String userName){
//		LinkedHashMap<String, String>map = new LinkedHashMap<String, String>();
//		map.put(PARA_USERNAME, userName);
//
//		return connect(METHOD_GetSportGoalInformation, map);
//	}
//
//	public int modifySportData(SportInfo_AP APEntity){
//		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		DataTranslate.SportInfo.DB2WebMap(map, APEntity);
//
//		return connect(METHOD_ModifySportData, map);
//	}
//
//	public int uploadBaseSportData(List<SportInfo_AP> list){
//		List<PropertyInfo> proList = new ArrayList<PropertyInfo>();
//
//
//
//		PropertyInfo userName = new PropertyInfo();
//		userName.setName(PARA_USERNAME);
//		userName.setValue(MySavedState.UserLoginInfo.getLoginName());
//
//		PropertyInfo sportData = DataTranslate.SportInfo.DB2SoapMap(list);
//
//		proList.add(userName);
//		proList.add(sportData);
//
//
//		return connect(METHOD_ModifySportData, proList);
//	}
	

	public int getSportData(String userName, long date){
		LinkedHashMap<String, String>map = new LinkedHashMap<String, String>();
		map.put(PARA_USERNAME, userName);
		map.put(PARA_DATE_OF_INFO, String.valueOf(date) );
		
		return connect(METHOD_GetSportData, map);
	}
	/***
	 * connect to web service
	 * 
	 * @param method method name of web service
	 * @param map property list for method
	 */
	private int connect(final String method, final HashMap<String, String> map){
		ConnectThread t = new ConnectThread(){

			@Override
			public void run() {
				//final String SOAP_ACTION = "AtlasAccount/AMD_Keyword_Report";
				//final String METHOD_NAME = "Registration";
				//final String METHOD_NAME = "AccountLogin";
				final String METHOD_NAME = method;
				final String NAMESPACE_used = NAMESPACE;
				//final String URL = "http://10.180.3.248/Besta/services/WristBand?wsdl";
				final String URL = "http://61.185.207.11/Besta/services/WristBand?wsdl";
				
				
				
				SoapObject request = new SoapObject(NAMESPACE_used, METHOD_NAME);
				//SimpleDateFormat sdf=new SimpleDateFormat("MMddyyyy");
				//String date = sdf.format(new Date());
				
				
				//get all parameter for connection
				//HashMap<String, String> map = new HashMap<String, String>();
				
				Iterator<String> keylist =map.keySet().iterator();
				while( keylist.hasNext() ){
					String bundleKey = keylist.next();
					request.addProperty(bundleKey, map.get(bundleKey) ) ;
				}
				

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = false;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				Object test = null;
				int count = 0;
				while ( true ){
					try {
						if ( count > 10 )
							break;
						count++;
						androidHttpTransport.call(NAMESPACE+method, envelope);
						test = envelope.getResponse();
					} catch (IOException e) {
						if ( e instanceof SoapFault )
							Log.i("SoapError", e.toString());
						if ( e instanceof EOFException )
							continue;
						;//connect fail
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						;//report fail
						e.printStackTrace();
					}
					
					break;
				}
				
				
//				if ( test != null )
//					Log.i("response", ""+test.toString());
//				
//				SoapObject soapresult = (SoapObject) envelope.bodyIn;
//				if ( soapresult != null ){
//					String response = soapresult.getProperty(0).toString();
//					String resultstring = response.toString();
//					Log.i("Report Keyword", resultstring);
//				}
				SoapObject resultSoap = new SoapObject();
				if ( test == null ){
					//res = "Connect error! Please try again.";
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, "Connect error! Please try again.");
				}
				if ( test instanceof SoapPrimitive){
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, test.toString());
				}
				else if ( test instanceof SoapObject ){
					resultSoap = (SoapObject)test;
				}
				else{
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, "Connect error! Please try again.");
				}
				
				if ( mHandler != null )
					mHandler.sendMessage(Message.obtain(mHandler, getThreadID(), resultSoap));
				
			}
			
		};
		
		t.start();
		
		return t.getThreadID();
	}
	
	private int connect(final String method, final List<PropertyInfo> list){
		ConnectThread t = new ConnectThread(){

			@Override
			public void run() {
				//final String SOAP_ACTION = "AtlasAccount/AMD_Keyword_Report";
				//final String METHOD_NAME = "Registration";
				//final String METHOD_NAME = "AccountLogin";
				final String METHOD_NAME = method;
				final String NAMESPACE = "http://wristband.besta.com";;
				//final String URL = "http://10.180.3.248/Besta/services/WristBand?wsdl";
				final String URL = "http://61.185.207.11/Besta/services/WristBand?wsdl";
				
				
				
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				//SimpleDateFormat sdf=new SimpleDateFormat("MMddyyyy");
				//String date = sdf.format(new Date());
				

				//get all parameter for connection
				//HashMap<String, String> map = new HashMap<String, String>();

				for ( PropertyInfo info : list){
					request.addProperty(info);
				}
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				//envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				Object test = null;
				int count = 0;
				while ( true ){
					try {
						if ( count > 10 )
							break;
						count++;
						androidHttpTransport.call(null, envelope);
						test = envelope.getResponse();
					} catch (IOException e) {
						if ( e instanceof SoapFault )
							Log.i("SoapError", e.toString());
						if ( e instanceof EOFException )
							continue;
						;//connect fail
						e.printStackTrace();
					} catch (XmlPullParserException e) {
						;//report fail
						e.printStackTrace();
					}
					
					break;
				}
				
				
//				if ( test != null )
//					Log.i("response", ""+test.toString());
//				
//				SoapObject soapresult = (SoapObject) envelope.bodyIn;
//				if ( soapresult != null ){
//					String response = soapresult.getProperty(0).toString();
//					String resultstring = response.toString();
//					Log.i("Report Keyword", resultstring);
//				}
				SoapObject resultSoap = new SoapObject();
				if ( test == null ){
					//res = "Connect error! Please try again.";
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, "Connect error! Please try again.");
				}
				if ( test instanceof SoapPrimitive){
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, test.toString());
				}
				else if ( test instanceof SoapObject ){
					resultSoap = (SoapObject)test;
				}
				else{
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, "Connect error! Please try again.");
				}
				
				if ( mHandler != null )
					mHandler.sendMessage(Message.obtain(mHandler, getThreadID(), resultSoap));
				
			}
			
		};
		
		t.start();
		
		return t.getThreadID();
	}
	
	public void setConnectListener(ConnectListener listener){
		mConnectListener = listener;
	}
	
	private ConnectListener mConnectListener;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if ( mConnectListener != null )
				mConnectListener.onConnected(msg.what, (SoapObject)msg.obj);
			else
				DebugTool.show("Can't handle without listener");
		}
	};
		
	
	private abstract static class ConnectThread extends Thread{
		private static int count = START_USE_ID_OF_THREAD;
		private int threadID;
		ConnectThread(){
			threadID = count;
			
			count++;
			if ( count % MAX_USE_ID_OF_THREAD == 0 )
				count = START_USE_ID_OF_THREAD;
		}
		
		public int getThreadID(){
			return threadID;
		}
	}
	
	static public interface ConnectListener{
		/***
		 * listener of callback for Web connected
		 * 
		 * Must consider of connected after View destroyed
		 *
		 */
		void onConnected(int threadID, SoapObject message);
	}
}
