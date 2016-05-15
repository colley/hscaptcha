/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.model;

import java.io.OutputStream;

import com.hs.captcha.component.image.ContentTypeEnum;


/**
 *@FileName  ImageCaptcha.java
 *@Date  16-5-3 下午12:46
 *@author Ma Yuanchao
 *@version 1.0
 */
public class ImageCaptcha implements HsCaptcha {
    private static final long serialVersionUID = -5180820899393600323L;
    private Boolean hasChallengeBeenCalled = Boolean.FALSE;
    protected String code;
    protected transient OutputStream challenge;
    protected ContentTypeEnum contentType;
        
    
    public ImageCaptcha(){}
    
    public ImageCaptcha(String code){
    	this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

    public OutputStream getChallenge() {
        this.hasChallengeBeenCalled = Boolean.TRUE;
        return challenge;
    }

    public final Boolean validateResponse(Object response) {
        return ((null != response) && (response instanceof String)) ? validateResponse((String) response) : Boolean.FALSE;
    }

    private final Boolean validateResponse(String response) {
        return Boolean.valueOf(response.equalsIgnoreCase(this.code));
    }

    public void disposeChallenge() {
        challenge = null;
    }

    public Boolean hasGetChalengeBeenCalled() {
        return hasChallengeBeenCalled;
    }

	public ContentTypeEnum getContentType() {
		return this.contentType;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setChallenge(OutputStream challenge) {
		this.challenge = challenge;
	}

	public void setContentType(ContentTypeEnum contentType) {
		this.contentType = contentType;
	}
}
