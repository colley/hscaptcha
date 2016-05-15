/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component.captchastore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.hs.captcha.model.HsCaptcha;


/**
 *@FileName  HsMapCaptchaStore.java
 *@Date  16-5-3 上午11:02
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsMapCaptchaStore implements CaptchaStore {
    protected Map<String, HsCaptcha> store;

    public HsMapCaptchaStore() {
        store = new HashMap<String, HsCaptcha>();
    }

    public boolean hasCaptcha(String id) {
        return store.containsKey(id);
    }

    public void storeCaptcha(String id, HsCaptcha captcha) {
        store.put(id, captcha);
    }

    public HsCaptcha getCaptcha(String id) {
        HsCaptcha captcha = store.get(id);
        return captcha;
    }

    public boolean removeCaptcha(String id) {
        if (store.get(id) != null) {
            store.remove(id);
            return true;
        }

        return false;
    }

    public int getSize() {
        return store.size();
    }

    public Collection<String> getKeys() {
        return store.keySet();
    }

    public void empty() {
        store = new HashMap<String, HsCaptcha>();
    }

    public void initAndStart() {
    }

    public void cleanAndShutdown() {
        store.clear();
    }
}
