<template>
  <div class="sal-warning">
    <!-- 红色Banner -->
    <div class="sal-banner-red" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-content">
        <div>
          <div class="banner-title"><i class="el-icon-warning"></i> 薪酬风险预警管理</div>
          <div class="banner-sub">工资超标 · 效益脱钩 · 高管超限 · 激励违规 · 人工成本失控 · 多维度自动预警</div>
        </div>
        <el-row :gutter="12" class="banner-kpi">
          <el-col :span="6" v-for="k in bannerKpis" :key="k.key">
            <div class="bkpi-card">
              <div class="bkpi-val" :style="{ color: k.color }">{{ k.value }}</div>
              <div class="bkpi-label">{{ k.label }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 查询条件 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.level" clearable placeholder="全部" style="width:110px">
            <el-option label="高危" value="HIGH"></el-option>
            <el-option label="中危" value="MEDIUM"></el-option>
            <el-option label="低危" value="LOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="queryForm.status" clearable placeholder="全部" style="width:120px">
            <el-option label="待处置" value="PENDING"></el-option>
            <el-option label="处置中" value="PROCESSING"></el-option>
            <el-option label="核查中" value="INVESTIGATING"></el-option>
            <el-option label="已关闭" value="CLOSED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="never" class="warn-card">
      <div slot="header"><span class="card-title">薪酬预警列表</span></div>
      <el-table :data="warningList" border size="small" :row-class-name="rowClass">
        <el-table-column prop="warnNo" label="预警编号" width="160"></el-table-column>
        <el-table-column prop="companyName" label="企业名称" width="130"></el-table-column>
        <el-table-column prop="warnType" label="预警类型"></el-table-column>
        <el-table-column prop="ruleCode" label="规则编号" width="100" align="center"></el-table-column>
        <el-table-column prop="level" label="风险等级" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.level === 'HIGH' ? 'danger' : row.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">
              {{ { HIGH: '高危', MEDIUM: '中危', LOW: '低危' }[row.level] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="triggerTime" label="触发时间" width="160" align="center"></el-table-column>
        <el-table-column prop="status" label="处置状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="statusTag(row.status).type" size="mini">{{ statusTag(row.status).text }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="openHandle(row)" :disabled="row.status === 'CLOSED'">处置</el-button>
            <el-button type="text" size="mini" @click="viewDetail(row)">详情</el-button>
            <el-button type="text" size="mini" style="color:#8c8c8c" @click="closeWarning(row)" :disabled="row.status === 'CLOSED'">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 处置弹窗 -->
    <el-dialog title="预警处置" :visible.sync="handleVisible" width="500px">
      <div v-if="currentRow" class="handle-info">
        <el-alert :title="currentRow.warnType" :description="currentRow.triggerCondition" type="error" :closable="false" show-icon style="margin-bottom:16px"></el-alert>
      </div>
      <el-form :model="handleForm" label-width="100px" size="small">
        <el-form-item label="处置方式">
          <el-select v-model="handleForm.method" style="width:100%">
            <el-option label="责令整改" value="RECTIFY"></el-option>
            <el-option label="启动核查" value="INVESTIGATE"></el-option>
            <el-option label="约谈企业" value="TALK"></el-option>
            <el-option label="移交纪检" value="TRANSFER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处置说明">
          <el-input v-model="handleForm.remark" type="textarea" :rows="3" placeholder="请填写处置说明..."></el-input>
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker v-model="handleForm.deadline" type="date" value-format="yyyy-MM-dd" placeholder="选择截止日期" style="width:100%"></el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="danger" @click="submitHandle">确认处置</el-button>
      </span>
    </el-dialog>

    <!-- 详情抽屉（含时间轴） -->
    <el-drawer title="预警详情" :visible.sync="drawerVisible" size="480px" direction="rtl">
      <div v-if="currentRow" class="drawer-content">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="预警编号" :span="2">{{ currentRow.warnNo }}</el-descriptions-item>
          <el-descriptions-item label="企业名称">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="currentRow.level === 'HIGH' ? 'danger' : 'warning'" size="mini">{{ { HIGH: '高危', MEDIUM: '中危', LOW: '低危' }[currentRow.level] }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警类型" :span="2">{{ currentRow.warnType }}</el-descriptions-item>
          <el-descriptions-item label="触发条件" :span="2">{{ currentRow.triggerCondition }}</el-descriptions-item>
          <el-descriptions-item label="规则编号">{{ currentRow.ruleCode }}</el-descriptions-item>
          <el-descriptions-item label="触发时间">{{ currentRow.triggerTime }}</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top:20px">
          <div style="font-weight:600;margin-bottom:12px">处置进度</div>
          <el-timeline v-if="currentRow.timeline && currentRow.timeline.length">
            <el-timeline-item v-for="(t, i) in currentRow.timeline" :key="i" :timestamp="t.time" :type="t.type">{{ t.content }}</el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无处置记录" :image-size="60"></el-empty>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getSalaryWarningList, handleSalaryWarning, closeSalaryWarning } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'



const STATUS_MAP = { PENDING: { type: 'danger', text: '待处置' }, PROCESSING: { type: 'warning', text: '处置中' }, INVESTIGATING: { type: 'warning', text: '核查中' }, CLOSED: { type: 'info', text: '已关闭' } }

export default {
  name: 'SalaryRiskWarning',
  data() {
    return {
      warningList: [],
      queryForm: { level: '', status: '' },
      handleVisible: false,
      drawerVisible: false,
      currentRow: null,
      handleForm: { method: 'RECTIFY', remark: '', deadline: '' },
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
    bannerKpis() {
      const high = this.warningList.filter(r => r.level === 'HIGH' && r.status !== 'CLOSED').length
      const medium = this.warningList.filter(r => r.level === 'MEDIUM' && r.status !== 'CLOSED').length
      const low = this.warningList.filter(r => r.level === 'LOW' && r.status !== 'CLOSED').length
      const processing = this.warningList.filter(r => ['PROCESSING', 'INVESTIGATING'].includes(r.status)).length
      return [
        { key: 'high', label: '高危预警', value: high, color: '#FF7875' },
        { key: 'medium', label: '中危预警', value: medium, color: '#FFD666' },
        { key: 'low', label: '低危预警', value: low, color: '#95DE64' },
        { key: 'processing', label: '处置中', value: processing, color: '#69C0FF' },
      ]
    },
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await getSalaryWarningList({ ...this.queryForm, pageSize: 50 })
        if (res.data && res.data.tlist) {
          this.warningList = res.data.tlist.map(r => {
            // 从后端数据构建时间轴
            const timeline = []
            if (r.createTime || r.triggerTime) {
              timeline.push({ time: r.triggerTime || r.createTime, content: '预警触发：' + (r.warnType || r.warningType || ''), type: 'danger' })
            }
            if (r.handleTime && r.handleResult) {
              timeline.push({ time: r.handleTime, content: '处置：' + r.handleResult, type: 'warning' })
            }
            if (r.status === 'CLOSED') {
              timeline.push({ time: r.handleTime || r.createTime, content: '预警已关闭', type: 'info' })
            }
            return {
              ...r,
              warnNo: r.warnNo || r.warningCode,
              warnType: r.warnType || r.warningType,
              triggerTime: r.triggerTime || r.createTime,
              ruleCode: r.ruleCode || '',
              triggerCondition: r.triggerCondition || r.warningContent,
              timeline: timeline,
            }
          })
        }
      } catch (e) { console.error('加载薪酬预警列表失败', e) }
    },
    handleReset() { this.queryForm = { level: '', status: '' }; this.loadData() },
    rowClass({ row }) {
      return row.level === 'HIGH' && row.status !== 'CLOSED' ? 'row-high' : row.level === 'MEDIUM' && row.status !== 'CLOSED' ? 'row-medium' : ''
    },
    statusTag(s) { return STATUS_MAP[s] || { type: 'info', text: s } },
    openHandle(row) { this.currentRow = row; this.handleForm = { method: 'RECTIFY', remark: '', deadline: '' }; this.handleVisible = true },
    async submitHandle() {
      if (!this.handleForm.remark) { this.$message.warning('请填写处置说明'); return }
      try {
        // 使用 warningId（主键）而非 warnNo（编号）
        await handleSalaryWarning({ id: this.currentRow.warningId, ...this.handleForm })
        this.$message.success('处置操作已提交')
      } catch (e) {
        this.$message.error('处置操作提交失败')
        return
      }
      this.handleVisible = false
      this.loadData()
    },
    async closeWarning(row) {
      this.$confirm('确认关闭该预警？', '提示', { type: 'warning' }).then(async () => {
        try {
          // 使用 warningId（主键）而非 warnNo（编号）
          await closeSalaryWarning(row.warningId)
          this.$message.success('预警已关闭')
          this.loadData()
        } catch (e) {
          this.$message.error('关闭预警失败')
        }
      }).catch(() => {})
    },
    viewDetail(row) { this.currentRow = row; this.drawerVisible = true },
  },
}
</script>

<style scoped>
.sal-warning { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner-red {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-content { }
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin-bottom: 16px; }
.banner-kpi { }
.bkpi-card { background: rgba(255,255,255,0.15); border-radius: 6px; padding: 10px 12px; text-align: center; }
.bkpi-val { font-size: 26px; font-weight: 700; }
.bkpi-label { font-size: 12px; opacity: 0.9; }
.query-card { margin-bottom: 12px; }
.warn-card { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
::v-deep .row-high td { background: #fff1f0 !important; }
::v-deep .row-medium td { background: #fffbe6 !important; }
.handle-info { margin-bottom: 8px; }
.drawer-content { padding: 20px; }
</style>
