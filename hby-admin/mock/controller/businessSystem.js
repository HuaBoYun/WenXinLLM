/**
 * @description 业务系统注册 mock 接口
 */

const mockList = [
  {
    id: 1,
    systemName: 'ERP系统',
    systemCode: 'SYS001',
    systemType: 'ERP',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://erp.example.com/api',
    authType: 'TOKEN',
    lastHeartbeat: '2024-03-01 10:00:00',
    status: 1,
    createTime: '2024-01-01 09:00:00',
  },
  {
    id: 2,
    systemName: '资金管理系统',
    systemCode: 'SYS002',
    systemType: 'TREASURY',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://treasury.example.com/api',
    authType: 'OAUTH2',
    lastHeartbeat: '2024-03-01 09:55:00',
    status: 1,
    createTime: '2024-01-15 10:00:00',
  },
  {
    id: 3,
    systemName: '银企直连系统',
    systemCode: 'SYS003',
    systemType: 'BANK',
    connectionStatus: 'OFFLINE',
    apiUrl: 'https://bank.example.com/api',
    authType: 'CERT',
    lastHeartbeat: '2024-02-28 18:00:00',
    status: 1,
    createTime: '2024-02-01 11:00:00',
  },
  {
    id: 4,
    systemName: '票据管理系统',
    systemCode: 'SYS004',
    systemType: 'BILL',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://bill.example.com/api',
    authType: 'TOKEN',
    lastHeartbeat: '2024-03-01 09:50:00',
    status: 0,
    createTime: '2024-02-15 14:00:00',
  },
  {
    id: 5,
    systemName: '支付结算系统',
    systemCode: 'SYS005',
    systemType: 'PAYMENT',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://pay.example.com/api',
    authType: 'OAUTH2',
    lastHeartbeat: '2024-03-01 09:58:00',
    status: 1,
    createTime: '2024-03-01 08:00:00',
  },
  {
    id: 6,
    systemName: '风控系统',
    systemCode: 'SYS006',
    systemType: 'RISK',
    connectionStatus: 'OFFLINE',
    apiUrl: 'https://risk.example.com/api',
    authType: 'TOKEN',
    lastHeartbeat: '2024-02-27 12:00:00',
    status: 1,
    createTime: '2024-01-20 09:00:00',
  },
  {
    id: 7,
    systemName: '报表系统',
    systemCode: 'SYS007',
    systemType: 'REPORT',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://report.example.com/api',
    authType: 'TOKEN',
    lastHeartbeat: '2024-03-01 10:01:00',
    status: 1,
    createTime: '2024-02-10 15:00:00',
  },
  {
    id: 8,
    systemName: '审批流系统',
    systemCode: 'SYS008',
    systemType: 'WORKFLOW',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://workflow.example.com/api',
    authType: 'OAUTH2',
    lastHeartbeat: '2024-03-01 09:59:00',
    status: 1,
    createTime: '2024-02-20 10:00:00',
  },
  {
    id: 9,
    systemName: '档案管理系统',
    systemCode: 'SYS009',
    systemType: 'ARCHIVE',
    connectionStatus: 'OFFLINE',
    apiUrl: 'https://archive.example.com/api',
    authType: 'CERT',
    lastHeartbeat: '2024-02-25 16:00:00',
    status: 0,
    createTime: '2024-01-25 11:00:00',
  },
  {
    id: 10,
    systemName: '预算管理系统',
    systemCode: 'SYS010',
    systemType: 'BUDGET',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://budget.example.com/api',
    authType: 'TOKEN',
    lastHeartbeat: '2024-03-01 09:57:00',
    status: 1,
    createTime: '2024-02-05 09:00:00',
  },
  {
    id: 11,
    systemName: '合同管理系统',
    systemCode: 'SYS011',
    systemType: 'CONTRACT',
    connectionStatus: 'ONLINE',
    apiUrl: 'https://contract.example.com/api',
    authType: 'OAUTH2',
    lastHeartbeat: '2024-03-01 09:56:00',
    status: 1,
    createTime: '2024-02-12 13:00:00',
  },
  {
    id: 12,
    systemName: '供应链金融系统',
    systemCode: 'SYS012',
    systemType: 'SCF',
    connectionStatus: 'OFFLINE',
    apiUrl: 'https://scf.example.com/api',
    authType: 'TOKEN',
    lastHeartbeat: '2024-02-26 14:00:00',
    status: 0,
    createTime: '2024-01-30 10:00:00',
  },
]

module.exports = [
  {
    url: '/qqsk/financial/basicConfig/system/list',
    type: 'post',
    response(req) {
      const {
        page = 1,
        limit = 20,
        systemName,
        systemType,
        connectionStatus,
        status,
      } = req.body || {}
      let filtered = [...mockList]
      if (systemName)
        filtered = filtered.filter((i) => i.systemName.includes(systemName))
      if (systemType)
        filtered = filtered.filter((i) => i.systemType === systemType)
      if (connectionStatus)
        filtered = filtered.filter(
          (i) => i.connectionStatus === connectionStatus
        )
      if (status !== undefined && status !== null && status !== '') {
        filtered = filtered.filter((i) => i.status === Number(status))
      }
      const start = (Number(page) - 1) * Number(limit)
      const tlist = filtered.slice(start, start + Number(limit))
      return {
        code: 1,
        msg: 'success',
        data: { tlist, totalRecord: filtered.length },
      }
    },
  },
]
