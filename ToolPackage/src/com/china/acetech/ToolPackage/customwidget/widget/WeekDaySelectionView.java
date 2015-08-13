package com.china.acetech.ToolPackage.customwidget.widget;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.china.acetech.ToolPackage.R;


/**
 * 自定义的星期选择控件，外观不错
 */
public class WeekDaySelectionView extends LinearLayout
  implements CompoundButton.OnCheckedChangeListener
{
  private static final int[] boxID = { R.id.chk_day_1,
	  R.id.chk_day_2, R.id.chk_day_3, R.id.chk_day_4, R.id.chk_day_5,
	  R.id.chk_day_6, R.id.chk_day_7 };
  private CheckBox[] checkBoxArray = new CheckBox[7];
  private Calendar mCalendar;
  private Set<Integer> checkedSet = new TreeSet<Integer>();
  private WeekDayFormat mFormat;

  public WeekDaySelectionView(Context paramContext)
  {
    super(paramContext);
  }

  public WeekDaySelectionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public WeekDaySelectionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private int getDayOfWeek(int paramInt)
  {
    this.mCalendar.set(Calendar.DAY_OF_WEEK, this.mCalendar.getFirstDayOfWeek());
    this.mCalendar.add(Calendar.DAY_OF_WEEK, paramInt);
    return this.mCalendar.get(Calendar.DAY_OF_WEEK);
  }

  private int getCheckBoxNumber(CompoundButton paramCompoundButton)
  {
    for (int i = 0; i < this.checkBoxArray.length; ++i)
      if (paramCompoundButton == this.checkBoxArray[i])
        return i;
    return -1;
  }

  private void initCheckBoxStatus()
  {
    this.mCalendar.set(Calendar.DAY_OF_WEEK, this.mCalendar.getFirstDayOfWeek());
    for (int i = 0; i < this.checkBoxArray.length; ++i)
    {
      this.checkBoxArray[i] = ((CheckBox)findViewById(boxID[i]));
      this.checkBoxArray[i].setText(this.mFormat.format(this.mCalendar.getTime()));
      this.checkBoxArray[i].setChecked(this.checkedSet.contains(Integer.valueOf(this.mCalendar.get(Calendar.DAY_OF_WEEK))));
      this.checkBoxArray[i].setOnCheckedChangeListener(this);
      this.mCalendar.add(Calendar.DAY_OF_WEEK, 1);
    }
  }

  protected void init()
  {
    if (this.mCalendar == null)
      this.mCalendar = new GregorianCalendar(Locale.US);
    if (this.mFormat == null)
      this.mFormat = new WeekDayFormat(getContext());
    initCheckBoxStatus();
  }

  public void initStatusSet(int repeatStatus ){
	  int count = 0;
	  for ( count = 0; count < 7; count++){
		  if ( ((repeatStatus>>count) & 0x01) == 1){
			  this.checkedSet.add(getDayOfWeek(count));
		  }
	  }
	  initCheckBoxStatus();
  }
  public void initStatusSet(Set<Integer> paramSet)
  {
    this.checkedSet = paramSet;
    if (this.checkedSet == null)
      this.checkedSet = new TreeSet<Integer>();
    initCheckBoxStatus();
  }

  public Set<Integer> getCheckedSet()
  {
    return new TreeSet<Integer>(this.checkedSet);
  }
  
  public int getStatusSet(){
	  
	  int count = 0;
	  int res = 0;
	  this.mCalendar.set(Calendar.DAY_OF_WEEK, this.mCalendar.getFirstDayOfWeek());
	  for (int i = 0; i < this.checkBoxArray.length; ++i){
		  if ( checkBoxArray[i].isChecked() )
			  res = res | (0x01<<count);
		  else
			  res = res & (~(0x01<<count));
		  
		  count++;
	  }
	  
	  return res;
  }

  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    int i = getCheckBoxNumber(paramCompoundButton);
    if (i >= 0)
    {
      if (!paramBoolean){
    	  this.checkedSet.remove(Integer.valueOf(getDayOfWeek(i)));
    	  return;
      }
      this.checkedSet.add(Integer.valueOf(getDayOfWeek(i)));
    }
  }

//  protected void onRestoreInstanceState(Parcelable paramParcelable)
//  {
//    BundleSaveState localBundleSaveState = (BundleSaveState)paramParcelable;
//    int[] arrayOfInt = localBundleSaveState.a().getIntArray("days");
//    this.d.clear();
//    this.d.addAll(b.a(arrayOfInt));
//    d();
//    super.onRestoreInstanceState(localBundleSaveState.getSuperState());
//  }
//
//  protected Parcelable onSaveInstanceState()
//  {
//    BundleSaveState localBundleSaveState = new BundleSaveState(super.onSaveInstanceState());
//    Bundle localBundle = new Bundle();
//    localBundle.putIntArray("days", b.a(this.d));
//    localBundleSaveState.a(localBundle);
//    return localBundleSaveState;
//  }

  @SuppressLint({"UseSparseArrays"})
  private static class WeekDayFormat extends DateFormat
  {
    private static final long serialVersionUID = -7693782491638024993L;
    private Calendar calendar;
    private Context context;
    
    private static SparseIntArray shortNameArray;
    
    static
    {
    	shortNameArray = new SparseIntArray();
      shortNameArray.append(2, R.string.monday_statis);
      shortNameArray.append(3, R.string.tuesday_statis);
      shortNameArray.append(4, R.string.wednesday_statis);
      shortNameArray.append(5, R.string.thursday_statis);
      shortNameArray.append(6, R.string.friday_statis);
      shortNameArray.append(7, R.string.saturday_statis);
      shortNameArray.append(1, R.string.sunday_statis);
    }

    public WeekDayFormat(Context paramContext)
    {
      this.context = paramContext.getApplicationContext();
      this.calendar = new GregorianCalendar();
    }

    public StringBuffer format(Date paramDate, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
    {
      this.calendar.setTime(paramDate);
      int week = shortNameArray.get(this.calendar.get(Calendar.DAY_OF_WEEK));
      return new StringBuffer(this.context.getString(week));
    }

    public Date parse(String paramString, ParsePosition paramParsePosition)
    {
      return null;
    }
  }

}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes-dex2jar.jar
 * Qualified Name:     com.fitbit.ui.WeekDaySelectionView
 * JD-Core Version:    0.5.4
 */