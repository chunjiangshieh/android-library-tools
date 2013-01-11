package com.xcj.android.util;

import android.os.Environment;

/**
 * 存储的相关方法
 * @author chunjiang.shieh
 *
 */
public class StorageUtil {
	
	
	/**
	 * 判断SDCard是否可用
	 * @return
	 */
	public static boolean checkSDCardAvailable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	

}
