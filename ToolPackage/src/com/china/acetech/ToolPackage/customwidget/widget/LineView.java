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
import com.china.acetech.ToolPackage.java.NumberCount;


/**
 * 具有动画的离散式连线变化示意图。
 *
 * 外部使用需要关心三个方法：
 * </br>
 * clear()、setBottomTextList()、setDataList()
 *  </br>
 * 第一个是重置接口。后两个分别是底部的信息和对应数据的设置接口。
 * </br>动画播放将在方法 {@link LineView#refreshAfterDataChanged()}调用后开始，
 * </br>
 * Created by Ace on 11/4/13.
 */
public class LineView extends View {
    private int mViewHeight;
    //drawBackground
    private boolean autoSetDataOfGird = true;
    private boolean autoSetGridWidth = true;
    private int dataOfAGird = 10;
    private int bottomTextHeight = 0;
    private ArrayList<String> bottomTextList;
    private ArrayList<Integer> dataList;
    private ArrayList<Float> xCoordinateList = new ArrayList<Float>();
    private ArrayList<Integer> yCoordinateList = new ArrayList<Integer>();
    private ArrayList<Dot> drawDotList = new ArrayList<Dot>();;
    private Paint bottomTextPaint = new Paint();
    private Paint leftTextPaint;
    private int bottomTextDescent;

    //popup
    private Paint popupTextPaint = new Paint();
    private final int bottomTriangleHeight = 12;
    private boolean showPopup = false;
    private Dot selectedDot;

    private int topLineLength = MyUtils.dip2px(getContext(), 12);; //padding to view's top bound.
    private int sideLineLength = MyUtils.dip2px(getContext(), 30)/3*4;// padding to view's left bound.
    private float backgroundGridWidth = MyUtils.dip2px(getContext(),45)*1.0f;	//Grid Node's width and height.

    //Constants
    private final int popupTopPadding = MyUtils.dip2px(getContext(),2);
    private final int popupBottomMargin = MyUtils.dip2px(getContext(),5);
    private final int bottomTextTopMargin = MyUtils.sp2px(getContext(),5);
    private final int bottomLineLength = MyUtils.sp2px(getContext(), 12);
    private final int leftTextMargin = MyUtils.sp2px(getContext(), 5)+23;//add by ace for left number
    private final int leftTextMaxWidth = 0;//add by ace for left number
    private final int DOT_INNER_CIR_RADIUS = MyUtils.dip2px(getContext(), 2);
    private final int DOT_OUTER_CIR_RADIUS = MyUtils.dip2px(getContext(),5);
    private final int MIN_TOP_LINE_LENGTH = MyUtils.dip2px(getContext(),12);
    private final int MIN_VERTICAL_GRID_NUM = 4;
    private final int MIN_HORIZONTAL_GRID_NUM = 1;
    //private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");
    private final int BOTTOM_TEXT_COLOR = Color.parseColor("#9B9A9B");

    
    private boolean isDoubleValue = false;
    private double valueBEI = 100.0;
    
    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for(Dot dot : drawDotList){
                dot.update();
                if(!dot.isAtRest()){
                    needNewFrame = true;
                }
            }
            
