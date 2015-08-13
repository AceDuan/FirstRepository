package com.china.acetech.ToolPackage.customwidget.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.china.acetech.ToolPackage.R;

public final class WeekDaySelectionView_ extends WeekDaySelectionView
{
  private boolean isInflate = false;

  public WeekDaySelectionView_(Context paramContext)
  {
    super(paramContext);
  }

  public WeekDaySelectionView_(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public WeekDaySelectionView_(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public static WeekDaySelectionView a(Context paramContext)
  {
    WeekDaySelectionView_ localWeekDaySelectionView_ = new WeekDaySelectionView_(paramContext);
    localWeekDaySelectionView_.onFinishInflate();
    return localWeekDaySelectionView_;
  }

  public static WeekDaySelectionView a(Context paramContext, AttributeSet paramAttributeSet)
  {
    WeekDaySelectionView_ localWeekDaySelectionView_ = new WeekDaySelectionView_(paramContext, paramAttributeSet);
    localWeekDaySelectionView_.onFinishInflate();
    return localWeekDaySelectionView_;
  }

  public static WeekDaySelectionView a(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    WeekDaySelectionView_ localWeekDaySelectionView_ = new WeekDaySelectionView_(paramContext, paramAttributeSet, paramInt);
    localWeekDaySelectionView_.onFinishInflate();
    return localWeekDaySelectionView_;
  }


  private void initAll()
  {
    init();
  }

  public void onFinishInflate()
  {
	  super.onFinishInflate();
    if (!this.isInflate)
    {
      this.isInflate = true;
      inflate(getContext(), R.layout.l_week_day_selection_view, this);
      initAll();
    }
    
  }
}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes-dex2jar.jar
 * Qualified Name:     com.fitbit.ui.WeekDaySelectionView_
 * JD-Core Version:    0.5.4
 */