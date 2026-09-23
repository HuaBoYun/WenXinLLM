/**
 * 穿透式监管模块 Mock 数据
 * 用于在后端接口未实现时提供临时数据支持，让前端页面可以预览样式
 * @author 示例云开发团队
 * @since 2026-03-27
 */

// ==================== 通用工具函数 ====================
const companyNames = ['示例集团总部', '示例科技有限公司', '示例新能源股份', '示例智能装备', '示例国际贸易', '示例金融控股', '示例地产开发', '示例医药科技', '示例信息技术', '示例物流集团']
const statusList = ['PENDING', 'APPROVED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED']
const riskLevels = ['HIGH', 'MEDIUM', 'LOW']

function randomDate(start, end) {
  const d = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()))
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0') + ' ' + String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0') + ':' + String(d.getSeconds()).padStart(2, '0')
}
function randomNum(min, max) { return Math.floor(Math.random() * (max - min + 1)) + min }
function randomDecimal(min, max, digits = 2) { return parseFloat((Math.random() * (max - min) + min).toFixed(digits)) }
function randomItem(arr) { return arr[Math.floor(Math.random() * arr.length)] }
function genId(prefix, i) { return prefix + String(i).padStart(6, '0') }
const d1 = new Date('2025-01-01'), d2 = new Date('2026-03-27')

// ==================== 投资穿透 Mock 数据 ====================
function mockInvestProjectList() {
  const types = ['EQUITY', 'DEBT', 'FUND', 'OTHER']
  const industries = ['新能源', '半导体', '人工智能', '生物医药', '高端装备', '新材料', '信息技术', '金融服务', '现代物流', '文化传媒']
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    projectId: genId('INV', i + 1), projectName: `${randomItem(['示例', '国盛', '中科', '鼎新', '恒信'])}${randomItem(industries)}投资项目${i + 1}号`,
    investType: randomItem(types), investAmount: randomNum(500, 50000), targetCompany: randomItem(companyNames),
    approvalStatus: randomItem(statusList.slice(0, 3)), complianceStatus: randomItem(['COMPLIANT', 'NON_COMPLIANT', 'PENDING']),
    approvalChain: '董事会→投委会→总经理', decisionLevel: randomItem(['董事会', '总经理办公会', '投委会']),
    overrideFlag: Math.random() > 0.8 ? 'Y' : 'N', industry: randomItem(industries),
    expectedReturn: randomNum(100, 5000), actualReturn: randomNum(50, 6000),
    evalStatus: randomItem(['PENDING', 'IN_PROGRESS', 'COMPLETED']), impairmentFlag: Math.random() > 0.7 ? 'Y' : 'N',
    isMainBusiness: Math.random() > 0.3 ? 'Y' : 'N', remark: '项目进展正常',
    createUser: '管理员', createTime: randomDate(d1, d2), updateTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 56, currentPage: 1, pageSize: 10 } }
}

function mockInvestStatistics() {
  return { result: 200, msg: '成功', data: { totalAmount: 285600, projectCount: 56, riskCount: 8, nonMainTotal: 42300, nonMainRatio: '14.8%', nonMainCount: 12, approvedCount: 38, pendingCount: 11, rejectedCount: 7 } }
}

function mockInvestDetail() {
  return { result: 200, msg: '成功', data: { projectId: 'INV000001', projectName: '示例新能源投资项目1号', investType: 'EQUITY', investAmount: 15000, targetCompany: '示例新能源股份', approvalStatus: 'APPROVED', createTime: '2025-06-15 10:30:00', remark: '战略性投资项目，预期收益良好' } }
}

// ==================== 产权穿透 Mock 数据 ====================
function mockPropertyRightList() {
  const types = ['SOLE', 'HOLDING', 'PARTICIPATING']
  const regStatus = ['REGISTERED', 'PENDING', 'CHANGING']
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    rightId: genId('PR', i + 1), companyName: companyNames[i], rightType: randomItem(types),
    holdingRatio: randomDecimal(10, 100), registeredCapital: randomNum(1000, 100000),
    registrationStatus: randomItem(regStatus), parentCompany: i > 0 ? companyNames[0] : '-',
    equityLevel: randomNum(1, 5), changeType: randomItem(['新设', '增资', '转让', '减资']),
    changeDate: randomDate(d1, d2).split(' ')[0], changeAmount: randomNum(100, 50000),
    createUser: '管理员', createTime: randomDate(d1, d2), updateTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 42, currentPage: 1, pageSize: 10 } }
}

function mockPropertyTransactionList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    transactionId: genId('PT', i + 1), companyName: randomItem(companyNames),
    transactionType: randomItem(['挂牌转让', '协议转让', '增资扩股', '产权置换']),
    transactionAmount: randomNum(500, 80000), buyerName: randomItem(['中信集团', '国投资本', '招商局', '中粮集团', '保利集团']),
    transactionStatus: randomItem(['进行中', '已完成', '已终止']), listingDate: randomDate(d1, d2).split(' ')[0],
    completionDate: Math.random() > 0.3 ? randomDate(d1, d2).split(' ')[0] : '',
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 35, currentPage: 1, pageSize: 10 } }
}


