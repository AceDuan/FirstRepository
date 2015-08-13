package com.china.acetech.ToolPackage.customwidget.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.java.CalendarToolForSync;

/**
 * 睡眠的初始化已经被关闭(其实只有sleepStatisticView的初始化因为类型原因才被放弃)
 *
 * 这个控件内部必须要有4个textview来作为切换的button。但是代码中初始化textview的过程还没有
 * 仔细研究过，所以暂时是在xml中定义这些textview。例子在 资源文件item_main_info_sportlist_chid.xml
 * @author bxc2010011
 *
 */
public class StatisticSwitcher extends LinearLayout{

	public StatisticSwitcher(Context context) {
		super(context);
	}
	
	public StatisticSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public StatisticSwitcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	TextView day;
	TextView week;
	TextView month;
	TextView year;
	
	ViewSwitcher mSwitcher;
	
	TextView focusView;
		
	int mItemNumber;
	
	DataProvider mProvider;
	
	public void init(int position, DataProvider provider , ViewSwitcher switcher, boolean isFocusedOnDay){
		day = (TextView)findViewById(R.id.statistic_day);
		week = (TextView)findViewById(R.id.statistic_week);
		month = (TextView)findViewById(R.id.statistic_month);
		year = (TextView)findViewById(R.id.statistic_year);
		
		this.mSwitcher = switcher;
		
		OnClickListener listener = new TextViewClickListener();
		if ( day != null )
			day.setOnClickListener(listener);
		week.setOnClickListener(listener);
		month.setOnClickListener(listener);
		year.setOnClickListener(listener);
		
		mItemNumber = position;
		
		mProvider = provider;
		if ( isFocusedOnDay )
			setFocusView(day);
		else
			setFocusView(week);
		
		
	}
	
	public void clear(){
		if ( mSwitcher == null )
			return;
		
		if ( mSwitcher.getChildAt(0) instanceof SleepStatisticView){
			SleepStatisticView sleep = (SleepStatisticView)mSwitcher.getChildAt(0);
			sleep.clear();
		}
		else if ( mSwitcher.getChildAt(0) instanceof BarView){
			BarView bar = (BarView)mSwitcher.getChildAt(0);
			bar.clear();
		}
		
		LineView line = (LineView)mSwitcher.getChildAt(1);
		line.clear();
		
		if ( focusView != null ){
			focusView.setBackground(getResources().getDrawable(R.drawable.statistic_nomal));
			focusView.setTextColor(getResources().getColorStateList(R.color.statistic_text_color));
		}
		focusView = null;
	}
	
	public void RefreshView(){
		if ( focusView != week ){
			focusView.setBackground(getResources().getDrawable(R.drawable.statistic_nomal));
			focusView.setTextColor(getResources().getColorStateList(R.color.statistic_text_color));
		}
		focusView = week;
		focusView.setBackground(getResources().getDrawable(R.drawable.statistic_focus));
		focusView.setTextColor(getResources().getColorStateList(R.color.statistic_text_color_inver));
		
		if ( focusView != day ){
			mSwitcher.setDisplayedChild(1);
			setLineViewData(focusView);
		}
		else{
			mSwitcher.setDisplayedChild(0);
			setBarViewData(focusView);
			
		}
	}
	public void setFocusView(TextView view){
		if ( focusView == view )
			return;
		
		if ( focusView != null ){
			focusView.setBackground(getResources().getDrawable(R.drawable.statistic_nomal));
			focusView.setTextColor(getResources().getColorStateList(R.color.statistic_text_color));
		}
		focusView = view;
		focusView.setBackground(getResources().getDrawable(R.drawable.statistic_focus));
		focusView.setTextColor(getResources().getColorStateList(R.color.statistic_text_color_inver));
		
		if ( focusView != day ){
			mSwitcher.setDisplayedChild(1);
			setLineViewData(focusView);
		}
		else{
			mSwitcher.setDisplayedChild(0);
			setBarViewData(focusView);
			
		}
	}
	
