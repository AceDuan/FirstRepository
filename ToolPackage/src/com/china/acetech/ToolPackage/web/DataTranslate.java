package com.china.acetech.ToolPackage.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.java.CalendarToolForSync;
import com.china.acetech.ToolPackage.web.customsoap.MySoapObject;
import com.china.acetech.ToolPackage.web.customsoap.SoapXmlMaker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * 注意！！userinfo 包含了自定义soap和使用第三方的http协议的数据交互定义。
 *
 * Service和AP通信的接口參數定義及數據交換規則定義<br/>
 * 兩種轉換模式 JSON轉數據JSON2DB，數據轉JSOND2J(在數據上傳時暫時沒有使用JSON，而是字串直接使用，故使用的BD2Map)
 * 
 * 最新的要求，服務器存儲的時間以秒為單位，所以與服務器進行數據交換時需要進行單位轉換。
 * @author bxc2010011
 *
 */
public class DataTranslate {

//	private static final String NULLString = "NULL";
	private static final String LASTSYNCTIME = "ModifyDate";  //LastSyncTime is used in every entity
	
	public static final String RETURN_VALUE = "ReturnString";
	
//	public static Date getLastSyncTime(String message){
//		try {
//			return getLastSyncTime(new JSONArray(message));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public static Date getLastSyncTime(JSONArray array){
//		Date date = null;
//		try {
//			String time = array.getJSONObject(0).getString(LASTSYNCTIME);
//			if ( time.equals("null") ) return SBApplication.getZeroTime();
//			if ( !time.equals(NULLString) )
//				date = new Date(Long.valueOf(time));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		return date;
//	}
//	
//	public static Date getLastSyncTime(SoapObject object){
//		SoapObject container = (SoapObject)object.getProperty(0);
//		
//		
//		String lastSyncTime = container.getPropertySafelyAsString(LASTSYNCTIME);
//		if ( lastSyncTime.length() == 0 )
//			return new Date(0);
//		else
//			return new Date(Long.valueOf(lastSyncTime));
//	}
	
	/**
	 * 從服務器抓取的時間以秒為單位，需要乘以1000
	 * @param nodeObject
	 * @return
	 */
	public static Date getLastSyncTime(Node nodeObject){
		String lastSyncTime = GetElementsValueByTagName(LASTSYNCTIME, (Element)nodeObject);
		
		if ( lastSyncTime == null )
			return CalendarToolForSync.getZeroTime();
		else if ( lastSyncTime.length() == 0 )
			return CalendarToolForSync.getZeroTime();
		else
			return new Date(Long.valueOf(lastSyncTime)*1000);
	}
	
	
	public static PropertyInfo makeSoapEntity( String name, String value){
		PropertyInfo info = new PropertyInfo();
		info.setName(name);
		info.setValue(value);
		info.setNamespace(SoapXmlMaker.BXCName);
		
		return info;
	}

	/**
	 * 每天的運動數據信息的交互規則
	 * @author bxc2010011
	 *
	 */
	public static class SportInfo{

		private static final String NUMBER = "ID";
		private static final String STEPS = "StepCounts";
		private static final String DISTANCE = "Distance";
		private static final String CALORIES = "Calories";
		private static final String ACTIVE = "Time";
		private static final String FLOOR = "FloorsCounts";
		private static final String SLEEPSTATUS = "SleepStatusInfo";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailySportsSatusRecords";

		private static final String DATA_TITLE = "SportsSatusRecords";
		public static final String DATA_NAME = "SportsStatusRecordsSet";

		public static final String DownLoad_Data_Name = "StatisticsTable";

