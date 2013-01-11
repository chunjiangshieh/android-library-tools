package com.xcj.android.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

/**
 *  日志工具
 * 1.添加日志输出选项.控制日志输出位置 
 * 2.添加文件日志功能.
 * 3.控制单个日志文件最大限制.由LOG_MAXSIZE常量控制,保留两个最新日志文件
 * 4.文件日志输出目标
 * TODO：出于方便管理，可以做到一天存一个文件，目前暂未实现
 * @author chunjiang.shieh
 */
public class LogUtil {
	
	
	public static final String TAG = LogUtil.class.getName();

	//日志打印到控制台
	public static final int TO_CONSOLE = 0x1;
	//日志打印到文件
	public static final int TO_FILE = 0x10;

	public static final int DEBUG_ALL = TO_CONSOLE  | TO_FILE ;

	private static LogUtil mLog;
	/**
	 * 打印日志的级别
	 */
	private int LOG_LEVEL = Log.VERBOSE;
	
	
	
	/**
	 * 日志打印到手机的存储路径
	 */
	private  final static String LOCAL_LOG_PATH = "/data/data/%packagename%/files/";
	/**
	 * 日志打印到Sdcard的存储路径
	 */
	private  final static String SDCARD_LOG_PAHT = "/%packagename%/log/files";
	
	
	private static  String mAppPath;
	
	private static  String mLogFilePrefix;
	
	/**
	 * 输出日志的文件名称
	 */
	private static final String LOG_TEMP_FILE = "log.temp";
	/**
	 * 最近一次的日志名称
	 */
	private static final String LOG_LAST_FILE = "log_last.txt";
	/**
	 * 控制日志输出标志位，打包控制
	 */
	public static boolean DEBUG = true;
	
	/**
	 * 文件的最大数
	 */
	private static final int LOG_MAXSIZE = 1024 * 200; // double the size
	
	/**
	 * 同步锁
	 */
	private static Object lockObj = new Object();

	/**
	 * 日志的输出流
	 */
	private OutputStream mLogStream;
	/**
	 * 文件的大小
	 */
	private long mFileSize;
	
	/**
	 * 打印文件日志的时间
	 */
	private Calendar mDate = Calendar.getInstance();
	/**
	 * 打印文件日志的内容
	 */
	private StringBuffer mBuffer = new StringBuffer();
	

	
	public static LogUtil getInstance(){
		if(mLog == null){
			mLog = new LogUtil();
		}
		return mLog;
	}
	
	public LogUtil(){
		if(mAppPath == null){
			throw new NullPointerException("mAppPath is null,please init at Application onCreate");
		}
	}
	
