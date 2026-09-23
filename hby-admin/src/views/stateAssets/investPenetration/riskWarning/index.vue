<template>
  <div class="risk-warning-page" :style="themeVars">
    <div class="page-banner">
      <div class="banner-left">
        <i class="el-icon-bell banner-icon" />
        <div>
          <h2>投资风险预警管理</h2>
          <p>10类预警规则引擎，实时监控越权投资、收益偏离、进度滞后等风险，驱动闭环整改</p>
        </div>
      </div>
      <div class="banner-tags">
        <el-tag type="danger" effect="dark"><i class="el-icon-warning" /> 高级预警 {{ highCount }} 条</el-tag>
        <el-tag type="warning" effect="dark" style="margin-left:8px">中级预警 {{ midCount }} 条</el-tag>
        <el-tag type="info" effect="plain" style="margin-left:8px">低级预警 {{ lowCount }} 条</el-tag>
      </div>
    </div>

    <!-- 预警统计卡 -->
    <el-row :gutter="12" class="stat-row">
      <el-col :span="4" v-for="s in statCards" :key="s.label">
        <div class="stat-mini" :style="{borderLeftColor:s.color}">
          <div class="sm-value" :style="{color:s.color}">{{ s.value }}</div>
          <div class="sm-label">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 查询 + 操作 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预警编号">
          <el-input v-model="queryForm.warningCode" placeholder="预警编号" clearable style="width:140px" />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="queryForm.warningType" clearable placeholder="全部" style="width:130px">
            <el-option v-for="t in warningTypes" :key="t.value" :label="t.label" :value="t.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险级别">
          <el-select v-model="queryForm.level" clearable placeholder="全部" style="width:100px">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="queryForm.handleStatus" clearable placeholder="全部" style="width:100px">
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="HANDLING" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="never" style="margin-top:12px">
      <div slot="header" class="card-header">
        <span><i class="el-icon-bell" /> 预警列表（{{ filteredList.length }}条）</span>
        <div>
          <el-button size="small" type="danger" @click="handleBatchDispatch" :disabled="selected.length === 0">批量派发</el-button>
        </div>
      </div>
      <el-table :data="filteredList" border size="small" style="width:100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" align="center" />
        <el-table-column label="预警编号" prop="warningCode" width="160" />
        <el-table-column label="规则编号" prop="ruleCode" width="90" />
        <el-table-column label="预警类型" prop="warningTypeLabel" width="110" />
        <el-table-column label="触发企业" prop="company" width="90" />
        <el-table-column label="关联项目" prop="projectName" min-width="150" show-overflow-tooltip />
        <el-table-column label="预警描述" prop="warningDesc" min-width="180" show-overflow-tooltip />
        <el-table-column label="风险级别" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.level === 'HIGH' ? 'danger' : row.level === 'MEDIUM' ? 'warning' : 'info'" size="mini" effect="dark">
              {{ row.level === 'HIGH' ? '高' : row.level === 'MEDIUM' ? '中' : '低' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="触发时间" prop="triggerTime" width="100" />
        <el-table-column label="处理状态" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="handleStatusMap[row.handleStatus].type" size="mini">{{ handleStatusMap[row.handleStatus].label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="处理期限" width="100">
          <template slot-scope="{row}">
            <span :style="{color: row.isOverdue ? '#FF4D4F' : '#333'}">
              {{ row.deadline }}
              <i v-if="row.isOverdue" class="el-icon-warning" style="color:#FF4D4F" />
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button size="mini" type="text" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.handleStatus === 'PENDING'" size="mini" type="text" style="color:#FA8C16" @click="handleDispatch(row)">派发</el-button>
            <el-button v-if="row.handleStatus === 'HANDLING'" size="mini" type="text" style="color:#52C41A" @click="handleComplete(row)">完成</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 预警规则说明 -->
    <el-card shadow="never" style="margin-top:16px">
      <div slot="header" class="card-header"><span><i class="el-icon-info" /> 预警规则说明（INV-W01 ~ INV-W10）</span></div>
      <el-table :data="ruleDefinitions" border size="small" style="width:100%">
        <el-table-column label="规则编号" prop="code" width="90" />
        <el-table-column label="规则名称" prop="name" width="120" />
        <el-table-column label="触发条件" prop="condition" min-width="200" show-overflow-tooltip />
        <el-table-column label="级别" width="70" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.level === 'HIGH' ? 'danger' : row.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ row.levelLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="处理时限" prop="deadline" width="80" align="center" />
        <el-table-column label="处理要求" prop="action" min-width="150" show-overflow-tooltip />
      </el-table>
    </el-card>

    <!-- 处理详情抽屉 -->
    <el-drawer title="预警处理详情" :visible.sync="drawerVisible" size="560px" direction="rtl">
      <div style="padding:20px" v-if="currentWarning">
        <el-descriptions :column="2" border size="small" style="margin-bottom:14px">
          <el-descriptions-item label="预警编号">{{ currentWarning.warningCode }}</el-descriptions-item>
          <el-descriptions-item label="规则编号">{{ currentWarning.ruleCode }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">{{ currentWarning.warningTypeLabel }}</el-descriptions-item>
          <el-descriptions-item label="触发企业">{{ currentWarning.company }}</el-descriptions-item>
          <el-descriptions-item label="关联项目" :span="2">{{ currentWarning.projectName }}</el-descriptions-item>
          <el-descriptions-item label="预警描述" :span="2">{{ currentWarning.warningDesc }}</el-descriptions-item>
          <el-descriptions-item label="触发时间">{{ currentWarning.triggerTime }}</el-descriptions-item>
          <el-descriptions-item label="处理期限">
            <span :style="{color: currentWarning.isOverdue ? '#FF4D4F' : '#333'}">{{ currentWarning.deadline }}</span>
          </el-descriptions-item>
        </el-descriptions>
        <div v-if="currentWarning.handleStatus !== 'PENDING'">
          <div class="section-title">处理记录</div>
          <el-timeline>
            <el-timeline-item v-for="h in currentWarning.handleHistory" :key="h.time" :timestamp="h.time" placement="top" :color="h.color">
              <el-card :body-style="{padding:'10px'}">
                <p style="margin:0;font-weight:600">{{ h.action }}</p>
                <p style="margin:4px 0 0;font-size:12px;color:#888">{{ h.operator }} · {{ h.desc }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div v-if="currentWarning.handleStatus !== 'COMPLETED'" style="margin-top:14px">
          <div class="section-title">处理意见</div>
          <el-input type="textarea" v-model="handleRemark" :rows="3" placeholder="请输入处理意见..." />
          <div style="margin-top:10px;text-align:right">
            <el-button size="small" @click="drawerVisible=false">关闭</el-button>
            <el-button size="small" type="primary" @click="submitHandle">提交处理</el-button>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getInvestWarningList, getWarningRuleList, dispatchInvestWarning, handleInvestWarning } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'


export default {
  name: 'InvestRiskWarning',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      companyId: '',
      queryForm: { warningCode: '', warningType: '', level: '', handleStatus: '' },
      list: [],
      total: 0,
      selected: [],
      drawerVisible: false,
      currentWarning: null,
      handleRemark: '',
      ruleDefinitions: [
        { code: 'INV-W01', name: '越权投资', condition: '投资金额超出该级企业审批权限上限（一级5亿/二级0.5亿/三级0.05亿）', level: 'HIGH', levelLabel: '高', deadline: '7天', action: '停止审批，上报上级决策机构' },
        { code: 'INV-W02', name: '投资集中度', condition: '单一项目投资额占全部投资额比例 > 30%', level: 'MEDIUM', levelLabel: '中', deadline: '14天', action: '分散投资风险，重新评估投资组合' },
        { code: 'INV-W03', name: '收益严重偏离', condition: '实际收益率 < 预期收益率 × (1-50%)', level: 'HIGH', levelLabel: '高', deadline: '7天', action: '启动专项审查，评估是否退出' },
        { code: 'INV-W04', name: '收益轻度偏离', condition: '预期收益率 × (1-30%) ≤ 实际收益率 < 预期收益率 × (1-50%)', level: 'LOW', levelLabel: '低', deadline: '30天', action: '制定改善计划，季度跟踪' },
        { code: 'INV-W05', name: '进度严重滞后', condition: '实际完成率 < 计划完成率 × 60%（偏差 > 40%）', level: 'MEDIUM', levelLabel: '中', deadline: '14天', action: '分析原因，制定追赶计划' },
        { code: 'INV-W06', name: '非主业超限', condition: '企业非主业投资占比 > 20%', level: 'MEDIUM', levelLabel: '中', deadline: '30天', action: '制定3年清退计划，报集团批准' },
        { code: 'INV-W07', name: '非主业严重超限', condition: '企业非主业投资占比 > 30%', level: 'HIGH', levelLabel: '高', deadline: '7天', action: '启动紧急清退程序，6个月内降至30%以下' },
        { code: 'INV-W08', name: '清退逾期', condition: '非主业投资清退计划逾期未完成 > 90天', level: 'MEDIUM', levelLabel: '中', deadline: '14天', action: '重新制定清退时间表，追责' },
        { code: 'INV-W09', name: '投后评价超期', condition: '项目未按要求（年度/季度）提交投后评价报告', level: 'LOW', levelLabel: '低', deadline: '30天', action: '催促提交，纳入绩效考核' },
        { code: 'INV-W10', name: '决策程序缺失', condition: '项目决策缺少必要的专家评审、环评或法务审查', level: 'LOW', levelLabel: '低', deadline: '30天', action: '补充决策程序材料，整改合规' },
      ],
      handleStatusMap: {
        PENDING: { type: 'danger', label: '待处理' },
        HANDLING: { type: 'warning', label: '处理中' },
        COMPLETED: { type: 'success', label: '已完成' },
      },
      warningTypes: [
        { value: 'UNAUTHORIZED', label: '越权投资' },
        { value: 'RETURN_DEVIATION', label: '收益偏离' },
        { value: 'PROGRESS_DELAY', label: '进度滞后' },
        { value: 'NON_MAIN_EXCEED', label: '非主业超限' },
        { value: 'COMPLIANCE', label: '决策程序' },
        { value: 'EXIT_OVERDUE', label: '清退逾期' },
      ],
      warningTypeMap: {
        UNAUTHORIZED: '越权投资',
        RETURN_DEVIATION: '收益偏离',
        PROGRESS_DELAY: '进度滞后',
        NON_MAIN_EXCEED: '非主业超限',
        COMPLIANCE: '决策程序',
        EXIT_OVERDUE: '清退逾期',
      },
    }
  },
  computed: {
    highCount() { return this.list.filter(i => i.level === 'HIGH' && i.handleStatus !== 'COMPLETED').length },
    midCount() { return this.list.filter(i => i.level === 'MEDIUM' && i.handleStatus !== 'COMPLETED').length },
    lowCount() { return this.list.filter(i => i.level === 'LOW' && i.handleStatus !== 'COMPLETED').length },
    statCards() {
      return [
        { label: '预警总数', value: this.list.length + ' 条', color: this.ipPrimary },
        { label: '高级预警', value: this.list.filter(i => i.level === 'HIGH').length + ' 条', color: '#FF4D4F' },
        { label: '中级预警', value: this.list.filter(i => i.level === 'MEDIUM').length + ' 条', color: '#FA8C16' },
        { label: '低级预警', value: this.list.filter(i => i.level === 'LOW').length + ' 条', color: this.ipAccent },
        { label: '待处理', value: this.list.filter(i => i.handleStatus === 'PENDING').length + ' 条', color: '#FF4D4F' },
        { label: '已完成', value: this.list.filter(i => i.handleStatus === 'COMPLETED').length + ' 条', color: '#52C41A' },
      ]
    },
    filteredList() {
      let data = [...this.list]
      if (this.queryForm.warningCode) data = data.filter(d => d.warningCode.includes(this.queryForm.warningCode))
      if (this.queryForm.warningType) data = data.filter(d => d.warningType === this.queryForm.warningType)
      if (this.queryForm.level) data = data.filter(d => d.level === this.queryForm.level)
      if (this.queryForm.handleStatus) data = data.filter(d => d.handleStatus === this.queryForm.handleStatus)
      return data
    },
  },
  created() {
    this.initCompanyId()
    this.fetchData()
    this.loadRules()
  },
  methods: {
    initCompanyId() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = userInfo.currentOrg || {}
        if (org.id) this.companyId = String(org.id)
      } catch (e) { console.warn('[initCompanyId]', e) }
    },
    async loadRules() {
      try {
        const res = await getWarningRuleList()
        if (res.result === 200 && res.data) {
          const levelMap = { HIGH: '高', MEDIUM: '中', LOW: '低' }
          this.ruleDefinitions = (res.data || []).map(r => ({
            code: r.ruleCode,
            name: r.ruleName,
            condition: r.triggerCondition,
            level: r.riskLevel,
            levelLabel: levelMap[r.riskLevel] || r.riskLevel,
            deadline: r.deadlineDays + '天',
            action: r.handleAction
          }))
        }
      } catch (e) { console.error(e) }
    },
    computeDeadline(triggerTime, level) {
      if (!triggerTime) return ''
      const days = level === 'HIGH' ? 7 : level === 'MEDIUM' ? 14 : 30
      const date = new Date(triggerTime)
      date.setDate(date.getDate() + days)
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      return `${y}-${m}-${d}`
    },
    async fetchData() {
      this.loading = true
      try {
        const params = { pageNumber: 1, pageSize: 100, companyId: this.companyId }
        if (this.queryForm.level) params.level = this.queryForm.level
        if (this.queryForm.handleStatus) params.status = this.queryForm.handleStatus
        if (this.queryForm.warningType) params.warningType = this.queryForm.warningType
        const res = await getInvestWarningList(params)
        if (res.result === 200 && res.data) {
          this.list = (res.data.tlist || []).map(w => {
            const deadline = this.computeDeadline(w.triggerTime, w.level)
            const isOverdue = deadline ? new Date() > new Date(deadline) : false
            return {
              ...w,
              company: w.companyName || '',
              handleStatus: w.status || 'PENDING',
              warningTypeLabel: this.warningTypeMap[w.warningType] || w.warningType,
              ruleCode: w.warningType || '',
              warningDesc: (w.projectName || '') + ' - ' + (this.warningTypeMap[w.warningType] || ''),
              triggerTime: w.triggerTime ? w.triggerTime.substring(0, 10) : '',
              deadline: deadline,
              isOverdue: isOverdue,
              handleHistory: [],
            }
          })
          this.total = res.data.totalRecord || 0
        }
      } catch (e) {
        this.$message.error('查询预警列表失败')
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = { warningCode: '', warningType: '', level: '', handleStatus: '' }
      this.fetchData()
    },
    handleSelectionChange(val) { this.selected = val },
    handleView(row) { this.currentWarning = row; this.handleRemark = ''; this.drawerVisible = true },
    async handleDispatch(row) {
      try {
        await this.$confirm('确认派发该预警给相关责任部门？', '派发预警', { type: 'warning' })
        const res = await dispatchInvestWarning({ warningId: row.warningId })
        if (res.result === 200) {
          this.$message.success('已派发成功')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '派发失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('派发操作失败')
      }
    },
    async handleComplete(row) {
      try {
        await this.$confirm('确认标记该预警为已处理完成？', '完成确认', { type: 'warning' })
        const res = await handleInvestWarning({ warningId: row.warningId, handleResult: '确认完成', handleUser: '当前用户' })
        if (res.result === 200) {
          this.$message.success('已标记完成')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '操作失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('操作失败')
      }
    },
    async handleBatchDispatch() {
      try {
        await this.$confirm(`确认批量派发 ${this.selected.length} 条预警？`, '批量派发', { type: 'warning' })
        const pendingItems = this.selected.filter(row => row.handleStatus === 'PENDING')
        if (pendingItems.length === 0) {
          this.$message.warning('没有待处理的预警可派发')
          return
        }
        const promises = pendingItems.map(row => dispatchInvestWarning({ warningId: row.warningId }))
        await Promise.all(promises)
        this.$message.success('批量派发成功')
        this.fetchData()
      } catch (e) {
        if (e !== 'cancel') this.$message.error('批量派发失败')
      }
    },
    async submitHandle() {
      if (!this.handleRemark) { this.$message.warning('请输入处理意见'); return }
      try {
        const res = await handleInvestWarning({
          warningId: this.currentWarning.warningId,
          handleResult: this.handleRemark,
          handleUser: '当前用户',
        })
        if (res.result === 200) {
          this.$message.success('处理意见已提交')
          this.drawerVisible = false
          this.fetchData()
        } else {
          this.$message.error(res.msg || '提交失败')
        }
      } catch (e) {
        this.$message.error('提交处理意见失败')
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.risk-warning-page { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px; padding: 16px 24px; margin-bottom: 12px; color: #fff;
  display: flex; justify-content: space-between; align-items: center;
  .banner-left { display: flex; align-items: center; gap: 14px; }
  .banner-icon { font-size: 32px; color: var(--ip-accent, #FAAD14); }
  h2 { margin: 0; font-size: 18px; }
  p { margin: 4px 0 0; font-size: 12px; opacity: 0.8; }
  .banner-tags { display: flex; }
}
.stat-row { margin-bottom: 12px; }
.stat-mini {
  background: #fff; border-left: 4px solid var(--ip-primary, #1A3A6B); border-radius: 6px;
  padding: 10px 14px; box-shadow: 0 1px 4px rgba(0,0,0,.06);
  .sm-value { font-size: 18px; font-weight: 700; }
  .sm-label { font-size: 12px; color: #888; margin-top: 2px; }
}
.query-card { border-left: 3px solid var(--ip-primary, #1A3A6B); margin-bottom: 0; }
.query-card .el-form-item { margin-bottom: 0; }
.card-header {
  display: flex; justify-content: space-between; align-items: center;
  font-size: 14px; font-weight: 600; color: var(--ip-primary, #1A3A6B);
  padding-bottom: 6px; border-bottom: 2px solid var(--ip-accent, #FAAD14);
}
.section-title { font-size: 13px; font-weight: 600; color: var(--ip-primary, #1A3A6B); margin-bottom: 8px; }
::v-deep .el-table th { background: #f5f7ff; color: var(--ip-primary, #1A3A6B); }
</style>
