/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component.captchastore;

import org.apache.commons.collections.FastHashMap;


/**
 * 使用FastHashMap存储验证码，不能使用集群
 *@FileName  FastHashMapCaptchaStore.java
 *@Date  16-5-3 上午11:08
 *@author Ma Yuanchao
 *@version 1.0
 */
public class FastHashMapCaptchaStore extends HsMapCaptchaStore {
    @SuppressWarnings("unchecked")
    public FastHashMapCaptchaStore() {
        store = new FastHashMap();
    }
}
