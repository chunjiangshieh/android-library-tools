package com.xcj.android.util;

import java.io.File;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * 调用系统级别的意图
 * @author chunjiang.shieh
 *
 */
public class OSIntentUtil {
	
	/**
	 * 直接拨打电话的意图
	 * @param num	 手机号码
	 * @return
	 */
	public static Intent getIntentPhoneCall(String num){
		 Intent intent=new Intent(Intent.ACTION_CALL,
				 Uri.parse("tel:"+num));
		 return intent;
	}
	
	/**
	 * 调用拨打电话的界面的意图
	 * @param num
	 * @return
	 */
	public static Intent getIntentPhoneCallView(String num){
		Intent intent = new Intent(Intent.ACTION_DIAL,  
				Uri.parse("tel:"+num));  
		return intent;
	}
	
	/**
	 * 调用发短信的程序
	 * @return
	 */
	public static Intent getIntentSmsProgram(){
		Intent intent = new Intent(Intent.ACTION_VIEW);   
		intent.setType("vnd.android-dir/mms-sms");  
		return intent;
	}
	
	/**
	 * 发送短信
	 * @param address
	 * @param body
	 * @return
	 */
	public static Intent getIntentSendSms(String address,String body){
		Uri uri =Uri.parse("smsto:"+address);   
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);   
		intent.putExtra("sms_body", body);   
		return intent;
	}
	
	/**
	 * 获取网络设置意图
	 * @return
	 */
	public static Intent getIntentNetSetting(){
		return new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	}

	/**
	 * 获取安装APK文件的意图
	 * @param apkFilePath
	 * @return
	 */
	public static Intent getIntentInstallApk(String apkFilePath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		//Uri.parse("file://"+ "/sdcard/solitaire.apk")  可以为这个
		intent.setDataAndType(Uri.fromFile(new File(apkFilePath)),
				"application/vnd.android.package-archive");
		return intent;
	}
	
	/**
	 * 获取联系人列表视图的意图
	 * @return
	 */
	public static Intent getIntentContactsView(){
		Uri uri = Uri.parse("content://contacts/people");
		Intent intent = new Intent(Intent.ACTION_PICK, uri);
		return intent;
	}
	
	/**
	 * 打开另一个程序的意图
	 * @param pkg  应用程序的包名
	 * @param cls	应用程序主Activity的名字
	 * @return
	 */
	public static Intent getIntentOpenOtherApp(String pkg,String cls){
		Intent intent = new Intent();
		ComponentName componentName = new ComponentName(pkg, cls);
		intent.setComponent(componentName);
		intent.setAction("android.intent.action.MAIN");
		return intent;
	}
	
	
	
	public static final String PIKE_FILE_TYPE_IMAGE = "image";
	public static final String PIKE_FILE_TYPE_FILE = "file";
	/**
	 * 通过系统自带的程序选取文件
	 * @param fileType  根据不同手机自带的程序提供的文件类型不一
	 * @return  
	 */
	public static Intent getIntentPickFile(String fileType){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		if(fileType == null){
			intent.setType("*/*");
		}else{
			intent.setType(fileType + "/*");
		}
		return intent;
	}
	
	/**
	 * 调用系统拍照页面
	 * @return
	 */
	public static Intent getIntentImageCapture(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		return intent;
	}

}