            //needNewFrame = false;
            for(Line line : drawLineList){
                line.update();
                if(!line.isAtRest()){
                    needNewFrame = true;
                }
            }
            
            
            if (needNewFrame) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };

    public LineView(Context context){
        this(context,null);
    }
    public LineView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    /**
     * clear all data in this view
     */
    public void clear(){
    	init();
    	this.dataList = null;
    	this.bottomTextList = null;
    	this.xCoordinateList = new ArrayList<Float>();
    	this.yCoordinateList = new ArrayList<Integer>();
    	this.drawDotList = new ArrayList<Dot>();
    	
    	showPopup = false;
        isDoubleValue = false;
        valueBEI = 100.0;
    }
    
    /**
     * initial all value 
     */
    public void init(){
    	
    	popupTextPaint = new Paint();
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(MyUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint = new Paint();
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(),12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);
        
        
    }
    
    /**
     * dataList will be reset when called is method.
     * @param bottomTextList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomTextList){
        this.dataList = null;
        this.bottomTextList = bottomTextList;

        Rect r = new Rect();
        int longestWidth = 0;
        String longestStr = "";
        //bottomTextDescent = 0; 讓text和底部不是貼的太緊
        bottomTextDescent = MyUtils.sp2px(getContext(),10);
        for(String s:bottomTextList){
            bottomTextPaint.getTextBounds(s,0,s.length(),r);
            if(bottomTextHeight<r.height()){
                bottomTextHeight = r.height();
            }
            if(autoSetGridWidth&&(longestWidth<r.width())){
                longestWidth = r.width();
                longestStr = s;
            }
            if(bottomTextDescent<(Math.abs(r.bottom))){
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        
        if(autoSetGridWidth){
            if(backgroundGridWidth<longestWidth){
                backgroundGridWidth = longestWidth+(int)bottomTextPaint.measureText(longestStr,0,1);
            }
            if(sideLineLength<longestWidth/2){
                sideLineLength = longestWidth/2;
            }
        }
        
        //新增的計算辦法，根據bottom需要顯示的數量來決定間隔，保證全部顯示在一個屏幕內
        int number = bottomTextList.size();
        if ( number > 25 ) number -=1;
        int length = MyApplication.getTopApp().getResources().getDisplayMetrics().widthPixels;
        backgroundGridWidth = (length - leftTextMargin - sideLineLength)*1.0f/number;
        
        leftTextPaint = new Paint(bottomTextPaint);
        leftTextPaint.setTextSize(MyUtils.sp2px(getContext(), 12));
        if ( number > 25 ){
        	bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(),9));
        }
        else{
        	bottomTextPaint.setTextSize(MyUtils.sp2px(getContext(),12));
        }

        refreshXCoordinateList(getHorizontalGridNum());
    }

    /**
     *
     * @param dataList The Integer ArrayList for showing,
     *                 dataList.size() must < bottomTextList.size()
     */
    @SuppressWarnings("unchecked")
	public void setDataList(ArrayList<?> dataList){
    	valueBEI = 100;
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
        if(autoSetDataOfGird){
            int biggestData = 0;
            for(Integer i:this.dataList){
                if(biggestData<i){
                    biggestData = i;
                }
            }
            dataOfAGird = 1;
            while(biggestData/3 > dataOfAGird){
                dataOfAGird *= 3;
            }
        }
        //dataOfAGird = 3;
        refreshAfterDataChanged();
        showPopup = false;
        setMinimumWidth(0); // It can help the LineView reset the Width,
                                // I don't know the better way..
        postInvalidate();
    }

    private void refreshAfterDataChanged(){
        int verticalGridNum = getVerticalGridlNum();
        
        //為了整體的整齊，這這裡把最大數字規範化。
        verticalGridNum = (int) NumberCount.getCeiling(verticalGridNum).getMax();
        refreshTopLineLength(verticalGridNum);
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
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

    private void refreshXCoordinateList(int horizontalGridNum){
        xCoordinateList.clear();
        for(int i=0;i<(horizontalGridNum+1);i++){
//            xCoordinateList.add(sideLineLength + backgroundGridWidth*i
//            		+leftTextMaxWidth+leftTextMargin);
//            為保證點和底下文字的位置一致，暫時去掉margin。後續如果需要可以再次加上
        	xCoordinateList.add(sideLineLength + backgroundGridWidth*i
            		+leftTextMaxWidth);
        }

    }

    //修改一下整個算法。
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

    private void refreshDrawDotList(int verticalGridNum){
        if(dataList != null && !dataList.isEmpty()){
            int drawDotSize = drawDotList.isEmpty()? 0:drawDotList.size();
            for(int i=0;i<dataList.size();i++){
                int x = xCoordinateList.get(i).intValue() ;
                //int y = yCoordinateList.get(verticalGridNum - dataList.get(i));
                int y = topLineLength+positiveArea*(verticalGridNum-dataList.get(i))/verticalGridNum;//重新修改了下y的獲取方法，讓其和整個list無關聯化
                if(i>drawDotSize-1){
                    drawDotList.add(new Dot(x, 0, x, y, dataList.get(i)));
                }else{
                    drawDotList.set(i, drawDotList.get(i).setTargetData(x,y,dataList.get(i)));
                }
            }
            int temp = drawDotList.size() - dataList.size();
            for(int i=0; i<temp; i++){
                drawDotList.remove(drawDotList.size()-1);
            }
        }
        
        //Replaced by ace 
        //暫時把線條的刷新放這裡
        int drawLineSize = drawLineList.isEmpty()? 0:drawLineList.size();
        for ( Line line : drawLineList) line.setNotDraw();
        for(int i=0, j=0 ; i+1<drawDotList.size() ; i++){
        	
        	j = i+1;
            if(i>drawLineSize-1){
                drawLineList.add( new Line(drawDotList.get(i), drawDotList.get(j)) );
            }else{
                drawLineList.set(i, drawLineList.get(i).setTargetData(
                											drawDotList.get(i), 
                											drawDotList.get(j))
                											);
            }
            
            if ( i == 0){
            	 drawLineList.get(i).setPreLine(null);
            	 //drawLineList.get(i).setNotDraw();//取消原點至第一個位置的線條
            }
            else{
            	drawLineList.get(i).setPreLine(drawLineList.get(i-1));
            }
            
            drawDotList.get(i+1).setPreLine(drawLineList.get(i));
        }
       
        int temp = drawDotList.size() - drawLineList.size() - 1;
        for(int i=0; i<temp; i++){
        	drawLineList.remove(drawLineList.size()-1);
        }
        
        removeCallbacks(animator);
        post(animator);
    }

    private void refreshTopLineLength(int verticalGridNum){
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        // But this code not so good.
        if((mViewHeight-topLineLength-bottomTextHeight-bottomTextTopMargin)/
                (verticalGridNum+2)<getPopupHeight()){
            topLineLength = getPopupHeight()+DOT_OUTER_CIR_RADIUS+DOT_INNER_CIR_RADIUS+2;
        }else{
            topLineLength = MIN_TOP_LINE_LENGTH;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackgroundLines(canvas);
        drawLines(canvas);
        drawDots(canvas);
        if(showPopup && selectedDot != null){
        	if ( isDoubleValue ){
        		double value = selectedDot.data;
        		drawPopup(canvas,
                        String.valueOf(value/valueBEI),
                        selectedDot.getPoint());
        	}
        	else{
        		drawPopup(canvas,
                        String.valueOf(selectedDot.data),
                        selectedDot.getPoint());
        	}
            
        }
    }

    /**
     *
     * @param canvas  The canvas you need to draw on.
     * @param point   The Point consists of the x y coordinates from left bottom to right top.
     *                Like is ↓
     *                3
     *                2
     *                1
     *                0 1 2 3 4 5
     */
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

    private int getPopupHeight(){
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9",0,1,popupTextRect);
        Rect r = new Rect(-popupTextRect.width()/2,
                 - popupTextRect.height()-bottomTriangleHeight-popupTopPadding*2-popupBottomMargin,
                 + popupTextRect.width()/2,
                +popupTopPadding-popupBottomMargin);
        return r.height();
    }

    private void drawDots(Canvas canvas){
        if(drawDotList!=null && !drawDotList.isEmpty()){

            for(Dot dot : drawDotList){
                //canvas.drawCircle(dot.x,dot.y,DOT_OUTER_CIR_RADIUS,bigCirPaint);
                //canvas.drawCircle(dot.x,dot.y,DOT_INNER_CIR_RADIUS,smallCirPaint);
            	
            	//replace by ace
            	//暫時封掉，不讓點可以進行移動
            	//canvas.drawCircle(dot.targetX,dot.targetY,DOT_OUTER_CIR_RADIUS,bigCirPaint);
                //canvas.drawCircle(dot.targetX,dot.targetY,DOT_INNER_CIR_RADIUS,smallCirPaint);

            	dot.drawDotInCava(canvas);
            }
        }
    }

    private void drawLines(Canvas canvas){
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#2ac3c5"));
        linePaint.setStrokeWidth(MyUtils.dip2px(getContext(), 2)*2);
//        for(int i=0; i<drawDotList.size()-1; i++){
//            canvas.drawLine(drawDotList.get(i).x,
//                    drawDotList.get(i).y,
//                    drawDotList.get(i+1).x,
//                    drawDotList.get(i+1).y,
//                    linePaint);
//        }
        for(int i=0; i<drawLineList.size(); i++){
            drawLineList.get(i).drawLineInCava(canvas, linePaint);
        }
    }

    private void drawBackgroundLines(Canvas canvas){
        Paint paint = new Paint();
        
        
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(MyUtils.dip2px(getContext(),1f));
//        paint.setColor(BACKGROUND_LINE_COLOR);
//        PathEffect effects = new DashPathEffect(
//                new float[]{10,5,10,5}, 1);
//
//        //draw vertical lines
//
//        int length = yCoordinateList.get(yCoordinateList.size()-1);
//        int start = length/6;
//        int end = length - start;
//        Shader sha = new LinearGradient(0, start, 0, end, new int[]{
//        		getResources().getColor(R.color.start), 
//        		getResources().getColor(R.color.center),
//        		getResources().getColor(R.color.end)}, null,
//        Shader.TileMode.CLAMP);
//        paint.setShader(sha);
//        
//        for(int i=0;i<xCoordinateList.size();i++){
//            canvas.drawLine(xCoordinateList.get(i),
//                    0,
//                    xCoordinateList.get(i),
//                    //mViewHeight - bottomTextTopMargin - bottomTextHeight-bottomTextDescent,
//                    yCoordinateList.get(yCoordinateList.size()-1),
//                    paint);
//        }
//        
//        paint.setShader(null);
//        
//
//        for(int i=0;i<yCoordinateList.size();i++){
//            if((yCoordinateList.size()-1-i)%dataOfAGird == 0){
//                canvas.drawLine(xCoordinateList.get(0),//0,
//                		yCoordinateList.get(i),
//                		getWidth(),
//                		yCoordinateList.get(i),
//                		paint);
//            }
//        }
        
        
        //以下均為為了手環而採用的新的使用方式
        //首先畫兩條坐標線。
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MyUtils.dip2px(getContext(),1f));
        //paint.setColor(BACKGROUND_LINE_COLOR);
        paint.setColor(getResources().getColor(R.color.center));
        //兩條線分別是x的第一條線和y的最後一條線。 最新要求，第一條豎線不再需要畫
//        canvas.drawLine(xCoordinateList.get(0),
//                topLineLength,
//                xCoordinateList.get(0),
//                //mViewHeight - bottomTextTopMargin - bottomTextHeight-bottomTextDescent,
//                topLineLength+positiveArea,
//                paint);
        canvas.drawLine(xCoordinateList.get(0),//0,
        		topLineLength+positiveArea,
        		getWidth(),
        		topLineLength+positiveArea,
        		paint);
        //然後畫背景的裝飾線
        
//        int length = topLineLength+positiveArea;
//        int start = length/9 + topLineLength;
//        int end = length - start + topLineLength;
//        Shader sha = new LinearGradient(0, start, 0, end, new int[]{
//        		getResources().getColor(R.color.start), 
//        		getResources().getColor(R.color.center),
//        		getResources().getColor(R.color.end)}, null,
//        Shader.TileMode.CLAMP);
//        paint.setShader(sha);
        paint.setAlpha(99);
        
//        for(int i=1;i<xCoordinateList.size();i++){
//            canvas.drawLine(xCoordinateList.get(i),
//            		topLineLength,
//                    xCoordinateList.get(i),
//                    //mViewHeight - bottomTextTopMargin - bottomTextHeight-bottomTextDescent,
//                    topLineLength+positiveArea,
//                    paint);
//        }

        //取消豎線，改畫橫線
		for (int i = 0; i < yCoordinateList.size(); i++) {
			canvas.drawLine(
					xCoordinateList.get(0),// 0,
					yCoordinateList.get(i), getWidth(), yCoordinateList.get(i),
					paint);
		}
		paint.setAlpha(255);

        mLeftNumber.init();
		for (int i = 0; i < yCoordinateList.size(); i++) {
			//if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {
//              canvas.drawLine(xCoordinateList.get(0),//0,
//        		yCoordinateList.get(i),
//        		getWidth(),
//        		yCoordinateList.get(i),
//        		bottomTextPaint);
			if ( isDoubleValue ){
				double value = mLeftNumber.getMax() - mLeftNumber.next();
				canvas.drawText(String.valueOf(value/valueBEI),
						leftTextMargin,// 0,
						yCoordinateList.get(i)+bottomTextPaint.getTextSize()/2, leftTextPaint);
			}
			else{
				canvas.drawText(String.valueOf(mLeftNumber.getMax() - mLeftNumber.next()),
						leftTextMargin,// 0,
						yCoordinateList.get(i)+bottomTextPaint.getTextSize()/2, leftTextPaint);
			}
				
			//}
		}
        //最後畫描述文字 由於曲線圖不是由原點開始，所以第一個文字需要畫在除Y軸外的第一個位置。
        //draw bottom text
        if(bottomTextList != null){
            for(int i=0;i<bottomTextList.size();i++){
//                canvas.drawText(bottomTextList.get(i), sideLineLength+backgroundGridWidth*i,
//                        mViewHeight-bottomTextDescent, bottomTextPaint);
            	
//            	canvas.drawText(bottomTextList.get(i), sideLineLength+backgroundGridWidth*(i+1),
//                        mViewHeight-bottomTextDescent, bottomTextPaint);
            	canvas.drawText(bottomTextList.get(i), xCoordinateList.get(i),
                      mViewHeight-bottomTextDescent, bottomTextPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        refreshAfterDataChanged();
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

    boolean mDownTouch = false;
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        Region r = new Region();
        int width = Float.valueOf(backgroundGridWidth).intValue()/2;
        if(drawDotList != null || !drawDotList.isEmpty()){
            for(Dot dot : drawDotList){
                r.set(dot.x-width,dot.y-width,dot.x+width,dot.y+width);
                if (r.contains(point.x,point.y) && event.getAction() == MotionEvent.ACTION_DOWN){
                    selectedDot = dot;
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
        //這裡return true並不會影響到上一層的消息處理。
        //其原因是，Ontouch return true後，僅僅是Touch事件不會向上傳遞，event仍然會經由Interrupt來判斷是否需要進行截獲
        //所以即使這裡的ontouch已經return true。上一層view仍然可以在需要的時候進行截獲。
        //換句話說，View層級越高，對event越有掌控能力
        //當然。dispatch方法是在這些處理之前的，不過不是太建議在這裡進行處理。
    }

    @Override
    public boolean performClick() {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick();

        // Handle the action for the custom click here

        return true;
    }
    
    private int updateSelf(int origin, int target, int velocity){
        if (origin < target) {
            origin += velocity;
        } else if (origin > target){
            origin-= velocity;
        }
        if(Math.abs(target-origin)<velocity){
            origin = target;
        }
        return origin;
    }
    
    class Dot{
        int x;
        int y;
        int data;
        int targetX;
        int targetY;
        int velocity = MyUtils.dip2px(getContext(),10);
        
        float dotvelocity = (DOT_OUTER_CIR_RADIUS+4)/10.0f;
        float small_dotvelocity = (DOT_INNER_CIR_RADIUS+5)/10.0f;
        float current_ratus;
        float small_ratus;
        float last_ratus;
        
        Line mPreLine;

        Dot(int x,int y,int targetX,int targetY,Integer data){
            this.x = x;
            this.y = y;
            initDatabase(targetX, targetY, data);
            //setTargetData(targetX, targetY,data);
            
        }

        Point getPoint(){
            return new Point(x,y);
        }

        Dot setTargetData(int targetX, int targetY, Integer data){
        	initDatabase(targetX, targetY, data);
            return this;
        }
        
        private void initDatabase(int targetX, int targetY, Integer data){
        	this.targetX = targetX;
            this.targetY = targetY;
            this.data = data;
            
            current_ratus = 0;
            small_ratus = 0;
            last_ratus = 0;
            
            initPaint();
        }
        
        void setPreLine(Line preLine){
        	mPreLine = preLine;
        }

        void drawDotInCava(Canvas canvas){
            
        	if ( Math.abs(small_ratus-DOT_INNER_CIR_RADIUS-7.0f) < precision){
            canvas.drawCircle(targetX, targetY, current_ratus, mBigCirPaint);
            canvas.drawCircle(targetX, targetY, small_ratus, mSmallCirPaint);
        	}
            canvas.drawCircle(targetX, targetY, last_ratus, mBigCirPaint);
            
        }
        
        boolean isAtRest(){
        	return Math.abs(current_ratus-DOT_OUTER_CIR_RADIUS-4) < precision;
            //return (x==targetX)&&(y==targetY);
        }

        void update(){
        	if ( mPreLine != null && !mPreLine.isAtRest() )
        		return;
        	        	
            x = updateSelf(x, targetX, velocity);
            y = updateSelf(y, targetY, velocity);
            
            if ( targetX == 30 ){
            	System.out.println("");
            }
            //current_ratus += dotvelocity;
            //small_ratus += small_dotvelocity;
            current_ratus = updateDotRadius(current_ratus, small_dotvelocity, DOT_OUTER_CIR_RADIUS+4);
            small_ratus = updateDotRadius(small_ratus, small_dotvelocity, DOT_INNER_CIR_RADIUS+7.0f);
            last_ratus = updateDotRadius(last_ratus, small_dotvelocity, DOT_INNER_CIR_RADIUS+5.0f);
        }
        
        
        public float getPositionX(){
        	return targetX;
        }
        
        public float getPositionY(){
        	return targetY;
        }
        
        private float updateDotRadius(float current, float velocity, float goal){
        	return updateProcess(current, velocity, goal);
        }
        
       
        

        private Paint mBigCirPaint;
        private Paint mSmallCirPaint;
        
        private void initPaint(){
			if (mBigCirPaint == null) {
				mBigCirPaint = new Paint();
				mBigCirPaint.setAntiAlias(true);
				mBigCirPaint.setColor(Color.parseColor("#2ac3c5"));

				mSmallCirPaint = new Paint(mBigCirPaint);
				mSmallCirPaint.setColor(Color.parseColor("#FFFFFF"));
			}
        }
        
        public void setDotColor(int color){
        	mBigCirPaint.setColor(color);
        }
        
    }
    
      
    
    
    private ArrayList<Line> drawLineList = new ArrayList<Line>();
    private int LineView_velocity = (MyApplication.getTopApp().getResources().getDisplayMetrics().widthPixels)
    								/    (MyUtils.dip2px(getContext(),45));
    class Line{
         int velocity = LineView.this.LineView_velocity;
    	//int velocity = MyUtils.dip2px(getContext(),10);
         
         float velocityX;
         float velocityY;
         
         Dot mStart;
         Dot mTarget;
         
         float currentX;
         float currentY;
         
         Line mPreLine;

         Line(Dot startDot, Dot targetDot){
        	 initDatabase(startDot, targetDot);
        	 
         }

         Line setTargetData(Dot startDot, Dot targetDot){
        	 initDatabase(startDot, targetDot);
             return this;
         }
         
         void setPreLine(Line preLine){
        	 mPreLine = preLine;
         }
         
         private void initDatabase(Dot s, Dot t){
        	 mStart = s;
        	 mTarget = t;
        	 currentX = mStart.getPositionX();
        	 currentY = mStart.getPositionY();
        	 
//        	 velocityX = (mTarget.getPositionX() - mStart.getPositionX())/velocity;
//        	 velocityY = (mTarget.getPositionY() - mStart.getPositionY())/velocity;
        	 velocityX = velocity;
        	 velocityY = velocityX * ((mTarget.getPositionY() - mStart.getPositionY())/
        			 (mTarget.getPositionX() - mStart.getPositionX()));
         }

         boolean isAtRest(){
             return ( Math.abs(currentX-mTarget.getPositionX()) < precision)&&
            		 (  Math.abs(currentY-mTarget.getPositionY())< precision ) ;
         }

         void update(){
        	 if ( mPreLine != null && !mPreLine.isAtRest() )
        		 return;
             currentX = updateLineByTime(currentX, mTarget.getPositionX(), velocityX);
             currentY = updateLineByTime(currentY, mTarget.getPositionY(), velocityY);
         }
         
         
         public void drawLineInCava(Canvas canvas, Paint linePaint){

        	 if ( mPreLine != null && !mPreLine.isAtRest())
        		 return;
        	 
        	// linePaint.setStrokeWidth(linePaint.getStrokeWidth()*3);
        	 canvas.drawLine(mStart.getPositionX(),
        			 mStart.getPositionY(),
                     currentX,
                     currentY,
        			 //mTarget.getPositionX(),
        			 //mTarget.getPositionY(),
                     linePaint);
         }
         
         private float updateLineByTime(float current, float target, float velocity){
             return updateProcess(current, velocity, target);
         }
         
         public void setNotDraw(){
        	 setTargetData(mStart, mStart);
         }
         
         
    }
    private float updateProcess(float current, float velocity, float goal){
    	if ( current == goal )
    		return current;
    	
    	//if ( (goal - current) < velocity && (goal - current) * velocity <= 0 )
    	if ( Math.abs(goal-current) < Math.abs(velocity) )
    		current = goal;
    	else
    		current += velocity;
    	
    	return current;
    }
    private static final float precision = 0.001f;
}
