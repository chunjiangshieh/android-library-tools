package com.xcj.android.util;

/**
 * 数学运算的工具类
 * @author chunjiang.shieh
 *
 */
public class MathUtil {
	
	/**
	 * Float类型减法运算
	 * @param sub1_f
	 * @param sub2_f
	 * @return
	 */
	public static float floatSub(float sub1_f,float sub2_f){
		int sub1 = yuan2fen(sub1_f);
		int sub2 = yuan2fen(sub2_f);
		int rlt = sub1 - sub2;
		String str = (Math.abs(rlt) / 100) + ".";
		str += String.format("%02d", Math.abs(rlt % 100));
		if(rlt < 0){
			return Float.parseFloat(str) * -1;
		}else{
			return Float.parseFloat(str);
		}
	}
	
	
	/**
	 * Float类型加法运算
	 * @param sum1_f
	 * @param sum2_f
	 * @return
	 */
	public static float floatSum(float sum1_f,float sum2_f){
		int sum1 = yuan2fen(sum1_f);
		int sum2 = yuan2fen(sum2_f);
		int rlt = sum1 + sum2;
		String t = (Math.abs(rlt) / 100) + "." ;
		t += String.format("%02d", Math.abs((rlt % 100)));
		if(rlt < 0){
			return Float.parseFloat(t) * -1;
		}else{
			return Float.parseFloat(t);
		}
	}
	
	/**
	 * 元 to 分
	 * @param value
	 * @return
	 */
	private static int yuan2fen(float value){
		int f = (int)(value * 1000);
		int t =  f % 10;
		int fen = 0;
		if(t > 5){
			fen  = f/10 + 1;
		}else{
			fen = f/10;
		}
		return fen;
	}

}
