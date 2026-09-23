/**
 * @description 电票账户配置 mock 接口
 */

const mockList = [
  {
    id: 1,
    accountCode: 'ETA001',
    accountName: '工行电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'ICBC',
    accountNumber: '6222021234567890001',
    balanceLimit: 10000000,
    dailyLimit: 5000000,
    createTime: '2024-01-01 09:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 2,
    accountCode: 'ETA002',
    accountName: '建行电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'CCB',
    accountNumber: '6222031234567890002',
    balanceLimit: 8000000,
    dailyLimit: 4000000,
    createTime: '2024-01-15 10:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 3,
    accountCode: 'ETA003',
    accountName: '农行电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'PERSONAL',
    bankCode: 'ABC',
    accountNumber: '6222041234567890003',
    balanceLimit: 5000000,
    dailyLimit: 2000000,
    createTime: '2024-02-01 11:00:00',
    accountStatus: 'FROZEN',
  },
  {
    id: 4,
    accountCode: 'ETA004',
    accountName: '中行电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'BOC',
    accountNumber: '6222051234567890004',
    balanceLimit: 12000000,
    dailyLimit: 6000000,
    createTime: '2024-02-15 14:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 5,
    accountCode: 'ETA005',
    accountName: '交行电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'BOCOM',
    accountNumber: '6222061234567890005',
    balanceLimit: 6000000,
    dailyLimit: 3000000,
    createTime: '2024-03-01 08:00:00',
    accountStatus: 'INACTIVE',
  },
  {
    id: 6,
    accountCode: 'ETA006',
    accountName: '招行电票账户',
    eTicketSystem: 'SWIFT',
    accountType: 'ENTERPRISE',
    bankCode: 'CMB',
    accountNumber: '6222071234567890006',
    balanceLimit: 15000000,
    dailyLimit: 8000000,
    createTime: '2024-01-20 09:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 7,
    accountCode: 'ETA007',
    accountName: '浦发电票账户',
    eTicketSystem: 'SWIFT',
    accountType: 'PERSONAL',
    bankCode: 'SPDB',
    accountNumber: '6222081234567890007',
    balanceLimit: 3000000,
    dailyLimit: 1500000,
    createTime: '2024-02-10 15:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 8,
    accountCode: 'ETA008',
    accountName: '民生电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'CMBC',
    accountNumber: '6222091234567890008',
    balanceLimit: 7000000,
    dailyLimit: 3500000,
    createTime: '2024-02-20 10:00:00',
    accountStatus: 'FROZEN',
  },
  {
    id: 9,
    accountCode: 'ETA009',
    accountName: '兴业电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'CIB',
    accountNumber: '6222101234567890009',
    balanceLimit: 9000000,
    dailyLimit: 4500000,
    createTime: '2024-02-25 11:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 10,
    accountCode: 'ETA010',
    accountName: '光大电票账户',
    eTicketSystem: 'SWIFT',
    accountType: 'ENTERPRISE',
    bankCode: 'CEB',
    accountNumber: '6222111234567890010',
    balanceLimit: 4000000,
    dailyLimit: 2000000,
    createTime: '2024-03-01 09:00:00',
    accountStatus: 'ACTIVE',
  },
  {
    id: 11,
    accountCode: 'ETA011',
    accountName: '华夏电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'PERSONAL',
    bankCode: 'HXB',
    accountNumber: '6222121234567890011',
    balanceLimit: 2000000,
    dailyLimit: 1000000,
    createTime: '2024-02-28 10:00:00',
    accountStatus: 'INACTIVE',
  },
  {
    id: 12,
    accountCode: 'ETA012',
    accountName: '平安电票账户',
    eTicketSystem: 'ECDS',
    accountType: 'ENTERPRISE',
    bankCode: 'PAB',
    accountNumber: '6222131234567890012',
    balanceLimit: 11000000,
    dailyLimit: 5500000,
    createTime: '2024-01-25 13:00:00',
    accountStatus: 'ACTIVE',
  },
]

module.exports = [
  {
    url: '/qqsk/financial/basicConfig/eTicketAccount/list',
    type: 'post',
    response(req) {
      const {
        page = 1,
        limit = 20,
        accountCode,
        accountName,
        eTicketSystem,
        accountType,
        accountStatus,
      } = req.body || {}
      let filtered = [...mockList]
      if (accountCode)
        filtered = filtered.filter((i) => i.accountCode.includes(accountCode))
      if (accountName)
        filtered = filtered.filter((i) => i.accountName.includes(accountName))
      if (eTicketSystem)
        filtered = filtered.filter((i) => i.eTicketSystem === eTicketSystem)
      if (accountType)
        filtered = filtered.filter((i) => i.accountType === accountType)
      if (accountStatus)
        filtered = filtered.filter((i) => i.accountStatus === accountStatus)
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
