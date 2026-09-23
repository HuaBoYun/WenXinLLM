/**
 * 全球司库模块模拟数据生成工具
 * 用于在后端API异常时提供假数据，确保页面正常访问
 */

// 通用工具函数
const generateId = () => Math.floor(Math.random() * 10000) + 1
const generateCode = (prefix = 'CODE') => `${prefix}${String(Math.floor(Math.random() * 9999) + 1).padStart(4, '0')}`
const generateAmount = (min = 1000, max = 1000000) => Math.floor(Math.random() * (max - min) + min)
const generateDate = (daysAgo = 30) => {
  const date = new Date()
  date.setDate(date.getDate() - Math.floor(Math.random() * daysAgo))
  return date.toISOString().split('T')[0]
}
const generateDateTime = (daysAgo = 30) => {
  const date = new Date()
  date.setDate(date.getDate() - Math.floor(Math.random() * daysAgo))
  date.setHours(Math.floor(Math.random() * 24))
  date.setMinutes(Math.floor(Math.random() * 60))
  return date.toISOString().replace('T', ' ').split('.')[0]
}

// 随机选择数组元素
const randomChoice = (arr) => arr[Math.floor(Math.random() * arr.length)]

// 电票账户配置模拟数据
export const generateETicketAccountData = (count = 2) => {
  const systems = ['ECDS', 'BECP', 'BANK_ETICKET']
  const accountTypes = ['ACCEPTANCE', 'DISCOUNT', 'REDISCOUNT', 'PLEDGE', 'CUSTODY', 'MARGIN']
  const banks = ['ICBC', 'CCB', 'ABC', 'BOC', 'CMB', 'SPDB']
  const statuses = ['NORMAL', 'FROZEN', 'CLOSED']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    accountNumber: generateCode('EA'),
    accountName: `电票账户${index + 1}`,
    eTicketSystem: randomChoice(systems),
    accountType: randomChoice(accountTypes),
    bankCode: randomChoice(banks),
    bankAccountNumber: `6222${String(Math.floor(Math.random() * 100000000000000)).padStart(14, '0')}`,
    accountBalance: generateAmount(10000, 5000000),
    availableBalance: generateAmount(5000, 3000000),
    openDate: generateDate(365),
    accountStatus: randomChoice(statuses),
    isEnabled: Math.random() > 0.3 ? 1 : 0,
    createTime: generateDateTime(30),
    updateTime: generateDateTime(7)
  }))
}

// 印鉴组合配置模拟数据
export const generateSealCombinationData = (count = 2) => {
  const combinationTypes = ['SINGLE_SEAL', 'DUAL_SEAL', 'MULTI_SEAL']
  const businessTypes = ['FUND_TRANSFER', 'INVESTMENT_TRADE', 'BILL_BUSINESS', 'CONTRACT_SIGN', 'AUTHORIZATION']
  const authorityLevels = ['LOW', 'MEDIUM', 'HIGH', 'SUPER']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    combinationCode: generateCode('SC'),
    combinationName: `印鉴组合${index + 1}`,
    combinationType: randomChoice(combinationTypes),
    sealCount: Math.floor(Math.random() * 5) + 1,
    businessType: randomChoice(businessTypes),
    authorityLevel: randomChoice(authorityLevels),
    maxAmountLimit: generateAmount(100000, 10000000),
    usageCount: Math.floor(Math.random() * 100),
    isEnabled: Math.random() > 0.2 ? 1 : 0,
    createTime: generateDateTime(60),
    updateTime: generateDateTime(10)
  }))
}

// 印鉴使用记录模拟数据
export const generateSealUsageRecordData = (count = 2) => {
  const businessTypes = ['FUND_TRANSFER', 'INVESTMENT_TRADE', 'BILL_BUSINESS', 'CONTRACT_SIGN']
  const usageStatuses = ['SUCCESS', 'FAILED', 'PENDING', 'CANCELLED']
  const operators = ['张三', '李四', '王五', '赵六', '钱七']
  const approvers = ['审核员A', '审核员B', '审核员C']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    recordNumber: generateCode('SR'),
    sealCode: generateCode('SEAL'),
    sealName: `公章${index + 1}`,
    operatorName: randomChoice(operators),
    businessType: randomChoice(businessTypes),
    businessNumber: generateCode('BIZ'),
    usageAmount: generateAmount(1000, 1000000),
    usageTime: generateDateTime(30),
    usageStatus: randomChoice(usageStatuses),
    approverName: randomChoice(approvers),
    remark: `印鉴使用记录${index + 1}的备注信息`,
    createTime: generateDateTime(30)
  }))
}

