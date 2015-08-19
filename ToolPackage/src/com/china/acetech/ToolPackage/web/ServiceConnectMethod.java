package com.china.acetech.ToolPackage.web;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Future;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.debug.DebugTool;
import com.china.acetech.ToolPackage.web.customsoap.MySoapObject;
import com.china.acetech.ToolPackage.web.customsoap.SoapXmlMaker;
import com.china.acetech.ToolPackage.web.customsoap.SoapXmlParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Node;
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
public abstract class ServiceConnectMethod {

	public static final String Connect_Error_Message = "Connect error! Please try again.";
	public static final String Connect_Success_Message = "Success";
	public static final String HEALTH_COURSE_URL;
	public static final String BESTA_WEBSITE;
	static {
		HEALTH_COURSE_URL = "http://health.5dkg.com/axis2/services/TestHealthCourse?wsdl";
		BESTA_WEBSITE = "http://api.beta.besta.com.tw/";
	}

	//public static final String URL = "http://61.185.207.11/Besta/services/WristBand?wsdl";
	private static final int REQUEST_TIMEOUT = 5 * 1000;//設置請求超時10秒鐘
	private static final int SO_TIMEOUT = 5 * 1000; // 設置等到數據超時時間10秒鐘



	public static final int NEVER_USE_ID_OF_THREAD = 0;
	private static final int START_USE_ID_OF_THREAD = 100;
	private static final int MAX_USE_ID_OF_THREAD = 10000;


	public int connect_HttpGet(final String method, final String URL, final String resourceName, final String resourceDirectory, final int resourceType) {
		File directory = new File(resourceDirectory);
		if ( !directory.exists() ){
			directory.mkdirs();
		}

		ConnectThread t = new ConnectThread(method) {

			@Override
			public void run() {
				HttpGet request = new HttpGet(URL);
				//request.addHeader("charset", HTTP.UTF_8);

				HttpResponse response = null;
				int timeOutCount = 0;
				while ( true ){
					try {
						timeOutCount++;
						//System.out.println(method + " start");
						HttpClient client = getHttpClient();
						client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10*1000);
						response = client.execute(request);
						//System.out.println(method + " work over");
						break;
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						break;
					} catch (IOException e) {
						if ( e instanceof org.apache.http.conn.ConnectTimeoutException
								&& timeOutCount >= 5){
							//System.out.println(method + " retry :" + timeOutCount);
							continue;
						}else if ( e instanceof java.net.SocketTimeoutException
								&& timeOutCount >= 5 ){
							continue;
						}

						//System.out.println("Error method is :" +method);
						e.printStackTrace();
						break;
					}
				}
				Object result = null;
				if( response != null ){
					try {
						int reponseCode = response.getStatusLine().getStatusCode();
						if(reponseCode == HttpStatus.SC_OK) {
							//如果中間失敗的話該怎麼處理？？？？？？？
							//System.out.println(EntityUtils.toString(response.getEntity()));
							InputStream in = response.getEntity().getContent();
							File file = new File(resourceDirectory + resourceName);
							if ( !file.exists() ){
								file.createNewFile();
							}
							OutputStream out = new FileOutputStream(file);

							BufferedInputStream buIn = new BufferedInputStream(in);
							BufferedOutputStream buOut = new BufferedOutputStream(out);

							byte[] data = new byte[512];
							int readLeanth = 0;
							while ((readLeanth = buIn.read(data, 0, 512)) != -1) {
								buOut.write(data, 0, readLeanth);

							}

							//将缓冲区中的数据全部写出
							buOut.flush();
							//关闭流

							buIn.close();

							buOut.close();

							result = Connect_Success_Message;
						}
						else{
							result = null;
						}

					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				if ( result == null ){
					result = Connect_Error_Message;
				}
				handleMessage(getThreadID(), result);
				//if ( mHandler != null )
				//	mHandler.sendMessage(Message.obtain(mHandler, getThreadID(), result));
			}
		};

		//t.start();
		if ( resourceType == DownloadResourceSync.VALUE_MOV)
			MyApplication.getVideoThreadPool().execute(t);
		else
			MyApplication.getPictureThreadPool().execute(t);

		return t.getThreadID();

	}
	public Future<?> videotest;
	public Future<?> picturetest;


	//採用Callable接口可以得到訪問的返回值並且可以管理線程的運行狀態。考慮一下還有那些地方需要改成這種機制
	public int connect_HttpMulGet(final String method, final String useURL, final String resourceName, final String resourceDirectory, final int resourceType
			, DownloadResourceSync.Package pack) {
		File directory = new File(resourceDirectory);
		if ( !directory.exists() ){
			directory.mkdirs();
		}

		ConnectThread t = new PurchaseAndDownload.downloadTask(useURL, 6, resourceDirectory+resourceName) {

			@Override
			public void run() {
				//super.run();
				//這裡根據父類的success或fail來進行下一步動作
				boolean ret = downloadFile();
				String result;
				if ( ret == true )
					result = Connect_Success_Message;
				else
					result = Connect_Error_Message;

				handleMessage(getThreadID(), result);
			}

//			@Override
//			public Boolean call() throws Exception {
//				boolean ret = downloadFile();
//				String result;
//				if ( ret == true )
//					result = Connect_Success_Message;
//				else
//					result = Connect_Error_Message;
//				handleMessage(getThreadID(), result);
//				return ret;
//			}
		};

		if ( resourceType == DownloadResourceSync.VALUE_MOV)
			pack.res = MyApplication.getVideoThreadPool().submit(t);
			//MyApplication.getVideoThreadPool().execute(t);
		else
			MyApplication.getPictureThreadPool().execute(t);

		return t.getThreadID();

	}

	protected int connect_HttpPost(final String method, final List<MySoapObject> list) {
		ConnectThread t = new ConnectThread(method) {

			@Override
			public void run() {

				HttpPost request = new HttpPost(HEALTH_COURSE_URL);
				request.addHeader("charset", HTTP.UTF_8);
				try {
					String xml = new SoapXmlMaker().createXML(method, list);
					//System.out.println(xml);
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
						//System.out.println(method + " start");
						HttpClient client = getHttpClient();
						client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10*1000);
						response = client.execute(request);
						//System.out.println(method + " work over");
						break;
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						break;
					} catch (IOException e) {
						if ( e instanceof org.apache.http.conn.ConnectTimeoutException
								&& timeOutCount != 5){
							//System.out.println(method + " retry :" + timeOutCount);
							continue;
						}
						//System.out.println("Error method is :" +method);
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
					result = Connect_Error_Message;
				}
				else if ( result instanceof Node){

					String message = ((Node)result).getFirstChild().getNodeValue();
					if ( message != null )
						result = message;
				}
				else{
					result = Connect_Error_Message;
				}

				handleMessage(getThreadID(), result);
				//if ( mHandler != null )
				//	mHandler.sendMessage(Message.obtain(mHandler, getThreadID(), result));
			}
		};

		//t.start();
		MyApplication.getThreadPool().execute(t);

		return t.getThreadID();

	}

