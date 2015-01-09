package com.china.acetech.ToolPackage;

import android.app.Application;
import com.china.acetech.ToolPackage.ble.services.BLEServiceManagerWorker;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;

public class MyApplication extends Application {

	private static MyApplication mApplication;

	public MyApplication() {
		mApplication = this;
		// com.fitbit.serverinteraction.k.a(this);
	}

	public static MyApplication getTopApp() {
		return mApplication;
	}



	private static BLEServiceManagerWorker BLEWorker;
	public static void setBLEManagerWorker(BLEServiceManagerWorker worker){
		BLEWorker = worker;
	}
	public static BLEServiceManagerWorker getBLEManagerWorker(){
		return BLEWorker;
	}

	private static PropertyRegisterable mRegister;
	public static void setRegisterable(PropertyRegisterable worker){
		mRegister = worker;
	}
	public static PropertyRegisterable getRegisterable(){
		//由於register able的唯一性，這裡可以考慮採用單例模式。
		return mRegister;
	}
}
