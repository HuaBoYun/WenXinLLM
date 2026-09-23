package com.huabo.financialdata.config.redis.entity;

/**
 * RedisKey绑定对象基类
 *
 * @author lee
 * @version 1.0.0
 */

public abstract class RedisKeyBindingObject {
    /**
     * 默认前缀
     */
    protected static String preFixStart = "calorie:";

    protected String name;

    protected String prefix = "";

    protected RedisKeyNamespace keyNamespace = null;

    protected RedisKeyBindingObject(String name, String prefix) {
        this.name = name;
        this.setPrefix(prefix);
    }

    public String getRedisKey() {
        if (this.keyNamespace == null) {
            return this.prefix + this.name;
        } else {
            return this.prefix + this.keyNamespace.key(this.name);
        }
    }

    public void setPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }

        this.prefix = prefix;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefixEnabled(boolean prefixEnabled) {
        if (!prefixEnabled) {
            this.prefix = "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RedisKeyBindingObject)) {
            return false;
        }

        return ((RedisKeyBindingObject) o).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    public void setKeyNamespace(RedisKeyNamespace redisKeyGroup) {
        this.keyNamespace = redisKeyGroup;
    }

    protected RedisKeyNamespace getKeyNamespace() {
        return this.keyNamespace;
    }
}
