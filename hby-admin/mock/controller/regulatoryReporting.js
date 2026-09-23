/**
 * @description 监管报送模块 mock 接口
 * 覆盖 jgbs.vue（监管报送）和 bbsj.vue（报表数据管理）所有按钮
 */

// ==================== 公共 mock 数据 ====================
const authorityList = [
  {
    authorityId: 1,
    authorityCode: 'AUTH001',
    authorityName: '中国人民银行',
    authorityNameEng: 'PBOC',
    authorityType: 'CENTRAL_BANK',
    countryCode: 'CN',
    contactPerson: '张三',
    contactPhone: '010-66194114',
    isActive: 1,
    createdTime: '2024-01-01 10:00:00',
  },
  {
    authorityId: 2,
    authorityCode: 'AUTH002',
    authorityName: '国家金融监督管理总局',
    authorityNameEng: 'NFRA',
    authorityType: 'BANKING_REGULATOR',
    countryCode: 'CN',
    contactPerson: '李四',
    contactPhone: '010-66286688',
    isActive: 1,
    createdTime: '2024-01-02 10:00:00',
  },
  {
    authorityId: 3,
    authorityCode: 'AUTH003',
    authorityName: '中国证券监督管理委员会',
    authorityNameEng: 'CSRC',
    authorityType: 'SECURITIES_COMMISSION',
    countryCode: 'CN',
    contactPerson: '王五',
    contactPhone: '010-88061000',
    isActive: 1,
    createdTime: '2024-01-03 10:00:00',
  },
  {
    authorityId: 4,
    authorityCode: 'AUTH004',
    authorityName: '国家外汇管理局',
    authorityNameEng: 'SAFE',
    authorityType: 'FOREX_REGULATOR',
    countryCode: 'CN',
    contactPerson: '赵六',
    contactPhone: '010-68402265',
    isActive: 1,
    createdTime: '2024-01-04 10:00:00',
  },
]

const templateList = [
  {
    templateId: 1,
    templateCode: 'TPL001',
    templateName: '月度资产负债表模板',
    authorityId: 1,
    authorityName: '中国人民银行',
    reportFrequency: 'MONTHLY',
    templateVersion: '2.0',
    templateType: 'BALANCE_SHEET',
    effectiveDate: '2024-01-01',
    expiryDate: '2025-12-31',
    isEnabled: 1,
    createdTime: '2024-01-10 10:00:00',
  },
  {
    templateId: 2,
    templateCode: 'TPL002',
    templateName: '季度风险报告模板',
    authorityId: 2,
    authorityName: '国家金融监督管理总局',
    reportFrequency: 'QUARTERLY',
    templateVersion: '1.5',
    templateType: 'RISK_REPORT',
    effectiveDate: '2024-01-01',
    expiryDate: '2025-12-31',
    isEnabled: 1,
    createdTime: '2024-01-15 10:00:00',
  },
  {
    templateId: 3,
    templateCode: 'TPL003',
    templateName: '年度合规报告模板',
    authorityId: 3,
    authorityName: '中国证券监督管理委员会',
    reportFrequency: 'ANNUAL',
    templateVersion: '1.0',
    templateType: 'COMPLIANCE_REPORT',
    effectiveDate: '2024-01-01',
    expiryDate: '2025-12-31',
    isEnabled: 1,
    createdTime: '2024-02-01 10:00:00',
  },
]

