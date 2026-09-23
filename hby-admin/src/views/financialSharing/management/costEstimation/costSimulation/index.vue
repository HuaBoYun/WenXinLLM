<template>
  <div class="cost-simulation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          成本模拟计算
        </h1>
        <p class="page-description">基于不同参数和场景进行成本模拟计算和预测分析</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-cpu" @click="handleStartSimulation">
          开始模拟
        </el-button>
        <el-button type="success" icon="el-icon-view" @click="handleViewHistory">
          历史记录
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExportResult">
          导出结果
        </el-button>
      </div>
    </div>

    <!-- 模拟参数配置 -->
    <div class="simulation-config">
      <el-card class="config-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-setting"></i> 模拟参数配置</span>
          <el-button type="text" @click="handleResetConfig">重置配置</el-button>
        </div>
        
        <el-form :model="configForm" :rules="configRules" ref="configForm" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="估算方案" prop="schemeId">
                <el-select
                  v-model="configForm.schemeId"
                  placeholder="请选择估算方案"
                  style="width: 100%"
                  @change="handleSchemeChange"
                >
                  <el-option
                    v-for="scheme in schemeOptions"
                    :key="scheme.schemeId"
                    :label="scheme.schemeName"
                    :value="scheme.schemeId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="模拟期间" prop="simulationPeriod">
                <el-date-picker
                  v-model="configForm.simulationPeriod"
                  type="month"
                  placeholder="选择模拟期间"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="模拟数量" prop="simulationQuantity">
                <el-input-number
                  v-model="configForm.simulationQuantity"
                  :min="1"
                  :max="999999"
                  placeholder="请输入模拟数量"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="材料成本率" prop="materialCostRate">
                <el-input-number
                  v-model="configForm.materialCostRate"
                  :min="0"
                  :max="100"
                  :precision="2"
                  placeholder="材料成本占比(%)"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="人工成本率" prop="laborCostRate">
                <el-input-number
                  v-model="configForm.laborCostRate"
                  :min="0"
                  :max="100"
                  :precision="2"
                  placeholder="人工成本占比(%)"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="制造费用率" prop="overheadCostRate">
                <el-input-number
                  v-model="configForm.overheadCostRate"
                  :min="0"
                  :max="100"
                  :precision="2"
                  placeholder="制造费用占比(%)"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="模拟场景" prop="simulationScenarios">
            <el-checkbox-group v-model="configForm.simulationScenarios">
              <el-checkbox label="optimistic">乐观场景</el-checkbox>
              <el-checkbox label="normal">正常场景</el-checkbox>
              <el-checkbox label="pessimistic">悲观场景</el-checkbox>
              <el-checkbox label="custom">自定义场景</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="备注说明" prop="remark">
            <el-input
              v-model="configForm.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入模拟说明或备注"
            />
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 模拟结果展示 -->
    <div class="simulation-result" v-if="simulationResult">
      <el-card class="result-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-pie-chart"></i> 模拟结果</span>
          <div class="result-actions">
            <el-tag :type="getResultStatusType(simulationResult.status)">
              {{ simulationResult.statusName }}
            </el-tag>
            <el-button type="text" @click="handleSaveResult">保存结果</el-button>
          </div>
        </div>

        <!-- 结果概览 -->
        <div class="result-overview">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="overview-item">
                <div class="item-value">{{ formatCurrency(simulationResult.totalCost) }}</div>
                <div class="item-label">总成本</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-item">
                <div class="item-value">{{ formatCurrency(simulationResult.unitCost) }}</div>
                <div class="item-label">单位成本</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-item">
                <div class="item-value">{{ simulationResult.simulationQuantity }}</div>
                <div class="item-label">模拟数量</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-item">
                <div class="item-value">{{ simulationResult.simulationPeriod }}</div>
                <div class="item-label">模拟期间</div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 成本构成分析 -->
        <div class="cost-composition">
          <h4>成本构成分析</h4>
          <el-table :data="simulationResult.costBreakdown" border stripe>
            <el-table-column prop="costType" label="成本类型" width="150" />
            <el-table-column prop="amount" label="金额" width="150" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="percentage" label="占比" width="100" align="right">
              <template slot-scope="scope">
                {{ scope.row.percentage }}%
              </template>
            </el-table-column>
            <el-table-column prop="unitCost" label="单位成本" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.unitCost) }}
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 场景对比分析 -->
        <div class="scenario-comparison" v-if="simulationResult.scenarioResults">
          <h4>场景对比分析</h4>
          <el-table :data="simulationResult.scenarioResults" border stripe>
            <el-table-column prop="scenarioName" label="场景" width="120" />
            <el-table-column prop="totalCost" label="总成本" width="150" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.totalCost) }}
              </template>
            </el-table-column>
            <el-table-column prop="unitCost" label="单位成本" width="150" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.unitCost) }}
              </template>
            </el-table-column>
            <el-table-column prop="variance" label="差异" width="120" align="right">
              <template slot-scope="scope">
                <span :class="scope.row.variance >= 0 ? 'positive-variance' : 'negative-variance'">
                  {{ scope.row.variance >= 0 ? '+' : '' }}{{ scope.row.variance }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
              <template slot-scope="scope">
                <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
                  {{ scope.row.riskLevelName }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="recommendation" label="建议" min-width="200" />
          </el-table>
        </div>
      </el-card>
    </div>

    <!-- 历史记录对话框 -->
    <el-dialog
      title="模拟历史记录"
      :visible.sync="historyDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <el-table :data="historyData" border stripe v-loading="historyLoading">
        <el-table-column prop="simulationId" label="模拟ID" width="120" />
        <el-table-column prop="schemeName" label="估算方案" width="150" />
        <el-table-column prop="simulationPeriod" label="模拟期间" width="120" />
        <el-table-column prop="totalCost" label="总成本" width="150" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.totalCost) }}
          </template>
        </el-table-column>
        <el-table-column prop="statusName" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getResultStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleViewHistoryDetail(scope.row)">
              查看详情
            </el-button>
            <el-button size="mini" type="text" @click="handleLoadHistory(scope.row)">
              加载配置
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCostEstimateSchemeList, executeCostSimulation, getCostSimulationPage, saveOrUpdateCostSimulation } from '@/api/financialSharing/costEstimation'
import { normalizeKeys, normalizeKeysArray } from '@/utils/keyNormalize'

