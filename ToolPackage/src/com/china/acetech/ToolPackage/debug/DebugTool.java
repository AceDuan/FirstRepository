package com.china.acetech.ToolPackage.debug;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class DebugTool {
	public static int show(String str){
		if ( !DebugFlags.OPENDEBUG )
			return 0;
		//Class<?> c = Thread.currentThread().getStackTrace()[3].getClass();
		String c = Thread.currentThread().getStackTrace()[3].getClassName(); //完整的包結構
		
		if ( !DebugFlags.isShowing(c))
			return -1;
		
		c = c.substring(c.lastIndexOf(".")+1, c.length());	//僅有當前類的名稱
		
		return DebugInfo(c, str);
	}
	
	public static boolean isDebug(){
		return DebugFlags.OPENDEBUG;
	}
	
//	private static int DebugInfo(Class<?> c, String str){
//		if ( !DebugFlags.OPENDEBUG )
//			return 0;
//		return Log.i(c.getSimpleName(), str);
//		
//	}
	
	private static int DebugInfo(String c, String str){
		return Log.i(c, str);

		
	}
	
	
	public static void toastShow(Activity context, String show){
		if ( !DebugFlags.OPENDEBUG)
			return;
		
		Toast.makeText(context, show, Toast.LENGTH_SHORT).show();
		
	}
}
