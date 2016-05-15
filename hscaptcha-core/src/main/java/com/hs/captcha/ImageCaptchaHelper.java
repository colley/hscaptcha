/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hs.captcha.component.image.ContentTypeEnum;
import com.hs.captcha.service.HsCaptchaService;


/**
 *@FileName  ImageToJpegHelper.java
 *@Date  16-5-3 上午10:02
 *@author Ma Yuanchao
 *@version 1.0
 */
public class ImageCaptchaHelper {
    private static Log log = LogFactory.getLog(ImageCaptchaHelper.class);

	public static void flushNewCaptchaToResponse(HttpServletRequest theRequest, HttpServletResponse theResponse,
        HsCaptchaService service, String id) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream challenge = null;
        try {
        	challenge = (ByteArrayOutputStream) service.getImageChallengeForID(id);
        } catch (IllegalArgumentException e) {
            if ((log != null) && (log.isWarnEnabled())) {
                log.warn("There was a try from " + theRequest.getRemoteAddr() +
                    " to render an captcha with invalid ID :'" + id + "' or with a too long one");
                theResponse.sendError(404);
                return;
            }
        } catch (Exception e) {
            if ((log != null) && (log.isWarnEnabled())) {
                log.warn("Error trying to generate a captcha and render its challenge as JPEG", e);
            }

            theResponse.sendError(404);
            return;
        }
        if(challenge == null){
        	if ((log != null) && (log.isWarnEnabled())) {
                log.warn("Error trying to generate a captcha and render its challenge as JPEG,challenge is null");
            }
            theResponse.sendError(404);
            return;
        }

        captchaChallengeAsJpeg = challenge.toByteArray();
        theResponse.setHeader("Cache-Control", "no-store");
        theResponse.setHeader("Pragma", "no-cache");
        theResponse.setDateHeader("Expires", 0L);
        theResponse.setContentType(ContentTypeEnum.JPEG.getContentType());
        ServletOutputStream responseOutputStream = theResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
