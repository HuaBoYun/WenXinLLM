/**
 * 预算期间 Mock 接口
 */

module.exports = [
  // 导出期间
  {
    url: '/glkj/accountant/budget/period/export',
    type: 'post',
    response(req, res) {
      const { budgetYear, periodIds } = req.body || {}
      const allRecords = [
        {
          periodCode: '2026-YEAR',
          periodName: '2026年度预算',
          budgetYear: 2026,
          periodType: 'YEARLY',
          startDate: '2026-01-01',
          endDate: '2026-12-31',
          periodStatus: 'OPEN',
          isCurrent: 1,
          isLocked: 0,
        },
        {
          periodCode: '2026-Q1',
          periodName: '2026年第一季度',
          budgetYear: 2026,
          periodType: 'QUARTERLY',
          startDate: '2026-01-01',
          endDate: '2026-03-31',
          periodStatus: 'OPEN',
          isCurrent: 1,
          isLocked: 0,
        },
        {
          periodCode: '2026-M01',
          periodName: '2026年1月',
          budgetYear: 2026,
          periodType: 'MONTHLY',
          startDate: '2026-01-01',
          endDate: '2026-01-31',
          periodStatus: 'OPEN',
          isCurrent: 1,
          isLocked: 0,
        },
      ]
      const records =
        periodIds && periodIds.length > 0
          ? allRecords.filter((r) => periodIds.includes(r.periodCode))
          : allRecords.filter(
              (r) => !budgetYear || r.budgetYear === parseInt(budgetYear)
            )
      const headers = [
        '期间编码',
        '期间名称',
        '会计年度',
        '期间类型',
        '开始日期',
        '结束日期',
        '期间状态',
        '是否当前',
        '是否锁定',
      ]
      const rows = records.map((r) => [
        r.periodCode,
        r.periodName,
        r.budgetYear,
        r.periodType,
        r.startDate,
        r.endDate,
        r.periodStatus,
        r.isCurrent,
        r.isLocked,
      ])
      const csv = [headers, ...rows].map((row) => row.join(',')).join('\n')
      res.setHeader('Content-Type', 'text/csv;charset=utf-8')
      res.setHeader(
        'Content-Disposition',
        `attachment; filename=budget_period_${budgetYear || 'all'}.csv`
      )
      res.status(200).send('\uFEFF' + csv)
    },
  },

  // 操作历史
  {
    url: '/glkj/accountant/budget/period/history/list/:periodId',
    type: 'get',
    response(req) {
      const periodId = req.params.periodId
      const now = Date.now()
      return {
        code: 1,
        msg: '查询成功',
        data: [
          {
            historyId: '1',
            periodId,
            operationType: 'CREATE',
            operationDesc: '创建期间',
            beforeValue: null,
            afterValue: null,
            operator: 'admin',
            operateTime: new Date(now - 86400000 * 10),
          },
          {
            historyId: '2',
            periodId,
            operationType: 'UPDATE',
            operationDesc: '修改期间名称',
            beforeValue: '旧名称',
            afterValue: '新名称',
            operator: 'admin',
            operateTime: new Date(now - 86400000 * 5),
          },
          {
            historyId: '3',
            periodId,
            operationType: 'OPEN',
            operationDesc: '开启期间',
            beforeValue: 'CLOSED',
            afterValue: 'OPEN',
            operator: 'admin',
            operateTime: new Date(now - 86400000 * 2),
          },
          {
            historyId: '4',
            periodId,
            operationType: 'CLOSE',
            operationDesc: '关闭期间',
            beforeValue: 'OPEN',
            afterValue: 'CLOSED',
            operator: 'admin',
            operateTime: new Date(now - 86400000 * 1),
          },
        ],
      }
    },
  },

  // 期间报告汇总
  {
    url: '/glkj/accountant/budget/period/report/data/:periodId',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '查询成功',
        data: {
          budgetTaskCount: 18,
          budgetAmount: 3200000,
          executionAmount: 2560000,
          executionRate: 80.0,
          adjustmentCount: 3,
        },
      }
    },
  },

  // 期间报告明细
  {
    url: '/glkj/accountant/budget/period/report/detail',
    type: 'post',
    response(req) {
      const { pageNum = 1, pageSize = 10 } = req.body || {}
      const accounts = [
        {
          accountCode: '600101',
          accountName: '管理费用-办公费',
          budgetAmount: 120000,
          executionAmount: 98000,
        },
        {
          accountCode: '600102',
          accountName: '管理费用-差旅费',
          budgetAmount: 80000,
          executionAmount: 75000,
        },
        {
          accountCode: '600103',
          accountName: '管理费用-会议费',
          budgetAmount: 50000,
          executionAmount: 62000,
        },
        {
          accountCode: '610101',
          accountName: '销售费用-广告费',
          budgetAmount: 200000,
          executionAmount: 180000,
        },
        {
          accountCode: '610102',
          accountName: '销售费用-业务招待费',
          budgetAmount: 60000,
          executionAmount: 55000,
        },
        {
          accountCode: '620101',
          accountName: '财务费用-利息支出',
          budgetAmount: 30000,
          executionAmount: 28000,
        },
        {
          accountCode: '630101',
          accountName: '研发费用-材料费',
          budgetAmount: 150000,
          executionAmount: 140000,
        },
        {
          accountCode: '630102',
          accountName: '研发费用-测试费',
          budgetAmount: 90000,
          executionAmount: 85000,
        },
        {
          accountCode: '640101',
          accountName: '其他费用-咨询费',
          budgetAmount: 40000,
          executionAmount: 38000,
        },
        {
          accountCode: '640102',
          accountName: '其他费用-培训费',
          budgetAmount: 35000,
          executionAmount: 42000,
        },
      ]
      const list = accounts.map((a) => ({
        ...a,
        variance: a.executionAmount - a.budgetAmount,
        varianceRate: (
          ((a.executionAmount - a.budgetAmount) / a.budgetAmount) *
          100
        ).toFixed(2),
      }))
      const from = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '查询成功',
        data: {
          records: list.slice(from, from + pageSize),
          total: list.length,
          pageNum,
          pageSize,
        },
      }
    },
  },
]
