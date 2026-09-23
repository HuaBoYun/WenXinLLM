/**
 * @description 合作伙伴类型管理mock接口
 */
module.exports = [
  // 分页查询合作伙伴类型列表
  {
    url: '/qqsk/financial/partner/type/getList',
    type: 'post',
    response() {
      const mockData = [
        {
          partnerTypeId: 1,
          typeCode: 'BANK',
          typeName: '银行',
          description: '商业银行、政策性银行等金融机构',
          riskLevel: 'AAA',
          sortOrder: 10,
          isEnabled: 1,
          requireApproval: true,
          businessScope: ['PAYMENT', 'LOAN', 'INVESTMENT'],
          remarks: '金融机构类型',
          createTime: '2024-01-15 10:00:00',
          updateTime: '2024-01-15 10:00:00',
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          partnerTypeId: 2,
          typeCode: 'FINANCIAL',
          typeName: '金融机构',
          description: '证券公司、保险公司、基金公司等',
          riskLevel: 'AA',
          sortOrder: 20,
          isEnabled: 1,
          requireApproval: true,
          businessScope: ['INVESTMENT', 'INSURANCE', 'GUARANTEE'],
          remarks: '非银行金融机构',
          createTime: '2024-02-10 14:30:00',
          updateTime: '2024-02-10 14:30:00',
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          partnerTypeId: 3,
          typeCode: 'SUPPLIER',
          typeName: '供应商',
          description: '原材料供应商、设备供应商等',
          riskLevel: 'BBB',
          sortOrder: 30,
          isEnabled: 1,
          requireApproval: false,
          businessScope: ['PAYMENT', 'OTHER'],
          remarks: '供应商类型',
          createTime: '2024-03-05 09:15:00',
          updateTime: '2024-03-05 09:15:00',
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          partnerTypeId: 4,
          typeCode: 'CUSTOMER',
          typeName: '客户',
          description: '企业客户、个人客户等',
          riskLevel: 'A',
          sortOrder: 40,
          isEnabled: 1,
          requireApproval: false,
          businessScope: ['PAYMENT', 'OTHER'],
          remarks: '客户类型',
          createTime: '2024-03-10 16:20:00',
          updateTime: '2024-03-10 16:20:00',
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          orgId: 1,
          orgName: '示例云集团',
        },
      ]

      return {
        code: 1,
        msg: 'success',
        data: {
          tlist: mockData,
          totalRecord: 4,
        },
      }
    },
  },

  // 获取统计信息
  {
    url: '/qqsk/financial/partner/type/getStatistics',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: {
          totalTypes: 4,
          financialTypes: 2,
          businessTypes: 2,
          activeTypes: 4,
        },
      }
    },
  },

  // 获取所有启用的合作伙伴类型
  {
    url: '/qqsk/financial/partner/type/getAllEnabled',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          { partnerTypeId: 1, typeCode: 'BANK', typeName: '银行' },
          { partnerTypeId: 2, typeCode: 'FINANCIAL', typeName: '金融机构' },
          { partnerTypeId: 3, typeCode: 'SUPPLIER', typeName: '供应商' },
          { partnerTypeId: 4, typeCode: 'CUSTOMER', typeName: '客户' },
        ],
      }
    },
  },
]