// ==================== 财务穿透 Mock 数据 ====================
function mockFinanceStatementList() {
  const periods = ['2025-Q1', '2025-Q2', '2025-Q3', '2025-Q4', '2026-Q1']
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    statementId: genId('FS', i + 1), companyName: randomItem(companyNames), reportPeriod: randomItem(periods),
    reportType: randomItem(['资产负债表', '利润表', '现金流量表']), totalAssets: randomNum(100000, 5000000),
    totalLiabilities: randomNum(50000, 3000000), netProfit: randomNum(-50000, 500000),
    assetLiabilityRatio: randomDecimal(30, 85), auditStatus: randomItem(['已审计', '未审计', '审计中']),
    anomalyFlag: Math.random() > 0.7 ? 'Y' : 'N', benchmarkScore: randomDecimal(60, 100),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 48, currentPage: 1, pageSize: 10 } }
}

function mockRelatedTransactionList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    transactionId: genId('RT', i + 1), companyName: randomItem(companyNames),
    relatedParty: randomItem(['示例金融控股', '示例地产开发', '示例国际贸易', '示例医药科技']),
    transactionType: randomItem(['商品购销', '资产转让', '资金借贷', '担保', '服务提供']),
    transactionAmount: randomNum(100, 50000), pricingMethod: randomItem(['市场价格', '协议定价', '成本加成']),
    fairnessStatus: randomItem(['公允', '待审查', '异常']), approvalStatus: randomItem(['已审批', '待审批']),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 38, currentPage: 1, pageSize: 10 } }
}

function mockFinanceStatistics() {
  return { result: 200, msg: '成功', data: { totalStatements: 48, relatedTransactions: 38, anomalyCount: 6, benchmarkAvg: 78.5 } }
}

// ==================== 金融风险穿透 Mock 数据 ====================
function mockFinancingRecordList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    financingId: genId('FR', i + 1), companyName: randomItem(companyNames),
    financingType: randomItem(['银行贷款', '债券发行', '信托融资', '融资租赁', '委托贷款']),
    financingAmount: randomNum(1000, 100000), interestRate: randomDecimal(3, 8),
    maturityDate: randomDate(d1, new Date('2028-12-31')).split(' ')[0],
    financingStatus: randomItem(['存续', '已到期', '提前偿还']), riskLevel: randomItem(riskLevels),
    lender: randomItem(['工商银行', '建设银行', '中国银行', '农业银行', '交通银行']),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 62, currentPage: 1, pageSize: 10 } }
}

function mockGuaranteeRecordList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    guaranteeId: genId('GR', i + 1), companyName: randomItem(companyNames),
    guaranteeType: randomItem(['连带责任担保', '一般担保', '抵押担保', '质押担保']),
    guaranteeAmount: randomNum(500, 80000), guaranteedParty: randomItem(companyNames),
    guaranteeStatus: randomItem(['有效', '已解除', '代偿中']), riskLevel: randomItem(riskLevels),
    startDate: randomDate(d1, d2).split(' ')[0], endDate: randomDate(d2, new Date('2028-12-31')).split(' ')[0],
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 45, currentPage: 1, pageSize: 10 } }
}

function mockFinancialRiskStatistics() {
  return { result: 200, msg: '成功', data: { totalFinancing: 285600, totalGuarantee: 156800, highRiskCount: 5, overdueCount: 3, financingCount: 62, guaranteeCount: 45 } }
}

// ==================== 会计穿透 Mock 数据 ====================
function mockAccountingPolicyList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    policyId: genId('AP', i + 1), companyName: randomItem(companyNames),
    policyType: randomItem(['收入确认', '折旧方法', '存货计价', '减值准备', '合并范围', '金融工具分类']),
    policyContent: randomItem(['按完工百分比法确认收入', '采用直线法计提折旧', '采用加权平均法', '按账龄分析法计提', '纳入合并范围', '以公允价值计量']),
    effectiveDate: randomDate(d1, d2).split(' ')[0], changeReason: randomItem(['准则变更', '管理层决策', '业务调整', '监管要求']),
    auditOpinion: randomItem(['标准无保留', '带强调事项段', '保留意见', '无法表示意见']),
    estimateType: randomItem(['坏账准备', '折旧年限', '减值测试', '公允价值']),
    estimateAmount: randomNum(100, 50000), impactAmount: randomNum(-10000, 10000),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 36, currentPage: 1, pageSize: 10 } }
}

function mockAccountingStatistics() {
  return { result: 200, msg: '成功', data: { totalPolicies: 36, changeCount: 12, abnormalOpinions: 4, estimateChanges: 8 } }
}
function mockPropertyStatistics() {
  return { result: 200, msg: '成功', data: { totalRights: 42, equityLevels: 5, changeCount: 18, warningCount: 3 } }
}

