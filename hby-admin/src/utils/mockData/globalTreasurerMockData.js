/**
 * 全球司库系统模拟数据生成器
 */

// 通用模拟数据生成函数
export const mockDataGenerators = {
  // 付款管理模拟数据
  paymentManagement: () => ({
    records: [
      {
        paymentId: 'FK202501240001',
        paymentNumber: 'FK202501240001',
        payeeName: '北京科技有限公司',
        payeeAccount: '6225880123456789',
        payeeBank: '中国工商银行北京分行',
        amount: 150000.00,
        currency: 'CNY',
        paymentDate: '2025-01-24',
        paymentStatus: 'PENDING',
        paymentMethod: 'BANK_TRANSFER',
        purpose: '采购设备款',
        accountId: 'ACC001',
        accountName: '示例云基本户',
        createTime: '2025-01-24 09:30:00',
        createUser: '张三',
        updateTime: '2025-01-24 09:30:00'
      },
      {
        paymentId: 'FK202501240002',
        paymentNumber: 'FK202501240002',
        payeeName: '上海贸易股份有限公司',
        payeeAccount: '6225880987654321',
        payeeBank: '中国建设银行上海分行',
        amount: 280000.00,
        currency: 'CNY',
        paymentDate: '2025-01-23',
        paymentStatus: 'COMPLETED',
        paymentMethod: 'BANK_TRANSFER',
        purpose: '服务费支付',
        accountId: 'ACC002',
        accountName: '示例云专用户',
        createTime: '2025-01-23 14:20:00',
        createUser: '李四',
        updateTime: '2025-01-23 16:45:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 收款管理模拟数据
  receiptManagement: () => ({
    records: [
      {
        receiptId: 'SK202501240001',
        receiptNumber: 'SK202501240001',
        payerName: '深圳创新科技公司',
        payerAccount: '6225881111111111',
        payerBank: '招商银行深圳分行',
        amount: 320000.00,
        currency: 'CNY',
        receiptDate: '2025-01-24',
        receiptStatus: 'COMPLETED',
        receiptMethod: 'BANK_TRANSFER',
        purpose: '项目款收入',
        accountId: 'ACC001',
        accountName: '示例云基本户',
        createTime: '2025-01-24 10:15:00',
        createUser: '王五',
        updateTime: '2025-01-24 10:15:00'
      },
      {
        receiptId: 'SK202501240002',
        receiptNumber: 'SK202501240002',
        payerName: '广州智能制造有限公司',
        payerAccount: '6225882222222222',
        payerBank: '中国银行广州分行',
        amount: 450000.00,
        currency: 'CNY',
        receiptDate: '2025-01-23',
        receiptStatus: 'PENDING',
        receiptMethod: 'BANK_TRANSFER',
        purpose: '产品销售收入',
        accountId: 'ACC003',
        accountName: '示例云收入户',
        createTime: '2025-01-23 15:30:00',
        createUser: '赵六',
        updateTime: '2025-01-23 15:30:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 账户管理模拟数据
  accountManagement: () => ({
    records: [
      {
        accountId: 'ACC001',
        accountNumber: '1234567890123456',
        accountName: '示例云基本户',
        bankCode: 'ICBC',
        bankName: '中国工商银行北京分行',
        currencyCode: 'CNY',
        accountType: 'BASIC',
        accountStatus: 'ACTIVE',
        balance: 5280000.00,
        availableBalance: 5180000.00,
        frozenBalance: 100000.00,
        openDate: '2024-01-15',
        isDefault: true,
        createTime: '2024-01-15 09:00:00',
        updateTime: '2025-01-24 08:30:00'
      },
      {
        accountId: 'ACC002',
        accountNumber: '2345678901234567',
        accountName: '示例云专用户',
        bankCode: 'CCB',
        bankName: '中国建设银行北京分行',
        currencyCode: 'CNY',
        accountType: 'SPECIAL',
        accountStatus: 'ACTIVE',
        balance: 3150000.00,
        availableBalance: 3150000.00,
        frozenBalance: 0.00,
        openDate: '2024-03-20',
        isDefault: false,
        createTime: '2024-03-20 10:30:00',
        updateTime: '2025-01-24 08:30:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 资金计划模拟数据
  fundPlanning: () => ({
    records: [
      {
        planId: 'ZJ202501240001',
        planName: '2025年第一季度资金计划',
        planType: 'QUARTERLY',
        planStatus: 'APPROVED',
        startDate: '2025-01-01',
        endDate: '2025-03-31',
        totalIncome: 15000000.00,
        totalExpense: 12000000.00,
        netCashFlow: 3000000.00,
        currency: 'CNY',
        createTime: '2024-12-20 14:00:00',
        createUser: '财务部',
        approveTime: '2024-12-25 16:30:00',
        approveUser: '总经理'
      },
      {
        planId: 'ZJ202501240002',
        planName: '设备采购专项资金计划',
        planType: 'PROJECT',
        planStatus: 'EXECUTING',
        startDate: '2025-01-15',
        endDate: '2025-06-30',
        totalIncome: 0.00,
        totalExpense: 8000000.00,
        netCashFlow: -8000000.00,
        currency: 'CNY',
        createTime: '2025-01-10 09:15:00',
        createUser: '采购部',
        approveTime: '2025-01-12 11:20:00',
        approveUser: '副总经理'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 投资理财模拟数据
  investmentManagement: () => ({
    records: [
      {
        investmentId: 'TZ202501240001',
        productName: '银行理财产品A',
        productType: 'BANK_WEALTH',
        investmentAmount: 5000000.00,
        currency: 'CNY',
        expectedReturn: 0.045,
        actualReturn: 0.042,
        startDate: '2025-01-01',
        maturityDate: '2025-07-01',
        status: 'ACTIVE',
        riskLevel: 'LOW',
        bankName: '中国工商银行',
        createTime: '2024-12-28 10:00:00',
        updateTime: '2025-01-24 08:00:00'
      },
      {
        investmentId: 'TZ202501240002',
        productName: '货币基金B',
        productType: 'MONEY_FUND',
        investmentAmount: 2000000.00,
        currency: 'CNY',
        expectedReturn: 0.025,
        actualReturn: 0.028,
        startDate: '2024-12-15',
        maturityDate: '2025-12-15',
        status: 'ACTIVE',
        riskLevel: 'VERY_LOW',
        bankName: '招商银行',
        createTime: '2024-12-15 15:30:00',
        updateTime: '2025-01-24 08:00:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 风险管理模拟数据
  riskManagement: () => ({
    records: [
      {
        riskId: 'RISK202501240001',
        riskType: 'CREDIT_RISK',
        riskLevel: 'MEDIUM',
        riskDescription: '客户信用风险评估',
        counterparty: '北京科技有限公司',
        exposureAmount: 1500000.00,
        currency: 'CNY',
        riskStatus: 'MONITORING',
        createTime: '2025-01-24 09:00:00',
        updateTime: '2025-01-24 09:00:00'
      },
      {
        riskId: 'RISK202501240002',
        riskType: 'MARKET_RISK',
        riskLevel: 'HIGH',
        riskDescription: '汇率波动风险',
        counterparty: '美元投资组合',
        exposureAmount: 2000000.00,
        currency: 'USD',
        riskStatus: 'ALERT',
        createTime: '2025-01-23 15:30:00',
        updateTime: '2025-01-24 08:30:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 衍生品管理模拟数据
  derivativesManagement: () => ({
    records: [
      {
        contractId: 'DER202501240001',
        contractType: 'FORWARD',
        underlyingAsset: 'USD/CNY',
        notionalAmount: 1000000.00,
        currency: 'USD',
        strikePrice: 7.2500,
        maturityDate: '2025-06-24',
        contractStatus: 'ACTIVE',
        counterparty: '中国银行',
        createTime: '2025-01-15 10:00:00',
        updateTime: '2025-01-24 08:00:00'
      },
      {
        contractId: 'DER202501240002',
        contractType: 'OPTION',
        underlyingAsset: 'EUR/CNY',
        notionalAmount: 500000.00,
        currency: 'EUR',
        strikePrice: 7.8000,
        maturityDate: '2025-04-24',
        contractStatus: 'PENDING',
        counterparty: '工商银行',
        createTime: '2025-01-20 14:30:00',
        updateTime: '2025-01-24 08:00:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 票证管理模拟数据
  billManagement: () => ({
    records: [
      {
        billId: 'BILL202501240001',
        billNumber: 'HT202501240001',
        billType: 'BANK_ACCEPTANCE',
        amount: 500000.00,
        currency: 'CNY',
        issueDate: '2025-01-24',
        maturityDate: '2025-07-24',
        status: 'ISSUED',
        drawer: '示例云科技',
        payee: '北京供应商',
        acceptor: '中国工商银行',
        createTime: '2025-01-24 10:00:00',
        updateTime: '2025-01-24 10:00:00'
      },
      {
        billId: 'BILL202501240002',
        billNumber: 'HT202501240002',
        billType: 'COMMERCIAL_ACCEPTANCE',
        amount: 300000.00,
        currency: 'CNY',
        issueDate: '2025-01-23',
        maturityDate: '2025-04-23',
        status: 'ENDORSED',
        drawer: '示例云科技',
        payee: '上海贸易公司',
        acceptor: '示例云科技',
        createTime: '2025-01-23 14:00:00',
        updateTime: '2025-01-24 09:00:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 资金归集模拟数据
  fundConcentration: () => ({
    records: [
      {
        poolId: 'POOL202501240001',
        poolName: '示例云资金池',
        poolType: 'PHYSICAL',
        poolStatus: 'ACTIVE',
        totalBalance: 15000000.00,
        availableBalance: 12000000.00,
        currency: 'CNY',
        memberCount: 5,
        createTime: '2024-12-01 09:00:00',
        updateTime: '2025-01-24 08:00:00'
      },
      {
        poolId: 'POOL202501240002',
        poolName: '境外美元资金池',
        poolType: 'VIRTUAL',
        poolStatus: 'ACTIVE',
        totalBalance: 2000000.00,
        availableBalance: 1800000.00,
        currency: 'USD',
        memberCount: 3,
        createTime: '2025-01-10 10:00:00',
        updateTime: '2025-01-24 08:00:00'
      }
    ],
    total: 2,
    current: 1,
    size: 10
  }),

  // 通用账户列表（用于下拉选择）
  accountList: () => [
    {
      accountId: 'ACC001',
      accountName: '示例云基本户',
      accountNumber: '1234567890123456',
      bankName: '中国工商银行北京分行',
      currencyCode: 'CNY',
      balance: 5280000.00
    },
    {
      accountId: 'ACC002',
      accountName: '示例云专用户',
      accountNumber: '2345678901234567',
      bankName: '中国建设银行北京分行',
      currencyCode: 'CNY',
      balance: 3150000.00
    }
  ]
}

// 导出默认的模拟数据生成器
export default mockDataGenerators
