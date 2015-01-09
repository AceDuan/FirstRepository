package com.china.acetech.ToolPackage.customwidget.tool;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * 可用来统一管理多个view动画的渐隐渐显过程
 */
public class TimerAnimation extends Animation{
	
	Context mContext;
	ArrayList<TextView> mView;
	
	private long priod = 100;
	
	private long times = 10;
	
	private long max = 255;
	
	private long alpha;
	
	private int start = 0;
	public TimerAnimation( Context context ){
		
		super();
	    mContext = context;
	    mView = new ArrayList<TextView>();
	    setDuration(300);
	}
	
	public void addView(TextView text){
		mView.add(text);
		text.setTextColor(text.getTextColors().withAlpha(0));
	}
	
	Timer tim = new Timer();
    TimerTask mTask = new TimerTask() {
		
		@Override
		public void run() {
			if ( mContext != null ){
				((Activity)mContext).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {

						if ( mView == null )	return;
						
						start += alpha;
						if ( start >= max ){
							start = (int)max;
						}
						
						for( TextView view: mView){
							view.setTextColor(
									view.getTextColors().withAlpha(start));
						}
						
						if ( start == max ){

							tim.cancel();
							isSchedule = true;
							mView.clear();
							mView = null;
							mContext = null;
						}
					}
				});
				
			}
		}
	};
	
	
	private boolean isSchedule = false;

	
	@Override
	public void cancel() {
		super.cancel();
		tim.cancel();
	}


	@Override
	public void reset() {
		super.reset();

		start = 0;
		if ( !isSchedule ){
			tim.schedule(mTask, 0, priod);
			isSchedule = true;
		}
	}


	@Override
	public void setDuration(long durationMillis) {
		super.setDuration(durationMillis);
		
		countPriod( durationMillis );
		
		
		
	}
	
	private void countPriod(long durationMillis){
		priod = durationMillis / times;
		
		alpha = 255 / times;
		if ( alpha* times < 255 )
			alpha += 1;
	}
	
	
}
