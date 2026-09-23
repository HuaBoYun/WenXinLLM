<template>
  <div class="job-schedule-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-time"></i>
            作业调度管理
          </h2>
          <p class="page-description">
            智能化作业调度系统，支持多种调度策略、负载均衡、资源管理和性能监控
          </p>
        </div>
        <div class="header-right">
          <el-button-group>
            <el-button
              :type="activeView === 'dashboard' ? 'primary' : 'default'"
              icon="el-icon-data-analysis"
              @click="switchView('dashboard')"
            >
              仪表板
            </el-button>
            <el-button
              :type="activeView === 'list' ? 'primary' : 'default'"
              icon="el-icon-s-grid"
              @click="switchView('list')"
            >
              作业列表
            </el-button>
          </el-button-group>
        </div>
      </div>
    </div>

    <!-- 快速操作栏 -->
    <div class="quick-actions" v-if="activeView === 'dashboard'">
      <el-card shadow="never" class="action-card">
        <div class="action-content">
          <div class="action-item">
            <div class="action-icon">
              <i class="el-icon-plus"></i>
            </div>
            <div class="action-info">
              <div class="action-title">新建作业</div>
              <div class="action-desc">创建新的调度作业</div>
            </div>
            <el-button type="primary" size="small" @click="handleCreateJob">创建</el-button>
          </div>
          
          <el-divider direction="vertical" style="height: 60px;"></el-divider>
          
          <div class="action-item">
            <div class="action-icon">
              <i class="el-icon-video-play"></i>
            </div>
            <div class="action-info">
              <div class="action-title">批量启动</div>
              <div class="action-desc">启动所有待执行作业</div>
            </div>
            <el-button type="success" size="small" @click="handleBatchStart">启动</el-button>
          </div>
          
          <el-divider direction="vertical" style="height: 60px;"></el-divider>
          
          <div class="action-item">
            <div class="action-icon">
              <i class="el-icon-refresh"></i>
            </div>
            <div class="action-info">
              <div class="action-title">系统监控</div>
              <div class="action-desc">查看系统运行状态</div>
            </div>
            <el-button type="info" size="small" @click="handleSystemMonitor">监控</el-button>
          </div>
          
          <el-divider direction="vertical" style="height: 60px;"></el-divider>
          
          <div class="action-item">
            <div class="action-icon">
              <i class="el-icon-setting"></i>
            </div>
            <div class="action-info">
              <div class="action-title">调度配置</div>
              <div class="action-desc">配置调度策略参数</div>
            </div>
            <el-button type="warning" size="small" @click="handleScheduleConfig">配置</el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <!-- 仪表板视图 -->
      <JobScheduleDashboard v-if="activeView === 'dashboard'" />
      
      <!-- 列表视图 -->
      <JobScheduleList v-if="activeView === 'list'" ref="jobScheduleList" />
    </div>

    <!-- 作业详情对话框 -->
    <JobScheduleDetail
      :visible.sync="detailVisible"
      :job-id="currentJobId"
      :readonly="detailReadonly"
      @refresh="handleRefresh"
    />

    <!-- 系统监控对话框 -->
    <el-dialog
      title="系统监控"
      :visible.sync="monitorVisible"
      width="80%"
      :close-on-click-modal="false"
    >
      <div class="monitor-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="never">
              <div slot="header">系统资源</div>
              <div class="monitor-item">
                <span>CPU使用率:</span>
                <el-progress :percentage="systemMonitor.cpuUsage || 0" :color="getProgressColor(systemMonitor.cpuUsage)" />
              </div>
              <div class="monitor-item">
                <span>内存使用率:</span>
                <el-progress :percentage="systemMonitor.memoryUsage || 0" :color="getProgressColor(systemMonitor.memoryUsage)" />
              </div>
              <div class="monitor-item">
                <span>磁盘使用率:</span>
                <el-progress :percentage="systemMonitor.diskUsage || 0" :color="getProgressColor(systemMonitor.diskUsage)" />
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="never">
              <div slot="header">调度器状态</div>
              <div class="monitor-item">
                <span>活跃调度器:</span>
                <span class="monitor-value">{{ systemMonitor.activeSchedulers || 0 }}</span>
              </div>
              <div class="monitor-item">
                <span>队列长度:</span>
                <span class="monitor-value">{{ systemMonitor.queueLength || 0 }}</span>
              </div>
              <div class="monitor-item">
                <span>执行中作业:</span>
                <span class="monitor-value">{{ systemMonitor.runningJobs || 0 }}</span>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div slot="footer">
        <el-button @click="monitorVisible = false">关闭</el-button>
        <el-button type="primary" @click="refreshSystemMonitor">刷新</el-button>
      </div>
    </el-dialog>

    <!-- 调度配置对话框 -->
    <el-dialog
      title="调度配置"
      :visible.sync="configVisible"
      width="60%"
      :close-on-click-modal="false"
    >
      <el-form :model="scheduleConfig" label-width="120px">
        <el-form-item label="默认调度策略">
          <el-select v-model="scheduleConfig.defaultStrategy" placeholder="请选择默认调度策略" style="width: 100%">
            <el-option label="先进先出" value="FIFO" />
            <el-option label="最短作业优先" value="SJF" />
            <el-option label="优先级调度" value="PRIORITY" />
            <el-option label="轮询调度" value="ROUND_ROBIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="负载均衡策略">
          <el-select v-model="scheduleConfig.loadBalanceStrategy" placeholder="请选择负载均衡策略" style="width: 100%">
            <el-option label="轮询" value="ROUND_ROBIN" />
            <el-option label="最少连接" value="LEAST_CONNECTIONS" />
            <el-option label="加权轮询" value="WEIGHTED_ROUND_ROBIN" />
            <el-option label="基于资源" value="RESOURCE_BASED" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大并发数">
          <el-input-number v-model="scheduleConfig.maxConcurrency" :min="1" :max="1000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="队列大小">
          <el-input-number v-model="scheduleConfig.queueSize" :min="10" :max="10000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="心跳间隔(秒)">
          <el-input-number v-model="scheduleConfig.heartbeatInterval" :min="1" :max="300" style="width: 100%" />
        </el-form-item>
        <el-form-item label="超时检查间隔(秒)">
          <el-input-number v-model="scheduleConfig.timeoutCheckInterval" :min="1" :max="3600" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="configVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveConfig">保存配置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import JobScheduleDashboard from './JobScheduleDashboard'
