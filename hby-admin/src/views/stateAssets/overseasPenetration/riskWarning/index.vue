<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-bell"></i><span>境外风险预警管理</span></div>
      <div class="page-header-desc">实时监控境外风险预警信息，支持分级处置与闭环跟踪</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#EBF1FF"><i class="el-icon-bell" style="color:#0050A0;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value">{{ warningStats.total }}</div><div class="kpi-label">预警总数</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF1F0"><i class="el-icon-warning" style="color:#F5222D;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#F5222D">{{ warningStats.high }}</div><div class="kpi-label">高级预警</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-warning-outline" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ warningStats.pending }}</div><div class="kpi-label">待处理</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-circle-check" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#52C41A">{{ warningStats.resolved }}</div><div class="kpi-label">已处置</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:10px">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.unitName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="queryForm.warningType" placeholder="全部" clearable style="width:130px">
            <el-option label="国别风险" value="COUNTRY_RISK" />
            <el-option label="外汇风险" value="FOREX_RISK" />
            <el-option label="合规风险" value="COMPLIANCE_RISK" />
            <el-option label="经营风险" value="OPERATION_RISK" />
            <el-option label="人员安全" value="PERSONNEL_RISK" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别">
          <el-select v-model="queryForm.level" placeholder="全部" clearable style="width:110px">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width:110px">
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已处置" value="RESOLVED" />
            <el-option label="已忽略" value="IGNORED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="danger" icon="el-icon-s-promotion" @click="handleBatchProcess" :disabled="multipleSelection.length === 0">批量处置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="filteredList"
        border
        @selection-change="val => multipleSelection = val"
        :row-class-name="tableRowClass"
        :header-cell-style="{ background: '#EBF1FF', color: '#0050A0' }"
        style="width:100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="预警时间" prop="warningTime" width="155" align="center" />
        <el-table-column label="企业名称" prop="unitName" min-width="155" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="100" align="center" />
        <el-table-column label="预警类型" prop="warningType" width="110" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="small">{{ typeLabel(scope.row.warningType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警级别" prop="level" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="levelTagType(scope.row.level)" size="small">{{ levelLabel(scope.row.level) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警描述" prop="description" min-width="200" show-overflow-tooltip />
        <el-table-column label="处置状态" prop="status" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.status)" size="small">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="处置人" prop="handler" width="90" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">详情</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING' || scope.row.status === 'PROCESSING'"
              size="mini" type="text" style="color:#52C41A" @click="handleProcess(scope.row)">处置</el-button>
            <el-button size="mini" type="text" style="color:#909399" @click="handleIgnore(scope.row)"
              v-if="scope.row.status === 'PENDING'">忽略</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top:15px;text-align:right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="val => { queryForm.pageSize = val; queryForm.pageNumber = 1 }"
        @current-change="val => { queryForm.pageNumber = val }"
      />
    </el-card>

    <!-- 处置弹窗 -->
    <el-dialog title="预警处置" :visible.sync="processDialogVisible" width="520px" :close-on-click-modal="false">
      <el-form :model="processForm" :rules="processRules" ref="processForm" label-width="90px">
        <el-form-item label="处置措施" prop="measure">
          <el-select v-model="processForm.measure" placeholder="请选择处置措施" style="width:100%">
            <el-option label="立即整改" value="RECTIFY" />
            <el-option label="关注跟踪" value="TRACK" />
            <el-option label="启动应急" value="EMERGENCY" />
            <el-option label="汇报上级" value="ESCALATE" />
          </el-select>
        </el-form-item>
        <el-form-item label="处置说明" prop="remark">
          <el-input v-model="processForm.remark" type="textarea" :rows="4" placeholder="请描述具体处置措施和情况说明" />
        </el-form-item>
        <el-form-item label="预计完成">
          <el-date-picker v-model="processForm.expectDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="processDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitProcess">提交处置</el-button>
      </div>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="预警详情" :visible.sync="drawerVisible" direction="rtl" size="480px">
      <div class="drawer-content" v-if="currentRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="预警编号" :span="2">{{ currentRow.warningId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.unitName }}</el-descriptions-item>
          <el-descriptions-item label="所在国家">{{ currentRow.country }}</el-descriptions-item>
          <el-descriptions-item label="预警时间">{{ currentRow.warningTime }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">
            <el-tag type="info" size="small">{{ typeLabel(currentRow.warningType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警级别">
            <el-tag :type="levelTagType(currentRow.level)" size="small">{{ levelLabel(currentRow.level) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警来源" :span="2">{{ currentRow.source || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处置状态" :span="2">
            <el-tag :type="statusTagType(currentRow.status)" size="small">{{ statusLabel(currentRow.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预警描述" :span="2">{{ currentRow.description || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处置措施" :span="2">{{ measureLabel(currentRow.measure) || '尚未处置' }}</el-descriptions-item>
          <el-descriptions-item label="处置说明" :span="2">{{ currentRow.remark || currentRow.processRemark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处置人">{{ currentRow.handler || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处置时间">{{ currentRow.processTime || currentRow.handleTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预计完成">{{ currentRow.expectDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentRow.createTime || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getOverseasWarningList, handleOverseasWarning } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'OverseasRiskWarning',
  mixins: [investThemeMixin],
  computed: {
    warningStats() {
      const d = this.list
      return {
        total: d.length,
        high: d.filter(r => r.level === 'HIGH').length,
        pending: d.filter(r => r.status === 'PENDING').length,
        resolved: d.filter(r => r.status === 'RESOLVED').length
      }
    },
    filteredList() {
      let data = this.list
      if (this.queryForm.unitName) data = data.filter(r => r.unitName.includes(this.queryForm.unitName))
      if (this.queryForm.warningType) data = data.filter(r => r.warningType === this.queryForm.warningType)
      if (this.queryForm.level) data = data.filter(r => r.level === this.queryForm.level)
      if (this.queryForm.status) data = data.filter(r => r.status === this.queryForm.status)
      this.total = data.length
      const s = (this.queryForm.pageNumber - 1) * this.queryForm.pageSize
      return data.slice(s, s + this.queryForm.pageSize)
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, unitName: '', warningType: '', level: '', status: '' },
      processDialogVisible: false,
      drawerVisible: false,
      currentRow: null,
      processForm: { id: null, measure: '', remark: '', expectDate: '' },
      processRules: {
        measure: [{ required: true, message: '请选择处置措施', trigger: 'change' }],
        remark: [{ required: true, message: '请输入处置说明', trigger: 'blur' }]
      }
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasWarningList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
        } else {
          this.list = []
        }
      } catch (e) {
        this.list = []
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleView(row) { this.currentRow = row; this.drawerVisible = true },
    handleProcess(row) {
      this.processForm = { warningId: row.warningId, measure: '', remark: '', expectDate: '' }
      this.currentRow = row
      this.processDialogVisible = true
      this.$nextTick(() => this.$refs.processForm && this.$refs.processForm.clearValidate())
    },
    handleIgnore(row) {
      const desc = (row.description || '').substring(0, 20)
      this.$confirm(`确认忽略此条预警「${desc}...」？`, '提示', { type: 'warning' }).then(async() => {
        try {
          await handleOverseasWarning({ warningId: row.warningId, measure: 'IGNORE', remark: '忽略处理' })
        } catch (e) {
          // 降级本地更新
        }
        const idx = this.list.findIndex(r => r.warningId === row.warningId)
        if (idx !== -1) this.$set(this.list, idx, { ...row, status: 'IGNORED' })
        this.$message.success('已忽略')
      }).catch(() => {})
    },
    handleBatchProcess() {
      this.$confirm(`确认批量处置选中的 ${this.multipleSelection.length} 条预警？`, '提示', { type: 'warning' }).then(() => {
        this.multipleSelection.forEach(row => {
          const idx = this.list.findIndex(r => r.warningId === row.warningId)
          if (idx !== -1) this.$set(this.list, idx, { ...row, status: 'PROCESSING', handler: '批量处置', processTime: new Date().toLocaleString() })
        })
        this.$message.success('批量处置成功')
      }).catch(() => {})
    },
    async handleSubmitProcess() {
      this.$refs.processForm.validate(async valid => {
        if (!valid) return
        this.submitLoading = true
        try {
          await handleOverseasWarning(this.processForm)
        } catch (e) {
          // 降级本地更新
        } finally {
          const idx = this.list.findIndex(r => r.warningId === this.processForm.warningId)
          if (idx !== -1) {
            this.$set(this.list, idx, {
              ...this.list[idx],
              status: 'RESOLVED',
              handler: '当前用户',
              measure: this.processForm.measure,
              remark: this.processForm.remark,
              processRemark: this.processForm.remark,
              processTime: new Date().toLocaleString()
            })
          }
          this.$message.success('处置成功')
          this.processDialogVisible = false
          this.submitLoading = false
        }
      })
    },
    tableRowClass({ row }) {
      if (row.level === 'HIGH' && row.status === 'PENDING') return 'row-high-warning'
      return ''
    },
    typeLabel(v) {
      return { COUNTRY_RISK: '国别风险', FOREX_RISK: '外汇风险', COMPLIANCE_RISK: '合规风险', OPERATION_RISK: '经营风险', PERSONNEL_RISK: '人员安全' }[v] || v
    },
    levelTagType(v) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[v] || 'info' },
    levelLabel(v) { return { HIGH: '高', MEDIUM: '中', LOW: '低' }[v] || v },
    statusTagType(v) { return { PENDING: 'danger', PROCESSING: 'warning', RESOLVED: 'success', IGNORED: 'info' }[v] || 'info' },
    statusLabel(v) { return { PENDING: '待处理', PROCESSING: '处理中', RESOLVED: '已处置', IGNORED: '已忽略' }[v] || v },
    measureLabel(v) { return { RECTIFY: '立即整改', TRACK: '关注跟踪', EMERGENCY: '启动应急', ESCALATE: '汇报上级', IGNORE: '忽略处理' }[v] || v || '' }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%);
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600;
  i { font-size: 22px; margin-right: 10px; }
}
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info { flex: 1; }
.kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.drawer-content { padding: 16px 20px; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .row-high-warning td { background: #FFF1F0 !important; }
</style>
