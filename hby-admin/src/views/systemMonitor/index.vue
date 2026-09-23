<template>
  <div class="sys-monitor">
    <!-- 顶部 KPI 卡片 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6">
        <div class="kpi-card kpi-green">
          <div class="kpi-icon"><i class="el-icon-monitor"></i></div>
          <div class="kpi-body">
            <div class="kpi-value">{{ health.onlineCount || 0 }}</div>
            <div class="kpi-label">在线服务</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="kpi-card kpi-red">
          <div class="kpi-icon"><i class="el-icon-warning"></i></div>
          <div class="kpi-body">
            <div class="kpi-value">{{ health.offlineCount || 0 }}</div>
            <div class="kpi-label">离线服务</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="kpi-card kpi-blue">
          <div class="kpi-icon"><i class="el-icon-cpu"></i></div>
          <div class="kpi-body">
            <div class="kpi-value">{{ metrics.cpuUsage || 0 }}%</div>
            <div class="kpi-label">CPU 使用率</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="kpi-card kpi-orange">
          <div class="kpi-icon"><i class="el-icon-coin"></i></div>
          <div class="kpi-body">
            <div class="kpi-value">{{ metrics.memoryUsage || 0 }}%</div>
            <div class="kpi-label">内存使用率</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 中部：服务列表 + 服务器资源 -->
    <el-row :gutter="16" class="main-row">
      <!-- 左侧：服务列表 -->
      <el-col :span="14">
        <el-card class="monitor-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-grid"></i> 微服务状态一览</span>
            <el-button size="mini" type="primary" icon="el-icon-refresh" @click="loadServices" :loading="loading.services">刷新</el-button>
          </div>
          <el-table :data="services" size="small" border stripe style="width:100%" max-height="400" v-loading="loading.services">
            <el-table-column prop="serviceName" label="服务名称" min-width="160" show-overflow-tooltip>
              <template slot-scope="{row}">
                <span class="svc-name">{{ row.serviceName }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="host" label="IP" width="140" />
            <el-table-column prop="port" label="端口" width="80" align="center" />
            <el-table-column prop="status" label="状态" width="90" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'UP' ? 'success' : 'danger'" size="mini">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="instanceId" label="实例ID" min-width="200" show-overflow-tooltip />
            <el-table-column label="地址" min-width="200" show-overflow-tooltip>
              <template slot-scope="{row}">
                <a :href="row.uri" target="_blank" class="uri-link">{{ row.uri }}</a>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧：服务器资源 -->
      <el-col :span="10">
        <el-card class="monitor-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-data-board"></i> 服务器资源</span>
            <el-button size="mini" type="primary" icon="el-icon-refresh" @click="loadMetrics" :loading="loading.metrics">刷新</el-button>
          </div>
          <div class="resource-grid">
            <!-- CPU -->
            <div class="resource-item">
              <div class="res-title">CPU 使用率</div>
              <el-progress type="dashboard" :percentage="metrics.cpuUsage || 0" :color="progressColor" :width="100" />
              <div class="res-detail">
                <span>系统: {{ metrics.cpuSystemUsage || 0 }}%</span>
                <span>用户: {{ metrics.cpuUserUsage || 0 }}%</span>
                <span>IO等待: {{ metrics.cpuIoWait || 0 }}%</span>
              </div>
            </div>
            <!-- 内存 -->
            <div class="resource-item">
              <div class="res-title">物理内存</div>
              <el-progress type="dashboard" :percentage="metrics.memoryUsage || 0" :color="progressColor" :width="100" />
              <div class="res-detail">
                <span>已用: {{ formatBytes(metrics.usedMemory) }}</span>
                <span>总量: {{ formatBytes(metrics.totalMemory) }}</span>
              </div>
            </div>
            <!-- JVM 堆 -->
            <div class="resource-item">
              <div class="res-title">JVM 堆内存</div>
              <el-progress type="dashboard" :percentage="metrics.jvmHeapUsage || 0" :color="progressColor" :width="100" />
              <div class="res-detail">
                <span>已用: {{ formatBytes(metrics.jvmHeapUsed) }}</span>
                <span>最大: {{ formatBytes(metrics.jvmHeapTotal) }}</span>
              </div>
            </div>
            <!-- 线程 -->
            <div class="resource-item">
              <div class="res-title">线程</div>
              <div class="thread-num">{{ metrics.threadCount || 0 }}</div>
              <div class="res-detail">
                <span>守护线程: {{ metrics.daemonThreadCount || 0 }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部：服务器信息 + 磁盘 -->
    <el-row :gutter="16" class="bottom-row">
      <!-- 服务器基础信息 -->
      <el-col :span="10">
        <el-card class="monitor-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-info"></i> 服务器信息</span>
          </div>
          <el-descriptions :column="1" size="small" border>
            <el-descriptions-item label="操作系统">{{ serverInfo.osName }}</el-descriptions-item>
            <el-descriptions-item label="架构">{{ serverInfo.osArch }}</el-descriptions-item>
            <el-descriptions-item label="主机名">{{ serverInfo.hostName }}</el-descriptions-item>
            <el-descriptions-item label="CPU 型号">{{ serverInfo.cpuModel }}</el-descriptions-item>
            <el-descriptions-item label="CPU 核心">物理 {{ serverInfo.cpuPhysicalCores }} / 逻辑 {{ serverInfo.cpuLogicalCores }}</el-descriptions-item>
            <el-descriptions-item label="总内存">{{ serverInfo.totalMemoryStr }}</el-descriptions-item>
            <el-descriptions-item label="JVM">{{ serverInfo.jvmName }} {{ serverInfo.jvmVersion }}</el-descriptions-item>
            <el-descriptions-item label="JVM最大堆">{{ serverInfo.jvmMaxMemoryStr }}</el-descriptions-item>
            <el-descriptions-item label="启动时间">{{ serverInfo.bootTime }}</el-descriptions-item>
            <el-descriptions-item label="运行时长">{{ serverInfo.uptime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <!-- 磁盘信息 -->
      <el-col :span="14">
        <el-card class="monitor-card" shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-box"></i> 磁盘分区</span>
            <el-button size="mini" type="primary" icon="el-icon-refresh" @click="loadDisks" :loading="loading.disks">刷新</el-button>
          </div>
          <el-table :data="disks" size="small" border stripe style="width:100%" max-height="300" v-loading="loading.disks">
            <el-table-column prop="mountPoint" label="挂载点" width="120" />
            <el-table-column prop="deviceName" label="设备" min-width="100" show-overflow-tooltip />
            <el-table-column prop="fsType" label="文件系统" width="90" />
            <el-table-column prop="totalSpaceStr" label="总容量" width="100" align="right" />
            <el-table-column prop="usedSpaceStr" label="已用" width="100" align="right" />
            <el-table-column prop="freeSpaceStr" label="可用" width="100" align="right" />
            <el-table-column label="使用率" width="160">
              <template slot-scope="{row}">
                <el-progress :percentage="row.usagePercent" :color="progressColor" :stroke-width="14" :text-inside="true" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getMonitorServices, getHealthSummary, getServerInfo, getServerMetrics, getDiskInfos } from '@/api/systemMonitor'

export default {
  name: 'SystemMonitor',
  data() {
    return {
      services: [],
      health: {},
      serverInfo: {},
      metrics: {},
      disks: [],
      loading: {
        services: false,
        metrics: false,
        disks: false
      },
      timer: null
    }
  },
  computed: {
    progressColor() {
      return [
        { color: '#67c23a', percentage: 50 },
        { color: '#e6a23c', percentage: 80 },
        { color: '#f56c6c', percentage: 100 }
      ]
    }
  },
  created() {
    this.loadAll()
    // 每 10 秒刷新实时指标
    this.timer = setInterval(() => {
      this.loadMetrics()
    }, 10000)
  },
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    async loadAll() {
      this.loadServices()
      this.loadHealth()
      this.loadServerInfo()
      this.loadMetrics()
      this.loadDisks()
    },
    async loadServices() {
      this.loading.services = true
      try {
        const res = await getMonitorServices()
        if (res && res.code === 200) {
          this.services = res.data || []
        }
      } catch (e) {
        console.error('获取服务列表失败', e)
      } finally {
        this.loading.services = false
      }
    },
    async loadHealth() {
      try {
        const res = await getHealthSummary()
        if (res && res.code === 200) {
          this.health = res.data || {}
        }
      } catch (e) {
        console.error('获取健康汇总失败', e)
      }
    },
    async loadServerInfo() {
      try {
        const res = await getServerInfo()
        if (res && res.code === 200) {
          this.serverInfo = res.data || {}
        }
      } catch (e) {
        console.error('获取服务器信息失败', e)
      }
    },
    async loadMetrics() {
      this.loading.metrics = true
      try {
        const res = await getServerMetrics()
        if (res && res.code === 200) {
          this.metrics = res.data || {}
        }
      } catch (e) {
        console.error('获取服务器指标失败', e)
      } finally {
        this.loading.metrics = false
      }
    },
    async loadDisks() {
      this.loading.disks = true
      try {
        const res = await getDiskInfos()
        if (res && res.code === 200) {
          this.disks = res.data || []
        }
      } catch (e) {
        console.error('获取磁盘信息失败', e)
      } finally {
        this.loading.disks = false
      }
    },
    formatBytes(bytes) {
      if (!bytes || bytes <= 0) return '0 B'
      const units = ['B', 'KB', 'MB', 'GB', 'TB']
      let idx = 0
      let val = bytes
      while (val >= 1024 && idx < units.length - 1) {
        val /= 1024
        idx++
      }
      return val.toFixed(2) + ' ' + units[idx]
    }
  }
}
</script>

<style scoped>
.sys-monitor {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}
.kpi-row { margin-bottom: 16px; }
.main-row { margin-bottom: 16px; }
.bottom-row { margin-bottom: 16px; }

/* KPI 卡片 */
.kpi-card {
  display: flex; align-items: center; padding: 20px 24px;
  border-radius: 12px; color: #fff; transition: transform .2s;
}
.kpi-card:hover { transform: translateY(-3px); box-shadow: 0 6px 20px rgba(0,0,0,.12); }
.kpi-green { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.kpi-red { background: linear-gradient(135deg, #f5576c 0%, #ff6a88 100%); }
.kpi-blue { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.kpi-orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.kpi-icon { font-size: 36px; margin-right: 16px; opacity: .85; }
.kpi-value { font-size: 28px; font-weight: 700; line-height: 1.2; }
.kpi-label { font-size: 13px; opacity: .85; margin-top: 4px; }

/* 卡片 */
.monitor-card { border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: 600; }
.card-header i { margin-right: 6px; }

/* 服务列表 */
.svc-name { font-weight: 500; color: #303133; }
.uri-link { color: #409eff; text-decoration: none; }
.uri-link:hover { text-decoration: underline; }

/* 资源面板 */
.resource-grid {
  display: grid; grid-template-columns: 1fr 1fr;
  gap: 20px; padding: 12px 0;
}
.resource-item { text-align: center; }
.res-title { font-weight: 600; margin-bottom: 8px; color: #303133; }
.res-detail { margin-top: 8px; font-size: 12px; color: #909399; display: flex; flex-wrap: wrap; justify-content: center; gap: 8px; }
.thread-num { font-size: 40px; font-weight: 700; color: #409eff; line-height: 100px; }
</style>
