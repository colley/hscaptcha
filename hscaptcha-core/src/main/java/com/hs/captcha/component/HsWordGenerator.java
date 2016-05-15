/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component;

/**
 *@FileName  HsWordGenerator.java
 *@Date  16-5-3 上午10:35
 *@author Ma Yuanchao
 *@version 1.0
 */
public interface HsWordGenerator {
	/**
	 * 获取生产验证码的字符串
	 * @param codeLenth
	 * @return
	 * @author mayuanchao
	 * @date 2016年5月4日
	 */
    public abstract String getWord(Integer codeLenth);
}
