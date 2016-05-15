/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.component;

import java.security.SecureRandom;
import java.util.Random;


/**
 * 随机生产验证码的code
 *@FileName  RandomWordGenerator.java
 *@Date  16-5-3 上午10:36
 *@author Ma Yuanchao
 *@version 1.0
 */
public class RandomWordGenerator implements HsWordGenerator {
    private char[] possiblesChars;
    private Random myRandom = new SecureRandom();

    public RandomWordGenerator(String acceptedChars) {
        possiblesChars = acceptedChars.toCharArray();
    }

    public String getWord(Integer codeLenth) {
        StringBuffer word = new StringBuffer(codeLenth.intValue());
        for (int i = 0; i < codeLenth.intValue(); i++) {
            word.append(possiblesChars[myRandom.nextInt(possiblesChars.length)]);
        }

        return word.toString();
    }
}
