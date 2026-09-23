package com.huabo.financialdata.config.redis.service;

import java.util.List;
import java.util.Map;

/**
 * Redis Cache Service接口
 *
 * @author lee
 * @version 1.0.0
 **/
public interface IRedisCacheService {

    /**
     * Redis Set
     * 将字符串值 value 关联到 key 。
     * 如果 key 已经持有其他值， SET 就覆写旧值， 无视类型。
     *
     * @param key   redis Key
     * @param value Key对应的值
     */
    void set(String key, Object value);

    /**
     * Redis Set，同时设置过期时间
     * 将字符串值 value 关联到 key 。
     * 如果 key 已经持有其他值， SET 就覆写旧值， 无视类型。
     * 当 SET 命令对一个带有生存时间（TTL）的键进行设置之后， 该键原有的 TTL 将被清除。
     *
     * @param key    redis Key
     * @param value  Key对应的值
     * @param expire 过期时间（秒）
     */
    void set(final String key, Object value, long expire);

    /**
     * 通过Redis setNx防止重复被Set，也起到一定的锁的作用。
     *
     * @param key    redis key
     * @param value  值
     * @param expire 过期时间
     * @return 返回结果
     */

    boolean setNx(final String key, Object value, long expire);

    /**
     * @param pattern 参数
     * @return 返回结果
     */
    String[] keys(final String pattern);

    /**
     * 从Redis中获取key的缓存值
     *
     * @param key redis key
     * @return 返回结果
     */
    String get(final String key);


    /**
     * 从Redis中获取缓存值，其中tpl为缓存对象的类型，最终返回tpl类型的对象
     *
     * @param key redis key
     * @param tpl 缓存对象的类型
     * @return 返回结果
     */
    <T> T get(final String key, final Class<T> tpl);

    /**
     * 从Redis中获取缓存值，其中tpl为缓存对象的类型，最终返回tpl类型的对象
     *
     * @param key redis key
     * @param tpl 缓存对象的类型
     * @return 返回结果
     */
    <T> T getSso(final String key, final Class<T> tpl);

    /**
     * 从Redis中获取缓存值，其中tpl为缓存对象的类型，最终返回tpl类型的对象集合
     *
     * @param keys redis key 集合
     * @param tpl  缓存对象的类型
     * @return 返回结果
     */
    <T> List<T> get(List<String> keys, Class<T> tpl);

    /**
     * getSet
     *
     * @param key    redis key 集合
     * @param value  值
     * @param expire 过期时间
     * @param tpl    缓存对象的类型
     * @return 返回结果
     */
    <T> T getSet(final String key, final Object value, final long expire, final Class<T> tpl);

    /**
     * 从Redis中获取List格式的缓存值，其中tpl为List中存的Object的类型
     *
     * @param key redis key 集合
     * @param tpl List中存的Object类型
     * @return 返回结果
     */
    <T> List<T> getList(final String key, final Class<T> tpl);

    /**
     * 判定Redis中Key是否存在
     *
     * @param key redis key 集合
     * @return 返回结果
     */
    boolean exists(final String key);


    /**
     * 批量删除RedisKey
     *
     * @param keys 一个或多个RedisKey值
     */
    void delete(final String... keys);

    /**
     * 设置Key的有效时间
     *
     * @param key    redis key
     * @param expire 过期时间
     */
    void expire(final String key, long expire);

    /**
     * 设置Key的有效时间，设置成功返回True,设置失败返回False
     *
     * @param key    redis key
     * @param expire 过期时间
     * @return
     */
    boolean expireKey(final String key, final long expire);

    /**
     * 哈希操作-hSet
     * 设置Hash中field的值， 如已存在，将替换原有field值，如不存在直接保存；
     * 该方法不对key的有效期进行修改（即如有效期保留不变）
     *
     * @param key   redis key
     * @param field Hash中field的值
     * @param value 值
     */
    void hSet(final String key, final String field, final Object value);

