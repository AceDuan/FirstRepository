package com.china.acetech.ToolPackage.customwidget.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.customwidget.tool.MyUtils;
import com.china.acetech.ToolPackage.debug.DebugTool;
import com.china.acetech.ToolPackage.java.NumberCount;


/**
 * 一个可以以柱状图显示需要的内容的view。具有侧边栏和底部栏。没有动画
 *
 * 使用示例参照{@link LineView}</br>
 * Created by Ace on 11/11/14.
 */
public class BarView extends View {
    private Paint bottomTextPaint;
    private Paint fgPaint;
    private Rect rect;
//    private boolean showSideMargin = true;
    private int bottomTextDescent;
    private int bottomTextHeight;
    private ArrayList<String> bottomTextList;
    private final int BAR_SIDE_MARGIN;
    private final int TEXT_COLOR = Color.parseColor("#9B9A9B");
    private final int FOREGROUND_COLOR = Color.parseColor("#2ac3c5");

    private int mViewHeight;
    
    private int topLineLength = MyUtils.dip2px(getContext(), 12);; //padding to view's top bound.
    private int sideLineLength = MyUtils.dip2px(getContext(),30)/3*4;// padding to view's left bound. 
    private float backgroundGridWidth = MyUtils.dip2px(getContext(),45)*1.0f;	//Grid Node's width and height.
    
    private final int popupTopPadding = MyUtils.dip2px(getContext(),2);
    private final int popupBottomMargin = MyUtils.dip2px(getContext(),5);
    private final int bottomTextTopMargin = MyUtils.sp2px(getContext(),5);
    private final int bottomLineLength = MyUtils.sp2px(getContext(), 12);
    private final int leftTextMargin = MyUtils.sp2px(getContext(), 5)+23;//add by ace for left number
    private final int leftTextMaxWidth = 0;//add by ace for left number
    
    private final int MIN_TOP_LINE_LENGTH = MyUtils.dip2px(getContext(),12);
    private final int MIN_VERTICAL_GRID_NUM = 4;
    private final int MIN_HORIZONTAL_GRID_NUM = 1;
    

    private ArrayList<Integer> dataList;
    private ArrayList<Integer> xCoordinateList = new ArrayList<Integer>();
    private ArrayList<Integer> yCoordinateList = new ArrayList<Integer>();
    private ArrayList<Bar> drawBarList = new ArrayList<Bar>();
    
    //popup
    private Paint popupTextPaint = new Paint();
    private final int bottomTriangleHeight = 12;
    private boolean showPopup = false;
    private Bar selectedBar;
    
    private boolean isDoubleValue = false;
    private double valueBEI = 100.0;
    
    private Runnable animator = new Runnable() {
        @Override
        public void run() {
//                boolean needNewFrame = false;
//                for (int i=0; i<targetPercentList.size();i++) {
//                    if (percentList.get(i) < targetPercentList.get(i)) {
//                        percentList.set(i,percentList.get(i)+0.02f);
//                        needNewFrame = true;
//                    } else if (percentList.get(i) > targetPercentList.get(i)){
//                        percentList.set(i,percentList.get(i)-0.02f);
//                        needNewFrame = true;
//                    }
//                    if(Math.abs(targetPercentList.get(i)-percentList.get(i))<0.02f){
//                        percentList.set(i,targetPercentList.get(i));
//                    }
//                }
//                if (needNewFrame) {
//                    postDelayed(this, 20);
//                }
//                invalidate();
        }
    };

    public BarView(Context context){
        this(context,null);
    }
    public BarView(Context context, AttributeSet attrs){
        super(context, attrs);
        BAR_SIDE_MARGIN  = MyUtils.dip2px(getContext(),7);
        init();
        
    }

    /**
     * clear all data in this view
     */
    public void clear(){
    	init();
    	this.dataList = null;
    	this.bottomTextList = null;
    	this.xCoordinateList = new ArrayList<Integer>();
    	this.yCoordinateList = new ArrayList<Integer>();
    	this.drawBarList = new ArrayList<Bar>();
    	
    	showPopup = false;
        selectedBar = null;
        
        isDoubleValue = false;
        valueBEI = 100.0;
    }
    
