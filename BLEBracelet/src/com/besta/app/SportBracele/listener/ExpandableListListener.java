package com.besta.app.SportBracele.listener;

import android.view.View;
import android.widget.ExpandableListView;


public class ExpandableListListener implements ExpandableListView.OnChildClickListener,
	ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener{

	@Override
	public void onGroupExpand(int groupPosition) {
		// TODO Auto-generated method stub
//		runOnUiThread(new Runnable(t)
//	    {
//	      public void run()
//	      {
//	        for (int i = 0; ; ++i)
//	        {
//	          if (i >= this.val$count)
//	            return;
//	          if (this.val$position == i)
//	            continue;
//	          ServiceActivity.this.mExpandableListView.collapseGroup(i);
//	        }
//	      }
//	    });
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub
//		this.linearlayout_Group.setVisibility(8);
	    return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
//		int i = ((Integer)((HashMap)((ArrayList)this.characteristicsList.get(paramInt1)).get(paramInt2)).get("DEVICE_PROPERTIES")).intValue();
//	    this.linearlayout_Group.setVisibility(0);
//	    if (this.mNotify)
//	    {
//	      this.mBluetoothLeService.setCharacteristicNotification(this.mBluetoothGattCharacteristic, false);
//	      this.NotifyButton.setText("Notify");
//	      this.mNotify = false;
//	    }
//	    this.mBluetoothGattCharacteristic = ((BluetoothGattCharacteristic)((HashMap)((ArrayList)this.characteristicsList.get(paramInt1)).get(paramInt2)).get("DEVICE_CHARACTERISTIC"));
//	    this.hexTextView.setText("");
//	    this.dateTextView.setText("");
//	    this.decimalTextView.setText("");
//	    this.asciiTextView.setText("");
//	    this.ReadButton.setVisibility(4);
//	    this.WriteButton.setVisibility(4);
//	    this.NotifyButton.setVisibility(4);
//	    switch (i)
//	    {
//	    default:
//	      return false;
//	    case 2:
//	      ReadButton.setVisibility(0);
//	      return false;
//	    case 4:
//	    case 8:
//	      WriteButton.setVisibility(0);
//	      return false;
//	    case 10:
//	      ReadButton.setVisibility(0);
//	      WriteButton.setVisibility(0);
//	      return false;
//	    case 16:
//	      NotifyButton.setVisibility(0);
//	      return false;
//	    case 18:
//	      ReadButton.setVisibility(0);
//	      NotifyButton.setVisibility(0);
//	      return false;
//	    case 48:
//	      NotifyButton.setVisibility(0);
//	      return false;
//	    case 64:
//	    }
//	    WriteButton.setVisibility(0);
	    return false;
	}

}
