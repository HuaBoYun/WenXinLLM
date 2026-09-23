package com.hbfk.util;

public class RandowUtil {
	private static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	
	
	public static String uuId() {
		long nextId = snowflakeIdWorker.nextId();
		return nextId+"";
	}
}
