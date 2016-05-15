/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component.image;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hs.captcha.common.xml.FontConfig;
import com.hs.captcha.common.xml.XMLHelper;
import com.hs.captcha.component.HsWordGenerator;
import com.hs.captcha.component.RandomWordGenerator;
import com.hs.captcha.model.ImageCaptcha;


/**
 *@FileName  CaptchaUtil.java
 *@Date  16-5-2 下午7:36
 *@author Ma Yuanchao
 *@version 1.0
 */
public class CaptchaUtil {
    public static final Log logger = LogFactory.getLog(CaptchaUtil.class);
    private static final List<FontConfig> fontConfigList;
    private static Random myRandom = new SecureRandom();
    static{
    	fontConfigList = XMLHelper.getInstance().getCaptchaConfig().getCaptchas();
    }
    public static ImageCaptcha genGifImage(boolean appendInterference) {
        FontConfig fontConfig = (FontConfig) fontConfigList.get(myRandom.nextInt(fontConfigList.size()));
        String code = generateCodeString(fontConfig.getDict(), 4);

        return ImageCaptchaUtil.getGifImage(code, fontConfig, appendInterference);
    }

    public static ImageCaptcha genGifImage(int codeLength, boolean appendInterference) {
        FontConfig fontConfig = (FontConfig) fontConfigList.get(myRandom.nextInt(fontConfigList.size()));
        String code = generateCodeString(fontConfig.getDict(), codeLength);

        return ImageCaptchaUtil.getGifImage(code, fontConfig, appendInterference);
    }

    public static ImageCaptcha genGifImage(FontConfig fontConfig, int codeLength, boolean appendInterference) {
        String code = generateCodeString(fontConfig.getDict(), codeLength);

        return ImageCaptchaUtil.getGifImage(code, fontConfig, appendInterference);
    }

    public static ImageCaptcha genGifImage(FontConfig fontConfig, int codeLength, int imageWidth,
        boolean appendInterference) {
        String code = generateCodeString(fontConfig.getDict(), codeLength);

        return ImageCaptchaUtil.getGifImage(code, fontConfig, imageWidth, appendInterference);
    }

    public static ImageCaptcha genJpegImage(boolean appendInterference) {
        FontConfig fontConfig = (FontConfig) fontConfigList.get(myRandom.nextInt(fontConfigList.size()));
        String code = generateCodeString(fontConfig.getDict(), 4);

        ImageCaptcha captcha = null;
        try {
            captcha = ImageCaptchaUtil.getJpegImage(code, fontConfig, appendInterference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return captcha;
    }

    public static ImageCaptcha genJpegImage(int codeLength, boolean appendInterference) {
        FontConfig fontConfig = (FontConfig) fontConfigList.get(new Random().nextInt(fontConfigList.size()));
        String code = generateCodeString(fontConfig.getDict(), codeLength);

        ImageCaptcha captcha = null;
        try {
            captcha = ImageCaptchaUtil.getJpegImage(code, fontConfig, appendInterference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return captcha;
    }

    public static ImageCaptcha genJpegImage(FontConfig fontConfig, int codeLength, boolean appendInterference) {
        String code = generateCodeString(fontConfig.getDict(), codeLength);
        ImageCaptcha captcha = null;
        try {
            captcha = ImageCaptchaUtil.getJpegImage(code, fontConfig, appendInterference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return captcha;
    }

    public static ImageCaptcha genJpegImage(FontConfig fontConfig, int codeLength, int imageWidth,
        boolean appendInterference) {
        String code = generateCodeString(fontConfig.getDict(), codeLength);
        ImageCaptcha captcha = null;
        try {
            captcha = ImageCaptchaUtil.getJpegImage(code, fontConfig, imageWidth, appendInterference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return captcha;
    }

    private static String generateCodeString(String dictionary, int length) {
    	HsWordGenerator wordGenerator = new RandomWordGenerator(dictionary);
    	return wordGenerator.getWord(length);
    }
}
