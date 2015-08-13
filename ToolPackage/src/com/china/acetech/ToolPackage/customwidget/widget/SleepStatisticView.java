package com.china.acetech.ToolPackage.customwidget.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.customwidget.tool.MyUtils;
import com.china.acetech.ToolPackage.java.CalendarTool;
import com.china.acetech.ToolPackage.java.CalendarToolForSync;


/**
 * 模仿BarView制作的连续状态显示View。({@link com.china.acetech.ToolPackage.customwidget.widget.BarView} 是离散的view)
 * </br>显示部分请看代码，数据处理可以看{@link SleepStatisticView#initValueList(int first, int second)}
 * Created by Dacer on 11/4/13.
 */
public class SleepStatisticView extends View {
    private int mViewHeight;
    //drawBackground
    private Paint bottomTextPaint = new Paint();

    //popup
    private Paint popupTextPaint = new Paint();
    private final int bottomTriangleHeight = 12;
    private boolean showPopup = false;

    private int topLineLength = MyUtils.dip2px(getContext(), 12);; //padding to view's top bound.
 
    //Constants
    private final int popupTopPadding = MyUtils.dip2px(getContext(), 2);
    //private final int popupBottomMargin = MyUtils.dip2px(getContext(),5);
    //private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");
    private final int BOTTOM_TEXT_COLOR = Color.parseColor("#9B9A9B");

    private Bound selectedBound;
    
    
    
    private static final int sleepColor = R.color.sleep_deep_sleep;
    private static final int restlessColor = R.color.sleep_restless;
    private static final int awakeColor = R.color.sleep_awake;
    private List<Bound> drawBoundList = new ArrayList<Bound>();
    
    private class Bound{
    	float startX;
    	float endX;
    	Calendar startCalendar;
    	Calendar endCalendar;
    	int sleepStatus;
    	
    	public Bitmap board;
		public Canvas boardCanvas;
		public RectF drawRectF;
		
		public void drawBound(Canvas canvas){
			switch(sleepStatus){
                case 1://SleepStatus_AP.SLEEP:
				boardCanvas.drawColor(getContext().getResources().getColor(sleepColor));
				break;
                case 2://SleepStatus_AP.RESTLESS:
				boardCanvas.drawColor(getContext().getResources().getColor(restlessColor));
				break;
                case 3://SleepStatus_AP.AWAKE:
				boardCanvas.drawColor(getContext().getResources().getColor(awakeColor));
				break;
			default:
				break;
			}
			canvas.drawBitmap(board, drawRectF.left, drawRectF.top, null);
		}
		
		public String getString(){
			StringBuffer buffer = new StringBuffer();
			
			//buffer.append(DateFormatTool.getTimeText(getContext(), startCalendar));
			//buffer.append(" - ");
			//buffer.append(DateFormatTool.getTimeText(getContext(), endCalendar));
			
			return buffer.toString();
		}
		
		public int getCenterPosition(){
			return (int)(endX + startX)/2;
		}
    }


