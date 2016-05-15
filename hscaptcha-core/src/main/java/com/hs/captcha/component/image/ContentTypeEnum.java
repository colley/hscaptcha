 package com.hs.captcha.component.image;
 
 public enum ContentTypeEnum
 {
   JPEG("image/jpeg"),  GIF("image/gif"),  MP3("mp3"),  WAV("wav");
   
   private final String contentType;
   
   private ContentTypeEnum(String contentType)
   {
     this.contentType = contentType;
   }
   
   public String getContentType()
   {
     return this.contentType;
   }
 }