/**
 * 企业画像相关接口Mock数据
 * @author AI Agent
 * @since 2025-01-21
 */

// 企业全息画像数据Mock
const hologramDataMock = {
  enterpriseTags: [
    {
      tagId: 'TAG001',
      tagCode: 'HIGH_TECH',
      tagName: '高新技术企业',
      tagType: 'ADVANTAGE',
      tagCategory: 'QUALIFICATION',
    },
    {
      tagId: 'TAG002',
      tagCode: 'LISTED_COMPANY',
      tagName: '上市公司',
      tagType: 'ADVANTAGE',
      tagCategory: 'QUALIFICATION',
    },
    {
      tagId: 'TAG003',
      tagCode: 'FINANCIAL_STABLE',
      tagName: '财务稳健',
      tagType: 'ADVANTAGE',
      tagCategory: 'FINANCIAL',
    },
    {
      tagId: 'TAG004',
      tagCode: 'INDUSTRY_LEADER',
      tagName: '行业领先',
      tagType: 'ADVANTAGE',
      tagCategory: 'BUSINESS',
    },
    {
      tagId: 'TAG005',
      tagCode: 'INNOVATION_DRIVEN',
      tagName: '创新驱动',
      tagType: 'ADVANTAGE',
      tagCategory: 'BUSINESS',
    },
    {
      tagId: 'TAG010',
      tagCode: 'DIGITAL_TRANSFORMATION',
      tagName: '数字化转型',
      tagType: 'ADVANTAGE',
      tagCategory: 'BUSINESS',
    },
  ],
  keyIndicators: [
    {
      code: 'TOTAL_REVENUE',
      name: '营业收入',
      value: '15.6亿',
      unit: '元',
      type: 'FINANCIAL',
    },
    {
      code: 'NET_PROFIT',
      name: '净利润',
      value: '2.3亿',
      unit: '元',
      type: 'FINANCIAL',
    },
    {
      code: 'TOTAL_ASSETS',
      name: '总资产',
      value: '45.2亿',
      unit: '元',
      type: 'FINANCIAL',
    },
    {
      code: 'EMPLOYEE_COUNT',
      name: '员工总数',
      value: '1250人',
      unit: '人',
      type: 'HR',
    },
    {
      code: 'ROE',
      name: '净资产收益率',
      value: '15.6%',
      unit: '%',
      type: 'FINANCIAL',
    },
    {
      code: 'DEBT_RATIO',
      name: '资产负债率',
      value: '45.2%',
      unit: '%',
      type: 'FINANCIAL',
    },
    {
      code: 'REVENUE_GROWTH',
      name: '营收增长率',
      value: '12.5%',
      unit: '%',
      type: 'FINANCIAL',
    },
    {
      code: 'RD_INVESTMENT',
      name: '研发投入',
      value: '1.56亿',
      unit: '元',
      type: 'OPERATIONAL',
    },
    {
      code: 'MARKET_SHARE',
      name: '市场份额',
      value: '8.5%',
      unit: '%',
      type: 'OPERATIONAL',
    },
    {
      code: 'CUSTOMER_COUNT',
      name: '客户数量',
      value: '2580家',
      unit: '家',
      type: 'OPERATIONAL',
    },
  ],
  riskLevel: '1',
  riskScore: '25.50',
  riskFactors:
    '整体风险较低，财务状况良好，市场地位稳固。主要关注点：行业竞争加剧、技术更新换代风险',
}

// 企业详细信息数据Mock
const detailInfoMock = {
  enterpriseId: 'ENT067',
  enterpriseName: '示例云科技有限公司',
  enterpriseCode: 'CODE067',
  unifiedSocialCreditCode: '91110108MA01234567',
  legalRepresentative: '张三',
  registeredCapital: 5000.0,
  establishmentDate: '2018-03-15',
  status: 'ACTIVE',
  statusName: '正常',
  businessScope:
    '技术开发、技术推广、技术转让、技术咨询、技术服务；软件开发；计算机系统服务；基础软件服务；应用软件服务',
  industryType: 'TECHNOLOGY',
  industryTypeName: '科技行业',
  enterpriseScale: 'LARGE',
  enterpriseScaleName: '大型企业',
  registeredAddress: '北京市海淀区中关村科技园区创新大厦A座15层',
  contactPhone: '010-82345678',
  email: 'info@huaboyun.com',
  website: 'https://www.huaboyun.com',
  totalAssets: 4520000000.0,
  totalAssetsText: '45.2亿元',
  totalRevenue: 1560000000.0,
  totalRevenueText: '15.6亿元',
  listingStatus: 'LISTED',
  listingExchange: '深圳证券交易所',
  stockCode: '002888',
}

// 企业基本信息数据Mock
const enterpriseInfoMock = {
  enterpriseInfo: {
    enterpriseId: 'ENT067',
    enterpriseCode: 'CODE067',
    enterpriseName: '示例云科技有限公司',
    legalRepresentative: '张三',
    registeredCapital: 5000.0,
    establishmentDate: 1521043200000, // 2018-03-15的时间戳
    status: 'ACTIVE',
    statusName: '正常',
    industryType: 'TECHNOLOGY',
    industryTypeName: '科技行业',
    enterpriseScale: 'LARGE',
    enterpriseScaleName: '大型企业',
  },
  keyMetrics: {
    totalAssets: 4520000000.0,
    annualRevenue: 1560000000.0,
    netProfit: 230000000.0,
    employeeCount: 1250,
  },
  systemStatus: {
    lastUpdateTime: '2025-01-21 15:30:00',
    refreshInterval: 300,
  },
}

module.exports = [
  // 获取企业基本信息
  {
    url: '/vab-mock-server/riskcontrol/enterpriseProfile/dashboard/enterpriseInfo',
    type: 'post',
    response: () => {
      return {
        code: 1,
        msg: '操作成功',
        data: enterpriseInfoMock,
        result: '查询成功',
      }
    },
  },

  // 获取企业全息画像数据
  {
    url: '/vab-mock-server/riskcontrol/enterpriseProfile/dashboard/hologramData',
    type: 'post',
    response: () => {
      return {
        code: 1,
        msg: '查询成功',
        data: hologramDataMock,
        result: '查询成功',
      }
    },
  },

  // 获取企业详细信息
  {
    url: '/vab-mock-server/riskcontrol/enterpriseProfile/dashboard/detailInfo',
    type: 'post',
    response: () => {
      return {
        code: 1,
        msg: '查询成功',
        data: detailInfoMock,
        result: '查询成功',
      }
    },
  },
]
