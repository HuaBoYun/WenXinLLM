package com.hbfk.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	
	public final static String USERINFOKEY = "account";
	public final static String SUBMITMESSAGE = "submitMessage";
	public final static String TIPRECTIFICATIONPLAN = "tipPrecitificationPlan";
	public final static String PRESSKEY = "presskey";
	public static final String DISTRIKEY = "distribution";//业务数据下发办理存储的redis的key
	
	private String redisAddressIp;
	private Integer redisAddressPort;
	private String redisPwd;
	private Integer redisMaxTotal;
	
	private Integer redisMaxIdle;
	private Integer redisMaxWait;
	private Integer redisTimeOut;
	private String redisTextonBorrow;
	
	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
	static {
		try {
			//Properties properties = loadPropertyFile("application-dev.yml");
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(new ClassPathResource("application-dev.yml"));
			Properties properties = yaml.getObject();
			
			String redisAddressIp = properties.getProperty("spring.redis.host");
			Integer redisAddressPort = Integer.parseInt(properties.getProperty("spring.redis.port"));
			String redisPwd = properties.getProperty("spring.redis.password");
			Integer redisMaxTotal = 512;
			Integer redisMaxIdle = 100;
			Integer redisMaxWait = 10000;
			Integer redisTimeOut = 1000;
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(redisMaxTotal);
			config.setMaxIdle(redisMaxIdle);
			config.setMaxWaitMillis(redisMaxWait);
			config.setTestOnBorrow(true);
			if(StringUtils.isBlank(redisPwd)) {
				jedisPool = new JedisPool(config, redisAddressIp, redisAddressPort, redisTimeOut,null);
			}else {
				jedisPool = new JedisPool(config, redisAddressIp, redisAddressPort, redisTimeOut,redisPwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			jedis.close();
		}
	}
	/**
	 * 将数据存放到redis
	 * @param key
	 * @param value
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
		str  = jedis.get("test");
		System.out.println(str);
		jedis.del("test");
		JedisUtil.returnResource(jedis);
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