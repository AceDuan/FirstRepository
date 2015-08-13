package com.besta.app.SportBracele.tool;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class Debug {
	public static int show(String str){
		//Class<?> c = Thread.currentThread().getStackTrace()[3].getClass();
//		String c = Thread.currentThread().getStackTrace()[3].getClassName();
//		c = c.substring(c.lastIndexOf(".")+1, c.length());
		String c = "1";
		//Class<?> c = re.;
		
		//return DebugInfo(c, str);
		return DebugInfo(c, str);
	}
	
//	private static int DebugInfo(Class<?> c, String str){
//		if ( !DebugFlags.OPENDEBUG )
//			return 0;
//		return Log.i(c.getSimpleName(), str);
//		
//	}
	
	private static int DebugInfo(String c, String str){
		if ( !DebugFlags.OPENDEBUG )
			return 0;
		return Log.i(c, str);
		
	}
	
	
	public static void toastShow(Activity context, String show){
		if ( !DebugFlags.OPENDEBUG)
			return;
		
		Toast.makeText(context, show, Toast.LENGTH_SHORT).show();
		
	}
}
