<template>
  <div class="industry-public-service" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card public-service-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-service"></i></div>
            <div class="card-info">
              <div class="card-title">服务机构数量</div>
              <div class="card-value">{{ overviewData.totalCount }}家</div>
              <div class="card-desc">医疗机构 {{ overviewData.medicalCount }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card public-service-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-s-finance"></i></div>
            <div class="card-info">
              <div class="card-title">总资产规模</div>
              <div class="card-value">{{ overviewData.totalAssets }}亿</div>
              <div class="card-desc">平均社会效益 {{ overviewData.avgSocialScore }}分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card public-service-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-warning"></i></div>
            <div class="card-info">
              <div class="card-title">风险预警</div>
              <div class="card-value">{{ overviewData.highRiskCount }}项</div>
              <div class="card-desc">亏损企业 {{ overviewData.lossCount }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card public-service-gradient">
          <div class="card-content">
            <div class="card-icon"><i class="el-icon-medal"></i></div>
            <div class="card-info">
              <div class="card-title">社会效益评分</div>
              <div class="card-value">{{ overviewData.avgSocialScore }}分</div>
              <div class="card-desc">{{ overviewData.avgSocialScore >= 80 ? '优秀等级' : '待提升' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable />
        </el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="queryForm.serviceType" placeholder="请选择服务类型" clearable>
            <el-option label="医疗" value="医疗" />
            <el-option label="教育" value="教育" />
            <el-option label="文化" value="文化" />
            <el-option label="通信" value="通信" />
            <el-option label="环保" value="环保" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增监管</el-button>
          <el-button type="warning" @click="handleExport" icon="el-icon-download">导出报告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 功能模块 -->
    <el-card class="function-card" v-loading="loading">
      <div slot="header" class="card-header">
        <span class="card-title">公共服务类国企监管功能</span>
        <span class="card-total">共 {{ total }} 条记录</span>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="医疗服务监管" name="medical">
          <el-table :data="getTabData('医疗')" border size="small" :header-cell-style="{ background: '#FFF2E8', color: '#ad4e00' }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="补贴比例%" prop="subsidyRatio" width="100" align="center" />
            <el-table-column label="社会效益" prop="socialScore" width="90" align="center" />
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="教育服务监管" name="education">
          <el-table :data="getTabData('教育')" border size="small" :header-cell-style="{ background: '#FFF2E8', color: '#ad4e00' }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="补贴比例%" prop="subsidyRatio" width="100" align="center" />
            <el-table-column label="社会效益" prop="socialScore" width="90" align="center" />
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="文化服务监管" name="culture">
          <el-table :data="getTabData('文化')" border size="small" :header-cell-style="{ background: '#FFF2E8', color: '#ad4e00' }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="补贴比例%" prop="subsidyRatio" width="100" align="center" />
            <el-table-column label="社会效益" prop="socialScore" width="90" align="center" />
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="通信服务监管" name="telecom">
          <el-table :data="getTabData('通信')" border size="small" :header-cell-style="{ background: '#FFF2E8', color: '#ad4e00' }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="补贴比例%" prop="subsidyRatio" width="100" align="center" />
            <el-table-column label="社会效益" prop="socialScore" width="90" align="center" />
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="环保服务监管" name="environment">
          <el-table :data="getTabData('环保')" border size="small" :header-cell-style="{ background: '#FFF2E8', color: '#ad4e00' }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="资产规模(亿)" prop="totalAssets" width="110" align="right" />
            <el-table-column label="补贴比例%" prop="subsidyRatio" width="100" align="center" />
            <el-table-column label="社会效益" prop="socialScore" width="90" align="center" />
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="服务质量监控" name="quality">
          <el-table :data="tabDataList" border size="small" :header-cell-style="{ background: '#FFF2E8', color: '#ad4e00' }">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="服务类型" prop="serviceType" width="100" align="center" />
            <el-table-column label="社会效益" prop="socialScore" width="90" align="center" />
            <el-table-column label="亏损年数" prop="lossYears" width="90" align="center">
              <template slot-scope="s"><span :style="{ color: s.row.lossYears > 0 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.lossYears }}</span></template>
            </el-table-column>
            <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
              <template slot-scope="s"><el-tag :type="s.row.riskLevel === '低' ? 'success' : s.row.riskLevel === '中' ? 'warning' : 'danger'" size="small">{{ s.row.riskLevel }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center" fixed="right">
              <template slot-scope="s">
                <el-button type="text" size="small" @click="handleView(s.row)">查看</el-button>
                <el-button type="text" size="small" @click="handleEdit(s.row)">编辑</el-button>
                <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="formDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="formData.companyName" placeholder="请输入企业名称" /></el-form-item>
        <el-form-item label="服务类型" prop="serviceType">
          <el-select v-model="formData.serviceType" placeholder="请选择服务类型" style="width:100%">
            <el-option label="医疗" value="医疗" /><el-option label="教育" value="教育" /><el-option label="文化" value="文化" /><el-option label="通信" value="通信" /><el-option label="环保" value="环保" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产规模(亿)"><el-input-number v-model="formData.totalAssets" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="社会效益评分"><el-input-number v-model="formData.socialScore" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="formData.riskLevel" placeholder="请选择风险等级" style="width:100%">
            <el-option label="低风险" value="LOW" /><el-option label="中风险" value="MEDIUM" /><el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="formDialogVisible = false">取 消</el-button><el-button type="primary" :loading="formLoading" @click="submitForm">确 定</el-button></div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="监管详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="企业名称">{{ viewData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="服务类型">{{ viewData.serviceType }}</el-descriptions-item>
        <el-descriptions-item label="资产规模(亿)">{{ viewData.totalAssets }}</el-descriptions-item>
        <el-descriptions-item label="社会效益评分">{{ viewData.socialScore }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="viewData.riskLevel === 'LOW' ? 'success' : viewData.riskLevel === 'MEDIUM' ? 'warning' : 'danger'" size="small">{{ viewData.riskLevel === 'LOW' ? '低' : viewData.riskLevel === 'MEDIUM' ? '中' : '高' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="viewDialogVisible = false">关 闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getPublicServiceList, addPublicServiceMonitor, getPublicServiceDetail, updatePublicServiceMonitor, deletePublicServiceMonitor, exportPublicServiceData } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'IndustryPublicService',
  mixins: [investThemeMixin],
  data() {
    return {
      activeTab: 'medical',
      loading: false,
      total: 0,
      queryForm: { companyName: '', serviceType: '', riskLevel: '', pageNum: 1, pageSize: 200 },
      overviewData: { totalCount: 0, medicalCount: 0, totalAssets: 0, avgSocialScore: 0, highRiskCount: 0, lossCount: 0 },
      tabDataList: [],
      formDialogVisible: false,
      formLoading: false,
      dialogTitle: '新增公共服务监管',
      isEdit: false,
      formData: { id: '', companyName: '', serviceType: '', totalAssets: 0, socialScore: 0, riskLevel: 'LOW' },
      formRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        serviceType: [{ required: true, message: '请选择服务类型', trigger: 'change' }]
      },
      viewDialogVisible: false,
      viewData: {}
    }
  },
  mounted() { this.loadData() },
  methods: {
    getTabData(keyword) {
      return this.tabDataList.filter(r => r.serviceType && r.serviceType.includes(keyword))
    },
    async loadData() {
      this.loading = true
      try {
        const params = { companyName: this.queryForm.companyName, subType: this.queryForm.serviceType, riskLevel: this.queryForm.riskLevel, pageNum: this.queryForm.pageNum, pageSize: this.queryForm.pageSize }
        const res = await getPublicServiceList(params)
        if (res && res.result === 200 && res.data) {
          const list = res.data.tlist || res.data.list || []
          this.total = res.data.totalRecord || list.length
          const highRiskCount = list.filter(item => item.riskLevel === 'HIGH').length
          const lossCount = list.filter(item => item.lossYears > 0).length
          const avgSocial = list.length > 0 ? (list.reduce((s, r) => s + (Number(r.socialScore) || 0), 0) / list.length).toFixed(1) : 0
          this.overviewData = {
            totalCount: this.total,
            medicalCount: list.filter(item => (item.serviceType || item.subType || '').includes('医疗')).length,
            totalAssets: list.reduce((sum, item) => sum + (Number(item.totalAssets) || 0), 0).toFixed(1),
            avgSocialScore: avgSocial,
            highRiskCount: highRiskCount,
            lossCount: lossCount
          }
          this.tabDataList = list.map(item => ({
            id: item.id,
            companyName: item.companyName,
            serviceType: item.serviceType || item.subType || '通信',
            totalAssets: item.totalAssets || 0,
            subsidyRatio: item.subsidyRatio || 0,
            socialScore: item.socialScore || 0,
            lossYears: item.lossYears || 0,
            riskLevel: item.riskLevel === 'HIGH' ? '高' : item.riskLevel === 'MEDIUM' ? '中' : '低',
            riskLevelRaw: item.riskLevel
          }))
        }
      } catch (error) {
        console.error('获取公共服务数据异常:', error)
        this.$message.error('获取数据失败，请稍后重试')
      } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNum = 1; this.loadData() },
    handleReset() {
      this.queryForm = { companyName: '', serviceType: '', riskLevel: '', pageNum: 1, pageSize: 200 }
      this.$nextTick(() => { if (this.$refs.queryForm) this.$refs.queryForm.resetFields(); this.loadData() })
    },
    handleAdd() {
      this.isEdit = false; this.dialogTitle = '新增公共服务监管'
      this.formData = { id: '', companyName: '', serviceType: '', totalAssets: 0, socialScore: 0, riskLevel: 'LOW' }
      this.formDialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },
    async handleView(row) {
      try {
        const res = await getPublicServiceDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.viewData = { companyName: d.companyName, serviceType: d.subType, totalAssets: d.totalAssets, socialScore: d.rdIntensity || d.socialScore, riskLevel: d.riskLevel || 'LOW', createTime: d.createTime || '' }
        } else {
          this.viewData = { companyName: row.companyName, serviceType: row.serviceType, totalAssets: row.totalAssets, socialScore: row.socialScore, riskLevel: row.riskLevelRaw || 'LOW', createTime: '' }
        }
      } catch (e) {
        this.viewData = { companyName: row.companyName, serviceType: row.serviceType, totalAssets: row.totalAssets, socialScore: row.socialScore, riskLevel: row.riskLevelRaw || 'LOW', createTime: '' }
      }
      this.viewDialogVisible = true
    },
    async handleEdit(row) {
      this.isEdit = true; this.dialogTitle = '编辑公共服务监管'
      try {
        const res = await getPublicServiceDetail(row.id)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.formData = { id: d.id, companyName: d.companyName, serviceType: d.subType, totalAssets: Number(d.totalAssets) || 0, socialScore: Number(d.rdIntensity) || Number(d.socialScore) || 0, riskLevel: d.riskLevel || 'LOW' }
        } else {
          this.formData = { id: row.id, companyName: row.companyName, serviceType: row.serviceType, totalAssets: Number(row.totalAssets) || 0, socialScore: Number(row.socialScore) || 0, riskLevel: row.riskLevelRaw || 'LOW' }
        }
      } catch (e) {
        this.formData = { id: row.id, companyName: row.companyName, serviceType: row.serviceType, totalAssets: Number(row.totalAssets) || 0, socialScore: Number(row.socialScore) || 0, riskLevel: row.riskLevelRaw || 'LOW' }
      }
      this.formDialogVisible = true
      this.$nextTick(() => { if (this.$refs.formRef) this.$refs.formRef.clearValidate() })
    },
    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.formLoading = true
        try {
          const submitData = { id: this.formData.id, companyName: this.formData.companyName, subType: this.formData.serviceType, totalAssets: this.formData.totalAssets, rdIntensity: this.formData.socialScore, riskLevel: this.formData.riskLevel }
          const apiFn = this.isEdit ? updatePublicServiceMonitor : addPublicServiceMonitor
          const res = await apiFn(submitData)
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '编辑成功' : '新增成功'); this.formDialogVisible = false; this.loadData() }
          else { this.$message.error(res.msg || '操作失败') }
        } catch (error) { this.$message.error('操作失败，请稍后重试') }
        finally { this.formLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm(`确定要删除企业「${row.companyName}」的监管记录吗？`, '删除确认', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try {
          const res = await deletePublicServiceMonitor(row.id)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.loadData() }
          else { this.$message.error(res.msg || '删除失败') }
        } catch (error) { this.$message.error('删除失败，请稍后重试') }
      }).catch(() => {})
    },
    async handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出报告...', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const params = { companyName: this.queryForm.companyName, subType: this.queryForm.serviceType, riskLevel: this.queryForm.riskLevel }
        const res = await exportPublicServiceData(params)
        if (res && res.result === 200 && res.data) { this.downloadCsv(res.data); this.$message.success('导出成功') }
        else { this.$message.error(res.msg || '导出失败') }
      } catch (error) { this.$message.error('导出失败，请稍后重试') }
      finally { loading.close() }
    },
    downloadCsv(data) {
      if (!data || data.length === 0) { this.$message.warning('暂无数据可导出'); return }
      const headers = Object.keys(data[0])
      const csvContent = [headers.map(h => `"${h}"`).join(','), ...data.map(row => headers.map(h => { const val = row[h] != null ? String(row[h]) : ''; return `"${val.replace(/"/g, '""')}"` }).join(','))].join('\n')
      const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a'); link.href = URL.createObjectURL(blob)
      link.download = `公共服务监管报告_${new Date().toISOString().slice(0, 10)}.csv`; link.click(); URL.revokeObjectURL(link.href)
    }
  }
}
</script>

<style lang="scss" scoped>
.industry-public-service {
  padding: 20px;
  .overview-cards { margin-bottom: 20px;
    .overview-card { height: 120px; border: none; box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
      &.public-service-gradient { background: linear-gradient(135deg, var(--ip-secondary) 0%, var(--ip-bright) 100%); color: #fff;
        .card-content { display: flex; align-items: center; height: 100%;
          .card-icon { font-size: 36px; margin-right: 16px; opacity: 0.8; color: rgba(255,255,255,0.85); }
          .card-info { flex: 1;
            .card-title { font-size: 14px; margin-bottom: 8px; color: rgba(255,255,255,0.85); }
            .card-value { font-size: 24px; font-weight: bold; margin-bottom: 4px; color: #fff; }
            .card-desc { font-size: 12px; color: #666; }
          }
        }
      }
    }
  }
  .search-card { margin-bottom: 20px; }
  .function-card {
    .card-header { display: flex; justify-content: space-between; align-items: center;
      .card-title { font-size: 16px; font-weight: 600; color: #303133; }
      .card-total { font-size: 13px; color: #909399; }
    }
  }
  ::v-deep .el-tabs__content { padding: 20px; min-height: 400px; }
  ::v-deep .el-card__body { padding: 0; }
}
</style>

