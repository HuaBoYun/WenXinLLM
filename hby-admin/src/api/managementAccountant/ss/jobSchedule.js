import request from '@/utils/request'

/**
 * 作业调度API接口
 */
const jobScheduleApi = {
  // 基础CRUD操作
  getPage: '/accountant/ss/jobSchedule/page',
  getById: '/accountant/ss/jobSchedule',
  getByCode: '/accountant/ss/jobSchedule/code',
  create: '/accountant/ss/jobSchedule',
  update: '/accountant/ss/jobSchedule',
  delete: '/accountant/ss/jobSchedule',
  batchDelete: '/accountant/ss/jobSchedule/batch',

  // 作业控制操作
  start: '/accountant/ss/jobSchedule/{id}/start',
  stop: '/accountant/ss/jobSchedule/{id}/stop',
  pause: '/accountant/ss/jobSchedule/{id}/pause',
  resume: '/accountant/ss/jobSchedule/{id}/resume',
  execute: '/accountant/ss/jobSchedule/{id}/execute',
  reschedule: '/accountant/ss/jobSchedule/{id}/reschedule',

  // 批量操作
  batchStart: '/accountant/ss/jobSchedule/batch/start',
  batchStop: '/accountant/ss/jobSchedule/batch/stop',
  batchPause: '/accountant/ss/jobSchedule/batch/pause',
  batchResume: '/accountant/ss/jobSchedule/batch/resume',
  batchAssignScheduler: '/accountant/ss/jobSchedule/batch/assign-scheduler',
  batchSetPriority: '/accountant/ss/jobSchedule/batch/set-priority',

  // 任务分配管理
  assign: '/accountant/ss/jobSchedule/{id}/assign',
  loadBalance: '/accountant/ss/jobSchedule/load-balance',
  managePriority: '/accountant/ss/jobSchedule/{id}/priority',

  // SLA监控体系
  slaMonitor: '/accountant/ss/jobSchedule/sla-monitor',

  // 调度算法优化
  optimizeAlgorithm: '/accountant/ss/jobSchedule/optimize-algorithm',
  optimizePerformance: '/accountant/ss/jobSchedule/{id}/optimize-performance',

  // 调度报告分析
  report: '/accountant/ss/jobSchedule/report',

  // 容量规划管理
  capacityPlanning: '/accountant/ss/jobSchedule/capacity-planning',

  // 调度策略配置
  configureStrategy: '/accountant/ss/jobSchedule/{id}/configure-strategy',

  // 作业状态查询
  pending: '/accountant/ss/jobSchedule/pending',
  running: '/accountant/ss/jobSchedule/running',
  completed: '/accountant/ss/jobSchedule/completed',
  failed: '/accountant/ss/jobSchedule/failed',

  // 统计分析
  statistics: '/accountant/ss/jobSchedule/statistics',
  jobStatusDistribution: '/accountant/ss/jobSchedule/statistics/job-status',
  jobTypeDistribution: '/accountant/ss/jobSchedule/statistics/job-type',
  scheduleStatusDistribution: '/accountant/ss/jobSchedule/statistics/schedule-status',
  executionStatusDistribution: '/accountant/ss/jobSchedule/statistics/execution-status',
  priorityDistribution: '/accountant/ss/jobSchedule/statistics/priority',
  trend: '/accountant/ss/jobSchedule/statistics/trend',
  schedulerWorkload: '/accountant/ss/jobSchedule/statistics/scheduler-workload',
  executorWorkload: '/accountant/ss/jobSchedule/statistics/executor-workload',
  performanceMetrics: '/accountant/ss/jobSchedule/statistics/performance',
  slaMetrics: '/accountant/ss/jobSchedule/statistics/sla',
  ranking: '/accountant/ss/jobSchedule/ranking',

  // 数据导入导出
  import: '/accountant/ss/jobSchedule/import',
  export: '/accountant/ss/jobSchedule/export',

  // 通知功能
  notify: '/accountant/ss/jobSchedule/{id}/notify',
  batchNotify: '/accountant/ss/jobSchedule/batch/notify'
}

