<template>
  <div class="variance-analysis-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-warning"></i>
          成本差异分析
        </h1>
        <p class="page-description">分析实际成本与预算成本的差异，识别成本控制关键点</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-search" @click="handleAnalyze">
          开始分析
        </el-button>
        <el-button type="success" icon="el-icon-view" @click="handleViewReport">
          查看报告
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">
          导出分析
        </el-button>
      </div>
    </div>

    <!-- 分析条件 -->
    <div class="analysis-conditions">
      <el-card class="condition-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-filter"></i> 分析条件</span>
          <el-button type="text" @click="handleResetConditions">重置条件</el-button>
        </div>
        
        <el-form :model="conditionForm" :rules="conditionRules" ref="conditionForm" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="分析期间" prop="analysisPeriod">
                <el-date-picker
                  v-model="conditionForm.analysisPeriod"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始月份"
                  end-placeholder="结束月份"
                  format="yyyy-MM"
                  value-format="yyyy-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="成本中心" prop="costCenterIds">
                <el-select
                  v-model="conditionForm.costCenterIds"
                  multiple
                  placeholder="请选择成本中心"
                  style="width: 100%"
                >
                  <el-option
                    v-for="center in costCenterOptions"
                    :key="center.centerId"
                    :label="center.centerName"
                    :value="center.centerId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="产品类别" prop="productCategoryIds">
                <el-select
                  v-model="conditionForm.productCategoryIds"
                  multiple
                  placeholder="请选择产品类别"
                  style="width: 100%"
                >
                  <el-option
                    v-for="category in productCategoryOptions"
                    :key="category.categoryId"
                    :label="category.categoryName"
                    :value="category.categoryId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="差异类型" prop="varianceTypes">
                <el-checkbox-group v-model="conditionForm.varianceTypes">
                  <el-checkbox label="material">材料差异</el-checkbox>
                  <el-checkbox label="labor">人工差异</el-checkbox>
                  <el-checkbox label="overhead">制造费用差异</el-checkbox>
                  <el-checkbox label="total">总成本差异</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="差异阈值" prop="varianceThreshold">
                <el-input-number
                  v-model="conditionForm.varianceThreshold"
                  :min="0"
                  :max="100"
                  :precision="2"
                  placeholder="差异百分比阈值"
                  style="width: 100%"
                />
                <span style="margin-left: 8px; color: #909399;">%</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="分析维度" prop="analysisDimensions">
                <el-select
                  v-model="conditionForm.analysisDimensions"
                  multiple
                  placeholder="请选择分析维度"
                  style="width: 100%"
                >
                  <el-option label="按时间" value="time" />
                  <el-option label="按产品" value="product" />
                  <el-option label="按成本中心" value="costCenter" />
                  <el-option label="按成本类型" value="costType" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>

    <!-- 分析结果 -->
    <div class="analysis-result" v-if="analysisResult">
      <!-- 差异概览 -->
      <el-card class="overview-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-pie-chart"></i> 差异概览</span>
          <el-tag :type="getOverallVarianceType(analysisResult.overallVariance)">
            总体差异: {{ analysisResult.overallVariance }}%
          </el-tag>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-item">
              <div class="item-value" :class="getVarianceClass(analysisResult.materialVariance)">
                {{ analysisResult.materialVariance }}%
              </div>
              <div class="item-label">材料成本差异</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="item-value" :class="getVarianceClass(analysisResult.laborVariance)">
                {{ analysisResult.laborVariance }}%
              </div>
              <div class="item-label">人工成本差异</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="item-value" :class="getVarianceClass(analysisResult.overheadVariance)">
                {{ analysisResult.overheadVariance }}%
              </div>
              <div class="item-label">制造费用差异</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="item-value">{{ analysisResult.analyzedPeriods }}</div>
              <div class="item-label">分析期间数</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 详细差异分析 -->
      <el-card class="detail-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-data-line"></i> 详细差异分析</span>
          <div class="header-actions">
            <el-radio-group v-model="detailViewType" size="small">
              <el-radio-button label="table">表格视图</el-radio-button>
              <el-radio-button label="chart">图表视图</el-radio-button>
            </el-radio-group>
          </div>
        </div>

        <!-- 表格视图 -->
        <div v-if="detailViewType === 'table'">
          <el-table :data="analysisResult.detailData" border stripe>
            <el-table-column prop="period" label="期间" width="100" />
            <el-table-column prop="costCenterName" label="成本中心" width="150" />
            <el-table-column prop="productName" label="产品" width="150" />
            <el-table-column prop="budgetCost" label="预算成本" width="120" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.budgetCost) }}
              </template>
            </el-table-column>
            <el-table-column prop="actualCost" label="实际成本" width="120" align="right">
              <template slot-scope="scope">
                {{ formatCurrency(scope.row.actualCost) }}
              </template>
            </el-table-column>
            <el-table-column prop="variance" label="差异金额" width="120" align="right">
              <template slot-scope="scope">
                <span :class="getVarianceClass(scope.row.varianceRate)">
                  {{ formatCurrency(scope.row.variance) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="varianceRate" label="差异率" width="100" align="right">
              <template slot-scope="scope">
                <span :class="getVarianceClass(scope.row.varianceRate)">
                  {{ scope.row.varianceRate }}%
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
            <el-table-column prop="reason" label="差异原因" min-width="200" show-overflow-tooltip />
            <el-table-column label="操作" width="120" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleViewDetail(scope.row)">
                  详情
                </el-button>
                <el-button size="mini" type="text" @click="handleCreateAction(scope.row)">
                  制定措施
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 图表视图 -->
        <div v-if="detailViewType === 'chart'" class="chart-container">
          <div id="varianceChart" style="width: 100%; height: 400px;"></div>
        </div>
      </el-card>

      <!-- 差异原因分析 -->
      <el-card class="reason-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-question"></i> 差异原因分析</span>
        </div>
        
        <el-table :data="analysisResult.reasonAnalysis" border stripe>
          <el-table-column prop="reasonCategory" label="原因类别" width="120" />
          <el-table-column prop="reasonDescription" label="原因描述" min-width="200" />
          <el-table-column prop="impactAmount" label="影响金额" width="120" align="right">
            <template slot-scope="scope">
              {{ formatCurrency(scope.row.impactAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="impactPercentage" label="影响占比" width="100" align="right">
            <template slot-scope="scope">
              {{ scope.row.impactPercentage }}%
            </template>
          </el-table-column>
          <el-table-column prop="frequency" label="发生频次" width="100" align="center" />
          <el-table-column prop="recommendation" label="改进建议" min-width="200" />
        </el-table>
      </el-card>

      <!-- 改进措施建议 -->
      <el-card class="improvement-card" shadow="hover">
        <div slot="header" class="card-header">
          <span><i class="el-icon-magic-stick"></i> 改进措施建议</span>
        </div>
        
        <div class="improvement-list">
          <div
            v-for="(improvement, index) in analysisResult.improvements"
            :key="index"
            class="improvement-item"
          >
            <div class="improvement-header">
              <el-tag :type="getPriorityType(improvement.priority)">
                {{ improvement.priorityName }}
              </el-tag>
              <span class="improvement-title">{{ improvement.title }}</span>
            </div>
            <div class="improvement-content">
              <p>{{ improvement.description }}</p>
              <div class="improvement-meta">
                <span>预期效果: {{ improvement.expectedEffect }}</span>
                <span>实施周期: {{ improvement.implementationPeriod }}</span>
                <span>责任部门: {{ improvement.responsibleDepartment }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 详情对话框 -->
    <el-dialog
      title="差异详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="期间">{{ selectedDetail.period }}</el-descriptions-item>
          <el-descriptions-item label="成本中心">{{ selectedDetail.costCenterName }}</el-descriptions-item>
          <el-descriptions-item label="产品">{{ selectedDetail.productName }}</el-descriptions-item>
          <el-descriptions-item label="预算成本">{{ formatCurrency(selectedDetail.budgetCost) }}</el-descriptions-item>
          <el-descriptions-item label="实际成本">{{ formatCurrency(selectedDetail.actualCost) }}</el-descriptions-item>
          <el-descriptions-item label="差异金额">{{ formatCurrency(selectedDetail.variance) }}</el-descriptions-item>
          <el-descriptions-item label="差异率">{{ selectedDetail.varianceRate }}%</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskLevelType(selectedDetail.riskLevel)">
              {{ selectedDetail.riskLevelName }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div style="margin-top: 20px;">
          <h4>差异原因分析</h4>
          <p>{{ selectedDetail.reason }}</p>
        </div>
        
        <div style="margin-top: 20px;">
          <h4>建议措施</h4>
          <p>{{ selectedDetail.recommendation }}</p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  executeVarianceAnalysis,
  getVarianceAnalysisPage,
  exportCostEstimateDataBlob
} from '@/api/financialSharing/costEstimation'
import { getCostCenterPage } from '@/api/financialSharing/costCenter'
import { normalizeKeysArray } from '@/utils/keyNormalize'

export default {
  name: 'VarianceAnalysis',
  data() {
    return {
      costCenterOptions: [],
      productCategoryOptions: [],
      analysisResult: null,
      detailViewType: 'table',
      detailDialogVisible: false,
      selectedDetail: null,
      conditionForm: {
        analysisPeriod: [],
        costCenterIds: [],
        productCategoryIds: [],
        varianceTypes: ['material', 'labor', 'overhead', 'total'],
        varianceThreshold: 5.00,
        analysisDimensions: ['time', 'product']
      },
      conditionRules: {
        analysisPeriod: [
          { required: true, message: '请选择分析期间', trigger: 'change' }
        ],
        varianceTypes: [
          { required: true, message: '请选择差异类型', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadOptions()
  },
  methods: {
    // 加载选项数据
    async loadOptions() {
      // 成本中心：复用 /cwgxAi/ma/costcenter/getList，pageSize 拉到 1000 当全量字典用
      // 后端无 /options 端点，曾经的 getCostCenterOptions 调用直接 404,所以列表永远是空
      try {
        const response = await getCostCenterPage({ pageNumber: 1, pageSize: 1000, isEnabled: 1 })
        if (response && response.code === 1) {
          const dataObj = response.data || {}
          const rawList = dataObj.tlist || dataObj.list || dataObj.records || (Array.isArray(dataObj) ? dataObj : [])
          // VO 已是 camelCase（centerId/centerName/centerCode），归一化做双保险
          this.costCenterOptions = normalizeKeysArray(rawList)
        }
      } catch (error) {
        console.error('加载成本中心失败:', error)
      }

      // 产品类别：后端尚未建产品主数据表，先用通用业务枚举兜底
      // 待后端落地 TBL_PRODUCT_CATEGORY 后切到对应接口即可，无需改模板
      this.productCategoryOptions = [
        { categoryId: 'RAW_MATERIAL',     categoryName: '原材料' },
        { categoryId: 'SEMI_PRODUCT',     categoryName: '半成品' },
        { categoryId: 'FINISHED_PRODUCT', categoryName: '产成品' },
        { categoryId: 'SERVICE',          categoryName: '服务类' },
        { categoryId: 'OTHER',            categoryName: '其他' }
      ]
    },

    // 开始分析
    async handleAnalyze() {
      try {
        await this.$refs.conditionForm.validate()

        // 后端 executeVarianceAnalysis 用 (String) 强转读字段，
        // 前端控件天然是数组（monthrange / multiple select / checkbox-group），
        // 不做整形会触发 ClassCastException。这里把数组拍成后端期望的标量：
        // 1) analysisPeriod: ["2025-01","2025-06"] -> "2025-01~2025-06"
        // 2) costCenterIds / productCategoryIds / varianceTypes / analysisDimensions
        //    -> 逗号分隔字符串；空数组 -> null
        // 3) varianceTypes 同时挂一份单数 varianceType 兼容后端旧字段
        const f = this.conditionForm
        const join = (arr) => Array.isArray(arr) && arr.length ? arr.join(',') : null
        const periodStr = Array.isArray(f.analysisPeriod) && f.analysisPeriod.length === 2
          ? `${f.analysisPeriod[0]}~${f.analysisPeriod[1]}`
          : (f.analysisPeriod || null)
        const payload = {
          analysisPeriod: periodStr,
          costCenterIds: join(f.costCenterIds),
          productCategoryIds: join(f.productCategoryIds),
          varianceTypes: join(f.varianceTypes),
          varianceType: join(f.varianceTypes),
          analysisDimensions: join(f.analysisDimensions),
          varianceThreshold: f.varianceThreshold
        }

        const response = await executeVarianceAnalysis(payload)

        if (response && (response.code === 1 || response.code === 200)) {
          this.analysisResult = response.data || {}
          this.$message.success('差异分析完成')
        } else {
          this.$message.error(response.msg || '分析失败，请稍后重试')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('分析失败：' + (error.message || ''))
        }
      }
    },

    // 查看详情
    handleViewDetail(row) {
      this.selectedDetail = row
      this.detailDialogVisible = true
    },

    // 制定措施
    handleCreateAction(row) {
      const recommendation = row.recommendation || '暂无建议措施'
      this.$alert(
        `【${row.costCenterName || ''} - ${row.productName || ''}】\n\n` +
        `差异原因：${row.reason || '未知'}\n` +
        `差异率：${row.varianceRate}%\n` +
        `风险等级：${row.riskLevelName || '未知'}\n\n` +
        `建议措施：${recommendation}`,
        '制定改进措施',
        { confirmButtonText: '确定', type: 'info' }
      )
    },

    // 查看报告
    handleViewReport() {
      if (!this.analysisResult) {
        this.$message.warning('请先进行差异分析')
        return
      }
      const result = this.analysisResult
      this.$alert(
        `总体差异：${result.overallVariance || 0}%\n` +
        `材料成本差异：${result.materialVariance || 0}%\n` +
        `人工成本差异：${result.laborVariance || 0}%\n` +
        `制造费用差异：${result.overheadVariance || 0}%\n` +
        `分析期间数：${result.analyzedPeriods || 0}`,
        '差异分析结果摘要',
        { confirmButtonText: '确定', type: 'info' }
      )
    },

    // 导出分析
    async handleExport() {
      if (!this.analysisResult) {
        this.$message.warning('请先进行差异分析')
        return
      }
      try {
        const response = await exportCostEstimateDataBlob(this.conditionForm)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '成本差异分析.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    // 重置条件
    handleResetConditions() {
      this.$refs.conditionForm.resetFields()
      this.analysisResult = null
    },

    // 格式化货币
    formatCurrency(amount) {
      if (amount == null) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 获取差异样式类
    getVarianceClass(variance) {
      if (variance > 0) return 'positive-variance'
      if (variance < 0) return 'negative-variance'
      return ''
    },

    // 获取总体差异类型
    getOverallVarianceType(variance) {
      if (Math.abs(variance) <= 5) return 'success'
      if (Math.abs(variance) <= 10) return 'warning'
      return 'danger'
    },

    // 获取风险等级类型
    getRiskLevelType(level) {
      const levelMap = {
        1: 'success', // 低风险
        2: 'warning', // 中风险
        3: 'danger'   // 高风险
      }
      return levelMap[level] || 'info'
    },

    // 获取优先级类型
    getPriorityType(priority) {
      const priorityMap = {
        1: 'danger',  // 高优先级
        2: 'warning', // 中优先级
        3: 'info'     // 低优先级
      }
      return priorityMap[priority] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.variance-analysis-container {
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
        color: #e6a23c;
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

.analysis-conditions {
  margin-bottom: 20px;

  .condition-card {
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

.analysis-result {
  .overview-card,
  .detail-card,
  .reason-card,
  .improvement-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      span {
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
        }
      }

      .header-actions {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }
  }

  .overview-card {
    .overview-item {
      text-align: center;
      padding: 20px;
      background: #f8f9fa;
      border-radius: 8px;

      .item-value {
        font-size: 24px;
        font-weight: 600;
        margin-bottom: 8px;

        &.positive-variance {
          color: #f56c6c;
        }

        &.negative-variance {
          color: #67c23a;
        }
      }

      .item-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }

  .improvement-card {
    .improvement-list {
      .improvement-item {
        margin-bottom: 20px;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .improvement-header {
          display: flex;
          align-items: center;
          margin-bottom: 10px;

          .improvement-title {
            margin-left: 10px;
            font-weight: 600;
            color: #303133;
          }
        }

        .improvement-content {
          p {
            margin: 0 0 10px 0;
            color: #606266;
          }

          .improvement-meta {
            display: flex;
            gap: 20px;
            font-size: 12px;
            color: #909399;
          }
        }
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

.chart-container {
  padding: 20px 0;
}
</style>
