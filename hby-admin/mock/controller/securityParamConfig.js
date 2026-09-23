/**
 * @description 安全参数配置 mock 接口 - 支持 CRUD + 状态更新
 */

let mockList = [
  {
    id: 1,
    paramCode: 'SEC_TOKEN_EXPIRE',
    paramName: 'Token过期时间',
    paramType: 'SECURITY',
    paramValue: '7200',
    description: '系统Token过期时间，单位秒',
    isEnabled: 1,
    createTime: '2024-01-01 09:00:00',
    updateTime: '2024-06-15 10:30:00',
  },
  {
    id: 2,
    paramCode: 'SYS_MAX_RETRY',
    paramName: '最大重试次数',
    paramType: 'SYSTEM',
    paramValue: '3',
    description: '接口调用最大重试次数',
    isEnabled: 1,
    createTime: '2024-01-10 14:00:00',
    updateTime: '2024-05-20 16:00:00',
  },
  {
    id: 3,
    paramCode: 'BIZ_APPROVAL_LEVEL',
    paramName: '审批层级',
    paramType: 'BUSINESS',
    paramValue: '2',
    description: '业务审批所需层级数',
    isEnabled: 1,
    createTime: '2024-02-01 08:00:00',
    updateTime: '2024-07-01 09:00:00',
  },
  {
    id: 4,
    paramCode: 'API_TIMEOUT',
    paramName: '接口超时时间',
    paramType: 'INTERFACE',
    paramValue: '30000',
    description: '外部接口调用超时时间，单位毫秒',
    isEnabled: 0,
    createTime: '2024-03-15 11:00:00',
    updateTime: '2024-08-10 14:30:00',
  },
  {
    id: 5,
    paramCode: 'SEC_PASSWORD_POLICY',
    paramName: '密码策略',
    paramType: 'SECURITY',
    paramValue: 'STRONG',
    description: '用户密码强度策略：WEAK/MEDIUM/STRONG',
    isEnabled: 1,
    createTime: '2024-04-01 10:00:00',
    updateTime: '2024-09-05 11:00:00',
  },
]

let nextId = 6

module.exports = [
  // 分页查询参数配置
  {
    url: '/qqsk/financial/basicConfig/parameters/page',
    type: 'get',
    response(req) {
      const {
        page = 1,
        limit = 20,
        pageNum,
        pageSize,
        paramName,
        paramType,
        isEnabled,
      } = req.query || {}
      const currentPage = Number(page) || Number(pageNum) || 1
      const currentLimit = Number(limit) || Number(pageSize) || 20
      let filtered = [...mockList]
      if (paramName)
        filtered = filtered.filter((i) => i.paramName.includes(paramName))
      if (paramType)
        filtered = filtered.filter((i) => i.paramType === paramType)
      if (isEnabled !== undefined && isEnabled !== null && isEnabled !== '') {
        filtered = filtered.filter((i) => i.isEnabled === Number(isEnabled))
      }
      const start = (currentPage - 1) * currentLimit
      const list = filtered.slice(start, start + currentLimit)
      return {
        code: 1,
        msg: 'success',
        data: { tlist: list, totalRecord: filtered.length },
      }
    },
  },

  // 更新参数状态
  {
    url: '/qqsk/financial/basicConfig/parameters/status',
    type: 'put',
    response(req) {
      const { id, isEnabled } = req.body || {}
      const idx = mockList.findIndex((i) => i.id === Number(id))
      if (idx === -1) return { code: 0, msg: '参数不存在', data: null }
      mockList[idx].isEnabled = Number(isEnabled)
      mockList[idx].updateTime = new Date().toLocaleString('zh-CN')
      return { code: 1, msg: '状态更新成功', data: mockList[idx] }
    },
  },

  // 保存或更新参数配置
  {
    url: '/qqsk/financial/basicConfig/parameters/save-or-update',
    type: 'post',
    response(req) {
      const data = req.body || {}
      if (data.id) {
        // 更新
        const idx = mockList.findIndex((i) => i.id === Number(data.id))
        if (idx === -1) return { code: 0, msg: '参数不存在', data: null }
        mockList[idx] = {
          ...mockList[idx],
          ...data,
          updateTime: new Date().toLocaleString('zh-CN'),
        }
        return { code: 1, msg: '更新成功', data: mockList[idx] }
      }
      // 新增
      const newItem = {
        ...data,
        id: nextId++,
        isEnabled: data.isEnabled !== undefined ? Number(data.isEnabled) : 1,
        createTime: new Date().toLocaleString('zh-CN'),
        updateTime: new Date().toLocaleString('zh-CN'),
      }
      mockList.push(newItem)
      return { code: 1, msg: '创建成功', data: newItem }
    },
  },

  // 删除参数配置（通过URL路径中的id）
  {
    url: '/qqsk/financial/basicConfig/parameters/[0-9]+',
    type: 'delete',
    response(req) {
      const urlParts = req.path.split('/')
      const id = Number(urlParts[urlParts.length - 1])
      const idx = mockList.findIndex((i) => i.id === id)
      if (idx === -1) return { code: 0, msg: '参数不存在', data: null }
      mockList.splice(idx, 1)
      return { code: 1, msg: '删除成功', data: null }
    },
  },
]
