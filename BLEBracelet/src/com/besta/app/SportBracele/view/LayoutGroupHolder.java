package com.besta.app.SportBracele.view;


import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.besta.app.SportBracele.R;
import com.besta.app.SportBracele.BLE.BluetoothLeService;
import com.besta.app.SportBracele.attributes.CharacteristicArrributes;
import com.besta.app.SportBracele.tool.Debug;
import com.besta.app.SportBracele.tool.UsingWidget;


public class LayoutGroupHolder implements View.OnClickListener{
	
	LinearLayout mlayout;
	public LayoutGroupHolder( Activity context, LinearLayout layout){
		mlayout = layout;
		
		hexTextView = (TextView)context.findViewById(R.id.hex_textView);
		dateTextView = (TextView)context.findViewById(R.id.date_textView);
		decimalTextView = (TextView)context.findViewById(R.id.decimal_textView);
		asciiTextView = (TextView)context.findViewById(R.id.ascii_textView);
		
		ReadButton = (Button)context.findViewById(R.id.btn_Read);
		WriteButton = (Button)context.findViewById(R.id.btn_Write);
		NotifyButton = (Button)context.findViewById(R.id.btn_Notify);
		
		ReadButton.setOnClickListener(this);
		WriteButton.setOnClickListener(this);
		NotifyButton.setOnClickListener(this);
		
		mAction = new EnableButton();
		
		mNotify = false;
	}
	
	TextView hexTextView;
	TextView dateTextView;
	TextView decimalTextView;
	TextView asciiTextView;
	
	Button ReadButton;
	Button WriteButton;
	Button NotifyButton;

	BluetoothGattCharacteristic mchar;
	BluetoothLeService mservice;
	
	EnableButton mAction;
	private void clearView(){
		this.hexTextView.setText("");
	    this.dateTextView.setText("");
	    this.decimalTextView.setText("");
	    this.asciiTextView.setText("");
	    this.ReadButton.setVisibility(View.INVISIBLE);
	    this.WriteButton.setVisibility(View.INVISIBLE);
	    this.NotifyButton.setVisibility(View.INVISIBLE);
	}
	public void show( BluetoothLeService service, BluetoothGattCharacteristic characteristic ){
		
	   mlayout.setVisibility(View.VISIBLE);
	   clearView();

	   mchar = characteristic;
	   mservice = service;
	   mAction.function(characteristic.getProperties());
//	    if (this.mNotify)
//	    {
//	      this.mBluetoothLeService.setCharacteristicNotification(this.mBluetoothGattCharacteristic, false);
//	      this.NotifyButton.setText("Notify");
//	      this.mNotify = false;
//	    }
//	    //this.mBluetoothGattCharacteristic = ((BluetoothGattCharacteristic)((HashMap)((ArrayList)this.characteristicsList.get(paramInt1)).get(paramInt2)).get("DEVICE_CHARACTERISTIC"));

	}
	private void handleCurrentTime()
	  {
	    CharSequence localCharSequence = DateFormat.format("yyyy/MM/dd hh:mm:ss a", new Date().getTime());
	    dateTextView.setText(localCharSequence);
	  }

	boolean mNotify;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		handleCurrentTime();
		switch ( v.getId() ){
		case R.id.btn_Read:
			
			mservice.readCharacteristic(mchar);
			break;
		case R.id.btn_Write:
			writeAlertMessageDialog();
			break;
		case R.id.btn_Notify:
			if ((!this.mNotify) && (mservice.setCharacteristicNotification(mchar, true))){
				mNotify = true;
				NotifyButton.setText("Stop Notify");
		    }
			else{
				mservice.setCharacteristicNotification(mchar, false);
				mNotify = false;
		    	NotifyButton.setText("Notify");
			}
			break;
		default:
			break;
		}
	}
	
	public void setInfomation(Intent intent){
		String hex = intent.getStringExtra(BluetoothLeService.EXTRA_HEX);
		String ascii = intent.getStringExtra(BluetoothLeService.EXTRA_ASCII);
		String decimal = intent.getStringExtra(BluetoothLeService.EXTRA_DECIMAL);
		
		if ( hex != null )
			hexTextView.setText(hex);
		if ( ascii != null )
			asciiTextView.setText(ascii);
		if ( decimal != null )
			decimalTextView.setText(decimal);
	}
	
	public void writeAlertMessageDialog() {
		final EditText editor = new EditText(mlayout.getContext());
		editor.setHint("Hex");
		new AlertDialog.Builder(mlayout.getContext())
				.setTitle("Write Hex")
				.setView(editor)
				.setPositiveButton("Send",
						
		new DialogInterface.OnClickListener() {
			public void onClick(
				DialogInterface paramDialogInterface,
					int paramInt) {
				String str = editor.getText()
										.toString();
								try {
									str.replaceAll(" ", "");
									if (str.length() % 2 != 0)
										str = "0" + str;
									//Integer.parseInt(str, 16);
									mservice.writeDataToCharacteristic(mchar,str);
									return;
								} catch (NumberFormatException localNumberFormatException) {
									UsingWidget.handleAlertMessage(mlayout.getContext(),
											"Warning Message.",
											"Invalid value.");
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramDialogInterface,
									int paramInt) {
							}
						}).show();
	}
	
	
	
	private class EnableButton extends CharacteristicArrributes.PropertyAction{

		@Override
		protected void action(int flag) {
			// TODO Auto-generated method stub
			switch ( flag ){
			case BluetoothGattCharacteristic.PROPERTY_READ:
				ReadButton.setVisibility(View.VISIBLE);
				break;
			case BluetoothGattCharacteristic.PROPERTY_WRITE:
			case BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE:
				WriteButton.setVisibility(View.VISIBLE);
				break;
			case BluetoothGattCharacteristic.PROPERTY_NOTIFY:
				mservice.setCharacteristicNotification(mchar, false);
				mNotify = false;
				
				NotifyButton.setVisibility(View.VISIBLE);
				break;
			case BluetoothGattCharacteristic.PROPERTY_BROADCAST:
				Debug.show("Now do nothing");
				break;
			default:
				Debug.show("Error property switch");
				break;
			}
		}
		
	}


}
