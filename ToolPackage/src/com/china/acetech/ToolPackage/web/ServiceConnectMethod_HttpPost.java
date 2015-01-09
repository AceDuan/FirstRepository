package com.china.acetech.ToolPackage.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.debug.DebugTool;
import com.china.acetech.ToolPackage.web.customsoap.MySoapObject;
import com.china.acetech.ToolPackage.web.customsoap.SoapXmlMaker;
import com.china.acetech.ToolPackage.web.customsoap.SoapXmlParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Node;



public class ServiceConnectMethod_HttpPost {

	public static final String URL = "http://www.5dkg.com/Besta/services/WristBand?wsdl";
	//public static final String URL = "http://61.185.207.11/Besta/services/WristBand?wsdl";
	private static final int REQUEST_TIMEOUT = 5 * 1000;//設置請求超時10秒鐘
	private static final int SO_TIMEOUT = 5 * 1000; // 設置等到數據超時時間10秒鐘

	
	
	private static final String PARA_USERNAME = "UserName";
	private static final String PARA_PASSWORD = "Password";
	
	private static final String PARA_OLD_PASSWORD = "OldPassword";
	private static final String PARA_NEW_PASSWORD = "NewPassword";

	private static final String PARA_START_DATE = "sDate";
	private static final String PARA_END_DATE = "eDate";

	
	private static final String METHOD_Registration = "AccountRegistration";
	private static final String METHOD_AccountLogin = "LoginSystem";
	private static final String METHOD_ChangePassword = "ChangePassword";
	private static final String METHOD_ModifyPersonalInformation = "UploadAccountInfo";
	private static final String METHOD_GetPersonalInformation = "DownloadAccountInfo";
	
	private static final String METHOD_ModifySportGoal = "UploadSportsTarget";
	private static final String METHOD_GetSportGoalInformation = "DownloadSportsTarget";
	private static final String METHOD_ModifySportData = "UploadDailySportsSatusRecords";
	
	private static final String METHOD_ModifySleepLog = "UploadDailySleepRecords";
	private static final String METHOD_GetSleepLog = "DownloadDailySleepRecords";
	private static final String METHOD_GetMulSleepLog = "DownloadMultiDaySleepRecords";
	
	private static final String METHOD_ModifyDeviceInfo = "UploadBraceletInfo";
	private static final String METHOD_GetDeviceInfo = "DownloadBraceletInfo";
	
	private static final String METHOD_ModifyWaterLog = "UploadDailyWaterRecords";
	private static final String METHOD_GetMulWaterLog = "DownloadMultiDayWaterRecords";
	
	private static final String METHOD_ModifyWeightLog = "UploadDailyWeightRecords";
	private static final String METHOD_GetMulWeightLog = "DownloadMultiDayWeightRecords";
	
	private static final String METHOD_GetMulSleepStatus = "DownloadMultiDaySleepStatus";
	
	private static final String METHOD_ModifyActiveLog = "UploadDailySportsRecords";
	private static final String METHOD_GetMulActiveLog = "DownloadMultiDaySportsRecords";
	
	private static final String METHOD_ModifyFoodLog = "UploadDailyDietRecords";
	private static final String METHOD_GetMulFoodLog = "DownloadMultiDayDietRecords";
	
	private static final String METHOD_ModifyAlarmLog = "UploadAlarmSettingRecords";
	private static final String METHOD_GetAlarmLog = "DownloadAlarmSettingRecords";
	
	private static final String METHOD_ModifyFitnessPlan = "UploadFitnessPlan";
	private static final String METHOD_GetFitnessPlan = "DownloadFitnessPlan";
	
	private static final String METHOD_ForgotPassword = "FindPassword";
	
	public static final int NEVER_USE_ID_OF_THREAD = 0;
	private static final int START_USE_ID_OF_THREAD = 100;
	private static final int MAX_USE_ID_OF_THREAD = 10000;


	public int uploadBaseSportData(List<SportInfo_AP> sportList){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		//object.setValue(getCurrentUserName());
		list.add(object);

		object = DataTranslate.SportInfo.DB2MySoapObject(sportList);
		list.add(object);

		return connect(METHOD_ModifySportData, list);
	}

	public int modifySportData(SportInfo_AP sportEntity){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		//object.setValue(getCurrentUserName());
		list.add(object);

		object = DataTranslate.SportInfo.DB2OneMySoap(sportEntity);
		list.add(object);

		return connect(METHOD_ModifySportData, list);
	}

