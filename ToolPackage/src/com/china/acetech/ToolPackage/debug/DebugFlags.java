package com.china.acetech.ToolPackage.debug;

		import java.util.HashMap;


/**
 *  Debug信息打印工具
 */
public class DebugFlags {

	public static final boolean OPENDEBUG = false;

	private static final boolean WEB_DEBUG_FLAG = false;
	private static final String WEB_DEBUG_PATH = "com.besta.app.SportBracelet.web";


	private static final boolean BLE_DEBUG_FLAG = true;
	private static final String BLE_DEBUG_PATH = "com.besta.app.SportBracelet.BLE";


	private static final boolean MAIN_DEBUG_FLAG = false;
	private static final String MAIN_DEBUG_PATH = "com.besta.app.SportBracelet.main";

	//funcFrgment
	//data

	private static final boolean MAININT_DEBUG_FLAG = false;
	private static final String MAININT_DEBUG_PATH = "com.besta.app.SportBracelet.mainInterface";


	private static final boolean SET_DEBUG_FLAG = false;
	private static final String SET_DEBUG_PATH = "com.besta.app.SportBracelet.setting";


	private static final boolean STARTUP_DEBUG_FLAG = false;
	private static final String STARTUP_DEBUG_PATH = "com.besta.app.SportBracelet.startup";

	private static final boolean TOOL_DEBUG_FLAG = false;
	private static final String TOOL_DEBUG_PATH = "com.besta.app.SportBracelet.tool";

	private static final boolean UI_DEBUG_FLAG = false;
	private static final String UI_DEBUG_PATH = "com.besta.app.SportBracelet.ui";

	private static final boolean USERINFO_DEBUG_FLAG = false;
	private static final String USERINFO_DEBUG_PATH = "com.besta.app.SportBracelet.userinfo";

	private static final boolean SECONDARY_DEBUG_FLAG = false;
	private static final String SECONDARY_DEBUG_PATH = "com.besta.app.SportBracelet.secondary";

	private static final String sumHead = "com.besta.app.SportBracelet.";


	private static final String[] customDebugPath = {
	};

	private static DebugMap debugablePath;
	static{
		debugablePath = new DebugMap();
		if ( debugablePath.size() == 0  || !OPENDEBUG )
			System.out.println("Deubg is closed!!!!");
	}




	public static boolean isShowing(String classpath){
		classpath = classpath.substring(0, classpath.indexOf('.', sumHead.length()) );
		//Log.i("Deubg Info in", classpath);
		return debugablePath.containsKey(classpath);
	}

	protected static class DebugMap extends HashMap<String, Boolean>{
		private static final long serialVersionUID = 75920235;
		DebugMap(){
			super();
			if ( WEB_DEBUG_FLAG )		this.put(WEB_DEBUG_PATH, WEB_DEBUG_FLAG);

			if ( BLE_DEBUG_FLAG )		this.put(BLE_DEBUG_PATH, BLE_DEBUG_FLAG);

			if ( MAIN_DEBUG_FLAG )		this.put(MAIN_DEBUG_PATH, MAIN_DEBUG_FLAG);

			if ( MAININT_DEBUG_FLAG )	this.put(MAININT_DEBUG_PATH, MAININT_DEBUG_FLAG);

			if ( SET_DEBUG_FLAG )		this.put(SET_DEBUG_PATH, SET_DEBUG_FLAG);

			if ( STARTUP_DEBUG_FLAG )	this.put(STARTUP_DEBUG_PATH, STARTUP_DEBUG_FLAG);

			if ( TOOL_DEBUG_FLAG )		this.put(TOOL_DEBUG_PATH, TOOL_DEBUG_FLAG);

			if ( UI_DEBUG_FLAG )		this.put(UI_DEBUG_PATH, UI_DEBUG_FLAG);

			if ( USERINFO_DEBUG_FLAG )	this.put(USERINFO_DEBUG_PATH, USERINFO_DEBUG_FLAG);

			if ( SECONDARY_DEBUG_FLAG)  this.put(SECONDARY_DEBUG_PATH, SECONDARY_DEBUG_FLAG);

			putCustomDebugPath(this);
		}

		private static void putCustomDebugPath(HashMap<String, Boolean> map){

			for ( String str : customDebugPath ){
				map.put(str, true);
			}
		}
	}

}
