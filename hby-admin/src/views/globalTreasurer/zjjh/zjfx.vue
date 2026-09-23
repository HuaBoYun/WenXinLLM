<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金计划分析管理</h2>
      <p>管理资金计划的各类分析报告，包括差异分析、趋势分析、风险分析等</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.analysisType"
        placeholder="分析类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in analysisTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.isValid"
        placeholder="有效性"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="有效" :value="1" />
        <el-option label="无效" :value="0" />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="filter-item"
        style="width: 240px"
        @change="handleDateRangeChange"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        新建分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showQualityAnalysis"
      >
        质量分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-trend-charts"
        @click="showTrendAnalysis"
      >
        趋势分析
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">分析总数</div>
          </div>
          <i class="el-icon-data-analysis statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.validCount || 0 }}</div>
            <div class="statistics-label">有效分析</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgQualityScore || 0 }}分</div>
            <div class="statistics-label">平均质量分</div>
          </div>
          <i class="el-icon-star-on statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.highQualityCount || 0 }}</div>
            <div class="statistics-label">高质量分析</div>
          </div>
          <i class="el-icon-trophy statistics-icon"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column
        label="分析编号"
        prop="analysisNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.analysisNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="分析类型"
        prop="analysisType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="analysisTypeTagMap[row.analysisType]">
            {{ analysisTypeMap[row.analysisType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="分析周期"
        prop="analysisPeriod"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.analysisPeriod || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="质量分数"
        prop="qualityScore"
        width="100"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span :class="getQualityScoreClass(row.qualityScore)">
            {{ row.qualityScore || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="有效性"
        prop="isValid"
        width="80"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="row.isValid ? 'success' : 'danger'" size="mini">
            {{ row.isValid ? '有效' : '无效' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="有效期至"
        prop="validUntil"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.validUntil">{{ row.validUntil | parseTime('{y}-{m}-{d}') }}</span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="价值评估"
        prop="valueAssessment"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.valueAssessment" :type="valueTagMap[row.valueAssessment]" size="mini">
            {{ valueMap[row.valueAssessment] }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="模板来源"
        prop="templateId"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.templateId" type="info" size="mini">
            模板生成
          </el-tag>
          <span v-else class="text-muted">手工创建</span>
        </template>
      </el-table-column>
      <el-table-column
        label="分析内容"
        prop="analysisContent"
        width="200"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ getContentSummary(row.analysisContent) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createTime"
        width="160"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="!row.isValid"
            size="mini"
            type="success"
            @click="handleValidate(row)"
          >
            验证
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
          <el-dropdown
            trigger="click"
            @command="(command) => handleCommand(command, row)"
          >
            <el-button size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="detail">查看详情</el-dropdown-item>
              <el-dropdown-item command="copy">复制分析</el-dropdown-item>
              <el-dropdown-item command="export">导出报告</el-dropdown-item>
              <el-dropdown-item command="template">生成模板</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 验证对话框 -->
    <el-dialog
      title="分析验证"
      :visible.sync="validateDialogVisible"
      width="500px"
      @close="resetValidateForm"
    >
      <el-form
        ref="validateForm"
        :model="validateForm"
        :rules="validateRules"
        label-width="100px"
      >
        <el-form-item label="分析编号">
          <el-input v-model="validateForm.analysisNo" disabled />
        </el-form-item>
        <el-form-item label="分析类型">
          <el-input v-model="validateForm.analysisTypeText" disabled />
        </el-form-item>
        <el-form-item label="质量分数" prop="qualityScore">
          <el-input-number
            v-model="validateForm.qualityScore"
            :min="0"
            :max="100"
            :precision="1"
            placeholder="请输入质量分数"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="价值评估" prop="valueAssessment">
          <el-select v-model="validateForm.valueAssessment" placeholder="请选择价值评估" style="width: 100%">
            <el-option label="高价值" value="HIGH" />
            <el-option label="中等价值" value="MEDIUM" />
            <el-option label="低价值" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="有效期至" prop="validUntil">
          <el-date-picker
            v-model="validateForm.validUntil"
            type="date"
            placeholder="选择有效期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="验证说明">
          <el-input
            v-model="validateForm.validateNotes"
            type="textarea"
            :rows="3"
            placeholder="请输入验证说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="validateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmValidate">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanAnalysisPage, createFundPlanAnalysis, updateFundPlanAnalysis, deleteFundPlanAnalysis,
         getFundPlanAnalysisSummary, validateFundPlanAnalysis } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'FundPlanAnalysis',
  components: { Pagination },
  directives: { waves },
  filters: {
    parseTime
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        analysisType: undefined,
        isValid: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-createTime'
      },
      dateRange: [],
      summaryInfo: {},
      validateDialogVisible: false,
      validateForm: {
        analysisId: null,
        analysisNo: '',
        analysisTypeText: '',
        qualityScore: null,
        valueAssessment: '',
        validUntil: null,
        validateNotes: ''
      },
      validateRules: {
        qualityScore: [
          { required: true, message: '请输入质量分数', trigger: 'blur' },
          { type: 'number', min: 0, max: 100, message: '质量分数必须在0-100之间', trigger: 'blur' }
        ],
        valueAssessment: [
          { required: true, message: '请选择价值评估', trigger: 'change' }
        ],
        validUntil: [
          { required: true, message: '请选择有效期', trigger: 'change' }
        ]
      },
      analysisTypeOptions: [
        { label: '差异分析', value: 'VARIANCE' },
        { label: '趋势分析', value: 'TREND' },
        { label: '预测分析', value: 'FORECAST' },
        { label: '风险分析', value: 'RISK' },
        { label: '绩效分析', value: 'PERFORMANCE' },
        { label: '敏感性分析', value: 'SENSITIVITY' }
      ],
      analysisTypeMap: {
        'VARIANCE': '差异分析',
        'TREND': '趋势分析',
        'FORECAST': '预测分析',
        'RISK': '风险分析',
        'PERFORMANCE': '绩效分析',
        'SENSITIVITY': '敏感性分析'
      },
      analysisTypeTagMap: {
        'VARIANCE': 'primary',
        'TREND': 'success',
        'FORECAST': 'info',
        'RISK': 'danger',
        'PERFORMANCE': 'warning',
        'SENSITIVITY': ''
      },
      valueMap: {
        'HIGH': '高价值',
        'MEDIUM': '中等价值',
        'LOW': '低价值'
      },
      valueTagMap: {
        'HIGH': 'success',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
    }
  },
  created() {
    this.getList()
    this.getSummaryInfo()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundPlanAnalysisPage(this.listQuery).then(response => {
        if (response.code === 200) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    getSummaryInfo() {
      getFundPlanAnalysisSummary(this.listQuery).then(response => {
        if (response.code === 200) {
          this.summaryInfo = response.data
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
      this.getSummaryInfo()
    },
    handleDateRangeChange(val) {
      if (val) {
        this.listQuery.startDate = val[0]
        this.listQuery.endDate = val[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'qualityScore') {
        this.sortByQualityScore(order)
      } else if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByQualityScore(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+qualityScore'
      } else {
        this.listQuery.sort = '-qualityScore'
      }
      this.handleFilter()
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+createTime'
      } else {
        this.listQuery.sort = '-createTime'
      }
      this.handleFilter()
    },
    handleCreate() {
      this.$router.push('/globalTreasurer/zjjh/analysis/create')
    },
    handleUpdate(row) {
      this.$router.push(`/globalTreasurer/zjjh/analysis/edit/${row.analysisId}`)
    },
    handleValidate(row) {
      this.validateForm = {
        analysisId: row.analysisId,
        analysisNo: row.analysisNo,
        analysisTypeText: this.analysisTypeMap[row.analysisType],
        qualityScore: row.qualityScore || 80,
        valueAssessment: row.valueAssessment || 'MEDIUM',
        validUntil: row.validUntil || new Date(Date.now() + 90 * 24 * 60 * 60 * 1000), // 默认90天后
        validateNotes: ''
      }
      this.validateDialogVisible = true
    },
    confirmValidate() {
      this.$refs.validateForm.validate(valid => {
        if (valid) {
          const validateData = {
            analysisId: this.validateForm.analysisId,
            qualityScore: this.validateForm.qualityScore,
            valueAssessment: this.validateForm.valueAssessment,
            validUntil: this.validateForm.validUntil,
            isValid: 1,
            validateNotes: this.validateForm.validateNotes
          }

          updateFundPlanAnalysis(validateData).then(response => {
            if (response.code === 200) {
              this.$message.success('验证成功')
              this.validateDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.message || '验证失败')
            }
          })
        }
      })
    },
    resetValidateForm() {
      this.validateForm = {
        analysisId: null,
        analysisNo: '',
        analysisTypeText: '',
        qualityScore: null,
        valueAssessment: '',
        validUntil: null,
        validateNotes: ''
      }
      if (this.$refs.validateForm) {
        this.$refs.validateForm.resetFields()
      }
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该分析记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanAnalysis(row.analysisId).then(response => {
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.list.splice(index, 1)
            this.total--
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyAnalysis(row)
          break
        case 'export':
          this.exportAnalysis(row)
          break
        case 'template':
          this.generateTemplate(row)
          break
      }
    },
    showDetail(row) {
      this.$router.push(`/globalTreasurer/zjjh/analysis/view/${row.analysisId}`)
    },
    copyAnalysis(row) {
      const newAnalysis = { ...row }
      delete newAnalysis.analysisId
      newAnalysis.analysisNo = `${row.analysisNo}_COPY`
      this.$router.push({
        path: '/globalTreasurer/zjjh/analysis/create',
        query: { copyData: JSON.stringify(newAnalysis) }
      })
    },
    exportAnalysis(row) {
      // 导出分析报告
      this.$message.info('导出功能开发中...')
    },
    generateTemplate(row) {
      // 从分析生成模板
      this.$message.info('模板生成功能开发中...')
    },
    showQualityAnalysis() {
      this.$router.push('/globalTreasurer/zjjh/analysis/quality-analysis')
    },
    showTrendAnalysis() {
      this.$router.push('/globalTreasurer/zjjh/analysis/trend-analysis')
    },
    getQualityScoreClass(score) {
      if (score >= 90) return 'text-success'
      if (score >= 80) return 'text-warning'
      if (score >= 70) return 'text-info'
      return 'text-danger'
    },
    getContentSummary(content) {
      if (!content) return '-'
      return content.length > 50 ? content.substring(0, 50) + '...' : content
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;

  h2 {
    margin: 0 0 8px 0;
    color: #303133;
    font-size: 24px;
    font-weight: 500;
  }

  p {
    margin: 0;
    color: #909399;
    font-size: 14px;
  }
}

.filter-container {
  padding: 10px 0;
  margin-bottom: 20px;

  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
    margin-right: 10px;
  }
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  position: relative;
  overflow: hidden;

  .statistics-content {
    padding: 20px;

    .statistics-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }

    .statistics-label {
      font-size: 14px;
      color: #909399;
    }
  }

  .statistics-icon {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 40px;
    color: #E4E7ED;
  }
}

.text-success {
  color: #67C23A !important;
  font-weight: 500;
}

.text-warning {
  color: #E6A23C !important;
  font-weight: 500;
}

.text-info {
  color: #409EFF !important;
  font-weight: 500;
}

.text-danger {
  color: #F56C6C !important;
  font-weight: 500;
}

.text-muted {
  color: #C0C4CC !important;
}

::v-deep .el-table {
  .text-success {
    color: #67C23A;
    font-weight: 500;
  }

  .text-warning {
    color: #E6A23C;
    font-weight: 500;
  }

  .text-info {
    color: #409EFF;
    font-weight: 500;
  }

  .text-danger {
    color: #F56C6C;
    font-weight: 500;
  }

  .text-muted {
    color: #C0C4CC;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