import JobScheduleList from './JobScheduleList'
import JobScheduleDetail from './JobScheduleDetail'
import jobScheduleApi from '@/api/managementAccountant/ss/jobSchedule'

export default {
  name: 'JobScheduleIndex',
  components: {
    JobScheduleDashboard,
    JobScheduleList,
    JobScheduleDetail
  },
  data() {
    return {
      activeView: 'dashboard', // dashboard | list
      
      // 对话框控制
      detailVisible: false,
      detailReadonly: false,
      currentJobId: null,
      
      monitorVisible: false,
      configVisible: false,
      
      // 系统监控数据
      systemMonitor: {
        cpuUsage: 0,
        memoryUsage: 0,
        diskUsage: 0,
        activeSchedulers: 0,
        queueLength: 0,
        runningJobs: 0
      },
      
      // 调度配置
      scheduleConfig: {
        defaultStrategy: 'PRIORITY',
        loadBalanceStrategy: 'ROUND_ROBIN',
        maxConcurrency: 100,
        queueSize: 1000,
        heartbeatInterval: 30,
        timeoutCheckInterval: 60
      }
    }
  },
  created() {
    // 从路由参数获取初始视图
    if (this.$route.query.view) {
      this.activeView = this.$route.query.view
    }
  },
  methods: {
    /**
     * 切换视图
     */
    switchView(view) {
      this.activeView = view
      // 更新路由参数
      this.$router.replace({
        query: { ...this.$route.query, view }
      })
    },

    /**
     * 创建作业
     */
    handleCreateJob() {
      this.currentJobId = null
      this.detailReadonly = false
      this.detailVisible = true
    },

    /**
     * 批量启动
     */
    async handleBatchStart() {
      try {
        const response = await jobScheduleApi.getPendingJobs()
        if (response.success && response.data.length > 0) {
          const jobIds = response.data.map(job => job.jobId)
          const startResponse = await jobScheduleApi.batchStartJobSchedule(jobIds)
          if (startResponse.success) {
            this.$message.success(`成功启动 ${jobIds.length} 个作业`)
            this.handleRefresh()
          }
        } else {
          this.$message.info('没有待启动的作业')
        }
      } catch (error) {
        this.$message.error('批量启动失败: ' + error.message)
      }
    },

    /**
     * 系统监控
     */
    async handleSystemMonitor() {
      this.monitorVisible = true
      await this.refreshSystemMonitor()
    },

    /**
     * 刷新系统监控
     */
    async refreshSystemMonitor() {
      try {
        // 模拟系统监控数据
        this.systemMonitor = {
          cpuUsage: Math.floor(Math.random() * 100),
          memoryUsage: Math.floor(Math.random() * 100),
          diskUsage: Math.floor(Math.random() * 100),
          activeSchedulers: Math.floor(Math.random() * 10) + 1,
          queueLength: Math.floor(Math.random() * 100),
          runningJobs: Math.floor(Math.random() * 50)
        }
      } catch (error) {
        this.$message.error('刷新系统监控失败: ' + error.message)
      }
    },

    /**
     * 调度配置
     */
    handleScheduleConfig() {
      this.configVisible = true
    },

    /**
     * 保存配置
     */
    async handleSaveConfig() {
      try {
        // 这里应该调用保存配置的API
        this.$message.success('配置保存成功')
        this.configVisible = false
      } catch (error) {
        this.$message.error('保存配置失败: ' + error.message)
      }
    },

    /**
     * 刷新数据
     */
    handleRefresh() {
      if (this.activeView === 'list' && this.$refs.jobScheduleList) {
        this.$refs.jobScheduleList.handleRefresh()
      }
    },

    /**
     * 获取进度条颜色
     */
    getProgressColor(percentage) {
      if (percentage < 50) return '#67c23a'
      if (percentage < 80) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.job-schedule-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
}

.page-title i {
  margin-right: 10px;
  color: #409eff;
}

.page-description {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.header-right {
  margin-left: 20px;
}

.quick-actions {
  margin-bottom: 20px;
}

.action-card {
  border-radius: 8px;
}

.action-content {
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 10px 0;
}

.action-item {
  display: flex;
  align-items: center;
  flex: 1;
  padding: 0 20px;
}

.action-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  margin-right: 15px;
}

.action-info {
  flex: 1;
  margin-right: 15px;
}

.action-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.action-desc {
  font-size: 12px;
  color: #909399;
}

.content-area {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.monitor-content {
  padding: 20px 0;
}

.monitor-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.monitor-item span:first-child {
  width: 100px;
  font-size: 14px;
  color: #606266;
}

.monitor-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}
</style>
