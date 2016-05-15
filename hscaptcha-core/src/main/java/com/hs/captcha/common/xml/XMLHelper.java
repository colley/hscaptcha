/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.hs.captcha.common.xml;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hs.common.xml.XMLHsMakeup;
import com.hs.common.xml.XMLUtilHelper;


/**
 *@FileName  XMLHelper.java
 *@Date  16-4-13 下午5:58
 *@author Ma Yuanchao
 *@version 1.0
 */
public class XMLHelper {
    public static final Log logger = LogFactory.getLog(XMLHelper.class);
    private static final String CONFIG_NAME = "captcha/captchaConfig.xml";
    private static XMLHelper instance = null;
    private static Captchas captchas = null;
    private static Boolean isInit = Boolean.FALSE;
    private static Boolean isCacheInit = Boolean.FALSE;

    public static XMLHelper getInstance() {
        if (isInit.equals(Boolean.FALSE)) {
            synchronized (isInit) {
                if (isInit.equals(Boolean.FALSE)) {
                    isInit = Boolean.TRUE;
                }
            }

            instance = new XMLHelper();
        }

        return instance;
    }

    public Captchas getCaptchaConfig() {
        if (captchas == null) {
            synchronized (isCacheInit) {
                if (isCacheInit.equals(Boolean.FALSE)) {
                    try {
                        captchas = parseXml();
                        isCacheInit = Boolean.TRUE;
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        return captchas;
    }

    public Captchas parseXml() throws Exception {
        String captchaFileName = CONFIG_NAME;
        if (!StringUtils.isBlank(System.getProperty("hs.captcha.filename"))) {
            captchaFileName = System.getProperty("hs.captcha.filename").trim();
            logger.warn("Use the Specified cache.xml file:" + captchaFileName);
        }

        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(captchaFileName);
        if (stream == null) {
            throw new IllegalArgumentException(captchaFileName + " not exits!");
        }

        Captchas captchas = new Captchas();
        XMLHsMakeup XMLHsMakeup = XMLUtilHelper.getDataFromStream(stream);
        parseCaptUser(XMLHsMakeup, captchas);
        parseCaptcha(XMLHsMakeup, captchas);
        
        return captchas;
    }

    @SuppressWarnings("unchecked")
    protected void parseCaptUser(XMLHsMakeup XMLHsMakeup, Captchas captchas) {
        Map<String, Object> user = new HashMap<String, Object>();
        getElementIterator(user, XMLHsMakeup.getChild("user")[0]);
        List<Map<String, Object>> userList = (List<Map<String, Object>>) ((Map<String, Object>) user.get("user")).get(
                "list");
        for (Map<String, Object> captUser : userList) {
            String userName = (String) captUser.get("userName");
            String captCode = (String) captUser.get("captCode");
            if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(captCode)) {
                captchas.addUser(userName, captCode);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void parseCaptcha(XMLHsMakeup XMLHsMakeup, Captchas captchas) {
        Map<String, Object> captcha = new HashMap<String, Object>();
        XMLHsMakeup[] captchaMakeup = XMLHsMakeup.getChild("captcha");
        for (int i = 0; i < captchaMakeup.length; i++) {
            getElementIterator(captcha, captchaMakeup[i]);
        }

        @SuppressWarnings("rawtypes")
		List<Map> captchaList = (List<Map>) captcha.get("captcha");
        for (Map<String, Object> capt : captchaList) {
            String fontName = (String) capt.get("fontName");
            String dict = (String) capt.get("dict");
            
            int fontSizeMax = Integer.parseInt((String) capt.get("fontSizeMax"));
            int fontSizeMin = Integer.parseInt((String) capt.get("fontSizeMin"));
            int spacingSize = Integer.parseInt((String) capt.get("spacingSize"));
            int rotateRange = Integer.parseInt((String) capt.get("rotateRange"));
            float sinMin = Float.parseFloat((String) capt.get("sinMin"));
            float sinMax = Float.parseFloat((String) capt.get("sinMax"));
            float waveMin = Float.parseFloat((String) capt.get("waveMin"));
            float waveMax = Float.parseFloat((String) capt.get("waveMax"));
            FontConfig config = new FontConfig();
            config.setDict(dict);
            config.setFontSizeMax(fontSizeMax);
            config.setFontSizeMin(fontSizeMin);
            config.setSpacingSize(spacingSize);
            config.setRotateRange(rotateRange);
            config.setSinMin(sinMin);
            config.setSinMax(sinMax);
            config.setWaveMin(waveMin);
            config.setWaveMax(waveMax);
            if ((StringUtils.isNotBlank(fontName)) && (fontSizeMax > 0) && (fontSizeMin > 0)) {
                List<Font> fontList = new ArrayList<Font>();
                fontList = fileToFontList(fontName, fontSizeMin, fontSizeMax);
                config.setFontList(fontList);
            }

            captchas.addCaptcha(config);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void getElementIterator(Map<String, Object> rootMap, XMLHsMakeup element) {
        Map<String, Object> elemMap = new HashMap<String, Object>();
        List<XMLHsMakeup> iter = element.getChildren();
        for (XMLHsMakeup x : iter) {
            String nodeName =StringUtils.trim(x.getName());
            
            //System.out.println(childEle.elements().size());
            if (x.getChildren().size() == 0) {
                String nodeValue = x.getText();
                elemMap.put(nodeName, nodeValue);
            } else {
                getElementIterator(elemMap, x);
            }
        }

        if (!elemMap.isEmpty()) {
            String rootEleName = StringUtils.trim(element.getName());
            if (rootMap.containsKey(rootEleName)) {
                Object obj = rootMap.get(rootEleName);
                if (obj instanceof List) {
                    List listData = (List) obj;
                    listData.add(elemMap);
                    rootMap.put(rootEleName, listData);
                } else if (obj instanceof Map) {
                    List nList = new ArrayList();
                    nList.add(obj);
                    nList.add(elemMap);
                    rootMap.put(rootEleName, nList);
                }
            } else {
                rootMap.put(rootEleName, elemMap);
            }
        }
    }

    private static List<Font> fileToFontList(String fontName, int fontSizeMin, int fontSizeMax) {
        List<Font> fontList = new ArrayList<Font>();
        try {
            Font font = new Font(fontName, 0, 10);
            for (int i = fontSizeMin; i <= fontSizeMax; i++) {
                fontList.add(font.deriveFont(0, i));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return fontList;
    }
}
