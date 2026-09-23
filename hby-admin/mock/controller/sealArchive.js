/**
 * @description 印鉴档案管理 mock 接口 - 支持 CRUD
 */

let mockList = [
  {
    id: 1,
    sealCode: 'SEAL001',
    sealName: '公司公章',
    sealType: 'OFFICIAL_SEAL',
    sealTypeName: '公章',
    department: '综合管理部',
    ownerName: '张三',
    ownerIdCard: '110101199001010001',
    contactPhone: '13800138001',
    effectiveDate: '2024-01-01',
    expiryDate: '2027-01-01',
    sealStatus: 'ACTIVE',
    isEnabled: 1,
    deleteFlag: 0,
    description: '公司对外公章',
    createTime: '2024-01-01 09:00:00',
    updateTime: '2024-01-01 09:00:00',
  },
  {
    id: 2,
    sealCode: 'SEAL002',
    sealName: '财务专用章',
    sealType: 'FINANCE_SEAL',
    sealTypeName: '财务章',
    department: '财务部',
    ownerName: '李四',
    ownerIdCard: '110101199002020002',
    contactPhone: '13900139002',
    effectiveDate: '2024-02-01',
    expiryDate: '2027-02-01',
    sealStatus: 'ACTIVE',
    isEnabled: 1,
    deleteFlag: 0,
    description: '财务部专用印鉴',
    createTime: '2024-02-01 10:00:00',
    updateTime: '2024-02-01 10:00:00',
  },
  {
    id: 3,
    sealCode: 'SEAL003',
    sealName: '法人名章',
    sealType: 'LEGAL_PERSON_SEAL',
    sealTypeName: '法人章',
    department: '董事会',
    ownerName: '王五',
    ownerIdCard: '110101199003030003',
    contactPhone: '13700137003',
    effectiveDate: '2024-03-01',
    expiryDate: '2027-03-01',
    sealStatus: 'ACTIVE',
    isEnabled: 1,
    deleteFlag: 0,
    description: '法定代表人名章',
    createTime: '2024-03-01 11:00:00',
    updateTime: '2024-03-01 11:00:00',
  },
]

let nextId = 4

module.exports = [
  // 分页查询印鉴档案列表
  {
    url: '/qqsk/financial/basicConfig/sealArchive/list',
    type: 'get',
    response(req) {
      const {
        page = 1,
        limit = 20,
        sealCode,
        sealName,
        sealType,
        isEnabled,
      } = req.query || {}
      let filtered = mockList.filter((item) => item.deleteFlag === 0)
      if (sealCode)
        filtered = filtered.filter((i) => i.sealCode.includes(sealCode))
      if (sealName)
        filtered = filtered.filter((i) => i.sealName.includes(sealName))
      if (sealType) filtered = filtered.filter((i) => i.sealType === sealType)
      if (isEnabled !== undefined && isEnabled !== null && isEnabled !== '') {
        filtered = filtered.filter((i) => i.isEnabled === Number(isEnabled))
      }
      const start = (page - 1) * limit
      const tlist = filtered.slice(start, start + limit)
      return {
        code: 1,
        msg: 'success',
        data: { tlist, totalRecord: filtered.length },
      }
    },
  },

  // 新增印鉴档案
  {
    url: '/qqsk/financial/basicConfig/sealArchive/add',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const newItem = {
        ...data,
        id: nextId++,
        deleteFlag: 0,
        createTime: new Date().toLocaleString('zh-CN'),
        updateTime: new Date().toLocaleString('zh-CN'),
      }
      mockList.push(newItem)
      return { code: 1, msg: '新增成功', data: newItem }
    },
  },

  // 更新印鉴档案
  {
    url: '/qqsk/financial/basicConfig/sealArchive/update',
    type: 'post',
    response(req) {
      const data = req.body || {}
      const idx = mockList.findIndex((i) => i.id === data.id)
      if (idx === -1) return { code: 0, msg: '记录不存在', data: null }
      mockList[idx] = {
        ...mockList[idx],
        ...data,
        updateTime: new Date().toLocaleString('zh-CN'),
      }
      return { code: 1, msg: '更新成功', data: mockList[idx] }
    },
  },

  // 删除印鉴档案
  {
    url: '/qqsk/financial/basicConfig/sealArchive/delete',
    type: 'post',
    response(req) {
      const { id } = req.body || {}
      const idx = mockList.findIndex((i) => i.id === id)
      if (idx === -1) return { code: 0, msg: '记录不存在', data: null }
      mockList[idx].deleteFlag = 1
      return { code: 1, msg: '删除成功', data: null }
    },
  },

  // 查询印鉴档案详情
  {
    url: '/qqsk/financial/basicConfig/sealArchive/detail',
    type: 'get',
    response(req) {
      const id = Number(req.query && req.query.id)
      const item = mockList.find((i) => i.id === id && i.deleteFlag === 0)
      if (!item) return { code: 0, msg: '记录不存在', data: null }
      return { code: 1, msg: 'success', data: item }
    },
  },
]
