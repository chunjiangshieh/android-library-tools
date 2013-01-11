package com.xcj.android.util;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
/**
 * @author hljdrl@gmail.com
 * */
public class DeviceHelper {
	
	public  String UA = Build.MODEL;
	private String mIMEI;
	private String mSIM;
	private String mMobileVersion;
	private String mNetwrokIso;
	private String mNetType;
	private String mDeviceID;
	private List<NeighboringCellInfo> mCellinfos;
	Context mContext;
	
	//-----------------------------------------------------------
	private static DeviceHelper INSTANCE = null;
	public static synchronized DeviceHelper getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new DeviceHelper(context);
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * */
	public DeviceHelper(Context context) {
		mContext = context;
		findData();
	}
	/**
	 * 
	 * 设置手机立刻震动
	 * */
	public void Vibrate(Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	TelephonyManager mTm = null;
	private void findData() {
		mTm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		mIMEI = mTm.getDeviceId();
		mMobileVersion = mTm.getDeviceSoftwareVersion();
		mCellinfos = mTm.getNeighboringCellInfo();
		mNetwrokIso = mTm.getNetworkCountryIso();
		mSIM = mTm.getSimSerialNumber();
		mDeviceID = getDeviceId();
		//
		try {
			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			// WIFI/MOBILE
			mNetType = info.getTypeName();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onRefresh() {
		findData();
	}
	/**
	 * 获得android设备-唯一标识，android2.2 之前无法稳定运行.
	 * */
	public static String getDeviceId(Context mCm) {
		return Secure.getString(mCm.getContentResolver(), Secure.ANDROID_ID);
	}

	private String getDeviceId() {
		return Secure.getString(mContext.getContentResolver(),
				Secure.ANDROID_ID);
	}

	public String getImei() {
		return mIMEI;
	}

	public String getSIM() {
		return mSIM;
	}
	public String getUA() {
		return UA;
	}
	public String getDeviceInfo() {
		StringBuffer info = new StringBuffer();
		info.append("IMEI:").append(getImei());
		info.append("\n");
		info.append("SIM:").append(getSIM());
		info.append("\n");
		info.append("UA:").append(getUA());
		info.append("\n");
		info.append("MobileVersion:").append(mMobileVersion);

		info.append("\n");
		info.append("SDK: ").append(android.os.Build.VERSION.SDK);
		info.append("\n");
		info.append(getCallState());
		info.append("\n");
		info.append("SIM_STATE: ").append(getSimState());
		info.append("\n");
		info.append("SIM: ").append(getSIM());
		info.append("\n");
		info.append(getSimOpertorName());
		info.append("\n");
		info.append(getPhoneType());
		info.append("\n");
		info.append(getPhoneSettings());
		info.append("\n");
		return info.toString();
	}

	public String getSimState() {
		switch (mTm.getSimState()) {
		case android.telephony.TelephonyManager.SIM_STATE_UNKNOWN:
			return "未知SIM状态_"
					+ android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
		case android.telephony.TelephonyManager.SIM_STATE_ABSENT:
			return "没插SIM卡_"
					+ android.telephony.TelephonyManager.SIM_STATE_ABSENT;
		case android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED:
			return "锁定SIM状态_需要用户的PIN码解锁_"
					+ android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED;
		case android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED:
			return "锁定SIM状态_需要用户的PUK码解锁_"
					+ android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED;
		case android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return "锁定SIM状态_需要网络的PIN码解锁_"
					+ android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED;
		case android.telephony.TelephonyManager.SIM_STATE_READY:
			return "就绪SIM状态_"
					+ android.telephony.TelephonyManager.SIM_STATE_READY;
		default:
			return "未知SIM状态_"
					+ android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;

		}
	}

	public String getPhoneType() {
		switch (mTm.getPhoneType()) {
		case android.telephony.TelephonyManager.PHONE_TYPE_NONE:
			return "PhoneType: 无信号_"
					+ android.telephony.TelephonyManager.PHONE_TYPE_NONE;
		case android.telephony.TelephonyManager.PHONE_TYPE_GSM:
			return "PhoneType: GSM信号_"
					+ android.telephony.TelephonyManager.PHONE_TYPE_GSM;
		case android.telephony.TelephonyManager.PHONE_TYPE_CDMA:
			return "PhoneType: CDMA信号_"
					+ android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
		default:
			return "PhoneType: 无信号_"
					+ android.telephony.TelephonyManager.PHONE_TYPE_NONE;
		}
	}

	/**
	 * 服务商名称：例如：中国移动、联通 　　 SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断). 　　
	 */
	public String getSimOpertorName() {
		if (mTm.getSimState() == android.telephony.TelephonyManager.SIM_STATE_READY) {
			StringBuffer sb = new StringBuffer();
			 sb.append("SimOperatorName: ").append(mTm.getSimOperatorName());
			 sb.append("\n");
			sb.append("SimOperator: ").append(mTm.getSimOperator());
			sb.append("\n");
			sb.append("Phone:").append(mTm.getLine1Number());
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("SimOperatorName: ").append("未知");
			sb.append("\n");
			sb.append("SimOperator: ").append("未知");
			return sb.toString();
		}
	}

	public String getPhoneSettings() {
		StringBuffer buf = new StringBuffer();
		String str = Secure.getString(mContext.getContentResolver(),
				Secure.BLUETOOTH_ON);
		buf.append("蓝牙:");
		if (str.equals("0")) {
			buf.append("禁用");
		} else {
			buf.append("开启");
		}
		//
		str = Secure.getString(mContext.getContentResolver(),
				Secure.BLUETOOTH_ON);
		buf.append("WIFI:");
		buf.append(str);

		str = Secure.getString(mContext.getContentResolver(),
				Secure.INSTALL_NON_MARKET_APPS);
		buf.append("APP位置来源:");
		buf.append(str);

		return buf.toString();
	}

	public String getCallState() {

		switch (mTm.getCallState()) {
		case android.telephony.TelephonyManager.CALL_STATE_IDLE:
			return "电话状态[CallState]: 无活动";
		case android.telephony.TelephonyManager.CALL_STATE_OFFHOOK:
			return "电话状态[CallState]: 无活动";
		case android.telephony.TelephonyManager.CALL_STATE_RINGING:
			return "电话状态[CallState]: 无活动";
		default:
			return "电话状态[CallState]: 未知";
		}
	}

	public String getNetwrokIso() {
		return mNetwrokIso;
	}

	/**
	 * @return the mDeviceID
	 */
	public String getDeviceID() {
		return mDeviceID;
	}

	/**
	 * @return the mNetType
	 */
	public String getNetType() {
		return mNetType;
	}

	/**
	 * @return the mCellinfos
	 */
	public List<NeighboringCellInfo> getCellinfos() {
		return mCellinfos;
	}

	

}
