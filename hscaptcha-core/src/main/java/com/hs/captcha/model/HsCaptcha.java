/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.model;

import java.io.Serializable;

import com.hs.captcha.component.image.ContentTypeEnum;


/**
 *@FileName  HsCaptcha.java
 *@Date  16-5-3 上午10:14
 *@author Ma Yuanchao
 *@version 1.0
 */
public interface HsCaptcha extends Serializable {
    public abstract String getCode();

    public abstract Object getChallenge();

    public abstract Boolean validateResponse(Object paramObject);

    public abstract void disposeChallenge();

    public abstract Boolean hasGetChalengeBeenCalled();
    
    public abstract ContentTypeEnum getContentType();
}
