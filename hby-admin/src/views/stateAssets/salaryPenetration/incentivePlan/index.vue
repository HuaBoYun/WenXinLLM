<template>
  <div class="sal-incentive">
    <div class="sal-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">中长期激励计划</div>
      <div class="banner-sub">激励方案全链路追踪 · 审批状态监控 · 合规性核查 · 实施进度管理</div>
    </div>

    <!-- KPI卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="k in kpiCards" :key="k.key">
        <div class="kpi-card"><div class="kpi-label">{{ k.label }}</div><div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div></div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">激励类型分布</span></div>
          <div ref="typePie" style="height:240px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">激励计划实施进度</span></div>
          <div class="gantt-area">
            <div v-for="item in tableData.filter(r => r.approvalStatus !== 'REJECTED')" :key="item.planNo" class="gantt-row">
              <div class="gantt-name">{{ item.companyName.replace('集团','').replace('科技','') }} · {{ item.incentiveType }}</div>
              <div class="gantt-bar-wrap">
                <el-progress
                  :percentage="item.progress"
                  :color="item.approvalStatus === 'COMPLETED' ? '#52C41A' : item.approvalStatus === 'PENDING' ? '#d9d9d9' : '#1677FF'"
                  :stroke-width="14"
                ></el-progress>
              </div>
              <div class="gantt-status">
                <el-tag :type="statusTag(item.approvalStatus).type" size="mini">{{ statusTag(item.approvalStatus).text }}</el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 激励计划台账 -->
    <el-card shadow="never" class="table-card">
      <div slot="header">
        <span class="card-title">激励计划台账</span>
        <div style="float:right">
          <el-button size="small" type="primary" icon="el-icon-plus" @click="openEdit(null)">新增计划</el-button>
        </div>
      </div>
      <el-table :data="tableData" border stripe size="small" :row-class-name="rowClass">
        <el-table-column prop="planNo" label="计划编号" width="130"></el-table-column>
        <el-table-column prop="companyName" label="企业名称" width="130"></el-table-column>
        <el-table-column prop="incentiveType" label="激励类型" width="110" align="center"></el-table-column>
        <el-table-column prop="scale" label="激励规模（万元）" align="right">
          <template slot-scope="{ row }">{{ (row.scale || 0).toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="personCount" label="激励人数" align="center"></el-table-column>
        <el-table-column prop="startDate" label="起始日期" align="center" width="100"></el-table-column>
        <el-table-column prop="endDate" label="截止日期" align="center" width="100"></el-table-column>
        <el-table-column prop="progress" label="实施进度" align="center" width="120">
          <template slot-scope="{ row }">
            <el-progress :percentage="row.progress" :stroke-width="8" :color="row.progress === 100 ? '#52C41A' : '#1677FF'"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="approvalStatus" label="审批状态" align="center" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="statusTag(row.approvalStatus).type" size="mini">{{ statusTag(row.approvalStatus).text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="openEdit(row)">编辑</el-button>
            <el-button type="text" size="mini" @click="viewDetail(row)">详情</el-button>
            <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="editForm.planId ? '编辑激励计划' : '新增激励计划'" :visible.sync="editVisible" width="560px">
      <el-form :model="editForm" :rules="editRules" ref="editRef" label-width="120px" size="small">
        <el-form-item label="企业" prop="companyName">
          <el-select v-model="editForm.companyName" style="width:100%">
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激励类型" prop="incentiveType">
          <el-select v-model="editForm.incentiveType" style="width:100%">
            <el-option label="股权激励" value="股权激励"></el-option>
            <el-option label="超额利润分享" value="超额利润分享"></el-option>
            <el-option label="跟投机制" value="跟投机制"></el-option>
            <el-option label="任期激励" value="任期激励"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="激励规模（万元）" prop="scale">
          <el-input-number v-model="editForm.scale" :min="0" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="激励人数" prop="personCount">
          <el-input-number v-model="editForm.personCount" :min="1" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="计划周期">
          <el-date-picker v-model="editForm.dateRange" type="daterange" range-separator="至" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </span>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="激励计划详情" :visible.sync="drawerVisible" size="460px" direction="rtl">
      <div v-if="currentRow" class="drawer-content">
        <el-descriptions :column="2" border size="small" :title="currentRow.planNo">
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="激励类型">{{ currentRow.incentiveType }}</el-descriptions-item>
          <el-descriptions-item label="激励人数">{{ currentRow.personCount }}人</el-descriptions-item>
          <el-descriptions-item label="激励规模">{{ (currentRow.scale || 0).toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="实施进度">{{ currentRow.progress }}%</el-descriptions-item>
          <el-descriptions-item label="计划周期" :span="2">{{ currentRow.startDate }} 至 {{ currentRow.endDate }}</el-descriptions-item>
          <el-descriptions-item label="审批状态" :span="2">
            <el-tag :type="statusTag(currentRow.approvalStatus).type">{{ statusTag(currentRow.approvalStatus).text }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getIncentivePlanList, saveIncentivePlan, deleteIncentivePlan } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'



const STATUS_MAP = {
  ACTIVE: { type: 'success', text: '实施中' },
  PENDING: { type: 'info', text: '待审批' },
  REVIEWING: { type: 'warning', text: '审批中' },
  COMPLETED: { type: '', text: '已兑现' },
  REJECTED: { type: 'danger', text: '已驳回' },
}

export default {
  name: 'SalaryIncentivePlan',
  data() {
    return {
      tableData: [],
      editVisible: false,
      drawerVisible: false,
      currentRow: null,
      editForm: { companyName: '', incentiveType: '', scale: 0, personCount: 0, dateRange: [] },
      editRules: {
        companyName: [{ required: true, message: '请选择企业' }],
        incentiveType: [{ required: true, message: '请选择激励类型' }],
        scale: [{ required: true, message: '请填写激励规模' }],
      },
      charts: {},
      companyOptions: [],
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
    kpiCards() {
      const active = this.tableData.filter(r => r.approvalStatus === 'ACTIVE').length
      const pending = this.tableData.filter(r => r.approvalStatus === 'PENDING' || r.approvalStatus === 'REVIEWING').length
      const completed = this.tableData.filter(r => r.approvalStatus === 'COMPLETED').length
      return [
        { key: 'total', label: '激励计划总数', value: this.tableData.length, color: '#1677FF' },
        { key: 'active', label: '实施中', value: active, color: '#52C41A' },
        { key: 'pending', label: '审批中/待审批', value: pending, color: '#FA8C16' },
        { key: 'completed', label: '已兑现', value: completed, color: '#8c8c8c' },
      ]
    },
  },
  mounted() {
    this.loadData().then(() => {
      this.$nextTick(() => this.initTypePie())
    })
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(c => c && c.dispose())
  },
  methods: {
    async loadData() {
      try {
        const res = await getIncentivePlanList({ pageSize: 20 })
        if (res.data && res.data.tlist) {
          this.tableData = res.data.tlist.map(r => ({
            ...r,
            progress: r.progress || 0,
            approvalStatus: r.approvalStatus || r.status || 'PENDING',
          }))
          // 从数据中提取企业选项
          const companies = [...new Set(this.tableData.map(r => r.companyName).filter(Boolean))]
          if (companies.length) this.companyOptions = companies
        }
      } catch (e) { console.error('加载激励计划列表失败', e) }
    },
    rowClass({ row }) {
      return row.approvalStatus === 'REJECTED' ? 'row-rejected' : ''
    },
    statusTag(s) { return STATUS_MAP[s] || { type: 'info', text: s } },
    openEdit(row) {
      if (row) {
        // 编辑模式：只取表单需要的字段 + planId用于后端识别更新
        this.editForm = {
          planId: row.planId,
          companyName: row.companyName || '',
          incentiveType: row.incentiveType || '',
          scale: row.scale || 0,
          personCount: row.personCount || 0,
          dateRange: (row.startDate && row.endDate) ? [row.startDate, row.endDate] : [],
        }
      } else {
        this.editForm = { companyName: '', incentiveType: '', scale: 0, personCount: 0, dateRange: [] }
      }
      this.editVisible = true
    },
    async submitEdit() {
      this.$refs.editRef.validate(async valid => {
        if (!valid) return
        try {
          const data = { ...this.editForm }
          if (data.dateRange && data.dateRange.length === 2) {
            data.startDate = data.dateRange[0]
            data.endDate = data.dateRange[1]
          }
          delete data.dateRange
          await saveIncentivePlan(data)
          this.$message.success('保存成功')
        } catch (e) {
          this.$message.error('保存失败')
          return
        }
        this.editVisible = false
        this.loadData()
      })
    },
    viewDetail(row) { this.currentRow = row; this.drawerVisible = true },
    handleDelete(row) {
      this.$confirm('确认删除该激励计划？', '提示', { type: 'warning' }).then(async () => {
        try {
          await deleteIncentivePlan(row.planId)
          this.$message.success('删除成功')
          this.loadData()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    initTypePie() {
      const ec = echarts
      if (!ec) return
      const c = ec.init(this.$refs.typePie)
      this.charts.typePie = c
      const types = {}
      this.tableData.forEach(r => { types[r.incentiveType] = (types[r.incentiveType] || 0) + 1 })
      c.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        series: [{ type: 'pie', radius: ['35%', '65%'], data: Object.entries(types).map(([k, v]) => ({ name: k, value: v })), label: { formatter: '{b}\n{c}个' } }],
      })
    },
  },
}
</script>

<style scoped>
.sal-incentive { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 14px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.kpi-value { font-size: 24px; font-weight: 700; }
.chart-row { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.gantt-area { padding: 8px 4px; }
.gantt-row { display: flex; align-items: center; margin-bottom: 12px; gap: 8px; }
.gantt-name { width: 120px; font-size: 12px; flex-shrink: 0; }
.gantt-bar-wrap { flex: 1; }
.gantt-status { width: 60px; }
.table-card { margin-bottom: 16px; }
::v-deep .row-rejected td { background: #fffbe6 !important; }
.drawer-content { padding: 20px; }
</style>
