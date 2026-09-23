/**
 * 全面预算模块 Mock 数据
 * 用于在后端接口未实现时提供临时数据支持
 * @author 示例云开发团队
 * @since 2025-01-05
 */

// ==================== 组织体系管理 Mock 数据 ====================

/**
 * Mock数据 - 组织体系分页查询
 * @param {Object} params 查询参数
 * @returns {Object} Mock响应数据
 */
export function mockOrganizationStructurePage(params) {
  return {
    code: 1,
    message: '成功',
    data: {
      tlist: [
        {
          structureId: '1',
          structureCode: 'ORG001',
          structureName: '集团总部',
          structureType: 'single',
          controlMode: 'centralized',
          maxLevels: 5,
          isEnabled: 1,
          description: '集团总部组织体系',
          createTime: '2025-01-01 10:00:00',
          updateTime: '2025-01-05 15:30:00'
        },
        {
          structureId: '2',
          structureCode: 'ORG002',
          structureName: '财务中心',
          structureType: 'hierarchical',
          controlMode: 'decentralized',
          maxLevels: 3,
          isEnabled: 1,
          description: '财务中心组织体系',
          createTime: '2025-01-02 14:30:00',
          updateTime: '2025-01-04 09:20:00'
        },
        {
          structureId: '3',
          structureCode: 'ORG003',
          structureName: '销售部门',
          structureType: 'matrix',
          controlMode: 'hybrid',
          maxLevels: 4,
          isEnabled: 1,
          description: '销售部门组织体系',
          createTime: '2025-01-03 11:15:00',
          updateTime: '2025-01-05 16:45:00'
        }
      ],
      totalRecord: 3
    }
  }
}

// ==================== 高级功能 Mock 数据 ====================

/**
 * Mock数据 - 高级功能统计
 * @returns {Object} Mock响应数据
 */
export function mockFeatureStats() {
  return {
    code: 1,
    msg: '成功',
    data: {
      rollingBudgets: 12,
      formulaTraces: 45,
      reminderTasks: 8,
      currencies: 15
    }
  }
}

/**
 * Mock数据 - 最近活动
 * @returns {Object} Mock响应数据
 */
export function mockRecentActivities() {
  return {
    code: 1,
    msg: '成功',
    data: [
      {
        id: '1',
        type: 'create',
        title: '2025年Q1滚动预算执行',
        description: '成功执行2025年第一季度滚动预算计划',
        status: 'completed',
        timestamp: '2025-01-05 10:30:00',
        operator: '张三'
      },
      {
        id: '2',
        type: 'execute',
        title: '销售费用公式依赖分析',
        description: '完成销售费用科目的公式依赖关系追踪',
        status: 'completed',
        timestamp: '2025-01-05 09:15:00',
        operator: '李四'
      },
      {
        id: '3',
        type: 'update',
        title: '预算填报催报提醒',
        description: '向5个部门发送预算填报催报通知',
        status: 'in_progress',
        timestamp: '2025-01-05 08:00:00',
        operator: '王五'
      },
      {
        id: '4',
        type: 'update',
        title: '汇率更新',
        description: '更新美元、欧元、日元汇率',
        status: 'completed',
        timestamp: '2025-01-04 16:45:00',
        operator: '赵六'
      },
      {
        id: '5',
        type: 'execute',
        title: '年度预算批量计算',
        description: '执行2025年度全部门预算批量计算任务',
        status: 'completed',
        timestamp: '2025-01-04 14:20:00',
        operator: '孙七'
      }
    ]
  }
}

/**
 * Mock数据 - 系统通知
 * @returns {Object} Mock响应数据
 */
export function mockNotifications() {
  return {
    code: 1,
    msg: '成功',
    data: [
      {
        id: '1',
        type: 'warning',
        title: '预算超支预警',
        message: '市场部2025年1月预算执行率已达95%，请注意控制',
        time: '2025-01-05 11:00:00',
        isRead: false
      },
      {
        id: '2',
        type: 'info',
        title: '系统升级通知',
        message: '系统将于2025年1月10日进行升级维护，预计停机2小时',
        time: '2025-01-05 09:30:00',
        isRead: false
      },
      {
        id: '3',
        type: 'success',
        title: '预算审批通过',
        message: '您提交的2025年Q1预算调整申请已通过审批',
        time: '2025-01-04 17:20:00',
        isRead: true
      },
      {
        id: '4',
        type: 'error',
        title: '数据同步失败',
        message: 'ERP系统数据同步失败，请检查网络连接',
        time: '2025-01-04 15:10:00',
        isRead: true
      }
    ]
  }
}

// ==================== 执行监控 Mock 数据 ====================

/**
 * Mock数据 - 执行监控分页查询
 * @param {Object} params 查询参数
 * @returns {Object} Mock响应数据
 */
