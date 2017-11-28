package com.huifu.cache.client;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		MemcachedClient c=new MemcachedClient(
		        AddrUtil.getAddresses("192.168.1.230:11211"));
		MyDTO d= new MyDTO();
		d.setAttr1("1111");
	//	d.setAttr2(11);
		System.out.println(d);
		// Store a value (async) for one hour
		c.set("someKey", 3600, d);
		// Retrieve a value (synchronously).
		Object myObject = c.get("someKey");
		System.out.println(myObject); 
	}

}
