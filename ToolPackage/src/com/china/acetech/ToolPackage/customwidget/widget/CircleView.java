package com.china.acetech.ToolPackage.customwidget.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.debug.DebugTool;

/**
 * 动态变化并具有动画效果的进度图，外层的实线表示了进度。
 *</br>
 * 需要关注setImageResourceId、setDrawColor方法</br>
 * 启动动画使用start接口
 *</br>
 * 相关资源有：R.drawable.step; R.color.step_line;R.color.step_circle;
 *
 */
public class CircleView extends View{

	private int mImageId = -1;
	float padding = 0;
	
	public CircleView(Context context) {
		super(context);
	}
	
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray attrType = context.obtainStyledAttributes(attrs,
				R.styleable.Image);
		try {
			mImageId = attrType.getResourceId(R.styleable.Image_src, -1);

		} 
		catch(Exception e){
			e.printStackTrace();
			throw (RuntimeException)e;
		}finally {
			attrType.recycle();
		}
		
//		((MainActivity)getContext()).getRegisterable().addListener(
//				PropertyRegisterable.ACTIVITY_ACTIVE, mListener);
		init();
	}
	
	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	//Add by ace
	public void setImageResourceId( int resId){
		mImageId = resId;
		init();
	}
	boolean isStart = false;
	
	public void start(){
		isStart = true;
		invalidate();
	}
	
	public void countSweepAngle(float current, float goal){
		DebugTool.show("cur:" + current + "\t\t\tgoal:" + goal + "\t\t\tres:" + (current * 360 * 1f) / goal);
		
		if ( goal < 0 ){
			goal = 9;
			current = 0;
		}
		if ( current > goal )
			current = goal;

		if ( goal == 0 )
			drawCircleStruct.setResultSweepAngle( 0.0f);
		else
			drawCircleStruct.setResultSweepAngle((current*360*1f)/goal);
	}
	public void countSweepAngle(){
		
	}
	public void setSweepAngle(){
		
	}
	
	/**
	 * 
	 * all params must be color!!!!
	 *
	 * @param LineColor
	 * @param CircleColor
	 */
	public void setDrawColor( int LineColor, int CircleColor){
		drawCircleStruct.linePaint.setColor(LineColor);
		drawCircleStruct.circlePaint.setColor(CircleColor);
	}
	
	private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            
            if ( !isStart ) return;
            
            //drawCircleStruct.sweep += DrawCircleStruct.SWEEP_INC;
            drawCircleStruct.addSweepAngle(CircleView.this.getTag());
            
            //if ( drawCircleStruct.getSweepAngle() <= drawCircleStruct.getResultSweepAngle()  ){
            if ( !drawCircleStruct.isOverMaxSweep() ){
            	needNewFrame = true;
            }
            else{
            	CircleView.this.removeCallbacks(animator);
            	animator = null;
            	invalidate();//不刷新則最後一幀無法繪製上去
            	return;
            }
            
            
            if (needNewFrame) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };
	

	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		float leftstart = (getWidth() - drawCircleStruct.getPhotoWidth())/2;
		float topstart = (getHeight() - drawCircleStruct.getPhotoHeight())/2;
		drawCircleStruct.updatePhoto(animator == null || !isStart);
		canvas.drawBitmap(drawCircleStruct.getPhoto(), padding+leftstart, padding+topstart, null);
		
		if ( animator != null ){
			removeCallbacks(animator);
	        post(animator);
		}
	}



	
	//private SurfaceHolder shd;
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
//		((MainActivity)getContext()).getRegisterable().removeListener(mListener);
//		mListener = null;
		
		if ( drawCircleStruct != null )
			drawCircleStruct.clear();
	}

	private void init(){
		DisplayMetrics display = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(display);
		
		
		if ( mImageId != -1 ){
			//这里的处理是为了防止重复读取资源导致的速度下降，所以使用了一个holder来存储已经加载的图片
			//可以直接去getdrawable。详细的操作可以看底部已经被注释的代码
			drawCircleStruct = new DrawCircleStruct(
					/*DrawableHolder.getDrawable(mImageId).getBitmap()*/
					((BitmapDrawable)getContext().getResources().getDrawable(mImageId)).getBitmap()
					, display.scaledDensity);
		}
		
	}
	
	//暫時先將pause時期釋放圖片的邏輯封掉，現在的做法是當view被回收的時候再去釋放圖片資源。有機會了可以測試一下。
