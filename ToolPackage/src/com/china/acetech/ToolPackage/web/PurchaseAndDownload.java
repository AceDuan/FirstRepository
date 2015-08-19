package com.china.acetech.ToolPackage.web;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.debug.DebugTool;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;


/**
 * 和{@link FileDownloadThread} 一起组成多线程下载的组件，不是十分好用
 */
public class PurchaseAndDownload
{
	static int FileSum = 0;
	static int SuccessFile = 0;
	static int FailureFile = 0;
	protected static int connectTimeout = 30 * 1000; // 连接超时:30s
	protected static int readTimeout = 1 * 1000 * 1000; // IO超时:1min

	public static abstract class downloadTask extends ServiceConnectMethod.ConnectThread {
		private int blockSize;  
		private int threadNum = 1;  
		private String urlStr, fileName;  
		
		private boolean isNeedStop = false;

		public downloadTask(String urlStr, int threadNum, String fileName) {
			super("123");
			this.urlStr = urlStr;
			this.threadNum = threadNum;  
			this.fileName = fileName;
		}  
		
		protected boolean downloadFile(){
			SuccessFile = 0;
			FailureFile = 0;
			
			Future<?>[] res = new Future[threadNum];
			for ( int i = 0 ; i < res.length; i++) res[i] = null;
			
			FileDownloadThread[] fds = new FileDownloadThread[threadNum];  
			try {  
				URL url = new URL(urlStr);  
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
				conn.setRequestMethod("GET");
				//防止返回-1  

				InputStream in = conn.getInputStream();  
				//获取下载文件的总大小  

				int fileSize = 0;
				fileSize = conn.getContentLength();  
				in.close();
				conn.disconnect();
				
				DebugTool.show("======================fileSize:" + fileSize);
				//计算每个线程要下载的数据量  
				blockSize = fileSize / threadNum;  
				//Log.i("bb", "======================blockSize:"+blockSize);  
				// 解决整除后百分比计算误差  
				//downloadSizeMore = (fileSize % threadNum);  
				//Log.i("bb", "======================downloadSizeMore:"+downloadSizeMore);  
				File file = new File(fileName);  
				for (int i = 0; i < threadNum; i++) {
					//启动线程，分别下载自己需要下载的部分  
					int endSize = 0;
					if ( i == threadNum-1 )
						endSize = fileSize;
					else
						endSize = (i + 1) * blockSize - 1;
					FileDownloadThread fdt = new FileDownloadThread(url, file, i * blockSize, endSize);  
					//fdt.setName("VideoMulThread" + i);  
					//fdt.start();
					//MyApplication.getVideoMulThreadPool().execute(fdt);
					res[i] = MyApplication.getVideoMulThreadPool().submit(fdt);
					fds[i] = fdt;  
				}  
				boolean finished = false;  
				boolean errored = false;
				
				while (!finished) {  
					// 先把整除的余数搞定  
					finished = true;  
					for (int i = 0; i < fds.length; i++) {  
						if (!fds[i].isFinished() && fds[i].isNotError()) {  
							finished = false;  
						}  
						if ( !fds[i].isNotError() ){
							finished = true;
							errored = true;
							break;
						}
					}  
					//线程暂停一秒  
					sleep(200);  
					//這裡要增加判斷，如果外部要求線程終結，此處將結束調用
					if( isNeedStop ){
						finished = false;
						break;
					}
				}  
				if ( finished && !errored) {  
					SuccessFile++;
				}
				else
				{
					FailureFile++;
				}

			} catch (Exception e) {  
				e.printStackTrace();
				DebugTool.show("Exception");
				FailureFile++;
			}  
			
			if ( FailureFile != 0 ){
				DebugTool.show("Cancel thread num:" + res.length);
				for ( int i = 0 ; i < res.length; i++ ){
					if ( res[i] != null )
						res[i].cancel(true);
				}
				return false;
			}
			else
				return true;
		}
		
		

		public void interruptSafly(){
			isNeedStop = true;
		}
	}  
}


