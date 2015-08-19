package com.china.acetech.ToolPackage.customwidget.tool;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;

public class PropertyRegisterable {

	public static final String ACTIVITY_ACTIVE = "ActivityActive";
	public static final boolean PAUSE = false;
	public static final boolean ACTIVE = true;
	
	public static final String ACTIVITY_REFRESH = "ActivityRefresh";
	public static final String REFRESH_USERINFO = "re_UserInfo";
	public static final String REFRESH_SLEEPSTATISTIC = "re_SleepStatistic";
	public static final String REFRESH_MAINLIST = "re_MainList";
	
	public static final String BLE_SYNC_OVER = "ble_sync_over";
	public static final String BLE_START_SYNC = "ble_start_sync";
	public static final String START_SYNC_ONLY_SET = "start_sync_only_set";
	public static final String START_SYNC_ALL = "start_sync_all";
	
	public static final String BLE_DISCONNECT_WITH_WRONG_DE = "ble_disconnect_wrong_device";
	
	public static final String BLE_DISCONNECTED = "ble_disconnected";
	public static final String BLE_CONNECTING = "ble_connecting";
	public static final String BLE_CONNECT_STATE = "ble_connect_state";
	
	
	
	public static final String BLE_CLOSED_CONNECT = "ble_colsed_connect";
	
	//這幾項消息出現在設備連接過程中
	public static final String BLE_CONNECTION = "ble_connection";
	public static final String BLE_CONNECTION_START_SYNC = "ble_connection_start_sync";
	
	public static final String BLE_FACTORY_INFO_REFRESH = "ble_factory_info_refresh";


	public static final String FAVORITE_FRAGMENT = "FavoriteView";
	public static final String FAVORITE_CUSTOMCOURSE = "CustomCourse";

	public static final String UPDATE_BRACELET_DOWNLOAD_SUCCESS = "UpdateBraceletDataDownloadSuccess";


	private PropertyChangeSupport support;
	private HashMap<PropertyChangeListener, Integer> listenerList;
	
	public PropertyRegisterable(){
		support = new PropertyChangeSupport(this);
		listenerList = new HashMap<PropertyChangeListener, Integer>();
	}
	
	public void addListener(String propertyName, PropertyChangeListener listener){
		if ( support == null ) return;
		
		support.addPropertyChangeListener(propertyName, listener);
		
		listenerList.put(listener, 1);
	}
	
	public void addListener(PropertyChangeListener listener){
		if ( support == null )return;
		
		support.addPropertyChangeListener(listener);
		
		listenerList.put(listener, 1);
	}
	
	public void removeListener(String propertyName, PropertyChangeListener listener){
		if ( support == null ) return;
		
		support.removePropertyChangeListener(propertyName, listener);
		
		listenerList.remove(listener);
	}
	
	public void removeListener(PropertyChangeListener listener){
		if ( support == null ) return;
		
		support.removePropertyChangeListener(listener);
		
		listenerList.remove(listener);
	}
	
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue){
		if ( support != null )
			support.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue){
		if ( support != null )
			support.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public void firePropertyChange(String propertyName, int oldValue, int newValue){
		if ( support != null )
			support.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	
	public void removeAllListener(){
		Iterator<PropertyChangeListener> iterator = listenerList.keySet().iterator();
		while ( iterator.hasNext() ){
			support.removePropertyChangeListener(iterator.next());
		}
		listenerList.clear();
		listenerList = null;
		support = null;
		
	}

}
