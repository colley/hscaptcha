/*
 * Copyright (c) 2016-2017 by colley
 * All rights reserved.
 */
package com.hs.captcha.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.collections.FastHashMap;

import com.hs.captcha.component.captchaengine.HsCaptchaEngine;
import com.hs.captcha.component.captchastore.CaptchaStore;
import com.hs.captcha.model.HsCaptcha;
import com.hs.captcha.model.ImageCaptcha;
import com.hs.captcha.service.HsCaptchaService;


/**
 *@FileName  AbstractCaptchaService.java
 *@Date  16-5-3 上午11:11
 *@author Ma Yuanchao
 *@version 1.0
 */
public abstract class AbstractCaptchaService implements HsCaptchaService {
	protected int minGuarantedStorageDelayInSeconds;
    protected int captchaStoreMaxSize;
    protected int captchaStoreSizeBeforeGarbageCollection;
    protected int numberOfGeneratedCaptchas = 0;
    protected int numberOfCorrectResponse = 0;
    protected int numberOfUncorrectResponse = 0;
    protected int numberOfGarbageCollectedCaptcha = 0;
    protected FastHashMap times;
    private long oldestCaptcha = 0L;
    protected CaptchaStore store;
    protected HsCaptchaEngine engine;

    protected AbstractCaptchaService(CaptchaStore store, HsCaptchaEngine engine) {
        this.store = store;
        this.engine = engine;
    }

    protected AbstractCaptchaService(CaptchaStore captchaStore, HsCaptchaEngine captchaEngine,
        int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize) {
        this(captchaStore, captchaEngine);
        setCaptchaStoreMaxSize(maxCaptchaStoreSize);
        setMinGuarantedStorageDelayInSeconds(minGuarantedStorageDelayInSeconds);
        setCaptchaStoreSizeBeforeGarbageCollection((int) Math.round(0.8D * maxCaptchaStoreSize));
        times = new FastHashMap();
    }

    protected AbstractCaptchaService(CaptchaStore captchaStore, HsCaptchaEngine captchaEngine,
        int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
        this(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize);
        if (maxCaptchaStoreSize < captchaStoreLoadBeforeGarbageCollection) {
            throw new IllegalArgumentException(
                "the max store size can't be less than garbage collection size. if you want to disable garbage collection (this is not recommended) you may set them equals (max=garbage)");
        }

        setCaptchaStoreSizeBeforeGarbageCollection(captchaStoreLoadBeforeGarbageCollection);
    }

    public String getCaptchaEngineClass() {
        return engine.getClass().getName();
    }