const reportList = [
  {
    reportId: 'RPT2025001',
    reportNo: 'RPT2025001',
    reportName: '2025年1月资产负债表',
    authorityId: 1,
    authorityName: '中国人民银行',
    templateId: 1,
    templateName: '月度资产负债表模板',
    reportPeriod: '2025-01',
    dueDate: '2025-02-15',
    reportDate: '2025-01-31',
    reportStatus: 'SUBMITTED',
    submissionMethod: 'ONLINE',
    submitDate: '2025-02-10',
    acknowledgmentNo: 'ACK20250210001',
    isOverdue: false,
    createdTime: '2025-01-20 10:00:00',
  },
  {
    reportId: 'RPT2025002',
    reportNo: 'RPT2025002',
    reportName: '2025年Q1风险报告',
    authorityId: 2,
    authorityName: '国家金融监督管理总局',
    templateId: 2,
    templateName: '季度风险报告模板',
    reportPeriod: '2025-Q1',
    dueDate: '2025-04-30',
    reportDate: '2025-03-31',
    reportStatus: 'VALIDATED',
    submissionMethod: 'EMAIL',
    submitDate: null,
    acknowledgmentNo: null,
    isOverdue: false,
    createdTime: '2025-03-01 10:00:00',
  },
  {
    reportId: 'RPT2025003',
    reportNo: 'RPT2025003',
    reportName: '2025年2月资产负债表',
    authorityId: 1,
    authorityName: '中国人民银行',
    templateId: 1,
    templateName: '月度资产负债表模板',
    reportPeriod: '2025-02',
    dueDate: '2025-03-15',
    reportDate: '2025-02-28',
    reportStatus: 'GENERATED',
    submissionMethod: 'ONLINE',
    submitDate: null,
    acknowledgmentNo: null,
    isOverdue: false,
    createdTime: '2025-02-20 10:00:00',
  },
  {
    reportId: 'RPT2025004',
    reportNo: 'RPT2025004',
    reportName: '2024年度合规报告',
    authorityId: 3,
    authorityName: '中国证券监督管理委员会',
    templateId: 3,
    templateName: '年度合规报告模板',
    reportPeriod: '2024',
    dueDate: '2025-03-31',
    reportDate: '2024-12-31',
    reportStatus: 'DRAFT',
    submissionMethod: 'UPLOAD',
    submitDate: null,
    acknowledgmentNo: null,
    isOverdue: false,
    createdTime: '2025-01-05 10:00:00',
  },
  {
    reportId: 'RPT2025005',
    reportNo: 'RPT2025005',
    reportName: '2025年1月外汇报告',
    authorityId: 4,
    authorityName: '国家外汇管理局',
    templateId: null,
    templateName: null,
    reportPeriod: '2025-01',
    dueDate: '2025-01-20',
    reportDate: '2025-01-31',
    reportStatus: 'REJECTED',
    submissionMethod: 'ONLINE',
    submitDate: '2025-01-18',
    acknowledgmentNo: null,
    rejectReason: '数据格式不符合要求',
    isOverdue: true,
    createdTime: '2025-01-10 10:00:00',
  },
]

const ruleList = [
  {
    ruleId: 1,
    ruleCode: 'RULE001',
    ruleName: '资本充足率检查',
    ruleType: 'RATIO_CHECK',
    severityLevel: 'CRITICAL',
    checkFrequency: 'DAILY',
    checkScope: '全行',
    thresholdValue: 8.0,
    warningThreshold: 10.0,
    ruleCondition: '资本充足率 >= 8%',
    ruleFormula: 'capital / risk_weighted_assets * 100',
    regulationReference: '《商业银行资本管理办法》',
    isEnabled: 1,
    createdTime: '2024-01-01 10:00:00',
  },
  {
    ruleId: 2,
    ruleCode: 'RULE002',
    ruleName: '流动性覆盖率检查',
    ruleType: 'RATIO_CHECK',
    severityLevel: 'HIGH',
    checkFrequency: 'DAILY',
    checkScope: '全行',
    thresholdValue: 100.0,
    warningThreshold: 110.0,
    ruleCondition: '流动性覆盖率 >= 100%',
    ruleFormula: 'hqla / net_cash_outflows * 100',
    regulationReference: '《商业银行流动性风险管理办法》',
    isEnabled: 1,
    createdTime: '2024-01-02 10:00:00',
  },
  {
    ruleId: 3,
    ruleCode: 'RULE003',
    ruleName: '大额风险暴露检查',
    ruleType: 'LIMIT_CHECK',
    severityLevel: 'HIGH',
    checkFrequency: 'WEEKLY',
    checkScope: '对公业务',
    thresholdValue: 25.0,
    warningThreshold: 20.0,
    ruleCondition: '单一客户风险暴露 <= 25%',
    ruleFormula: 'exposure / tier1_capital * 100',
    regulationReference: '《商业银行大额风险暴露管理办法》',
    isEnabled: 1,
    createdTime: '2024-01-03 10:00:00',
  },
  {
    ruleId: 4,
    ruleCode: 'RULE004',
    ruleName: '数据完整性检查',
    ruleType: 'DATA_QUALITY',
    severityLevel: 'MEDIUM',
    checkFrequency: 'DAILY',
    checkScope: '报表数据',
    thresholdValue: 99.0,
    warningThreshold: 99.5,
    ruleCondition: '数据完整率 >= 99%',
    ruleFormula: null,
    regulationReference: '内部数据质量管理规范',
    isEnabled: 1,
    createdTime: '2024-01-04 10:00:00',
  },
  {
    ruleId: 5,
    ruleCode: 'RULE005',
    ruleName: '报送时效性检查',
    ruleType: 'BUSINESS_RULE',
    severityLevel: 'MEDIUM',
    checkFrequency: 'DAILY',
    checkScope: '报送管理',
    thresholdValue: 0,
    warningThreshold: 3,
    ruleCondition: '报送时间 <= 截止日期',
    ruleFormula: null,
    regulationReference: '监管报送管理规定',
    isEnabled: 1,
    createdTime: '2024-01-05 10:00:00',
  },
]

