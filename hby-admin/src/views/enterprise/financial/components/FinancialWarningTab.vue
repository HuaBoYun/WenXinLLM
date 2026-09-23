<template>
  <div class="financial-warning-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>财务预警概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">
          刷新
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalWarnings || 0 }}</div>
              <div class="stat-label">预警总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #F56C6C 0%, #f78989 100%);">
              <i class="el-icon-error"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.highRiskWarnings || 0 }}</div>
              <div class="stat-label">高风险</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.mediumRiskWarnings || 0 }}</div>
              <div class="stat-label">中风险</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-icon" style="background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.handledWarnings || 0 }}</div>
              <div class="stat-label">已处理</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>预警趋势分析</span>
          </div>
          <div ref="warningTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">
            <span>预警类型分布</span>
          </div>
          <div ref="warningTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="table-card">
      <div slot="header">
        <span>财务预警列表</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="addWarningRule">新增预警规则</el-button>
          <el-button type="success" size="small" @click="batchHandle">批量处理</el-button>
        </div>
      </div>
      
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="预警类型">
          <el-select v-model="queryForm.warningType" placeholder="请选择预警类型" clearable>
            <el-option label="流动性风险" value="liquidity"></el-option>
            <el-option label="偿债能力" value="solvency"></el-option>
            <el-option label="盈利能力" value="profitability"></el-option>
            <el-option label="营运能力" value="operational"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="高风险" value="high"></el-option>
            <el-option label="中风险" value="medium"></el-option>
            <el-option label="低风险" value="low"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="queryForm.status" placeholder="请选择处理状态" clearable>
            <el-option label="待处理" value="pending"></el-option>
            <el-option label="处理中" value="processing"></el-option>
            <el-option label="已处理" value="handled"></el-option>
            <el-option label="已忽略" value="ignored"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="warningList" border v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="warningName" label="预警名称" width="200"></el-table-column>
        <el-table-column prop="warningType" label="预警类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getWarningTypeTag(scope.row.warningType)">
              {{ getWarningTypeText(scope.row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelType(scope.row.riskLevel)">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentValue" label="当前值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatValue(scope.row.currentValue, scope.row.unit) }}
          </template>
        </el-table-column>
        <el-table-column prop="thresholdValue" label="阈值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatValue(scope.row.thresholdValue, scope.row.unit) }}
          </template>
        </el-table-column>
        <el-table-column prop="deviation" label="偏离度" width="100" align="right">
          <template slot-scope="scope">
            <span :class="getDeviationClass(scope.row.deviation)">
              {{ scope.row.deviation }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="triggerTime" label="触发时间" width="150"></el-table-column>
        <el-table-column prop="status" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handler" label="处理人" width="100"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetail(scope.row)">
              详情
            </el-button>
            <el-button type="text" size="small" @click="handleWarningRow(scope.row)" v-if="scope.row.status === 'pending' || scope.row.status === 'processing'">
              处理
            </el-button>
            <el-button type="text" size="small" @click="ignoreWarningRow(scope.row)" v-if="scope.row.status === 'pending'">
              忽略
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

    <!-- 处理预警对话框 -->
    <el-dialog title="处理预警" :visible.sync="handleDialogVisible" width="500px">
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="预警名称">
          <span>{{ currentRow.warningName }}</span>
        </el-form-item>
        <el-form-item label="处理人">
          <el-input v-model="handleForm.handler" placeholder="请输入处理人"></el-input>
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input v-model="handleForm.handleRemark" type="textarea" :rows="3" placeholder="请输入处理说明"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">确认处理</el-button>
      </div>
    </el-dialog>

    <!-- 新增预警规则对话框 -->
    <el-dialog title="新增预警规则" :visible.sync="ruleDialogVisible" width="550px">
      <el-form :model="ruleForm" :rules="ruleRules" ref="ruleFormRef" label-width="100px">
        <el-form-item label="预警名称" prop="warningName">
          <el-input v-model="ruleForm.warningName" placeholder="请输入预警名称"></el-input>
        </el-form-item>
        <el-form-item label="预警类型" prop="warningType">
          <el-select v-model="ruleForm.warningType" placeholder="请选择预警类型" style="width: 100%;">
            <el-option label="流动性风险" value="liquidity"></el-option>
            <el-option label="偿债能力" value="solvency"></el-option>
            <el-option label="盈利能力" value="profitability"></el-option>
            <el-option label="营运能力" value="operational"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="ruleForm.riskLevel" placeholder="请选择风险等级" style="width: 100%;">
            <el-option label="高风险" value="high"></el-option>
            <el-option label="中风险" value="medium"></el-option>
            <el-option label="低风险" value="low"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="阈值" prop="thresholdValue">
          <el-input-number v-model="ruleForm.thresholdValue" :precision="2" :step="1" :min="0" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="ruleForm.unit" placeholder="请选择单位" style="width: 100%;">
            <el-option label="%" value="%"></el-option>
            <el-option label="万元" value="万元"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="ruleForm.remark" type="textarea" :rows="3" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="ruleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRule" :loading="ruleSubmitting">确认新增</el-button>
      </div>
    </el-dialog>

    <!-- 预警详情对话框 -->
    <el-dialog title="预警详情" :visible.sync="detailDialogVisible" width="650px">
      <el-descriptions :column="2" border v-if="detailRow">
        <el-descriptions-item label="预警名称" :span="2">{{ detailRow.warningName }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">
          <el-tag :type="getWarningTypeTag(detailRow.warningType)">
            {{ getWarningTypeText(detailRow.warningType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelType(detailRow.riskLevel)">
            {{ getRiskLevelText(detailRow.riskLevel) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="当前值">{{ formatValue(detailRow.currentValue, detailRow.unit) }}</el-descriptions-item>
        <el-descriptions-item label="阈值">{{ formatValue(detailRow.thresholdValue, detailRow.unit) }}</el-descriptions-item>
        <el-descriptions-item label="偏离度">
          <span :class="getDeviationClass(detailRow.deviation)">{{ detailRow.deviation }}%</span>
        </el-descriptions-item>
        <el-descriptions-item label="触发时间">{{ detailRow.triggerTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getStatusType(detailRow.status)">
            {{ getStatusText(detailRow.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detailRow.handler || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ detailRow.handleTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理备注" :span="2">{{ detailRow.handleRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getFinancialWarningList, handleWarning, setWarningRules, batchHandleWarning, batchIgnoreWarning } from '@/api/enterprise/financial'

export default {
  name: 'FinancialWarningTab',
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
      warningList: [],
      total: 0,
      queryForm: {
        warningType: '',
        riskLevel: '',
        status: '',
        pageNumber: 1,
        pageSize: 10
      },
      overview: {
        totalWarnings: 0,
        highRiskWarnings: 0,
        mediumRiskWarnings: 0,
        handledWarnings: 0
      },
      // 处理对话框
      handleDialogVisible: false,
      currentRow: {},
      handleForm: {
        handler: '',
        handleRemark: ''
      },
      // 新增预警规则对话框
      ruleDialogVisible: false,
      ruleSubmitting: false,
      ruleForm: {
        warningName: '',
        warningType: '',
        riskLevel: '',
        thresholdValue: 0,
        unit: '%',
        remark: ''
      },
      ruleRules: {
        warningName: [{ required: true, message: '请输入预警名称', trigger: 'blur' }],
        warningType: [{ required: true, message: '请选择预警类型', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        thresholdValue: [{ required: true, message: '请输入阈值', trigger: 'blur' }],
        unit: [{ required: true, message: '请选择单位', trigger: 'change' }]
      },
      // 预警详情对话框
      detailDialogVisible: false,
      detailRow: null,
      // 批量选择
      multipleSelection: [],
      // 图表
      trendChart: null,
      typeChart: null
    }
  },
  watch: {
    enterpriseId: {
      handler(val) {
        if (val) {
          this.getList()
          this.loadOverview()
          this.loadCharts()
        }
      },
      immediate: true
    }
  },
  beforeDestroy() {
    if (this.trendChart) { this.trendChart.dispose(); this.trendChart = null }
    if (this.typeChart) { this.typeChart.dispose(); this.typeChart = null }
    window.removeEventListener('resize', this.handleResize)
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  methods: {
    handleResize() {
      if (this.trendChart) this.trendChart.resize()
      if (this.typeChart) this.typeChart.resize()
    },

    getList() {
      if (!this.enterpriseId) return
      this.loading = true
      const params = {
        ...this.queryForm,
        enterpriseId: this.enterpriseId
      }
      getFinancialWarningList(params).then(response => {
        const data = response.data || {}
        this.warningList = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || 0
        this.loading = false
      }).catch((error) => {
        this.loading = false
        console.error('查询财务预警列表失败:', error)
        this.$message.error('查询财务预警列表失败，请检查网络连接')
      })
    },

    loadOverview() {
      if (!this.enterpriseId) return
      // 获取全部数据来计算统计概览
      getFinancialWarningList({ enterpriseId: this.enterpriseId, pageSize: 999 }).then(response => {
        const data = response.data || {}
        const list = data.tlist || data.records || []
        this.overview = {
          totalWarnings: list.length,
          highRiskWarnings: list.filter(w => w.riskLevel === 'high' || w.riskLevel === 'HIGH').length,
          mediumRiskWarnings: list.filter(w => w.riskLevel === 'medium' || w.riskLevel === 'MEDIUM').length,
          handledWarnings: list.filter(w => w.status === 'handled' || w.status === 'ignored').length
        }
      }).catch(() => {})
    },

    loadCharts() {
      if (!this.enterpriseId) return
      // 使用列表数据绘制图表
      getFinancialWarningList({ enterpriseId: this.enterpriseId, pageSize: 999 }).then(response => {
        const data = response.data || {}
        const list = data.tlist || data.records || []
        this.initTrendChart(list)
        this.initTypeChart(list)
      }).catch(() => {
        this.initTrendChart([])
        this.initTypeChart([])
      })
    },

    initTrendChart(list) {
      if (!this.$refs.warningTrendChart) return
      if (this.trendChart) this.trendChart.dispose()
      this.trendChart = echarts.init(this.$refs.warningTrendChart)

      // 按月份统计预警趋势
      const monthMap = {}
      list.forEach(item => {
        const t = item.triggerTime || item.createTime || ''
        const month = t.substring(0, 7)
        if (month) {
          if (!monthMap[month]) monthMap[month] = { total: 0, high: 0, medium: 0, low: 0 }
          monthMap[month].total++
          if (item.riskLevel === 'high') monthMap[month].high++
          else if (item.riskLevel === 'medium') monthMap[month].medium++
          else monthMap[month].low++
        }
      })

      const months = Object.keys(monthMap).sort()
      this.trendChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['高风险', '中风险', '低风险'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: months },
        yAxis: { type: 'value', name: '数量' },
        series: [
          { name: '高风险', type: 'bar', stack: 'total', data: months.map(m => monthMap[m].high), itemStyle: { color: '#F56C6C' } },
          { name: '中风险', type: 'bar', stack: 'total', data: months.map(m => monthMap[m].medium), itemStyle: { color: '#E6A23C' } },
          { name: '低风险', type: 'bar', stack: 'total', data: months.map(m => monthMap[m].low), itemStyle: { color: '#409EFF' } }
        ]
      })
    },

    initTypeChart(list) {
      if (!this.$refs.warningTypeChart) return
      if (this.typeChart) this.typeChart.dispose()
      this.typeChart = echarts.init(this.$refs.warningTypeChart)

      // 按类型统计
      const typeMap = {}
      list.forEach(item => {
        const type = item.warningType || 'unknown'
        if (!typeMap[type]) typeMap[type] = 0
        typeMap[type]++
      })

      const typeTextMap = {
        'liquidity': '流动性风险',
        'solvency': '偿债能力',
        'profitability': '盈利能力',
        'operational': '营运能力',
        'unknown': '其他'
      }
      const colorMap = {
        'liquidity': '#409EFF',
        'solvency': '#67C23A',
        'profitability': '#E6A23C',
        'operational': '#909399',
        'unknown': '#C0C4CC'
      }

      const chartData = Object.keys(typeMap).map(type => ({
        value: typeMap[type],
        name: typeTextMap[type] || type,
        itemStyle: { color: colorMap[type] || '#C0C4CC' }
      }))

      this.typeChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}个 ({d}%)' },
        legend: { orient: 'vertical', left: 'left' },
        series: [
          {
            name: '预警类型',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
            label: { show: true, formatter: '{b}: {d}%' },
            data: chartData
          }
        ]
      })
    },

    refreshData() {
      this.getList()
      this.loadOverview()
      this.loadCharts()
      this.$message.success('数据刷新成功')
    },

    queryData() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    resetQuery() {
      this.queryForm = {
        warningType: '',
        riskLevel: '',
        status: '',
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

    viewDetail(row) {
      this.detailRow = { ...row }
      this.detailDialogVisible = true
    },

    handleWarningRow(row) {
      this.currentRow = { ...row }
      this.handleForm = {
        warningId: row.warningId,
        handler: '',
        handleRemark: ''
      }
      this.handleDialogVisible = true
    },

    submitHandle() {
      if (!this.handleForm.handler) {
        this.$message.warning('请输入处理人')
        return
      }
      handleWarning({
        warningId: this.currentRow.warningId,
        status: 'handled',
        handler: this.handleForm.handler,
        handleRemark: this.handleForm.handleRemark
      }).then(() => {
        this.$message.success('处理成功')
        this.handleDialogVisible = false
        this.getList()
        this.loadOverview()
        this.loadCharts()
      }).catch(() => {
        this.$message.error('处理失败')
      })
    },

    ignoreWarningRow(row) {
      this.$confirm('确认忽略该预警？忽略后将不再提醒。', '忽略预警', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleWarning({
          warningId: row.warningId,
          status: 'ignored',
          handleRemark: '手动忽略'
        }).then(() => {
          this.$message.success('已忽略该预警')
          this.getList()
          this.loadOverview()
          this.loadCharts()
        }).catch(() => {
          this.$message.error('操作失败')
        })
      })
    },

    addWarningRule() {
      this.ruleForm = {
        warningName: '',
        warningType: '',
        riskLevel: '',
        thresholdValue: 0,
        unit: '%',
        remark: ''
      }
      this.ruleDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.ruleFormRef) {
          this.$refs.ruleFormRef.clearValidate()
        }
      })
    },

    submitRule() {
      this.$refs.ruleFormRef.validate(valid => {
        if (!valid) return
        this.ruleSubmitting = true
        setWarningRules({
          enterpriseId: this.enterpriseId,
          warningName: this.ruleForm.warningName,
          warningType: this.ruleForm.warningType,
          riskLevel: this.ruleForm.riskLevel,
          threshold: this.ruleForm.thresholdValue,
          unit: this.ruleForm.unit
        }).then(() => {
          this.$message.success('预警规则添加成功')
          this.ruleDialogVisible = false
          this.ruleSubmitting = false
          this.getList()
          this.loadOverview()
          this.loadCharts()
        }).catch(() => {
          this.$message.error('添加失败')
          this.ruleSubmitting = false
        })
      })
    },

    batchHandle() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message.warning('请先选择要处理的预警')
        return
      }
      this.$confirm(`确认批量处理选中的 ${this.multipleSelection.length} 条预警？`, '批量处理', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        const warningIds = this.multipleSelection.map(item => item.warningId)
        batchHandleWarning({
          warningIds: warningIds,
          handler: '系统管理员',
          handleRemark: '批量处理'
        }).then(() => {
          this.$message.success('批量处理成功')
          this.getList()
          this.loadOverview()
          this.loadCharts()
        }).catch(() => {
          this.$message.error('批量处理失败')
        })
      })
    },

    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    getWarningTypeTag(type) {
      const typeMap = {
        'liquidity': 'primary',
        'solvency': 'success',
        'profitability': 'warning',
        'operational': 'info'
      }
      return typeMap[type] || 'info'
    },

    getWarningTypeText(type) {
      const textMap = {
        'liquidity': '流动性风险',
        'solvency': '偿债能力',
        'profitability': '盈利能力',
        'operational': '营运能力'
      }
      return textMap[type] || type
    },

    getRiskLevelType(level) {
      const levelMap = {
        'high': 'danger',
        'medium': 'warning',
        'low': 'info'
      }
      return levelMap[level] || 'info'
    },

    getRiskLevelText(level) {
      const textMap = {
        'high': '高风险',
        'medium': '中风险',
        'low': '低风险'
      }
      return textMap[level] || level
    },

    getStatusType(status) {
      const statusMap = {
        'pending': 'warning',
        'processing': 'primary',
        'handled': 'success',
        'ignored': 'info'
      }
      return statusMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'pending': '待处理',
        'processing': '处理中',
        'handled': '已处理',
        'ignored': '已忽略'
      }
      return textMap[status] || status
    },

    getDeviationClass(deviation) {
      if (deviation > 0) return 'deviation-over'
      if (deviation < 0) return 'deviation-under'
      return ''
    },

    formatValue(value, unit) {
      if (!value && value !== 0) return '-'
      if (unit === '%') return value + '%'
      return value
    }
  }
}
</script>

<style scoped>
.financial-warning-tab {
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
.chart-card {
  margin-bottom: 20px;
}
.table-card {
  margin-bottom: 20px;
}
.query-form {
  margin-bottom: 20px;
}
.deviation-over {
  color: #F56C6C;
}
.deviation-under {
  color: #67C23A;
}
</style>
