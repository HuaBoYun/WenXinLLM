<template>
  <div class="gjjgfx-container">
    <div class="page-header">
      <h2>国际监管分析</h2>
      <p>分析和监控国际监管要求的合规情况</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="监管机构">
          <el-select v-model="searchForm.regulator" placeholder="请选择监管机构" clearable>
            <el-option label="中国人民银行" value="pboc" />
            <el-option label="银保监会" value="cbirc" />
            <el-option label="证监会" value="csrc" />
            <el-option label="外汇管理局" value="safe" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析类型">
          <el-select v-model="searchForm.analysisType" placeholder="请选择分析类型" clearable>
            <el-option label="合规性分析" value="compliance" />
            <el-option label="风险评估" value="risk_assessment" />
            <el-option label="监管指标" value="regulatory_metrics" />
            <el-option label="趋势分析" value="trend_analysis" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 监管指标概览 -->
    <el-row :gutter="20" class="metrics-row">
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">98.5%</div>
            <div class="metric-label">合规率</div>
          </div>
          <i class="el-icon-success metric-icon success"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">15</div>
            <div class="metric-label">待处理事项</div>
          </div>
          <i class="el-icon-warning metric-icon warning"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">3</div>
            <div class="metric-label">风险预警</div>
          </div>
          <i class="el-icon-error metric-icon danger"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="metric-card" shadow="never">
          <div class="metric-content">
            <div class="metric-value">127</div>
            <div class="metric-label">监管报告</div>
          </div>
          <i class="el-icon-document metric-icon info"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card title="合规性趋势分析" shadow="never">
          <div ref="complianceChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="监管指标分布" shadow="never">
          <div ref="metricsChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析结果表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span>监管分析结果</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增分析</el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="analysisName" label="分析名称" min-width="150" />
        <el-table-column prop="regulator" label="监管机构" width="120">
          <template slot-scope="scope">
            <span>{{ getRegulatorName(scope.row.regulator) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="analysisType" label="分析类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAnalysisTypeTag(scope.row.analysisType)">
              {{ getAnalysisTypeName(scope.row.analysisType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complianceScore" label="合规评分" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.complianceScore"
              :color="getScoreColor(scope.row.complianceScore)"
              :show-text="false"
              :stroke-width="8"
            />
            <div style="margin-top: 5px;">{{ scope.row.complianceScore }}%</div>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTag(scope.row.riskLevel)">
              {{ getRiskLevelName(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="analysisDate" label="分析日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleReport(scope.row)">生成报告</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分析名称" prop="analysisName">
              <el-input v-model="form.analysisName" placeholder="请输入分析名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监管机构" prop="regulator">
              <el-select v-model="form.regulator" placeholder="请选择监管机构">
                <el-option label="中国人民银行" value="pboc" />
                <el-option label="银保监会" value="cbirc" />
                <el-option label="证监会" value="csrc" />
                <el-option label="外汇管理局" value="safe" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分析类型" prop="analysisType">
              <el-select v-model="form.analysisType" placeholder="请选择分析类型">
                <el-option label="合规性分析" value="compliance" />
                <el-option label="风险评估" value="risk_assessment" />
                <el-option label="监管指标" value="regulatory_metrics" />
                <el-option label="趋势分析" value="trend_analysis" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分析日期" prop="analysisDate">
              <el-date-picker
                v-model="form.analysisDate"
                type="date"
                placeholder="选择分析日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合规评分" prop="complianceScore">
              <el-input-number
                v-model="form.complianceScore"
                :min="0"
                :max="100"
                :precision="1"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
                <el-option label="低风险" value="low" />
                <el-option label="中风险" value="medium" />
                <el-option label="高风险" value="high" />
                <el-option label="极高风险" value="critical" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="分析描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入分析描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'GjjgfxManage',
  data() {
    return {
      loading: false,
      searchForm: {
        regulator: '',
        analysisType: '',
        dateRange: []
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增监管分析',
      form: {
        analysisName: '',
        regulator: '',
        analysisType: '',
        analysisDate: '',
        complianceScore: 0,
        riskLevel: '',
        description: ''
      },
      rules: {
        analysisName: [
          { required: true, message: '请输入分析名称', trigger: 'blur' }
        ],
        regulator: [
          { required: true, message: '请选择监管机构', trigger: 'change' }
        ],
        analysisType: [
          { required: true, message: '请选择分析类型', trigger: 'change' }
        ],
        analysisDate: [
          { required: true, message: '请选择分析日期', trigger: 'change' }
        ],
        complianceScore: [
          { required: true, message: '请输入合规评分', trigger: 'blur' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  methods: {
    loadData() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.tableData = [
          {
            id: 1,
            analysisName: '2024年第一季度合规性分析',
            regulator: 'pboc',
            analysisType: 'compliance',
            complianceScore: 95.5,
            riskLevel: 'low',
            analysisDate: '2024-04-01',
            status: 'completed'
          }
        ]
        this.pagination.total = 1
        this.loading = false
      }, 1000)
    },
    initCharts() {
      // 初始化图表
      this.$nextTick(() => {
        // 合规性趋势分析图表
        // 监管指标分布图表
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        regulator: '',
        analysisType: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleExport() {
      this.$message.success('导出功能开发中...')
    },
    handleAdd() {
      this.dialogTitle = '新增监管分析'
      this.form = {
        analysisName: '',
        regulator: '',
        analysisType: '',
        analysisDate: '',
        complianceScore: 0,
        riskLevel: '',
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑监管分析'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中...')
    },
    handleReport(row) {
      this.$message.info('生成报告功能开发中...')
    },
    handleDelete(row) {
      this.$confirm('确认删除该监管分析记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    getRegulatorName(regulator) {
      const names = {
        pboc: '中国人民银行',
        cbirc: '银保监会',
        csrc: '证监会',
        safe: '外汇管理局'
      }
      return names[regulator] || regulator
    },
    getAnalysisTypeTag(type) {
      const tags = {
        compliance: 'primary',
        risk_assessment: 'warning',
        regulatory_metrics: 'success',
        trend_analysis: 'info'
      }
      return tags[type] || 'default'
    },
    getAnalysisTypeName(type) {
      const names = {
        compliance: '合规性分析',
        risk_assessment: '风险评估',
        regulatory_metrics: '监管指标',
        trend_analysis: '趋势分析'
      }
      return names[type] || type
    },
    getRiskLevelTag(level) {
      const tags = {
        low: 'success',
        medium: 'warning',
        high: 'danger',
        critical: 'danger'
      }
      return tags[level] || 'default'
    },
    getRiskLevelName(level) {
      const names = {
        low: '低风险',
        medium: '中风险',
        high: '高风险',
        critical: '极高风险'
      }
      return names[level] || level
    },
    getStatusTag(status) {
      const tags = {
        draft: 'info',
        processing: 'warning',
        completed: 'success',
        cancelled: 'danger'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        draft: '草稿',
        processing: '处理中',
        completed: '已完成',
        cancelled: '已取消'
      }
      return names[status] || status
    },
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style scoped>
.gjjgfx-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card {
  margin-bottom: 20px;
}

.metrics-row {
  margin-bottom: 20px;
}

.metric-card {
  position: relative;
  overflow: hidden;
}

.metric-content {
  padding: 20px;
}

.metric-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.metric-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.metric-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 40px;
  opacity: 0.3;
}

.metric-icon.success {
  color: #67C23A;
}

.metric-icon.warning {
  color: #E6A23C;
}

.metric-icon.danger {
  color: #F56C6C;
}

.metric-icon.info {
  color: #409EFF;
}

.chart-row {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