    /**
     * initial all value 
     */
    public void init(){
    	fgPaint = new Paint();
        fgPaint.setAntiAlias(true);
        fgPaint.setColor(FOREGROUND_COLOR);
        rect = new Rect();
        int textSize = MyUtils.sp2px(getContext(), 15);
        
        bottomTextPaint = new Paint();
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setColor(TEXT_COLOR);
        bottomTextPaint.setTextSize(textSize);
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        
        
        popupTextPaint = new Paint();
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(MyUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);
        
    }
    
    /**
     * dataList will be reset when called is method.
     * @param bottomStringList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomStringList){
        
        this.dataList = null;
        this.bottomTextList = bottomStringList;

        Rect r = new Rect();
        //bottomTextDescent = 0; 讓text和底部不是貼的太緊
        bottomTextDescent = MyUtils.sp2px(getContext(),10);
        for(String s:bottomTextList){
            bottomTextPaint.getTextBounds(s,0,s.length(),r);
            if(bottomTextHeight<r.height()){
                bottomTextHeight = r.height();
            }
            if(bottomTextDescent<(Math.abs(r.bottom))){
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        
        //新增的計算辦法，根據bottom需要顯示的數量來決定間隔，保證全部顯示在一個屏幕內
        int number = bottomTextList.size() -1;
        int length = MyApplication.getTopApp().getResources().getDisplayMetrics().widthPixels;
        backgroundGridWidth = (length - leftTextMargin - sideLineLength)*1.0f/number;
        if ( number > 25 ){
        	bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(),9));
        }
        else{
        	bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(),12));
        }

        refreshXCoordinateList(getHorizontalGridNum());
    }

    @SuppressWarnings("unchecked")
	public void setDataList(ArrayList<?> dataList){
    	
    	if ( dataList.size() != 0 ){
    		//增加對double類型的支持
    		if ( dataList.get(0) instanceof Double){
				ArrayList<Double> tempDoublelist = (ArrayList<Double>)dataList.clone();
    			ArrayList<Integer> countList = new ArrayList<Integer>();
    			for ( Double d : tempDoublelist ){
    				d = d*100;
    				countList.add(d.intValue());
    			}
    			
    			boolean isAllover10 = true;
    			for ( Integer i : countList){
    				if ( i%10 != 0 ){
    					isAllover10 = false;
    					break;
    				}
    			}
    			if ( isAllover10 ){
    				ArrayList<Integer> tempIntList = new ArrayList<Integer>();
    				for ( Integer i : countList ){
    					tempIntList.add(i/10);
    					valueBEI = 10;
    				}
    				countList = tempIntList;
    			}
    			
    			isAllover10 = true;
    			for ( Integer i : countList){
    				if ( i%10 != 0 ){
    					isAllover10 = false;
    					break;
    				}
    			}
    			if ( isAllover10 ){
    				ArrayList<Integer> tempIntList = new ArrayList<Integer>();
    				for ( Integer i : countList ){
    					tempIntList.add(i/10);
    					valueBEI = 1;
    				}
    				countList = tempIntList;
    			}
    			
    			this.dataList = countList;
    			isDoubleValue = true;
    		}
    		else{
    			this.dataList = (ArrayList<Integer>)dataList;
    		}
    	}
    	else{
    		throw new RuntimeException("dacer.LineView error:" +
                    " dataList.size() > bottomTextList.size() !!!");
    	}
    	
        if(dataList.size() > bottomTextList.size()){
            throw new RuntimeException("dacer.LineView error:" +
                    " dataList.size() > bottomTextList.size() !!!");
        }
        //dataOfAGird = 3;
        refreshAfterDataChanged();
        setMinimumWidth(0); // It can help the LineView reset the Width,
                                // I don't know the better way..
        postInvalidate();
    }
    
    boolean mDownTouch = false;
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        Region r = new Region();
        //int width = backgroundGridWidth/2;
        if(drawBarList != null || !drawBarList.isEmpty()){
        	//System.out.println(point.x + "   " + point.y);
            for(Bar bar : drawBarList){
                r.set(bar.mPosition-bar.mBarWidth/2,0,bar.mPosition+bar.mBarWidth/2,getHeight()-sideLineLength);
                
                
                
                DebugTool.show(bar.mPosition + "  height:" + getHeight() + "  bottom:" + sideLineLength);
                if (r.contains(point.x,point.y) && event.getAction() == MotionEvent.ACTION_DOWN
                		&& bar.mValue != 0 ){
                    selectedBar = bar;
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
        
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mDownTouch = true;
            return true;

        case MotionEvent.ACTION_UP:
            if (mDownTouch) {
                mDownTouch = false;
                performClick(); // Call this method to handle the response, and
                                // thereby enable accessibility services to
                                // perform this action for a user who cannot
                                // click the touchscreen.
                return true;
            }
        }
        return true;
	}
    
    @Override
    public boolean performClick() {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick();

        // Handle the action for the custom click here

        return true;
    }
    
	/**
     *
     * @param list The ArrayList of Integer with the range of [0-max].
     */
    public void setDataList(ArrayList<Integer> list, int max){
//        targetPercentList = new ArrayList<Float>();
//        if(max == 0) max = 1;
//
//        for(Integer integer : list){
//            targetPercentList.add(1-(float)integer/(float)max);
//        }
//
//        // Make sure percentList.size() == targetPercentList.size()
//        if(percentList.isEmpty() || percentList.size()<targetPercentList.size()){
//            int temp = targetPercentList.size()-percentList.size();
//            for(int i=0; i<temp;i++){
//                percentList.add(1f);
//            }
//        } else if (percentList.size()>targetPercentList.size()){
//            int temp = percentList.size()-targetPercentList.size();
//            for(int i=0; i<temp;i++){
//                percentList.remove(percentList.size()-1);
//            }
//        }
//        setMinimumWidth(2);
//        removeCallbacks(animator);
//        post(animator);
    	
    	this.dataList = list;
        if(dataList.size() > bottomTextList.size()){
            throw new RuntimeException("dacer.LineView error:" +
                    " dataList.size() > bottomTextList.size() !!!");
        }
        //dataOfAGird = 3;
        refreshAfterDataChanged();
        showPopup = false;
        setMinimumWidth(0); // It can help the LineView reset the Width,
                                // I don't know the better way..
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
       
        DrawBackgroundLine(canvas);
        DrawBar(canvas);
        if(showPopup && selectedBar != null){
        	if ( isDoubleValue ){
        		double value = selectedBar.mValue;
        		drawPopup(canvas,
                        String.valueOf(value/valueBEI),
                        selectedBar.getPoint());
        	}
        	else{
        		drawPopup(canvas,
                        String.valueOf(selectedBar.mValue),
                        selectedBar.getPoint());
        	}
        	
        }
    }
    
