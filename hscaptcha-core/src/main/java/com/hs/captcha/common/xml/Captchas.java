/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *@FileName  Captchas.java
 *@Date  16-5-3 下午2:06
 *@author Ma Yuanchao
 *@version 1.0
 */
public class Captchas {
    private List<FontConfig> list = new ArrayList<FontConfig>();
    private Map<String,String> users = new HashMap<String,String>();

    public void addCaptcha(FontConfig captcha) {
        this.list.add(captcha);
    }

    public List<FontConfig> getCaptchas() {
        return this.list;
    }

    public void addUser(String userName,String captCode) {
        this.users.put(userName, captCode);
    }

    public boolean containsKeyAndVal(String userName,Object captCode) {
    	if(captCode!=null && captCode instanceof String){
    		 return this.users.containsKey(userName)&& this.users.containsValue(captCode);
    	}
       return false;
    }
}