//	PropertyChangeListener mListener =new PropertyChangeListener(){
//		@Override
//		public void propertyChange(PropertyChangeEvent event) {
//			Object o = event.getNewValue();
//			if ( (Boolean)o == PropertyRegisterable.ACTIVE ){
//				;
//			}
//			else{
//				//在這裡響應pause消息以後仍然會去調用View的draw方法，很是奇怪。暫時
//				//使用線程延時進行釋放吧
//				Thread run =new Thread(){
//
//					@Override
//					public void run() {
////						try {
////							sleep(2000);
////						} catch (InterruptedException e) {
////							e.printStackTrace();
////						}
//						drawCircleStruct.clear();
//					}
//					
//				};
//				run.start();
//				run = null;
//				CircleView.this.removeCallbacks(animator);
//            	animator = null;
//			}
//			
//		}
//	};
	
	DrawCircleStruct drawCircleStruct;
	//DrawCircleStruct drawCircleStruct = new DrawCircleStruct(((BitmapDrawable)getResources().getDrawable(R.drawable.test)).getBitmap());
	//DrawThread drawThread = new DrawThread();


	private static class DrawCircleStruct{
		public static final float SWEEP_INC = 1;
		public static final float START_INC = 15;
		public Bitmap frontBitmap;
		private Bitmap board;
		private Canvas boardCanvas;
		//private boolean direction;
		private RectF drawRectF;
		private RectF drawRect_lineAndCircle;
		private RectF drawRect_circle;
		private float start;
		private float sweep;
		private static final int EXT_LEN = 1;
		private static final int LINE_LEN = 1;
		private static final int CIR_LEN = 2;
		public DrawCircleStruct(Bitmap frontImageBitmap, float scaleDen) {

			int ext_len = EXT_LEN * (int)scaleDen;
			int cir_len = CIR_LEN * (int)scaleDen;
			int line_len = LINE_LEN * (int)scaleDen;

			frontBitmap = frontImageBitmap;
			drawRectF = new RectF(0, 0,
					frontBitmap.getWidth() + ext_len,
					frontBitmap.getHeight() + ext_len);
			drawRect_lineAndCircle = new RectF(line_len, line_len,
					frontBitmap.getWidth() + ext_len - line_len,
					frontBitmap.getHeight() + ext_len - line_len);

			drawRect_circle = new RectF(cir_len +3 , cir_len+3,
					frontBitmap.getWidth() + ext_len - cir_len-3,
					frontBitmap.getHeight() + ext_len - cir_len-3);
			board = Bitmap.createBitmap(Math.round(drawRectF.width()) + 1,
					Math.round(drawRectF.height()) + 1,
					Bitmap.Config.ARGB_8888);
			boardCanvas = new Canvas(board);
			start = 270;
			sweep = 0;


//這個是畫一個圓餅，但是事實上可以不用這麼麻煩。
//		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		circlePaint.setStyle(Style.FILL);
//		circlePaint.setAntiAlias(true);
//		circlePaint.setColor(Color.BLUE);

			circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			circlePaint.setStyle(Style.STROKE);
			circlePaint.setAntiAlias(true);
			circlePaint.setColor(Color.BLACK);
			circlePaint.setStrokeWidth(6*scaleDen);
//	    paint.setStrokeWidth(DrawCircleStruct.EXT_LEN/2 - 1);

			linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			linePaint.setStyle(Style.STROKE);
			linePaint.setAntiAlias(true);
			linePaint.setColor(Color.BLACK);
			linePaint.setStrokeWidth(1*scaleDen);

//負責繪製空的表面，暫時無用了
//		emptyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		emptyPaint.setStyle(Style.FILL);
//		emptyPaint.setAntiAlias(true);
//		emptyPaint.setColor(Color.WHITE);



		}


		public boolean isOverMaxSweep(){
			if ( (int)sweep >= (int) resSweep ){
				sweep = resSweep;
				return true;
			}
			else
				return false;
		}

		public void updatePhoto(boolean isStopAnim){
			//boardCanvas.drawColor(Color.WHITE);
			Paint pain = new Paint();
			pain.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			boardCanvas.drawPaint(pain);
			int offset = 0;
			if ( sweep == 0 )			offset = 0;
			else						offset = 5;

			if ( isStopAnim || sweep > resSweep )	sweep = resSweep;

			if ( sweep -360 >=0 && sweep-360<1 ){
				boardCanvas.drawArc(drawRect_lineAndCircle, 0, 0, false, linePaint);	//Draw line
				boardCanvas.drawArc(drawRect_circle, start, sweep, false, circlePaint);			//Draw circle
			}
			else{
				boardCanvas.drawArc(drawRect_lineAndCircle, start+sweep+5, 360-sweep-offset, false, linePaint);	//Draw line
				boardCanvas.drawArc(drawRect_circle, start+2, sweep, false, circlePaint);			//Draw circle
			}
			//boardCanvas.drawArc(drawRect_empty, 0, 360, true, emptyPaint);			//Draw empty circle
//		drawCircleStruct.boardCanvas.drawCircle(drawCircleStruct.drawRectF.width()/2, drawCircleStruct.drawRectF.height()/2, drawCircleStruct.drawRectF.width()/2, paint);
			boardCanvas.drawBitmap(frontBitmap, drawRectF.left + DrawCircleStruct.EXT_LEN/2, drawRectF.top + DrawCircleStruct.EXT_LEN/2, null);
		}

		public float getPhotoLeft(){
			return drawRectF.left;
		}
		public float getPhotoTop(){
			return drawRectF.top;
		}
		public float getPhotoWidth(){
			return drawRectF.width();
		}
		public float getPhotoHeight(){
			return drawRectF.height();
		}

		public Bitmap getPhoto(){
			return board;
		}

		public float getSweepAngle(){
			return sweep;
		}

		public float getResultSweepAngle(){
			return resSweep;
		}
		private float resSweep;
		public void setResultSweepAngle(float setSweep){
			resSweep = setSweep;
		}

		public void addSweepAngle(Object o){
			sweep += getAddAngle(sweep);
		}

		private float getAddAngle(float current){

			int add = ((int)(current+0.5f)) / 30 + 3;
			if ( current >= 330 )
				add = 15;
			return add;
		}

		public Paint circlePaint;
		public Paint linePaint;


		public void clear(){
			if ( frontBitmap != null ){
				//frontBitmap.recycle();
				//frontBitmap = null;
			}
		}

	}
}




//public class DrawableHolder {
//
//	private static  SparseArray<BitmapDrawable> imageMap = new SparseArray<BitmapDrawable>();
//	public static BitmapDrawable getDrawable(int imageId){
//		BitmapDrawable image;
//		image = imageMap.get(imageId);
//		if ( image == null ){
//			image = (BitmapDrawable)MyApplication.getTopApp().getResources()
//					.getDrawable(imageId);
//			imageMap.append(imageId, image);
//		}
//
//		return image;
//	}
//}