		public static void JSON2DB(SportInfo_AP entity, JSONArray string){
			try {
				String temp;
				JSONObject dataSet = string.getJSONObject(0);
				temp = dataSet.getString(STEPS);
				entity.setSteps(Integer.valueOf(temp));
				temp = dataSet.getString(DISTANCE);
				entity.setDistance(Integer.valueOf(temp));
				temp = dataSet.getString(CALORIES);
				entity.setCalories(Integer.valueOf(temp));
				temp = dataSet.getString(ACTIVE);
				entity.setActive(Long.valueOf(temp));
				temp = dataSet.getString(FLOOR);
				entity.setFloor(Integer.valueOf(temp));
				temp = dataSet.getString(DATE);
				entity.setCalendar(new Date(Long.valueOf(temp)));
				temp = dataSet.getString(LASTSYNCTIME);
				entity.setLastsynctime(new Date(Long.valueOf(temp)));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		public static void DB2WebMap(HashMap<String, String> map, SportInfo_AP APEntity){
			map.put(STEPS, String.valueOf( APEntity.getSteps() ) );
			map.put(DISTANCE, String.valueOf( APEntity.getDistance() ) );
			map.put(CALORIES, String.valueOf( APEntity.getCalories() ) );
			map.put(ACTIVE, String.valueOf( APEntity.getActive() ) );
			map.put(FLOOR, String.valueOf( APEntity.getFloor() ) );
			map.put(DATE, String.valueOf( APEntity.getCalendar().getTime() ) );
			map.put(LASTSYNCTIME, String.valueOf( APEntity.getLastsynctime().getTime()) );

		}

		public static long getDateOfSportData(SoapObject object){
			SoapObject sportInfo = (SoapObject)object.getProperty(SOAP_TITLE);
			String date = sportInfo.getPropertySafelyAsString(DATE);
			return Long.valueOf(date);
		}
		/**
		 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
		 * @param object
		 * @return
		 */
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty("StatisticsTable", (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}

		public static void Soap2DB(SportInfo_AP entity, SoapObject object){
			String temp;
			SoapObject sportInfo = (SoapObject)object.getProperty(SOAP_TITLE);

			temp = sportInfo.getPropertySafelyAsString(STEPS);
			entity.setSteps(Integer.valueOf(temp));
			temp = sportInfo.getPropertySafelyAsString(DISTANCE);
			entity.setDistance(Integer.valueOf(temp));
			temp = sportInfo.getPropertySafelyAsString(CALORIES);
			entity.setCalories(Integer.valueOf(temp));
			temp = sportInfo.getPropertySafelyAsString(ACTIVE);
			entity.setActive(Long.valueOf(temp));
			temp = sportInfo.getPropertySafelyAsString(FLOOR);
			entity.setFloor(Integer.valueOf(temp));
			temp = sportInfo.getPropertySafelyAsString(DATE);
			if ( temp.length() == 0 ) temp = "0";
			entity.setCalendar(new Date(Long.valueOf(temp)));
			temp = sportInfo.getPropertySafelyAsString(LASTSYNCTIME);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastsynctime(new Date(Long.valueOf(temp)));
		}


		public static PropertyInfo DB2SoapOfOneEntity( SportInfo_AP APEntity){


			PropertyInfo Proinfo = new PropertyInfo();

			//info.setName(name)
			//object.addProperty(ACCOUNT, APEntity.getAccount());
			SoapObject info = new SoapObject("http://wristband.besta.com", "anyType");



			Proinfo.setValue(info);
			Proinfo.setType(SoapObject.class);
			Proinfo.setName(DATA_TITLE);
			Proinfo.setNamespace("http://www.w3.org/2001/XMLSchema");
			return Proinfo;
		}

		public static PropertyInfo DB2SoapMap( List<SportInfo_AP> entityList ){
			PropertyInfo resultSet = new PropertyInfo();
			SoapObject resultObject = new SoapObject("http://wristband.besta.com", "anyType");

			SoapObject object = new SoapObject("http://wristband.besta.com", "anyType");

			for ( SportInfo_AP entity : entityList){

				object.addProperty( DB2SoapOfOneEntity(entity) );
			}

			PropertyInfo sportInfo = new PropertyInfo();
			sportInfo.setValue(object);
			sportInfo.setType(SoapObject.class);
			sportInfo.setName(DATA_NAME);
			//到此處所有數據相關的集合已經創建完畢，按照文檔參數的順序是日期，集合，修改時間。

			PropertyInfo date = new PropertyInfo();
			date.setName(DATE);
			date.setValue(entityList.get(0).getCalendar().getTime());

			PropertyInfo syncTime = new PropertyInfo();
			syncTime.setName(LASTSYNCTIME);
			syncTime.setValue(entityList.get(0).getLastsynctime().getTime());

			resultObject.addProperty(date);
			resultObject.addProperty(sportInfo);
			resultObject.addProperty(syncTime);

			resultSet.setName(SOAP_TITLE);
			resultSet.setValue(resultObject);


			return resultSet;

		}

		public static void Node2DB(SportInfo_AP entity, Node object){

			Element sportInfo = (Element)object;

			String temp = GetElementsValueByTagName(STEPS, sportInfo);
			entity.setSteps(Integer.valueOf(temp));
			temp = GetElementsValueByTagName(DISTANCE, sportInfo);
			entity.setDistance(Integer.valueOf(temp));
			temp = GetElementsValueByTagName(CALORIES, sportInfo);
			entity.setCalories(Integer.valueOf(temp));
			temp = GetElementsValueByTagName(ACTIVE, sportInfo);
			entity.setActive(Long.valueOf(temp));
			temp = GetElementsValueByTagName(FLOOR, sportInfo);
			entity.setFloor(Integer.valueOf(temp));
			temp = GetElementsValueByTagName(DATE, sportInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setCalendar(new Date(S2CTime(temp)));
			temp = GetElementsValueByTagName(LASTSYNCTIME, sportInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastsynctime(new Date(S2CTime(temp)));
		}

		public static final String category = "StatisticsTable";
		public static void NodeSet2DBList(List<SportInfo_AP> entityList, int timeStyle, Node objectSet){

			Element sportInfo = (Element)objectSet;

			NodeList list = GetElementListByTagName(category, sportInfo);
			Node object;
			SportInfo_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				entity = new SportInfo_AP(null, null);
				entity.setTimeStyle(timeStyle);
				Node2DB(entity, object);
				entityList.add(entity);
			}
		}

		public static MySoapObject DB2OneMySoap( SportInfo_AP entity){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, "StatisticsTable");
			MySoapObject child;

			//這裡有問題，需要抓取的是統計數據而不是基礎數據，需要重新建立一張表
			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entity.getCalendar().getTime()));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, STEPS);
			child.setValue(String.valueOf(entity.getSteps()));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, DISTANCE);
			child.setValue(String.valueOf(entity.getDistance()));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, CALORIES);
			child.setValue(String.valueOf(entity.getCalories()));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, ACTIVE);
			child.setValue(String.valueOf(entity.getActive()));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, FLOOR);
			child.setValue(String.valueOf(entity.getFloor()));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, SLEEPSTATUS);
			child.setValue(String.valueOf(entity.getNumber()));
			root.addItem(child);

			return root;
		}
		public static MySoapObject DB2MySoapObject( List<SportInfo_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entityList.get(0).getCalendar().getTime()));
			root.addItem(child);

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<SportInfo_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( SportInfo_AP entity : entityList ){
				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, NUMBER);
				child.setValue(String.valueOf(entity.getNumber()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, STEPS);
				child.setValue(String.valueOf(entity.getSteps()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, DISTANCE);
				child.setValue(String.valueOf(entity.getDistance()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, CALORIES);
				child.setValue(String.valueOf(entity.getCalories()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, ACTIVE);
				child.setValue(String.valueOf(entity.getActive()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, FLOOR);
				child.setValue(String.valueOf(entity.getFloor()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, SLEEPSTATUS);
				child.setValue(String.valueOf(entity.getSleepStatus()));
				oneDataSet.addItem(child);
			}

			return root;
		}


	}



	private static final String[] stringMap = {"<", "&lt;"};
	public static String getXMLStandardString(String source){
		StringBuffer buffer = new StringBuffer();
		
		
		int position = 0;
		while ( position < source.length() ){
			String ch = String.valueOf(source.charAt(position));
			for (int i = 0; i < stringMap.length; i+= 2){
				if ( stringMap[i].equals(ch)){
					ch = stringMap[i+1];
					break;
				}
			}
			buffer.append(ch);
				
			position++;
		}
		
		return buffer.toString();
	}
	public static String GetElementsValueByTagName(String TagName, Element element)
	{
		String RetValue = "";
		if ( !isElementEmpty(TagName, element) )
			RetValue = element.getElementsByTagName(SoapXmlMaker.BXCPreix + ":"+TagName).item(0).getFirstChild().getNodeValue();
		
		return RetValue;
	}
	
	public static Node GetElementsByTagName(String TagName, Element element){
		return element.getElementsByTagName(SoapXmlMaker.BXCPreix + ":" + TagName).item(0);
	}
	
	public static NodeList GetElementListByTagName(String TagName, Element element){
		return element.getElementsByTagName(SoapXmlMaker.BXCPreix + ":" + TagName);
	}
	
	public static boolean isElementEmpty(String TagName, Element element){
		Node object = GetElementsByTagName(TagName, element);
		NamedNodeMap map = object.getAttributes();
		Node attr = map.getNamedItem("i:nil");
		
		return attr != null && attr.getNodeValue().equals("true");
	}
	
	public static String C2STime(long clientTime){
		return String.valueOf(clientTime/1000);
	}
	public static long S2CTime(String serverTime){
		return Long.valueOf(serverTime)*1000;
	}
}



/*
	*//**
 * 用戶個人信息的交互規則
 * @author bxc2010011
 *
 *//*
	public static class UserInfo{

		private static final String ACCOUNT = "UserName";
		private static final String USERNAME = "ShortName";
		private static final String SEX = "Gender";
		private static final String BRITHDAY = "BirthDate";
		private static final String HEIGHT = "Height";
		private static final String WEIGHT = "Weight";
		private static final String LENGTHUNIT = "LengthUnits";
		private static final String WEIGHTUNIT = "WeightUnits";
		private static final String VOLUMEUNIT = "VolumeUnits";
		private static final String FOODLOCAL = "FoodLocal";
		private static final String COUNTRY = "Country";
		private static final String TIMEZONE = "TimeZone";
		private static final String REGESITERTIME = "RegistrationDate";


		public static final String SOAP_TITLE = "AccountInfo";

		public static void JSON2DB(UserInfo_AP entity, JSONArray string){
			try {
				String temp;
				JSONObject dataSet = string.getJSONObject(0);

//				temp = dataSet.getString(ACCOUNT);
//				entity.setAccount(temp);
				temp = dataSet.getString(USERNAME);
				entity.setUserName(temp);
				temp = dataSet.getString(SEX);
				entity.setSex(temp);
				temp = dataSet.getString(BRITHDAY);
				entity.setBirthday(new Date(Long.valueOf(temp)));
				temp = dataSet.getString(HEIGHT);
				entity.setPersionHeight(Long.valueOf(temp));
				temp = dataSet.getString(WEIGHT);
				entity.setPersionWeight(Long.valueOf(temp));
				temp = dataSet.getString(LENGTHUNIT);
				entity.setLengthUnit(temp);
				temp = dataSet.getString(WEIGHTUNIT);
				entity.setWeightUnit(temp);
				temp = dataSet.getString(VOLUMEUNIT);
				entity.setVolumeUnit(temp);
				temp = dataSet.getString(FOODLOCAL);
				entity.setFoodLocal(temp);
				temp = dataSet.getString(COUNTRY);
				entity.setCountry(temp);
				temp = dataSet.getString(TIMEZONE);
				entity.setTimeZone(temp);
				temp = dataSet.getString(REGESITERTIME);
				entity.setRegisterDate(new Date(Long.valueOf(temp)));
				temp = dataSet.getString(LASTSYNCTIME);
				entity.setLastSyncTime(new Date(Long.valueOf(temp)));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		public static void DB2WebMap(HashMap<String, String> map, UserInfo_AP APEntity){

			map.put(ACCOUNT, APEntity.getAccount());
			map.put(USERNAME, APEntity.getUserName());
			map.put(SEX, APEntity.getSex());
			map.put(BRITHDAY, String.valueOf( APEntity.getRegisterDate().getTime()));
			map.put(HEIGHT, APEntity.getPersionHeight().toString());
			map.put(WEIGHT, APEntity.getPersionWeight().toString());
			map.put(LENGTHUNIT, APEntity.getLengthUnit());
			map.put(WEIGHTUNIT, APEntity.getWeightUnit());
			map.put(VOLUMEUNIT, APEntity.getVolumeUnit());
			map.put(REGESITERTIME, String.valueOf( APEntity.getRegisterDate().getTime()) );
			map.put(LASTSYNCTIME, String.valueOf( APEntity.getLastSyncTime().getTime()) );

		}

		public static void Soap2DB(UserInfo_AP entity, SoapObject object){
			SoapObject accountInfo = (SoapObject)object.getProperty(SOAP_TITLE);

			String temp = accountInfo.getPropertySafelyAsString(USERNAME);
			entity.setUserName(temp);
			temp = accountInfo.getPropertySafelyAsString(SEX);
			entity.setSex(temp);
			temp = accountInfo.getPropertySafelyAsString(BRITHDAY);
			if ( temp.length() == 0 ) temp = "0";
			entity.setBirthday(new Date(Long.valueOf(temp)));
			temp = accountInfo.getPropertySafelyAsString(HEIGHT);
			entity.setPersionHeight(Long.valueOf(temp));
			temp = accountInfo.getPropertySafelyAsString(WEIGHT);
			entity.setPersionWeight(Long.valueOf(temp));
			temp = accountInfo.getPropertySafelyAsString(LENGTHUNIT);
			entity.setLengthUnit(temp);
			temp = accountInfo.getPropertySafelyAsString(WEIGHTUNIT);
			entity.setWeightUnit(temp);
			temp = accountInfo.getPropertySafelyAsString(VOLUMEUNIT);
			entity.setVolumeUnit(temp);
			temp = accountInfo.getPropertySafelyAsString(FOODLOCAL);
			entity.setFoodLocal(temp);
			temp = accountInfo.getPropertySafelyAsString(COUNTRY);
			entity.setCountry(temp);
			temp = accountInfo.getPropertySafelyAsString(TIMEZONE);
			entity.setTimeZone(temp);
			temp = accountInfo.getPropertySafelyAsString(REGESITERTIME);
			if ( temp.length() == 0 ) temp = "0";
			entity.setRegisterDate(new Date(Long.valueOf(temp)));
			temp = accountInfo.getPropertySafelyAsString(LASTSYNCTIME);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastSyncTime(new Date(Long.valueOf(temp)));



			//temp = ((SoapObject)accountInfo.getProperty(USERNAME)).get;
		}

		public static PropertyInfo DB2SoapMap( UserInfo_AP APEntity){


			PropertyInfo Proinfo = new PropertyInfo();

			//info.setName(name)
			//object.addProperty(ACCOUNT, APEntity.getAccount());
			SoapObject info = new SoapObject("http://wristband.besta.com", "anyType");

			info.addProperty(makeSoapEntity(REGESITERTIME,
						String.valueOf( APEntity.getRegisterDate().getTime())));
			info.addProperty(makeSoapEntity(ACCOUNT, APEntity.getAccount()));
			info.addProperty(makeSoapEntity(USERNAME, APEntity.getUserName()));
			info.addProperty(makeSoapEntity(SEX, APEntity.getSex()));
			info.addProperty(makeSoapEntity(HEIGHT, APEntity.getPersionHeight().toString()));
			info.addProperty(makeSoapEntity(WEIGHT, APEntity.getPersionWeight().toString()));
			info.addProperty(makeSoapEntity(BRITHDAY,
					String.valueOf( APEntity.getRegisterDate().getTime())));
			info.addProperty(makeSoapEntity(LENGTHUNIT, APEntity.getLengthUnit()));
			info.addProperty(makeSoapEntity(WEIGHTUNIT, APEntity.getWeightUnit()));
			info.addProperty(makeSoapEntity(VOLUMEUNIT, APEntity.getVolumeUnit()));
			info.addProperty(makeSoapEntity(FOODLOCAL, APEntity.getFoodLocal()));
			info.addProperty(makeSoapEntity(COUNTRY, APEntity.getCountry()));
			info.addProperty(makeSoapEntity(TIMEZONE, APEntity.getTimeZone()));
			info.addProperty(makeSoapEntity(LASTSYNCTIME,
					String.valueOf( APEntity.getLastSyncTime().getTime())));


			Proinfo.setValue(info);
			Proinfo.setType(SoapObject.class);
			Proinfo.setName(SOAP_TITLE);
			Proinfo.setNamespace("http://www.w3.org/2001/XMLSchema");
			return Proinfo;
		}

		public static MySoapObject DB2MySoapObject( UserInfo_AP APEntity){

			MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, "AccountInfo");

			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, REGESITERTIME);
			child.setValue(C2STime( APEntity.getRegisterDate().getTime()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, ACCOUNT);
			child.setValue(APEntity.getAccount());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, USERNAME);
			child.setValue(APEntity.getUserName());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, SEX);
			child.setValue(APEntity.getSex());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, HEIGHT);
			child.setValue(APEntity.getPersionHeight().toString());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, WEIGHT);
			child.setValue(APEntity.getPersionWeight().toString());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, BRITHDAY);
			child.setValue(C2STime( APEntity.getBirthday().getTime()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LENGTHUNIT);
			child.setValue(APEntity.getLengthUnit());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, WEIGHTUNIT);
			child.setValue(APEntity.getWeightUnit());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, VOLUMEUNIT);
			child.setValue(APEntity.getVolumeUnit());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, FOODLOCAL);
			child.setValue(APEntity.getFoodLocal());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, COUNTRY);
			child.setValue(APEntity.getCountry());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, TIMEZONE);
			child.setValue(APEntity.getTimeZone());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime( APEntity.getLastSyncTime().getTime()));
			object.addItem(child);

			return object;
		}

		public static void Node2DB(UserInfo_AP entity, Node object){
			Element accountInfo = (Element)object;

			String temp = GetElementsValueByTagName(USERNAME, accountInfo);
			entity.setUserName(temp);
			temp = GetElementsValueByTagName(SEX, accountInfo);
			entity.setSex(temp);
			temp = GetElementsValueByTagName(BRITHDAY, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setBirthday(new Date(S2CTime(temp)));
			temp = GetElementsValueByTagName(HEIGHT, accountInfo);
			entity.setPersionHeight(Long.valueOf(temp));
			temp = GetElementsValueByTagName(WEIGHT, accountInfo);
			entity.setPersionWeight(Long.valueOf(temp));
			temp = GetElementsValueByTagName(LENGTHUNIT, accountInfo);
			entity.setLengthUnit(temp);
			temp = GetElementsValueByTagName(WEIGHTUNIT, accountInfo);
			entity.setWeightUnit(temp);
			temp = GetElementsValueByTagName(VOLUMEUNIT, accountInfo);
			entity.setVolumeUnit(temp);
			temp = GetElementsValueByTagName(FOODLOCAL, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setFoodLocal(temp);
			temp = GetElementsValueByTagName(COUNTRY, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setCountry(temp);
			temp = GetElementsValueByTagName(TIMEZONE, accountInfo);
			entity.setTimeZone(temp);
			temp = GetElementsValueByTagName(REGESITERTIME, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setRegisterDate(new Date(S2CTime(temp)));
			temp = GetElementsValueByTagName(LASTSYNCTIME, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastSyncTime(new Date(S2CTime(temp)));



			//temp = ((SoapObject)accountInfo.getProperty(USERNAME)).get;
		}
	}

	*//**
 * 手环设定信息
 * @author BXC2010011
 *
 *//*
	public static class DeviceInfo{

		private static final String PLACEMENT = "HabitHand";
		private static final String STATEDISPLAY = "TargetList";
		private static final String CLOCKDISPLAY = "TimeFormat";
		private static final String SYNCDATE = "SyncDate";
		private static final String MAINGOAL = "MainTarget";


		public static final String SOAP_TITLE = "BraceletInfo";

		public static MySoapObject DB2MySoapObject( DeviceInfo_AP APEntity){

			MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);

			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, MAINGOAL);
			child.setValue(APEntity.getMainGoal() );
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, PLACEMENT);
			child.setValue(APEntity.getWristPlacement());
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, STATEDISPLAY);
			child.setValue(String.valueOf(APEntity.getStatsDisplay()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, CLOCKDISPLAY);
			child.setValue(String.valueOf(APEntity.getClockDisplay()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, SYNCDATE);
			child.setValue(C2STime(APEntity.getSyncDate().getTime()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime( APEntity.getLastsynctime().getTime()));
			object.addItem(child);

			return object;
		}

		public static void Node2DB(DeviceInfo_AP entity, Node object){
			Element sportGoal = (Element)object;

			String temp = GetElementsValueByTagName(MAINGOAL, sportGoal);
			entity.setMainGoal(temp);

			temp = GetElementsValueByTagName(PLACEMENT, sportGoal);
			entity.setWristPlacement(temp);

			temp = GetElementsValueByTagName(STATEDISPLAY, sportGoal);
			entity.setStatsDisplay(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(CLOCKDISPLAY, sportGoal);
			entity.setClockDisplay(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(SYNCDATE, sportGoal);
			if ( temp.length() == 0 ) temp = "0";
			entity.setSyncDate(new Date(S2CTime(temp)));

			temp = GetElementsValueByTagName(LASTSYNCTIME, sportGoal);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastsynctime(new Date(S2CTime(temp)));

		}
	}

	*//**
 * 運動目標信息的交互規則
 * @author bxc2010011
 *
 *//*
	public static class SportGoal{

		private static final String STEPS = "StepCounts";
		private static final String DISTANCE = "Distance";
		private static final String CALORIES = "Calories";
		private static final String ACTIVE = "SportTime";
		private static final String FLOOR = "FloorsCounts";
		private static final String TARGETWEIGHT = "WeightTarget";
		private static final String WATERVOLUMES = "WaterVolumes";

		public static final String SOAP_TITLE = "SportsTarget";

		public static void JSON2DB(SportGoal_AP entity, JSONArray string){
			try {
				String temp;
				JSONObject dataSet = string.getJSONObject(0);
				temp = dataSet.getString(STEPS);
				entity.setSteps(Integer.valueOf(temp));
				temp = dataSet.getString(DISTANCE);
				entity.setDistance(Integer.valueOf(temp));
				temp = dataSet.getString(CALORIES);
				entity.setCalories(Integer.valueOf(temp));
				temp = dataSet.getString(ACTIVE);
				entity.setActive(Long.valueOf(temp));
				temp = dataSet.getString(FLOOR);
				entity.setFloor(Integer.valueOf(temp));
				temp = dataSet.getString(TARGETWEIGHT);
				entity.setTargetWeight(Long.valueOf(temp));
				temp = dataSet.getString(LASTSYNCTIME);
				entity.setLastsynctime(new Date(Long.valueOf(temp)));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		public static void DB2WebMap(HashMap<String, String> map, SportGoal_AP APEntity){
			map.put(STEPS, String.valueOf( APEntity.getSteps() ) );
			map.put(DISTANCE, String.valueOf( APEntity.getDistance() ) );
			map.put(CALORIES, String.valueOf( APEntity.getCalories() ) );
			map.put(ACTIVE, String.valueOf( APEntity.getActive() ) );
			map.put(FLOOR, String.valueOf( APEntity.getFloor() ) );
			map.put(TARGETWEIGHT, String.valueOf( APEntity.getTargetWeight() ) );
			map.put(LASTSYNCTIME, String.valueOf( APEntity.getLastsynctime().getTime()) );

		}

		public static void Soap2DB(SportGoal_AP entity, SoapObject object){

			SoapObject sportGoalInfo = (SoapObject)object.getProperty(SOAP_TITLE);

			String temp;
			temp = sportGoalInfo.getPropertySafelyAsString(STEPS);
			entity.setSteps(Integer.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(DISTANCE);
			entity.setDistance(Integer.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(CALORIES);
			entity.setCalories(Integer.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(ACTIVE);
			entity.setActive(Long.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(FLOOR);
			entity.setFloor(Integer.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(TARGETWEIGHT);
			entity.setTargetWeight(Long.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(WATERVOLUMES);
			entity.setWaterVolumes(Integer.valueOf(temp));
			temp = sportGoalInfo.getPropertySafelyAsString(LASTSYNCTIME);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastsynctime(new Date(Long.valueOf(temp)));
		}


		public static MySoapObject DB2MySoapObject( SportGoal_AP APEntity){

			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, "SportsTarget");

			MySoapObject child;
			child = new MySoapObject(SoapXmlMaker.BXCName, STEPS);
			child.setValue(String.valueOf( APEntity.getSteps() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, DISTANCE);
			child.setValue(String.valueOf( APEntity.getDistance() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, CALORIES);
			child.setValue(String.valueOf( APEntity.getCalories() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, ACTIVE);
			child.setValue(String.valueOf( APEntity.getActive() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, FLOOR);
			child.setValue(String.valueOf( APEntity.getFloor() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, TARGETWEIGHT);
			child.setValue(String.valueOf( APEntity.getTargetWeight() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, WATERVOLUMES);
			child.setValue(String.valueOf( APEntity.getWaterVolumes() ));
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime( APEntity.getLastsynctime().getTime() ));
			root.addItem(child);

			return root;
		}

		public static void Node2DB(SportGoal_AP entity, Node object){
			Element sportGoal = (Element)object;

			String temp = GetElementsValueByTagName(STEPS, sportGoal);
			entity.setSteps(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(DISTANCE, sportGoal);
			entity.setDistance(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(CALORIES, sportGoal);
			entity.setCalories(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(ACTIVE, sportGoal);
			entity.setActive(Long.valueOf(temp));

			temp = GetElementsValueByTagName(FLOOR, sportGoal);
			entity.setFloor(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(TARGETWEIGHT, sportGoal);
			entity.setTargetWeight(S2CTime(temp));

			temp = GetElementsValueByTagName(WATERVOLUMES, sportGoal);
			entity.setWaterVolumes(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(LASTSYNCTIME, sportGoal);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastsynctime(new Date(S2CTime(temp)));

		}
	}



	public static class WaterLog{

		private static final String WaterVolumes = "WaterVolumes";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailyWaterRecords";

		private static final String DATA_TITLE = "WaterRecords";
		public static final String DATA_NAME = "WaterRecordsSet";

		public static final String SOAP_MUL_TITLE = "MultiDayWaterRecords";
		*//**
 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
 * @param object
 * @return
 *//*
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty(DATA_NAME, (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}


		public static MySoapObject DB2MySoapObject( List<WaterLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entityList.get(0).getCalendar().getTime()));
			root.addItem(child);

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<WaterLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( WaterLog_AP entity : entityList ){
				if ( entity.getWaterGet() == -1 ){
					root.removeAllItem();
					break;
				}

				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, WaterVolumes);
				child.setValue(String.valueOf(entity.getWaterGet()));
				oneDataSet.addItem(child);
			}

			return root;
		}

		public static void Node2DB(WaterLog_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setWaterGet(-1l);
				return;
			}

			String temp = GetElementsValueByTagName(WaterVolumes, waterInfo);
			entity.setWaterGet(Long.valueOf(temp));
		}

		public static void NodeSet2DBList(List<WaterLog_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";

			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			WaterLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new WaterLog_AP(null);
				 entity.setCalendar(new Date(S2CTime(date)));
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}

		public static void MulNodeSet2DBList(List<List<WaterLog_AP>> entityList, Node mulObjectSet){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			List<WaterLog_AP> onedayList;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				onedayList = new ArrayList<WaterLog_AP>();
				NodeSet2DBList(onedayList, object);
				entityList.add(onedayList);
			}
		}
	}

	public static class SleepLog{

		private static final String StartDate = "StartDate";
		private static final String EndDate = "EndDate";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailySleepRecords";

		private static final String DATA_TITLE = "SleepRecords";
		public static final String DATA_NAME = "SleepRecordsSet";

		public static final String SOAP_MUL_TITLE = "MultiDaySleepRecords";
		*//**
 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
 * @param object
 * @return
 *//*
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty(DATA_NAME, (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}


		public static MySoapObject DB2MySoapObject( List<SleepLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entityList.get(0).getCalendar().getTime()));
			root.addItem(child);

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<SleepLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( SleepLog_AP entity : entityList ){
				if ( entity.getSleepTime() == 0 ){
					root.removeAllItem();
					break;
				}
				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, StartDate);
				child.setValue(C2STime(entity.getStartTime().getTime()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, EndDate);
				child.setValue(C2STime(entity.getStartTime().getTime()+entity.getSleepTime()*60*1000));
				oneDataSet.addItem(child);
			}

			return root;
		}

		public static void Node2DB(SleepLog_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setSleepTime(0l);
				return;
			}
			String temp = GetElementsValueByTagName(StartDate, waterInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setStartTime(new Date(S2CTime(temp)));

			temp = GetElementsValueByTagName(EndDate, waterInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setSleepTime((S2CTime(temp) - entity.getStartTime().getTime())/(60*1000));

//			temp = GetElementsValueByTagName(DATE, waterInfo);
//			if ( temp.length() == 0 ) temp = "0";
//			entity.setCalendar(new Date(S2CTime(temp)));
//			temp = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
//			if ( temp.length() == 0 ) temp = "0";
//			entity.setLastsynctime(new Date(S2CTime(temp)));
		}

		public static void NodeSet2DBList(List<SleepLog_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";


			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			SleepLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new SleepLog_AP(null);
				 entity.setCalendar(new Date(S2CTime(date)));
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}

		public static void MulNodeSet2DBList(List<List<SleepLog_AP>> entityList, Node mulObjectSet){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			List<SleepLog_AP> onedayList;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				onedayList = new ArrayList<SleepLog_AP>();
				NodeSet2DBList(onedayList, object);
				entityList.add(onedayList);
			}
		}

	}

	public static class SleepStatus{

		private static final String StartDate = "StartDate";
		private static final String EndDate = "EndDate";
		private static final String Status = "Status";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailySleepStatus";

		private static final String DATA_TITLE = "SleepStatus";
		public static final String DATA_NAME = "SleepStatusSet";

		public static final String SOAP_MUL_TITLE = "MultiDaySleepStatus";
		*//**
 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
 * @param object
 * @return
 *//*
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty(DATA_NAME, (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}


		public static void Node2DB(SleepStatus_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setSleepDtatus(-1);
				return;
			}

			String temp = GetElementsValueByTagName(StartDate, waterInfo);
			entity.setStartDate(Long.valueOf(temp)*1000);
			temp = GetElementsValueByTagName(EndDate, waterInfo);
			entity.setEndDate(Long.valueOf(temp)*1000);
			temp = GetElementsValueByTagName(Status, waterInfo);
			entity.setSleepDtatus(Integer.valueOf(temp));
		}

		public static void NodeSet2DBList(List<SleepStatus_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";

			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			SleepStatus_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new SleepStatus_AP(null);
				 entity.setCalendar(new Date(S2CTime(date)));
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}

		public static void MulNodeSet2DBList(List<List<SleepStatus_AP>> entityList, Node mulObjectSet){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			List<SleepStatus_AP> onedayList;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				onedayList = new ArrayList<SleepStatus_AP>();
				NodeSet2DBList(onedayList, object);
				entityList.add(onedayList);
			}
		}
	}

	public static class SleepStatistic{


		private static final String SleepTime = "SleepTime";
		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "SleepStatisticsTable";


		public static final String SOAP_MUL_TITLE = "MultiSleepStatisticsTable";

		public static void Node2DB(SleepStatistic_AP entity, Node object){

			Element waterInfo = (Element)object;

			String temp = GetElementsValueByTagName(SleepTime, waterInfo);
			entity.setSleepTime(Integer.valueOf(temp));

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			entity.setCalendar(new Date(S2CTime(date)));

			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";
			entity.setLastsynctime(new Date(S2CTime(syncDate)));

		}

		public static void MulNodeSet2DBList(List<SleepStatistic_AP> entityList, Node mulObjectSet, int timeStyle){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			SleepStatistic_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){

				entity = new SleepStatistic_AP(null);
				entity.setTimeStyle(timeStyle);
				Node2DB(entity, object);
				entityList.add(entity);
			}
		}

	}

	public static class ActiveLog{

		private static final String StartDate = "StartDate";
		private static final String Time = "Time";
		private static final String SportsName = "SportsName";
		private static final String CaloriePerUnit = "CaloriePerUnit";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailySportsRecords";

		private static final String DATA_TITLE = "SportsRecords";
		public static final String DATA_NAME = "SportsRecordsSet";

		public static final String SOAP_MUL_TITLE = "MultiDaySportsRecords";
		*//**
 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
 * @param object
 * @return
 *//*
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty(DATA_NAME, (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}


		public static MySoapObject DB2MySoapObject( List<ActiveLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entityList.get(0).getCalendar().getTime()));
			root.addItem(child);

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<ActiveLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( ActiveLog_AP entity : entityList ){
				if ( entity.getCaloriePerUnit() == -1 ){
					root.removeAllItem();
					break;
				}

				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, StartDate);
				child.setValue(C2STime(entity.getStartTime().getTime()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, Time);
				child.setValue(String.valueOf(entity.getActiveTime()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, SportsName);
				child.setValue(getXMLStandardString(entity.getSportName()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, CaloriePerUnit);
				child.setValue(String.valueOf(entity.getCaloriePerUnit()));
				oneDataSet.addItem(child);
			}

			return root;
		}

		public static void Node2DB(ActiveLog_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setCaloriePerUnit(-1);
				return;
			}

			String temp = GetElementsValueByTagName(StartDate, waterInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setStartTime(new Date(S2CTime(temp)));

			temp = GetElementsValueByTagName(Time, waterInfo);
			entity.setActiveTime(Long.valueOf(temp));

			temp = GetElementsValueByTagName(SportsName, waterInfo);
			entity.setSportName(temp);

			temp = GetElementsValueByTagName(CaloriePerUnit, waterInfo);
			entity.setCaloriePerUnit(Integer.valueOf(temp));
		}

		public static void NodeSet2DBList(List<ActiveLog_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";

			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			ActiveLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new ActiveLog_AP(null);
				 entity.setCalendar(new Date(S2CTime(date)));
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}

		public static void MulNodeSet2DBList(List<List<ActiveLog_AP>> entityList, Node mulObjectSet){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			List<ActiveLog_AP> onedayList;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				onedayList = new ArrayList<ActiveLog_AP>();
				NodeSet2DBList(onedayList, object);
				entityList.add(onedayList);
			}
		}
	}

	public static class FoodLog{

		private static final String Copies = "Copies";
		private static final String MealTime = "MealTime";
		private static final String FoodName = "FoodName";
		private static final String Calories = "Calories";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailyDietRecords";

		private static final String DATA_TITLE = "DietRecords";
		public static final String DATA_NAME = "DietRecordsSet";

		public static final String SOAP_MUL_TITLE = "MultiDayDietRecords";
		*//**
 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
 * @param object
 * @return
 *//*
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty(DATA_NAME, (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}


		public static MySoapObject DB2MySoapObject( List<FoodLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entityList.get(0).getCalendar().getTime()));
			root.addItem(child);

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<FoodLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( FoodLog_AP entity : entityList ){
				if ( entity.getCalories() == -1 ){
					root.removeAllItem();
					break;
				}

				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, Copies);
				child.setValue(String.valueOf(entity.getEatNumber()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, MealTime);
				child.setValue(String.valueOf(entity.getMealTime()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, FoodName);
				child.setValue(getXMLStandardString(entity.getFoodName()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, Calories);
				child.setValue(String.valueOf(entity.getCalories()));
				oneDataSet.addItem(child);
			}

			return root;
		}

		public static void Node2DB(FoodLog_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setCalories(-1);
				return;
			}

			String temp = GetElementsValueByTagName(Copies, waterInfo);
			entity.setEatNumber(Long.valueOf(temp));

			temp = GetElementsValueByTagName(MealTime, waterInfo);
			entity.setMealTime(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(FoodName, waterInfo);
			entity.setFoodName(temp);

			temp = GetElementsValueByTagName(Calories, waterInfo);
			entity.setCalories(Integer.valueOf(temp));
		}

		public static void NodeSet2DBList(List<FoodLog_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";

			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			FoodLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new FoodLog_AP(null);
				 entity.setCalendar(new Date(S2CTime(date)));
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}

		public static void MulNodeSet2DBList(List<List<FoodLog_AP>> entityList, Node mulObjectSet){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			List<FoodLog_AP> onedayList;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				onedayList = new ArrayList<FoodLog_AP>();
				NodeSet2DBList(onedayList, object);
				entityList.add(onedayList);
			}
		}
	}

	public static class FoodList{

		private static final String FoodName = "FoodName";
		private static final String CaloriePerUnit = "CaloriePerUnit";
		private static final String Times = "Times";


		public static final String SOAP_TITLE = "FoodCategoryList";

		private static final String DATA_TITLE = "FoodCategory";
		public static final String DATA_NAME = "FoodCategorySet";


		public static void Node2DB(FoodListLog_AP entity, Node object, int flag ){


			Element sportInfo = (Element)object;

			if (!sportInfo.hasChildNodes()){
				entity.setFoodName("");
				return;
			}

			String temp = GetElementsValueByTagName(FoodName, sportInfo);
			entity.setFoodName(temp);
			temp = GetElementsValueByTagName(CaloriePerUnit, sportInfo);
			entity.setCalories(Long.valueOf(temp));
			temp = GetElementsValueByTagName(Times, sportInfo);
			entity.setUsingTimes(Integer.valueOf(temp));
			if ( flag == FoodListLogInfoSync.favorite )
				entity.setIsFavorite(true);
		}

		public static void NodeSet2DBList(List<FoodListLog_AP> entityList, Node objectSet, int flag){

			Element sportInfo = (Element)objectSet;


			String temp = GetElementsValueByTagName(LASTSYNCTIME, sportInfo);

			NodeList list = GetElementListByTagName(DATA_TITLE, sportInfo);
			Node object;
			FoodListLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new FoodListLog_AP(null);
				 entity.setLastsynctime(new Date(S2CTime(temp)));
				 Node2DB(entity, object, flag);
				 entityList.add(entity);
			}
		}

		public static MySoapObject DB2MySoapObject( List<FoodListLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;


			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			Date lastSync = RepositoryManager.getInstance().getFoodList().getById(FoodListLog_AP.SYNC_ID).getLastsynctime();
			child.setValue(C2STime(lastSync.getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<FoodListLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( FoodListLog_AP entity : entityList ){
				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, FoodName);
				child.setValue(String.valueOf(entity.getFoodName()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, CaloriePerUnit);
				child.setValue(String.valueOf(entity.getCalories()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, Times);
				child.setValue(String.valueOf(entity.getUsingTimes()));
				oneDataSet.addItem(child);
			}

			return root;
		}
	}

	public static class SportList{

		private static final String SportsName = "SportsName";
		private static final String CaloriePerUnit = "CaloriePerUnit";
		private static final String Times = "Times";


		public static final String SOAP_TITLE = "SportsCategoryList";

		private static final String DATA_TITLE = "SportsCategory>";
		public static final String DATA_NAME = "SportsCategorySet";


		public static void Node2DB(SportListLog_AP entity, Node object, int flag ){


			Element sportInfo = (Element)object;

			if (!sportInfo.hasChildNodes()){
				entity.setSportName("");
				return;
			}

			String temp = GetElementsValueByTagName(SportsName, sportInfo);
			entity.setSportName(temp);
			temp = GetElementsValueByTagName(CaloriePerUnit, sportInfo);
			entity.setCalories(Long.valueOf(temp));
			temp = GetElementsValueByTagName(Times, sportInfo);
			entity.setUsingTimes(Integer.valueOf(temp));
			if ( flag == FoodListLogInfoSync.favorite )
				entity.setIsFavorite(true);
		}

		public static void NodeSet2DBList(List<SportListLog_AP> entityList, Node objectSet, int flag){

			Element sportInfo = (Element)objectSet;


			String temp = GetElementsValueByTagName(LASTSYNCTIME, sportInfo);

			NodeList list = GetElementListByTagName(DATA_TITLE, sportInfo);
			Node object;
			SportListLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new SportListLog_AP(null);
				 entity.setLastsynctime(new Date(S2CTime(temp)));
				 Node2DB(entity, object, flag);
				 entityList.add(entity);
			}
		}

		public static MySoapObject DB2MySoapObject( List<SportListLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;


			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			Date lastSync = RepositoryManager.getInstance().getSportList().getById(SportListLog_AP.SYNC_ID).getLastsynctime();
			child.setValue(C2STime(lastSync.getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<SportListLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( SportListLog_AP entity : entityList ){
				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, SportsName);
				child.setValue(String.valueOf(entity.getSportName()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, CaloriePerUnit);
				child.setValue(String.valueOf(entity.getCalories()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, Times);
				child.setValue(String.valueOf(entity.getUsingTimes()));
				oneDataSet.addItem(child);
			}

			return root;
		}
	}

	public static class AlarmLog{

		private static final String AlarmState = "AlarmState";
		private static final String Date = "Date";
		private static final String Time = "Time";
		private static final String RepeatState = "RepeatState";
		private static final String RepeatDate = "RepeatDate";


		public static final String SOAP_TITLE = "AlarmSettingRecords";

		private static final String DATA_TITLE = "AlarmSetting";
		public static final String DATA_NAME = "AlarmSettingSet";


		public static MySoapObject DB2MySoapObject( List<AlarmClockLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<AlarmClockLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( AlarmClockLog_AP entity : entityList ){
				if ( entity.getAlarmTime() == -1 && entityList.size() == 1){
					root.removeAllItem();
					break;
				}

				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, AlarmState);
				child.setValue(String.valueOf(entity.getAlarmState()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, Date);
				child.setValue("0");
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, Time);
				child.setValue(C2STime(entity.getCalendar().getTime()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, RepeatState);
				int state = (entity.getDayOfWeek() >> 7) & 0x01;
				child.setValue(String.valueOf(state));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, RepeatDate);
				state = entity.getDayOfWeek() & 0x7f;
				child.setValue(String.valueOf(state));
				oneDataSet.addItem(child);
			}

			return root;
		}

		public static void Node2DB(AlarmClockLog_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setAlarmTime(-1);
				return;
			}

			String temp = GetElementsValueByTagName(AlarmState, waterInfo);
			entity.setAlarmState(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(Date, waterInfo);
			entity.setAlarmTime(0);
			//if ( temp.length() == 0 ) temp = "0";
			//entity.setCalendar(new Date(S2CTime(temp)));

			temp = GetElementsValueByTagName(Time, waterInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setCalendar(new Date(S2CTime(temp)));

			temp = GetElementsValueByTagName(RepeatState, waterInfo);
			int state = Integer.valueOf(temp);
			boolean res = state == 1? true:false;
			entity.setIsRepeat(res);

			temp = GetElementsValueByTagName(RepeatDate, waterInfo);
			int repeatState = Integer.valueOf(temp);
			repeatState = repeatState | (state << 7);
			entity.setDayOfWeek(repeatState);
		}

		public static void NodeSet2DBList(List<AlarmClockLog_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";

			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			AlarmClockLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new AlarmClockLog_AP(null);
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}
	}

	public static class FitnessPlan{

		private static final String INITWEIGHT = "InitialWeight";
		private static final String TARGETWEIGHT = "TargetWeight";
		private static final String FITMETHOD = "FitnessMethod";
		private static final String COMPLETEDATE = "CompletionDate";


		public static final String SOAP_TITLE = "FitnessPlan";


		public static MySoapObject DB2MySoapObject( FitnessPlan_AP APEntity){

			MySoapObject object = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);

			MySoapObject child;


			child = new MySoapObject(SoapXmlMaker.BXCName, INITWEIGHT);
			child.setValue(String.valueOf(APEntity.getInitialWeight()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, TARGETWEIGHT);
			child.setValue(String.valueOf(APEntity.getTargetWeight()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, FITMETHOD);
			child.setValue(String.valueOf(APEntity.getFitnessMethod()));
			object.addItem(child);


			child = new MySoapObject(SoapXmlMaker.BXCName, COMPLETEDATE);
			child.setValue(C2STime( APEntity.getCompletionDate().getTime()));
			object.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime( APEntity.getLastsynctime().getTime()));
			object.addItem(child);

			return object;
		}

		public static void Node2DB(FitnessPlan_AP entity, Node object){
			Element accountInfo = (Element)object;

			String temp = GetElementsValueByTagName(TARGETWEIGHT, accountInfo);
			entity.setInitialWeight(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(INITWEIGHT, accountInfo);
			entity.setTargetWeight(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(FITMETHOD, accountInfo);
			entity.setFitnessMethod(Integer.valueOf(temp));

			temp = GetElementsValueByTagName(COMPLETEDATE, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setCompletionDate(new Date(S2CTime(temp)));

			temp = GetElementsValueByTagName(LASTSYNCTIME, accountInfo);
			if ( temp.length() == 0 ) temp = "0";
			entity.setLastsynctime(new Date(S2CTime(temp)));

		}
	}

	public static class WeightLog{

		private static final String Weight = "Weight";
		private static final String FatRate = "FatRate";

		private static final String DATE = "Date";

		private static final String SOAP_TITLE = "DailyWeightRecords";

		private static final String DATA_TITLE = "WeightRecords";
		public static final String DATA_NAME = "WeightRecordsSet";

		public static final String SOAP_MUL_TITLE = "MultiDayWeightRecords";
		*//**
 * 從服務器上抓取的時間是以秒為單位的，需要乘以1000
 * @param object
 * @return
 *//*
		public static long getDateOfSportData(Node object){
			String date;
			if ( isElementEmpty(DATA_NAME, (Element)object))
				date = "0";
			else
				date = GetElementsValueByTagName(DATE, (Element)object);
			return Long.valueOf(date)*1000;
		}


		public static MySoapObject DB2MySoapObject( List<WeightLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, SOAP_TITLE);
			MySoapObject child;

			child = new MySoapObject(SoapXmlMaker.BXCName, DATE);
			child.setValue(C2STime(entityList.get(0).getCalendar().getTime()));
			root.addItem(child);

			child = getSoapOfDataSet(entityList);
			root.addItem(child);

			child = new MySoapObject(SoapXmlMaker.BXCName, LASTSYNCTIME);
			child.setValue(C2STime(entityList.get(0).getLastsynctime().getTime()));
			root.addItem(child);

			return root;

		}

		private static MySoapObject getSoapOfDataSet( List<WeightLog_AP> entityList ){
			MySoapObject root = new MySoapObject(SoapXmlMaker.BXCName, DATA_NAME);
			MySoapObject oneDataSet;
			MySoapObject child;
			for ( WeightLog_AP entity : entityList ){
				if ( entity.getWeightChange() == -1 ){
					root.removeAllItem();
					break;
				}

				oneDataSet = new MySoapObject(SoapXmlMaker.BXCName, DATA_TITLE);
				root.addItem(oneDataSet);

				child = new MySoapObject(SoapXmlMaker.BXCName, Weight);
				child.setValue(String.valueOf(entity.getWeightChange()));
				oneDataSet.addItem(child);

				child = new MySoapObject(SoapXmlMaker.BXCName, FatRate);
				child.setValue(String.valueOf(entity.getBodyfat()));
				oneDataSet.addItem(child);
			}

			return root;
		}

		public static void Node2DB(WeightLog_AP entity, Node object){

			Element waterInfo = (Element)object;

			if (!waterInfo.hasChildNodes()){
				entity.setWeightChange(-1l);
				return;
			}

			String temp = GetElementsValueByTagName(Weight, waterInfo);
			entity.setWeightChange(Long.valueOf(temp));

			temp = GetElementsValueByTagName(FatRate, waterInfo);
			entity.setBodyfat(Long.valueOf(temp));
		}

		public static void NodeSet2DBList(List<WeightLog_AP> entityList, Node objectSet){

			Element waterInfo = (Element)objectSet;

			String date = GetElementsValueByTagName(DATE, waterInfo);
			if ( date.length() == 0 ) date = "0";
			String syncDate = GetElementsValueByTagName(LASTSYNCTIME, waterInfo);
			if ( syncDate.length() == 0 ) syncDate = "0";

			NodeList list = GetElementListByTagName(DATA_TITLE, waterInfo);
			Node object;
			WeightLog_AP entity;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				 entity = new WeightLog_AP(null);
				 entity.setCalendar(new Date(S2CTime(date)));
				 entity.setLastsynctime(new Date(S2CTime(syncDate)));
				 Node2DB(entity, object);
				 entityList.add(entity);
			}
		}

		public static void MulNodeSet2DBList(List<List<WeightLog_AP>> entityList, Node mulObjectSet){

			Element waterInfo = (Element)mulObjectSet;

			NodeList list = GetElementListByTagName(SOAP_TITLE, waterInfo);
			Node object;
			List<WeightLog_AP> onedayList;
			for ( int i = 0; (object=list.item(i)) != null; i++ ){
				onedayList = new ArrayList<WeightLog_AP>();
				NodeSet2DBList(onedayList, object);
				entityList.add(onedayList);
			}
		}
	}
*/