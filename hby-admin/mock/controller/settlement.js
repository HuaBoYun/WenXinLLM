/**
 * @description 结算平台首页 mock 接口
 */
module.exports = [
  // 待结算数据汇总
  {
    url: '/qqsk/settlement/pending-data/summary',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: {
          pendingCount: 18,
          processingCount: 5,
          settledCount: 132,
          failedCount: 3,
          totalCount: 158,
          pendingAmount: 4350000,
          processingAmount: 1200000,
          settledAmount: 28450000,
          failedAmount: 320000,
          totalAmount: 34320000,
        },
      }
    },
  },

  // 高优先级待结算数据
  {
    url: '/qqsk/settlement/pending-data/high-priority',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          {
            pendingId: 1001,
            businessNo: 'BIZ20260316001',
            settlementAmount: 1500000,
            priority: 'URGENT',
            expectedSettlementDate: '2026-03-16',
            settlementStatus: 'PENDING',
            businessType: 'TRANSFER',
            currencyCode: 'CNY',
          },
          {
            pendingId: 1002,
            businessNo: 'BIZ20260316002',
            settlementAmount: 850000,
            priority: 'URGENT',
            expectedSettlementDate: '2026-03-16',
            settlementStatus: 'APPROVED',
            businessType: 'PAYMENT',
            currencyCode: 'CNY',
          },
          {
            pendingId: 1003,
            businessNo: 'BIZ20260315003',
            settlementAmount: 2200000,
            priority: 'HIGH',
            expectedSettlementDate: '2026-03-15',
            settlementStatus: 'PENDING',
            businessType: 'TRANSFER',
            currencyCode: 'USD',
          },
          {
            pendingId: 1004,
            businessNo: 'BIZ20260315004',
            settlementAmount: 430000,
            priority: 'HIGH',
            expectedSettlementDate: '2026-03-17',
            settlementStatus: 'PENDING',
            businessType: 'COLLECTION',
            currencyCode: 'CNY',
          },
          {
            pendingId: 1005,
            businessNo: 'BIZ20260314005',
            settlementAmount: 980000,
            priority: 'HIGH',
            expectedSettlementDate: '2026-03-14',
            settlementStatus: 'APPROVED',
            businessType: 'PAYMENT',
            currencyCode: 'EUR',
          },
        ],
      }
    },
  },

  // 严重异常列表
  {
    url: '/qqsk/settlement/exception/critical',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          {
            exceptionId: 2001,
            exceptionNo: 'EXC20260316001',
            exceptionLevel: 'CRITICAL',
            exceptionType: 'BANK_CONNECTION_FAILED',
            errorMessage: '银行接口连接超时，结算通道异常',
            exceptionStatus: 'OPEN',
            createTime: '2026-03-16 09:15:00',
            businessNo: 'BIZ20260316001',
          },
          {
            exceptionId: 2002,
            exceptionNo: 'EXC20260316002',
            exceptionLevel: 'HIGH',
            exceptionType: 'AMOUNT_MISMATCH',
            errorMessage: '结算金额与账户余额不匹配，差额 ¥500',
            exceptionStatus: 'OPEN',
            createTime: '2026-03-16 10:30:00',
            businessNo: 'BIZ20260316002',
          },
          {
            exceptionId: 2003,
            exceptionNo: 'EXC20260315003',
            exceptionLevel: 'CRITICAL',
            exceptionType: 'DUPLICATE_TRANSACTION',
            errorMessage: '检测到重复交易，业务编号重复提交',
            exceptionStatus: 'PROCESSING',
            createTime: '2026-03-15 16:45:00',
            businessNo: 'BIZ20260315003',
          },
        ],
      }
    },
  },
]