    private void drawPopup(Canvas canvas,String num, Point point){
        boolean singularNum = (num.length() == 1);
        int sidePadding = MyUtils.dip2px(getContext(),singularNum? 8:5);
        int x = point.x;
        int y = point.y-MyUtils.dip2px(getContext(),5);
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(num,0,num.length(),popupTextRect);
        Rect r = new Rect(x-popupTextRect.width()/2-sidePadding,
                y - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                x + popupTextRect.width()/2+sidePadding,
                y+popupTopPadding-popupBottomMargin);

        NinePatchDrawable popup = (NinePatchDrawable)getResources().
                getDrawable(R.drawable.popup_red);
        popup.setBounds(r);
        popup.draw(canvas);
        canvas.drawText(num, x, y-bottomTriangleHeight-popupBottomMargin, popupTextPaint);
    }
    
    private void DrawBar(Canvas canvas){
         //第一個位置空出來不畫
         for(int j=1; j<drawBarList.size(); j++){
             drawBarList.get(j).drawBarInCava(canvas, fgPaint);
         }

//         if(bottomTextList != null && !bottomTextList.isEmpty()){
//             i = 1;
//             for(String s:bottomTextList){
//                 canvas.drawText(s,BAR_SIDE_MARGIN*i+barWidth*(i-1)+barWidth/2,
//                         mViewHeight-bottomTextDescent,bottomTextPaint);
//                 i++;
//             }
//         }
    }
    private void DrawBackgroundLine(Canvas canvas){
    	Paint paint = new Paint();
    	 paint = new Paint();
         paint.setStyle(Paint.Style.STROKE);
         paint.setStrokeWidth(MyUtils.dip2px(getContext(),1f));
         //paint.setColor(BACKGROUND_LINE_COLOR);
         paint.setColor(getResources().getColor(R.color.center));
         
         canvas.drawLine(xCoordinateList.get(0),//0,
         		topLineLength+positiveArea,
         		getWidth(),
         		topLineLength+positiveArea,
         		paint);
         
         paint.setAlpha(99);
         
         for (int i = 0; i < yCoordinateList.size(); i++) {
 			canvas.drawLine(
 					xCoordinateList.get(0),// 0,
 					yCoordinateList.get(i), getWidth(), yCoordinateList.get(i),
 					paint);
 		}
 		paint.setAlpha(255);

         mLeftNumber.init();
 		for (int i = 0; i < yCoordinateList.size(); i++) {
 			if ( isDoubleValue ){
 				double value = mLeftNumber.getMax() - mLeftNumber.next();
 				canvas.drawText(String.valueOf(value/valueBEI),
 						leftTextMargin+10,// 0,
 						yCoordinateList.get(i)+bottomTextPaint.getTextSize()/2, bottomTextPaint);
 			}
 			else{
 				canvas.drawText(String.valueOf(mLeftNumber.getMax() - mLeftNumber.next()),
 						leftTextMargin+10,// 0,
 						yCoordinateList.get(i)+bottomTextPaint.getTextSize()/2, bottomTextPaint);
 			}
 				
 			//}
 		}
 		
 		if(bottomTextList != null){
            for(int i=0;i<bottomTextList.size();i++){
            	if ( i != 0 )
            	canvas.drawText(bottomTextList.get(i), xCoordinateList.get(i),
                      mViewHeight-bottomTextDescent, bottomTextPaint);
            }
        }
    }
    
