/**
 * @description 云连接合同管理mock接口
 */
module.exports = [
  // 分页查询云连接合同列表
  {
    url: '/qqsk/settlement/cloud-contract/page',
    type: 'get',
    response() {
      const mockData = [
        {
          contractId: 1,
          contractNumber: 'CT2025001',
          contractName: '阿里云服务合同',
          serviceProvider: 'ALIYUN',
          serviceType: 'IaaS',
          contractAmount: 500000,
          currency: 'CNY',
          signDate: '2024-01-15',
          effectiveDate: '2024-02-01',
          expiryDate: '2025-02-01',
          contractStatus: 'ACTIVE',
          paymentMethod: 'YEARLY',
          paymentCycle: '年付',
          sla: '99.9%',
          contractFilePath: '/contracts/aliyun.pdf',
          description: '阿里云IaaS服务合同',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-01-15 10:00:00',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          contractId: 2,
          contractNumber: 'CT2025002',
          contractName: '腾讯云服务合同',
          serviceProvider: 'TENCENT',
          serviceType: 'PaaS',
          contractAmount: 300000,
          currency: 'CNY',
          signDate: '2024-03-10',
          effectiveDate: '2024-04-01',
          expiryDate: '2025-04-01',
          contractStatus: 'ACTIVE',
          paymentMethod: 'QUARTERLY',
          paymentCycle: '季付',
          sla: '99.5%',
          contractFilePath: '/contracts/tencent.pdf',
          description: '腾讯云PaaS服务合同',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-03-10 14:30:00',
          orgId: 1,
          orgName: '示例云集团',
        },
        {
          contractId: 3,
          contractNumber: 'CT2025003',
          contractName: '华为云服务合同',
          serviceProvider: 'HUAWEI',
          serviceType: 'IaaS',
          contractAmount: 800000,
          currency: 'CNY',
          signDate: '2024-05-20',
          effectiveDate: '2024-06-01',
          expiryDate: '2025-06-01',
          contractStatus: 'ACTIVE',
          paymentMethod: 'HALF_YEARLY',
          paymentCycle: '半年付',
          sla: '99.95%',
          contractFilePath: '/contracts/huawei.pdf',
          description: '华为云IaaS服务合同',
          isEnabled: 1,
          deleteFlag: 0,
          createdBy: 1,
          createdByName: '管理员',
          createdTime: '2024-05-20 09:15:00',
          orgId: 1,
          orgName: '示例云集团',
        },
      ]

      return {
        code: 1,
        msg: 'success',
        data: {
          rows: mockData,
          total: 3,
        },
      }
    },
  },

  // 获取合同统计数据
  {
    url: '/qqsk/settlement/cloud-contract/statistics',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: {
          totalContracts: 3,
          activeContracts: 3,
          executingContracts: 3,
          expiringContracts: 1,
          totalAmount: 1600000,
        },
      }
    },
  },

  // 获取服务提供商列表
  {
    url: '/qqsk/settlement/cloud-contract/service-providers',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          { value: 'ALIYUN', label: '阿里云' },
          { value: 'TENCENT', label: '腾讯云' },
          { value: 'HUAWEI', label: '华为云' },
          { value: 'AWS', label: 'AWS' },
          { value: 'AZURE', label: 'Azure' },
        ],
      }
    },
  },

  // 获取服务类型列表
  {
    url: '/qqsk/settlement/cloud-contract/service-types',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: [
          { value: 'IaaS', label: '基础设施即服务' },
          { value: 'PaaS', label: '平台即服务' },
          { value: 'SaaS', label: '软件即服务' },
        ],
      }
    },
  },
]
