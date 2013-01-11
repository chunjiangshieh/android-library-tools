package com.xcj.android.util;




import android.content.Context;
import android.content.SharedPreferences;

/**
 * 轻量级存储一些常用的配置的工具类
 * TODO：出于安全考虑，可以对存储的数据进行AES加密或者其他方式的对称加密，暂未实现
 * @author chunjiang.shieh
 *
 */
public class PreferencesUtil {	
	
	private static PreferencesUtil mInstance;

	private static Context mContext;

	private SharedPreferences mSharedPreferences;
	
	private static PreferencesUtil getInstance(){
		if(mInstance == null){
				mInstance = new PreferencesUtil();
		}
		return mInstance;
	}
	
	/**
	 * 使用PreferencesUtil前需要在Application启动（onCreate）时调用initContext
	 * @param context
	 */
	public static void initContext(Context context){
		mContext = context;
	}
	
	
	private PreferencesUtil() {
		if(mContext == null){
			throw new NullPointerException("Android Context  is null,Please initialization!");
		}
		String packageName = mContext.getPackageName();
		StringBuilder spName = new StringBuilder();
		spName.append(packageName);
		spName.append(".preferences");
		String preferencesFile = spName.toString();
		mSharedPreferences = mContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE);
	}
	
	
	/**
	 * 设置参数的KEY和Value
	 * @param key
	 * @param obj
	 */
	public static void setAttr(String key,Object obj){
		if(obj instanceof String){
			String value = (String) obj;
			getInstance().setStringAttr(key, value);
		}else if(obj instanceof Boolean){
			Boolean value = (Boolean) obj;
			getInstance().setBooleanAttr(key, value);
		}else if(obj instanceof Integer){
			Integer value = (Integer) obj;
			getInstance().setIntAttr(key, value);
		}else if(obj instanceof Long){
			Long value = (Long) obj;
			getInstance().setLongAttr(key, value);
		}
	}
	

	/**
	 * 获取参数的String值
	 * @param key
	 * @return
	 */
	public static String getAttrString(String key) {
		return getInstance().getStringAttr(key);
	}
	
	public static void removeAttrString(String key){
		getInstance().removeStringAttr(key);
	}
	
	/**
	 * 获取参数的Boolean值
	 * @param key
	 * @return
	 */
	public static boolean getAttrBoolean(String key){
		return getInstance().getBooleanAttr(key);
	}
	
	/**
	 * 获取参数的Int值
	 * @param key
	 * @return
	 */
	public static int getAttrInt(String key) {
		return getInstance().getIntAttr(key);
	}
	
	/**
	 * 获取参数的Long值
	 * @param key
	 * @return
	 */
	public static long getAttrLong(String key) {
		return getInstance().getLongAttr(key);
	}
	
	
	
	private void setStringAttr(String key, String value) {
		SharedPreferences.Editor edit = mSharedPreferences.edit();
		if (value == null) {
			edit.remove(key);
		} else {
			edit.putString(key, value);
		}
		edit.commit();
	}

	private String getStringAttr(String key) {
		return mSharedPreferences.getString(key, null);
	}
	

	private void removeStringAttr(String key){
		SharedPreferences.Editor edit = mSharedPreferences.edit();
		String value = getStringAttr(key);
		if(value != null){
			edit.remove(key);
		}
		edit.commit();
	}
	
	
	private void setBooleanAttr(String key,boolean value){
		SharedPreferences.Editor edit = mSharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	private boolean getBooleanAttr(String key){
		return mSharedPreferences.getBoolean(key, false);

	}
	
	
	private void setIntAttr(String key, int value) {
		SharedPreferences.Editor edit = mSharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}


	private int getIntAttr(String key) {
		return mSharedPreferences.getInt(key, -1);
	}
	
	
	
	private void setLongAttr(String key, long value) {
		SharedPreferences.Editor edit = mSharedPreferences.edit();
		edit.putLong(key, value);
		edit.commit();
	}


	private long getLongAttr(String key) {
		return mSharedPreferences.getLong(key, -1);
	}
}
