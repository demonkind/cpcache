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
 * 
 * @author zhanghaijie
 * @version $Id: CPMasterSlaveCacheClientImpl.java, v 0.1 2012-9-18 上午08:34:42 zhanghaijie Exp $
 */
public class CPMasterSlaveCacheClientImpl implements CPCacheClient {

    private static final Logger logger = Logger.getLogger(CPMasterSlaveCacheClientImpl.class);

    private MemcachedClient     masterMemcachedClient;

    private MemcachedClient     slaveMemcachedClient;

    private Integer             exipredTime;

    public CPMasterSlaveCacheClientImpl(String masterAddresses, String slaveAddresses,
                                        Integer exipredTime) {
        this.exipredTime = exipredTime;
        try {
            masterMemcachedClient = new MemcachedClient(AddrUtil.getAddresses(masterAddresses));
            slaveMemcachedClient = new MemcachedClient(AddrUtil.getAddresses(slaveAddresses));
            if (logger.isInfoEnabled()) {
                logger.info("【分布式缓存】分布式缓存初始化完毕，主集群IP：" + masterAddresses + " 备集群IP:"
                            + slaveAddresses);
            }
        } catch (IOException e) {
            logger.error("【分布式缓存】缓存客户端初始化失败,主集群:" + masterAddresses + " 备集群:" + slaveAddresses, e);
            throw new RuntimeException(e);
        }
    }

    /** 
     * @see com.huifu.cache.client.CPCacheClient#getObject(java.lang.String)
     */
    @Override
    public Object getObject(String key) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("【分布式缓存】主机缓存获取：key:" + key);
            }
            Object masterValue = masterMemcachedClient.get(key);
            if (null == masterValue) {
                if (logger.isInfoEnabled()) {
                    logger.info("【分布式缓存】主机缓存获取为空，备机缓存获取：key:" + key);
                }
                Object slaveValue = slaveAsyncGet(key);
                return slaveValue;
            }
            slaveMemcachedClient.set(key, exipredTime, masterValue);
            return masterValue;
        } catch (Exception e) {
            logger.error("【分布式缓存】主集群取值异常,转移至备份集群 key:" + key, e);
            Object slaveValue = slaveAsyncGet(key);
            return slaveValue;
        }
    }

    /**
     * 
     * @param key
     * @return
     */
    private Object slaveAsyncGet(String key) {
        if (logger.isInfoEnabled()) {
            logger.info("【分布式缓存】备机缓存获取，key:" + key);
        }
        Object slaveValue = slaveMemcachedClient.get(key);
        if (null != slaveValue) {
            if (logger.isInfoEnabled()) {
                logger.info("【分布式缓存】备机数据同步至主机,key " + key);
            }
            masterMemcachedClient.set(key, exipredTime, slaveValue);
        }
        return slaveValue;
    }

    /** 
     * @see com.huifu.cache.client.CPCacheClient#removeObject(java.lang.String)
     */
    @Override
    public void removeObject(String key) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("【分布式缓存】主机缓存删除，key:" + key);
            }
            masterMemcachedClient.delete(key);
        } catch (Exception e) {
            logger.error("【分布式缓存】主集群删除值失败,KEY:" + key, e);
        }
        try {
            if (logger.isInfoEnabled()) {
                logger.info("【分布式缓存】备机缓存删除，key:" + key);
            }
            slaveMemcachedClient.delete(key);
        } catch (Exception e) {
            logger.error("【分布式缓存】子集群删除值失败,KEY:" + key, e);
        }
    }

    /** e;
     * @see com.huifu.cache.client.CPCacheClient#putObject(java.lang.String, java.lang.Object, int)
     */
    @Override
    public void putObject(String key, Object data, int exp) {
        if (logger.isInfoEnabled()) {
            logger.info("【分布式缓存】主机缓存添加，key:" + key);
        }
        masterMemcachedClient.set(key, exp, data);

        if (logger.isInfoEnabled()) {
            logger.info("【分布式缓存】备机缓存添加，key:" + key);
        }
        slaveMemcachedClient.set(key, exp, data);
    }

    /** 
     * @see com.huifu.cache.client.CPCacheClient#putObject(java.lang.String, java.lang.Object)
     */
    @Override
    public void putObject(String key, Object data) {
        putObject(key, data, exipredTime);
    }
}
