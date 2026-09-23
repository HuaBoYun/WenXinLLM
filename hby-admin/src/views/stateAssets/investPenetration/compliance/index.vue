<template>
  <div class="app-container invest-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document-checked"></i><span>投资决策合规追踪</span></div>
      <div class="page-header-desc">追踪投资决策审批链路、越权投资识别与合规状态全程监控</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="14" class="stat-row">
      <el-col :span="6" v-for="c in statCards" :key="c.key">
        <div class="stat-card" :class="c.cls">
          <div class="stat-icon"><i :class="c.icon"></i></div>
          <div class="stat-body">
            <div class="stat-value">{{ c.value }}</div>
            <div class="stat-label">{{ c.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="项目名称"><el-input v-model="queryForm.projectName" placeholder="请输入" clearable style="width:180px" /></el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="queryForm.complianceStatus" placeholder="请选择" clearable style="width:130px">
            <el-option label="合规" value="COMPLIANT" /><el-option label="不合规" value="NON_COMPLIANT" /><el-option label="待审查" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item label="越权标记">
          <el-select v-model="queryForm.overrideFlag" placeholder="请选择" clearable style="width:110px">
            <el-option label="越权" value="Y" /><el-option label="正常" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item label="投资类型">
          <el-select v-model="queryForm.investType" placeholder="请选择" clearable style="width:120px">
            <el-option label="股权投资" value="EQUITY" /><el-option label="债权投资" value="DEBT" /><el-option label="基金投资" value="FUND" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 列表 -->
    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="list" border style="width:100%" :row-class-name="rowClassName">
        <el-table-column label="项目名称" prop="projectName" min-width="160" show-overflow-tooltip />
        <el-table-column label="投资类型" prop="investTypeLabel" width="100" align="center" />
        <el-table-column label="投资金额(万元)" prop="investAmount" width="130" align="right">
          <template slot-scope="{ row }"><span class="amount-text">{{ row.investAmount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="决策层级" prop="decisionLevel" width="90" align="center" />
        <el-table-column label="审批完整性" width="150" align="center">
          <template slot-scope="{ row }">
            <el-progress :percentage="row.approvalCompleteness"
              :color="row.approvalCompleteness < 80 ? '#fa8c16' : '#52c41a'"
              :stroke-width="10" />
          </template>
        </el-table-column>
        <el-table-column label="合规状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ COMPLIANT:'success', NON_COMPLIANT:'danger', PENDING:'warning' }[row.complianceStatus]" size="small">
              {{ { COMPLIANT:'合规', NON_COMPLIANT:'不合规', PENDING:'待审查' }[row.complianceStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="越权标记" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.overrideFlag === 'Y'" type="danger" size="small">越权</el-tag>
            <span v-else style="color:#67c23a">正常</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="handleChain(row)">审批链路</el-button>
            <el-divider direction="vertical" />
            <el-button size="mini" type="text" style="color:#fa8c16" @click="handleDispatch(row)">派单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right"
        :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="v => { queryForm.pageSize = v; fetchData() }"
        @current-change="v => { queryForm.pageNumber = v; fetchData() }" />
    </el-card>

    <!-- 审批链路抽屉 -->
    <el-drawer title="审批链路详情" :visible.sync="drawerVisible" size="700px" direction="rtl">
      <div v-if="currentRow" class="drawer-body">
        <el-card shadow="never" class="info-card">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="项目名称">{{ currentRow.projectName }}</el-descriptions-item>
            <el-descriptions-item label="投资类型">{{ currentRow.investTypeLabel }}</el-descriptions-item>
            <el-descriptions-item label="投资金额">{{ currentRow.investAmount.toLocaleString() }} 万元</el-descriptions-item>
            <el-descriptions-item label="决策层级">{{ currentRow.decisionLevel }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
        <el-divider content-position="left">审批链路</el-divider>
        <el-steps direction="vertical" :active="currentRow.approvalSteps.filter(s => s.status === 'done').length" style="padding:0 20px">
          <el-step v-for="(step, i) in currentRow.approvalSteps" :key="i"
            :title="step.node"
            :status="step.status === 'done' ? 'finish' : step.status === 'reject' ? 'error' : step.status === 'missing' ? 'error' : 'wait'">
            <div slot="description" class="step-desc">
              <span v-if="step.approver">审批人：{{ step.approver }}</span>
              <span v-if="step.time" style="margin-left:12px">时间：{{ step.time }}</span>
              <el-tag v-if="step.status === 'missing'" type="danger" size="mini" style="margin-left:8px">缺失</el-tag>
              <el-tag v-if="step.status === 'reject'" type="danger" size="mini" style="margin-left:8px">驳回</el-tag>
              <div v-if="step.opinion" style="margin-top:4px;color:#666">意见：{{ step.opinion }}</div>
            </div>
          </el-step>
        </el-steps>
        <el-divider content-position="left">合规性结论</el-divider>
        <el-alert :title="currentRow.complianceConclusion" :type="currentRow.overrideFlag === 'Y' ? 'error' : currentRow.complianceStatus === 'COMPLIANT' ? 'success' : 'warning'" show-icon :closable="false" />
      </div>
    </el-drawer>

    <!-- 派单对话框 -->
    <el-dialog title="违规投资核查派单" :visible.sync="dispatchVisible" width="480px">
      <el-form :model="dispatchForm" label-width="100px" size="small">
        <el-form-item label="违规类型">
          <el-select v-model="dispatchForm.violationType" style="width:100%">
            <el-option label="越权投资" value="越权投资" />
            <el-option label="审批缺失" value="审批缺失" />
            <el-option label="程序违规" value="程序违规" />
          </el-select>
        </el-form-item>
        <el-form-item label="核查责任人"><el-input v-model="dispatchForm.owner" placeholder="请输入" /></el-form-item>
        <el-form-item label="核查期限">
          <el-date-picker v-model="dispatchForm.deadline" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="核查要求"><el-input v-model="dispatchForm.requirement" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dispatchVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDispatch">确认派单</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getComplianceList, getComplianceChain, dispatchCompliance } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'


export default {
  name: 'InvestCompliance',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, list: [], total: 0,
      companyId: '',
      queryForm: { pageNumber: 1, pageSize: 10, projectName: '', complianceStatus: '', overrideFlag: '', investType: '' },
      drawerVisible: false, currentRow: null,
      dispatchVisible: false,
      dispatchForm: { violationType: '', owner: '', deadline: '', requirement: '' }
    }
  },
  computed: {
    statCards() {
      return [
        { key:'total', label:'追踪项目总数', value: this.list.length, icon:'el-icon-data-board', cls:'card-blue' },
        { key:'nc', label:'不合规项目数', value: this.list.filter(r => r.complianceStatus === 'NON_COMPLIANT').length, icon:'el-icon-warning', cls:'card-red' },
        { key:'ov', label:'越权投资数', value: this.list.filter(r => r.overrideFlag === 'Y').length, icon:'el-icon-remove-outline', cls:'card-orange' },
        { key:'pd', label:'待审查数', value: this.list.filter(r => r.complianceStatus === 'PENDING').length, icon:'el-icon-time', cls:'card-yellow' }
      ]
    }
  },
  created() { this.initCompanyId(); this.fetchData() },
  methods: {
    initCompanyId() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = userInfo.currentOrg || {}
        if (org.id) this.companyId = String(org.id)
      } catch (e) { console.warn('[initCompanyId]', e) }
    },
    async fetchData() {
      this.loading = true
      try {
        const params = { pageNumber: this.queryForm.pageNumber, pageSize: this.queryForm.pageSize, companyId: this.companyId }
        if (this.queryForm.projectName) params.projectName = this.queryForm.projectName
        if (this.queryForm.complianceStatus) params.complianceStatus = this.queryForm.complianceStatus
        // 越权标记筛选：传递 overrideFlag 给后端，后端通过 issueDesc 字段判断
        if (this.queryForm.overrideFlag) params.overrideFlag = this.queryForm.overrideFlag
        const res = await getComplianceList(params)
        if (res.result === 200 && res.data) {
          const tlist = res.data.tlist || res.data || []
          let mappedList = (Array.isArray(tlist) ? tlist : []).map(item => ({
            ...item,
            id: item.complianceId,
            complianceStatus: item.isCompliant,
            overrideFlag: (item.isCompliant === 'NON_COMPLIANT' && item.issueDesc && item.issueDesc.includes('越权')) ? 'Y' : 'N',
            investType: item.investType || 'EQUITY',
            investTypeLabel: { EQUITY: '股权投资', DEBT: '债权投资', FUND: '基金投资' }[item.investType] || '股权投资',
            investAmount: item.investAmount || 0,
            approvalCompleteness: item.isCompliant === 'COMPLIANT' ? 100 : item.isCompliant === 'PENDING' ? 80 : 60,
            complianceConclusion: item.issueDesc || '审批流程完整，合规通过',
            approvalSteps: this.buildApprovalSteps(item)
          }))
          // 投资类型前端本地过滤（后端暂无此字段支持）
          if (this.queryForm.investType) {
            mappedList = mappedList.filter(item => item.investType === this.queryForm.investType)
          }
          this.list = mappedList
          this.total = this.queryForm.investType ? mappedList.length : (res.data.totalRecord || mappedList.length)
        }
      } catch (e) {
        console.error('查询合规列表失败', e)
        this.$message.error('查询失败')
      } finally { this.loading = false }
    },
    buildApprovalSteps(item) {
      // 根据决策层级和合规状态生成审批链路
      const level = item.decisionLevel || '集团董事会'
      const isCompliant = item.isCompliant
      const steps = []
      if (level.includes('董事会')) {
        steps.push({ node: '业务部门申请', approver: '项目负责人', time: '', status: 'done', opinion: '同意立项' })
        steps.push({ node: '财务部审查', approver: '财务总监', time: '', status: 'done', opinion: '资金来源合规' })
        steps.push({ node: '法务部审查', approver: '法务总监', time: '', status: isCompliant === 'NON_COMPLIANT' ? 'missing' : 'done', opinion: isCompliant === 'NON_COMPLIANT' ? '' : '合同条款无异议' })
        steps.push({ node: '总经理审批', approver: '总经理', time: '', status: 'done', opinion: '同意' })
        steps.push({ node: '董事会决议', approver: '董事会', time: '', status: isCompliant === 'PENDING' ? 'wait' : isCompliant === 'NON_COMPLIANT' ? 'missing' : 'done', opinion: isCompliant === 'COMPLIANT' ? '全票通过' : '' })
      } else {
        steps.push({ node: '业务部门申请', approver: '项目负责人', time: '', status: 'done', opinion: '同意' })
        steps.push({ node: '财务部审查', approver: '财务经理', time: '', status: 'done', opinion: '财务可行' })
        steps.push({ node: '总经理审批', approver: '总经理', time: '', status: isCompliant === 'PENDING' ? 'wait' : 'done', opinion: isCompliant !== 'PENDING' ? '同意' : '' })
      }
      return steps
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.fetchData() },
    rowClassName({ row }) {
      if (row.overrideFlag === 'Y') return 'row-danger'
      if (row.approvalCompleteness < 80) return 'row-warn'
      return ''
    },
    async handleChain(row) {
      this.currentRow = { ...row }
      this.drawerVisible = true
      try {
        const res = await getComplianceChain(row.id || row.complianceId)
        if (res.result === 200 && res.data) {
          if (res.data.issueDesc) {
            this.currentRow.complianceConclusion = res.data.issueDesc
          }
          if (res.data.decisionLevel) {
            this.currentRow.decisionLevel = res.data.decisionLevel
          }
        }
      } catch (e) { /* ignore */ }
    },
    handleDispatch(row) {
      this.currentRow = row
      this.dispatchForm = { violationType: row.overrideFlag === 'Y' ? '越权投资' : '', owner: '', deadline: '', requirement: '' }
      this.dispatchVisible = true
    },
    async submitDispatch() {
      if (!this.dispatchForm.violationType || !this.dispatchForm.owner || !this.dispatchForm.deadline) {
        this.$message.warning('请填写完整派单信息'); return
      }
      try {
        await dispatchCompliance({ complianceId: this.currentRow.id || this.currentRow.complianceId })
        this.$message.success('核查派单已提交')
        this.dispatchVisible = false
        this.fetchData()
      } catch (e) {
        this.$message.error('派单失败')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.invest-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:14px; padding:14px 20px; background:linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%); border-radius:6px; color:#fff;
  .page-header-left { display:flex; align-items:center; font-size:16px; font-weight:600; i { font-size:22px; margin-right:10px; } }
  .page-header-desc { font-size:13px; opacity:.85; }
}
.stat-row { margin-bottom:14px; }
.stat-card { display:flex; align-items:center; padding:16px; border-radius:8px; background:#fff; box-shadow:0 1px 4px rgba(0,0,0,.08);
  .stat-icon { font-size:30px; padding:10px; border-radius:8px; margin-right:12px; }
  .stat-value { font-size:26px; font-weight:bold; }
  .stat-label { font-size:12px; color:#909399; margin-top:2px; }
  &.card-blue  { .stat-icon { color:#1890ff; background:#e6f7ff; } .stat-value { color:#1890ff; } }
  &.card-red   { .stat-icon { color:#ff4d4f; background:#fff1f0; } .stat-value { color:#ff4d4f; } }
  &.card-orange{ .stat-icon { color:#fa8c16; background:#fff7e6; } .stat-value { color:#fa8c16; } }
  &.card-yellow{ .stat-icon { color:var(--ip-accent, #FAAD14); background:#fffbe6; } .stat-value { color:var(--ip-accent, #FAAD14); } }
}
.search-card { margin-bottom:12px; border-left:3px solid var(--ip-secondary, #2A5298); }
.table-card { margin-top:0; }
.amount-text { font-weight:600; color:var(--ip-primary, #1A3A6B); }
.step-desc { font-size:12px; color:#666; line-height:1.8; }
.info-card { margin:16px 20px 0; }
.drawer-body { padding-bottom:20px; }
::v-deep .row-danger td { background:#fff1f0 !important; }
::v-deep .row-warn td { background:#fff7e6 !important; }
::v-deep .el-table th { background:#f0f7ff; }
::v-deep .el-card { border-radius:6px; }
</style>


