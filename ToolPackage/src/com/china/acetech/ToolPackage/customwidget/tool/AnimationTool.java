package com.china.acetech.ToolPackage.customwidget.tool;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

public class AnimationTool {
	
	public static void setAminationByOrder(Animation[] startUp, Animation[] second){
		Animation start_anim = startUp[0];
		long offset;
		if ( start_anim != null )
			offset = start_anim.getDuration() + start_anim.getStartOffset();
		else
			offset = 0;
		
		for ( int i = 0; i < second.length; i++){
			second[i].setStartOffset(offset);
		}
	}
	
	/**
	 * 
	 * 遍歷並disable所有的可操作的view
	 * 
	 * @param viewGroup 需要disable的View
	 */
	public static void disableSubControls(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				disableSubControls((ViewGroup) v);
//				if (v instanceof Spinner) {
//					Spinner spinner = (Spinner) v;
//					spinner.setClickable(false);
//					spinner.setEnabled(false);
//
//					Log.i(TAG, "A Spinner is unabled");
//				} else if (v instanceof ListView) {
//					((ListView) v).setClickable(false);
//					((ListView) v).setEnabled(false);
//
//					Log.i(TAG, "A ListView is unabled");
//				} else {
//					disableSubControls((ViewGroup) v);
//				}
			} else if (v instanceof EditText) {
				((EditText) v).setEnabled(false);
				((EditText) v).setClickable(false);

			} else if (v instanceof Button) {
				((Button) v).setEnabled(false);
			}
		}
	}
	
	/**
	 * 
	 * 遍歷並enable所有的可操作的view
	 * 
	 * @param viewGroup 需要enable的View
	 */
	public static void enableSubControls(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View v = viewGroup.getChildAt(i);
			if (v instanceof ViewGroup) {
				enableSubControls((ViewGroup) v);
			} else if (v instanceof EditText) {
				((EditText) v).setEnabled(true);
				((EditText) v).setClickable(true);

			} else if (v instanceof Button) {
				((Button) v).setEnabled(true);
			}
		}
	}
}