// ==================== 薪酬穿透 Mock 数据 ====================
function mockSalaryTotalList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    salaryId: genId('SAL', i + 1), companyName: companyNames[i], year: randomItem(['2024', '2025', '2026']),
    totalSalary: randomNum(5000, 80000), executivePay: randomNum(200, 5000),
    performanceRatio: randomDecimal(30, 80), incentiveAmount: randomNum(100, 3000),
    laborCost: randomNum(3000, 60000), headcount: randomNum(50, 5000),
    avgSalary: randomDecimal(8, 35), payGrowthRate: randomDecimal(-5, 20),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 30, currentPage: 1, pageSize: 10 } }
}

function mockSalaryStatistics() {
  return { result: 200, msg: '成功', data: { totalSalary: 356000, avgSalary: 18.5, executivePayTotal: 28600, performanceLinkRatio: 62.3, totalHeadcount: 19200, laborCostRatio: 45.8 } }
}

// ==================== 军品穿透 Mock 数据 ====================
function mockMilitaryTaskList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    taskId: genId('MIL', i + 1), taskName: `${randomItem(['型号', '预研', '批产', '维修', '改装'])}任务${randomItem(['甲', '乙', '丙', '丁'])}${i + 1}`,
    taskType: randomItem(['科研', '生产', '维修保障', '技术改造']),
    qualificationStatus: randomItem(['有效', '待审', '过期']), securityLevel: randomItem(['机密', '秘密', '内部']),
    qualityScore: randomDecimal(70, 100), assetAmount: randomNum(1000, 100000),
    progressRate: randomDecimal(10, 100), responsiblePerson: randomItem(['张工', '李工', '王工', '赵工']),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 28, currentPage: 1, pageSize: 10 } }
}

function mockMilitaryStatistics() {
  return { result: 200, msg: '成功', data: { totalTasks: 28, qualifiedCount: 22, avgQualityScore: 88.6, totalAssetAmount: 568000, onTimeRate: 91.2 } }
}

// ==================== 采购穿透 Mock 数据 ====================
function mockProcurementProjectList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    projectId: genId('PROC', i + 1), projectName: `${randomItem(['办公设备', '原材料', 'IT设备', '工程服务', '咨询服务'])}采购项目${i + 1}`,
    procurementType: randomItem(['公开招标', '邀请招标', '竞争性谈判', '单一来源', '询价']),
    budgetAmount: randomNum(50, 50000), actualAmount: randomNum(40, 48000),
    supplierName: randomItem(['华为技术', '中兴通讯', '联想集团', '海尔集团', '格力电器', '比亚迪', '宁德时代']),
    procurementStatus: randomItem(['计划中', '招标中', '评标中', '已定标', '已完成']),
    savingRate: randomDecimal(2, 15), complianceStatus: randomItem(['合规', '待审查', '整改中']),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 85, currentPage: 1, pageSize: 10 } }
}

function mockSupplierList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    supplierId: genId('SUP', i + 1), supplierName: randomItem(['华为技术', '中兴通讯', '联想集团', '海尔集团', '格力电器', '比亚迪', '宁德时代', '京东方', '中芯国际', '紫光集团']),
    supplierType: randomItem(['生产商', '代理商', '服务商']), creditLevel: randomItem(['AAA', 'AA', 'A', 'BBB']),
    cooperationYears: randomNum(1, 15), totalAmount: randomNum(1000, 500000),
    qualityScore: randomDecimal(60, 100), deliveryRate: randomDecimal(80, 100),
    blacklistFlag: Math.random() > 0.9 ? 'Y' : 'N', createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 120, currentPage: 1, pageSize: 10 } }
}

function mockProcurementStatistics() {
  return { result: 200, msg: '成功', data: { totalProjects: 85, totalAmount: 256000, avgSavingRate: 8.6, supplierCount: 120, complianceRate: 94.2 } }
}

// ==================== 境外穿透 Mock 数据 ====================
function mockOverseasUnitList() {
  const countries = ['美国', '英国', '德国', '日本', '新加坡', '澳大利亚', '巴西', '阿联酋', '印度尼西亚', '南非']
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    unitId: genId('OVS', i + 1), unitName: `示例${countries[i]}${randomItem(['子公司', '分公司', '办事处', '合资公司'])}`,
    country: countries[i], investAmount: randomNum(1000, 100000),
    riskLevel: randomItem(riskLevels), complianceStatus: randomItem(['合规', '待审查', '整改中']),
    forexExposure: randomNum(500, 50000), localRevenue: randomNum(2000, 80000),
    employeeCount: randomNum(10, 500), operatingStatus: randomItem(['正常运营', '筹建中', '整合中']),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 18, currentPage: 1, pageSize: 10 } }
}

function mockOverseasStatistics() {
  return { result: 200, msg: '成功', data: { totalUnits: 18, totalInvestment: 458000, highRiskCount: 3, forexExposureTotal: 186000, countryCoverage: 10 } }
}

