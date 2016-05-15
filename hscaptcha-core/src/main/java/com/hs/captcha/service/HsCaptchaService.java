/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.service;

import java.io.OutputStream;

import com.hs.captcha.model.HsCaptcha;


/**
 * 
 *@FileName  HsCaptchaService.java
 *@Date  16-5-3 上午10:50
 *@author Ma Yuanchao
 *@version 1.0
 */
public interface HsCaptchaService {
	
	/**
	 * 根据id获取验证码图片BufferedImage
	 * @param Id
	 * @return
	 * @author mayuanchao
	 * @date 2016年5月4日
	 */
    public abstract OutputStream getImageChallengeForID(String Id);
    
    /**
     * 根据id获取验证码
     * @param id
     * @return
     * @author mayuanchao
     * @date 2016年5月4日
     */
    public abstract HsCaptcha getHsCaptcha(String id);

    public abstract Boolean validateResponseForID(String id, Object code);
    
    /**
     * 根据userName来过滤pass的的userName
     * @param id
     * @param code
     * @param userName
     * @return
     * @author mayuanchao
     * @date 2016年5月4日
     */
    public abstract Boolean validateResponseForID(String id, Object code,String userName);
}