    /**
     * 实际的函数是下面的被注释的函数
     *
     * 其根本思路是，得到一段连续的时间，然后读取所有的状态记录表
     * 将状态相同的两个记录合二为一，如果前后有空余的时间间隔则将其填补上
     * @param first
     * @param second
     */
    public void initValueList(int first, int second){}

//    public void initValueList(SleepLog_AP entity, List<SleepStatus_AP> valueList){
//
//    	if ( entity == null )
//    		return;
//    	int length = SBApplication.getTopApp().getResources().getDisplayMetrics().widthPixels;
//    	int sumWidth = length;
//    	int sumTime = entity.getSleepTime().intValue()*60*1000;
//
//    	Calendar startCalendar = SBApplication.getZeroOfToday();
//    	startCalendar.setTime(entity.getStartTime());
//    	Calendar endCalendar = (Calendar)startCalendar.clone();
//    	endCalendar.add(Calendar.MINUTE, entity.getSleepTime().intValue());
//
//    	//如果睡眠記錄的時間不能填滿整個睡眠記錄時間的話，需要增加默認的熟睡記錄進行顯示
//    	if (valueList.size() == 0 ){
//    		valueList.add(new SleepStatus_AP(null, null, startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), SleepStatus_AP.SLEEP, null));
//    	}
//    	long startTime = entity.getStartTime().getTime();
//    	if ( startCalendar.getTimeInMillis() < valueList.get(0).getStartDate() ){
//    		//如果增加的默認熟睡記錄和第一個，最後一個記錄的狀態一樣，需要將兩個記錄合併
//    		SleepStatus_AP temp = valueList.get(0);
//    		if ( temp.getSleepDtatus() == SleepStatus_AP.SLEEP ){
//    			temp = valueList.remove(0);
//    			temp = new SleepStatus_AP(null, null, startCalendar.getTimeInMillis(), temp.getEndDate(), SleepStatus_AP.SLEEP, null);
//    		}
//    		else{
//    			temp = new SleepStatus_AP(null, null, startCalendar.getTimeInMillis(), temp.getStartDate().longValue(), SleepStatus_AP.SLEEP, null);
//    		}
//
//    		valueList.add(0, temp);
//
//    		//valueList.add(0, new SleepStatus_AP(null, null, startCalendar.getTimeInMillis(), temp.getStartDate().longValue(), SleepStatus_AP.SLEEP, null));
//    	}
//
//    	if ( endCalendar.getTimeInMillis() > valueList.get(valueList.size()-1).getEndDate()){
//    		SleepStatus_AP temp = valueList.get(valueList.size()-1);
//    		if ( temp.getSleepDtatus() == SleepStatus_AP.SLEEP ){
//    			temp = valueList.remove(valueList.size()-1);
//    			temp = new SleepStatus_AP(null, null, temp.getStartDate(), endCalendar.getTimeInMillis(), SleepStatus_AP.SLEEP, null);
//    		}
//    		else{
//    			temp = new SleepStatus_AP(null, null, temp.getEndDate(), endCalendar.getTimeInMillis(), SleepStatus_AP.SLEEP, null);
//    		}
//    		valueList.add(temp);
//    	}
//
//    	for ( int i = 0; i < valueList.size(); i ++){
//    		SleepStatus_AP one = valueList.get(i);
//
//    		Bound temp = new Bound();
//    		if ( drawBoundList.size() != 0 )
//    			temp.startX = drawBoundList.get(drawBoundList.size()-1).endX;
//    		else
//    			temp.startX = Float.valueOf(sumWidth * ((one.getStartDate() - startTime)*1.0f)/sumTime);
//    		temp.endX = Float.valueOf(sumWidth * ((one.getEndDate() - startTime)*1.0f)/sumTime);
//    		if ( i == valueList.size() - 1)  temp.endX = sumWidth;
//    		temp.sleepStatus = one.getSleepDtatus();
//
//    		temp.drawRectF = new RectF(temp.startX, topLineLength, temp.endX, MyUtils.dip2px(getContext(), 200- topLineLength));
//
//    		//Add by ace for NO1677-2014-1209-164212-Mick. avoid 0 width of area.
//    		int bitmapWidth = Math.round(temp.drawRectF.width());
//    		if ( bitmapWidth == 0 )  bitmapWidth = 1;
//    		//Add end
//
//    		temp.board = Bitmap.createBitmap(Math.round(temp.drawRectF.width()), Math.round(temp.drawRectF.height()), Bitmap.Config.ARGB_8888);
//    		temp.boardCanvas = new Canvas(temp.board);
//
//    		temp.startCalendar = CalendarToolForSync.getZeroOfToday();
//    		temp.startCalendar.setTimeInMillis(one.getStartDate());
//    		temp.endCalendar = CalendarToolForSync.getZeroOfToday();
//    		temp.endCalendar.setTimeInMillis(one.getEndDate());
//
//    		drawBoundList.add(temp);
//    	}
//    }
    
