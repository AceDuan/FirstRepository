package com.china.acetech.ToolPackage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySavedState
{
	static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getTopApp());
  //private static final String a = "SavedState";

	public static class UserLoginInfo{
		private static final String USER_LOGIN_NAME = "SavedState.UserInfoState.LoginName";
		private static final String IS_USER_LOGED_IN = "SavedState.UserInfoState.LoginState";
		
		
		public static String getLoginName(){
			return preferences.getString(USER_LOGIN_NAME, null);
		}
		public static void setLoginName(String username){
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(USER_LOGIN_NAME, username);
			editor.commit();
		}
		
		public static boolean isAlreadyLogin(){
			return preferences.getBoolean(IS_USER_LOGED_IN, false);
		}
		public static void setLoginState(boolean isLogin){
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(IS_USER_LOGED_IN, isLogin);
			editor.commit();
		}
		
		
	}
	
	public static class SaveFlagOrInfo{

		private static final String BASE_DATABASE = "SavedState.UserInfoState.BaseDatabase";

		public static boolean isBaseDatabaseBuilt(){
			return preferences.getBoolean(BASE_DATABASE, false);
		}

		public static void setBaseDataBuiltFlag(boolean builtFlag){
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(BASE_DATABASE, builtFlag);
			editor.commit();
		}

	}


	public static void clear(){
		preferences.edit().clear().commit();
	}
}