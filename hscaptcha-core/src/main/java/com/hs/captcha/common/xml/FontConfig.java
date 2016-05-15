/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.common.xml;

import java.awt.Font;

import java.util.List;


/**
 *@FileName  FontConfig.java
 *@Date  16-5-2 下午8:37
 *@author Ma Yuanchao
 *@version 1.0
 */
public class FontConfig {
    private String dict;
    private List<Font> fontList;
    private int fontSizeMax;
    private int fontSizeMin;
    private int spacingSize;
    private int rotateRange;
    private float sinMin;
    private float sinMax;
    private float waveMin;
    private float waveMax;
	public String getDict() {
		return dict;
	}
	public void setDict(String dict) {
		this.dict = dict;
	}
	public List<Font> getFontList() {
		return fontList;
	}
	public void setFontList(List<Font> fontList) {
		this.fontList = fontList;
	}
	public int getFontSizeMax() {
		return fontSizeMax;
	}
	public void setFontSizeMax(int fontSizeMax) {
		this.fontSizeMax = fontSizeMax;
	}
	public int getFontSizeMin() {
		return fontSizeMin;
	}
	public void setFontSizeMin(int fontSizeMin) {
		this.fontSizeMin = fontSizeMin;
	}
	public int getSpacingSize() {
		return spacingSize;
	}
	public void setSpacingSize(int spacingSize) {
		this.spacingSize = spacingSize;
	}
	public int getRotateRange() {
		return rotateRange;
	}
	public void setRotateRange(int rotateRange) {
		this.rotateRange = rotateRange;
	}
	public float getSinMin() {
		return sinMin;
	}
	public void setSinMin(float sinMin) {
		this.sinMin = sinMin;
	}
	public float getSinMax() {
		return sinMax;
	}
	public void setSinMax(float sinMax) {
		this.sinMax = sinMax;
	}
	public float getWaveMin() {
		return waveMin;
	}
	public void setWaveMin(float waveMin) {
		this.waveMin = waveMin;
	}
	public float getWaveMax() {
		return waveMax;
	}
	public void setWaveMax(float waveMax) {
		this.waveMax = waveMax;
	}
}