    private void refreshAfterDataChanged(){
        int verticalGridNum = getVerticalGridlNum();
        
        //為了整體的整齊，這這裡把最大數字規範化。
        verticalGridNum = (int)NumberCount.getCeiling(verticalGridNum).getMax();
        refreshTopLineLength(verticalGridNum);
        refreshYCoordinateList(verticalGridNum);
        refreshBarList(verticalGridNum);
    }
    
    private void refreshTopLineLength(int verticalGridNum){
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        // But this code not so good.
        if((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin)/
                (verticalGridNum+2)<getPopupHeight()){
            topLineLength = getPopupHeight()+2;
        }else{
            topLineLength = MIN_TOP_LINE_LENGTH;
        }
    }
    
    private void refreshXCoordinateList(int horizontalGridNum){
        xCoordinateList.clear();
        for(int i=0;i<(horizontalGridNum+1);i++){
//            xCoordinateList.add(sideLineLength + backgroundGridWidth*i
//            		+leftTextMaxWidth+leftTextMargin);
//            為保證點和底下文字的位置一致，暫時去掉margin。後續如果需要可以再次加上
        	xCoordinateList.add(sideLineLength + Float.valueOf(backgroundGridWidth*i).intValue()
            		+leftTextMaxWidth);
        }

    }
    
