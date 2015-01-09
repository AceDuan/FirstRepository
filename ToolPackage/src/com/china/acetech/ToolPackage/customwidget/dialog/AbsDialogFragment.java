package com.china.acetech.ToolPackage.customwidget.dialog;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.china.acetech.ToolPackage.R;


/**
 * 具有选择性质的对话框。右端有check box标志当前选中项
 */
public abstract class AbsDialogFragment extends DialogFragment{

	public static final String TAG = AbsDialogFragment.class.getSimpleName();

	public abstract int getSelection();
	public abstract void ItemClick(AdapterView<?> parent, View view,
			int position, long id);
	public abstract String[] getIDs();
	
	public AbsDialogFragment(){
		this.setStyle(STYLE_NO_TITLE, 0);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_setting, container, false);

		ListView list = (ListView)v.findViewById(R.id.setting_list);
		final SettingItemAdapter adapter = new SettingItemAdapter(getIDs());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ItemClick(parent, view, position, id);
			}
		});

		currentPosition = getSelection();
		list.setSelection(currentPosition);
		return v;
	}
	
	int currentPosition = -1;
	
	private class SettingItemAdapter extends BaseAdapter{

		SettingItemAdapter(String[] idList){
			itemIDs = idList;
		}
		String[] itemIDs;
		@Override
		public int getCount() {
			return itemIDs.length;
		}

		@Override
		public Object getItem(int position) {
			return itemIDs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListItemHolder holder;
			if ( convertView == null ){
				LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(ContextWrapper.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.dialog_setting_item, null, false);
				holder = new ListItemHolder();
				holder.itemName = (TextView)convertView.findViewById(R.id.setting_item_hint);
				holder.button = (RadioButton)convertView.findViewById(R.id.setting_item_radio);
				convertView.setTag(holder);
				
			}
			else{
				holder = (ListItemHolder)convertView.getTag();
			}
			
			holder.itemName.setText(itemIDs[position]);
			holder.button.setFocusable(false);
			holder.button.setEnabled(false);
			holder.button.setChecked(false);
			if ( position == currentPosition ){
				holder.button.toggle();
			}
			
			return convertView;
		}
		
	}
	
	public static class ListItemHolder{
		TextView itemName;
		RadioButton button;
	}
}