const resultList = [
  {
    resultId: 'RES001',
    ruleId: 1,
    reportId: 'RPT2025001',
    isPassed: 1,
    severityLevel: 'CRITICAL',
    checkStatus: 'COMPLETED',
    violationDetails: null,
    actionTaken: null,
    isEscalated: 0,
    checkTime: '2025-02-10 08:00:00',
    resolvedTime: null,
    createdTime: '2025-02-10 08:00:00',
  },
  {
    resultId: 'RES002',
    ruleId: 2,
    reportId: 'RPT2025001',
    isPassed: 0,
    severityLevel: 'HIGH',
    checkStatus: 'COMPLETED',
    violationDetails: '流动性覆盖率为98.5%，低于100%最低要求',
    actionTaken: '已启动流动性补充措施',
    isEscalated: 1,
    checkTime: '2025-02-10 08:05:00',
    resolvedTime: null,
    createdTime: '2025-02-10 08:05:00',
  },
  {
    resultId: 'RES003',
    ruleId: 3,
    reportId: 'RPT2025002',
    isPassed: 1,
    severityLevel: 'HIGH',
    checkStatus: 'COMPLETED',
    violationDetails: null,
    actionTaken: null,
    isEscalated: 0,
    checkTime: '2025-03-15 09:00:00',
    resolvedTime: null,
    createdTime: '2025-03-15 09:00:00',
  },
  {
    resultId: 'RES004',
    ruleId: 4,
    reportId: 'RPT2025003',
    isPassed: 0,
    severityLevel: 'MEDIUM',
    checkStatus: 'COMPLETED',
    violationDetails: '数据完整率为97.8%，低于99%要求',
    actionTaken: null,
    isEscalated: 0,
    checkTime: '2025-03-01 10:00:00',
    resolvedTime: null,
    createdTime: '2025-03-01 10:00:00',
  },
]

