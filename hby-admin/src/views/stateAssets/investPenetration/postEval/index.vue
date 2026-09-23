<template>
  <div class="post-eval-page" :style="themeVars">
    <div class="page-banner">
      <div class="banner-left">
        <i class="el-icon-data-analysis banner-icon" />
        <div>
          <h2>投后评价管理</h2>
          <p>跟踪投资项目 KPI 完成率，对比预期与实际收益，评估资产保值增值状况</p>
        </div>
      </div>
      <div class="banner-right">
        <el-tag type="warning" effect="dark"><i class="el-icon-warning" /> {{ list.filter(i=>i.evalStatus==='PENDING').length }} 个项目待评价</el-tag>
      </div>
    </div>

    <!-- KPI概览条 -->
    <el-row :gutter="12" class="kpi-row">
      <el-col :span="4" v-for="k in kpiCards" :key="k.label">
        <div class="kpi-mini" :style="{borderLeftColor:k.color}">
          <div class="kpi-v" :style="{color:k.color}">{{ k.value }}</div>
          <div class="kpi-l">{{ k.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索 + 操作 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="项目名称">
          <el-input v-model="queryForm.projectName" placeholder="项目名称" clearable style="width:170px" />
        </el-form-item>
        <el-form-item label="评价状态">
          <el-select v-model="queryForm.evalStatus" clearable placeholder="全部" style="width:120px">
            <el-option label="待评价" value="PENDING" />
            <el-option label="评价中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="评价结论">
          <el-select v-model="queryForm.conclusion" clearable placeholder="全部" style="width:120px">
            <el-option label="优秀" value="EXCELLENT" />
            <el-option label="良好" value="GOOD" />
            <el-option label="一般" value="FAIR" />
            <el-option label="较差" value="POOR" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleStartEval">新增评价</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 列表 -->
    <el-card shadow="never" style="margin-top:12px">
      <el-table :data="filteredList" border size="small" style="width:100%">
        <el-table-column label="项目编号" prop="projectId" width="130" />
        <el-table-column label="项目名称" prop="projectName" min-width="160" show-overflow-tooltip />
        <el-table-column label="投资企业" prop="company" width="95" />
        <el-table-column label="投资金额(万)" width="110" align="right">
          <template slot-scope="{row}"><span class="amount-text">{{ row.investAmount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="预期收益率" prop="expectedReturn" width="100" align="right">
          <template slot-scope="{row}">{{ row.expectedReturn }}%</template>
        </el-table-column>
        <el-table-column label="实际收益率" width="100" align="right">
          <template slot-scope="{row}">
            <span :style="{color: row.actualReturn < row.expectedReturn * 0.7 ? '#FF4D4F' : row.actualReturn < row.expectedReturn ? '#FA8C16' : '#52C41A', fontWeight:600}">
              {{ row.actualReturn !== null ? row.actualReturn + '%' : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="KPI达成率" width="130">
          <template slot-scope="{row}">
            <el-progress :percentage="row.kpiRate"
              :color="row.kpiRate >= 90 ? '#52C41A' : row.kpiRate >= 70 ? '#FA8C16' : '#FF4D4F'"
              :stroke-width="10" />
          </template>
        </el-table-column>
        <el-table-column label="评价状态" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="evalStatusMap[row.evalStatus].type" size="mini">{{ evalStatusMap[row.evalStatus].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评价结论" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.conclusion" :type="conclusionMap[row.conclusion].type" size="mini">{{ conclusionMap[row.conclusion].label }}</el-tag>
            <span v-else style="color:#ccc">-</span>
          </template>
        </el-table-column>
        <el-table-column label="评价周期" prop="evalPeriod" width="100" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button size="mini" type="text" @click="handleViewKpi(row)">查看</el-button>
            <el-button v-if="row.evalStatus !== 'COMPLETED'" size="mini" type="text" style="color:#FA8C16" @click="handleEditEval(row)">填报</el-button>
            <el-button size="mini" type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#FF4D4F" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- KPI详情抽屉 -->
    <el-drawer :title="'KPI详情 - ' + (drawerProject ? drawerProject.projectName : '')"
      :visible.sync="drawerVisible" size="600px" direction="rtl">
      <div style="padding:20px" v-if="drawerProject">
        <div class="kpi-header">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="投资企业">{{ drawerProject.company }}</el-descriptions-item>
            <el-descriptions-item label="投资金额">{{ drawerProject.investAmount.toLocaleString() }}万元</el-descriptions-item>
            <el-descriptions-item label="预期收益率">{{ drawerProject.expectedReturn }}%</el-descriptions-item>
            <el-descriptions-item label="实际收益率">
              <span :style="{color: drawerProject.actualReturn < drawerProject.expectedReturn ? '#FF4D4F' : '#52C41A', fontWeight:600}">{{ drawerProject.actualReturn }}%</span>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="kpi-list" style="margin-top:16px">
          <div class="section-title">KPI 指标达成详情</div>
          <el-table :data="drawerProject.kpiItems" border size="small">
            <el-table-column label="指标名称" prop="name" width="140" />
            <el-table-column label="目标值" prop="target" width="90" align="right" />
            <el-table-column label="实际值" prop="actual" width="90" align="right">
              <template slot-scope="{row}">
                <span :style="{color: row.achieved ? '#52C41A' : '#FF4D4F', fontWeight:600}">{{ row.actual }}</span>
              </template>
            </el-table-column>
            <el-table-column label="达成率" width="120">
              <template slot-scope="{row}">
                <el-progress :percentage="row.rate" :color="row.rate >= 90 ? '#52C41A' : row.rate >= 70 ? '#FA8C16' : '#FF4D4F'" :stroke-width="8" />
              </template>
            </el-table-column>
            <el-table-column label="状态" width="70" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.achieved ? 'success' : 'danger'" size="mini">{{ row.achieved ? '达标' : '未达标' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="eval-comment" style="margin-top:16px" v-if="drawerProject.evalComment">
          <div class="section-title">评价意见</div>
          <div class="comment-box">{{ drawerProject.evalComment }}</div>
        </div>
      </div>
    </el-drawer>

    <!-- 新增/编辑评价对话框 -->
    <el-dialog :title="dialogType === 'add' ? '新增投后评价' : '编辑投后评价'" :visible.sync="dialogVisible" width="640px" :close-on-click-modal="false">
      <el-form :model="evalForm" :rules="evalRules" ref="evalForm" label-width="120px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="投资项目" prop="projectId">
              <el-select v-model="evalForm.projectId" placeholder="请选择项目" style="width:100%" filterable @change="handleProjectSelect">
                <el-option v-for="p in projectList" :key="p.projectId" :label="p.projectName" :value="p.projectId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资企业">
              <el-input v-model="evalForm.companyName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="评价类型" prop="evalType">
              <el-select v-model="evalForm.evalType" placeholder="请选择" style="width:100%">
                <el-option label="中期评价" value="中期评价" />
                <el-option label="终期评价" value="终期评价" />
                <el-option label="年度评价" value="年度评价" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评价状态" prop="evalStatus">
              <el-select v-model="evalForm.evalStatus" placeholder="请选择" style="width:100%">
                <el-option label="待评价" value="PENDING" />
                <el-option label="评价中" value="IN_PROGRESS" />
                <el-option label="已完成" value="COMPLETED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="预期收益率(%)">
              <el-input-number v-model="evalForm.expectedReturn" :min="0" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际收益率(%)">
              <el-input-number v-model="evalForm.actualReturn" :min="-100" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="评价得分">
              <el-input-number v-model="evalForm.evalScore" :min="0" :max="100" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评价结论" prop="evalConclusion">
              <el-select v-model="evalForm.evalConclusion" placeholder="请选择" style="width:100%" clearable>
                <el-option label="优秀" value="EXCELLENT" />
                <el-option label="良好" value="GOOD" />
                <el-option label="一般" value="FAIR" />
                <el-option label="较差" value="POOR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPostEvalList, startPostEval, updatePostEval } from '@/api/stateAssets/investPenetration'
import { getInvestProjectList } from '@/api/stateAssets/investPenetration'
import request from '@/utils/request'
import { investThemeMixin } from '../themeMixin'

const JSON_HEADER = { 'Content-Type': 'application/json' }


export default {
  name: 'InvestPostEval',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      companyId: '',
      submitLoading: false,
      queryForm: { projectName: '', evalStatus: '', conclusion: '' },
      list: [],
      total: 0,
      projectList: [],
      drawerVisible: false,
      drawerProject: null,
      dialogVisible: false,
      dialogType: 'add',
      evalForm: {},
      evalRules: {
        projectId: [{ required: true, message: '请选择投资项目', trigger: 'change' }],
        evalType: [{ required: true, message: '请选择评价类型', trigger: 'change' }],
        evalStatus: [{ required: true, message: '请选择评价状态', trigger: 'change' }],
      },
      evalStatusMap: {
        PENDING: { type: 'warning', label: '待评价' },
        IN_PROGRESS: { type: '', label: '评价中' },
        COMPLETED: { type: 'success', label: '已完成' },
      },
      conclusionMap: {
        EXCELLENT: { type: 'success', label: '优秀' },
        GOOD: { type: '', label: '良好' },
        FAIR: { type: 'warning', label: '一般' },
        POOR: { type: 'danger', label: '较差' },
      },
    }
  },
  mounted() { this.loadData() },
  computed: {
    filteredList() {
      let data = [...this.list]
      if (this.queryForm.projectName) {
        data = data.filter(d => d.projectName && d.projectName.includes(this.queryForm.projectName))
      }
      if (this.queryForm.conclusion) {
        data = data.filter(d => d.conclusion === this.queryForm.conclusion)
      }
      return data
    },
    kpiCards() {
      const d = this.list
      const avgKpi = d.length > 0 ? Math.round(d.reduce((s, i) => s + (i.kpiRate || 0), 0) / d.length) : 0
      const validReturns = d.filter(i => i.actualReturn !== null && i.actualReturn !== undefined)
      const avgReturn = validReturns.length > 0 ? (validReturns.reduce((s, i) => s + Number(i.actualReturn), 0) / validReturns.length).toFixed(1) : '0'
      return [
        { label: '评价项目总数', value: d.length + ' 个', color: this.ipPrimary },
        { label: '已完成评价', value: d.filter(i => i.evalStatus === 'COMPLETED').length + ' 个', color: '#52C41A' },
        { label: '待评价', value: d.filter(i => i.evalStatus === 'PENDING').length + ' 个', color: '#FA8C16' },
        { label: '平均KPI达成率', value: avgKpi + '%', color: avgKpi >= 80 ? '#52C41A' : avgKpi >= 60 ? '#FA8C16' : '#FF4D4F' },
        { label: '平均实际收益率', value: avgReturn + '%', color: this.ipSecondary },
        { label: '较差项目', value: d.filter(i => i.conclusion === 'POOR').length + ' 个', color: '#FF4D4F' },
      ]
    },
  },
  created() {
    this.initCompanyId()
    this.fetchData()
    this.loadProjects()
  },
  methods: {
    initCompanyId() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = userInfo.currentOrg || {}
        if (org.id) this.companyId = String(org.id)
      } catch (e) { console.warn('[initCompanyId]', e) }
    },
    async loadProjects() {
      try {
        const res = await getInvestProjectList({ pageNumber: 1, pageSize: 100 })
        if (res.result === 200 && res.data) {
          this.projectList = (res.data.tlist || []).map(p => ({
            projectId: p.projectId,
            projectName: p.projectName,
            companyId: p.companyId,
            companyName: p.companyName,
            expectedReturn: p.expectedReturn
          }))
        }
      } catch (e) { /* ignore */ }
    },
    handleProjectSelect(projectId) {
      const p = this.projectList.find(item => item.projectId === projectId)
      if (p) {
        this.evalForm.projectName = p.projectName
        this.evalForm.companyId = p.companyId
        this.evalForm.companyName = p.companyName
        this.evalForm.expectedReturn = Number(p.expectedReturn) || null
      }
    },
    async fetchData() {
      this.loading = true
      try {
        const params = { pageNumber: 1, pageSize: 100, companyId: this.companyId }
        if (this.queryForm.evalStatus) params.evalStatus = this.queryForm.evalStatus
        const res = await getPostEvalList(params)
        if (res.result === 200 && res.data) {
          const tlist = res.data.tlist || res.data || []
          this.list = (Array.isArray(tlist) ? tlist : []).map(item => ({
            ...item,
            company: item.companyName,
            investAmount: Number(item.investAmount) || 0,
            expectedReturn: Number(item.expectedReturn) || 0,
            actualReturn: item.actualReturn != null ? Number(item.actualReturn) : null,
            kpiRate: item.evalScore != null ? Number(item.evalScore) : 0,
            conclusion: item.evalConclusion,
            evalPeriod: item.evalDate ? item.evalDate.substring(0, 7) : (item.evalType || '-'),
            evalComment: item.evalConclusion
              ? '评价结论：' + ({ EXCELLENT: '优秀', GOOD: '良好', FAIR: '一般', POOR: '较差' }[item.evalConclusion] || item.evalConclusion)
              : null,
            kpiItems: this.generateKpiItems(item),
          }))
          this.total = res.data.totalRecord || this.list.length
        }
      } catch (e) {
        this.$message.error('查询失败')
      } finally { this.loading = false }
    },
    generateKpiItems(item) {
      const score = Number(item.evalScore) || 0
      const expected = Number(item.expectedReturn) || 0
      const actual = item.actualReturn != null ? Number(item.actualReturn) : null
      if (score === 0 && actual === null) return []
      const items = []
      if (expected > 0 && actual !== null) {
        const returnRate = Math.min(100, Math.round(actual / expected * 100))
        items.push({ name: '投资收益率', target: expected + '%', actual: actual + '%', rate: returnRate, achieved: actual >= expected })
      }
      if (score > 0) {
        items.push({ name: '综合评价得分', target: '80分', actual: score + '分', rate: Math.min(100, Math.round(score / 80 * 100)), achieved: score >= 80 })
        items.push({ name: '资产保值增值', target: '100%', actual: score >= 70 ? '达标' : '未达标', rate: score >= 70 ? 100 : Math.round(score / 70 * 100), achieved: score >= 70 })
        items.push({ name: '风险控制', target: '合规', actual: score >= 60 ? '合规' : '存在风险', rate: score >= 60 ? 100 : 50, achieved: score >= 60 })
      }
      return items
    },
    handleQuery() { this.fetchData() },
    resetQuery() { this.queryForm = { projectName: '', evalStatus: '', conclusion: '' }; this.fetchData() },
    handleStartEval() {
      this.dialogType = 'add'
      this.evalForm = { projectId: '', projectName: '', companyId: '', companyName: '', evalType: '中期评价', evalStatus: 'PENDING', expectedReturn: null, actualReturn: null, evalScore: null, evalConclusion: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.evalForm = {
        evalId: row.evalId,
        projectId: row.projectId,
        projectName: row.projectName,
        companyName: row.company || row.companyName,
        companyId: row.companyId,
        evalType: row.evalType || '中期评价',
        evalStatus: row.evalStatus,
        expectedReturn: row.expectedReturn,
        actualReturn: row.actualReturn,
        evalScore: row.kpiRate || row.evalScore,
        evalConclusion: row.conclusion || row.evalConclusion || '',
      }
      this.dialogVisible = true
    },
    handleEditEval(row) {
      this.dialogType = 'edit'
      this.evalForm = {
        evalId: row.evalId,
        projectId: row.projectId,
        projectName: row.projectName,
        companyName: row.company || row.companyName,
        companyId: row.companyId,
        evalType: row.evalType || '中期评价',
        evalStatus: 'IN_PROGRESS',
        expectedReturn: row.expectedReturn,
        actualReturn: row.actualReturn,
        evalScore: row.kpiRate || row.evalScore,
        evalConclusion: row.conclusion || row.evalConclusion || '',
      }
      this.dialogVisible = true
    },
    handleViewKpi(row) {
      this.drawerProject = row
      this.drawerVisible = true
    },
    async handleSubmit() {
      this.$refs.evalForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const companyIdMap = { '示例能源': 'HBNY', '示例科技': 'HBKJ', '示例金融': 'HBJR', '示例地产': 'HBDC', '示例物流': 'HBWL' }
          const payload = {
            ...this.evalForm,
            companyId: this.evalForm.companyId || companyIdMap[this.evalForm.companyName] || '',
            deviation: (this.evalForm.actualReturn != null && this.evalForm.expectedReturn != null)
              ? Number((this.evalForm.actualReturn - this.evalForm.expectedReturn).toFixed(2))
              : null,
          }
          let res
          if (this.dialogType === 'add') {
            res = await startPostEval(payload)
          } else {
            res = await updatePostEval(payload)
          }
          if (res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally { this.submitLoading = false }
      })
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该评价记录？', '提示', { type: 'warning' })
        const res = await request({ url: '/monitor/v1/supervision/investment/post-eval/' + row.evalId, method: 'delete' })
        if (res.result === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.post-eval-page { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px; padding: 16px 24px; margin-bottom: 12px; color: #fff;
  display: flex; justify-content: space-between; align-items: center;
  .banner-left { display: flex; align-items: center; gap: 14px; }
  .banner-icon { font-size: 32px; color: var(--ip-accent, #FAAD14); }
  h2 { margin: 0; font-size: 18px; }
  p { margin: 4px 0 0; font-size: 12px; opacity: 0.8; }
}
.kpi-row { margin-bottom: 12px; }
.kpi-mini {
  background: #fff; border-left: 4px solid var(--ip-primary, #1A3A6B); border-radius: 6px;
  padding: 10px 14px; box-shadow: 0 1px 4px rgba(0,0,0,.06);
  .kpi-v { font-size: 18px; font-weight: 700; }
  .kpi-l { font-size: 12px; color: #888; margin-top: 2px; }
}
.query-card { border-left: 3px solid var(--ip-primary, #1A3A6B); }
.query-card .el-form-item { margin-bottom: 0; }
.amount-text { font-weight: 600; color: var(--ip-primary, #1A3A6B); }
::v-deep .el-table th { background: #f5f7ff; color: var(--ip-primary, #1A3A6B); }
.section-title { font-size: 13px; font-weight: 600; color: var(--ip-primary, #1A3A6B); margin-bottom: 8px; }
.comment-box {
  background: #f9f9f9; border: 1px solid #e8e8e8; border-radius: 6px;
  padding: 12px 16px; font-size: 13px; color: #555; line-height: 1.8;
}
</style>
