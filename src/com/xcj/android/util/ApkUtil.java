package com.xcj.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;

/**
 * @author hljdrl@gmail.com
 * APK应用程序相关方法
 * */
public class ApkUtil {

	/**
	 * 指定PackageName包名的应用是否已安装.
	 * @param context
	 * @param PackageName
	 * @return
	 * @throws NotFoundException
	 */
	public static boolean hasInstallApk(Context context, String PackageName)
			throws NotFoundException {
		boolean flag = true;
		PackageManager mPm = context.getPackageManager();
		PackageInfo pkgInfo = null;
		try {
			pkgInfo = mPm.getPackageInfo(PackageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (NameNotFoundException e) {
			flag = false;
		} catch (Exception ex) {
			flag = false;
			if (pkgInfo == null) {
				throw new NotFoundException();
			}
		}
		return flag;
	}

	/**
	 * 检查系统中是否存在-这个Activity
	 * @param context
	 * @param pkg
	 * @param cls
	 * @return
	 */
	public static boolean hasActivity(Context context, String pkg, String cls) {
		boolean has = true;
		Intent intent = new Intent();
		intent.setClassName(pkg, cls);
		if (context.getPackageManager().resolveActivity(intent, 0) == null) {
			// 说明系统中不存在这个activity
			has = false;
		}
		return has;
	}

}