module.exports = [
  // ==================== 监管机构管理 ====================
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/list',
    type: 'get',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 10,
        authorityName,
        authorityType,
        isActive,
      } = req.query
      let list = [...authorityList]
      if (authorityName)
        list = list.filter((a) => a.authorityName.includes(authorityName))
      if (authorityType)
        list = list.filter((a) => a.authorityType === authorityType)
      if (isActive !== undefined && isActive !== '')
        list = list.filter((a) => a.isActive === Number(isActive))
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '操作成功',
        data: {
          rows: list.slice(start, start + Number(pageSize)),
          total: list.length,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/active',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: authorityList.filter((a) => a.isActive === 1),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/[0-9]+',
    type: 'get',
    response(req) {
      const id = Number(req.path.split('/').pop())
      const item = authorityList.find((a) => a.authorityId === id)
      return item
        ? { code: 1, msg: '操作成功', data: item }
        : { code: 0, msg: '未找到', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority',
    type: 'post',
    response(req) {
      const newItem = {
        ...req.body,
        authorityId: Date.now(),
        createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      }
      authorityList.push(newItem)
      return { code: 1, msg: '新增成功', data: newItem }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority',
    type: 'put',
    response(req) {
      const idx = authorityList.findIndex(
        (a) => a.authorityId === req.body.authorityId
      )
      if (idx >= 0) authorityList[idx] = { ...authorityList[idx], ...req.body }
      return { code: 1, msg: '修改成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/[0-9,]+',
    type: 'delete',
    response() {
      return { code: 1, msg: '删除成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/[0-9]+/status/[01]',
    type: 'put',
    response(req) {
      const parts = req.path.split('/')
      const id = Number(parts[parts.length - 3])
      const isActive = Number(parts[parts.length - 1])
      const item = authorityList.find((a) => a.authorityId === id)
      if (item) item.isActive = isActive
      return { code: 1, msg: '状态更新成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功', data: null }
    },
  },

  // ==================== 报告模板管理 ====================
  {
    url: '/qqsk/globalTreasurer/regulatory/template/list',
    type: 'get',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 10,
        templateName,
        authorityId,
        isEnabled,
      } = req.query
      let list = [...templateList]
      if (templateName)
        list = list.filter((t) => t.templateName.includes(templateName))
      if (authorityId)
        list = list.filter((t) => t.authorityId === Number(authorityId))
      if (isEnabled !== undefined && isEnabled !== '')
        list = list.filter((t) => t.isEnabled === Number(isEnabled))
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '操作成功',
        data: {
          rows: list.slice(start, start + Number(pageSize)),
          total: list.length,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/usable',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: templateList.filter((t) => t.isEnabled === 1),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/[0-9]+',
    type: 'get',
    response(req) {
      const id = Number(req.path.split('/').pop())
      const item = templateList.find((t) => t.templateId === id)
      return item
        ? { code: 1, msg: '操作成功', data: item }
        : { code: 0, msg: '未找到', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template',
    type: 'post',
    response(req) {
      const newItem = {
        ...req.body,
        templateId: Date.now(),
        createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      }
      templateList.push(newItem)
      return { code: 1, msg: '新增成功', data: newItem }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template',
    type: 'put',
    response(req) {
      const idx = templateList.findIndex(
        (t) => t.templateId === req.body.templateId
      )
      if (idx >= 0) templateList[idx] = { ...templateList[idx], ...req.body }
      return { code: 1, msg: '修改成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/[0-9,]+',
    type: 'delete',
    response() {
      return { code: 1, msg: '删除成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/[0-9]+/status/[01]',
    type: 'put',
    response(req) {
      const parts = req.path.split('/')
      const id = Number(parts[parts.length - 3])
      const isEnabled = Number(parts[parts.length - 1])
      const item = templateList.find((t) => t.templateId === id)
      if (item) item.isEnabled = isEnabled
      return { code: 1, msg: '状态更新成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/[0-9]+/copy',
    type: 'post',
    response(req) {
      const id = Number(req.path.split('/')[req.path.split('/').length - 2])
      const src = templateList.find((t) => t.templateId === id)
      if (src) {
        const copy = {
          ...src,
          templateId: Date.now(),
          templateCode: src.templateCode + '_COPY',
          templateName: src.templateName + '_副本',
          createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        }
        templateList.push(copy)
      }
      return { code: 1, msg: '复制成功', data: null }
    },
  },

  // ==================== 监管报告管理 ====================
  {
    url: '/qqsk/globalTreasurer/regulatory/report/statistics',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: {
          totalReports: reportList.length,
          overdueReports: reportList.filter((r) => r.isOverdue).length,
          dueSoonReports: 2,
          acceptedReports: reportList.filter(
            (r) =>
              r.reportStatus === 'SUBMITTED' || r.reportStatus === 'ACCEPTED'
          ).length,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/list',
    type: 'get',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 10,
        reportName,
        reportStatus,
        authorityId,
        reportPeriod,
      } = req.query
      let list = [...reportList]
      if (reportName)
        list = list.filter((r) => r.reportName.includes(reportName))
      if (reportStatus)
        list = list.filter((r) => r.reportStatus === reportStatus)
      if (authorityId)
        list = list.filter((r) => r.authorityId === Number(authorityId))
      if (reportPeriod)
        list = list.filter(
          (r) => r.reportPeriod && r.reportPeriod.includes(reportPeriod)
        )
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '操作成功',
        data: {
          rows: list.slice(start, start + Number(pageSize)),
          total: list.length,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/overdue',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: reportList.filter((r) => r.isOverdue),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/history',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: [
          {
            action: '创建报告',
            time: '2025-01-20 10:00:00',
            remark: '系统自动创建',
          },
          {
            action: '生成报告',
            time: '2025-02-01 09:00:00',
            remark: '数据已从核心系统提取',
          },
          {
            action: '验证通过',
            time: '2025-02-05 14:00:00',
            remark: '所有合规检查通过',
          },
          {
            action: '提交报告',
            time: '2025-02-10 10:00:00',
            remark: '通过在线系统提交',
          },
        ],
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+',
    type: 'get',
    response(req) {
      const id = req.path.split('/').pop()
      const item = reportList.find((r) => r.reportId === id)
      return item
        ? { code: 1, msg: '操作成功', data: item }
        : { code: 0, msg: '未找到', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report',
    type: 'post',
    response(req) {
      const newItem = {
        ...req.body,
        reportId: 'RPT' + Date.now(),
        reportNo: 'RPT' + Date.now(),
        reportStatus: 'DRAFT',
        isOverdue: false,
        createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      }
      reportList.push(newItem)
      return { code: 1, msg: '新增成功', data: newItem }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report',
    type: 'put',
    response(req) {
      const idx = reportList.findIndex((r) => r.reportId === req.body.reportId)
      if (idx >= 0) reportList[idx] = { ...reportList[idx], ...req.body }
      return { code: 1, msg: '修改成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9,]+',
    type: 'delete',
    response() {
      return { code: 1, msg: '删除成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/generate',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      if (item) item.reportStatus = 'GENERATED'
      return { code: 1, msg: '报告生成成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/validate',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      if (item) item.reportStatus = 'VALIDATED'
      return { code: 1, msg: '验证通过', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/submit',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      if (item) {
        item.reportStatus = 'SUBMITTED'
        item.submitDate = new Date().toISOString().slice(0, 10)
      }
      return { code: 1, msg: '报告提交成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/recall',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      if (item) item.reportStatus = 'GENERATED'
      return { code: 1, msg: '撤回成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/copy',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const src = reportList.find((r) => r.reportId === id)
      if (src) {
        const newId = 'RPT' + Date.now()
        reportList.push({
          ...src,
          reportId: newId,
          reportNo: newId,
          reportStatus: 'DRAFT',
          submitDate: null,
          acknowledgmentNo: null,
          createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        })
      }
      return { code: 1, msg: '复制成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/batch-generate',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = reportList.find((r) => r.reportId === id)
        if (item) item.reportStatus = 'GENERATED'
      })
      return {
        code: 1,
        msg: `批量生成成功，共处理 ${ids.length} 条`,
        data: null,
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/batch-submit',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = reportList.find((r) => r.reportId === id)
        if (item) {
          item.reportStatus = 'SUBMITTED'
          item.submitDate = new Date().toISOString().slice(0, 10)
        }
      })
      return {
        code: 1,
        msg: `批量提交成功，共处理 ${ids.length} 条`,
        data: null,
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功', data: null }
    },
  },

  // ==================== 合规检查规则管理 ====================
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/list',
    type: 'get',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 10,
        ruleName,
        ruleType,
        severityLevel,
        isEnabled,
      } = req.query
      let list = [...ruleList]
      if (ruleName) list = list.filter((r) => r.ruleName.includes(ruleName))
      if (ruleType) list = list.filter((r) => r.ruleType === ruleType)
      if (severityLevel)
        list = list.filter((r) => r.severityLevel === severityLevel)
      if (isEnabled !== undefined && isEnabled !== '')
        list = list.filter((r) => r.isEnabled === Number(isEnabled))
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '操作成功',
        data: {
          rows: list.slice(start, start + Number(pageSize)),
          total: list.length,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/[0-9]+',
    type: 'get',
    response(req) {
      const id = Number(req.path.split('/').pop())
      const item = ruleList.find((r) => r.ruleId === id)
      return item
        ? { code: 1, msg: '操作成功', data: item }
        : { code: 0, msg: '未找到', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule',
    type: 'post',
    response(req) {
      const newItem = {
        ...req.body,
        ruleId: Date.now(),
        createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      }
      ruleList.push(newItem)
      return { code: 1, msg: '新增成功', data: newItem }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule',
    type: 'put',
    response(req) {
      const idx = ruleList.findIndex((r) => r.ruleId === req.body.ruleId)
      if (idx >= 0) ruleList[idx] = { ...ruleList[idx], ...req.body }
      return { code: 1, msg: '修改成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/[0-9,]+',
    type: 'delete',
    response() {
      return { code: 1, msg: '删除成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/[0-9]+/status/[01]',
    type: 'put',
    response(req) {
      const parts = req.path.split('/')
      const id = Number(parts[parts.length - 3])
      const isEnabled = Number(parts[parts.length - 1])
      const item = ruleList.find((r) => r.ruleId === id)
      if (item) item.isEnabled = isEnabled
      return { code: 1, msg: '状态更新成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/[0-9]+/execute',
    type: 'post',
    response(req) {
      const id = Number(req.path.split('/')[req.path.split('/').length - 2])
      const rule = ruleList.find((r) => r.ruleId === id)
      const newResult = {
        resultId: 'RES' + Date.now(),
        ruleId: id,
        reportId: null,
        isPassed: Math.random() > 0.3 ? 1 : 0,
        severityLevel: rule ? rule.severityLevel : 'MEDIUM',
        checkStatus: 'COMPLETED',
        violationDetails: null,
        actionTaken: null,
        isEscalated: 0,
        checkTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
      }
      resultList.push(newResult)
      return { code: 1, msg: '规则执行成功', data: newResult }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/batch-execute',
    type: 'post',
    response(req) {
      const ids = req.body || []
      return {
        code: 1,
        msg: `批量执行成功，共执行 ${ids.length} 条规则`,
        data: null,
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/[0-9]+/copy',
    type: 'post',
    response(req) {
      const id = Number(req.path.split('/')[req.path.split('/').length - 2])
      const src = ruleList.find((r) => r.ruleId === id)
      if (src)
        ruleList.push({
          ...src,
          ruleId: Date.now(),
          ruleCode: src.ruleCode + '_COPY',
          ruleName: src.ruleName + '_副本',
          createdTime: new Date().toISOString().replace('T', ' ').slice(0, 19),
        })
      return { code: 1, msg: '复制成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功', data: null }
    },
  },

  // ==================== 合规检查结果管理 ====================
  {
    url: '/qqsk/globalTreasurer/regulatory/result/statistics',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: {
          totalRules: ruleList.length,
          failedResults: resultList.filter((r) => r.isPassed === 0).length,
          passedResults: resultList.filter((r) => r.isPassed === 1).length,
          complianceScore: 92.5,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/list',
    type: 'get',
    response(req) {
      const {
        pageNum = 1,
        pageSize = 10,
        ruleId,
        reportId,
        isPassed,
        checkStatus,
        severityLevel,
      } = req.query
      let list = [...resultList]
      if (ruleId) list = list.filter((r) => r.ruleId === Number(ruleId))
      if (reportId) list = list.filter((r) => r.reportId === reportId)
      if (isPassed !== undefined && isPassed !== '')
        list = list.filter((r) => r.isPassed === Number(isPassed))
      if (checkStatus) list = list.filter((r) => r.checkStatus === checkStatus)
      if (severityLevel)
        list = list.filter((r) => r.severityLevel === severityLevel)
      const start = (pageNum - 1) * pageSize
      return {
        code: 1,
        msg: '操作成功',
        data: {
          rows: list.slice(start, start + Number(pageSize)),
          total: list.length,
        },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/[A-Z0-9]+',
    type: 'get',
    response(req) {
      const id = req.path.split('/').pop()
      const item = resultList.find((r) => r.resultId === id)
      return item
        ? { code: 1, msg: '操作成功', data: item }
        : { code: 0, msg: '未找到', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/[A-Z0-9]+/process',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = resultList.find((r) => r.resultId === id)
      if (item) {
        item.checkStatus = 'COMPLETED'
        item.actionTaken = req.body.actionTaken || '已处理'
      }
      return { code: 1, msg: '处理成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/[A-Z0-9]+/resolve',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = resultList.find((r) => r.resultId === id)
      if (item) {
        item.checkStatus = 'COMPLETED'
        item.resolvedTime = new Date()
          .toISOString()
          .replace('T', ' ')
          .slice(0, 19)
      }
      return { code: 1, msg: '解决成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/[A-Z0-9]+/escalate',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = resultList.find((r) => r.resultId === id)
      if (item) item.isEscalated = 1
      return { code: 1, msg: '升级成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/batch-process',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = resultList.find((r) => r.resultId === id)
        if (item) item.checkStatus = 'COMPLETED'
      })
      return {
        code: 1,
        msg: `批量处理成功，共处理 ${ids.length} 条`,
        data: null,
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/batch-resolve',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = resultList.find((r) => r.resultId === id)
        if (item)
          item.resolvedTime = new Date()
            .toISOString()
            .replace('T', ' ')
            .slice(0, 19)
      })
      return {
        code: 1,
        msg: `批量解决成功，共处理 ${ids.length} 条`,
        data: null,
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/batch-escalate',
    type: 'post',
    response(req) {
      const ids = req.body || []
      ids.forEach((id) => {
        const item = resultList.find((r) => r.resultId === id)
        if (item) item.isEscalated = 1
      })
      return {
        code: 1,
        msg: `批量升级成功，共处理 ${ids.length} 条`,
        data: null,
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/export',
    type: 'post',
    response() {
      return { code: 1, msg: '导出成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/trends',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: [
          { month: '2025-01', passedCount: 45, failedCount: 5 },
          { month: '2025-02', passedCount: 48, failedCount: 3 },
          { month: '2025-03', passedCount: 50, failedCount: 2 },
        ],
      }
    },
  },

  // ==================== 辅助接口 ====================
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/type/[A-Z_]+',
    type: 'get',
    response() {
      return { code: 1, msg: '操作成功', data: authorityList }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/authority/important',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: authorityList.filter((a) => a.isImportant),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/authority/[0-9]+',
    type: 'get',
    response(req) {
      const id = Number(req.path.split('/').pop())
      return {
        code: 1,
        msg: '操作成功',
        data: templateList.filter((t) => t.authorityId === id),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/expiring/[0-9]+',
    type: 'get',
    response() {
      return { code: 1, msg: '操作成功', data: [] }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/template/[0-9]+/version',
    type: 'post',
    response() {
      return { code: 1, msg: '版本创建成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/due-soon/[0-9]+',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: reportList.filter((r) => !r.isOverdue),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/attention',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: reportList.filter(
          (r) => r.isOverdue || r.reportStatus === 'REJECTED'
        ),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/trends',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: [
          { month: '2025-01', total: 5, submitted: 3 },
          { month: '2025-02', total: 6, submitted: 4 },
          { month: '2025-03', total: 4, submitted: 2 },
        ],
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/accept',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      if (item) {
        item.reportStatus = 'ACCEPTED'
        item.acknowledgmentNo = req.query.acknowledgmentNo || 'ACK' + Date.now()
      }
      return { code: 1, msg: '接受成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/reject',
    type: 'post',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      if (item) {
        item.reportStatus = 'REJECTED'
        item.rejectReason = req.query.rejectReason || '不符合要求'
      }
      return { code: 1, msg: '拒绝成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/download',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: { downloadUrl: '/mock/download/report.xlsx' },
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/[A-Z0-9]+/preview',
    type: 'get',
    response(req) {
      const id = req.path.split('/')[req.path.split('/').length - 2]
      const item = reportList.find((r) => r.reportId === id)
      return { code: 1, msg: '操作成功', data: item || {} }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/report/importTemplate',
    type: 'get',
    response() {
      return { code: 1, msg: '操作成功', data: null }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/rule/executable',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: ruleList.filter((r) => r.isEnabled === 1),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/attention',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: resultList.filter((r) => r.isPassed === 0),
      }
    },
  },
  {
    url: '/qqsk/globalTreasurer/regulatory/result/immediate',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '操作成功',
        data: resultList.filter(
          (r) => r.isPassed === 0 && r.severityLevel === 'CRITICAL'
        ),
      }
    },
  },
]
