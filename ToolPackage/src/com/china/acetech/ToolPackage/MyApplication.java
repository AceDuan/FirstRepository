package com.china.acetech.ToolPackage;

import android.app.Application;
import android.os.Handler;
import com.china.acetech.ToolPackage.ble.services.BLEServiceManagerWorker;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

	private static MyApplication mApplication;

	public MyApplication() {
		mApplication = this;
		// com.fitbit.serverinteraction.k.a(this);

		mRegister = new PropertyRegisterable();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mHandler = new Handler();

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

	private Handler mHandler;

	/**
	 * 保证handler是在ui线程创建，方便其他代码载入UI线程
	 * @return handler in main thread
	 */
	public Handler getHandler(){
		return mHandler;
	}

	//创建了多个线程池

	private static ExecutorService mThreadPool;
	public static ExecutorService getThreadPool(){
		if ( mThreadPool == null ){
			mThreadPool = Executors.newFixedThreadPool(3);
		}

		return mThreadPool;
	}

	public static void closeThreadPool(){
		if ( mThreadPool != null ){
			mThreadPool.shutdownNow();
			mThreadPool = null;
		}
	}

	//鑒於圖片下載的數量很多並且有需要終止下載過程。專門建立一個用於圖片下載的線程池
	private static ExecutorService mPictureThreadPool;
	public static ExecutorService getPictureThreadPool(){
		if ( mPictureThreadPool == null ){
			mPictureThreadPool = Executors.newFixedThreadPool(10);
		}

		return mPictureThreadPool;
	}

	public static void closePictureThreadPool(){
		if ( mPictureThreadPool != null ){
			mPictureThreadPool.shutdown();
			mPictureThreadPool = null;
		}
	}

	private static ExecutorService mVideoThreadPool;
	public static ExecutorService getVideoThreadPool(){
		if ( mVideoThreadPool == null ){
			mVideoThreadPool = Executors.newSingleThreadExecutor();
		}

		return mVideoThreadPool;
	}

	public static void closeVideoThreadPool(){
		if ( mVideoThreadPool != null ){
			mVideoThreadPool.shutdown();
			mVideoThreadPool = null;
		}
	}

	private static ExecutorService mVideoMulThreadPool;
	public static ExecutorService getVideoMulThreadPool(){
		if ( mVideoMulThreadPool == null ){
			mVideoMulThreadPool = Executors.newFixedThreadPool(12);
		}

		return mVideoMulThreadPool;
	}

	public static void closeVideoMulThreadPool(){
		if ( mVideoMulThreadPool != null ){
			mVideoMulThreadPool.shutdownNow();
			mVideoMulThreadPool = null;
		}
	}
}
