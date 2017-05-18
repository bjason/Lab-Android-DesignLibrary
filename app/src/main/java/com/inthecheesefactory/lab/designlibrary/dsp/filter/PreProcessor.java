package com.inthecheesefactory.lab.designlibrary.dsp.filter;

import com.inthecheesefactory.lab.designlibrary.dsp.base.ArgumentDescribe;
import com.inthecheesefactory.lab.designlibrary.dsp.base.BasePipe;

/**
 * 预处理机，完成归一化和滤波
 * 
 * @author RobinTang
 * 
 */
public class PreProcessor extends BaseFilter {

	
	/**
	 * 预处理机
	 */
	public PreProcessor() {
		super("简单预处理机", "", ArgumentDescribe.doubleArray("输入Double数组"), ArgumentDescribe.doubleArray("输出处理过的Double数组"));
	}

	@Override
	public Object calculate(Object input) {
		return BasePipe.pipesCal(input, new MeanToZero(), new ToOne2(), new MeanFilter(10));
//		return BasePipe.pipesCal(input, new MeanToZero(), new ToOne2(), new WaveletFilter());
	}
}
