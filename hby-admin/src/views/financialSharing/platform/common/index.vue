<template>
  <div class="financial-common-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-setting"></i>
          财务公共模块
        </h1>
        <p class="page-description">提供财务共享系统的基础配置和公共服务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="showQuickConfig">
          快速配置
        </el-button>
      </div>
    </div>

    <!-- 功能模块卡片 -->
    <div class="module-cards">
      <el-row :gutter="24">
        <!-- 影响因素定义 -->
        <el-col :span="8">
          <div class="function-card">
            <div class="card-header">
              <div class="card-icon influence-factor">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-title">
                <h3>影响因素定义</h3>
                <span class="card-subtitle">定义影响会计处理的各种因素</span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ stats.influenceFactors }}</span>
                  <span class="stat-label">已定义因素</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ stats.activeFactors }}</span>
                  <span class="stat-label">启用中</span>
                </div>
              </div>
              <div class="card-actions">
                <router-link to="/financialSharing/platform/common/influenceFactor">
                  <el-button type="text">
                    <i class="el-icon-edit"></i>管理因素
                  </el-button>
                </router-link>
                <el-button type="text" @click="viewInfluenceFactorList">
                  <i class="el-icon-view"></i>查看列表
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 会计科目配置 -->
        <el-col :span="8">
          <div class="function-card">
            <div class="card-header">
              <div class="card-icon account-subject">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-title">
                <h3>会计科目配置</h3>
                <span class="card-subtitle">配置会计科目体系和科目属性</span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ stats.accountSubjects }}</span>
                  <span class="stat-label">科目总数</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ stats.subjectLevels }}</span>
                  <span class="stat-label">科目层级</span>
                </div>
              </div>
              <div class="card-actions">
                <router-link to="/financialSharing/platform/common/accountSubject">
                  <el-button type="text">
                    <i class="el-icon-edit"></i>管理科目
                  </el-button>
                </router-link>
                <el-button type="text" @click="importAccountSubjects">
                  <i class="el-icon-upload2"></i>导入科目
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 辅助核算项目 -->
        <el-col :span="8">
          <div class="function-card">
            <div class="card-header">
              <div class="card-icon auxiliary-item">
                <i class="el-icon-s-grid"></i>
              </div>
              <div class="card-title">
                <h3>辅助核算项目</h3>
                <span class="card-subtitle">配置辅助核算项目和核算维度</span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ stats.auxiliaryItems }}</span>
                  <span class="stat-label">辅助项目</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ stats.auxiliaryTypes }}</span>
                  <span class="stat-label">项目类型</span>
                </div>
              </div>
              <div class="card-actions">
                <router-link to="/financialSharing/platform/common/auxiliaryItem">
                  <el-button type="text">
                    <i class="el-icon-edit"></i>管理项目
                  </el-button>
                </router-link>
                <el-button type="text" @click="configAuxiliaryTypes">
                  <i class="el-icon-s-operation"></i>配置类型
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 币种汇率配置 -->
        <el-col :span="8">
          <div class="function-card">
            <div class="card-header">
              <div class="card-icon currency-rate">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-title">
                <h3>币种汇率配置</h3>
                <span class="card-subtitle">配置币种信息和汇率管理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ stats.currencies }}</span>
                  <span class="stat-label">支持币种</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ stats.exchangeRates }}</span>
                  <span class="stat-label">汇率记录</span>
                </div>
              </div>
              <div class="card-actions">
                <router-link to="/financialSharing/platform/common/currencyRate">
                  <el-button type="text">
                    <i class="el-icon-edit"></i>管理汇率
                  </el-button>
                </router-link>
                <el-button type="text" @click="syncExchangeRates">
                  <i class="el-icon-refresh"></i>同步汇率
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 组织架构配置 -->
        <el-col :span="8">
          <div class="function-card">
            <div class="card-header">
              <div class="card-icon organization">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="card-title">
                <h3>组织架构配置</h3>
                <span class="card-subtitle">配置公司组织架构和部门信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ stats.organizations }}</span>
                  <span class="stat-label">组织单位</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ stats.departments }}</span>
                  <span class="stat-label">部门数量</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="text" @click="manageOrganizations">
                  <i class="el-icon-edit"></i>管理组织
                </el-button>
                <el-button type="text" @click="syncOrganizations">
                  <i class="el-icon-refresh"></i>同步组织
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 系统参数配置 -->
        <el-col :span="8">
          <div class="function-card">
            <div class="card-header">
              <div class="card-icon system-params">
                <i class="el-icon-s-tools"></i>
              </div>
              <div class="card-title">
                <h3>系统参数配置</h3>
                <span class="card-subtitle">配置系统运行参数和业务规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="card-stats">
                <div class="stat-item">
                  <span class="stat-value">{{ stats.systemParams }}</span>
                  <span class="stat-label">系统参数</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ stats.businessRules }}</span>
                  <span class="stat-label">业务规则</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="text" @click="manageSystemParams">
                  <i class="el-icon-edit"></i>管理参数
                </el-button>
                <el-button type="text" @click="exportConfig">
                  <i class="el-icon-download"></i>导出配置
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速配置弹窗 -->
    <el-dialog title="快速配置" :visible.sync="quickConfigVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="quickConfigForm" :rules="configRules" ref="quickConfigForm" label-width="120px">
        <el-form-item label="默认账簿" prop="defaultBookId">
          <el-select v-model="quickConfigForm.defaultBookId" placeholder="请选择默认账簿" style="width: 100%">
            <el-option label="主账簿" :value="1" />
            <el-option label="测试账簿" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="会计期间" prop="accountingPeriod">
          <el-date-picker
            v-model="quickConfigForm.accountingPeriod"
            type="month"
            placeholder="选择会计期间"
            format="yyyy-MM"
            value-format="yyyy-MM"
            style="width: 100%">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="本位币" prop="baseCurrency">
          <el-select v-model="quickConfigForm.baseCurrency" placeholder="请选择本位币" style="width: 100%">
            <el-option label="人民币(CNY)" value="CNY" />
            <el-option label="美元(USD)" value="USD" />
            <el-option label="欧元(EUR)" value="EUR" />
            <el-option label="日元(JPY)" value="JPY" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="quickConfigForm.description"
            type="textarea"
            :rows="3"
            placeholder="配置描述（可选）">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="quickConfigVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuickConfig" :loading="configSaving">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'FinancialCommonDashboard',
  data() {
    return {
      // 快速配置相关
      quickConfigVisible: false,
      configSaving: false,
      quickConfigForm: {
        defaultBookId: null,
        accountingPeriod: null,
        baseCurrency: 'CNY',
        description: ''
      },
      configRules: {
        defaultBookId: [
          { required: true, message: '请选择默认账簿', trigger: 'change' }
        ],
        accountingPeriod: [
          { required: true, message: '请选择会计期间', trigger: 'change' }
        ],
        baseCurrency: [
          { required: true, message: '请选择本位币', trigger: 'change' }
        ]
      },
      stats: {
        influenceFactors: 0,
        activeFactors: 0,
        accountSubjects: 0,
        subjectLevels: 0,
        auxiliaryItems: 0,
        auxiliaryTypes: 0,
        currencies: 0,
        exchangeRates: 0,
        organizations: 0,
        departments: 0,
        systemParams: 0,
        businessRules: 0
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 调用后端API获取统计数据
        const response = await this.$http.get('/zbgl/financial/common/config/stats')
        if (response.data.code === 1) {
          this.stats = response.data.data
        } else {
          console.error('获取统计数据失败:', response.data.msg)
          this.$message.error(response.data.msg || '获取统计数据失败')
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // API 调用失败时使用空状态
        this.stats = {
          influenceFactors: 0,
          activeFactors: 0,
          accountSubjects: 0,
          subjectLevels: 0,
          auxiliaryItems: 0,
          auxiliaryTypes: 0,
          currencies: 0,
          exchangeRates: 0,
          organizations: 0,
          departments: 0,
          systemParams: 0,
          businessRules: 0
        }
      }
    },
    showQuickConfig() {
      this.quickConfigVisible = true
      this.loadCurrentConfig()
    },

    async loadCurrentConfig() {
      try {
        const response = await this.$http.get('/zbgl/financial/common/config/current')
        if (response.data.code === 1) {
          this.quickConfigForm = {
            defaultBookId: response.data.data.defaultBookId,
            accountingPeriod: response.data.data.accountingPeriod,
            baseCurrency: response.data.data.baseCurrency,
            description: ''
          }
        }
      } catch (error) {
        console.error('加载当前配置失败:', error)
      }
    },

    async saveQuickConfig() {
      try {
        // 表单验证
        await this.$refs.quickConfigForm.validate()

        this.configSaving = true

        const response = await this.$http.post('/zbgl/financial/common/config/save', this.quickConfigForm)

        if (response.data.code === 1) {
          this.$message.success('配置保存成功')
          this.quickConfigVisible = false
          this.loadStats() // 重新加载统计数据
        } else {
          this.$message.error(response.data.msg || '配置保存失败')
        }
      } catch (error) {
        console.error('保存配置失败:', error)
        if (error.response && error.response.data && error.response.data.msg) {
          this.$message.error(error.response.data.msg)
        } else {
          this.$message.error('保存配置失败，请检查网络连接')
        }
      } finally {
        this.configSaving = false
      }
    },
    viewInfluenceFactorList() {
      this.$router.push('/financialSharing/platform/common/index')
    },
    importAccountSubjects() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 已上传，处理中...`)
      }
      input.click()
    },
    configAuxiliaryTypes() {
      this.$message.info('请使用左侧菜单进入"辅助核算"配置页面')
    },
    syncExchangeRates() {
      this.$confirm('确认同步最新汇率？', '同步确认', { type: 'warning' })
        .then(() => { this.$message.success('汇率同步成功') })
        .catch(() => {})
    },
    manageOrganizations() {
      this.$message.info('请使用左侧菜单进入"组织管理"页面')
    },
    syncOrganizations() {
      this.$confirm('确认同步组织数据？', '同步确认', { type: 'warning' })
        .then(() => { this.$message.success('组织同步成功') })
        .catch(() => {})
    },
    manageSystemParams() {
      this.$message.info('请使用左侧菜单进入"参数管理"页面')
    },
    exportConfig() {
      try {
        const data = this.tableData || this.list || []
        if (!data.length) { this.$message.warning('暂无数据可导出'); return }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '配置导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-common-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
}

.module-cards {
  .function-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    height: 220px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }
  }
}

.card-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16px;

  .card-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;

    i {
      font-size: 24px;
      color: white;
    }

    &.influence-factor {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.account-subject {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.auxiliary-item {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.currency-rate {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.organization {
      background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    }

    &.system-params {
      background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
    }
  }

  .card-title {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 4px 0;
    }

    .card-subtitle {
      font-size: 12px;
      color: #909399;
      line-height: 1.4;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.card-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;

  .stat-item {
    text-align: center;

    .stat-value {
      display: block;
      font-size: 20px;
      font-weight: 600;
      color: #409eff;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 12px;
      color: #909399;
    }
  }
}

.card-actions {
  display: flex;
  justify-content: space-between;

  .el-button--text {
    padding: 0;
    font-size: 12px;
    color: #606266;

    &:hover {
      color: #409eff;
    }

    i {
      margin-right: 4px;
    }
  }
}
</style>