// 第三方账户管理模拟数据
export const generateThirdPartyAccountData = (count = 2) => {
  const systems = ['BANK_DIRECT', 'ALIPAY', 'WECHAT_PAY', 'ONLINE_BANKING', 'THIRD_PARTY_PAY', 'ERP_SYSTEM']
  const accountTypes = ['BANK_ACCOUNT', 'PAYMENT_ACCOUNT', 'API_ACCOUNT', 'SYSTEM_ACCOUNT', 'VIRTUAL_ACCOUNT']
  const connectionStatuses = ['CONNECTED', 'DISCONNECTED', 'ERROR', 'MAINTENANCE']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    accountCode: generateCode('TPA'),
    accountName: `第三方账户${index + 1}`,
    thirdPartySystem: randomChoice(systems),
    accountType: randomChoice(accountTypes),
    accountIdentifier: `${randomChoice(['ACC', 'API', 'SYS'])}${String(Math.floor(Math.random() * 1000000)).padStart(6, '0')}`,
    apiKey: Math.random() > 0.3 ? 'sk_' + Math.random().toString(36).substring(2, 15) : null,
    connectionStatus: randomChoice(connectionStatuses),
    lastSyncTime: generateDateTime(7),
    isEnabled: Math.random() > 0.2 ? 1 : 0,
    createTime: generateDateTime(60),
    updateTime: generateDateTime(10)
  }))
}

// 交易类型管理模拟数据
export const generateTransactionTypeData = (count = 2) => {
  const categories = ['INVESTMENT', 'FINANCING', 'PAYMENT', 'SETTLEMENT', 'EXCHANGE']
  const directions = ['IN', 'OUT', 'BOTH']
  const riskLevels = ['LOW', 'MEDIUM', 'HIGH']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    transactionTypeCode: generateCode('TT'),
    transactionTypeName: `交易类型${index + 1}`,
    transactionCategory: randomChoice(categories),
    transactionDirection: randomChoice(directions),
    riskLevel: randomChoice(riskLevels),
    accountingSubject: `${Math.floor(Math.random() * 9000) + 1000}`,
    requiresApproval: Math.random() > 0.5,
    limitAmount: generateAmount(10000, 5000000),
    isEnabled: Math.random() > 0.2 ? 1 : 0,
    createTime: generateDateTime(90),
    updateTime: generateDateTime(15)
  }))
}

// 银企直连管理模拟数据
export const generateBankDirectConnectionData = (count = 2) => {
  const interfaceTypes = ['REST_API', 'SOAP', 'FTP', 'SFTP', 'MQ']
  const authTypes = ['API_KEY', 'OAUTH2', 'CERTIFICATE', 'USERNAME_PASSWORD']
  const banks = ['ICBC', 'CCB', 'ABC', 'BOC', 'CMB']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    configName: `银企直连配置${index + 1}`,
    bankCode: randomChoice(banks),
    interfaceType: randomChoice(interfaceTypes),
    endpointUrl: `https://api.bank${index + 1}.com/v1/treasury`,
    authType: randomChoice(authTypes),
    timeoutSeconds: Math.floor(Math.random() * 60) + 30,
    retryTimes: Math.floor(Math.random() * 5) + 1,
    isEnabled: Math.random() > 0.2 ? 1 : 0,
    createTime: generateDateTime(120),
    updateTime: generateDateTime(20)
  }))
}

// 产品利息规则管理模拟数据
export const generateProductInterestRuleData = (count = 2) => {
  const productTypes = ['DEPOSIT', 'LOAN', 'INVESTMENT', 'BOND', 'FUND']
  const calculationMethods = ['SIMPLE', 'COMPOUND', 'DAILY', 'MONTHLY']
  const settlementPeriods = ['DAILY', 'MONTHLY', 'QUARTERLY', 'ANNUALLY']
  
  return Array.from({ length: count }, (_, index) => ({
    id: generateId(),
    ruleCode: generateCode('PIR'),
    ruleName: `利息规则${index + 1}`,
    productType: randomChoice(productTypes),
    baseInterestRate: (Math.random() * 10 + 1).toFixed(2),
    interestRateFloat: (Math.random() * 2 - 1).toFixed(2),
    interestCalculationMethod: randomChoice(calculationMethods),
    interestSettlementPeriod: randomChoice(settlementPeriods),
    effectiveDate: generateDate(30),
    expiryDate: generateDate(-30),
    isEnabled: Math.random() > 0.2 ? 1 : 0,
    createTime: generateDateTime(90),
    updateTime: generateDateTime(15)
  }))
}

// 通用API错误处理函数
export const handleApiError = (error, mockDataGenerator, count = 2) => {
  console.warn('API调用失败，使用模拟数据:', error)
  return {
    code: 200,
    data: {
      tlist: mockDataGenerator(count),
      totalRecord: count
    },
    message: '数据加载成功（模拟数据）'
  }
}

// 通用分页响应格式
export const createMockResponse = (data, total = null) => {
  return {
    code: 200,
    data: {
      tlist: data,
      totalRecord: total || data.length
    },
    message: '数据加载成功'
  }
}

// 衍生品监控仪表盘数据
export const generateDerivativesMonitoringDashboard = () => {
  return {
    totalPositionValue: Math.floor(Math.random() * 500000000) + 100000000, // 1亿-6亿
    todayPnL: (Math.random() - 0.5) * 10000000, // -500万到500万
    var: Math.floor(Math.random() * 20000000) + 5000000, // 500万-2500万
    alertCount: Math.floor(Math.random() * 15) + 1 // 1-15个预警
  }
}

