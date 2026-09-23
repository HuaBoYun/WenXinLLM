<template>
  <div class="cost-management-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>成本管理概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          刷新
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalCost || 0 }}</div>
              <div class="stat-label">总成本（万元）</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.directCost || 0 }}</div>
              <div class="stat-label">直接成本（万元）</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);">
              <i class="el-icon-wallet"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.indirectCost || 0 }}</div>
              <div class="stat-label">间接成本（万元）</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #F56C6C 0%, #f78989 100%);">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.controlRate || 0 }}%</div>
              <div class="stat-label">成本控制率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card class="table-card">
      <div slot="header">
        <span>成本明细</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="addCost">新增成本</el-button>
          <el-button type="success" size="small" @click="exportData">导出数据</el-button>
        </div>
      </div>
      
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="成本类型">
          <el-select v-model="queryForm.costType" placeholder="请选择成本类型" clearable>
            <el-option label="直接成本" value="direct"></el-option>
            <el-option label="间接成本" value="indirect"></el-option>
            <el-option label="固定成本" value="fixed"></el-option>
            <el-option label="变动成本" value="variable"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="costList" border v-loading="loading">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="costName" label="成本项目" width="200"></el-table-column>
        <el-table-column prop="costType" label="成本类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getCostTypeTag(scope.row.costType)">
              {{ getCostTypeText(scope.row.costType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="costAmount" label="金额" width="150" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.costAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="budgetAmount" label="预算金额" width="150" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.budgetAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="varianceAmount" label="差异" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getVarianceClass(scope.row.varianceAmount)">
              {{ formatAmount(scope.row.varianceAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="归属部门" width="120"></el-table-column>
        <el-table-column prop="reportPeriod" label="期间" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetail(scope.row)">
              详情
            </el-button>
            <el-button type="text" size="small" @click="editCost(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="handleAnalyzeCost(scope.row)">
              分析
            </el-button>
            <el-button type="text" size="small" @click="deleteCost(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right;"
      ></el-pagination>
    </el-card>

    <!-- 新增/编辑成本对话框 -->
    <el-dialog :title="dialogType === 'add' ? '新增成本' : '编辑成本'" :visible.sync="costDialogVisible" width="600px">
      <el-form :model="costForm" ref="costForm" label-width="100px" :rules="costRules">
        <el-form-item label="成本项目" prop="costName">
          <el-input v-model="costForm.costName" placeholder="请输入成本项目名称"></el-input>
        </el-form-item>
        <el-form-item label="成本类型" prop="costType">
          <el-select v-model="costForm.costType" placeholder="请选择成本类型" style="width: 100%;">
            <el-option label="直接成本" value="direct"></el-option>
            <el-option label="间接成本" value="indirect"></el-option>
            <el-option label="固定成本" value="fixed"></el-option>
            <el-option label="变动成本" value="variable"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成本金额" prop="costAmount">
          <el-input-number v-model="costForm.costAmount" :precision="2" :min="0" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="预算金额" prop="budgetAmount">
          <el-input-number v-model="costForm.budgetAmount" :precision="2" :min="0" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="归属部门" prop="department">
          <el-input v-model="costForm.department" placeholder="请输入归属部门"></el-input>
        </el-form-item>
        <el-form-item label="报告期间" prop="reportPeriod">
          <el-input v-model="costForm.reportPeriod" placeholder="如：2025-01"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="costForm.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="costDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCostForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 成本详情对话框 -->
    <el-dialog title="成本详情" :visible.sync="detailDialogVisible" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="成本项目">{{ currentRow.costName }}</el-descriptions-item>
        <el-descriptions-item label="成本类型">{{ getCostTypeText(currentRow.costType) }}</el-descriptions-item>
        <el-descriptions-item label="成本金额">{{ formatAmount(currentRow.costAmount) }}</el-descriptions-item>
        <el-descriptions-item label="预算金额">{{ formatAmount(currentRow.budgetAmount) }}</el-descriptions-item>
        <el-descriptions-item label="差异金额">{{ formatAmount(currentRow.varianceAmount) }}</el-descriptions-item>
        <el-descriptions-item label="差异率">{{ currentRow.varianceRate }}%</el-descriptions-item>
        <el-descriptions-item label="归属部门">{{ currentRow.department }}</el-descriptions-item>
        <el-descriptions-item label="报告期间">{{ currentRow.reportPeriod }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusText(currentRow.status) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 成本分析对话框 -->
    <el-dialog title="成本分析" :visible.sync="analysisDialogVisible" width="650px">
      <div v-if="analysisRow">
        <el-descriptions :column="2" border style="margin-bottom: 20px;">
          <el-descriptions-item label="成本项目">{{ analysisRow.costName }}</el-descriptions-item>
          <el-descriptions-item label="成本类型">{{ getCostTypeText(analysisRow.costType) }}</el-descriptions-item>
          <el-descriptions-item label="归属部门">{{ analysisRow.department }}</el-descriptions-item>
          <el-descriptions-item label="报告期间">{{ analysisRow.reportPeriod }}</el-descriptions-item>
        </el-descriptions>

        <div class="analysis-section">
          <h4 style="margin-bottom: 12px;">预算执行对比</h4>
          <div class="analysis-bar-group">
            <div class="analysis-bar-item">
              <span class="analysis-bar-label">实际成本</span>
              <el-progress
                :percentage="analysisRow.budgetAmount > 0 ? Math.min(((analysisRow.costAmount / analysisRow.budgetAmount) * 100), 100) : 0"
                :color="analysisRow.costAmount > analysisRow.budgetAmount ? '#F56C6C' : '#67C23A'"
                :stroke-width="20"
                :text-inside="true"
                :format="() => formatAmount(analysisRow.costAmount)"
              ></el-progress>
            </div>
            <div class="analysis-bar-item">
              <span class="analysis-bar-label">预算金额</span>
              <el-progress
                :percentage="100"
                color="#409EFF"
                :stroke-width="20"
                :text-inside="true"
                :format="() => formatAmount(analysisRow.budgetAmount)"
              ></el-progress>
            </div>
          </div>
        </div>

        <div class="analysis-section">
          <h4 style="margin-bottom: 12px;">差异分析</h4>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="analysis-metric">
                <div class="analysis-metric-label">差异金额</div>
                <div :class="['analysis-metric-value', analysisRow.varianceAmount > 0 ? 'variance-over' : 'variance-under']">
                  {{ formatAmount(analysisRow.varianceAmount) }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-metric">
                <div class="analysis-metric-label">差异率</div>
                <div :class="['analysis-metric-value', analysisRow.varianceAmount > 0 ? 'variance-over' : 'variance-under']">
                  {{ analysisRow.varianceRate || 0 }}%
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="analysis-metric">
                <div class="analysis-metric-label">执行状态</div>
                <div class="analysis-metric-value">
                  <el-tag :type="getStatusType(analysisRow.status)">{{ getStatusText(analysisRow.status) }}</el-tag>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="analysis-section">
          <h4 style="margin-bottom: 12px;">分析结论</h4>
          <el-alert
            :title="getAnalysisConclusion(analysisRow).title"
            :type="getAnalysisConclusion(analysisRow).type"
            :description="getAnalysisConclusion(analysisRow).description"
            show-icon
            :closable="false"
          ></el-alert>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="analysisDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostAccountingList,
  addCostAccounting,
  updateCostAccounting,
  deleteCostAccounting,
  analyzeCost
} from '@/api/enterprise/financial'

export default {
  name: 'CostManagementTab',
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      costList: [],
      total: 0,
      queryForm: {
        costType: '',
        dateRange: [],
        pageNumber: 1,
        pageSize: 10
      },
      overview: {
        totalCost: 0,
        directCost: 0,
        indirectCost: 0,
        controlRate: 0
      },
      // 对话框
      costDialogVisible: false,
      detailDialogVisible: false,
      analysisDialogVisible: false,
      dialogType: 'add',
      currentRow: {},
      analysisRow: null,
      costForm: {
        costName: '',
        costType: '',
        costAmount: 0,
        budgetAmount: 0,
        department: '',
        reportPeriod: '',
        reportYear: '',
        remark: ''
      },
      costRules: {
        costName: [{ required: true, message: '请输入成本项目名称', trigger: 'blur' }],
        costType: [{ required: true, message: '请选择成本类型', trigger: 'change' }],
        costAmount: [{ required: true, message: '请输入成本金额', trigger: 'blur' }]
      }
    }
  },
  watch: {
    enterpriseId: {
      handler(val) {
        if (val) {
          this.getList()
          this.loadOverview()
        }
      },
      immediate: true
    }
  },
  methods: {
    getList() {
      if (!this.enterpriseId) return
      this.loading = true
      const params = {
        ...this.queryForm,
        enterpriseId: this.enterpriseId
      }
      // 处理时间范围
      if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
        params.createTimeBegin = this.queryForm.dateRange[0]
        params.createTimeEnd = this.queryForm.dateRange[1]
      }
      delete params.dateRange

      getCostAccountingList(params).then(response => {
        const data = response.data || {}
        this.costList = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    loadOverview() {
      if (!this.enterpriseId) return
      analyzeCost({ enterpriseId: this.enterpriseId }).then(response => {
        const data = response.data || {}
        // 后端返回的金额单位是元，需要转换为万元
        // 兼容达梦数据库返回大写key
        const totalCost = data.totalCost || data.TOTALCOST || 0
        const directCost = data.directCost || data.DIRECTCOST || 0
        const indirectCost = data.indirectCost || data.INDIRECTCOST || 0
        const totalBudget = data.totalBudget || data.TOTALBUDGET || 0
        const controlRate = data.controlRate || data.CONTROLRATE || 0
        this.overview = {
          totalCost: totalCost ? (Number(totalCost) / 10000).toFixed(2) : 0,
          directCost: directCost ? (Number(directCost) / 10000).toFixed(2) : 0,
          indirectCost: indirectCost ? (Number(indirectCost) / 10000).toFixed(2) : 0,
          controlRate: totalBudget && Number(totalBudget) > 0 ? ((Number(totalCost) / Number(totalBudget)) * 100).toFixed(1) : (Number(controlRate) || 0)
        }
      }).catch(() => {})
    },

    refreshData() {
      this.getList()
      this.loadOverview()
      this.$message.success('数据刷新成功')
    },

    queryData() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    resetQuery() {
      this.queryForm = {
        costType: '',
        dateRange: [],
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },

    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.getList()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.getList()
    },

    addCost() {
      this.dialogType = 'add'
      this.costForm = {
        costName: '',
        costType: '',
        costAmount: 0,
        budgetAmount: 0,
        department: '',
        reportPeriod: '',
        reportYear: '',
        remark: ''
      }
      this.costDialogVisible = true
    },

    editCost(row) {
      this.dialogType = 'edit'
      this.costForm = { ...row }
      this.costDialogVisible = true
    },

    submitCostForm() {
      this.$refs.costForm.validate(valid => {
        if (!valid) return
        const data = {
          ...this.costForm,
          enterpriseId: this.enterpriseId,
          enterpriseName: this.enterpriseName,
          varianceAmount: this.costForm.costAmount - this.costForm.budgetAmount,
          varianceRate: this.costForm.budgetAmount > 0
            ? (((this.costForm.costAmount - this.costForm.budgetAmount) / this.costForm.budgetAmount) * 100).toFixed(2)
            : 0,
          reportYear: this.costForm.reportPeriod ? this.costForm.reportPeriod.substring(0, 4) : ''
        }
        const api = this.dialogType === 'add' ? addCostAccounting : updateCostAccounting
        api(data).then(() => {
          this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
          this.costDialogVisible = false
          this.getList()
          this.loadOverview()
          this.$emit('refresh')
        }).catch(() => {
          this.$message.error(this.dialogType === 'add' ? '新增失败' : '编辑失败')
        })
      })
    },

    viewDetail(row) {
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    handleAnalyzeCost(row) {
      this.analysisRow = { ...row }
      this.analysisDialogVisible = true
    },

    getAnalysisConclusion(row) {
      if (!row || !row.budgetAmount) {
        return {
          title: '暂无预算数据',
          type: 'info',
          description: '该成本项目未设置预算金额，无法进行预算执行分析。'
        }
      }
      const variance = row.varianceAmount || (row.costAmount - row.budgetAmount)
      const rate = row.varianceRate || (row.budgetAmount > 0 ? ((variance / row.budgetAmount) * 100).toFixed(2) : 0)
      if (variance > 0) {
        return {
          title: '成本超支预警',
          type: 'error',
          description: `"${row.costName}"实际成本超出预算 ${this.formatAmount(Math.abs(variance))}，超支率 ${Math.abs(rate)}%。建议审查该项成本支出，分析超支原因并制定控制措施。`
        }
      } else if (variance < 0) {
        return {
          title: '成本控制良好',
          type: 'success',
          description: `"${row.costName}"实际成本低于预算 ${this.formatAmount(Math.abs(variance))}，节约率 ${Math.abs(rate)}%。成本控制效果良好，建议保持当前管理策略。`
        }
      }
      return {
        title: '预算执行持平',
        type: 'warning',
        description: `"${row.costName}"实际成本与预算持平，建议持续关注后续变动趋势。`
      }
    },

    deleteCost(row) {
      this.$confirm('确认删除该成本项目？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCostAccounting(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
          this.loadOverview()
          this.$emit('refresh')
        }).catch(() => {
          this.$message.error('删除失败')
        })
      })
    },

    exportData() {
      if (!this.costList || this.costList.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const headers = ['成本项目', '成本类型', '成本金额', '预算金额', '差异金额', '差异率', '归属部门', '期间', '状态']
      const rows = this.costList.map(item => [
        item.costName || '',
        this.getCostTypeText(item.costType),
        item.costAmount || 0,
        item.budgetAmount || 0,
        item.varianceAmount || 0,
        (item.varianceRate || 0) + '%',
        item.department || '',
        item.reportPeriod || '',
        this.getStatusText(item.status)
      ])
      let csvContent = headers.join(',') + '\n'
      rows.forEach(row => {
        csvContent += row.map(cell => {
          const str = String(cell)
          // 包含逗号或引号时用双引号包裹
          if (str.includes(',') || str.includes('"') || str.includes('\n')) {
            return '"' + str.replace(/"/g, '""') + '"'
          }
          return str
        }).join(',') + '\n'
      })
      // BOM头确保中文Excel兼容
      const BOM = '\uFEFF'
      const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' })
      const now = new Date()
      const dateStr = now.getFullYear() +
        String(now.getMonth() + 1).padStart(2, '0') +
        String(now.getDate()).padStart(2, '0')
      const fileName = `成本管理_${this.enterpriseName || '企业'}_${dateStr}.csv`
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(link.href)
      this.$message.success('导出成功')
    },

    getCostTypeTag(type) {
      const typeMap = {
        'direct': 'primary',
        'indirect': 'success',
        'fixed': 'warning',
        'variable': 'info',
        'DIRECT': 'primary',
        'INDIRECT': 'success',
        'FIXED': 'warning',
        'VARIABLE': 'info'
      }
      return typeMap[type] || 'info'
    },

    getCostTypeText(type) {
      const textMap = {
        'direct': '直接成本',
        'indirect': '间接成本',
        'fixed': '固定成本',
        'variable': '变动成本',
        'DIRECT': '直接成本',
        'INDIRECT': '间接成本',
        'FIXED': '固定成本',
        'VARIABLE': '变动成本'
      }
      return textMap[type] || type
    },

    getVarianceClass(variance) {
      if (variance > 0) return 'variance-over'
      if (variance < 0) return 'variance-under'
      return ''
    },

    getStatusType(status) {
      const statusMap = {
        '正常': 'success',
        '超支': 'danger',
        '预警': 'warning',
        'NORMAL': 'success',
        'OVER_BUDGET': 'danger',
        'WARNING': 'warning'
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        '正常': '正常',
        '超支': '超支',
        '预警': '预警',
        'NORMAL': '正常',
        'OVER_BUDGET': '超支',
        'WARNING': '预警'
      }
      return textMap[status] || status
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万元'
    }
  }
}
</script>

<style scoped>
.cost-management-tab {
  padding: 20px;
}
.overview-card {
  margin-bottom: 20px;
}
.stat-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #fff;
  margin-right: 12px;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
  min-width: 0;
}
.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
  margin-bottom: 4px;
}
.stat-label {
  font-size: 13px;
  color: #909399;
}
.table-card {
  margin-bottom: 20px;
}
.query-form {
  margin-bottom: 20px;
}
.variance-over {
  color: #F56C6C;
}
.variance-under {
  color: #67C23A;
}
.analysis-section {
  margin-bottom: 24px;
}
.analysis-bar-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.analysis-bar-item {
  display: flex;
  align-items: center;
  gap: 12px;
}
.analysis-bar-label {
  width: 70px;
  font-size: 13px;
  color: #606266;
  flex-shrink: 0;
}
.analysis-metric {
  text-align: center;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
}
.analysis-metric-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}
.analysis-metric-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
</style>
