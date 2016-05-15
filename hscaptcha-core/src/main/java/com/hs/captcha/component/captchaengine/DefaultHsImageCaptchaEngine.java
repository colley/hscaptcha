/*
 * Copyright (c) 2016-2017 by Colley
 * All rights reserved.
 */
package com.hs.captcha.component.captchaengine;

import com.hs.captcha.common.CaptchaCacheUtil;
import com.hs.captcha.common.enums.CaptchaTypeEnum;
import com.hs.captcha.model.ImageCaptcha;

import java.security.SecureRandom;

import java.util.Random;


/**
 *@FileName  DefaultHsImageCaptchaEngine.java
 *@Date  16-5-14 下午11:23
 *@author Colley
 *@version 1.0
 */
public class DefaultHsImageCaptchaEngine implements HsCaptchaEngine {
    private Random myRandom = new SecureRandom();
    private CaptchaTypeEnum[] typeEnums = CaptchaTypeEnum.values();

    public ImageCaptcha getNextCaptcha() {
        CaptchaTypeEnum typeEnum = typeEnums[myRandom.nextInt(typeEnums.length)];
        return CaptchaCacheUtil.genCaptcha(typeEnum);
    }
}
