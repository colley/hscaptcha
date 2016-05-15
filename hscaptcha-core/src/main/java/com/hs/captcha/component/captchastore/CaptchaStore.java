/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component.captchastore;

import java.util.Collection;

import com.hs.captcha.model.HsCaptcha;


/**
 * 验证码存储接口
 *@FileName  CaptchaStore.java
 *@Date  16-5-3 上午11:02
 *@author Ma Yuanchao
 *@version 1.0
 */
public abstract interface CaptchaStore {
    /**
     * check验证码是否存在
     * @param ID
     * @return
     * @author mayuanchao
     * @date 2016年5月4日
     */
    boolean hasCaptcha(String ID);

    /**
     * 存储验证码
     * @param ID
     * @param paramCaptcha
     * @author mayuanchao
     * @date 2016年5月4日
     */
    void storeCaptcha(String ID, HsCaptcha paramCaptcha);

    /**
     * 删除验证码
     * @param ID
     * @return
     * @author mayuanchao
     * @date 2016年5月4日
     */
    boolean removeCaptcha(String ID);

    /**
     * 获取验证码
     * @param ID
     * @return
     * @author mayuanchao
     * @date 2016年5月4日
     */
    HsCaptcha getCaptcha(String ID);

    int getSize();

    Collection<String> getKeys();

    void empty();

    void initAndStart();

    void cleanAndShutdown();
}