	protected int connect_Soap(final String method, final List<PropertyInfo> list){
		ConnectThread t = new ConnectThread(method){

			@Override
			public void run() {
				//final String SOAP_ACTION = "AtlasAccount/AMD_Keyword_Report";
				//final String METHOD_NAME = "Registration";
				//final String METHOD_NAME = "AccountLogin";
				final String METHOD_NAME = method;
				final String NAMESPACE = "http://output.web.besta.com";;
				//final String URL = "http://10.180.3.248/Besta/services/WristBand?wsdl";
				//final String URL = "http://www.5dkg.com/Besta/services/HealthCourse?wsdl";
				//final String URL = "http://10.180.3.132:8080/axis2/services/MyService?wsdl";



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
				DebugTool.show(request.toString());
				HttpTransportSE androidHttpTransport = new HttpTransportSE(HEALTH_COURSE_URL);
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
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, Connect_Error_Message);
				}
				if ( test instanceof SoapPrimitive){
					//考慮到走到這裡的時候基本都是出了異常，所以後續處理都按異常對待，有bug再詳細查看問題原因
					DebugTool.show(test.toString());
					//resultSoap.addProperty(DataTranslate.RETURN_VALUE, test.toString());
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, Connect_Error_Message);
				}
				else if ( test instanceof SoapObject ){
					resultSoap = (SoapObject)test;
				}
				else{
					resultSoap.addProperty(DataTranslate.RETURN_VALUE, Connect_Error_Message);
				}
				DebugTool.show(method + "\t" +resultSoap.toString());
				handleMessage(getThreadID(), resultSoap);
			}

		};

		MyApplication.getThreadPool().execute(t);

		return t.getThreadID();
	}

	public int connect_Empty(){
		ConnectThread t = new ConnectThread("EmptyThread"){
			@Override
			public void run() {
				try {
					sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handleMessage(getThreadID(), null);
			}
		};
		//這裡需要修改，需要使用相應資源的線程池
		MyApplication.getThreadPool().execute(t);
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

	public abstract static class ConnectThread extends Thread{
		private static int count = START_USE_ID_OF_THREAD;
		private int threadID;

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
		 * @param threadID thread's ID
		 * @param message thread's result
		 */
		void onConnected(int threadID, Object message);
	}
}