export default {
  name: 'CostSimulation',
  data() {
    return {
      schemeOptions: [],
      simulationResult: null,
      historyDialogVisible: false,
      historyData: [],
      historyLoading: false,
      configForm: {
        schemeId: null,
        simulationPeriod: '',
        simulationQuantity: 1000,
        materialCostRate: 60.00,
        laborCostRate: 25.00,
        overheadCostRate: 15.00,
        simulationScenarios: ['normal'],
        remark: ''
      },
      configRules: {
        schemeId: [
          { required: true, message: '请选择估算方案', trigger: 'change' }
        ],
        simulationPeriod: [
          { required: true, message: '请选择模拟期间', trigger: 'change' }
        ],
        simulationQuantity: [
          { required: true, message: '请输入模拟数量', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadSchemeOptions()
  },
  methods: {
    // 加载方案选项
    async loadSchemeOptions() {
      try {
        // 仅拉取启用中的方案，给下拉用足够大
        const response = await getCostEstimateSchemeList({ pageNumber: 1, pageSize: 200, isEnabled: 1 })
        if (response && response.code === 1) {
          const dataObj = response.data || {}
          const rawList = dataObj.tlist || dataObj.list || dataObj.records || (Array.isArray(dataObj) ? dataObj : [])
          // 后端可能返回 ALLCAPS（达梦默认列名风格），统一归一化到 camelCase
          this.schemeOptions = normalizeKeysArray(rawList)
        }
      } catch (error) {
        this.$message.error('加载方案选项失败：' + (error.message || ''))
      }
    },

    // 方案变化
    handleSchemeChange(schemeId) {
      // 根据方案带出默认参数：找到选中的方案对象，把 schemeName 留作冗余字段供后端落库
      const picked = this.schemeOptions.find(s => s.schemeId === schemeId)
      if (picked) {
        this.configForm.schemeName = picked.schemeName
        this.configForm.schemeCode = picked.schemeCode
      }
    },

    // 开始模拟
    async handleStartSimulation() {
      try {
        await this.$refs.configForm.validate()

        const response = await executeCostSimulation(this.configForm)
        if (response && response.code === 1) {
          // 后端可能返回 ALLCAPS 字段，统一归一化让结果区域取值不为空
          const raw = response.data || {}
          this.simulationResult = (Array.isArray(raw) ? raw[0] : raw)
          if (this.simulationResult && typeof this.simulationResult === 'object') {
            this.simulationResult = normalizeKeys(this.simulationResult)
          }
          this.$message.success('模拟计算完成')
        } else {
          this.$message.error(response.msg || '模拟计算失败')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('模拟计算失败：' + (error.message || ''))
        }
      }
    },

    // 查看历史记录
    async handleViewHistory() {
      this.historyDialogVisible = true
      this.historyLoading = true
      try {
        const response = await getCostSimulationPage({ pageNumber: 1, pageSize: 20 })
        if (response && response.code === 1) {
          const dataObj = response.data || {}
          const rawList = dataObj.tlist || dataObj.list || dataObj.records || []
          this.historyData = normalizeKeysArray(rawList)
        }
      } catch (error) {
        this.$message.error('获取历史记录失败：' + (error.message || ''))
      } finally {
        this.historyLoading = false
      }
    },

    // 查看历史详情
    handleViewHistoryDetail(row) {
      this.$alert(
        `<div>
          <p><strong>模拟ID：</strong>${row.simulationId || ''}</p>
          <p><strong>方案名称：</strong>${row.schemeName || ''}</p>
          <p><strong>模拟期间：</strong>${row.simulationPeriod || ''}</p>
          <p><strong>总成本：</strong>${this.formatCurrency(row.totalCost)}</p>
          <p><strong>状态：</strong>${row.statusName || ''}</p>
          <p><strong>创建时间：</strong>${row.createTime || ''}</p>
        </div>`,
        '模拟详情',
        { dangerouslyUseHTMLString: true }
      )
    },

    // 加载历史配置
    handleLoadHistory(row) {
      if (row.schemeId) {
        this.configForm.schemeId = row.schemeId
      }
      if (row.simulationPeriod) {
        this.configForm.simulationPeriod = row.simulationPeriod
      }
      this.$message.success('配置已加载')
      this.historyDialogVisible = false
    },

    // 导出结果
    handleExportResult() {
      if (!this.simulationResult) {
        this.$message.warning('请先进行模拟计算')
        return
      }
      const dataStr = JSON.stringify(this.simulationResult, null, 2)
      const blob = new Blob([dataStr], { type: 'application/json' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '成本模拟结果_' + new Date().toISOString().slice(0, 10) + '.json'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      this.$message.success('导出成功')
    },

    // 保存结果
    async handleSaveResult() {
      try {
        const response = await saveOrUpdateCostSimulation(this.simulationResult)
        if (response && response.code === 1) {
          this.$message.success('结果已保存')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + (error.message || ''))
      }
    },

    // 重置配置
    handleResetConfig() {
      this.$refs.configForm.resetFields()
      this.simulationResult = null
    },

    // 格式化货币
    formatCurrency(amount) {
      if (amount == null) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 获取结果状态类型
    getResultStatusType(status) {
      const statusMap = {
        1: 'warning', // 进行中
        2: 'success', // 已完成
        3: 'danger'   // 失败
      }
      return statusMap[status] || 'info'
    },

    // 获取风险等级类型
    getRiskLevelType(level) {
      const levelMap = {
        1: 'success', // 低风险
        2: 'warning', // 中风险
        3: 'danger'   // 高风险
      }
      return levelMap[level] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-simulation-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .header-right {
    .el-button {
      margin-left: 10px;
    }
  }
}

.simulation-config {
  margin-bottom: 20px;

  .config-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      span {
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
          color: #409eff;
        }
      }
    }
  }
}

.simulation-result {
  .result-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      span {
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
          color: #67c23a;
        }
      }

      .result-actions {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }

    .result-overview {
      margin-bottom: 30px;

      .overview-item {
        text-align: center;
        padding: 20px;
        background: #f8f9fa;
        border-radius: 8px;

        .item-value {
          font-size: 24px;
          font-weight: 600;
          color: #409eff;
          margin-bottom: 8px;
        }

        .item-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }

    .cost-composition,
    .scenario-comparison {
      margin-top: 30px;

      h4 {
        margin: 0 0 15px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }
  }
}

.positive-variance {
  color: #f56c6c;
}

.negative-variance {
  color: #67c23a;
}
</style>
