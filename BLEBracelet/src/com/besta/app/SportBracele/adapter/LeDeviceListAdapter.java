package com.besta.app.SportBracele.adapter;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.besta.app.SportBracele.R;


public class LeDeviceListAdapter extends BaseAdapter {

	private final ArrayList<BluetoothDevice> mLeDevices;
	private final Context mcontext;
	private LayoutInflater mInflator;

	public LeDeviceListAdapter(Context context)
	{
		mcontext = context;
		mLeDevices = new ArrayList<BluetoothDevice>();
	    mInflator = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLeDevices.size();
	}
	
	public void clear() {
        mLeDevices.clear();
    }

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mLeDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//mInflator.inflate(R.layout.listitem_device, parent, false);
		
		ViewHolder viewHolder;
        // General ListView optimization code.
        if (convertView == null) {
        	convertView = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        BluetoothDevice device = mLeDevices.get(position);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
        viewHolder.deviceAddress.setText(device.getAddress());

        return convertView;
	}
	
	public void addDevice(BluetoothDevice bluetoothDevice)
	{
	    if (this.mLeDevices.contains(bluetoothDevice))
	      return;
	    this.mLeDevices.add(bluetoothDevice);
	}
	
	public BluetoothDevice getDevice(int position ){
		return mLeDevices.get(position);
	}

	private class ViewHolder{
		TextView deviceAddress;
		TextView deviceName;
	}
}
