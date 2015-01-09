package com.china.acetech.ToolPackage.customwidget.tool;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import com.china.acetech.ToolPackage.R;


/**
 * 滑动动画原生接口有些问题，这个是自定义的
 */
public class SlippingAnimation{

	public static AnimationSet loadAnimation(Context context, int leftOrRight, int inOrOut ){
		AnimationSet animSet = (AnimationSet)AnimationUtils.loadAnimation(context, R.anim.empty_set);
		switch (leftOrRight + inOrOut) {
		case OUT_SCREEN + BY_LEFT:
			animSet.addAnimation(   slipOutScreenByLeft() );
			break;
		case OUT_SCREEN + BY_RIGHT:
			animSet.addAnimation(   slipOutScreenByRight() );
			break;
		case TO_SCREEN + BY_LEFT:
			animSet.addAnimation(   slipToScreenByLeft() );
			break;
		case TO_SCREEN + BY_RIGHT:
			animSet.addAnimation(   slipToScreenByRight() );
			break;

		default:
			animSet = null;
			break;
		}
		return animSet;
	}

	public static final int BY_LEFT = 1;
	public static final int BY_RIGHT = 2;
	public static final int OUT_SCREEN = 10;
	public static final int TO_SCREEN = 19;
	
	
	private static Animation slipToScreenByLeft(){
		return new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_SELF, 0, 
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
	}
	
	private static Animation slipToScreenByRight(){
		return new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
	}
	
	private static Animation slipOutScreenByLeft(){
		return new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, -1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
	}
	
	private static Animation slipOutScreenByRight(){
		return new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
	}
}
