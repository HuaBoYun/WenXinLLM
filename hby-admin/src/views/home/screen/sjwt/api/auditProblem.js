/**
 * 审计问题数据接口
 */

/**
 * 获取完整的审计问题数据
 */
export function getAuditProblemData() {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        // 审计问题整改迟缓事项
        delayedItems: [
          { id: 1, projectName: 'xxxx建设控股有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '1.00', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 2, projectName: 'xxxx汽车有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.90', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 3, projectName: 'xxxx投资发展有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.85', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 4, projectName: 'xxxx汽车集团有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.80', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 5, projectName: 'xxxx集团有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.75', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 6, projectName: 'xxxx建设开发有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.70', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 7, projectName: 'xxxx集团有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.65', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 8, projectName: 'xxxx汽车集团有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.60', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 9, projectName: 'xxxx建设集团有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.55', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' },
          { id: 10, projectName: 'xxxx汽车集团有限公司', auditType: '其它类审计事项1', date: '2018-10-05', type: '委托1', person: '张明', dept: '系统工程中心', amount: '0.50', nature: '本元成', remark: '样本单位和样本项目样本单位和样本项目样本单位和样本项目样本单位和样本项目' }
        ],
        // 审计项目数（环形饼图）
        auditProjectStats: [
          { name: '委托1', value: 26 },
          { name: '委托2', value: 18 },
          { name: '委托3', value: 16 },
          { name: '委托4', value: 30 },
          { name: '委托5', value: 15 }
        ],
        // 审计覆盖情况（环形饼图）
        auditCoverageStats: [
          { name: '已覆盖', value: 600 },
          { name: '未覆盖', value: 400 }
        ],
        // 审计项目计划完成情况（环形饼图）
        projectCompletionStats: [
          { name: '已完成', value: 60 },
          { name: '进行中', value: 25 },
          { name: '未开始', value: 15 }
        ],
        // 高风险评估事项（环形饼图）
        highRiskStats: [
          { name: '委托1', value: 32 },
          { name: '委托2', value: 12 },
          { name: '委托3', value: 5 },
          { name: '其他', value: 15 }
        ],
        // 集团审计问题数量变化趋势（折线图）
        problemTrendChart: {
          categories: ['2018/2', '2018/3', '2018/4', '2018/5', '2018/6', '2018/7', '2018/8', '2018/9', '2018/10', '2018/11', '2018/12', '2019/1'],
          data: [
            { name: '审计问题数量', values: [2000, 4500, 3500, 5000, 4000, 3000, 4500, 3800, 4200, 3500, 4000, 3200] }
          ]
        },
        // 整体统计
        overallStats: {
          projectCount: 156,
          problemCount: 1245,
          rectificationRate: 60,
          involvedUnits: 89
        }
      })
    }, 300)
  })
}

/**
 * 获取审计问题整改迟缓事项列表
 */
export function getDelayedItems() {
  return getAuditProblemData().then(data => data.delayedItems)
}

/**
 * 获取审计项目数统计
 */
export function getAuditProjectStats() {
  return getAuditProblemData().then(data => data.auditProjectStats)
}

/**
 * 获取审计覆盖情况统计
 */
export function getAuditCoverageStats() {
  return getAuditProblemData().then(data => data.auditCoverageStats)
}

/**
 * 获取审计项目计划完成情况统计
 */
export function getProjectCompletionStats() {
  return getAuditProblemData().then(data => data.projectCompletionStats)
}

/**
 * 获取高风险评估事项统计
 */
export function getHighRiskStats() {
  return getAuditProblemData().then(data => data.highRiskStats)
}

/**
 * 获取集团审计问题数量变化趋势
 */
export function getProblemTrendChart() {
  return getAuditProblemData().then(data => data.problemTrendChart)
}

/**
 * 获取整体统计数据
 */
export function getOverallStats() {
  return getAuditProblemData().then(data => data.overallStats)
}
