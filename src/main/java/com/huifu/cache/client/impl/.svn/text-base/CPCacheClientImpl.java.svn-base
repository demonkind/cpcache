/**
 * 
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.cache.client.impl;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;

import com.huifu.cache.client.CPCacheClient;

/**
 * @author su.zhang
 * @version $Id: CPCacheClient.java, v 0.1 2012-8-21 下午05:03:47 Administrator Exp $
 * 
 * example:
 // Get a memcached client connected to several servers
MemcachedClient c=new MemcachedClient(
        AddrUtil.getAddresses("server1:11211 server2:11211"));

// Try to get a value, for up to 5 seconds, and cancel if it doesn't return
Object myObj=null;
Future<Object> f=c.asyncGet("someKey");
try {
    myObj=f.get(5, TimeUnit.SECONDS);
} catch(TimeoutException e) {
    // Since we don't need this, go ahead and cancel the operation.  This
    // is not strictly necessary, but it'll save some work on the server.
    f.cancel(false);
    // Do other timeout related stuff
}
 * 
 */
public class CPCacheClientImpl implements CPCacheClient {

    private static final Logger logger = Logger.getLogger(CPCacheClientImpl.class);

    private String              cpAddress;
    private String              defaultExpiredTime;
    private MemcachedClient     memcachedClient;

    public CPCacheClientImpl() {
    }

    public CPCacheClientImpl(String cpAddress) {
        setCpAddress(cpAddress);
    }

    public void removeObject(String key) {
        if (logger.isInfoEnabled()) {
            logger.info("【分布式缓存】缓存删除，key " + key);
        }
        memcachedClient.delete(key);
    }

    /** 
     * @see com.chinapnr.cache.client.CPCacheClient#getObject(java.lang.String)
     */
    public Object getObject(String key) {
        if (logger.isInfoEnabled()) {
            logger.info("【分布式缓存】缓存获取，key " + key);
        }
        return memcachedClient.get(key);
    }

    /** 
     * @see com.chinapnr.cache.client.CPCacheClient#putObjectExpire(java.lang.String, java.lang.Object, int)
     */
    public void putObject(String key, Object data, int exp) {
        if (logger.isInfoEnabled()) {
            logger.info("【分布式缓存】缓存设置,key " + key);
        }
        memcachedClient.set(key, exp, data);
    }

    public void putObject(String key, Object data) {
        putObject(key, data, Integer.valueOf(defaultExpiredTime));
    }

    /**
     * Setter method for property <tt>cpAddress</tt>.
     * 
     * @param cpAddress value to be assigned to property cpAddress
     */
    public void setCpAddress(String cpAddress) {
        this.cpAddress = cpAddress;
        try {
            if (logger.isInfoEnabled()) {
                logger.info("【分布式缓存】分布式缓存初始化，集群地址:" + cpAddress);
            }
            memcachedClient = new MemcachedClient(AddrUtil.getAddresses(cpAddress));
        } catch (IOException e) {
            logger.error("【分布式缓存】分布式缓存初始化失败,集群地址:" + cpAddress);
            throw new RuntimeException(e);
        }
    }

    public void setDefaultExpiredTime(String defaultExpiredTime) {
        this.defaultExpiredTime = defaultExpiredTime;
    }
}
