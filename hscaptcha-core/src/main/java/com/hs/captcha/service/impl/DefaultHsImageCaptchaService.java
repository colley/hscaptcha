/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.service.impl;

import java.io.OutputStream;

import com.hs.captcha.common.xml.Captchas;
import com.hs.captcha.common.xml.XMLHelper;
import com.hs.captcha.component.captchaengine.DefaultHsImageCaptchaEngine;
import com.hs.captcha.component.captchaengine.HsCaptchaEngine;
import com.hs.captcha.component.captchastore.CaptchaStore;
import com.hs.captcha.component.captchastore.FastHashMapCaptchaStore;


/**
 *@FileName  HsImageCaptchaService.java
 *@Date  16-5-3 下午1:18
 *@author Ma Yuanchao
 *@version 1.0
 */
public class DefaultHsImageCaptchaService extends AbstractCaptchaService {
    private Captchas captchas;

    public DefaultHsImageCaptchaService(CaptchaStore captchaStore, HsCaptchaEngine captchaEngine,
        int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize,
            captchaStoreLoadBeforeGarbageCollection);
        captchas = XMLHelper.getInstance().getCaptchaConfig();
    }

    public DefaultHsImageCaptchaService(CaptchaStore captchaStore, int minGuarantedStorageDelayInSeconds,
        int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, new DefaultHsImageCaptchaEngine(), minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize);
        captchas = XMLHelper.getInstance().getCaptchaConfig();
    }

    public DefaultHsImageCaptchaService(int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
        int captchaStoreLoadBeforeGarbageCollection) {
        super(new FastHashMapCaptchaStore(), new DefaultHsImageCaptchaEngine(), minGuarantedStorageDelayInSeconds,
            maxCaptchaStoreSize);
        captchas = XMLHelper.getInstance().getCaptchaConfig();
    }

    public OutputStream getImageChallengeForID(String ID) {
        return (OutputStream) getChallengeForID(ID);
    }

    public Boolean validateResponseForID(String id, String code) {
        return super.validateResponseForID(id, code);
    }

    @Override
    public Boolean validateResponseForID(String id, Object code, String userName) {
        if (captchas.containsKeyAndVal(userName, code)) {
            store.removeCaptcha(id);
            times.remove(id);
            numberOfCorrectResponse += 1;
            return Boolean.TRUE;
        }

        return super.validateResponseForID(id, code);
    }
}
