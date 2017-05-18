package com.inthecheesefactory.lab.designlibrary.dsp.filter;

import com.inthecheesefactory.lab.designlibrary.dsp.base.ArgumentDescribe;
import com.inthecheesefactory.lab.designlibrary.dsp.util.SinMath;

/**
 * Double数组归一化
 * 
 * @author RobinTang
 * 
 */
public class ToOne extends BaseFilter {
	/**
	 * Double数组归一化
	 */
	public ToOne() {
		super("Double数组归一化", "", ArgumentDescribe.doubleArray("输入Double数组"), ArgumentDescribe.doubleArray("返回归一化Double数组"));
	}

	@Override
	public Object calculate(Object input) {
		double[] ins = (double[]) input;
		int len = ins.length;
		double[] ret = new double[len];
		double min = SinMath.min(ins);
		double max = SinMath.max(ins);
		double off = max - min;
		for (int i = 0; i < len; ++i) {
			ret[i] = (ins[i] - min) / off * 2 - 1;
		}
		return ret;
	}
}