export default {
  /**
   * 分页查询作业调度列表
   */
  getJobSchedulePage(params) {
    return request({
      url: jobScheduleApi.getPage,
      method: 'get',
      params
    })
  },

  /**
   * 根据ID查询作业调度详情
   */
  getJobScheduleById(jobId) {
    return request({
      url: `${jobScheduleApi.getById}/${jobId}`,
      method: 'get'
    })
  },

  /**
   * 根据作业编码查询
   */
  getJobScheduleByCode(jobCode, tenantId = 1) {
    return request({
      url: `${jobScheduleApi.getByCode}/${jobCode}`,
      method: 'get',
      params: { tenantId }
    })
  },

  /**
   * 创建作业调度
   */
  createJobSchedule(data) {
    return request({
      url: jobScheduleApi.create,
      method: 'post',
      data
    })
  },

  /**
   * 更新作业调度
   */
  updateJobSchedule(data) {
    return request({
      url: jobScheduleApi.update,
      method: 'put',
      data
    })
  },

  /**
   * 删除作业调度
   */
  deleteJobSchedule(jobId, tenantId = 1) {
    return request({
      url: `${jobScheduleApi.delete}/${jobId}`,
      method: 'delete',
      params: { tenantId }
    })
  },

  /**
   * 批量删除作业调度
   */
  batchDeleteJobSchedule(jobIds, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchDelete,
      method: 'delete',
      data: jobIds,
      params: { tenantId }
    })
  },

  /**
   * 启动作业调度
   */
  startJobSchedule(jobId, tenantId = 1) {
    return request({
      url: jobScheduleApi.start.replace('{id}', jobId),
      method: 'post',
      params: { tenantId }
    })
  },

  /**
   * 停止作业调度
   */
  stopJobSchedule(jobId, tenantId = 1) {
    return request({
      url: jobScheduleApi.stop.replace('{id}', jobId),
      method: 'post',
      params: { tenantId }
    })
  },

  /**
   * 暂停作业调度
   */
  pauseJobSchedule(jobId, tenantId = 1) {
    return request({
      url: jobScheduleApi.pause.replace('{id}', jobId),
      method: 'post',
      params: { tenantId }
    })
  },

  /**
   * 恢复作业调度
   */
  resumeJobSchedule(jobId, tenantId = 1) {
    return request({
      url: jobScheduleApi.resume.replace('{id}', jobId),
      method: 'post',
      params: { tenantId }
    })
  },

  /**
   * 立即执行作业
   */
  executeJobImmediately(jobId, tenantId = 1) {
    return request({
      url: jobScheduleApi.execute.replace('{id}', jobId),
      method: 'post',
      params: { tenantId }
    })
  },

  /**
   * 重新调度作业
   */
  rescheduleJob(jobId, scheduleExpression, tenantId = 1) {
    return request({
      url: jobScheduleApi.reschedule.replace('{id}', jobId),
      method: 'post',
      params: { scheduleExpression, tenantId }
    })
  },

  /**
   * 批量启动作业调度
   */
  batchStartJobSchedule(jobIds, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchStart,
      method: 'post',
      data: jobIds,
      params: { tenantId }
    })
  },

  /**
   * 批量停止作业调度
   */
  batchStopJobSchedule(jobIds, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchStop,
      method: 'post',
      data: jobIds,
      params: { tenantId }
    })
  },

  /**
   * 批量暂停作业调度
   */
  batchPauseJobSchedule(jobIds, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchPause,
      method: 'post',
      data: jobIds,
      params: { tenantId }
    })
  },

  /**
   * 批量恢复作业调度
   */
  batchResumeJobSchedule(jobIds, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchResume,
      method: 'post',
      data: jobIds,
      params: { tenantId }
    })
  },

  /**
   * 批量分配调度器
   */
  batchAssignScheduler(jobIds, schedulerId, schedulerName, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchAssignScheduler,
      method: 'post',
      data: jobIds,
      params: { schedulerId, schedulerName, tenantId }
    })
  },

  /**
   * 批量设置优先级
   */
  batchSetPriority(jobIds, priority, priorityWeight, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchSetPriority,
      method: 'post',
      data: jobIds,
      params: { priority, priorityWeight, tenantId }
    })
  },

  /**
   * 任务分配管理
   */
  assignJobToScheduler(jobId, schedulerId, loadBalanceStrategy, tenantId = 1) {
    return request({
      url: jobScheduleApi.assign.replace('{id}', jobId),
      method: 'post',
      params: { schedulerId, loadBalanceStrategy, tenantId }
    })
  },

  /**
   * 负载均衡功能
   */
  performLoadBalancing(strategy, jobIds, tenantId = 1) {
    return request({
      url: jobScheduleApi.loadBalance,
      method: 'post',
      data: jobIds,
      params: { strategy, tenantId }
    })
  },

  /**
   * 优先级管理
   */
  managePriority(jobId, priority, weight, reason, tenantId = 1) {
    return request({
      url: jobScheduleApi.managePriority.replace('{id}', jobId),
      method: 'post',
      params: { priority, weight, reason, tenantId }
    })
  },

  /**
   * SLA监控体系
   */
  monitorSLA(params) {
    return request({
      url: jobScheduleApi.slaMonitor,
      method: 'get',
      params
    })
  },

  /**
   * 调度算法优化
   */
  optimizeSchedulingAlgorithm(algorithmType, parameters, tenantId = 1) {
    return request({
      url: jobScheduleApi.optimizeAlgorithm,
      method: 'post',
      data: parameters,
      params: { algorithmType, tenantId }
    })
  },

  /**
   * 性能优化管理
   */
  optimizePerformance(jobId, optimizationType, config, tenantId = 1) {
    return request({
      url: jobScheduleApi.optimizePerformance.replace('{id}', jobId),
      method: 'post',
      data: config,
      params: { optimizationType, tenantId }
    })
  },

  /**
   * 调度报告分析
   */
  generateSchedulingReport(params) {
    return request({
      url: jobScheduleApi.report,
      method: 'get',
      params
    })
  },

  /**
   * 容量规划管理
   */
  planCapacity(planningType, forecastPeriod, parameters, tenantId = 1) {
    return request({
      url: jobScheduleApi.capacityPlanning,
      method: 'post',
      data: parameters,
      params: { planningType, forecastPeriod, tenantId }
    })
  },

  /**
   * 调度策略配置
   */
  configureSchedulingStrategy(jobId, strategyType, config, tenantId = 1) {
    return request({
      url: jobScheduleApi.configureStrategy.replace('{id}', jobId),
      method: 'post',
      data: config,
      params: { strategyType, tenantId }
    })
  },

  /**
   * 查询待调度的作业列表
   */
  getPendingJobs(tenantId = 1) {
    return request({
      url: jobScheduleApi.pending,
      method: 'get',
      params: { tenantId }
    })
  },

  /**
   * 查询正在运行的作业列表
   */
  getRunningJobs(tenantId = 1) {
    return request({
      url: jobScheduleApi.running,
      method: 'get',
      params: { tenantId }
    })
  },

  /**
   * 查询已完成的作业列表
   */
  getCompletedJobs(tenantId = 1) {
    return request({
      url: jobScheduleApi.completed,
      method: 'get',
      params: { tenantId }
    })
  },

  /**
   * 查询失败的作业列表
   */
  getFailedJobs(tenantId = 1) {
    return request({
      url: jobScheduleApi.failed,
      method: 'get',
      params: { tenantId }
    })
  },

  /**
   * 统计作业调度数据
   */
  getJobScheduleStatistics(params) {
    return request({
      url: jobScheduleApi.statistics,
      method: 'get',
      params
    })
  },

  /**
   * 统计作业状态分布
   */
  getJobStatusDistribution(params) {
    return request({
      url: jobScheduleApi.jobStatusDistribution,
      method: 'get',
      params
    })
  },

  /**
   * 统计作业类型分布
   */
  getJobTypeDistribution(params) {
    return request({
      url: jobScheduleApi.jobTypeDistribution,
      method: 'get',
      params
    })
  },

  /**
   * 统计调度状态分布
   */
  getScheduleStatusDistribution(params) {
    return request({
      url: jobScheduleApi.scheduleStatusDistribution,
      method: 'get',
      params
    })
  },

  /**
   * 统计执行状态分布
   */
  getExecutionStatusDistribution(params) {
    return request({
      url: jobScheduleApi.executionStatusDistribution,
      method: 'get',
      params
    })
  },

  /**
   * 统计优先级分布
   */
  getPriorityDistribution(params) {
    return request({
      url: jobScheduleApi.priorityDistribution,
      method: 'get',
      params
    })
  },

  /**
   * 统计作业调度趋势
   */
  getJobScheduleTrend(params) {
    return request({
      url: jobScheduleApi.trend,
      method: 'get',
      params
    })
  },

  /**
   * 统计调度器工作负载
   */
  getSchedulerWorkload(params) {
    return request({
      url: jobScheduleApi.schedulerWorkload,
      method: 'get',
      params
    })
  },

  /**
   * 统计执行用户工作负载
   */
  getExecutorWorkload(params) {
    return request({
      url: jobScheduleApi.executorWorkload,
      method: 'get',
      params
    })
  },

  /**
   * 统计作业性能指标
   */
  getJobPerformanceMetrics(params) {
    return request({
      url: jobScheduleApi.performanceMetrics,
      method: 'get',
      params
    })
  },

  /**
   * 统计SLA达成情况
   */
  getSlaMetrics(params) {
    return request({
      url: jobScheduleApi.slaMetrics,
      method: 'get',
      params
    })
  },

  /**
   * 查询作业调度排行榜
   */
  getJobScheduleRanking(params) {
    return request({
      url: jobScheduleApi.ranking,
      method: 'get',
      params
    })
  },

  /**
   * 导入作业调度数据
   */
  importJobScheduleData(dataList, tenantId = 1) {
    return request({
      url: jobScheduleApi.import,
      method: 'post',
      data: dataList,
      params: { tenantId }
    })
  },

  /**
   * 导出作业调度数据
   */
  exportJobScheduleData(queryParams, tenantId = 1) {
    return request({
      url: jobScheduleApi.export,
      method: 'post',
      data: queryParams,
      params: { tenantId }
    })
  },

  /**
   * 发送通知
   */
  sendNotification(jobId, notificationType, message, tenantId = 1) {
    return request({
      url: jobScheduleApi.notify.replace('{id}', jobId),
      method: 'post',
      params: { notificationType, message, tenantId }
    })
  },

  /**
   * 批量发送通知
   */
  batchSendNotification(jobIds, notificationType, message, tenantId = 1) {
    return request({
      url: jobScheduleApi.batchNotify,
      method: 'post',
      data: jobIds,
      params: { notificationType, message, tenantId }
    })
  },

  // 快捷操作方法
  quickActions: {
    /**
     * 快速启动作业
     */
    quickStart(jobIds, tenantId = 1) {
      if (Array.isArray(jobIds)) {
        return this.batchStartJobSchedule(jobIds, tenantId)
      } else {
        return this.startJobSchedule(jobIds, tenantId)
      }
    },

    /**
     * 快速停止作业
     */
    quickStop(jobIds, tenantId = 1) {
      if (Array.isArray(jobIds)) {
        return this.batchStopJobSchedule(jobIds, tenantId)
      } else {
        return this.stopJobSchedule(jobIds, tenantId)
      }
    },

    /**
     * 快速重启作业
     */
    async quickRestart(jobIds, tenantId = 1) {
      try {
        // 先停止
        if (Array.isArray(jobIds)) {
          await this.batchStopJobSchedule(jobIds, tenantId)
          // 等待一秒后启动
          setTimeout(() => {
            this.batchStartJobSchedule(jobIds, tenantId)
          }, 1000)
        } else {
          await this.stopJobSchedule(jobIds, tenantId)
          setTimeout(() => {
            this.startJobSchedule(jobIds, tenantId)
          }, 1000)
        }
        return { success: true, message: '重启操作已执行' }
      } catch (error) {
        return { success: false, message: '重启操作失败: ' + error.message }
      }
    },

    /**
     * 快速设置高优先级
     */
    setHighPriority(jobIds, tenantId = 1) {
      return this.batchSetPriority(jobIds, 1, 10.0, tenantId)
    },

    /**
     * 快速设置低优先级
     */
    setLowPriority(jobIds, tenantId = 1) {
      return this.batchSetPriority(jobIds, 9, 0.1, tenantId)
    }
  },

  // 工具函数
  utils: {
    /**
     * 格式化作业状态
     */
    formatJobStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '激活',
        'INACTIVE': '未激活',
        'SUSPENDED': '暂停',
        'COMPLETED': '完成',
        'CANCELLED': '取消'
      }
      return statusMap[status] || status
    },

    /**
     * 格式化调度状态
     */
    formatScheduleStatus(status) {
      const statusMap = {
        'STOPPED': '已停止',
        'RUNNING': '运行中',
        'PAUSED': '已暂停',
        'ERROR': '错误'
      }
      return statusMap[status] || status
    },

    /**
     * 格式化执行状态
     */
    formatExecutionStatus(status) {
      const statusMap = {
        'PENDING': '等待中',
        'RUNNING': '执行中',
        'COMPLETED': '已完成',
        'FAILED': '失败',
        'TIMEOUT': '超时',
        'CANCELLED': '已取消',
        'STOPPED': '已停止'
      }
      return statusMap[status] || status
    },

    /**
     * 格式化作业类型
     */
    formatJobType(type) {
      const typeMap = {
        'BATCH': '批处理',
        'REALTIME': '实时处理',
        'SCHEDULED': '定时任务',
        'TRIGGER': '触发任务',
        'WORKFLOW': '工作流',
        'ETL': '数据处理',
        'BACKUP': '备份任务',
        'MAINTENANCE': '维护任务'
      }
      return typeMap[type] || type
    },

    /**
     * 格式化优先级
     */
    formatPriority(priority) {
      if (priority <= 2) return '高'
      if (priority <= 5) return '中'
      if (priority <= 8) return '低'
      return '最低'
    },

    /**
     * 计算成功率百分比
     */
    calculateSuccessRate(successCount, totalCount) {
      if (totalCount === 0) return '0%'
      return ((successCount / totalCount) * 100).toFixed(2) + '%'
    },

    /**
     * 格式化执行时间
     */
    formatDuration(seconds) {
      if (!seconds) return '0秒'

      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      const secs = seconds % 60

      let result = ''
      if (hours > 0) result += hours + '小时'
      if (minutes > 0) result += minutes + '分钟'
      if (secs > 0) result += secs + '秒'

      return result || '0秒'
    },

    /**
     * 验证Cron表达式
     */
    validateCronExpression(expression) {
      if (!expression) return false

      // 简单的Cron表达式验证
      const parts = expression.split(' ')
      return parts.length >= 5 && parts.length <= 7
    },

    /**
     * 生成作业编码
     */
    generateJobCode(prefix = 'JOB') {
      const timestamp = Date.now()
      const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      return `${prefix}_${timestamp}_${random}`
    }
  }
}
