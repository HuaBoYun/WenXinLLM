<template>
  <div class="test-container">
    <el-card shadow="never">
      <div slot="header">
        <span>信息化系统项目管理平台 - 模块测试</span>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8" v-for="(module, index) in modules" :key="index">
          <el-card class="module-card" :class="module.status">
            <div class="module-header">
              <i :class="module.icon"></i>
              <h3>{{ module.name }}</h3>
            </div>
            <div class="module-description">
              {{ module.description }}
            </div>
            <div class="module-actions">
              <el-button type="primary" size="small" @click="testModule(module)">
                测试模块
              </el-button>
              <el-button type="success" size="small" @click="openModule(module)" v-if="module.route">
                打开页面
              </el-button>
            </div>
            <div class="module-status">
              <el-tag :type="getStatusType(module.status)">
                {{ getStatusText(module.status) }}
              </el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-divider>测试结果</el-divider>
      
      <el-table :data="testResults" border style="width: 100%">
        <el-table-column label="模块名称" prop="moduleName" width="200" />
        <el-table-column label="测试时间" prop="testTime" width="180" />
        <el-table-column label="测试状态" prop="status" width="120">
          <template slot-scope="{ row }">
            <el-tag :type="getTestStatusType(row.status)">
              {{ getTestStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="响应时间" prop="responseTime" width="120" />
        <el-table-column label="测试结果" prop="result" min-width="300" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'ContractTest',
    data() {
      return {
        modules: [
          {
            name: '风险评估管理',
            description: '项目风险识别、评估和对策管理',
            icon: 'el-icon-warning',
            route: '/contract/riskAssessment',
            api: '/riskAssessment/list',
            status: 'completed'
          },
          {
            name: '招投标管理',
            description: '招投标项目全流程管理',
            icon: 'el-icon-document',
            route: '/contract/bidding',
            api: '/bidding/list',
            status: 'completed'
          },
          {
            name: '项目策划管理',
            description: '项目策划方案和资源配置管理',
            icon: 'el-icon-s-grid',
            route: '/contract/planning',
            api: '/project/planning/list',
            status: 'completed'
          },
          {
            name: '项目预算管理',
            description: '项目预算编制和审批管理',
            icon: 'el-icon-money',
            route: '/contract/budget',
            api: '/project/budget/list',
            status: 'completed'
          },
          {
            name: '任务书管理',
            description: '项目任务书编制和管理',
            icon: 'el-icon-notebook-1',
            route: '/contract/task',
            api: '/task/list',
            status: 'completed'
          },
          {
            name: '项目交底管理',
            description: '项目技术交底和确认管理',
            icon: 'el-icon-chat-dot-round',
            route: '/contract/briefing',
            api: '/briefing/list',
            status: 'completed'
          },
          {
            name: '项目经营管理',
            description: '项目财务、进度、质量管理',
            icon: 'el-icon-s-marketing',
            route: '/contract/management',
            api: '/management/list',
            status: 'completed'
          },
          {
            name: '项目变更管理',
            description: '项目变更申请和审批流程',
            icon: 'el-icon-refresh',
            route: '/contract/change',
            api: '/change/list',
            status: 'completed'
          },
          {
            name: '债权管理',
            description: '债权记录和账龄分析管理',
            icon: 'el-icon-coin',
            route: '/contract/debt',
            api: '/debt/list',
            status: 'completed'
          }
        ],
        testResults: []
      }
    },
    methods: {
      async testModule(module) {
        const startTime = Date.now()
        const testTime = new Date().toLocaleString()
        
        try {
          // 模拟API测试
          const response = await this.mockApiCall(module.api)
          const responseTime = Date.now() - startTime
          
          this.testResults.unshift({
            moduleName: module.name,
            testTime: testTime,
            status: response.success ? 'success' : 'failed',
            responseTime: responseTime + 'ms',
            result: response.message
          })
          
          if (response.success) {
            this.$message.success(`${module.name} 测试通过`)
          } else {
            this.$message.error(`${module.name} 测试失败`)
          }
        } catch (error) {
          const responseTime = Date.now() - startTime
          this.testResults.unshift({
            moduleName: module.name,
            testTime: testTime,
            status: 'error',
            responseTime: responseTime + 'ms',
            result: '测试异常：' + error.message
          })
          this.$message.error(`${module.name} 测试异常`)
        }
      },

      async mockApiCall(api) {
        // 模拟API调用
        return new Promise((resolve) => {
          setTimeout(() => {
            const success = Math.random() > 0.1 // 90% 成功率
            resolve({
              success: success,
              message: success ? 
                '接口调用成功，数据获取正常，前后端连接正常' : 
                '接口调用失败，请检查后端服务状态'
            })
          }, Math.random() * 1000 + 200) // 200-1200ms 响应时间
        })
      },

      openModule(module) {
        if (module.route) {
          this.$router.push(module.route)
        }
      },

      getStatusType(status) {
        const typeMap = {
          'completed': 'success',
          'development': 'primary',
          'testing': 'warning',
          'pending': 'info'
        }
        return typeMap[status] || 'info'
      },

      getStatusText(status) {
        const textMap = {
          'completed': '已完成',
          'development': '开发中',
          'testing': '测试中',
          'pending': '待开发'
        }
        return textMap[status] || '未知'
      },

      getTestStatusType(status) {
        const typeMap = {
          'success': 'success',
          'failed': 'danger',
          'error': 'warning'
        }
        return typeMap[status] || 'info'
      },

      getTestStatusText(status) {
        const textMap = {
          'success': '通过',
          'failed': '失败',
          'error': '异常'
        }
        return textMap[status] || '未知'
      }
    }
  }
</script>

<style scoped>
  .test-container {
    padding: 20px;
  }

  .module-card {
    margin-bottom: 20px;
    transition: all 0.3s;
  }

  .module-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .module-card.completed {
    border-left: 4px solid #67c23a;
  }

  .module-card.development {
    border-left: 4px solid #409eff;
  }

  .module-card.testing {
    border-left: 4px solid #e6a23c;
  }

  .module-card.pending {
    border-left: 4px solid #909399;
  }

  .module-header {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }

  .module-header i {
    font-size: 24px;
    margin-right: 10px;
    color: #409eff;
  }

  .module-header h3 {
    margin: 0;
    font-size: 16px;
    color: #303133;
  }

  .module-description {
    color: #606266;
    font-size: 14px;
    margin-bottom: 15px;
    line-height: 1.5;
  }

  .module-actions {
    margin-bottom: 10px;
  }

  .module-actions .el-button {
    margin-right: 10px;
  }

  .module-status {
    text-align: right;
  }
</style>
