/**
 * 
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.cache.client;

/**
 * 
 * 分布式缓存客户端
 * @author su.zhang
 * @version $Id: CPCacheClient.java, v 0.1 2012-8-21 下午05:03:47 su.zhang Exp $
 * 
 * 
 */
public interface CPCacheClient {

    /**
     * 根据key获取对象
     * 
     * @param key
     * @return 必须序列化
     */
    public Object getObject(String key);

    /**
     * 根据key删除对象
     * 
     * @param key
     */
    public void removeObject(String key);

    /**
     * @param key
     * @param data  必须序列化
     * @param exp 超时时间，以秒为单位
     */
    public void putObject(String key, Object data, int exp);

    /**
     * 
     * @param key
     * @param data  必须序列化
     */
    public void putObject(String key, Object data);

}
