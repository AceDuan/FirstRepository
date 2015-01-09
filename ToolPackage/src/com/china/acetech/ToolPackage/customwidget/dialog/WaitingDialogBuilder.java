package com.china.acetech.ToolPackage.customwidget.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.china.acetech.ToolPackage.R;

public class WaitingDialogBuilder extends AlertDialog{

	protected WaitingDialogBuilder(Context context, int theme) {
		super(context, theme);
	}
	
	protected WaitingDialogBuilder(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	
	protected WaitingDialogBuilder(Context context) {
		super(context);
	}

	public static AlertDialog create(Context context) {
		AlertDialog dialog;
		View progressBar = ((LayoutInflater) context
				.getSystemService(ContextWrapper.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.dialog_waiting, null, false);
		TextView hint = (TextView) progressBar.findViewById(R.id.wait_hint);
		hint.setText(R.string.waiting);
		dialog = new Builder(context).setView(progressBar).create();
		dialog.setCanceledOnTouchOutside(true);
		
		return dialog;
	}

}
