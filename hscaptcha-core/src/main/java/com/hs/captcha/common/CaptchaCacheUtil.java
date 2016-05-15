/*
 * Copyright (c) 2016-2017 by Colley
 * All rights reserved.
 */
package com.hs.captcha.common;

import com.hs.captcha.common.enums.CaptchaTypeEnum;
import com.hs.captcha.component.image.CaptchaUtil;
import com.hs.captcha.model.ImageCaptcha;


/**
 *@FileName  CaptchaCacheUtil.java
 *@Date  16-5-14 下午11:19
 *@author Colley
 *@version 1.0
 */
public class CaptchaCacheUtil {
    public static ImageCaptcha genCaptcha(CaptchaTypeEnum captchaType) {
        ImageCaptcha captcha = null;
        switch (captchaType) {
        case JPG60:
            captcha = CaptchaUtil.genJpegImage(6, false);
            break;
        case JPG61:
            captcha = CaptchaUtil.genJpegImage(6, true);
            break;
        case JPG40:
            captcha = CaptchaUtil.genJpegImage(4, false);
            break;
        case JPG41:
            captcha = CaptchaUtil.genJpegImage(4, true);
            break;
        case GIF60:
            captcha = CaptchaUtil.genGifImage(6, false);
            break;
        case GIF61:
            captcha = CaptchaUtil.genGifImage(6, true);
            break;
        case GIF40:
            captcha = CaptchaUtil.genGifImage(4, false);
            break;
        case GIF41:
            captcha = CaptchaUtil.genGifImage(4, true);
            break;
        case JPG:
            captcha = CaptchaUtil.genJpegImage(false);
            break;
        case GIF:
            captcha = CaptchaUtil.genGifImage(false);
            break;
        default:
            captcha = CaptchaUtil.genJpegImage(4, false);
        }

        return captcha;
    }
}
