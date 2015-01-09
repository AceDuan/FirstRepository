package com.china.acetech.ToolPackage.data;

import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.MySavedState;
import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.SportInfoGreenDaoRepository;
import com.china.acetech.ToolPackage.java.CalendarToolForSync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *  已经生成好的db文件从asset中复制到本地数据库目录下的过程
 *  <br/>
 *  为了加入到greenDao的数据库管理中，需要在复制完毕后使用本地的数据库方法
 *  读取信息再转存到greenDao框架的表中
 */
public class CustomDatabaseBuilder {

	public static void BuildDBIfNeed(){
		if ( MySavedState.SaveFlagOrInfo.isBaseDatabaseBuilt() )
			return;

		new Thread() {
			
			@Override
			public void run() {
				CustomDatabaseBuilder builder = new CustomDatabaseBuilder();
				builder.createFile("sport.db", "fooddata.db");
				
				builder.copyDataToRealDB("fooddata.db");
				
				MySavedState.SaveFlagOrInfo.setBaseDataBuiltFlag(true);
			}
		}.start();
		
	}
	
	private void copyDataToRealDB(String sourceDbName){
		//RepositoryManager.getInstance().getFoodList().clear(false);
		//RepositoryManager.getInstance().getSportList().clear(false);

		SportInfoGreenDaoRepository manager = new SportInfoGreenDaoRepository();
		manager.clear(false);
		
		SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabaseFile(sourceDbName), 
									null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		Cursor cursor = db.query   ("SPORT",null,null,null,null,null,null);
		
		List<SportInfo_AP> sportList = new ArrayList<SportInfo_AP>();
		long dbID = 0;
		if (cursor.moveToFirst()) {
			while ( cursor.moveToNext() ) {
				String sportName = cursor.getString(0);
				long calories = 1000 * cursor.getInt(1);

				sportList.add(new SportInfo_AP()); //这里只是简易的new，正式使用的时候需要另行改动
				//sportList.add(new SportInfo_AP(dbID, sportName, calories, false, 0, CalendarToolForSync.getZeroTime()));
				dbID++;
			}
		}
		cursor.close();
		//RepositoryManager.getInstance().getSportList().addAll(sportList);
		manager.addAll(sportList);
		
//		cursor = db.query("FOOD",null,null,null,null,null,null);
//		List<FoodListLog_AP> foodList = new ArrayList<FoodListLog_AP>();
//		dbID = 0;
//		if ( cursor.moveToFirst()) {
//			while ( cursor.moveToNext() ){
//				String foodName = cursor.getString(0);
//				long calories = 1000 * cursor.getInt(1);
//
//				foodList.add(new FoodListLog_AP(dbID, foodName, calories, false, 0, SBApplication.getZeroTime()));
//				dbID++;
//			}
//		}
//		cursor.close();
//		RepositoryManager.getInstance().getFoodList().addAll(foodList);

		db.close();
	}
	private void createFile(String dbInAssets, String desName){
		
		File file = new File(getDatabaseFile(desName));
		if(!file.exists()){
			file = new File(getDatabaseFilepath());
			if(!file.exists() && !file.mkdirs()){
				return ;
			}
		}
		copyAssetsToFilesystem(dbInAssets, desName);
	}
	
	private static final String databasepath = MyApplication.getTopApp().getFilesDir() + "data/%s/database"; // %s is packageName
	
	private String getDatabaseFilepath(){
		return String.format(databasepath, MyApplication.getTopApp().getApplicationInfo().packageName);
	}
	
	private String getDatabaseFile(String dbfile){
		return getDatabaseFilepath()+"/"+dbfile;
	}
	
	private boolean copyAssetsToFilesystem(String dbInAssets, String desName){
		InputStream istream = null;
		OutputStream ostream = null;
		try{
			AssetManager am = MyApplication.getTopApp().getAssets();
			istream = am.open(dbInAssets);
						
			ostream = new FileOutputStream(getDatabaseFile(desName));
			byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = istream.read(buffer))>0){
	    		ostream.write(buffer, 0, length);
	    	}
	    	istream.close();
	    	ostream.close();
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				if(istream!=null)
			    	istream.close();
				if(ostream!=null)
			    	ostream.close();
			}
			catch(Exception ee){
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}
}
