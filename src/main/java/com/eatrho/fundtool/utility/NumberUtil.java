package com.eatrho.fundtool.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {
	private static final int DEFAULT_SCALE = 10;// 默认精度
	private static final RoundingMode DEFAULT_ROUNDINGMODE = RoundingMode.HALF_UP;// 浮点取舍-默认四舍五入

	/**
	 * 指定format的格式格式化参数n
	 */
	public static String format(Number n, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(n);
	}

	/**
	 * 将数字或数字字符串封装成BigDecimal
	 *
	 * @param number
	 * @return
	 */
	private static BigDecimal getBigDecimal(Object number) {
		if (number == null) {
			return null;
		}
		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof Double || number instanceof Float) {
			return new BigDecimal(number.toString());
		} else if (number instanceof Integer) {
			return new BigDecimal((Integer) number);
		} else if (number instanceof Long) {
			return new BigDecimal((Long) number);
		} else {
			return new BigDecimal(number.toString());
		}
	}

	/**
	 * 求商运算(a/b)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Number div(Number a, Number b) {
		return getBigDecimal(a).divide(getBigDecimal(b), DEFAULT_SCALE, DEFAULT_ROUNDINGMODE);
	}

	/**
	 * 求商运算(a/b)
	 * @param a
	 * @param b
	 * @param scale 使用的浮点精度位数
	 * @return
	 */
	public static Number div(Number a, Number b, int scale) {
		return getBigDecimal(a).divide(getBigDecimal(b), scale, DEFAULT_ROUNDINGMODE);
	}

	/**
	 * 求和运算(a+b)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Number sum(Number a, Number b) {
		return getBigDecimal(a).add(getBigDecimal(b));
	}

	/**
	 * 求差运算(a-b)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Number sub(Number a, Number b) {
		return getBigDecimal(a).subtract(getBigDecimal(b));
	}

	/**
	 * 求积运算(a*b)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Number mul(Number a, Number b) {
		return getBigDecimal(a).multiply(getBigDecimal(b));
	}

}
