package com.china.acetech.ToolPackage.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapTool {


	public static Bitmap getResizeBitmap(String filePath, int scaleValue){
		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//		int imageHeight = options.outHeight;
//		int imageWidth = options.outWidth;
//		options.inJustDecodeBounds = false;
		// recreate the stream
		// make some calculation to define inSampleSize
		options.inSampleSize = scaleValue;
		return BitmapFactory.decodeFile(filePath, options);
	}

}
