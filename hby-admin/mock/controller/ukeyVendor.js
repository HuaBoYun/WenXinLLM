/**
 * @description Ukey厂商管理 mock 接口
 */

const mockList = [
  {
    id: 1,
    vendorCode: 'UV001',
    vendorName: '飞天诚信',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'ePass2003, ePass3003',
    contactInfo: '010-82150088',
    createTime: '2024-01-01 09:00:00',
  },
  {
    id: 2,
    vendorCode: 'UV002',
    vendorName: '握奇数据',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'CKey200, CKey500',
    contactInfo: '010-62106688',
    createTime: '2024-01-15 10:00:00',
  },
  {
    id: 3,
    vendorCode: 'UV003',
    vendorName: '天地融',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'TDR-U100, TDR-U200',
    contactInfo: '010-82896060',
    createTime: '2024-02-01 11:00:00',
  },
  {
    id: 4,
    vendorCode: 'UV004',
    vendorName: '明华澳汉',
    vendorType: 'DOMESTIC',
    certificationStatus: 'PENDING',
    cooperationStatus: 'ACTIVE',
    productModels: 'EP-K2, EP-K3',
    contactInfo: '0755-26551688',
    createTime: '2024-02-15 14:00:00',
  },
  {
    id: 5,
    vendorCode: 'UV005',
    vendorName: '海泰方圆',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'INACTIVE',
    productModels: 'HT-K100',
    contactInfo: '010-62669966',
    createTime: '2024-03-01 08:00:00',
  },
  {
    id: 6,
    vendorCode: 'UV006',
    vendorName: '金邦达',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'JBD-U1, JBD-U2',
    contactInfo: '0756-3399888',
    createTime: '2024-01-20 09:00:00',
  },
  {
    id: 7,
    vendorCode: 'UV007',
    vendorName: 'SafeNet',
    vendorType: 'FOREIGN',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'eToken 5110, eToken 5300',
    contactInfo: '+1-800-545-6608',
    createTime: '2024-02-10 15:00:00',
  },
  {
    id: 8,
    vendorCode: 'UV008',
    vendorName: 'Gemalto',
    vendorType: 'FOREIGN',
    certificationStatus: 'PENDING',
    cooperationStatus: 'INACTIVE',
    productModels: 'IDPrime MD 830',
    contactInfo: '+33-1-5501-5000',
    createTime: '2024-02-20 10:00:00',
  },
  {
    id: 9,
    vendorCode: 'UV009',
    vendorName: '龙脉科技',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'mToken K1, mToken K2',
    contactInfo: '010-82896161',
    createTime: '2024-02-25 11:00:00',
  },
  {
    id: 10,
    vendorCode: 'UV010',
    vendorName: '信安世纪',
    vendorType: 'DOMESTIC',
    certificationStatus: 'EXPIRED',
    cooperationStatus: 'INACTIVE',
    productModels: 'SecKey-U100',
    contactInfo: '010-62669977',
    createTime: '2024-03-01 09:00:00',
  },
  {
    id: 11,
    vendorCode: 'UV011',
    vendorName: '渔翁信息',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'YW-U100, YW-U200',
    contactInfo: '0531-88871688',
    createTime: '2024-02-28 10:00:00',
  },
  {
    id: 12,
    vendorCode: 'UV012',
    vendorName: '卫士通',
    vendorType: 'DOMESTIC',
    certificationStatus: 'CERTIFIED',
    cooperationStatus: 'ACTIVE',
    productModels: 'WST-K300',
    contactInfo: '028-85185888',
    createTime: '2024-01-25 13:00:00',
  },
]

module.exports = [
  {
    url: '/qqsk/financial/basicConfig/ukeyVendor/list',
    type: 'get',
    response(req) {
      const {
        page = 1,
        limit = 20,
        vendorName,
        vendorType,
        certificationStatus,
        cooperationStatus,
      } = req.query || {}
      let filtered = [...mockList]
      if (vendorName)
        filtered = filtered.filter((i) => i.vendorName.includes(vendorName))
      if (vendorType)
        filtered = filtered.filter((i) => i.vendorType === vendorType)
      if (certificationStatus)
        filtered = filtered.filter(
          (i) => i.certificationStatus === certificationStatus
        )
      if (cooperationStatus)
        filtered = filtered.filter(
          (i) => i.cooperationStatus === cooperationStatus
        )
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