    public void setCaptchaEngineClass(String theClassName)
        throws IllegalArgumentException {
        try {
            Object engine = Class.forName(theClassName).newInstance();
            if ((engine instanceof HsCaptchaEngine)) {
                this.engine = ((HsCaptchaEngine) engine);
            } else {
                throw new IllegalArgumentException("Class is not instance of CaptchaEngine! " + theClassName);
            }
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public HsCaptchaEngine getEngine() {
        return engine;
    }

    public void setCaptchaEngine(HsCaptchaEngine engine) {
        this.engine = engine;
    }

    public int getMinGuarantedStorageDelayInSeconds() {
        return minGuarantedStorageDelayInSeconds;
    }

    public void setMinGuarantedStorageDelayInSeconds(int theMinGuarantedStorageDelayInSeconds) {
        minGuarantedStorageDelayInSeconds = theMinGuarantedStorageDelayInSeconds;
    }

    public long getNumberOfGeneratedCaptchas() {
        return numberOfGeneratedCaptchas;
    }

    public long getNumberOfCorrectResponses() {
        return numberOfCorrectResponse;
    }

    public long getNumberOfUncorrectResponses() {
        return numberOfUncorrectResponse;
    }

    public int getCaptchaStoreSize() {
        return store.getSize();
    }

    public int getNumberOfGarbageCollectableCaptchas() {
        return getGarbageCollectableCaptchaIds(System.currentTimeMillis()).size();
    }

    public long getNumberOfGarbageCollectedCaptcha() {
        return numberOfGarbageCollectedCaptcha;
    }

    public int getCaptchaStoreSizeBeforeGarbageCollection() {
        return captchaStoreSizeBeforeGarbageCollection;
    }

    public void setCaptchaStoreSizeBeforeGarbageCollection(int captchaStoreSizeBeforeGarbageCollection) {
        if (captchaStoreMaxSize < captchaStoreSizeBeforeGarbageCollection) {
            throw new IllegalArgumentException(
                "the max store size can't be less than garbage collection size. if you want to disable garbage collection (this is not recommended) you may set them equals (max=garbage)");
        }

        this.captchaStoreSizeBeforeGarbageCollection = captchaStoreSizeBeforeGarbageCollection;
    }

    public void setCaptchaStoreMaxSize(int size) {
        if (size < captchaStoreSizeBeforeGarbageCollection) {
            throw new IllegalArgumentException(
                "the max store size can't be less than garbage collection size. if you want to disable garbage collection (this is not recommended) you may set them equals (max=garbage)");
        }

        captchaStoreMaxSize = size;
    }

    public int getCaptchaStoreMaxSize() {
        return captchaStoreMaxSize;
    }

    @SuppressWarnings("rawtypes")
	protected void garbageCollectCaptchaStore(Iterator garbageCollectableCaptchaIds) {
        long now = System.currentTimeMillis();
        long limit = now - (1000 * minGuarantedStorageDelayInSeconds);

        while (garbageCollectableCaptchaIds.hasNext()) {
            String id = garbageCollectableCaptchaIds.next().toString();
            if (((Long) times.get(id)).longValue() < limit) {
                times.remove(id);

                store.removeCaptcha(id);

                numberOfGarbageCollectedCaptcha += 1;
            }
        }
    }

    @SuppressWarnings("rawtypes")
	public void garbageCollectCaptchaStore() {
        long now = System.currentTimeMillis();
        Collection garbageCollectableCaptchaIds = getGarbageCollectableCaptchaIds(now);
        garbageCollectCaptchaStore(garbageCollectableCaptchaIds.iterator());
    }

    public void emptyCaptchaStore() {
        store.empty();
        times = new FastHashMap();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Collection getGarbageCollectableCaptchaIds(long now) {
        HashSet garbageCollectableCaptchas = new HashSet();

        long limit = now - (1000 * getMinGuarantedStorageDelayInSeconds());
        if (limit > oldestCaptcha) {
            Iterator ids = new HashSet(times.keySet()).iterator();
            while (ids.hasNext()) {
                String id = (String) ids.next();
                long captchaDate = ((Long) times.get(id)).longValue();
                oldestCaptcha = Math.min(captchaDate, (oldestCaptcha == 0L) ? captchaDate : oldestCaptcha);
                if (captchaDate < limit) {
                    garbageCollectableCaptchas.add(id);
                }
            }
        }

        return garbageCollectableCaptchas;
    }

    @SuppressWarnings("rawtypes")
	protected HsCaptcha generateAndStoreCaptcha(String ID) {
        if (isCaptchaStoreFull()) {
            long now = System.currentTimeMillis();
            Collection garbageCollectableCaptchaIds = getGarbageCollectableCaptchaIds(now);
            if (garbageCollectableCaptchaIds.size() > 0) {
                garbageCollectCaptchaStore(garbageCollectableCaptchaIds.iterator());
                return generateAndStoreHsCaptcha(ID);
            }
        }

        if (isCaptchaStoreQuotaReached()) {
            garbageCollectCaptchaStore();
        }

        return generateCountTimeStampAndStoreCaptcha(ID);
    }

    private HsCaptcha generateCountTimeStampAndStoreCaptcha(String ID) {
        numberOfGeneratedCaptchas += 1;
        Long now = new Long(System.currentTimeMillis());
        times.put(ID, now);
        HsCaptcha captcha = generateAndStoreHsCaptcha(ID);
        return captcha;
    }

    protected boolean isCaptchaStoreFull() {
        return getCaptchaStoreMaxSize() != 0;
    }

    protected boolean isCaptchaStoreQuotaReached() {
        return getCaptchaStoreSize() >= getCaptchaStoreSizeBeforeGarbageCollection();
    }

    public Boolean validateResponseForID(String ID, Object response) {
    	  if (!store.hasCaptcha(ID)) {
              return false;
          }
          Boolean valid = store.getCaptcha(ID).validateResponse(response);
          store.removeCaptcha(ID);

        times.remove(ID);

        if (valid.booleanValue()) {
            numberOfCorrectResponse += 1;
        } else {
            numberOfUncorrectResponse += 1;
        }

        return valid;
    }

    public Object getChallengeForID(String ID) {
        HsCaptcha captcha  =generateAndStoreHsCaptcha(ID);
        Object challenge = getChallengeClone(captcha);
        captcha.disposeChallenge();
        return challenge;
    }
    
    public  HsCaptcha getHsCaptcha(String ID){
    	  HsCaptcha captcha  = null;
          if (!store.hasCaptcha(ID)) {
              captcha = generateAndStoreCaptcha(ID);
          } else {
              captcha = store.getCaptcha(ID);
              if (captcha == null) {
                  captcha = generateAndStoreCaptcha(ID);
              } else if (captcha.hasGetChalengeBeenCalled().booleanValue()) {
                  captcha = generateAndStoreCaptcha(ID);
              }
          }
          return captcha;
    }
    
    protected HsCaptcha generateAndStoreHsCaptcha(String id){
    	  HsCaptcha captcha = engine.getNextCaptcha();
    	   store.storeCaptcha(id, captcha);
    	   
    	   return captcha;
    }
    
    @SuppressWarnings("rawtypes")
	protected Object getChallengeClone(HsCaptcha captcha){
         Class captchaClass = captcha.getClass();
         if (ImageCaptcha.class.isAssignableFrom(captchaClass)) {
           ByteArrayOutputStream challenge = (ByteArrayOutputStream)captcha.getChallenge();
           ByteArrayOutputStream clone = new ByteArrayOutputStream();
           try {
			clone.write(challenge.toByteArray());
		} catch (IOException e) {
			 throw new IllegalArgumentException(e.getMessage());
		}
           return clone; 
         }
         throw new IllegalArgumentException("Unknown captcha type, can't clone challenge captchaClass:'" + captcha.getClass() + "'");
       }
  
}
