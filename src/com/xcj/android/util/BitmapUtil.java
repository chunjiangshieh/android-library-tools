package com.xcj.android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;  
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;  
import android.graphics.Shader.TileMode; 
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 图片特效处理
 * @author chunjiang.shieh
 *
 */
public class BitmapUtil {
	
	/**
	 * 图片缩放
	 * @param bgimage 原图片资源
	 * @param newWidth 缩放后宽度
	 * @param newHeight 缩放后高度
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bgimage,int newWidth,int newHeight){
		//获取这个图片的宽和高
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		
		//创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		//计算缩放率，新尺寸/原始尺寸
		float scaleWidth = ((float)newWidth)/width;
		float scaleHeight = ((float)newHeight)/height;
		//缩放图片的动作
		matrix.postScale(scaleWidth, scaleHeight);
		//在原有位图的基础上，缩放原位图，创建一个新的位图
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height, matrix, true);
		return bitmap;
	}
	
	
	
	/**
	 * Drawable转化为Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable){
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		//建立对应bitmap
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				// 取drawable的颜色格式
				drawable.getOpacity()!=PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
		//建立对应bitmap的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		// 把drawable内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * Bitmap转化为Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap){
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}
	
	/**
	 * Byte转Bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap bytes2Bitmap(byte[] b){  
		if(b.length!=0){  
			return BitmapFactory.decodeByteArray(b, 0, b.length);  
		}  
		else {  
			return null;  
		}  
	}  
	
	/**
	 * Bitmap 转 byte
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	
	/**
	 * 获得圆角图片
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		//给画笔加上抗锯齿
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	
	
	/**
	 * 获得带倒影的图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap){
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		//创建矩阵对象   
		Matrix matrix = new Matrix();
		//指定矩阵(x轴不变，y轴相反) 
		matrix.preScale(1, -1);
		//将矩阵应用到该原图之中，返回一个宽度不变，高度为原图1/2的倒影位图   
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height/2, width, height/2, matrix, false);
		//创建一个宽度不变，高度为原图+倒影图高度的位图  
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2),Bitmap.Config.ARGB_8888);
//		//将上面创建的位图初始化到画布   
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height,width,height + reflectionGap,deafalutPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		/**  
		 * 参数一:为渐变起初点坐标x位置，  
		 * 参数二:为y轴位置，  
		 * 参数三和四:分辨对应渐变终点，  
		 * 最后参数为平铺方式，  
		 * 这里设置为镜像Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变  
		 *  TileMode.MIRROR 倒影
		 */
		LinearGradient shader = new LinearGradient(0,bitmap.getHeight(),
				0, bitmapWithReflection.getHeight()+ reflectionGap,
				0x70ffffff, 0x00ffffff, TileMode.MIRROR);
		//设置阴影
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		//用已经定义好的画笔构建一个矩形阴影渐变效果   
		canvas.drawRect(0, height, 
				width, bitmapWithReflection.getHeight()+ reflectionGap, paint);
		return bitmapWithReflection;
	}
	
	/**
	 * 图片透明度处理
	 * @param sourceImg 原始图片 
	 * @param number 透明度处理
	 * @return
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 修改最高2位的值
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Config.ARGB_8888);
		return sourceImg;
	}

	
	/**
	 * dip转pixel
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * pixel转dip
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	/**
	 * 通过图片的路径生成适应屏幕大小的位图
	 * @param activity
	 * @param imagePath
	 * @return
	 */
	public static Bitmap zoomBitmapAdjustScreen(Activity activity,String imagePath){
		Bitmap bm = null;
		BitmapFactory.Options opt = new BitmapFactory.Options(); 
		//获取没有像素点的Bitmap对象，但有Bitmap的宽高，所以完全不用分配内存
		opt.inJustDecodeBounds = true;
		bm = BitmapFactory.decodeFile(imagePath, opt);

		//获取到这个图片的原始宽度和高度
		int picWidth  = opt.outWidth;
		int picHeight = opt.outHeight;

		//获取屏的宽度和高度
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();

		//isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		opt.inSampleSize = 1;
		//根据屏的大小和图片大小计算出缩放比例
		if(picWidth > picHeight){
			if(picWidth > screenWidth)
				opt.inSampleSize = picWidth/screenWidth;
		}else{
			if(picHeight > screenHeight)
				opt.inSampleSize = picHeight/screenHeight;
		}

		//这次再真正地生成一个有像素的，经过缩放了的bitmap
		opt.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(imagePath, opt);
		return bm;
	}
	
	/**
	 * 按比例缩放 - 文件名
	 * @param file
	 * @param destW
	 * @param destH
	 * @return
	 */
	public static Bitmap getScalBitmap(String file, int destW, int destH) {
		if(file == null)
			return null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bm = BitmapFactory.decodeFile(file, options);
		options.inJustDecodeBounds = false;

		float blW = (float) options.outWidth / destW;
		float blH = (float) options.outHeight / destH;

		options.inSampleSize = 1;
		if (blW > 1 || blH > 1) {
			float bl = (blW > blH ? blW : blH);
			options.inSampleSize = (int) (bl + 0.9f);// 劲量不失真
		}
		try {
			bm = BitmapFactory.decodeFile(file, options);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		return bm;
	}
	
	
	
	/**
	 * 按比例缩放  --按Uri
	 * @param context
	 * @param uri
	 * @param destW
	 * @param destH
	 * @return
	 */
	public static Bitmap getScalBitmap(Context context, Uri uri, int destW, int destH) {
		if(uri == null)
			return null;

		InputStream in = null;
		try {
			in = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bm = BitmapFactory.decodeStream(in, null, options);
		options.inJustDecodeBounds = false;
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		float blW = (float) options.outWidth / destW;
		float blH = (float) options.outHeight / destH;

		options.inSampleSize = 1;
		if (blW > 1 || blH > 1) {
			float bl = (blW > blH ? blW : blH);
			options.inSampleSize = (int) (bl + 0.9f);// 劲量保证不失真
		}

		try {
			in = context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		bm = BitmapFactory.decodeStream(in, null, options);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}
	
	/**
	 * 对一张位图进行旋转
	 * @param b
	 * @param degrees
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees,
					(float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b,
						0,
						0,
						b.getWidth(),
						b.getHeight(),
						m,
						true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return b;
	}
	
	
	
	
	/**
	 * 保存图片到文件
	 * @param bmp
	 * @param path   保存图片的文件路径
	 * @param format 保存图片的格式  CompressFormat.JPEG 或 CompressFormat.PNG 等
	 * @param quality 0-100 图片质量
	 * @return
	 */
	public static boolean saveBitmaptoFile(Bitmap bmp, 
			String path,
			CompressFormat format,
			int quality){
		OutputStream stream = null;
		try {
			File file = new File(path);
			File filePath = file.getParentFile();
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			stream = new FileOutputStream(path);
			boolean rlt =  bmp.compress(format, quality, stream);
			stream.close();
			return rlt;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	
	/**
	 * 压缩位图 并保存到文件
	 * @param originalBitmap
	 * @param quality
	 * @return
	 */
	public static File compressBitmapToFile(Bitmap originalBitmap,String path, int quality){
		File file = new File(path);
		File filePath = file.getParentFile();
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//将原始位图进行压缩，并写入输出流中
		originalBitmap.compress(CompressFormat.JPEG,
				quality, baos);
		//将字节数据写入到文件
		byte[] imageData = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(imageData);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(originalBitmap != null 
				&& !originalBitmap.isRecycled()){
			originalBitmap.recycle();
		}
		return file;
	}
	
	
	/**
	 * 对控件截图。
	 * 
	 * @param v
	 *            需要进行截图的控件。
	 * @param quality
	 *            图片的质量
	 * @return 该控件截图的byte数组对象。
	 */
	public static byte[] printScreen(View v, int quality) {
		if(quality == -1)
			quality = 100;//默认值
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache(true);
		Bitmap bitmap = v.getDrawingCache();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		return baos.toByteArray();
	}

	/**
	 * 截图
	 * 
	 * @param v
	 *            需要进行截图的控件
	 * @return 该控件截图的Bitmap对象。
	 */
	public static Bitmap printScreen(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return v.getDrawingCache();
	}
	
}
