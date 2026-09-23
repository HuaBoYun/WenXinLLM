/**
 * @description 合作伙伴风险管理mock接口
 */
module.exports = [
  // 分页查询风险评估列表
  {
    url: '/qqsk/financial/partner/risk/getList',
    type: 'post',
    response() {
      const mockData = [
        {
          id: 1,
          partnerId: 1,
          partnerCode: 'P2025001',
          partnerName: '中国工商银行股份有限公司',
          partnerType: 'BANK',
          riskLevel: 'AAA',
          riskScore: 95.5,
          riskStatus: 'NORMAL',
          assessmentType: 'ANNUAL',
          lastAssessmentDate: '2024-12-15 10:00:00',
          nextAssessmentDate: '2025-12-15',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-01-15 10:00:00',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          id: 2,
          partnerId: 2,
          partnerCode: 'P2025002',
          partnerName: '中国建设银行股份有限公司',
          partnerType: 'BANK',
          riskLevel: 'AA',
          riskScore: 88.0,
          riskStatus: 'NORMAL',
          assessmentType: 'PERIODIC',
          lastAssessmentDate: '2024-11-20 14:30:00',
          nextAssessmentDate: '2025-05-20',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-02-20 14:30:00',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          id: 3,
          partnerId: 3,
          partnerCode: 'P2025003',
          partnerName: '华为技术有限公司',
          partnerType: 'SUPPLIER',
          riskLevel: 'BBB',
          riskScore: 72.5,
          riskStatus: 'WATCH',
          assessmentType: 'INITIAL',
          lastAssessmentDate: '2024-10-10 09:15:00',
          nextAssessmentDate: '2025-04-10',
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

  // 获取风险统计信息
  {
    url: '/qqsk/financial/partner/risk/getStatistics',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: {
          totalAssessments: 3,
          highRiskCount: 0,
          mediumRiskCount: 1,
          lowRiskCount: 2,
        },
      }
    },
  },

  // 获取合作伙伴列表
  {
    url: '/qqsk/financial/partner/risk/getPartners',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          {
            partnerId: 1,
            partnerCode: 'P2025001',
            partnerName: '中国工商银行股份有限公司',
          },
          {
            partnerId: 2,
            partnerCode: 'P2025002',
            partnerName: '中国建设银行股份有限公司',
          },
          {
            partnerId: 3,
            partnerCode: 'P2025003',
            partnerName: '华为技术有限公司',
          },
        ],
      }
    },
  },

  // 获取风险等级列表
  {
    url: '/qqsk/financial/partner/risk/getRiskLevels',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          { value: 'AAA', label: 'AAA级' },
          { value: 'AA', label: 'AA级' },
          { value: 'A', label: 'A级' },
          { value: 'BBB', label: 'BBB级' },
          { value: 'BB', label: 'BB级' },
          { value: 'B', label: 'B级' },
          { value: 'C', label: 'C级' },
        ],
      }
    },
  },

  // 获取评估类型列表
  {
    url: '/qqsk/financial/partner/risk/getAssessmentTypes',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          { value: 'INITIAL', label: '初始评估' },
          { value: 'PERIODIC', label: '定期评估' },
          { value: 'TEMPORARY', label: '临时评估' },
          { value: 'ANNUAL', label: '年度评估' },
        ],
      }
    },
  },
]
