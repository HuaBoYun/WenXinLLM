/**
 * @description 合作伙伴档案管理mock接口
 */
module.exports = [
  // 分页查询合作伙伴档案列表
  {
    url: '/qqsk/financial/xjgl/partnerDirectConnection/partner/archive/getList',
    type: 'post',
    response() {
      const mockData = [
        {
          partnerId: 1,
          partnerCode: 'P2025001',
          partnerName: '中国工商银行股份有限公司',
          partnerNameEng: 'Industrial and Commercial Bank of China',
          partnerTypeId: 1,
          partnerTypeName: '银行',
          unifiedCreditCode: '91110000100000001X',
          legalPerson: '陈四清',
          contactPerson: '王经理',
          contactPhone: '010-66108888',
          creditRating: 'AAA',
          riskLevel: 'LOW',
          partnerStatus: 'ACTIVE',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-01-15 10:00:00',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          partnerId: 2,
          partnerCode: 'P2025002',
          partnerName: '中国建设银行股份有限公司',
          partnerNameEng: 'China Construction Bank',
          partnerTypeId: 1,
          partnerTypeName: '银行',
          unifiedCreditCode: '91110000100000002X',
          legalPerson: '田国立',
          contactPerson: '李经理',
          contactPhone: '010-67598888',
          creditRating: 'AAA',
          riskLevel: 'LOW',
          partnerStatus: 'ACTIVE',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-02-20 14:30:00',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          partnerId: 3,
          partnerCode: 'P2025003',
          partnerName: '华为技术有限公司',
          partnerNameEng: 'Huawei Technologies Co., Ltd.',
          partnerTypeId: 2,
          partnerTypeName: '供应商',
          unifiedCreditCode: '91440300100000003X',
          legalPerson: '任正非',
          contactPerson: '张经理',
          contactPhone: '0755-28780888',
          creditRating: 'AA',
          riskLevel: 'MEDIUM',
          partnerStatus: 'ACTIVE',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-03-10 09:15:00',
          orgId: 1,
          orgName: '示例云集团',
        },
      ]

      return {
        code: 1,
        msg: 'success',
        data: {
          tlist: mockData,
          totalRecord: 3,
        },
      }
    },
  },

  // 获取合作伙伴统计数据
  {
    url: '/qqsk/financial/xjgl/partnerDirectConnection/partner/archive/statistics',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: {
          totalPartners: 3,
          bankPartners: 2,
          supplierPartners: 1,
          activePartners: 3,
        },
      }
    },
  },

  // 获取伙伴类型列表
  {
    url: '/qqsk/financial/xjgl/partnerDirectConnection/partner/type/getList',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          {
            partnerTypeId: 1,
            typeCode: 'BANK',
            typeName: '银行',
            isEnabled: 1,
          },
          {
            partnerTypeId: 2,
            typeCode: 'SUPPLIER',
            typeName: '供应商',
            isEnabled: 1,
          },
          {
            partnerTypeId: 3,
            typeCode: 'CUSTOMER',
            typeName: '客户',
            isEnabled: 1,
          },
          {
            partnerTypeId: 4,
            typeCode: 'FINANCIAL',
            typeName: '金融机构',
            isEnabled: 1,
          },
        ],
      }
    },
  },
]