	public int getSportData(String userName, String sDate, String eDate, int staLevel){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);

		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);

		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);

		return connect(SportInfoSync.getMethodName(staLevel), list);
	}
/*
	public int regeisterAccount(String userName, String password ){
		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_PASSWORD);
		object.setValue(password);
		list.add(object);
		
		
		return connect(METHOD_Registration, list);
	}

	public int logInAccount(String userName, String password ){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_PASSWORD);
		object.setValue(password);
		list.add(object);
		
		return connect(METHOD_AccountLogin, list);
	}
	
	public int ChangePassword(String userName, String oldpassword, String newpassword ){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_OLD_PASSWORD);
		object.setValue(oldpassword);
		list.add(object);
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_NEW_PASSWORD);
		object.setValue(newpassword);
		list.add(object);
		
		return connect(METHOD_ChangePassword, list);
	}
	
	public int modifyPersonalInfomation(UserInfo_AP APEntity){
		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		
		object = DataTranslate.UserInfo.DB2MySoapObject(APEntity);
		list.add(object);
		
		
		return connect(METHOD_ModifyPersonalInformation, list);
		
	}
	
	public int getPersonalInformation(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(METHOD_GetPersonalInformation, list);
	}
	
	public int modifySportGoal(SportGoal_AP APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.SportGoal.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifySportGoal, list);
	}
	
	public int getSportGoal(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(METHOD_GetSportGoalInformation, list);
	}
	
	public int getSleepLog(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(METHOD_GetSleepLog, list);
	}
	
	public int ForgotPassword(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		
		return connect(METHOD_ForgotPassword, list);
	}
	
	public int getDeviceInfo(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		
		return connect(METHOD_GetDeviceInfo, list);
	}
	
	public int modifyDeviceInfo(DeviceInfo_AP APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.DeviceInfo.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifyDeviceInfo, list);
	}
	
	public int getMulSleepLog(String userName, String sDate, String eDate){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(METHOD_GetMulSleepLog, list);
	}
	
	public int modifySleepLog(List<SleepLog_AP> APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.SleepLog.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifySleepLog, list);
	}
	
	public int getMulWaterLog(String userName, String sDate, String eDate){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(METHOD_GetMulWaterLog, list);
	}
	
	public int modifyWaterLog(List<WaterLog_AP> APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.WaterLog.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifyWaterLog, list);
	}
	
	public int getMulActiveLog(String userName, String sDate, String eDate){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(METHOD_GetMulActiveLog, list);
	}
	
	public int modifyActiveLog(List<ActiveLog_AP> APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.ActiveLog.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifyActiveLog, list);
	}
	
	public int getMulFoodLog(String userName, String sDate, String eDate){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(METHOD_GetMulFoodLog, list);
	}
	
	public int modifyFoodLog(List<FoodLog_AP> APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.FoodLog.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifyFoodLog, list);
	}
	
	public int getMulSleepStatistic(String userName, String sDate, String eDate, int staLevel){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(SleepStatisticInfoSync.getMethodName(staLevel), list);
	}
	
	public int getMulSleepStatus(String userName, String sDate, String eDate){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(METHOD_GetMulSleepStatus, list);
	}

	public int modifyFoodListData(List<FoodListLog_AP> entitylist, int flag){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		
		object = DataTranslate.FoodList.DB2MySoapObject(entitylist);
		list.add(object);
		
		return connect(FoodListLogInfoSync.getUploadMethodName(flag), list);
	}
	
	public int getFoodListData(String userName, int flag){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(FoodListLogInfoSync.getDownloadMethodName(flag), list);
	}
	
	public int modifySportListData(List<SportListLog_AP> entitylist, int flag){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		
		object = DataTranslate.SportList.DB2MySoapObject(entitylist);
		list.add(object);
		
		return connect(SportListLogInfoSync.getUploadMethodName(flag), list);
	}
	
	
	public int getSportListData(String userName, int flag){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(SportListLogInfoSync.getDownloadMethodName(flag), list);
	}
	
	public int modifyAlarmLogData(List<AlarmClockLog_AP> entitylist){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		
		object = DataTranslate.AlarmLog.DB2MySoapObject(entitylist);
		list.add(object);
		
		return connect(METHOD_ModifyAlarmLog, list);
	}
	
	public int getAlarmLogData(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(METHOD_GetAlarmLog, list);
	}
	
	public int modifyFitnessPlan(FitnessPlan_AP APEntity){
		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		
		object = DataTranslate.FitnessPlan.DB2MySoapObject(APEntity);
		list.add(object);
		
		
		return connect(METHOD_ModifyFitnessPlan, list);
		
	}
	
	public int getFitnessPlan(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(METHOD_GetFitnessPlan, list);
	}
	
	public int getMulWeightLog(String userName, String sDate, String eDate){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_START_DATE);
		object.setValue(sDate);
		list.add(object);
		
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_END_DATE);
		object.setValue(eDate);
		list.add(object);
		
		return connect(METHOD_GetMulWeightLog, list);
	}
	
	public int modifyWeightLog(List<WeightLog_AP> APEntity){		
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object;
		object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(getCurrentUserName());
		list.add(object);
		object = DataTranslate.WeightLog.DB2MySoapObject(APEntity);
		list.add(object);
		
		return connect(METHOD_ModifyWeightLog, list);
	}
	
	public int forgotPassword(String userName){
		List<MySoapObject> list = new ArrayList<MySoapObject>();
		MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, PARA_USERNAME);
		object.setValue(userName);
		list.add(object);
		
		return connect(METHOD_ForgotPassword, list);
	}
	*/
	public int connect(final String method, final List<MySoapObject> list) {
		ConnectThread t = new ConnectThread(method) {
			
			@Override
			public void run() {
				
				HttpPost request = new HttpPost(URL);
				request.addHeader("charset", HTTP.UTF_8);
				try {
					String xml = new SoapXmlMaker().createXML(method, list);
					System.out.println(xml);
					StringEntity formEntity;
					formEntity = new StringEntity(xml, "utf-8");
					DebugTool.show(xml);
					request.setEntity(formEntity);
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}

				HttpResponse response = null;
				int timeOutCount = 0;
				while ( true ){
					try {
						timeOutCount++;
						System.out.println(method + " start");
						HttpClient client = getHttpClient();
						client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10*1000);
						response = client.execute(request);
						System.out.println(method + " work over");
						break;
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						break;
					} catch (IOException e) {
						if ( e instanceof org.apache.http.conn.ConnectTimeoutException
								&& timeOutCount != 5){
							System.out.println(method + " retry :" + timeOutCount);
							continue;
						}
						System.out.println("Error method is :" +method);
						e.printStackTrace();
						break;
					}
				}
				Object result = null;
				if (response != null) {
					try {
						String res = getValue(response.getEntity().getContent());
						SoapXmlParser parse = new SoapXmlParser();
						result = parse.Parser(res);
					} catch (IllegalStateException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				
				if ( result == null ){
					//res = "Connect error! Please try again.";
					result = "Connect error! Please try again.";
				}
				else if ( result instanceof Node ){
					
					String message = ((Node)result).getFirstChild().getNodeValue();
					if ( message != null )
						result = message;
				}
				else{
					result = "Connect error! Please try again.";
				}
				
				handleMessage(getThreadID(), result);
				//if ( mHandler != null )
				//	mHandler.sendMessage(Message.obtain(mHandler, getThreadID(), result));
			}
		};
		
		t.start();
		
		return t.getThreadID();
		
	}
	
	//使用StringEntity，需要把完整的請求內容填充到entity中。
	public static HttpClient getHttpClient(){
		BasicHttpParams httpParams;
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
	
	public static String getValue( InputStream stream) throws IOException{
		String sum = "";
		BufferedReader in  = new BufferedReader(new InputStreamReader(stream));
		String str;
		while ( (str = in.readLine()) != null )
			sum += str+"\n";
		
		return sum;
	}
	
	
	public void setConnectListener(ConnectListener listener){
		mConnectListener = listener;
	}
	
	private ConnectListener mConnectListener;
	
	private void handleMessage(int threadID, Object result ){
		if ( mConnectListener != null )
			mConnectListener.onConnected(threadID, result);
		else
			DebugTool.show("Can't handle without listener");
	}
//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			if ( mConnectListener != null )
//				mConnectListener.onConnected(msg.what, msg.obj);
//			else
//				DebugTool.show("Can't handle without listener");
//		}
//	};


	private abstract static class ConnectThread extends Thread{
		private static int count = START_USE_ID_OF_THREAD;
		private int threadID;
//		public ConnectThread(){
//			super();
//			init();
//		}
		
		public ConnectThread(String threadName){
			super(threadName);
			init();
		}
		
		private void init(){
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
		void onConnected(int threadID, Object message);
	}
}