    /**
     * 哈希操作-hSet
     * 设置Hash中field的值， 如已存在，将替换原有field值，如不存在直接保存；
     * 该方法将对key的有效期进行修改，即将Key的有效性期为expire
     *
     * @param key    redis key
     * @param field  Hash中field的值
     * @param value  值
     * @param expire 过期时间
     */
    void hSet(final String key, final String field, final Object value, final long expire);

    /**
     * 哈希操作-hMSet
     * 批量设置Hash中field的值， 如已存在，将替换原有field值，如不存在直接保存；
     * 该方法不对key的有效期进行修改（即如有效期保留不变）
     *
     * @param key    redis key
     * @param values 值
     */
    void hMset(final String key, final Map<String, Object> values);

    /**
     * 哈希操作-hMSet
     * 批量设置Hash中field的值， 如已存在，将替换原有field值，如不存在直接保存；
     * 该方法将对key的有效期进行修改，即将Key的有效性期为expire
     *
     * @param key    redis key
     * @param values 值
     * @param expire 过期时间
     */
    void hMset(final String key, final Map<String, Object> values, final long expire);

    /**
     * 哈希操作-hSetNx
     * 设置Hash中field的值， 如已存在，将不操作, 返回false, 如不存在直接保存, 返回true；
     * 该方法不对key的有效期进行修改（即如有效期保留不变）
     *
     * @param key      redis key
     * @param fieldKey Hash中field的值
     * @param value    值
     * @return 返回结果
     */
    boolean hSetNx(final String key, final String fieldKey, final Object value);

    /**
     * 哈希操作-hSetNx
     * 设置Hash中field的值， 如已存在，将不操作, 返回false, 如不存在直接保存, 返回true；
     * 该方法将对key的有效期进行修改，即将Key的有效性期为expire
     *
     * @param key      redis key
     * @param fieldKey Hash中field的值
     * @param value    值
     * @param expire   过期时间
     * @return 返回结果
     */
    boolean hSetNx(final String key, final String fieldKey, final Object value, final long expire);

    /**
     * 哈希操作-hDel
     * 删除Hash中field的值，不管field是否存在，如操作正常将返回true
     *
     * @param key      redis key
     * @param fieldKey Hash中field的值
     * @return 返回结果
     */
    boolean hDel(final String key, final String fieldKey);

    /**
     * 哈希操作-hMDel
     * 批量删除Hash中field的值，不管field是否存在，如操作正常将返回true
     *
     * @param key       redis key
     * @param fieldKeys Hash中field的值集合
     * @return 返回结果
     */
    boolean hMDel(final String key, final List<String> fieldKeys);

    /**
     * 哈希操作-hExist
     * 判定Hash中field是否存在，如存在返回true, 如不存在返回false
     *
     * @param key      redis key
     * @param fieldKey Hash中field的值
     * @return 返回结果
     */
    boolean hExist(final String key, final String fieldKey);

    /**
     * 哈希操作-hGet
     * 查找指定hash中的field的值，如不存在返回空。
     *
     * @param key   redis key
     * @param field Hash中field的值
     * @param clazz 类型
     * @return 返回结果
     */
    <T> T hGet(final String key, final String field, final Class<T> clazz);

    /**
     * 哈希操作-hMGet
     * 批量查找给定hash中的field的值，如指定field不存在，则空值将不放入返回List中。
     *
     * @param key    redis key
     * @param fields hash中的field的值集合
     * @param clazz  类型
     * @return 返回结果
     */
    <T> List<T> hMGet(final String key, final List<String> fields, final Class<T> clazz);

    /**
     * 哈希操作-hMGet
     * 全量查找给定hash中的所有field的值，如指定field不存在，则空值将不放入返回List中。
     *
     * @param key   redis key
     * @param clazz 类型
     * @return 返回结果
     */
    <T> List<T> hMGet(final String key, final Class<T> clazz);


    /**
     * 从Redis中获取缓存值，其中tpl为缓存对象的类型，最终返回tpl类型的对象集合
     *
     * @param keys redis key
     * @param tpl  缓存对象的类型
     * @param <T>  类型
     * @return 返回结果
     */
    <T> List<T> pipelined(List<String> keys, Class<T> tpl);

}
