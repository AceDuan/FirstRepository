package com.china.acetech.ToolPackage.customwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * 在该view上的点击操作会相应的发送给checkbox进行响应。相当于自定义的checkbox
 */
public class CheckableWrapperRelativeLayout extends RelativeLayout implements
		Checkable {
	private Checkable mCheckBox;

	public CheckableWrapperRelativeLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public void setCheckboxID(int CheckBoxID) {
		View view = findViewById(CheckBoxID);
		view.setClickable(false);
		this.mCheckBox = ((Checkable) view);
	}

	public boolean isChecked() {
		return this.mCheckBox.isChecked();
	}

	public void setChecked(boolean isCheck) {
		this.mCheckBox.setChecked(isCheck);
	}

	public void toggle() {
		this.mCheckBox.toggle();
	}
}