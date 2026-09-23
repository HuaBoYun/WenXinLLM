package com.management.accountant.util;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedisUtil {

	public final static String USERINFOKEY = "account";
	
	private String redisAddressIp;
	private Integer redisAddressPort;
	private String redisPwd;
	private Integer redisMaxTotal;
	private Integer redisMaxIdle;
	private Integer redisMaxWait;
	private Integer redisTimeOut;
	private String redisTextonBorrow;
	

	/*//Redis服务器IP
	private static String ADDR = "192.0.2.200";

	//Redis的端口号
	private static int PORT = 6379;

	//访问密码,若你的redis服务器没有设置密码，就不需要用密码去连接
	private static String AUTH = "123456";

	//可用连接实例的最大数目，默认值为8；
	private static int MAX_TOTAL = 512;

	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
	private static int MAX_IDLE = 100;

	//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。
	private static int MAX_WAIT = 10000;

	private static int TIMEOUT = 10000;

	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；；
	private static boolean TEST_ON_BORROW = true;*/

	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
	static {
		try {
			Properties properties = loadPropertyFile("application-dev.yml");
			String redisAddressIp = trimOrNull(properties.getProperty("redisAddressIp"));
			String redisAddressPortStr = trimOrNull(properties.getProperty("redisAddressPort"));
			String redisPwd = trimOrNull(properties.getProperty("redisPwd"));
			String redisMaxTotalStr = trimOrNull(properties.getProperty("redisMaxTotal"));
			String redisMaxIdleStr = trimOrNull(properties.getProperty("redisMaxIdle"));
			String redisMaxWaitStr = trimOrNull(properties.getProperty("redisMaxWait"));
			String redisTimeOutStr = trimOrNull(properties.getProperty("redisTimeOut"));
			String redisTextonBorrow = trimOrNull(properties.getProperty("redisTextonBorrow"));

			if (redisAddressIp != null && redisAddressPortStr != null) {
				Integer redisAddressPort = Integer.parseInt(redisAddressPortStr);
				Integer redisMaxTotal = redisMaxTotalStr != null ? Integer.parseInt(redisMaxTotalStr) : 100;
				Integer redisMaxIdle = redisMaxIdleStr != null ? Integer.parseInt(redisMaxIdleStr) : 20;
				Integer redisMaxWait = redisMaxWaitStr != null ? Integer.parseInt(redisMaxWaitStr) : 10000;
				Integer redisTimeOut = redisTimeOutStr != null ? Integer.parseInt(redisTimeOutStr) : 10000;

				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(redisMaxTotal);
				config.setMaxIdle(redisMaxIdle);
				config.setMaxWaitMillis(redisMaxWait);
				config.setTestOnBorrow("true".equals(redisTextonBorrow));

				if (redisPwd != null && !redisPwd.isEmpty()) {
					jedisPool = new JedisPool(config, redisAddressIp, redisAddressPort, redisTimeOut, redisPwd);
				} else {
					jedisPool = new JedisPool(config, redisAddressIp, redisAddressPort, redisTimeOut);
				}

				// 测试连接并清理缓存
				Jedis jedis = JedisUtil.getJedis();
				if (jedis != null) {
					try {
						jedis.del("pybg");
					} finally {
						jedis.close();
					}
				}
			} else {
				System.err.println("[JedisUtil] Redis配置未找到，跳过连接池初始化");
			}
		} catch (Exception e) {
			System.err.println("[JedisUtil] Redis连接池初始化失败，服务将在无Redis模式下运行: " + e.getMessage());
		}
	}
	
	
	
	public static Properties loadPropertyFile(String fileName) {
		if (null == fileName || fileName.equals("")) {
			throw new IllegalArgumentException(
					"Properties file path can not be null: " + fileName);
		}

		InputStream inputStream = null;
		Properties properties = null;
		try {
			inputStream = JedisUtil.class.getClassLoader().getResourceAsStream(
					fileName);
			properties = new Properties();
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: "
					+ fileName);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Properties file can not be loading: " + fileName);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	private static String trimOrNull(String value) {
		if (value == null) return null;
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}



	/**
	 * 获取Jedis实例
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis jedis = jedisPool.getResource();
				return jedis;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
	/**
	 * 将数据存放到redis
	 * @param args
	 * @return
	 */
	public static void main(String []args){
		
		Jedis jedis = JedisUtil.getJedis();//获得redis客户端
		jedis.set("test","我是第一个程?，abc");
//		//Plcdata p1  =(Plcdata)SerializeUtil. unserialize(jedis.get("lastData".getBytes()));
		String str  = jedis.get("test");
//		
//		System.out.println(str);
//		jedis.set("test","我是第一个程?，json111111111111111111111");
//		str  = jedis.get("test");
		System.out.println(str);
//		JedisUtil.returnResource(jedis);
//		Jedis jedis = new Jedis("192.0.2.200", 6379);//初始化
//		jedis.set("key", "value1");//写入key-value
//		String value = jedis.get("key");//读取key-value
//		System.out.println("value:"+value);
//		jedis.disconnect();
//		jedis.close();
		
	}
	
	
	public String getRedisAddressIp() {
		return redisAddressIp;
	}

	public void setRedisAddressIp(String redisAddressIp) {
		this.redisAddressIp = redisAddressIp;
	}

	public Integer getRedisAddressPort() {
		return redisAddressPort;
	}

	public void setRedisAddressPort(Integer redisAddressPort) {
		this.redisAddressPort = redisAddressPort;
	}

	public String getRedisPwd() {
		return redisPwd;
	}

	public void setRedisPwd(String redisPwd) {
		this.redisPwd = redisPwd;
	}

	public Integer getRedisMaxTotal() {
		return redisMaxTotal;
	}

	public void setRedisMaxTotal(Integer redisMaxTotal) {
		this.redisMaxTotal = redisMaxTotal;
	}

	public Integer getRedisMaxIdle() {
		return redisMaxIdle;
	}

	public void setRedisMaxIdle(Integer redisMaxIdle) {
		this.redisMaxIdle = redisMaxIdle;
	}

	public Integer getRedisMaxWait() {
		return redisMaxWait;
	}

	public void setRedisMaxWait(Integer redisMaxWait) {
		this.redisMaxWait = redisMaxWait;
	}

	public Integer getRedisTimeOut() {
		return redisTimeOut;
	}

	public void setRedisTimeOut(Integer redisTimeOut) {
		this.redisTimeOut = redisTimeOut;
	}

	public String getRedisTextonBorrow() {
		return redisTextonBorrow;
	}

	public void setRedisTextonBorrow(String redisTextonBorrow) {
		this.redisTextonBorrow = redisTextonBorrow;
	}

	public static JedisPool getJedisPool() {
		return jedisPool;
	}

	public static void setJedisPool(JedisPool jedisPool) {
		JedisUtil.jedisPool = jedisPool;
	}

}