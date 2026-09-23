/**
 * @description 第三方账户管理 mock 接口 - 支持真正的 CRUD 操作
 */

// 使用内存存储数据，支持真正的增删改查
let mockDataList = [
  {
    id: 1,
    accountCode: 'TPA001',
    accountName: '工商银行银企直连账户',
    thirdPartySystem: 'BANK_DIRECT',
    accountType: 'BANK_ACCOUNT',
    accountIdentifier: 'ICBC_APP_001',
    accountSecret: '******',
    apiUrl: 'https://api.icbc.com.cn',
    syncFrequency: 'REAL_TIME',
    connectionStatus: 'CONNECTED',
    lastSyncTime: '2026-02-01 10:30:00',
    description: '工商银行银企直连主账户',
    isEnabled: 1,
    createTime: '2025-01-01 00:00:00',
    updateTime: '2026-02-01 10:30:00',
  },
  {
    id: 2,
    accountCode: 'TPA002',
    accountName: '支付宝企业商户账户',
    thirdPartySystem: 'ALIPAY',
    accountType: 'PAYMENT_ACCOUNT',
    accountIdentifier: '2088xxxxxxxxxxxx',
    accountSecret: '******',
    apiUrl: 'https://openapi.alipay.com',
    syncFrequency: 'HOURLY',
    connectionStatus: 'CONNECTED',
    lastSyncTime: '2026-02-01 09:00:00',
    description: '支付宝在线支付账户',
    isEnabled: 1,
    createTime: '2025-02-01 00:00:00',
    updateTime: '2026-02-01 09:00:00',
  },
  {
    id: 3,
    accountCode: 'TPA003',
    accountName: '微信支付商户账户',
    thirdPartySystem: 'WECHAT_PAY',
    accountType: 'PAYMENT_ACCOUNT',
    accountIdentifier: 'wx_mch_001',
    accountSecret: '******',
    apiUrl: 'https://api.mch.weixin.qq.com',
    syncFrequency: 'DAILY',
    connectionStatus: 'DISCONNECTED',
    lastSyncTime: '2026-01-30 08:00:00',
    description: '微信支付商户账户',
    isEnabled: 0,
    createTime: '2025-03-01 00:00:00',
    updateTime: '2026-01-30 08:00:00',
  },
]

let nextId = 4

module.exports = [
  // 获取列表
  {
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/list',
    type: 'get',
    response(req) {
      // 兼容 query 参数和 body 参数两种传参方式
      const params = { ...(req.body || {}), ...(req.query || {}) }
      const pageNumber = Number(params.page) || Number(params.pageNumber) || 1
      const pageSize = Number(params.limit) || Number(params.pageSize) || 10
      const accountCode = params.accountCode
      const accountName = params.accountName
      // 兼容前端字段名 thirdPartySystem 和后端字段名 platformType
      const thirdPartySystem = params.thirdPartySystem || params.platformType
      const accountType = params.accountType
      const connectionStatus = params.connectionStatus

      let filteredList = [...mockDataList]

      // 过滤条件
      if (accountCode)
        filteredList = filteredList.filter((item) =>
          item.accountCode.includes(accountCode)
        )
      if (accountName)
        filteredList = filteredList.filter((item) =>
          item.accountName.includes(accountName)
        )
      if (thirdPartySystem)
        filteredList = filteredList.filter(
          (item) => item.thirdPartySystem === thirdPartySystem
        )
      if (accountType)
        filteredList = filteredList.filter(
          (item) => item.accountType === accountType
        )
      if (connectionStatus)
        filteredList = filteredList.filter(
          (item) => item.connectionStatus === connectionStatus
        )

      // 分页
      const start = (pageNumber - 1) * pageSize
      const end = start + pageSize
      const pagedList = filteredList.slice(start, end)

      return {
        code: 1,
        msg: '查询成功',
        data: {
          tlist: pagedList,
          totalRecord: filteredList.length,
          pageNo: pageNumber,
          pageSize: pageSize,
        },
      }
    },
  },
  // 新增
  {
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/create',
    type: 'post',
    response(req) {
      const newItem = {
        ...req.body,
        id: nextId++,
        createTime: new Date().toLocaleString(),
        updateTime: new Date().toLocaleString(),
      }
      mockDataList.push(newItem)
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },
  // 更新 - 关键修复：真正更新内存中的数据
  {
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/update',
    type: 'post',
    response(req) {
      const updateData = req.body
      const index = mockDataList.findIndex(
        (item) => item.id === updateData.id || item.id === Number(updateData.id)
      )

      if (index === -1) {
        return { code: 0, msg: '记录不存在，ID: ' + updateData.id }
      }

      // 真正更新数据
      mockDataList[index] = {
        ...mockDataList[index],
        ...updateData,
        updateTime: new Date().toLocaleString(),
      }
      console.log(
        '[Mock] 更新成功，ID:',
        updateData.id,
        '更新后数据:',
        mockDataList[index]
      )

      return { code: 1, msg: '更新成功', data: mockDataList[index] }
    },
  },
  // 删除
  {
    url: '/qqsk/financial/basicConfig/thirdPartyAccount/delete',
    type: 'post',
    response(req) {
      const id = (req.query && req.query.id) || (req.body && req.body.id)
      const index = mockDataList.findIndex(
        (item) => item.id === id || item.id === Number(id)
      )

      if (index === -1) {
        return { code: 0, msg: '记录不存在' }
      }

      mockDataList.splice(index, 1)
      return { code: 1, msg: '删除成功' }
    },
  },
]
