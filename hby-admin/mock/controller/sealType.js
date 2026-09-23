/**
 * @description 印鉴类型管理 mock 接口
 */

const mockList = [
  {
    id: 1,
    name: '公章',
    code: 'ST001',
    sealLevel: 'HIGH',
    scope: '全公司',
    description: '公司公章，用于对外签署正式文件',
    status: 1,
    createTime: '2024-01-01 09:00:00',
  },
  {
    id: 2,
    name: '财务章',
    code: 'ST002',
    sealLevel: 'HIGH',
    scope: '财务部',
    description: '财务专用章，用于财务相关业务',
    status: 1,
    createTime: '2024-01-15 10:00:00',
  },
  {
    id: 3,
    name: '合同章',
    code: 'ST003',
    sealLevel: 'MEDIUM',
    scope: '全公司',
    description: '合同专用章，用于签署各类合同',
    status: 1,
    createTime: '2024-02-01 11:00:00',
  },
  {
    id: 4,
    name: '法人章',
    code: 'ST004',
    sealLevel: 'HIGH',
    scope: '法务部',
    description: '法定代表人印章',
    status: 1,
    createTime: '2024-02-15 14:00:00',
  },
  {
    id: 5,
    name: '发票章',
    code: 'ST005',
    sealLevel: 'MEDIUM',
    scope: '财务部',
    description: '发票专用章，用于开具发票',
    status: 1,
    createTime: '2024-03-01 08:00:00',
  },
  {
    id: 6,
    name: '人事章',
    code: 'ST006',
    sealLevel: 'LOW',
    scope: '人事部',
    description: '人事部门专用章',
    status: 0,
    createTime: '2024-01-20 09:00:00',
  },
  {
    id: 7,
    name: '部门章',
    code: 'ST007',
    sealLevel: 'LOW',
    scope: '各部门',
    description: '部门内部使用印章',
    status: 1,
    createTime: '2024-02-10 15:00:00',
  },
  {
    id: 8,
    name: '银行预留印鉴',
    code: 'ST008',
    sealLevel: 'HIGH',
    scope: '财务部',
    description: '银行预留印鉴，用于银行业务',
    status: 1,
    createTime: '2024-02-20 10:00:00',
  },
  {
    id: 9,
    name: '电子签章',
    code: 'ST009',
    sealLevel: 'MEDIUM',
    scope: '全公司',
    description: '电子签章，用于电子文件签署',
    status: 1,
    createTime: '2024-02-25 11:00:00',
  },
  {
    id: 10,
    name: '收据章',
    code: 'ST010',
    sealLevel: 'LOW',
    scope: '财务部',
    description: '收据专用章',
    status: 1,
    createTime: '2024-03-01 09:00:00',
  },
  {
    id: 11,
    name: '报关章',
    code: 'ST011',
    sealLevel: 'MEDIUM',
    scope: '进出口部',
    description: '报关专用章，用于海关报关',
    status: 0,
    createTime: '2024-02-28 10:00:00',
  },
  {
    id: 12,
    name: '质检章',
    code: 'ST012',
    sealLevel: 'LOW',
    scope: '质检部',
    description: '质量检验专用章',
    status: 1,
    createTime: '2024-01-25 13:00:00',
  },
]

module.exports = [
  {
    url: '/qqsk/financial/xjgl/basicConfig/seal/type/list',
    type: 'post',
    response(req) {
      const { page = 1, limit = 20, name, sealLevel, status } = req.body || {}
      let filtered = [...mockList]
      if (name) filtered = filtered.filter((i) => i.name.includes(name))
      if (sealLevel)
        filtered = filtered.filter((i) => i.sealLevel === sealLevel)
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