// ==================== 合同穿透 Mock 数据 ====================
function mockContractRecordList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    contractId: genId('CON', i + 1), contractName: `${randomItem(['采购', '销售', '服务', '工程', '租赁', '技术'])}合同${i + 1}号`,
    contractType: randomItem(['采购合同', '销售合同', '服务合同', '工程合同', '租赁合同']),
    contractAmount: randomNum(100, 100000), partyA: randomItem(companyNames), partyB: randomItem(['中信集团', '国投资本', '招商局', '中粮集团']),
    signDate: randomDate(d1, d2).split(' ')[0], performanceStatus: randomItem(['履行中', '已完成', '违约处理中', '已终止']),
    paymentProgress: randomDecimal(0, 100), riskLevel: randomItem(riskLevels),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 156, currentPage: 1, pageSize: 10 } }
}

function mockContractDisputeList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    disputeId: genId('DIS', i + 1), contractName: `合同纠纷案${i + 1}号`,
    disputeType: randomItem(['质量纠纷', '付款纠纷', '交付延迟', '违约责任', '知识产权']),
    disputeAmount: randomNum(50, 50000), disputeStatus: randomItem(['调解中', '仲裁中', '诉讼中', '已解决']),
    filingDate: randomDate(d1, d2).split(' ')[0], responsibleParty: randomItem(companyNames),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 23, currentPage: 1, pageSize: 10 } }
}

function mockContractStatistics() {
  return { result: 200, msg: '成功', data: { totalContracts: 156, totalAmount: 1256000, disputeCount: 23, performanceRate: 92.3, overdueCount: 8 } }
}

// ==================== 协同核查 Mock 数据 ====================
function mockInvestigationTaskList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    taskId: genId('INV_T', i + 1), taskName: `${randomItem(['专项', '常规', '联合', '重点'])}核查任务${i + 1}`,
    taskType: randomItem(['财务核查', '合规核查', '资产核查', '人事核查', '综合核查']),
    targetCompany: randomItem(companyNames), taskStatus: randomItem(['待分配', '进行中', '已完成', '已归档']),
    startDate: randomDate(d1, d2).split(' ')[0], dueDate: randomDate(d2, new Date('2026-12-31')).split(' ')[0],
    leadInvestigator: randomItem(['张主任', '李主任', '王主任', '赵主任']),
    findingsCount: randomNum(0, 15), createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 42, currentPage: 1, pageSize: 10 } }
}

function mockRectificationList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    rectificationId: genId('REC', i + 1), taskName: `整改事项${i + 1}`,
    issueDescription: randomItem(['财务数据不一致', '审批流程缺失', '资产台账不完整', '合同管理不规范', '人员配置不合理']),
    rectificationStatus: randomItem(['待整改', '整改中', '已整改', '验收通过']),
    responsiblePerson: randomItem(['张经理', '李经理', '王经理', '赵经理']),
    deadline: randomDate(d2, new Date('2026-12-31')).split(' ')[0],
    completionRate: randomDecimal(0, 100), createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 35, currentPage: 1, pageSize: 10 } }
}

function mockInvestigationStatistics() {
  return { result: 200, msg: '成功', data: { totalTasks: 42, completedTasks: 28, rectificationCount: 35, rectificationRate: 78.6, pendingCount: 14 } }
}


// ==================== 股权穿透 Mock 数据 ====================
function mockEquityStructureList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    structureId: genId('EQ', i + 1), companyName: companyNames[i],
    shareholderName: randomItem(['国务院国资委', '示例集团总部', '示例科技有限公司', '社会资本方A', '战略投资者B']),
    holdingRatio: randomDecimal(5, 100), equityType: randomItem(['国有股', '法人股', '社会公众股', '外资股']),
    equityLevel: randomNum(1, 5), controlType: randomItem(['绝对控股', '相对控股', '参股']),
    registeredCapital: randomNum(1000, 500000), paidInCapital: randomNum(800, 500000),
    changeDate: randomDate(d1, d2).split(' ')[0], createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 68, currentPage: 1, pageSize: 10 } }
}

function mockEquityStatistics() {
  return { result: 200, msg: '成功', data: { totalEntities: 68, maxLevel: 5, stateOwnedRatio: 72.5, changeCount: 15, crossHoldingCount: 3 } }
}

// ==================== 财务分析 Mock 数据 ====================
function mockFinancialAnalysisList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    analysisId: genId('FA', i + 1), companyName: randomItem(companyNames),
    analysisType: randomItem(['偿债能力', '盈利能力', '营运能力', '发展能力', '综合评价']),
    period: randomItem(['2025-Q1', '2025-Q2', '2025-Q3', '2025-Q4', '2026-Q1']),
    score: randomDecimal(50, 100), riskLevel: randomItem(riskLevels),
    currentRatio: randomDecimal(0.8, 3), debtRatio: randomDecimal(30, 80),
    roe: randomDecimal(-5, 30), revenueGrowth: randomDecimal(-10, 50),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 50, currentPage: 1, pageSize: 10 } }
}

