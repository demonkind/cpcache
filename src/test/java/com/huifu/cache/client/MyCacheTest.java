/**
 * Chinapnr.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.huifu.cache.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.huifu.cache.client.CPCacheClient;
import com.huifu.cache.client.impl.CPCacheClientImpl;

/**
 * 
 * @author su.zhang
 * @version $Id: MyCacheTest.java, v 0.1 2012-9-1 上午10:42:20 su.zhang Exp $
 */
public class MyCacheTest {
    
    public static void main(String[] args) {
        
        
        CPCacheClient cpcache= new CPCacheClientImpl("192.168.1.230:11211");
        
        boolean flag=false;
        List<MyDTO> list= new ArrayList<MyDTO>();
        long a= System.currentTimeMillis();
        for(int i=0;i<360 && flag ;i++){
            MyDTO d= new MyDTO();
            d.setAttr1("11111111111111111111");
            d.setAttr2("11111111111111111111");
            d.setAttr3("11111111111111111111");
            d.setAttr4("11111111111111111111");
            d.setAttr5("11111111111111111111");
            d.setAttr6("11111111111111111111");
            d.setAttr7("11111111111111111111");
            d.setAttr8("11111111111111111111");
            d.setAttr9("11111111111111111111");
            d.setAttr10("11111111111111111111");
            d.setAttr11("11111111111111111111");
            d.setAttr12("11111111111111111111");
            d.setAttr13("11111111111111111111");
            d.setAttr14("11111111111111111111");
            d.setAttr15("11111111111111111111");
            list.add(d);
        }
        long b= System.currentTimeMillis();
        // Store a value (async) for one hour
        if(flag){
            long c= System.currentTimeMillis();
            for(int i=0;i<1501;i++){
                cpcache.putObject("someKey"+i, list, 36000);
            }
            long d= System.currentTimeMillis();
            System.out.println("set end:"+(d-c));
            
            return;
            
        }

        for(int i=0;i<500;i++){
            ThreadTest t = new ThreadTest(cpcache);
            t.start();
        }
       
    }
    
}
class ThreadTest extends Thread {
    public static int tnum=0;
    CPCacheClient cpcache;
    public ThreadTest(CPCacheClient cpcache) {
        this.cpcache=cpcache;
        tnum++;
        System.out.println("当前总线程数："+tnum);
    }
 
    public void run() {
        while(true){
            Random r = new Random();
            long c= System.currentTimeMillis();
            String key ="someKey"+r.nextInt(1500);
            Object myObject = cpcache.getObject(key);
            long d= System.currentTimeMillis();
            List<MyDTO> mylist=(List<MyDTO>) myObject;
            System.out.println("获取耗时："+key+"--------------------------------------------------------"+(d-c)+"---------当前时间"+new Date());
            System.out.println("集合大小："+mylist.size());
        }
      
    }
 
    
 
}