// 衍生品持仓监控数据
export const generateDerivativesPositionData = (count = 8) => {
  const productTypes = ['FORWARD', 'OPTION', 'FUTURES', 'SWAP']
  const underlyingAssets = ['USD/CNY', 'EUR/CNY', 'JPY/CNY', 'GBP/CNY', 'AUD/CNY', 'CAD/CNY']
  const riskLevels = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL']
  const counterparties = ['中国工商银行', '中国建设银行', '中国银行', '招商银行', '中信银行', '平安银行']

  return Array.from({ length: count }, (_, index) => ({
    contractId: index + 1,
    contractCode: `DC${String(Date.now() + index).slice(-8)}`,
    contractName: `衍生品合约${index + 1}`,
    productType: randomChoice(productTypes),
    underlyingAsset: randomChoice(underlyingAssets),
    counterparty: randomChoice(counterparties),
    positionSize: Math.floor(Math.random() * 10000000) + 1000000,
    marketValue: Math.floor(Math.random() * 50000000) + 10000000,
    unrealizedPnL: (Math.random() - 0.5) * 5000000,
    delta: (Math.random() - 0.5) * 2,
    gamma: Math.random() * 0.1,
    theta: -Math.random() * 100,
    vega: Math.random() * 1000,
    rho: Math.random() * 500,
    maturityDate: generateDate(-Math.floor(Math.random() * 365 + 30)),
    riskLevel: randomChoice(riskLevels),
    notionalAmount: Math.floor(Math.random() * 100000000) + 10000000,
    contractStatus: 'ACTIVE',
    createTime: generateDateTime(30)
  }))
}

// 衍生品风险监控数据
export const generateDerivativesRiskData = () => {
  return {
    var95: Math.floor(Math.random() * 20000000) + 5000000,
    varChange: (Math.random() - 0.5) * 0.2,
    expectedShortfall: Math.floor(Math.random() * 30000000) + 8000000,
    esChange: (Math.random() - 0.5) * 0.15,
    maxDrawdown: Math.floor(Math.random() * 15000000) + 3000000,
    drawdownChange: (Math.random() - 0.5) * 0.1
  }
}

// 衍生品市场数据
export const generateDerivativesMarketData = (count = 12) => {
  const assetTypes = ['CURRENCY', 'INTEREST_RATE', 'COMMODITY', 'EQUITY', 'BOND']
  const assetCodes = ['USDCNY', 'EURCNY', 'JPYCNY', 'GBPCNY', 'AUDCNY', 'CADCNY', 'SHIBOR3M', 'LPR1Y', 'GOLD', 'OIL', 'CSI300', 'SSE50']
  const assetNames = ['美元兑人民币', '欧元兑人民币', '日元兑人民币', '英镑兑人民币', '澳元兑人民币', '加元兑人民币', '3个月SHIBOR', '1年期LPR', '黄金', '原油', '沪深300', '上证50']
  const dataSources = ['WIND', 'BLOOMBERG', 'REUTERS', 'EXCHANGE']

  return Array.from({ length: count }, (_, index) => ({
    assetId: index + 1,
    assetType: randomChoice(assetTypes),
    assetCode: assetCodes[index] || `ASSET${index + 1}`,
    assetName: assetNames[index] || `资产${index + 1}`,
    currentPrice: (Math.random() * 100 + 1).toFixed(4),
    priceChange: (Math.random() - 0.5) * 0.1,
    volatility: Math.random() * 0.3 + 0.05,
    volume: Math.floor(Math.random() * 1000000000) + 100000000,
    updateTime: generateDateTime(1),
    dataSource: randomChoice(dataSources)
  }))
}

// 衍生品预警数据
export const generateDerivativesAlertData = (count = 10) => {
  const alertTypes = ['PRICE_ALERT', 'RISK_ALERT', 'POSITION_ALERT', 'EXPIRY_ALERT']
  const alertLevels = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL']
  const alertStatuses = ['ACTIVE', 'TRIGGERED', 'PROCESSED', 'IGNORED']
  const monitoringMetrics = ['价格变动', 'VaR超限', '持仓集中度', '到期提醒', 'Delta中性', 'Gamma风险']

  return Array.from({ length: count }, (_, index) => ({
    alertId: index + 1,
    alertName: `预警规则${index + 1}`,
    alertType: randomChoice(alertTypes),
    monitoringMetric: randomChoice(monitoringMetrics),
    threshold: Math.floor(Math.random() * 1000000) + 100000,
    currentValue: Math.floor(Math.random() * 1200000) + 50000,
    alertLevel: randomChoice(alertLevels),
    status: randomChoice(alertStatuses),
    triggerTime: generateDateTime(1),
    description: `这是预警规则${index + 1}的详细描述`,
    notificationMethod: ['EMAIL', 'SMS', 'SYSTEM'],
    createTime: generateDateTime(30)
  }))
}
