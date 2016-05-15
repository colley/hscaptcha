
package com.hs.captcha.component.captchastore;

public interface HsMemcachedProxy {

    /**
     * 直接从memcached获取数据
     *
     * @param <T>
     * @param key
     * @return
     */
    public <T> T get(String key);


    /**
     * 直接将数据保存至memcached
     *
     * @param <T>
     * @param key
     * @param value
     * @return
     */
    public <T> boolean set(String key, T value) ;

    /**
     * 直接将数据保存至memcached
     *
     * @param <T>
     * @param key
     * @param value
     * @param expired
     *            单位：秒
     * @return
     */
    public <T> boolean set(String key, T value, int expired) ;

    /**
     * 保存计数器<br>
     * 调用此方法保存之后，才可调用increase decrease方法
     *
     * @param key
     * @param inc
     * @return
     * @since 0.0.3
     */
    public boolean storeCounter(String key, long inc, int expired);
    /**
     * 获取计数器<br>
     * 调用storeCounter方法之后，才可调用本方法获取计数器
     *
     * @param key
     * @return
     * @since 0.0.3
     */
    public long getCounter(String key) ;

    /**
     * 该方法不支持本地缓存。<br>
     * 注意：<b>如果在调用increase方法之前，需要先调用storeCounter方法。<br>
     *
     * @param key
     * @param delta
     * @param initValue
     * @param expired
     * @return
     * @see #incr(String, long, long, long, int)
     * @since 0.0.3
     */
    public long increase(String key, long delta, long initValue, int expired);

    /**
     * 该方法不支持本地缓存 注意：<b>如果在调用increase方法之前，需要先调用storeCounter方法。<br>
     *
     * @param key
     * @param delta
     * @param initValue
     * @param timeout
     * @param expired
     * @return
     * @since 0.0.3
     */
    public long increase(String key, long delta, long initValue, long timeout, int expired) ;

    /**
     * 该方法不支持本地缓存。<br>
     * 注意：<b>在调用decrease方法之前，需要先确保已调用过storeCounter方法。<br>
     * 返回值为“-1”时，减量操作失败
     *
     * @param key
     * @param delta
     * @return
     * @since 0.0.3
     */
    public long decrease(String key, long delta) ;

    /**
     * 首先从memcached中删除
     *
     * @param key
     * @param timeout
     *            无效参数
     * @return
     */
    public boolean delete(String key) ;

    /**
     * 设置数据在memcached中的新的过期时间
     *
     * @param key
     * @param expired
     * @return
     */
    public boolean touch(String key, int expired) ;

}
