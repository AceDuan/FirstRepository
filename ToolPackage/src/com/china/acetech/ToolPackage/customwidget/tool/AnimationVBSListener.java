package com.china.acetech.ToolPackage.customwidget.tool;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * VBS: Visible View before start animation
 * @author bxc2010011
 *
 */
public class AnimationVBSListener implements AnimationListener{

	private View[] visViews;
	
	public AnimationVBSListener(View[] view) {
		visViews = view;
	}
	@Override
	public void onAnimationStart(Animation animation) {
		for ( int i = 0; i < visViews.length; i++ ){
			visViews[i].setVisibility(View.VISIBLE);
		}
		visViews = null;
	}

	@Override
	public void onAnimationEnd(Animation animation) {}

	@Override
	public void onAnimationRepeat(Animation animation) {}

}