	private void setBarViewData(TextView currentView){
		if ( mSwitcher.getChildAt(0) instanceof BarView ){

			int timeStyle = SportInfo_AP.HOUR_STATUSTIC;
			Calendar cal = (Calendar)mProvider.getCalendar().clone();
			Calendar starthour = null;
			Calendar lasthour = null;
			
			setTimeToDaily(cal);
			
			starthour = (Calendar)cal.clone();
			
			if ( starthour.before(CalendarToolForSync.getZeroOfToday())){
				lasthour = (Calendar)starthour.clone();
				lasthour.add(Calendar.DATE, 1);
				lasthour.add(Calendar.HOUR, -1);
			}
			else{
				lasthour = (Calendar)starthour.clone();
				Calendar currentTime = CalendarToolForSync.getCurrentTimeOfStandardCalendar();
				lasthour.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY)+1);
			}
			
			ArrayList<?> infoList = mProvider.getValueList(mItemNumber, timeStyle, starthour, lasthour);
			ArrayList<String> bottomTextList = getBottomTextList(currentView, lasthour);
			
			 BarView line = (BarView)mSwitcher.getChildAt(0);
		        line.setBottomTextList(bottomTextList);
		        line.setDataList(infoList);
		}
		else{
			if ( mSwitcher.getChildAt(0) instanceof SleepStatisticView){
				SleepStatisticView sleep = (SleepStatisticView)mSwitcher.getChildAt(0);

			//因为类型未定义的原因。这里暂时先注释掉
			//sleep.initValueList(mEntity, sleepList);
			}
		}
	}
	
	/**
	 * 僅用與睡眠界面的顯示
	 */
