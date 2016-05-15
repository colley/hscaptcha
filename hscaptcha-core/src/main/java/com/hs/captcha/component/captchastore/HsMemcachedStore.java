/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component.captchastore;

import java.util.Collection;

import com.hs.captcha.model.HsCaptcha;
import com.hs.captcha.model.ImageCaptcha;


/**
 * 使用缓存存储验证码
 *@FileName  HsMemcachedStore.java
 *@Date  16-5-4 上午9:37
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsMemcachedStore implements CaptchaStore {
    private HsMemcachedProxy hsMemcachedProxy;
    private final static String CAPTCHA_PREFIX="HSCPT_";

    @Override
    public boolean hasCaptcha(String ID) {
    	String cacheKey = CAPTCHA_PREFIX+ID;
    	Object cahceObj = hsMemcachedProxy.get(cacheKey);
        return cahceObj==null? false:true;
    }

    @Override
    public void storeCaptcha(String ID, HsCaptcha paramCaptcha) {
    	String cacheKey = CAPTCHA_PREFIX+ID;
    	hsMemcachedProxy.set(cacheKey, paramCaptcha.getCode());
    }

    @Override
    public boolean removeCaptcha(String ID) {
        return hsMemcachedProxy.delete(ID);
    }

    @Override
    public HsCaptcha getCaptcha(String ID) {
    	String cacheKey = CAPTCHA_PREFIX+ID;
    	String code= hsMemcachedProxy.get(cacheKey); 
    	HsCaptcha hsCaptcha = new ImageCaptcha(code);
        return hsCaptcha;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Collection<String> getKeys() {
        return null;
    }

    @Override
    public void empty() {
    }

    @Override
    public void initAndStart() {
    }

    @Override
    public void cleanAndShutdown() {
    }

    public void setHsMemcachedProxy(HsMemcachedProxy hsMemcachedProxy) {
        this.hsMemcachedProxy = hsMemcachedProxy;
    }
}