    public void drawBounds(Canvas canvas){
    	for ( Bound bound : drawBoundList ){
    		bound.drawBound(canvas);
    	}
    }
    

    public SleepStatisticView(Context context){
        this(context,null);
    }
    public SleepStatisticView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public void clear(){
    	init();
    	
    	showPopup = false;    	
        topLineLength = MyUtils.dip2px(getContext(), 12);; //padding to view's top bound.     
        selectedBound = null;        
        drawBoundList = new ArrayList<Bound>();
    }
    
    public void init(){
    	popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(MyUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(),12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        drawBackgroundLines(canvas);
//        drawLines(canvas);
//        drawDots(canvas);
        
        
        drawBounds(canvas);
        
        if(showPopup && selectedBound != null){
            drawPopup(canvas,
                    selectedBound.getString(),
                    selectedBound.getCenterPosition());
        }
    }

    /**
     *
     * @param canvas  The canvas you need to draw on.
     *                Like is ↓
     *		          3
     *                2
     *                1
     *                0 1 2 3 4 5
     */
    private void drawPopup(Canvas canvas,String date, int center){
        boolean singularNum = (date.length() == 1);
        int sidePadding = MyUtils.dip2px(getContext(),singularNum? 8:5);
        
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(date,0,date.length(),popupTextRect);
        Rect r = new Rect(center-popupTextRect.width()/2-sidePadding,
                topLineLength,
                center + popupTextRect.width()/2+sidePadding,
                topLineLength+popupTextRect.height()+bottomTriangleHeight+2*popupTopPadding) ;

        NinePatchDrawable popup = (NinePatchDrawable)getResources().
                getDrawable(R.drawable.popup_red);
        popup.setBounds(r);
        popup.draw(canvas);
        canvas.drawText(date, center, topLineLength+popupTextRect.height()+popupTopPadding, popupTextPaint);
    }

//    private int getPopupHeight(){
//        Rect popupTextRect = new Rect();
//        popupTextPaint.getTextBounds("9",0,1,popupTextRect);
//        Rect r = new Rect(-popupTextRect.width()/2,
//                 - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
//                 + popupTextRect.width()/2,
//                +popupTopPadding-popupBottomMargin);
//        return r.height();
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureWidth(int measureSpec){
//        int horizontalGridNum = getHorizontalGridNum();
//        int preferred = backgroundGridWidth*horizontalGridNum+sideLineLength*2;
    	int width = MyApplication.getTopApp().getResources().getDisplayMetrics().widthPixels;
        return getMeasurement(measureSpec, width-10);
    }

    private int measureHeight(int measureSpec){
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        Region r = new Region();
        if(drawBoundList != null || !drawBoundList.isEmpty()){
        	for (Bound bound : drawBoundList ){
        		r.set((int)bound.startX, topLineLength, (int)bound.endX, MyUtils.dip2px(getContext(), 200- topLineLength));
        		if (r.contains(point.x,point.y) && event.getAction() == MotionEvent.ACTION_DOWN){
                    selectedBound = bound;
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    if (r.contains(point.x,point.y)){
                        showPopup = true;
                    }
                }
        	}
        }
        
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_UP){
            postInvalidate();
        }
        return true;
      //這裡return true並不會影響到上一層的消息處理。
        //其原因是，Ontouch return true後，僅僅是Touch事件不會向上傳遞，event仍然會經由Interrupt來判斷是否需要進行截獲
        //所以即使這裡的ontouch已經return true。上一層view仍然可以在需要的時候進行截獲。
        //換句話說，View層級越高，對event越有掌控能力
        //當然。dispatch方法是在這些處理之前的，不過不是太建議在這裡進行處理。
    }
}