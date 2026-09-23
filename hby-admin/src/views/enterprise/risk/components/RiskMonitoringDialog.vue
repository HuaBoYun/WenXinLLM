<template>
  <el-dialog title="风险监控" :visible.sync="dialogVisible" width="900px" @close="handleClose">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="监控概览" name="overview">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-view"></i></div><div class="stat-info"><div class="stat-value">{{ overviewData.totalIndicators || 0 }}</div><div class="stat-title">监控指标数</div></div></div></el-col>
          <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-warning"></i></div><div class="stat-info"><div class="stat-value">{{ overviewData.warningCount || 0 }}</div><div class="stat-title">预警数量</div></div></div></el-col>
          <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-bell"></i></div><div class="stat-info"><div class="stat-value">{{ overviewData.highRiskCount || 0 }}</div><div class="stat-title">高风险数</div></div></div></el-col>
          <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-success"></i></div><div class="stat-info"><div class="stat-value">{{ overviewData.coverageRate || 0 }}%</div><div class="stat-title">监控覆盖率</div></div></div></el-col>
        </el-row>
        <el-divider></el-divider>
        <el-table :data="monitoringList" border v-loading="loading">
          <el-table-column prop="indicatorName" label="指标名称" width="200"></el-table-column>
          <el-table-column prop="monitoringType" label="监控类型" width="120">
            <template slot-scope="scope">{{ getMonitoringTypeText(scope.row.monitoringType) }}</template>
          </el-table-column>
          <el-table-column prop="currentRiskLevel" label="风险等级" width="100">
            <template slot-scope="scope"><el-tag :type="getRiskLevelType(scope.row.currentRiskLevel)">{{ getRiskLevelText(scope.row.currentRiskLevel) }}</el-tag></template>
          </el-table-column>
          <el-table-column prop="monitoringStatus" label="状态" width="100">
            <template slot-scope="scope"><el-tag :type="getStatusType(scope.row.monitoringStatus)">{{ getStatusText(scope.row.monitoringStatus) }}</el-tag></template>
          </el-table-column>
          <el-table-column prop="currentValue" label="当前值" width="100" align="right"></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="新增指标" name="add">
        <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
          <el-form-item label="指标名称" prop="indicatorName"><el-input v-model="addForm.indicatorName"></el-input></el-form-item>
          <el-form-item label="监控类型" prop="monitoringType">
            <el-select v-model="addForm.monitoringType" style="width:100%">
              <el-option label="财务监控" value="financial"></el-option><el-option label="经营监控" value="operational"></el-option>
              <el-option label="市场监控" value="market"></el-option><el-option label="技术监控" value="technical"></el-option>
              <el-option label="合规监控" value="compliance"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="监控频率">
            <el-select v-model="addForm.monitoringFrequency" style="width:100%">
              <el-option label="每日" value="daily"></el-option><el-option label="每周" value="weekly"></el-option>
              <el-option label="每月" value="monthly"></el-option><el-option label="每季度" value="quarterly"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="当前值"><el-input-number v-model="addForm.currentValue" :precision="2"></el-input-number></el-form-item>
          <el-form-item label="预警阈值"><el-input-number v-model="addForm.warningThreshold" :precision="2"></el-input-number></el-form-item>
          <el-form-item label="危险阈值"><el-input-number v-model="addForm.dangerThreshold" :precision="2"></el-input-number></el-form-item>
          <el-form-item><el-button type="primary" @click="submitAddForm" :loading="loading">新增指标</el-button></el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getRiskMonitoringList, addRiskMonitoring, analyzeRiskMonitoring, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskMonitoringDialog',
  props: {
    visible: { type: Boolean, default: false },
    enterpriseId: { type: String, default: '' }
  },
  data() {
    return {
      loading: false,
      activeTab: 'overview',
      overviewData: {},
      monitoringList: [],
      addForm: { indicatorName: '', monitoringType: '', monitoringFrequency: 'monthly', currentValue: 0, warningThreshold: 0, dangerThreshold: 0 },
      addRules: {
        indicatorName: [{ required: true, message: '请输入指标名称', trigger: 'blur' }],
        monitoringType: [{ required: true, message: '请选择监控类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogVisible: { get() { return this.visible }, set(val) { this.$emit('update:visible', val) } }
  },
  watch: {
    visible(val) { if (val) this.loadMonitoringData() }
  },
  methods: {
    async loadMonitoringData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const [listRes, analyzeRes] = await Promise.all([
          getRiskMonitoringList({ enterpriseId: this.enterpriseId, pageNumber: 1, pageSize: 20 }),
          analyzeRiskMonitoring({ enterpriseId: this.enterpriseId })
        ])
        const listData = extractData(listRes)
        this.monitoringList = (listData && listData.records) || []
        this.overviewData = extractData(analyzeRes) || {}
      } catch (e) { console.error('加载监控数据失败:', e) }
      finally { this.loading = false }
    },
    async submitAddForm() {
      this.$refs.addForm.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          await addRiskMonitoring({ ...this.addForm, enterpriseId: this.enterpriseId, enterpriseName: this.$parent.selectedEnterpriseName || '', monitoringStatus: 'normal' })
          this.$message.success('新增成功')
          this.addForm = { indicatorName: '', monitoringType: '', monitoringFrequency: 'monthly', currentValue: 0, warningThreshold: 0, dangerThreshold: 0 }
          this.loadMonitoringData(); this.$emit('refresh')
        } catch (e) { this.$message.error('新增失败：' + e.message) }
        finally { this.loading = false }
      })
    },
    getRiskLevelType(level) { return { high: 'danger', medium: 'warning', low: 'success' }[level] || 'info' },
    getRiskLevelText(level) { return { high: '高风险', medium: '中风险', low: '低风险' }[level] || level || '-' },
    getStatusType(status) { return { normal: 'success', warning: 'warning', danger: 'danger' }[status] || 'info' },
    getStatusText(status) { return { normal: '正常', warning: '预警', danger: '危险' }[status] || status || '-' },
    getMonitoringTypeText(type) { return { financial: '财务监控', operational: '经营监控', market: '市场监控', technical: '技术监控', compliance: '合规监控' }[type] || type || '-' },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.stat-item { display: flex; align-items: center; padding: 8px 0; }
.stat-icon { font-size: 24px; margin-right: 10px; }
.stat-info { flex: 1; }
.stat-value { font-size: 20px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 12px; color: #909399; margin-top: 4px; }
</style>
