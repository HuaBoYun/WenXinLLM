<template>
  <div class="sal-compliance">
    <!-- 绿色Banner -->
    <div class="sal-banner-green" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">薪酬合规检查</div>
      <div class="banner-sub">三维合规体系 · 不合法 / 不合规 / 不合理 · 问题清单 · 整改追踪</div>
    </div>

    <!-- KPI卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="8" v-for="k in kpiCards" :key="k.key">
        <div class="kpi-card" :style="{ borderLeft: `4px solid ${k.color}` }">
          <div class="kpi-label">{{ k.label }}</div>
          <div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div>
          <div class="kpi-desc">{{ k.desc }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- Tab分类 -->
    <el-card shadow="never" class="tab-card">
      <div slot="header">
        <span class="card-title">合规问题清单</span>
        <div style="float:right">
          <el-button size="small" type="primary" @click="runCheck">执行合规检查</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab">
        <el-tab-pane name="all">
          <span slot="label">全部 <el-badge :value="allList.length" type="danger"></el-badge></span>
        </el-tab-pane>
        <el-tab-pane name="illegal">
          <span slot="label"><span style="color:#F5222D">不合法</span> <el-badge :value="illegalList.length" type="danger"></el-badge></span>
        </el-tab-pane>
        <el-tab-pane name="noncompliant">
          <span slot="label"><span style="color:#FA8C16">不合规</span> <el-badge :value="noncompliantList.length" type="warning"></el-badge></span>
        </el-tab-pane>
        <el-tab-pane name="unreasonable">
          <span slot="label"><span style="color:#FAAD14">不合理</span> <el-badge :value="unreasonableList.length" type="warning"></el-badge></span>
        </el-tab-pane>
      </el-tabs>

      <el-table :data="currentTabData" border stripe size="small" :row-class-name="rowClass">
        <el-table-column prop="issueNo" label="问题编号" width="130"></el-table-column>
        <el-table-column prop="companyName" label="企业名称" width="130"></el-table-column>
        <el-table-column prop="dimension" label="合规维度" width="90" align="center">
          <template slot-scope="{ row }">
            <span :style="dimensionStyle(row.dimension)">{{ row.dimension }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="issueDesc" label="问题描述"></el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.riskLevel === 'HIGH' ? 'danger' : row.riskLevel === 'MEDIUM' ? 'warning' : 'info'" size="mini">
              {{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="整改状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.status === 'INVESTIGATING' ? 'warning' : row.status === 'CLOSED' ? 'success' : 'danger'" size="mini">
              {{ { PENDING: '待整改', INVESTIGATING: '整改中', CLOSED: '已关闭' }[row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="openRectify(row)">下发整改</el-button>
            <el-button type="text" size="mini" @click="viewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 整改弹窗 -->
    <el-dialog title="下发整改通知" :visible.sync="rectifyVisible" width="500px">
      <el-form :model="rectifyForm" label-width="100px" size="small">
        <el-form-item label="问题编号">
          <el-input v-model="rectifyForm.issueNo" readonly></el-input>
        </el-form-item>
        <el-form-item label="整改要求">
          <el-input v-model="rectifyForm.requirement" type="textarea" :rows="3" placeholder="请填写整改要求..."></el-input>
        </el-form-item>
        <el-form-item label="整改截止日期">
          <el-date-picker v-model="rectifyForm.deadline" type="date" value-format="yyyy-MM-dd" placeholder="选择截止日期" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="责任人">
          <el-input v-model="rectifyForm.responsible" placeholder="请填写整改责任人"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="rectifyVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRectify">下发整改</el-button>
      </span>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="合规问题详情" :visible.sync="drawerVisible" size="480px" direction="rtl">
      <div v-if="currentRow" class="drawer-content">
        <div class="issue-header">
          <el-tag :type="currentRow.riskLevel === 'HIGH' ? 'danger' : 'warning'" size="medium">{{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[currentRow.riskLevel] }}</el-tag>
          <span class="issue-no">{{ currentRow.issueNo }}</span>
        </div>
        <el-descriptions :column="1" border size="small" style="margin-top:16px">
          <el-descriptions-item label="企业名称">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="合规维度">
            <span :style="dimensionStyle(currentRow.dimension)">{{ currentRow.dimension }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="问题描述">{{ currentRow.issueDesc }}</el-descriptions-item>
          <el-descriptions-item label="整改状态">
            <el-tag :type="currentRow.status === 'INVESTIGATING' ? 'warning' : currentRow.status === 'CLOSED' ? 'success' : 'danger'" size="mini">
              {{ { PENDING: '待整改', INVESTIGATING: '整改中', CLOSED: '已关闭' }[currentRow.status] }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <div class="dim-explanation" :style="{ background: dimensionBg(currentRow.dimension) }">
          <div class="dim-title">{{ currentRow.dimension }}定义</div>
          <div class="dim-desc">{{ dimensionDesc(currentRow.dimension) }}</div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getComplianceIssueList, sendRectification, runComplianceCheck } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'



const DIM_DESC = {
  '不合法': '违反劳动法、工资支付条例、社会保险法等国家法律法规，存在法律合规风险。',
  '不合规': '违反国资委有关薪酬管理政策规定，如工资总额超核准额度、高管薪酬超限高令等。',
  '不合理': '薪酬分配结构或水平存在明显不合理之处，如效益联动脱钩、薪酬差距过大等。',
}

export default {
  name: 'SalaryComplianceCheck',
  data() {
    return {
      allList: [],
      activeTab: 'all',
      rectifyVisible: false,
      drawerVisible: false,
      currentRow: null,
      rectifyForm: { issueNo: '', requirement: '', deadline: '', responsible: '' },
    }
  },
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
    illegalList() { return this.allList.filter(r => r.dimension === '不合法') },
    noncompliantList() { return this.allList.filter(r => r.dimension === '不合规') },
    unreasonableList() { return this.allList.filter(r => r.dimension === '不合理') },
    currentTabData() {
      if (this.activeTab === 'illegal') return this.illegalList
      if (this.activeTab === 'noncompliant') return this.noncompliantList
      if (this.activeTab === 'unreasonable') return this.unreasonableList
      return this.allList
    },
    kpiCards() {
      return [
        { key: 'illegal', label: '不合法问题', value: this.illegalList.length + '项', color: '#F5222D', desc: '违反法律法规' },
        { key: 'noncompliant', label: '不合规问题', value: this.noncompliantList.length + '项', color: '#FA8C16', desc: '违反国资委政策' },
        { key: 'unreasonable', label: '不合理问题', value: this.unreasonableList.length + '项', color: '#FAAD14', desc: '薪酬结构失衡' },
      ]
    },
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await getComplianceIssueList({ pageSize: 50 })
        if (res.data && res.data.tlist) {
          this.allList = res.data.tlist.map(r => ({
            ...r,
            issueNo: r.issueNo,
            dimension: r.dimension,
            riskLevel: r.riskLevel,
            status: r.status,
          }))
        }
      } catch (e) { console.error('加载合规问题列表失败', e) }
    },
    rowClass({ row }) {
      return row.riskLevel === 'HIGH' ? 'row-high' : ''
    },
    dimensionStyle(d) {
      const map = { '不合法': { color: '#F5222D', fontWeight: '700' }, '不合规': { color: '#FA8C16', fontWeight: '700' }, '不合理': { color: '#FAAD14', fontWeight: '700' } }
      return map[d] || {}
    },
    dimensionBg(d) {
      const map = { '不合法': '#fff1f0', '不合规': '#fff7e6', '不合理': '#fffbe6' }
      return map[d] || '#f5f5f5'
    },
    dimensionDesc(d) { return DIM_DESC[d] || '' },
    openRectify(row) {
      this.rectifyForm = { issueNo: row.issueNo, requirement: '', deadline: '', responsible: '' }
      this.rectifyVisible = true
    },
    async submitRectify() {
      if (!this.rectifyForm.requirement) { this.$message.warning('请填写整改要求'); return }
      try {
        await sendRectification(this.rectifyForm)
        this.$message.success('整改通知已下发')
      } catch (e) {
        this.$message.error('整改通知下发失败')
        return
      }
      this.rectifyVisible = false
      this.loadData()
    },
    viewDetail(row) { this.currentRow = row; this.drawerVisible = true },
    async runCheck() {
      this.$message.info('正在执行薪酬合规检查...')
      try {
        const res = await runComplianceCheck()
        if (res.data) {
          this.$message.success(`合规检查完成，发现${res.data.failCount + res.data.warningCount}项问题`)
        } else {
          this.$message.success('合规检查完成')
        }
        this.loadData()
      } catch (e) {
        this.$message.error('合规检查执行失败')
      }
    },
  },
}
</script>

<style scoped>
.sal-compliance { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner-green {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 14px 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.kpi-value { font-size: 26px; font-weight: 700; }
.kpi-desc { font-size: 12px; color: #bfbfbf; margin-top: 4px; }
.tab-card { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
::v-deep .row-high td { background: #fff1f0 !important; }
.drawer-content { padding: 20px; }
.issue-header { display: flex; align-items: center; gap: 12px; }
.issue-no { font-size: 16px; font-weight: 600; color: #262626; }
.dim-explanation { margin-top: 16px; padding: 12px 16px; border-radius: 6px; }
.dim-title { font-weight: 600; margin-bottom: 8px; font-size: 13px; }
.dim-desc { font-size: 13px; color: #595959; line-height: 1.6; }
</style>
