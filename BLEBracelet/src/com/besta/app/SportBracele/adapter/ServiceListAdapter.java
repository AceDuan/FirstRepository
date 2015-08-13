package com.besta.app.SportBracele.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.besta.app.SportBracele.R;
import com.besta.app.SportBracele.attributes.CharacteristicArrributes;

public class ServiceListAdapter extends BaseExpandableListAdapter{

	private ArrayList<ArrayList<HashMap<String, Object>>> childsArrayList;
	private Context mcontext;
	private ArrayList<HashMap<String, Object>> grounpsArrayList;
	
	public ServiceListAdapter( Context context, ArrayList<HashMap<String, Object>> servicesList, 
			ArrayList<ArrayList<HashMap<String, Object>>> childList){
		mcontext = context;
	    grounpsArrayList = servicesList;
	    childsArrayList = childList;
	}
	
	public void cleanObject()
	{
	    grounpsArrayList.clear();
	    childsArrayList.clear();
	    notifyDataSetChanged();
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return grounpsArrayList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return ((ArrayList<?>)childsArrayList.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return grounpsArrayList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return ((ArrayList<?>)childsArrayList.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if ( convertView == null ){
			convertView = ((LayoutInflater)mcontext.getSystemService(ContextWrapper.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listitem_service, null, false);
			viewHolder = new ViewHolder();
			viewHolder.item_name = (TextView)convertView.findViewById(R.id.service_name);
			viewHolder.item_uuid = (TextView)convertView.findViewById(R.id.service_uuid);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
	    HashMap<?,?> serviceMap = (HashMap<?,?>)this.grounpsArrayList.get(groupPosition);
	    viewHolder.item_name.setText(serviceMap.get("DEVICE_NAME").toString());
	    viewHolder.item_uuid.setText(serviceMap.get("DEVICE_UUID").toString());
	    return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if ( convertView == null ){
			viewHolder = new ViewHolder();
			convertView = ((LayoutInflater)mcontext.getSystemService("layout_inflater")).inflate(R.layout.listitem_child, null, false);
			viewHolder.item_name = (TextView)convertView.findViewById(R.id.child_name);
			viewHolder.item_uuid = (TextView)convertView.findViewById(R.id.child_uuid);
			viewHolder.item_propery = (TextView)convertView.findViewById(R.id.child_properies);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
	    HashMap<?,?> childHashMap = (HashMap<?,?>)((ArrayList<?>)this.childsArrayList.get(groupPosition)).get(childPosition);
	    viewHolder.item_name.setText(childHashMap.get("DEVICE_NAME").toString());
	    viewHolder.item_uuid.setText(childHashMap.get("DEVICE_UUID").toString());
	    viewHolder.item_propery.setText(
	    		CharacteristicArrributes.getCharacteristicProperties( 
	    				childHashMap.get("DEVICE_PROPERTIES").toString() )
	    		);
	    
	    return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	private class ViewHolder{
		TextView item_name;
		TextView item_uuid;
		TextView item_propery;
	}

}