// ==================== 资产穿透 Mock 数据 ====================
function mockAssetAllocationList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    allocationId: genId('AST', i + 1), companyName: randomItem(companyNames),
    assetType: randomItem(['固定资产', '无形资产', '长期投资', '流动资产', '在建工程']),
    assetAmount: randomNum(5000, 500000), allocationRatio: randomDecimal(5, 40),
    qualityScore: randomDecimal(60, 100), impairmentAmount: randomNum(0, 50000),
    returnRate: randomDecimal(-2, 15), concentrationIndex: randomDecimal(0.1, 0.8),
    createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 45, currentPage: 1, pageSize: 10 } }
}

// ==================== 风险穿透 Mock 数据 ====================
function mockRiskAssessmentList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    assessmentId: genId('RISK', i + 1), companyName: randomItem(companyNames),
    riskType: randomItem(['市场风险', '信用风险', '操作风险', '流动性风险', '合规风险', '战略风险']),
    riskLevel: randomItem(riskLevels), riskScore: randomDecimal(20, 100),
    probability: randomDecimal(0.05, 0.95), impactLevel: randomItem(['重大', '较大', '一般', '较小']),
    controlStatus: randomItem(['已控制', '监控中', '待处置', '已处置']),
    assessDate: randomDate(d1, d2).split(' ')[0], createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 78, currentPage: 1, pageSize: 10 } }
}

function mockRiskStatistics() {
  return { result: 200, msg: '成功', data: { totalRisks: 78, highRiskCount: 12, mediumRiskCount: 28, lowRiskCount: 38, controlRate: 85.6, incidentCount: 5 } }
}