    private void refreshBarList(int verticalGridNum){
    	if ( dataList == null )
    		return;
    	 int drawbarSize = drawBarList.isEmpty()? 0:drawBarList.size();
    	 int barwidth = Float.valueOf(backgroundGridWidth).intValue() - BAR_SIDE_MARGIN;
         //for ( Line line : drawLineList) line.setNotDraw();
         for(int i=0; i<dataList.size() ; i++){
         	int position = xCoordinateList.get(i);
         	int height = positiveArea*(verticalGridNum-dataList.get(i))/verticalGridNum;
             if(i>drawbarSize-1){
            	 drawBarList.add( new Bar(dataList.get(i), height, position, barwidth ) );
             }else{
            	 drawBarList.set(i, drawBarList.get(i).setTargetData(dataList.get(i), height, position));
             }
         }
        
         int temp = drawBarList.size() - dataList.size();
         for(int i=0; i<temp; i++){
        	 drawBarList.remove(drawBarList.size()-1);
         }
         
         removeCallbacks(animator);
         post(animator);
    }
    
    private NumberCount.LeftNumberStruct mLeftNumber;
    private int positiveArea;
    private void refreshYCoordinateList(int verticalGridNum){
//        yCoordinateList.clear();
//        for(int i=0;i<(verticalGridNum+1);i++){
//            yCoordinateList.add(topLineLength +
//                    ((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin-
//                            bottomLineLength-bottomTextDescent)*i/(verticalGridNum)));
//        }
    	
    	//現在yCoor list只記錄左側的提示數字的底端位置信息。
        yCoordinateList.clear();
        positiveArea = (mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin-
                bottomLineLength-bottomTextDescent);
        mLeftNumber = NumberCount.getCeiling(verticalGridNum);
        mLeftNumber.init();
        for(int i=0;i<3;i++){
            yCoordinateList.add(topLineLength +
                    (positiveArea*(int)mLeftNumber.next()/(verticalGridNum)));
        }

    }
    
    private int getPopupHeight(){
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9",0,1,popupTextRect);
        Rect r = new Rect(-popupTextRect.width()/2,
                 - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                 + popupTextRect.width()/2,
                +popupTopPadding-popupBottomMargin);
        return r.height();
    }
    
    private int getVerticalGridlNum(){
        int verticalGridNum = MIN_VERTICAL_GRID_NUM;
        if(dataList != null && !dataList.isEmpty()){
            for(Integer integer:dataList){
                if(verticalGridNum<(integer+1)){
                    verticalGridNum = integer+1;
                }
            }
        }
        return verticalGridNum;
    }

    private int getHorizontalGridNum(){
        int horizontalGridNum = bottomTextList.size()-1;
        if(horizontalGridNum<MIN_HORIZONTAL_GRID_NUM){
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth,mViewHeight);
        refreshAfterDataChanged();
    }

    private int measureWidth(int measureSpec){
//        int preferred = 0;
//        if(bottomTextList != null){
//            preferred = bottomTextList.size()*(barWidth+BAR_SIDE_MARGIN);
//        }
//        return getMeasurement(measureSpec, preferred);
    	int width = MyApplication.getTopApp().getResources().getDisplayMetrics().widthPixels;
        return getMeasurement(measureSpec, width-10);
    }

    private int measureHeight(int measureSpec){
        int preferred = 222;
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
    
    class Bar{
    	
    	int barHeight; 
    	int mBarWidth;
    	int mPosition;
    	int mValue;
    	public Bar(int value, int height, int position, int barwidth){
    		initDatabase(value, height, position);
    		mBarWidth = barwidth;
    		mValue = value;
    	}
    	public void drawBarInCava(Canvas canvas, Paint paint){
    		rect.set( mPosition - mBarWidth/2,
    				topLineLength + barHeight,
                    mPosition + mBarWidth/2,
                    topLineLength+positiveArea);//getHeight()-bottomTextHeight-TEXT_TOP_MARGIN);
            canvas.drawRect(rect,paint);
    	}
    	
    	public Bar setTargetData(int value, int height, int position){
    		initDatabase(value, height, position);
    		return this;
    	}
    	
    	private void initDatabase(int value, Integer barHeight, int position){
    		this.barHeight = barHeight;
    		this.mPosition = position;
    		this.mValue = value;
        }
    	
    	Point getPoint(){
            return new Point(mPosition,topLineLength + barHeight + MyUtils.dip2px(getContext(), 6));
        }
    }
}