export function mockExecutionPage(params) {
  return {
    code: 1,
    message: '成功',
    data: {
      records: [
        {
          id: '1',
          budgetPeriod: '2025-01',
          organizationName: '销售部',
          accountName: '办公费用',
          budgetAmount: 50000,
          actualAmount: 42500,
          executionRate: 85,
          variance: -7500,
          varianceRate: -15,
          executionStatus: 'NORMAL',
          lastUpdateTime: '2025-01-10 16:00:00'
        },
        {
          id: '2',
          budgetPeriod: '2025-01',
          organizationName: '市场部',
          accountName: '差旅费',
          budgetAmount: 80000,
          actualAmount: 85000,
          executionRate: 106.25,
          variance: 5000,
          varianceRate: 6.25,
          executionStatus: 'WARNING',
          lastUpdateTime: '2025-01-10 15:30:00'
        },
        {
          id: '3',
          budgetPeriod: '2025-01',
          organizationName: '研发部',
          accountName: '研发费用',
          budgetAmount: 200000,
          actualAmount: 180000,
          executionRate: 90,
          variance: -20000,
          varianceRate: -10,
          executionStatus: 'NORMAL',
          lastUpdateTime: '2025-01-10 14:45:00'
        }
      ],
      total: 10
    }
  }
}

/**
 * Mock数据 - 组织列表
 * @returns {Object} Mock响应数据
 */
export function mockOrganizations() {
  return {
    code: 1,
    message: '成功',
    data: [
      { organizationId: '1', organizationName: '销售部' },
      { organizationId: '2', organizationName: '市场部' },
      { organizationId: '3', organizationName: '研发部' },
      { organizationId: '4', organizationName: '财务部' }
    ]
  }
}

/**
 * Mock数据 - 科目列表
 * @returns {Object} Mock响应数据
 */
export function mockAccounts() {
  return {
    code: 1,
    message: '成功',
    data: [
      { accountId: '1', accountName: '办公费用' },
      { accountId: '2', accountName: '差旅费' },
      { accountId: '3', accountName: '研发费用' },
      { accountId: '4', accountName: '营销费用' }
    ]
  }
}

// ==================== 差异分析 Mock 数据 ====================

/**
 * Mock数据 - 差异分析
 * @param {Object} params 查询参数
 * @returns {Object} Mock响应数据
 */
export function mockVarianceAnalysis(params) {
  return {
    code: 1,
    message: '成功',
    data: {
      records: [
        {
          id: '1',
          organizationName: '销售部',
          budgetAccountName: '差旅费',
          budgetAmount: 100000,
          actualAmount: 85000,
          varianceAmount: -15000,
          varianceRate: -15,
          varianceType: 'NEGATIVE',
          varianceReason: '业务量减少导致差旅费用降低',
          improvementMeasures: '优化差旅路线，提高出差效率',
          responsiblePerson: '张三',
          analysisDate: '2025-01-10'
        },
        {
          id: '2',
          organizationName: '市场部',
          budgetAccountName: '营销费用',
          budgetAmount: 150000,
          actualAmount: 165000,
          varianceAmount: 15000,
          varianceRate: 10,
          varianceType: 'POSITIVE',
          varianceReason: '新产品推广活动增加',
          improvementMeasures: '加强预算控制，优化营销策略',
          responsiblePerson: '李四',
          analysisDate: '2025-01-10'
        }
      ],
      total: 5
    }
  }
}

/**
 * Mock数据 - 用户列表
 * @returns {Object} Mock响应数据
 */
export function mockUsers() {
  return {
    code: 1,
    message: '成功',
    data: [
      { userId: '1', userName: '张三' },
      { userId: '2', userName: '李四' },
      { userId: '3', userName: '王五' }
    ]
  }
}

// ==================== 集成监控 Mock 数据 ====================

/**
 * Mock数据 - 集成状态
 * @param {Object} params 查询参数
 * @returns {Object} Mock响应数据
 */
export function mockIntegrationStatus(params) {
  return {
    code: 1,
    message: '成功',
    data: [
      {
        integrationId: '1',
        integrationName: 'ERP系统集成',
        integrationType: 'ERP',
        status: 'RUNNING',
        availability: 99.5,
        responseTime: 120,
        throughput: 1500,
        errorRate: 0.5,
        lastCheckTime: '2025-01-10 16:30:00'
      },
      {
        integrationId: '2',
        integrationName: 'BI数据推送',
        integrationType: 'BI',
        status: 'RUNNING',
        availability: 98.8,
        responseTime: 200,
        throughput: 800,
        errorRate: 1.2,
        lastCheckTime: '2025-01-10 16:29:00'
      },
      {
        integrationId: '3',
        integrationName: 'OA系统对接',
        integrationType: 'API',
        status: 'STOPPED',
        availability: 0,
        responseTime: 0,
        throughput: 0,
        errorRate: 0,
        lastCheckTime: '2025-01-10 10:00:00'
      }
    ]
  }
}

/**
 * Mock数据 - 监控统计
 * @returns {Object} Mock响应数据
 */
export function mockIntegrationStats() {
  return {
    code: 1,
    message: '成功',
    data: {
      totalIntegrations: 5,
      runningIntegrations: 3,
      stoppedIntegrations: 1,
      errorIntegrations: 1,
      avgAvailability: 95.5,
      avgResponseTime: 180,
      totalThroughput: 3500
    }
  }
}