//	SleepLog_AP mEntity;
//	List<SleepStatus_AP> sleepList;
//	public void initSleepValue( SleepLog_AP entity, List<SleepStatus_AP> list){
//		mEntity = entity;
//		sleepList = list;
//
//		if ( mEntity == null )//理論上來說這裡是不可能為null。加一個空指針保護
//			return;
//		Calendar startCal = SBApplication.getZeroOfToday();
//		startCal.setTime(entity.getStartTime());
//		Calendar endCal = (Calendar)startCal.clone();
//		endCal.add(Calendar.MINUTE, entity.getSleepTime().intValue());
//
//		//sleep list里存放的是當天所有的睡眠狀態記錄，而實際顯示的只是一部分。所以需要過濾掉不顯示的部分。
//		//如果有一筆狀態記錄，起始在睡眠記錄之前，結束在記錄之後，需要截取有用的那一部分進行使用
//    	while ( sleepList.size() != 0 && startCal.getTimeInMillis() > sleepList.get(0).getStartDate() ){
//
//    		SleepStatus_AP tempEntity = sleepList.remove(0);
//    		if ( tempEntity.getEndDate() > startCal.getTimeInMillis() ){
//    			sleepList.add(0, new SleepStatus_AP(null, tempEntity.getCalendar(), startCal.getTimeInMillis(), tempEntity.getEndDate(), tempEntity.getSleepDtatus(), tempEntity.getLastsynctime()));
//    		}
//    	}
//
//    	while ( sleepList.size() != 0 && endCal.getTimeInMillis() < sleepList.get(sleepList.size()-1).getEndDate()){
//    		SleepStatus_AP tempEntity = sleepList.remove(sleepList.size()-1);
//    		if ( tempEntity.getStartDate() < endCal.getTimeInMillis() ){
//    			sleepList.add(new SleepStatus_AP(null, tempEntity.getCalendar(), tempEntity.getStartDate(), endCal.getTimeInMillis(), tempEntity.getSleepDtatus(), tempEntity.getLastsynctime()));
//    		}
//    	}
//	}
	
	private void setLineViewData(TextView currentView){
		int timeStyle = -1;
		Calendar cal = (Calendar)mProvider.getCalendar().clone();
		Calendar lastDay = null;
		
		if ( currentView == week ){
			timeStyle = SportInfo_AP.DAY_STATUSTIC;
			setTimeToDaily(cal);
			cal.add(Calendar.DATE, 0-(cal.get(Calendar.DAY_OF_WEEK)-1));
			lastDay = (Calendar)cal.clone();
			lastDay.add(Calendar.DATE, 6);
			
			
			
		}
		else if ( currentView == month ){
			timeStyle = SportInfo_AP.DAY_STATUSTIC;
			setTimeToMonthly(cal);//得到這個月的第一天的日期
			lastDay = (Calendar)cal.clone();
			lastDay.add(Calendar.MONTH, 1);
			lastDay.add(Calendar.DATE, -1);
		}
		else if ( currentView == year ){
			timeStyle = SportInfo_AP.MONTH_STATUSTIC;
			setTimeToFisrtDayOfYear(cal);
			lastDay = (Calendar)cal.clone();
			lastDay.add(Calendar.MONTH, 11);
		}
		else{
			return;
		}
		
		if ( lastDay.after(mProvider.getCalendar()))
			lastDay = (Calendar)mProvider.getCalendar().clone();
		ArrayList<?> infoList = mProvider.getValueList(mItemNumber, timeStyle, cal, lastDay);
		ArrayList<String> bottomTextList = getBottomTextList(currentView, lastDay);
		
        LineView line = (LineView)mSwitcher.getChildAt(1);
        line.setBottomTextList(bottomTextList);
        line.setDataList(infoList);
	}
	
	public static final int[] weekStringID = { R.string.monday_statis,
			R.string.tuesday_statis, R.string.wednesday_statis,
			R.string.thursday_statis, R.string.friday_statis,
			R.string.saturday_statis, R.string.sunday_statis };

	public static final int[] yearStringID = { R.string.january_statis,
			R.string.february_statis, R.string.march_statis,
			R.string.april_statis, R.string.may_statis, 
			R.string.june_statis, R.string.july_statis, R.string.august_statis,
			R.string.september_statis, R.string.october_statis,
			R.string.november_statis, R.string.december_statis };
	
	public ArrayList<String> getBottomTextList(View currentView, Calendar lastDayOfMonth){
		ArrayList<String> list = new ArrayList<String>();
		
		if ( currentView == week ){

			for ( int i = 0; i < weekStringID.length; i++){
				list.add(currentView.getResources().getString(weekStringID[i]));
			}
			if ( lastDayOfMonth.getFirstDayOfWeek() == Calendar.SUNDAY){
				String sun = list.remove(list.size()-1);
				list.add(0, sun);
			}
		}
		else if ( currentView == month ){
			Calendar cal = (Calendar)lastDayOfMonth.clone();
			cal.add(Calendar.DATE, -(cal.get(Calendar.DATE)-1));
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
			int length = cal.get(Calendar.DAY_OF_MONTH);
			for ( int i = 1; i <= length; i++){
				list.add(String.valueOf(i));
			}
		}
		else if ( currentView == year ){
			for ( int i = 0; i < yearStringID.length; i++){
				list.add(currentView.getResources().getString(yearStringID[i]));
			}
		}
		else if ( currentView == day ){
			for ( int i = 0; i <= 24; i++){
				list.add(String.valueOf(i));
			}
		}
		//mProvider.getCalendar().get
		
		return list;
	}
	
	public static void setTimeToMonthly(Calendar cal){
		Calendar temp = (Calendar)cal.clone();
		cal.clear();
		
		cal.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, temp.get(Calendar.MONTH));
		
	}
	
	public static void setTimeToHourly(Calendar cal){
		Calendar temp = (Calendar)cal.clone();
		cal.clear();
		
		cal.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, temp.get(Calendar.MONTH));
		cal.set(Calendar.DATE, temp.get(Calendar.DATE));
		cal.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
		
	}
	
	public static void setTimeToDaily(Calendar cal){
		Calendar temp = (Calendar)cal.clone();
		cal.clear();
		
		cal.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, temp.get(Calendar.MONTH));
		cal.set(Calendar.DATE, temp.get(Calendar.DATE));
		
	}
	
	public static void setTimeToFisrtDayOfYear(Calendar cal){
		Calendar temp = (Calendar)cal.clone();
		cal.clear();
		
		cal.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		
	}
	
	
	class TextViewClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			setFocusView((TextView)v);
		}
		
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return true;
	}
	
	public static interface DataProvider{
		public ArrayList<?> getValueList(int position, int timeStyle, Calendar sDate, Calendar eDate);
		
		public Calendar getCalendar();
	}
	
}
