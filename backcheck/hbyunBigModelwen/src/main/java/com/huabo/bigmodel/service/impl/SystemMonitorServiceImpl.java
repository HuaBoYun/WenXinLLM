package com.huabo.bigmodel.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.huabo.bigmodel.dto.*;
import com.huabo.bigmodel.service.SystemMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import javax.sql.DataSource;
import java.lang.management.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 系统监控服务实现
 * 使用 DiscoveryClient（Eureka）获取服务列表
 * 使用 OSHI 获取服务器硬件指标
 */
@Slf4j
@Service
public class SystemMonitorServiceImpl implements SystemMonitorService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    @Qualifier("dmDataSource")
    private DataSource dmDataSource;

    private final SystemInfo systemInfo = new SystemInfo();

    // ========================== 服务列表 ==========================

    @Override
    public List<ServiceInstanceVO> getAllServiceInstances() {
        List<ServiceInstanceVO> result = new ArrayList<>();
        List<String> services = discoveryClient.getServices();
        for (String serviceId : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            for (ServiceInstance inst : instances) {
                result.add(ServiceInstanceVO.builder()
                        .serviceId(serviceId)
                        .serviceName(serviceId.toUpperCase())
                        .instanceId(inst.getInstanceId())
                        .host(inst.getHost())
                        .port(inst.getPort())
                        .status("UP")
                        .uri(inst.getUri() != null ? inst.getUri().toString() : "")
                        .secure(inst.isSecure())
                        .metadata(inst.getMetadata() != null ? JSON.toJSONString(inst.getMetadata()) : "{}")
                        .build());
            }
        }
        return result;
    }

    @Override
    public HealthSummaryVO getHealthSummary() {
        List<String> services = discoveryClient.getServices();
        int totalInstances = 0;
        int onlineCount = 0;
        for (String serviceId : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            totalInstances += instances.size();
            if (!instances.isEmpty()) {
                onlineCount++;
            }
        }
        int totalCount = services.size();
        int offlineCount = totalCount - onlineCount;
        double healthRate = totalCount > 0 ? (onlineCount * 100.0 / totalCount) : 0;

        return HealthSummaryVO.builder()
                .onlineCount(onlineCount)
                .offlineCount(offlineCount)
                .totalCount(totalCount)
                .totalInstances(totalInstances)
                .healthRate(Math.round(healthRate * 100.0) / 100.0)
                .build();
    }

    // ========================== 服务器信息 ==========================

    @Override
    public ServerInfoVO getServerInfo() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();
        CentralProcessor processor = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();

        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostName = "unknown";
        }

        // JVM 信息
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memMx = ManagementFactory.getMemoryMXBean();
        long jvmMax = memMx.getHeapMemoryUsage().getMax();

        // 启动时间
        long bootTime = os.getSystemBootTime() * 1000L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long uptimeMs = System.currentTimeMillis() - bootTime;

        return ServerInfoVO.builder()
                .osName(os.getFamily() + " " + os.getVersionInfo().getVersion())
                .osVersion(os.getVersionInfo().getBuildNumber())
                .osArch(System.getProperty("os.arch"))
                .hostName(hostName)
                .cpuModel(processor.getProcessorIdentifier().getName())
                .cpuPhysicalCores(processor.getPhysicalProcessorCount())
                .cpuLogicalCores(processor.getLogicalProcessorCount())
                .totalMemory(memory.getTotal())
                .totalMemoryStr(formatBytes(memory.getTotal()))
                .jvmName(runtime.getVmName())
                .jvmVersion(runtime.getVmVersion())
                .jvmVendor(runtime.getVmVendor() != null ? runtime.getVmVendor() : "")
                .jvmMaxMemory(jvmMax)
                .jvmMaxMemoryStr(formatBytes(jvmMax))
                .bootTime(sdf.format(new Date(bootTime)))
                .uptime(formatDuration(uptimeMs))
                .build();
    }

    @Override
    public ServerMetricsVO getServerMetrics() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();

        // CPU 使用率（需要两次采样）
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;

        // 各分项 CPU
        long[] ticks = processor.getSystemCpuLoadTicks();
        long totalTicks = Arrays.stream(ticks).sum();
        double sysPct = totalTicks > 0 ?
                (ticks[CentralProcessor.TickType.SYSTEM.getIndex()] * 100.0 / totalTicks) : 0;
        double userPct = totalTicks > 0 ?
                (ticks[CentralProcessor.TickType.USER.getIndex()] * 100.0 / totalTicks) : 0;
        double ioWaitPct = totalTicks > 0 ?
                (ticks[CentralProcessor.TickType.IOWAIT.getIndex()] * 100.0 / totalTicks) : 0;

        // 内存
        long totalMem = memory.getTotal();
        long availMem = memory.getAvailable();
        long usedMem = totalMem - availMem;
        double memUsage = totalMem > 0 ? (usedMem * 100.0 / totalMem) : 0;

        // JVM 堆
        MemoryMXBean memMx = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memMx.getHeapMemoryUsage();
        MemoryUsage nonHeap = memMx.getNonHeapMemoryUsage();
        double heapUsage = heap.getMax() > 0 ? (heap.getUsed() * 100.0 / heap.getMax()) : 0;

        // 线程
        ThreadMXBean threadMx = ManagementFactory.getThreadMXBean();

        return ServerMetricsVO.builder()
                .cpuUsage(round2(cpuLoad))
                .cpuSystemUsage(round2(sysPct))
                .cpuUserUsage(round2(userPct))
                .cpuIoWait(round2(ioWaitPct))
                .totalMemory(totalMem)
                .usedMemory(usedMem)
                .availableMemory(availMem)
                .memoryUsage(round2(memUsage))
                .jvmHeapTotal(heap.getMax())
                .jvmHeapUsed(heap.getUsed())
                .jvmHeapUsage(round2(heapUsage))
                .jvmNonHeapUsed(nonHeap.getUsed())
                .threadCount(threadMx.getThreadCount())
                .daemonThreadCount(threadMx.getDaemonThreadCount())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public List<DiskInfoVO> getDiskInfos() {
        OperatingSystem os = systemInfo.getOperatingSystem();
        FileSystem fs = os.getFileSystem();
        List<OSFileStore> stores = fs.getFileStores();
        return stores.stream().map(store -> {
            long total = store.getTotalSpace();
            long free = store.getUsableSpace();
            long used = total - free;
            double pct = total > 0 ? (used * 100.0 / total) : 0;
            return DiskInfoVO.builder()
                    .mountPoint(store.getMount())
                    .fsType(store.getType())
                    .deviceName(store.getName())
                    .totalSpace(total)
                    .totalSpaceStr(formatBytes(total))
                    .freeSpace(free)
                    .freeSpaceStr(formatBytes(free))
                    .usedSpace(used)
                    .usedSpaceStr(formatBytes(used))
                    .usagePercent(round2(pct))
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getJvmDetails() {
        Map<String, Object> map = new LinkedHashMap<>();
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memMx = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadMx = ManagementFactory.getThreadMXBean();
        OperatingSystemMXBean osMx = ManagementFactory.getOperatingSystemMXBean();

        MemoryUsage heap = memMx.getHeapMemoryUsage();
        MemoryUsage nonHeap = memMx.getNonHeapMemoryUsage();

        map.put("jvmName", runtime.getVmName());
        map.put("jvmVersion", runtime.getVmVersion());
        map.put("jvmVendor", runtime.getVmVendor());
        map.put("jvmUptime", formatDuration(runtime.getUptime()));
        map.put("heapInit", formatBytes(heap.getInit()));
        map.put("heapUsed", formatBytes(heap.getUsed()));
        map.put("heapMax", formatBytes(heap.getMax()));
        map.put("heapCommitted", formatBytes(heap.getCommitted()));
        map.put("nonHeapUsed", formatBytes(nonHeap.getUsed()));
        map.put("nonHeapCommitted", formatBytes(nonHeap.getCommitted()));
        map.put("threadCount", threadMx.getThreadCount());
        map.put("peakThreadCount", threadMx.getPeakThreadCount());
        map.put("daemonThreadCount", threadMx.getDaemonThreadCount());
        map.put("availableProcessors", osMx.getAvailableProcessors());
        map.put("systemLoadAverage", round2(osMx.getSystemLoadAverage()));

        // GC 信息
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        List<Map<String, Object>> gcList = new ArrayList<>();
        for (GarbageCollectorMXBean gc : gcBeans) {
            Map<String, Object> gcMap = new LinkedHashMap<>();
            gcMap.put("name", gc.getName());
            gcMap.put("collectionCount", gc.getCollectionCount());
            gcMap.put("collectionTime", gc.getCollectionTime() + "ms");
            gcList.add(gcMap);
        }
        map.put("gcInfo", gcList);
        return map;
    }

    // ========================== 网络接口 ==========================

    @Override
    public List<NetworkInfoVO> getNetworkInfos() {
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        List<NetworkIF> networkIFs = hal.getNetworkIFs();
        List<NetworkInfoVO> result = new ArrayList<>();

        // 第一次采样
        Map<String, long[]> prevStats = new HashMap<>();
        for (NetworkIF net : networkIFs) {
            net.updateAttributes();
            prevStats.put(net.getName(), new long[]{net.getBytesRecv(), net.getBytesSent()});
        }

        // 等待1秒后第二次采样（计算实时网速）
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        for (NetworkIF net : networkIFs) {
            // 跳过loopback回环接口
            if (net.getName().startsWith("lo")) {
                continue;
            }
            net.updateAttributes();

            // 计算实时网速（字节/秒）
            long[] prev = prevStats.get(net.getName());
            long recvSpeed = prev != null ? (net.getBytesRecv() - prev[0]) : 0;
            long sentSpeed = prev != null ? (net.getBytesSent() - prev[1]) : 0;

            // IPv4/IPv6地址拼接
            String[] ipv4Arr = net.getIPv4addr();
            String[] ipv6Arr = net.getIPv6addr();
            String ipv4 = (ipv4Arr != null && ipv4Arr.length > 0)
                    ? String.join(",", ipv4Arr) : "";
            String ipv6 = (ipv6Arr != null && ipv6Arr.length > 0)
                    ? String.join(",", ipv6Arr) : "";

            result.add(NetworkInfoVO.builder()
                    .name(net.getName())
                    .ipv4(ipv4)
                    .ipv6(ipv6)
                    .mac(net.getMacaddr())
                    .mtu(net.getMTU())
                    .bytesRecv(net.getBytesRecv())
                    .bytesSent(net.getBytesSent())
                    .packetsRecv(net.getPacketsRecv())
                    .packetsSent(net.getPacketsSent())
                    .speedRecv(formatSpeed(recvSpeed))
                    .speedSent(formatSpeed(sentSpeed))
                    .errorsRecv(net.getInErrors())
                    .errorsSent(net.getOutErrors())
                    .build());
        }
        return result;
    }

    // ========================== 数据库连接池 ==========================

    @Override
    public DatabasePoolVO getDatabasePoolStatus() {
        try {
            DruidDataSource druid = (DruidDataSource) dmDataSource;
            int activeCount = druid.getActiveCount();
            int maxActive = druid.getMaxActive();
            double connectPercent = maxActive > 0 ? (activeCount * 100.0 / maxActive) : 0;

            return DatabasePoolVO.builder()
                    .url(druid.getUrl())
                    .driverClass(druid.getDriverClassName())
                    .activeCount(activeCount)
                    .maxActive(maxActive)
                    .initialSize(druid.getInitialSize())
                    .poolingCount(druid.getPoolingCount())
                    .waitThreadCount(druid.getWaitThreadCount())
                    .notEmptyWaitCount(druid.getNotEmptyWaitCount())
                    .connectCount(druid.getConnectCount())
                    .closeCount(druid.getCloseCount())
                    .errorCount(druid.getConnectErrorCount())
                    .connectPercent(round2(connectPercent))
                    .build();
        } catch (Exception e) {
            log.error("获取数据库连接池状态失败", e);
            return DatabasePoolVO.builder()
                    .url("N/A")
                    .driverClass("N/A")
                    .activeCount(0)
                    .maxActive(0)
                    .initialSize(0)
                    .poolingCount(0)
                    .waitThreadCount(0)
                    .notEmptyWaitCount(0L)
                    .connectCount(0L)
                    .closeCount(0L)
                    .errorCount(0L)
                    .connectPercent(0.0)
                    .build();
        }
    }

    // ========================== 系统告警 ==========================

    @Override
    public List<SystemAlertVO> getSystemAlerts() {
        List<SystemAlertVO> alerts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());

        HardwareAbstractionLayer hal = systemInfo.getHardware();
        CentralProcessor processor = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();

        // --- CPU 告警 ---
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;

        if (cpuUsage >= 85) {
            alerts.add(SystemAlertVO.builder()
                    .level("danger")
                    .type("CPU")
                    .title("CPU使用率过高")
                    .message(String.format("当前CPU使用率为%.1f%%，超过危险阈值85%%", cpuUsage))
                    .value(round2(cpuUsage))
                    .threshold(85.0)
                    .unit("%")
                    .time(now)
                    .build());
        } else if (cpuUsage >= 70) {
            alerts.add(SystemAlertVO.builder()
                    .level("warning")
                    .type("CPU")
                    .title("CPU使用率偏高")
                    .message(String.format("当前CPU使用率为%.1f%%，超过预警阈值70%%", cpuUsage))
                    .value(round2(cpuUsage))
                    .threshold(70.0)
                    .unit("%")
                    .time(now)
                    .build());
        }

        // --- 内存告警 ---
        long totalMem = memory.getTotal();
        long availMem = memory.getAvailable();
        long usedMem = totalMem - availMem;
        double memUsage = totalMem > 0 ? (usedMem * 100.0 / totalMem) : 0;

        if (memUsage >= 85) {
            alerts.add(SystemAlertVO.builder()
                    .level("danger")
                    .type("MEMORY")
                    .title("内存使用率过高")
                    .message(String.format("当前内存使用率为%.1f%%，已用%s / 总计%s，超过危险阈值85%%",
                            memUsage, formatBytes(usedMem), formatBytes(totalMem)))
                    .value(round2(memUsage))
                    .threshold(85.0)
                    .unit("%")
                    .time(now)
                    .build());
        } else if (memUsage >= 70) {
            alerts.add(SystemAlertVO.builder()
                    .level("warning")
                    .type("MEMORY")
                    .title("内存使用率偏高")
                    .message(String.format("当前内存使用率为%.1f%%，已用%s / 总计%s，超过预警阈值70%%",
                            memUsage, formatBytes(usedMem), formatBytes(totalMem)))
                    .value(round2(memUsage))
                    .threshold(70.0)
                    .unit("%")
                    .time(now)
                    .build());
        }

        // --- 磁盘告警 ---
        OperatingSystem os = systemInfo.getOperatingSystem();
        FileSystem fs = os.getFileSystem();
        List<OSFileStore> stores = fs.getFileStores();
        for (OSFileStore store : stores) {
            long diskTotal = store.getTotalSpace();
            long diskFree = store.getUsableSpace();
            long diskUsed = diskTotal - diskFree;
            double diskUsage = diskTotal > 0 ? (diskUsed * 100.0 / diskTotal) : 0;

            if (diskUsage >= 90) {
                alerts.add(SystemAlertVO.builder()
                        .level("danger")
                        .type("DISK")
                        .title("磁盘使用率过高 - " + store.getMount())
                        .message(String.format("磁盘%s使用率为%.1f%%，已用%s / 总计%s，超过危险阈值90%%",
                                store.getMount(), diskUsage, formatBytes(diskUsed), formatBytes(diskTotal)))
                        .value(round2(diskUsage))
                        .threshold(90.0)
                        .unit("%")
                        .time(now)
                        .build());
            } else if (diskUsage >= 70) {
                alerts.add(SystemAlertVO.builder()
                        .level("warning")
                        .type("DISK")
                        .title("磁盘使用率偏高 - " + store.getMount())
                        .message(String.format("磁盘%s使用率为%.1f%%，已用%s / 总计%s，超过预警阈值70%%",
                                store.getMount(), diskUsage, formatBytes(diskUsed), formatBytes(diskTotal)))
                        .value(round2(diskUsage))
                        .threshold(70.0)
                        .unit("%")
                        .time(now)
                        .build());
            }
        }

        // --- JVM 堆内存告警 ---
        MemoryMXBean memMx = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memMx.getHeapMemoryUsage();
        double jvmHeapUsage = heap.getMax() > 0 ? (heap.getUsed() * 100.0 / heap.getMax()) : 0;

        if (jvmHeapUsage >= 85) {
            alerts.add(SystemAlertVO.builder()
                    .level("danger")
                    .type("JVM")
                    .title("JVM堆内存使用率过高")
                    .message(String.format("当前JVM堆内存使用率为%.1f%%，已用%s / 最大%s，超过危险阈值85%%",
                            jvmHeapUsage, formatBytes(heap.getUsed()), formatBytes(heap.getMax())))
                    .value(round2(jvmHeapUsage))
                    .threshold(85.0)
                    .unit("%")
                    .time(now)
                    .build());
        } else if (jvmHeapUsage >= 70) {
            alerts.add(SystemAlertVO.builder()
                    .level("warning")
                    .type("JVM")
                    .title("JVM堆内存使用率偏高")
                    .message(String.format("当前JVM堆内存使用率为%.1f%%，已用%s / 最大%s，超过预警阈值70%%",
                            jvmHeapUsage, formatBytes(heap.getUsed()), formatBytes(heap.getMax())))
                    .value(round2(jvmHeapUsage))
                    .threshold(70.0)
                    .unit("%")
                    .time(now)
                    .build());
        }

        return alerts;
    }

    // ========================== 工具方法 ==========================

    private String formatBytes(long bytes) {
        if (bytes <= 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int idx = 0;
        double val = bytes;
        while (val >= 1024 && idx < units.length - 1) {
            val /= 1024;
            idx++;
        }
        return String.format("%.2f %s", val, units[idx]);
    }

    private String formatSpeed(long bytesPerSec) {
        if (bytesPerSec <= 0) return "0 B/s";
        String[] units = {"B/s", "KB/s", "MB/s", "GB/s", "TB/s"};
        int idx = 0;
        double val = bytesPerSec;
        while (val >= 1024 && idx < units.length - 1) {
            val /= 1024;
            idx++;
        }
        return String.format("%.2f %s", val, units[idx]);
    }

    private String formatDuration(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        if (days > 0) return days + "天" + hours + "小时" + minutes + "分钟";
        if (hours > 0) return hours + "小时" + minutes + "分钟";
        return minutes + "分钟";
    }

    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