// ==================== 协同管理 Mock 数据 ====================
function mockCollaborationList() {
  const tlist = Array.from({ length: 10 }, (_, i) => ({
    collaborationId: genId('COL', i + 1), taskName: `协同任务${i + 1}`,
    taskType: randomItem(['数据共享', '联合核查', '信息通报', '协同处置', '联合监管']),
    initiator: randomItem(companyNames), participants: randomItem(['3家', '5家', '8家', '全部']),
    taskStatus: randomItem(['待启动', '进行中', '已完成', '已归档']),
    priority: randomItem(['紧急', '高', '中', '低']),
    startDate: randomDate(d1, d2).split(' ')[0], createTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: 32, currentPage: 1, pageSize: 10 } }
}

// ==================== 综合驾驶舱 Mock 数据 ====================
function mockDashboardOverview() {
  return {
    result: 200, msg: '成功', data: {
      totalEnterprise: 156, totalAssets: 28560000, totalRevenue: 8560000, totalProfit: 1256000,
      investmentCount: 56, propertyCount: 42, financingCount: 62, contractCount: 156,
      riskAlertCount: 23, highRiskCount: 5, rectificationRate: 78.6,
      domainStats: [
        { domain: '投资穿透', count: 56, amount: 285600, riskCount: 8 },
        { domain: '产权穿透', count: 42, amount: 0, riskCount: 3 },
        { domain: '财务穿透', count: 48, amount: 0, riskCount: 6 },
        { domain: '金融风险', count: 62, amount: 285600, riskCount: 5 },
        { domain: '会计穿透', count: 36, amount: 0, riskCount: 4 },
        { domain: '薪酬穿透', count: 30, amount: 356000, riskCount: 2 },
        { domain: '军品穿透', count: 28, amount: 568000, riskCount: 1 },
        { domain: '采购穿透', count: 85, amount: 256000, riskCount: 3 },
        { domain: '境外穿透', count: 18, amount: 458000, riskCount: 3 },
        { domain: '合同穿透', count: 156, amount: 1256000, riskCount: 8 },
      ],
      recentAlerts: Array.from({ length: 8 }, (_, i) => ({
        alertId: genId('ALT', i + 1), level: randomItem(riskLevels),
        title: randomItem(['投资项目超预算预警', '融资到期提醒', '合同履行异常', '资产减值风险', '关联交易异常', '境外合规风险', '采购价格偏离', '薪酬总额超标']),
        time: randomDate(new Date('2026-03-01'), d2), domain: randomItem(['投资', '产权', '财务', '金融', '合同']),
      })),
    }
  }
}

// ==================== 通用 Mock 生成器 ====================
function mockGenericList(prefix, count = 10, total = 50) {
  const tlist = Array.from({ length: count }, (_, i) => ({
    id: genId(prefix, i + 1), name: `${prefix}记录${i + 1}`, companyName: randomItem(companyNames),
    status: randomItem(statusList), riskLevel: randomItem(riskLevels),
    amount: randomNum(100, 100000), score: randomDecimal(50, 100),
    createUser: '管理员', createTime: randomDate(d1, d2), updateTime: randomDate(d1, d2),
  }))
  return { result: 200, msg: '成功', data: { tlist, totalRecord: total, currentPage: 1, pageSize: 10 } }
}

function mockGenericStatistics() {
  return { result: 200, msg: '成功', data: { totalCount: randomNum(20, 100), completedCount: randomNum(10, 80), pendingCount: randomNum(5, 30), riskCount: randomNum(1, 15), score: randomDecimal(70, 95) } }
}

function mockGenericDetail(id) {
  return { result: 200, msg: '成功', data: { id: id || 'DETAIL001', name: '详情记录', companyName: randomItem(companyNames), status: 'APPROVED', riskLevel: 'LOW', amount: randomNum(1000, 100000), description: '详细描述信息', createUser: '管理员', createTime: randomDate(d1, d2) } }
}

function mockGenericTree() {
  return {
    result: 200, msg: '成功', data: [
      { id: '1', label: '示例集团总部', children: [
        { id: '1-1', label: '示例科技有限公司', children: [{ id: '1-1-1', label: '示例智能装备' }, { id: '1-1-2', label: '示例信息技术' }] },
        { id: '1-2', label: '示例金融控股', children: [{ id: '1-2-1', label: '示例国际贸易' }] },
        { id: '1-3', label: '示例新能源股份', children: [{ id: '1-3-1', label: '示例地产开发' }, { id: '1-3-2', label: '示例医药科技' }] },
      ] }
    ]
  }
}

function mockGenericChart() {
  return {
    result: 200, msg: '成功', data: {
      categories: ['2025-Q1', '2025-Q2', '2025-Q3', '2025-Q4', '2026-Q1'],
      series: [
        { name: '指标A', data: [randomNum(100, 500), randomNum(100, 500), randomNum(100, 500), randomNum(100, 500), randomNum(100, 500)] },
        { name: '指标B', data: [randomNum(50, 300), randomNum(50, 300), randomNum(50, 300), randomNum(50, 300), randomNum(50, 300)] },
      ]
    }
  }
}

// ==================== 采购风险穿透下钻 Mock 数据 ====================
function mockProcurementDrillDownTree() {
  return {
    result: 200, msg: '成功', data: [{
      id: 'HB-GROUP', nodeName: '示例集团总部', riskLevel: 'MEDIUM', nodeLevel: 0,
      children: [
        {
          id: 'HB-TECH', nodeName: '示例科技有限公司', riskLevel: 'HIGH', nodeLevel: 1,
          children: [
            {
              id: 'HB-TECH-AI', nodeName: '示例智能装备', riskLevel: 'HIGH', nodeLevel: 2,
              children: [
                { id: 'HB-TECH-AI-01', nodeName: '智能装备研发中心', riskLevel: 'MEDIUM', nodeLevel: 3, children: [] },
                { id: 'HB-TECH-AI-02', nodeName: '精密制造事业部', riskLevel: 'HIGH', nodeLevel: 3, children: [] },
              ]
            },
            {
              id: 'HB-TECH-IT', nodeName: '示例信息技术', riskLevel: 'LOW', nodeLevel: 2,
              children: [
                { id: 'HB-TECH-IT-01', nodeName: '云计算服务部', riskLevel: 'LOW', nodeLevel: 3, children: [] },
              ]
            },
          ]
        },
        {
          id: 'HB-FIN', nodeName: '示例金融控股', riskLevel: 'MEDIUM', nodeLevel: 1,
          children: [
            {
              id: 'HB-FIN-TRADE', nodeName: '示例国际贸易', riskLevel: 'HIGH', nodeLevel: 2,
              children: [
                { id: 'HB-FIN-TRADE-01', nodeName: '进出口业务部', riskLevel: 'HIGH', nodeLevel: 3, children: [] },
                { id: 'HB-FIN-TRADE-02', nodeName: '供应链金融部', riskLevel: 'MEDIUM', nodeLevel: 3, children: [] },
              ]
            },
            {
              id: 'HB-FIN-INV', nodeName: '示例投资管理', riskLevel: 'LOW', nodeLevel: 2,
              children: [
                { id: 'HB-FIN-INV-01', nodeName: '股权投资部', riskLevel: 'LOW', nodeLevel: 3, children: [] },
              ]
            },
          ]
        },
        {
          id: 'HB-ENERGY', nodeName: '示例新能源股份', riskLevel: 'LOW', nodeLevel: 1,
          children: [
            {
              id: 'HB-ENERGY-DEV', nodeName: '示例地产开发', riskLevel: 'MEDIUM', nodeLevel: 2,
              children: [
                { id: 'HB-ENERGY-DEV-01', nodeName: '商业地产事业部', riskLevel: 'MEDIUM', nodeLevel: 3, children: [] },
              ]
            },
            {
              id: 'HB-ENERGY-MED', nodeName: '示例医药科技', riskLevel: 'LOW', nodeLevel: 2,
              children: [
                { id: 'HB-ENERGY-MED-01', nodeName: '生物制药研发部', riskLevel: 'LOW', nodeLevel: 3, children: [] },
                { id: 'HB-ENERGY-MED-02', nodeName: '医疗器械事业部', riskLevel: 'MEDIUM', nodeLevel: 3, children: [] },
              ]
            },
          ]
        },
      ]
    }]
  }
}

function mockProcurementDrillDownDetail() {
  const methods = ['公开招标', '竞争性谈判', '单一来源', '询价采购']
  const types = ['工程', '货物', '服务', 'IT']
  const statuses = ['合规', '合规', '合规', '违规', '预警', '逾期']
  const projects = ['办公设备采购', 'IT系统升级', '原材料采购', '工程施工', '咨询服务', '设备维保', '软件开发', '物流运输', '安防工程']
  return {
    result: 200, msg: '成功', data: {
      node: {
        nodeName: '示例科技有限公司',
        riskLevel: 'HIGH',
        purchaseTotal: '12.6',
        relatedRatio: 35,
        warnCount: 3,
      },
      purchaseRecords: [
        { purchaseNo: 'PO-2025-001', purchaseType: randomItem(types), contractAmount: randomNum(200, 5000), biddingMethod: randomItem(methods), complianceStatus: randomItem(statuses) },
        { purchaseNo: 'PO-2025-002', purchaseType: randomItem(types), contractAmount: randomNum(200, 5000), biddingMethod: randomItem(methods), complianceStatus: randomItem(statuses) },
        { purchaseNo: 'PO-2025-003', purchaseType: randomItem(types), contractAmount: randomNum(200, 5000), biddingMethod: randomItem(methods), complianceStatus: randomItem(statuses) },
      ],
      warnings: [
        { id: 'WARN-001', level: 'HIGH', time: '2025-01-15 09:30', title: '关联交易占比超限', desc: '该企业关联交易占比达35%，超过集团规定的30%上限，需立即核查并整改' },
        { id: 'WARN-002', level: 'MEDIUM', time: '2025-01-12 14:20', title: '单一来源采购频次异常', desc: '近3个月单一来源采购占比达42%，远超行业平均水平，存在围标串标风险' },
        { id: 'WARN-003', level: 'HIGH', time: '2025-01-10 11:05', title: '供应商集中度过高', desc: 'TOP3供应商采购额占比达68%，供应链风险集中，建议引入更多合格供应商' },
      ],
    }
  }
}

// ==================== URL 匹配导出函数 ====================
export function getStateAssetsMockData(url, config) {
  // 投资穿透
  if (url.includes('/supervision/investment/project/list') || url.includes('/supervision/investment/projects/list')) return mockInvestProjectList()
  if (url.includes('/supervision/investment/') && url.includes('/statistics')) return mockInvestStatistics()
  if (url.includes('/supervision/investment/project/') && !url.includes('list') && !url.includes('statistics')) return mockInvestDetail()
  // 产权穿透
  if (url.includes('/supervision/property/right/transaction/list')) return mockPropertyTransactionList()
  if (url.includes('/supervision/property/right/list')) return mockPropertyRightList()
  if (url.includes('/supervision/property/') && url.includes('/statistics')) return mockPropertyStatistics()
  // 财务穿透
  if (url.includes('/supervision/financial/report/related/list')) return mockRelatedTransactionList()
  if (url.includes('/supervision/financial/report/list')) return mockFinanceStatementList()
  if (url.includes('/supervision/financial/related-transaction/list')) return mockRelatedTransactionList()
  if (url.includes('/supervision/financial/consolidated/list')) return mockFinanceStatementList()
  if (url.includes('/supervision/financial/fund-flow/list')) return mockGenericList('FF')
  if (url.includes('/supervision/financial/performance-consolidation/list')) return mockGenericList('PC')
  if (url.includes('/supervision/financial/') && url.includes('/statistics')) return mockFinanceStatistics()
  // 金融风险穿透
  if (url.includes('/supervision/financial-risk/financing/guarantee/list')) return mockGuaranteeRecordList()
  if (url.includes('/supervision/financial-risk/financing/list')) return mockFinancingRecordList()
  if (url.includes('/supervision/financial-risk/') && url.includes('/statistics')) return mockFinancialRiskStatistics()
  // 会计穿透
  if (url.includes('/supervision/accounting/policy/list')) return mockAccountingPolicyList()
  if (url.includes('/supervision/accounting/') && url.includes('/statistics')) return mockAccountingStatistics()
  // 薪酬穿透
  if (url.includes('/supervision/salary/total/list')) return mockSalaryTotalList()
  if (url.includes('/supervision/salary/') && url.includes('/statistics')) return mockSalaryStatistics()
  // 军品穿透
  if (url.includes('/supervision/military/task/list')) return mockMilitaryTaskList()
  if (url.includes('/supervision/military/') && url.includes('/statistics')) return mockMilitaryStatistics()
  // 采购穿透
  if (url.includes('/supervision/procurement/drill-down/tree')) return mockProcurementDrillDownTree()
  if (url.includes('/supervision/procurement/drill-down/detail')) return mockProcurementDrillDownDetail()
  if (url.includes('/supervision/procurement/project/supplier/list')) return mockSupplierList()
  if (url.includes('/supervision/procurement/project/list')) return mockProcurementProjectList()
  if (url.includes('/supervision/procurement/') && url.includes('/statistics')) return mockProcurementStatistics()
  // 境外穿透
  if (url.includes('/supervision/overseas/unit/list')) return mockOverseasUnitList()
  if (url.includes('/supervision/overseas/') && url.includes('/statistics')) return mockOverseasStatistics()
  // 合同穿透
  if (url.includes('/supervision/contract/record/dispute/list')) return mockContractDisputeList()
  if (url.includes('/supervision/contract/record/list')) return mockContractRecordList()
  if (url.includes('/supervision/contract/') && url.includes('/statistics')) return mockContractStatistics()
  // 协同核查
  if (url.includes('/supervision/investigation/task/rectification/list')) return mockRectificationList()
  if (url.includes('/supervision/investigation/task/list')) return mockInvestigationTaskList()
  if (url.includes('/supervision/investigation/') && url.includes('/statistics')) return mockInvestigationStatistics()
  // 股权穿透
  if (url.includes('/supervision/equity/structure/list') || url.includes('/supervision/equity/shareholder/list') || url.includes('/supervision/equity/control-chain/list') || url.includes('/supervision/equity/changes/list')) return mockEquityStructureList()
  if (url.includes('/supervision/equity/') && url.includes('/statistics')) return mockEquityStatistics()
  if (url.includes('/supervision/equity/') && url.includes('/chart')) return mockGenericChart()
  if (url.includes('/supervision/equity/') && url.includes('/tree')) return mockGenericTree()
  // 控制链
  if (url.includes('/supervision/control-chain/list')) return mockEquityStructureList()
  if (url.includes('/supervision/control-chain/') && url.includes('/statistics')) return mockEquityStatistics()
  // 财务分析
  if (url.includes('/supervision/financial/analysis/list')) return mockFinancialAnalysisList()
  if (url.includes('/supervision/financial/risk/list')) return mockRiskAssessmentList()
  // 资产穿透
  if (url.includes('/supervision/asset/') && url.includes('/list')) return mockAssetAllocationList()
  if (url.includes('/supervision/asset/') && url.includes('/statistics')) return mockGenericStatistics()
  // 风险穿透
  if (url.includes('/supervision/risk/assessment/list')) return mockRiskAssessmentList()
  if (url.includes('/supervision/risk/control-measure/list')) return mockGenericList('RCM')
  if (url.includes('/supervision/risk/incident/list')) return mockGenericList('RI')
  if (url.includes('/supervision/risk/monitoring/list')) return mockGenericList('RM')
  if (url.includes('/supervision/risk/') && url.includes('/statistics')) return mockRiskStatistics()
  // 综合驾驶舱
  if (url.includes('/supervision/dashboard/overview') || url.includes('/state-assets/dashboard/')) return mockDashboardOverview()
  // 企业管理
  if (url.includes('/enterprise/info/list') || url.includes('/enterprise/hierarchy/list')) return mockGenericList('ENT', 10, 156)
  if (url.includes('/enterprise/') && url.includes('/statistics')) return mockGenericStatistics()
  if (url.includes('/enterprise/hierarchy/chart')) return mockGenericTree()
  // 股权穿透（zbgl 路径）
  if (url.includes('/equity-penetration/beneficial-owner/getList')) return mockEquityStructureList()
  if (url.includes('/equity-penetration/') && url.includes('/statistics')) return mockEquityStatistics()
  // 监管配置
  if (url.includes('/supervision-config/') && url.includes('/list')) return mockGenericList('CFG')
  if (url.includes('/supervision-config/') && url.includes('/statistics')) return mockGenericStatistics()
  // 监管报告
  if (url.includes('/supervision-report/list') || url.includes('/supervision-report/templates/list')) return mockGenericList('RPT')
  if (url.includes('/supervision-report/') && url.includes('/statistics')) return mockGenericStatistics()
  // 党建管理
  if (url.includes('/party-building/') && url.includes('/list')) return mockGenericList('PB')
  if (url.includes('/party-building/') && url.includes('/statistics')) return mockGenericStatistics()
  if (url.includes('/party-building/organizations/tree')) return mockGenericTree()
  // 智能AI
  if (url.includes('/intelligent-ai/') && url.includes('/list')) return mockGenericList('AI')
  if (url.includes('/intelligent-ai/') && url.includes('/statistics')) return mockGenericStatistics()
  // 合规管理
  if (url.includes('/financial/compliance/list')) return mockGenericList('CMP')
  if (url.includes('/financial/compliance/') && url.includes('/statistics')) return mockGenericStatistics()
  if (url.includes('/financial/performance/list')) return mockGenericList('PERF')
  if (url.includes('/financial/performance/') && url.includes('/statistics')) return mockGenericStatistics()

  // 通用兜底：包含 /list 的返回列表，包含 /statistics 的返回统计，包含 /chart 的返回图表，包含 /tree 的返回树
  if (url.includes('/list')) return mockGenericList('GEN')
  if (url.includes('/statistics') || url.includes('/stats')) return mockGenericStatistics()
  if (url.includes('/chart') || url.includes('/charts')) return mockGenericChart()
  if (url.includes('/tree') || url.includes('/hierarchy')) return mockGenericTree()
  if (url.includes('/detail') || url.includes('/getById')) return mockGenericDetail()
  // 最终兜底
  return mockGenericDetail()
}