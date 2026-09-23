/**
 * @description 印鉴组合配置 mock 接口 - 支持 CRUD
 */

let mockList = [
  {
    id: 1,
    combinationCode: 'COMB001',
    combinationName: '资金划转组合',
    combinationType: 'MULTI_SEAL',
    businessType: 'FUND_TRANSFER',
    authorityLevel: 'LEVEL_1',
    sealIds: [1, 2],
    sealList: '1,2',
    description: '资金划转业务所需印鉴组合',
    isEnabled: 1,
    status: 1,
    usageCount: 5,
    createTime: '2024-01-01 09:00:00',
    updateTime: '2024-01-01 09:00:00',
  },
  {
    id: 2,
    combinationCode: 'COMB002',
    combinationName: '合同签署组合',
    combinationType: 'COMBINATION',
    businessType: 'CONTRACT',
    authorityLevel: 'LEVEL_2',
    sealIds: [1, 3],
    sealList: '1,3',
    description: '合同签署业务所需印鉴组合',
    isEnabled: 1,
    status: 1,
    usageCount: 12,
    createTime: '2024-02-01 10:00:00',
    updateTime: '2024-02-01 10:00:00',
  },
]

let nextId = 3

module.exports = [
  // 分页查询印鉴组合列表
  {
    url: '/qqsk/financial/basicConfig/sealCombination/list',
    type: 'post',
    response(req) {
      const {
        page = 1,
        limit = 20,
        combinationName,
        combinationCode,
        status,
      } = req.body || {}
      let filtered = [...mockList]
      if (combinationName)
        filtered = filtered.filter((i) =>
          i.combinationName.includes(combinationName)
        )
      if (combinationCode)
        filtered = filtered.filter((i) =>
          i.combinationCode.includes(combinationCode)
        )
      if (status !== undefined && status !== null && status !== '') {
        filtered = filtered.filter((i) => i.status === Number(status))
      }
      const start = (page - 1) * limit
      const tlist = filtered.slice(start, start + Number(limit))
      return {
        code: 1,
        msg: 'success',
        data: { tlist, totalRecord: filtered.length },
      }
    },
  },

  // 新增印鉴组合
  {
    url: '/qqsk/financial/basicConfig/sealCombination/add',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const newItem = {
        ...data,
        id: nextId++,
        status: data.isEnabled !== undefined ? Number(data.isEnabled) : 1,
        usageCount: 0,
        createTime: new Date().toLocaleString('zh-CN'),
        updateTime: new Date().toLocaleString('zh-CN'),
      }
      mockList.push(newItem)
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },

  // 更新印鉴组合
  {
    url: '/qqsk/financial/basicConfig/sealCombination/update',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const idx = mockList.findIndex((i) => i.id === Number(data.id))
      if (idx === -1) return { code: 0, msg: '记录不存在', data: null }
      mockList[idx] = {
        ...mockList[idx],
        ...data,
        updateTime: new Date().toLocaleString('zh-CN'),
      }
      return { code: 1, msg: '更新成功', data: mockList[idx] }
    },
  },

  // 删除印鉴组合
  {
    url: '/qqsk/financial/basicConfig/sealCombination/delete',
    type: 'post',
    response(req) {
      const { id } = req.body || {}
      const idx = mockList.findIndex((i) => i.id === Number(id))
      if (idx === -1) return { code: 0, msg: '记录不存在', data: null }
      mockList.splice(idx, 1)
      return { code: 1, msg: '删除成功', data: null }
    },
  },

  // 查询印鉴组合详情
  {
    url: '/qqsk/financial/basicConfig/sealCombination/detail',
    type: 'get',
    response(req) {
      const id = Number(req.query && req.query.id)
      const item = mockList.find((i) => i.id === id)
      if (!item) return { code: 0, msg: '记录不存在', data: null }
      return { code: 1, msg: 'success', data: item }
    },
  },
]
