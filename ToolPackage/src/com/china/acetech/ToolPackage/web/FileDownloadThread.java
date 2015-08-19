package com.china.acetech.ToolPackage.web;

import com.china.acetech.ToolPackage.debug.DebugTool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileDownloadThread extends Thread{  
//public class FileDownloadThread implements Callable<Boolean>{
    private static final int BUFFER_SIZE=1024 * 8;  
    protected int connectTimeout = 30 * 1000; // 连接超时:30s
    protected int readTimeout = 1 * 1000 * 1000; // IO超时:1min
    
    private URL url;  
    private File file;  
    private int startPosition;  
    private int endPosition;  
    private int curPosition;  
    //标识当前线程是否下载完成  
    private boolean finished=false;  
    private boolean IsError=true;  
    private int downloadSize=0;  
    public FileDownloadThread(URL url,File file,int startPosition,int endPosition){  
        this.url=url;  
        this.file=file;  
        this.startPosition=startPosition;  
        this.curPosition=startPosition;  
        this.endPosition=endPosition;  
    }  
    @Override  
    public void run() {  
        BufferedInputStream bis = null;  
        RandomAccessFile fos = null;                                                 
        byte[] buf = new byte[BUFFER_SIZE];  
        HttpURLConnection con = null;  
        try {  
            con = (HttpURLConnection)url.openConnection();  
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            con.setAllowUserInteraction(true);  
            //设置当前线程下载的起止点  
            con.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);  
            //Log.i("bb", Thread.currentThread().getName()+"  bytes=" + startPosition + "-" + endPosition);  
            //使用java中的RandomAccessFile 对文件进行随机读写操作  
            fos = new RandomAccessFile(file, "rw");  
            //设置写文件的起始位置  
            fos.seek(startPosition);  
            InputStream in = con.getInputStream();  
            bis = new BufferedInputStream(in);    
            //开始循环以流的形式读写文件  
            this.IsError = true;
            while (curPosition < endPosition) {  
                int len = bis.read(buf, 0, BUFFER_SIZE);                  
                if (len == -1) {  
                    break;  
                }  
                fos.write(buf, 0, len);  
                curPosition = curPosition + len;  
                if (curPosition > endPosition) {  
                    downloadSize+=len - (curPosition - endPosition) + 1;  
                } else {  
                    downloadSize+=len;  
                }  
                sleep(1);
            }  
            this.IsError = true;
            //下载完成设为true  
            this.finished = true;  
            in.close();
            con.disconnect();
            bis.close();  
            fos.close();  
        } catch (Exception e) {  
        	this.IsError = false;
        	con.disconnect();
        	DebugTool.show("Exception");
            //e.printStackTrace();  
        }  
    }  
	
    public boolean isFinished(){  
        return finished;  
    }
    public boolean isNotError(){  
        return IsError;  
    } 
   
    public int getDownloadSize() {  
        return downloadSize;  
    }  
}  
