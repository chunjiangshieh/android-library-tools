package com.xcj.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/**
 * IO操作的工具类
 * @author chunjiang.shieh
 *
 */
public class StreamUtil {


	
	
	/**
	* 读取流
	* @param inStream
	* @return 字节数组
	* @throws Exception
	*/
	public static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len=inStream.read(buffer)) != -1){
			outSteam.write(buffer, 0, len);
		}
		byte[] outData = outSteam.toByteArray();
		outSteam.close();
		inStream.close();
		return outData;
	}
	
	/**
	 * 字节数组转成流
	 * @param data
	 * @return
	 */
	public static InputStream bytesToStream(byte[] data){
		InputStream inStream = new ByteArrayInputStream(data);
		return inStream;
	}
}
