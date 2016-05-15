/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component.captchaengine;

import com.hs.captcha.model.HsCaptcha;


/**
 *@FileName  HsCaptchaEngine.java
 *@Date  16-5-3 上午11:13
 *@author Ma Yuanchao
 *@version 1.0
 */
public interface HsCaptchaEngine {
    public abstract HsCaptcha getNextCaptcha();
}
