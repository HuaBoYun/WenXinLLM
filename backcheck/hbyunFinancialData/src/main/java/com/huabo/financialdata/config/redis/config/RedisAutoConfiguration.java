package com.huabo.financialdata.config.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huabo.financialdata.config.redis.service.IRedisService;
import com.huabo.financialdata.config.redis.service.impl.RedisServiceDefaultImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
//@PropertySource(value= {"classpath:calorie-redis.properties"})
public class RedisAutoConfiguration {

    @Value("${huabo.redis.host:192.0.2.200}")
    private String host;

    @Value("${huabo.redis.port:6379}")
    private int port;

    @Value("${huabo.redis.password:}")
    private String password;

    @Value("${huabo.redis.database:0}")
    private int database;

    @Value("${huabo.redis.maxActive:8}")
    private int maxActive;

    @Value("${huabo.redis.maxIdle:8}")
    private int maxIdle;

    @Value("${huabo.redis.maxWait:-1}")
    private long maxWait;

    @Value("${huabo.redis.minIdle:0}")
    private int minIdle;

    @Value("${huabo.redis.hosts:}")
    private String hosts;

    @Value("${huabo.redis.hoststype:sentinel}")
    private String hostsType;

    @Value("${huabo.redis.sentinel.master:masterName}")
    private String sentinelMaster;

    @Value("${huabo.redis.metrics.report.interval:300}")
    private int reportInterval;

    @Value("${huabo.redis.keys.normalize:true}")
    private boolean keysNormalize;

    @Value("${huabo.redis.keys.namespace:}")
    private String keysNamespaceStr;

    @Value("${huabo.redis.timeout:2000}")
    private int timeout;
    /**
     * 哨兵模式
     */
    public static final String SENTINEL = "sentinel";
    /**
     * Cluster模式
     */
    public static final String CLUSTER = "cluster";

    private static final int DEFAULT_CLUSTER_REDIRECTS = 3;

    private static final int METRICS_REPORT_INTERVAL_MIN = 10;


    /**
     * 获取redisTemplate Bean
     *
     * @return org.springframework.data.redis.core.RedisTemplate
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //设置序列化
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        //key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        //value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        //Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public IRedisService redisService(RedisTemplate redisTemplate) {
        IRedisService redisService = new RedisServiceDefaultImpl("", redisTemplate, false);
        return redisService;
    }

    /**
     * 将hosts主机解析成host清单,Host间用逗号“,”分隔
     * 格式: host1:port1,host2:port2,host3:port3
     *
     * @param hosts redis hosts 字符串
     * @return
     */
    private List<RedisNode> parseRedisNodes(String hosts) {
        List<RedisNode> ret = new ArrayList<RedisNode>();
        String[] parts = hosts.split(",");
        for (String sHost : parts) {
            if (StringUtils.isEmpty(sHost)) {
                continue;
            }
            String hostPart[] = sHost.split(":");
            if (hostPart.length != 2) {
                continue;
            }
            //host为空
            if (StringUtils.isEmpty(hostPart[0])) {
                continue;
            }
            String hostName = hostPart[0];
            int iPort = Integer.parseInt(hostPart[1]);
            ret.add(new RedisNode(hostName, iPort));
        }
        return ret;
    }

    /**
     * JedisConnectionFacotyr
     *
     * @return jedisConnectionFactory JedisConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(false);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(30 * 60000L);
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(jedisPoolConfig)
                .and()
                .readTimeout(Duration.ofMillis(timeout))
                .connectTimeout(Duration.ofMillis(timeout))
                .build();

        RedisConnectionFactory connectionFactory = null;
        //hosts不为空，则为集群模式
        if (StringUtils.hasText(hosts)) {
            List<RedisNode> hostAndPorts = parseRedisNodes(hosts);
            if (CollectionUtils.isEmpty(hostAndPorts)) {
                throw new IllegalArgumentException("Invalid property configuration [calorie.redis.hosts]");
            }
            //哨兵模式
            if (SENTINEL.equalsIgnoreCase(hostsType)) {
                RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
                sentinelConfiguration.setSentinels(hostAndPorts);
                sentinelConfiguration.setPassword(RedisPassword.of(password));
                sentinelConfiguration.setMaster(sentinelMaster);
                sentinelConfiguration.setDatabase(database);
                connectionFactory = new JedisConnectionFactory(sentinelConfiguration, jedisClientConfiguration);
            }
            //Cluster模式
            else if (CLUSTER.equalsIgnoreCase(hostsType)) {
                RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
                clusterConfiguration.setClusterNodes(hostAndPorts);
                clusterConfiguration.setPassword(RedisPassword.of(password));
                clusterConfiguration.setMaxRedirects(DEFAULT_CLUSTER_REDIRECTS);
                connectionFactory = new JedisConnectionFactory(clusterConfiguration, jedisClientConfiguration);
            }
        }
        //单机模式
        else {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName(host);
            redisStandaloneConfiguration.setPort(port);
            redisStandaloneConfiguration.setDatabase(database);
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
            connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
        }

        //模拟容器对其进行初始化
        return connectionFactory;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory);
        return builder.build();
    }
}