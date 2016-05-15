package com.hs.captcha.common.enums;

public enum CaptchaTypeEnum {
		   JPG60("JPG60"), 
		   JPG61("JPG61"), 
		   
		   GIF60("GIF61"), 
		   GIF61("GIF61"), 
		   
		 
		   JPG40("JPG40"), 
		   JPG41("JPG41"), 
		   
		   GIF40("GIF40"), 
		   GIF41("GIF41"), 
		   
		 
		   JPG("JPG"), 
		   
		   GIF("GIF");
		   
		   private final String captchaType;
		   
		   private CaptchaTypeEnum(String captchaType) { this.captchaType = captchaType; }
		   
		   public String getCaptchaType()
		   {
		     return captchaType;
		   }
		 }
