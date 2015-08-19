package com.china.acetech.ToolPackage.web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseIntArray;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.web.customsoap.SoapXmlParser;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Element;

/**
 * 所有同步數據對象的抽象父類。目的是封裝同步路徑的具體分支操作過程。
 * 根據抓取到的同步標記確定上傳還是下載。所有的具體動作由具體子類負責。
 * </br>
 * 最新的做法是所有的條件路徑判斷都由子類決定及處理。這裡只針對連接失敗的情況做統一跳過處理。
 * 如果子類需要自行處理錯誤情況，需要將{@link AbsInfoSync#isProcessError}設為true。
 * </br>
 * 同一包下的sync子類，如果沒有置{@link AbsInfoSync#isProcessError}為true。將默認不對失敗情況進行處理。
 * 如果有再次重載的子類，需要自行處理相關失敗情況后再交至此包內的sync子類
 * @author bxc2010011
 *
 */
public abstract class AbsInfoSync {

//	protected static final int GET_SYNC_FLAG = 1;
//	protected static final int UPDATE_DATA_TO_S = 2;
//	protected static final int DOWNLOAD_DATA_FROM_S = 3;

	protected boolean isProcessError = false;

	protected AbsInfoSync(){
		mConnected = new ServiceConnectMethod_HttpPost();
		mConnected.setConnectListener(new ConnectedListener());
		syncFlag = false;
		mThreadMap = new SparseIntArray();
	}

	protected ServiceConnectMethod_HttpPost mConnected;
	boolean syncFlag;
	protected SparseIntArray mThreadMap;



	/**
	 * 提供給子Sync接口用於更新數據的操作。</br>注意！為了保證更新動作在主Thread里進行，
	 * 子Sync在刷新View的時候必須要調用{@link AbsInfoSync#RefreshShowingInMainThread}
	 */
	protected abstract void refreshShowing(Object object);
//	protected abstract void updateData(Object object);
//	protected abstract void downLoadData(Object object);
//	protected abstract int getSyncType(Object object);

	protected abstract void connectedForChild(int threadID, Object object);

	/**
	 * 子Sync需要調用此接口來更新數據。以保證其在主線程中
	 */
	protected void RefreshShowingInMainThread(final Object object){
//		if (MyApplication.getMainActivity() != null ){
//			MyApplication.getMainActivity().runOnUiThread(new Runnable() {
//				@Override
//				public void run(){
//
//					refreshShowing(object);
//				}
//			});
//		}
		MyApplication.getTopApp().getHandler().post(new Runnable() {
			@Override
			public void run(){

				refreshShowing(object);
			}
		});
	}

	protected static final int UPDATE = 1;
	protected static final int DOWNLOAD = 2;
	protected static final int EQUAL = 3;

//	private void judgeAndSendOrder(Object object){
//
//		int order = getSyncType(object);
//		switch ( order ){
//		case EQUAL:
//			syncFlag = false;
//			break;
//		case UPDATE:
//			updateData(object);
//			break;
//		case DOWNLOAD:
//			downLoadData(object);
//			break;
//		default:
//			break;
//		}
//	}

	/**
	 * 需要根據線程ID來確定當前運行到哪個階段，現在的做法是同步進程只能有一個。
	 */
	class ConnectedListener implements ServiceConnectMethod_HttpPost.ConnectListener{

		@Override
		public void onConnected(int threadID, Object object) {

			switch (mThreadMap.get(threadID)){
//			case GET_SYNC_FLAG:
//				judgeAndSendOrder(object);
//				break;
//			case UPDATE_DATA_TO_S:
//				break;
//			case DOWNLOAD_DATA_FROM_S:
//				break;
				default:
					if ( object instanceof SoapObject ){
						SoapObject soap = (SoapObject)object;
//					String message;
//					message = soap.getPropertySafelyAsString(DataTranslate.RETURN_VALUE);
//
//					if ( !message.equals(ServiceConnectMethod.Connect_Error_Message) ){
//						message = soap.getPropertySafelyAsString(DataTranslate.SERVICE_RESULT);
//						if ( message.equals("1") )
//							message = ServiceConnectMethod.Connect_Error_Message;
//						else if ( soap.getPropertyCount() >= 1 ){
//							Object o = soap.getProperty(0);
//							if ( o instanceof SoapObject ){
//								message = ((SoapObject)o).getPropertySafelyAsString(DataTranslate.SERVICE_RESULT);
//
//								if ( message.equals("1") )
//									message = ServiceConnectMethod.Connect_Error_Message;
//							}
//						}
//					}
//
//					if ( message.equals(ServiceConnectMethod.Connect_Error_Message) && !isProcessError ){
						if ( isConnectError(soap) && !isProcessError ){
							mThreadMap.delete(threadID);
							return;
						}

//						if ( !(AbsInfoSync.this instanceof BestaSSO_Verify) && isSSOError(soap) ){
//							new BestaSSO_Verify().syncDataInfo();
//						}
					}
					connectedForChild(threadID, object);
					break;
			}

			mThreadMap.delete(threadID);
		}
	}

	public static boolean isSSOError(SoapObject soap){
		boolean ret = false;
		String message;
		message = soap.getPropertySafelyAsString(DataTranslate.RETURN_VALUE);
		if ( !message.equals(ServiceConnectMethod.Connect_Error_Message) ){
			if ( message.length() != 0 ){
				Element element;// = new SoapXmlParser().ParserForRoot(message);
				String result;// = SoapXmlParser.getDeepElementsValueByTagName("/SYConst/Row", element);

				element = new SoapXmlParser().ParserForRoot(message);
				result = SoapXmlParser.getDeepElementsValueByTagName("/SYConst/Row", element);

				if ( result.length() != 0 && result.contains("RC150") )
					ret = true;
				else if ( result.length() != 0 && result.contains("RC148") )
					ret = true;

			}

		}
		return ret;
	}

	public static boolean isConnectError(SoapObject soap){
		String message;
		message = soap.getPropertySafelyAsString(DataTranslate.RETURN_VALUE);

		if ( !message.equals(ServiceConnectMethod.Connect_Error_Message) ){
			message = soap.getPropertySafelyAsString(DataTranslate.SERVICE_RESULT);
			if ( message.equals("1") )
				message = ServiceConnectMethod.Connect_Error_Message;
			else if ( soap.getPropertyCount() >= 1 ){
				Object o = soap.getProperty(0);
				if ( o instanceof SoapObject ){
					message = ((SoapObject)o).getPropertySafelyAsString(DataTranslate.SERVICE_RESULT);

					if ( message.equals("1") )
						message = ServiceConnectMethod.Connect_Error_Message;
				}
			}
		}

		return message.equals(ServiceConnectMethod.Connect_Error_Message);
	}

	public static class SyncHolder{
		public final AbsInfoSync Sync;
		public final Object value;
		public SyncHolder(AbsInfoSync sync, Object object){
			this.Sync = sync;
			this.value = object;
		}
	}
}

