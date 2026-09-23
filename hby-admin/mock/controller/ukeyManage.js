/**
 * @description U盾信息管理 mock 接口 - 支持 CRUD 及状态统计
 */

let mockList = [
  {
    ukeyId: 1,
    accountId: 1,
    accountNumber: '1111222233334444555',
    accountName: '示例云科技有限公司',
    ukeyNo: 'ICBC20240001',
    ukeyType: 'ICBC',
    ukeyStatus: 'ACTIVE',
    effectiveDate: '2024-01-01',
    expiryDate: '2026-01-01',
    holderName: '张三',
    contactPhone: '13800138001',
    holderIdCard: '110101199001010001',
    certificatePath: '/certs/icbc_ukey_001.crt',
    deviceDescription: '工商银行企业网银U盾',
    lastUsedTime: '2026-02-01 10:30:00',
    remark: '主账户U盾',
    createTime: '2024-01-01 09:00:00',
  },
  {
    ukeyId: 2,
    accountId: 2,
    accountNumber: '2222333344445555666',
    accountName: '示例云投资有限公司',
    ukeyNo: 'CCB20240002',
    ukeyType: 'CCB',
    ukeyStatus: 'ACTIVE',
    effectiveDate: '2024-02-01',
    expiryDate: '2026-03-20',
    holderName: '李四',
    contactPhone: '13900139002',
    holderIdCard: '110101199002020002',
    certificatePath: '/certs/ccb_ukey_002.crt',
    deviceDescription: '建设银行企业网银U盾',
    lastUsedTime: '2026-01-15 14:20:00',
    remark: '投资业务专用',
    createTime: '2024-02-01 10:00:00',
  },
  {
    ukeyId: 3,
    accountId: 3,
    accountNumber: '5555666677778888999',
    accountName: '示例云贸易有限公司',
    ukeyNo: 'ABC20240003',
    ukeyType: 'ABC',
    ukeyStatus: 'LOCKED',
    effectiveDate: '2024-03-01',
    expiryDate: '2025-03-01',
    holderName: '王五',
    contactPhone: '13700137000',
    holderIdCard: '110101199003030003',
    certificatePath: '/certs/abc_ukey_003.crt',
    deviceDescription: '农业银行企业网银U盾',
    lastUsedTime: '2024-09-20 09:15:00',
    remark: '贸易业务专用，因安全原因锁定',
    createTime: '2024-03-01 09:15:00',
  },
  {
    ukeyId: 4,
    accountId: 4,
    accountNumber: '3333444455556666777',
    accountName: '示例云物流有限公司',
    ukeyNo: 'BOC20240004',
    ukeyType: 'BOC',
    ukeyStatus: 'ACTIVE',
    effectiveDate: '2024-04-01',
    expiryDate: '2026-04-01',
    holderName: '赵六',
    contactPhone: '13600136004',
    holderIdCard: '110101199004040004',
    certificatePath: '/certs/boc_ukey_004.crt',
    deviceDescription: '中国银行企业网银U盾',
    lastUsedTime: '2026-02-10 16:00:00',
    remark: '物流结算专用',
    createTime: '2024-04-01 08:30:00',
  },
  {
    ukeyId: 5,
    accountId: 5,
    accountNumber: '4444555566667777888',
    accountName: '示例云能源有限公司',
    ukeyNo: 'CMB20240005',
    ukeyType: 'CMB',
    ukeyStatus: 'ACTIVE',
    effectiveDate: '2024-05-01',
    expiryDate: '2026-04-10',
    holderName: '钱七',
    contactPhone: '13500135005',
    holderIdCard: '110101199005050005',
    certificatePath: '/certs/cmb_ukey_005.crt',
    deviceDescription: '招商银行企业网银U盾',
    lastUsedTime: '2026-02-08 11:45:00',
    remark: '能源采购专用',
    createTime: '2024-05-01 09:00:00',
  },
]

let nextId = 6

/** 计算统计数据 */
function calcStats() {
  const today = new Date()
  const in30Days = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000)
  let active = 0,
    expiring = 0,
    blocked = 0
  for (const item of mockList) {
    if (item.ukeyStatus === 'LOCKED') {
      blocked++
      continue
    }
    if (item.ukeyStatus === 'ACTIVE') {
      const exp = item.expiryDate ? new Date(item.expiryDate) : null
      if (exp && exp <= in30Days) expiring++
      else active++
    }
  }
  return {
    totalUKeys: mockList.length,
    activeUKeys: active,
    expiringUKeys: expiring,
    blockedUKeys: blocked,
  }
}

module.exports = [
  // 分页查询
  {
    url: '/qqsk/financial/ukey-info/page',
    type: 'get',
    response(req) {
      const {
        page = 1,
        limit = 20,
        ukeyNo,
        ukeyStatus,
        accountNumber,
      } = req.query
      let list = [...mockList]
      if (ukeyNo) list = list.filter((i) => i.ukeyNo.includes(ukeyNo))
      if (ukeyStatus) list = list.filter((i) => i.ukeyStatus === ukeyStatus)
      if (accountNumber)
        list = list.filter((i) => i.accountNumber.includes(accountNumber))
      const total = list.length
      const start = (Number(page) - 1) * Number(limit)
      const records = list.slice(start, start + Number(limit))
      return {
        code: 1,
        msg: '操作成功',
        data: { records, tlist: records, total, totalRecord: total },
        result: null,
      }
    },
  },
  // 状态统计
  {
    url: '/qqsk/financial/ukey-info/status-count',
    type: 'get',
    response() {
      return { code: 1, msg: '操作成功', data: calcStats(), result: null }
    },
  },
  // 新增
  {
    url: '/qqsk/financial/ukey-info',
    type: 'post',
    response(req) {
      const item = {
        ...req.body,
        ukeyId: nextId++,
        createTime: new Date().toISOString(),
      }
      mockList.push(item)
      return { code: 1, msg: '操作成功', data: item, result: null }
    },
  },
  // 更新
  {
    url: '/qqsk/financial/ukey-info',
    type: 'put',
    response(req) {
      const idx = mockList.findIndex(
        (i) => String(i.ukeyId) === String(req.body.ukeyId)
      )
      if (idx !== -1) mockList[idx] = { ...mockList[idx], ...req.body }
      return {
        code: 1,
        msg: '操作成功',
        data: mockList[idx] || null,
        result: null,
      }
    },
  },
  // 锁定
  {
    url: /\/qqsk\/financial\/ukey-info\/(\d+)\/lock/,
    type: 'put',
    response(req) {
      const id = req.path.split('/').slice(-2)[0]
      const item = mockList.find((i) => String(i.ukeyId) === String(id))
      if (item) item.ukeyStatus = 'LOCKED'
      return { code: 1, msg: '锁定成功', data: null, result: null }
    },
  },
  // 解锁
  {
    url: /\/qqsk\/financial\/ukey-info\/(\d+)\/unlock/,
    type: 'put',
    response(req) {
      const id = req.path.split('/').slice(-2)[0]
      const item = mockList.find((i) => String(i.ukeyId) === String(id))
      if (item) item.ukeyStatus = 'ACTIVE'
      return { code: 1, msg: '解锁成功', data: null, result: null }
    },
  },
  // 删除
  {
    url: /\/qqsk\/account\/ukey\/\d+/,
    type: 'delete',
    response(req) {
      const id = req.path.split('/').pop()
      mockList = mockList.filter((i) => String(i.ukeyId) !== String(id))
      return { code: 1, msg: '删除成功', data: null, result: null }
    },
  },
]
