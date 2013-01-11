/*
 * @(#)NetUtil.java		       Project:ProgramList
 * Date:2012-8-10
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xcj.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络状态工具类
 * @author chunjiang.shieh
 *
 */
public class NetworkUtil {
	
	/**
	 * 检测当前手机是否联网
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测网络连接是否是cmwap
	 * 如果是的话需要设置代理
	 * @param context
	 * @return
	 */
	public static boolean isWap(Context context){
		//ConnectivityManager主要管理和网络连接相关的操作 
		ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if(nInfo == null || nInfo.getType() != ConnectivityManager.TYPE_MOBILE)
			return false;
		String extraInfo = nInfo.getExtraInfo();
		if(extraInfo == null || extraInfo.length() < 3)
			return false;
		if(extraInfo.toLowerCase().contains("wap"))
			return true;
		return false;
	}



	/**
	 * 检测手机是否开启GPRS网络
	 * 需要调用ConnectivityManager服务.
	 * @param context
	 * @return boolean
	 */
	public static boolean checkGprsNetwork(Context context) {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWorkInfo = connectivity.getActiveNetworkInfo();
		if (netWorkInfo !=null
				&& netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			has = netWorkInfo.isConnected();
		}
		return has;
	}

	/**
	 * 检测手机是否开启WIFI网络
	 * 需要调用ConnectivityManager服务.
	 * @param context
	 * @return boolean
	 */
	public static boolean checkWifiNetwork(Context context) {
		boolean has = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
		if (networkInfo != null 
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			has = networkInfo.isConnected();
		}
		return has;
	}
	
	/**
	 * 检测当前手机是否处在漫游
	 * @param mCm
	 * @return boolean
	 */
	public boolean isNetworkRoaming(Context mCm) {
		ConnectivityManager connectivity =
				(ConnectivityManager) mCm.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		}
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		boolean isMobile = (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
		TelephonyManager mTm = (TelephonyManager) mCm.getSystemService(Context.TELEPHONY_SERVICE);
		boolean isRoaming = isMobile && mTm.isNetworkRoaming();
		return isRoaming;
	}
}
