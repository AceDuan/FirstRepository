package com.china.acetech.ToolPackage.customwidget.dialog;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.java.UsedPairEntity;


/**與其他dialog不同，clock dialog顯示的是圖片，暫時進行特殊化處理，直接換掉adapter
 * @author bxc2010011
 *
 */
public class ClockTypeSettingDialog extends AbsDialogFragment{
	public static final String TAG = ClockTypeSettingDialog.class.getSimpleName();

	public ClockTypeSettingDialog(){
		super();
		mMap = new ClockTypeHolder().getPairMap();
	}
	
	UsedPairEntity mMap;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v =  super.onCreateView(inflater, container, savedInstanceState);
		ListView list = (ListView)v.findViewById(R.id.setting_list);
		list.setAdapter(new ClockAdapter());
		return v;
	}

	@Override
	public int getSelection() {
		int position = 0;
		
//		DeviceInfo_AP entity = RepositoryManager.getInstance().getDeviceInfo().getEntity();
//		String clocktype = String.valueOf( entity.getClockDisplay() );
		
		position = mMap.getValuePosition("");
		
		return position;
	}

	@Override
	public void ItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		DeviceInfo_AP entity = RepositoryManager.getInstance().getDeviceInfo().getEntity();
//		String clocktype = mMap.getValue(position);
//		if ( entity.getClockDisplay() != Integer.valueOf(clocktype)){
//			entity.setClockDisplay(Integer.valueOf(clocktype));
//			entity.setLastsynctime(SBApplication.getSyncTime());
//			RepositoryManager.getInstance().getDeviceInfo().save(entity);
//		}
//		((SettingChildActivity)getActivity()).refreshSettingInfo();

		ClockTypeSettingDialog.this.dismiss();
	}

	@Override
	public String[] getIDs() {
		return mMap.getKeyArray();
		//return null;
	}
	
	class ClockAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return getIDs().length;
		}

		@Override
		public Object getItem(int position) {
			return getIDs()[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ClockItemHolder holder;
			if ( convertView == null ){
				LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(ContextWrapper.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.dialog_clock_setting_item, parent, false);
				holder = new ClockItemHolder();
				holder.clockImage = (ImageView)convertView.findViewById(R.id.setting_item_hint);
				holder.button = (RadioButton)convertView.findViewById(R.id.setting_item_radio);
				convertView.setTag(holder);
				
			}
			else{
				holder = (ClockItemHolder)convertView.getTag();
			}
			
			holder.clockImage.setImageResource(Integer.valueOf(getIDs()[position]));
			holder.button.setFocusable(false);
			holder.button.setEnabled(false);
			holder.button.setChecked(false);
			if ( position == currentPosition ){
				holder.button.toggle();
			}
			
			return convertView;
		}

	}
	
	static class ClockItemHolder{
		ImageView clockImage;
		RadioButton button;
	}

	public static DialogBuilder getDialogBuilder() {
		return new DialogBuilder();
	}

	public static class DialogBuilder {
		private DialogBuilder() {
		}

		public ClockTypeSettingDialog create() {
			return new ClockTypeSettingDialog();
		}

	}


	public static class ClockTypeHolder {

		public UsedPairEntity getPairMap() {
			UsedPairEntity pairMap = new UsedPairEntity();

			for (int i = 0; i < imageID.length; i++) {
				pairMap.put(imageID[i], databaseSaving[i]);
			}
			return pairMap;
		}

//		private String[] stringID = {
//				getResources().getString(R.string.setting_device_clock_type_1),
//				getResources().getString(R.string.setting_device_clock_type_2),
//		};

		private String[] imageID = {
				String.valueOf("R.drawable.clock_type_1"),
				String.valueOf("R.drawable.clock_type_2"),
		};

		private String[] databaseSaving = { "0", "1" };
	}
}
