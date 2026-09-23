package com.huabo.bigmodel.config;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import okhttp3.Dns;

/**
 * IPv4 优先 + 重试 + 短 TTL 缓存的 DNS 解析器。
 *
 * 背景（两处真实故障，2026-09-03）：
 * 1. open.bigmodel.cn 的 DNS 会返回 AAAA(IPv6) 记录且排在 A 记录之前；本机 IPv6 无实际出口时，
 *    OkHttp 优先对 IPv6 发起 TCP 连接，Windows 协议栈上该连接挂起而非立即失败，
 *    耗满 connectTimeout（300 秒）才抛 "Failed to connect"。→ 解析时过滤只留 IPv4 解决。
 * 2. Windows 上 Java 的 DNS 查询偶发失败（UnknownHostException，消息即主机名），
 *    一次抖动就会报废整个分钟级的流式生成请求。→ 失败自动重试 + 成功结果短缓存解决。
 */
public class Ipv4FirstDns implements Dns {

    public static final Ipv4FirstDns INSTANCE = new Ipv4FirstDns();

    private static final int MAX_ATTEMPTS = 3;
    private static final long CACHE_TTL_MS = 60_000L;

    private static class CacheEntry {
        final List<InetAddress> addresses;
        final long cachedAt = System.currentTimeMillis();

        CacheEntry(List<InetAddress> addresses) {
            this.addresses = addresses;
        }

        boolean fresh() {
            return System.currentTimeMillis() - cachedAt < CACHE_TTL_MS;
        }
    }

    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        CacheEntry hit = cache.get(hostname);
        if (hit != null && hit.fresh()) {
            return hit.addresses;
        }

        UnknownHostException last = null;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                List<InetAddress> addresses = filterIpv4(Dns.SYSTEM.lookup(hostname));
                cache.put(hostname, new CacheEntry(addresses));
                return addresses;
            } catch (UnknownHostException e) {
                last = e;
                // DNS 查询偶发超时/丢包：短暂退避后重试
                try {
                    Thread.sleep(200L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
        throw last;
    }

    private List<InetAddress> filterIpv4(List<InetAddress> addresses) {
        List<InetAddress> ipv4 = addresses.stream()
                .filter(a -> a instanceof Inet4Address)
                .collect(Collectors.toList());
        // 仅当域名没有任何 A 记录时才回退原始结果（兼容纯 IPv6 域名）
        return ipv4.isEmpty() ? addresses : ipv4;
    }
}