	/**
	 * 初始化Log打印路径  该方法在Application启动时初始化
	 * @param packageName	应用包名
	 * @param filePrefix		文件名的前缀 一般为应用名
	 */
	public static void initAppPath(String packageName, String filePrefix) {
		synchronized (lockObj) {
			//Sdcard 可用
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File storage = Environment.getExternalStorageDirectory();
				mAppPath = storage 
						+ SDCARD_LOG_PAHT.replaceFirst("%packagename%", packageName)
						+ "/";
			}else{
				mAppPath = LOCAL_LOG_PATH.replaceFirst("%packagename%", packageName);
			}	
			mLogFilePrefix = filePrefix;
			Log.d("iniAppPath", mAppPath);
			File dir = new File(mAppPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}
	
	
	public static void d(String tag, String msg) {
		getInstance().log(tag, msg, DEBUG_ALL, Log.DEBUG);
	}

	public static void v(String tag, String msg) {
		getInstance().log(tag, msg, DEBUG_ALL, Log.VERBOSE);
	}

	public static void e(String tag, String msg) {
		getInstance().log(tag, msg, DEBUG_ALL, Log.ERROR);
	}

	public static void i(String tag, String msg) {
		getInstance().log(tag, msg, DEBUG_ALL, Log.INFO);
	}

	public static void w(String tag, String msg) {
		getInstance().log(tag, msg, DEBUG_ALL, Log.WARN);
	}

	protected  void log(String tag, String msg, int outdest, int level) {
		if(!DEBUG){
			return ;
		}
		if (tag == null)
			tag = "TAG_NULL";
		if (msg == null)
			msg = "MSG_NULL";
		if (level >= LOG_LEVEL) {
			if ((outdest & TO_CONSOLE) != 0) {
				LogToConsole(tag, msg, level);
			}
			if ((outdest & TO_FILE) != 0) {
				LogToFile(tag, msg, level);
			}
		}
	}

	

	/**
	 * 组成Log字符串.添加时间信息.
	 * @param tag
	 * @param msg
	 * @return
	 */
	private String getLogStr(String tag, String msg) {
		mDate.setTimeInMillis(System.currentTimeMillis());
		mBuffer.setLength(0);
		mBuffer.append("[");
		mBuffer.append(tag);
		mBuffer.append(" : ");
		mBuffer.append(mDate.get(Calendar.MONTH) + 1);
		mBuffer.append("-");
		mBuffer.append(mDate.get(Calendar.DATE));
		mBuffer.append(" ");
		mBuffer.append(mDate.get(Calendar.HOUR_OF_DAY));
		mBuffer.append(":");
		mBuffer.append(mDate.get(Calendar.MINUTE));
		mBuffer.append(":");
		mBuffer.append(mDate.get(Calendar.SECOND));
		mBuffer.append(":");
		mBuffer.append(mDate.get(Calendar.MILLISECOND));
		mBuffer.append("] ");
		mBuffer.append(msg);
		return mBuffer.toString();
	}

	/**
	 * 将log打到控制台
	 * 
	 * @param tag
	 * @param msg
	 * @param level
	 */
	private void LogToConsole(String tag, String msg, int level) {
		switch (level) {
		case Log.DEBUG:
			Log.d(tag, msg);
			break;
		case Log.ERROR:
			Log.e(tag, msg);
			break;
		case Log.INFO:
			Log.i(tag, msg);
			break;
		case Log.VERBOSE:
			Log.v(tag, msg);
			break;
		case Log.WARN:
			Log.w(tag, msg);
			break;
		default:
			break;
		}
	}

	/**
	 * 将log打到文件日志  没有测试过，有可能有问题
	 * 
	 * @param tag
	 * @param msg
	 * @param level
	 */
	private void LogToFile(String tag, String msg, int level) {
		synchronized (lockObj) {
			OutputStream outStream = openLogFileOutStream();
			if (outStream != null) {
				try {
					byte[] d = getLogStr(tag, msg).getBytes("utf-8");
					if (mFileSize < LOG_MAXSIZE) {
						outStream.write(d);
						outStream.write("\r\n".getBytes());
						outStream.flush();
						mFileSize += d.length;
					} else {
						closeLogFileOutStream();
						renameLogFile();
						LogToFile(tag, msg, level);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	

	/**
	 * 获取日志临时文件输入流
	 * 
	 * @return
	 */
	private OutputStream openLogFileOutStream() {
		if (mLogStream == null) {
			try {
				if (mAppPath == null || mAppPath.length() == 0) {
					String packetName = "com.tgram.app";	
					String filePrefix = "appname";
					initAppPath(packetName, filePrefix);
				}
				File file = openAbsoluteFile(LOG_TEMP_FILE);
				if (file == null) {
					return null;
				}
				if (file.exists()) {
					mLogStream = new FileOutputStream(file, true);
					mFileSize = file.length();
				} else {
					// file.createNewFile();
					mLogStream = new FileOutputStream(file);
					mFileSize = 0;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
		return mLogStream;
	}


	/**
	 * 关闭日志输出流
	 */
	private void closeLogFileOutStream() {
		try {
			if (mLogStream != null) {
				mLogStream.close();
				mLogStream = null;
				mFileSize = 0;
			}
//			File file = openAbsoluteFile(LOG_TEMP_FILE);
//			if(file != null && file.exists()){
//				file.delete();
//			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 绝对路径打开文件
	 * @param name
	 * @return
	 */
	public File openAbsoluteFile(String name) {
		if (mAppPath == null || mAppPath.length() == 0) {
			return null;
		} else {
			File file = new File(mAppPath + mLogFilePrefix + "_" + name);
			return file;
		}
	}

	
	/**
	 * 重命名日志文件
	 */
	private void renameLogFile() {
		synchronized (lockObj) {
			File file = openAbsoluteFile(LOG_TEMP_FILE);
			File destFile = openAbsoluteFile(LOG_LAST_FILE);
			if (destFile.exists()) {
				destFile.delete();
			}
			file.renameTo(destFile);
		}
	}
}
