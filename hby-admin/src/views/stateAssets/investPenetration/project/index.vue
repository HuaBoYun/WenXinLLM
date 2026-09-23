<template>
  <div class="invest-page" :style="themeVars">
    <!-- 页面标题栏 -->
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document"></i><span class="page-title">投资项目台账</span></div>
      <div class="page-header-desc">管理和跟踪所有投资项目全生命周期信息，穿透核查决策合规性与回报真实性</div>
    </div>

    <!-- 统计汇总 -->
    <el-row :gutter="12" style="margin-bottom:12px">
      <el-col :span="4" v-for="k in summaryKpi" :key="k.label">
        <div class="kpi-mini" :style="{borderLeftColor: k.color}">
          <div class="kpi-v" :style="{color:k.color}">{{ k.value }}</div>
          <div class="kpi-l">{{ k.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="queryForm.projectName" placeholder="请输入项目名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="投资类型" prop="investType">
          <el-select v-model="queryForm.investType" placeholder="请选择" clearable style="width: 120px">
            <el-option label="股权投资" value="EQUITY" />
            <el-option label="债权投资" value="DEBT" />
            <el-option label="基金投资" value="FUND" />
            <el-option label="混合型" value="MIXED" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目状态" prop="projectStatus">
          <el-select v-model="queryForm.projectStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="执行中" value="EXECUTING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已退出" value="EXITED" />
            <el-option label="暂停" value="PAUSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否主业" prop="isMainBiz">
          <el-select v-model="queryForm.isMainBiz" placeholder="请选择" clearable style="width: 100px">
            <el-option label="主业" :value="true" />
            <el-option label="非主业" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增项目</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button size="small" icon="el-icon-download" @click="handleExport">导出台账</el-button>
      </div>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="list" border @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="项目编号" prop="projectId" width="130" />
        <el-table-column label="项目名称" prop="projectName" min-width="170" show-overflow-tooltip />
        <el-table-column label="投资类型" prop="investTypeLabel" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag size="mini" type="info">{{ row.investTypeLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="投资企业" prop="company" width="100" />
        <el-table-column label="投资金额(万元)" prop="investAmount" width="130" align="right">
          <template slot-scope="{row}"><span class="amount-text">{{ Number(row.investAmount).toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="是否主业" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.isMainBiz ? 'success' : 'warning'" size="mini">{{ row.isMainBiz ? '主业' : '非主业' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="项目状态" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="statusTagMap[row.projectStatus]" size="mini">{{ statusTextMap[row.projectStatus] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预期收益率" prop="expectedReturn" width="100" align="right">
          <template slot-scope="{row}"><span>{{ row.expectedReturn }}%</span></template>
        </el-table-column>
        <el-table-column label="实际收益率" width="100" align="right">
          <template slot-scope="{row}">
            <span :style="{color: row.actualReturn < row.expectedReturn * 0.7 ? '#FF4D4F' : row.actualReturn < row.expectedReturn ? '#FA8C16' : '#52C41A', fontWeight:600}">
              {{ row.actualReturn !== null ? row.actualReturn + '%' : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="立项日期" prop="approvalDate" width="100" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button size="mini" type="text" @click="handleView(row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleDrill(row)">穿透</el-button>
            <el-button size="mini" type="text" style="color:#FF4D4F" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination background style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper"
        :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="720px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" placeholder="请输入项目名称" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资类型" prop="investType">
              <el-select v-model="form.investType" placeholder="请选择" :disabled="dialogType === 'view'" style="width:100%">
                <el-option label="股权投资" value="EQUITY" />
                <el-option label="债权投资" value="DEBT" />
                <el-option label="基金投资" value="FUND" />
                <el-option label="混合型" value="MIXED" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="投资企业" prop="companyId">
              <el-select v-model="form.companyId" placeholder="请选择" :disabled="dialogType === 'view'" style="width:100%">
                <el-option v-for="c in companyOptions" :key="c.value" :label="c.label" :value="c.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投资金额(万元)" prop="investAmount">
              <el-input-number v-model="form.investAmount" :min="0" :precision="2" style="width:100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="是否主业" prop="isMainBiz">
              <el-radio-group v-model="form.isMainBiz" :disabled="dialogType === 'view'">
                <el-radio :label="true">主业</el-radio>
                <el-radio :label="false">非主业</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目状态" prop="projectStatus">
              <el-select v-model="form.projectStatus" :disabled="dialogType === 'view'" style="width:100%">
                <el-option label="执行中" value="EXECUTING" />
                <el-option label="已完成" value="COMPLETED" />
                <el-option label="已退出" value="EXITED" />
                <el-option label="暂停" value="PAUSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="预期收益率(%)" prop="expectedReturn">
              <el-input-number v-model="form.expectedReturn" :min="0" :max="100" :precision="1" style="width:100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="立项日期" prop="approvalDate">
              <el-date-picker v-model="form.approvalDate" type="date" value-format="yyyy-MM-dd" style="width:100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" :disabled="dialogType === 'view'" />
        </el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 穿透分析弹窗 -->
    <el-dialog title="投资穿透分析" :visible.sync="drillVisible" width="680px" :close-on-click-modal="false">
      <div v-if="drillRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="项目编号">{{ drillRow.projectId }}</el-descriptions-item>
          <el-descriptions-item label="项目名称">{{ drillRow.projectName }}</el-descriptions-item>
          <el-descriptions-item label="投资类型">{{ drillRow.investTypeLabel }}</el-descriptions-item>
          <el-descriptions-item label="投资企业">{{ drillRow.company }}</el-descriptions-item>
          <el-descriptions-item label="投资金额">{{ drillRow.investAmount ? Number(drillRow.investAmount).toLocaleString() : 0 }} 万元</el-descriptions-item>
          <el-descriptions-item label="是否主业">
            <el-tag :type="drillRow.isMainBiz ? 'success' : 'warning'" size="mini">{{ drillRow.isMainBiz ? '主业' : '非主业' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预期收益率">{{ drillRow.expectedReturn }}%</el-descriptions-item>
          <el-descriptions-item label="实际收益率">
            <span :style="{color: drillRow.actualReturn < drillRow.expectedReturn * 0.7 ? '#FF4D4F' : '#52C41A', fontWeight:600}">
              {{ drillRow.actualReturn !== null ? drillRow.actualReturn + '%' : '-' }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="项目状态">{{ statusTextMap[drillRow.projectStatus] || drillRow.projectStatus }}</el-descriptions-item>
          <el-descriptions-item label="立项日期">{{ drillRow.approvalDate || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">穿透链路</el-divider>
        <div style="padding:0 20px">
          <el-steps :active="2" align-center>
            <el-step title="集团总部" description="控股方" icon="el-icon-office-building" />
            <el-step :title="drillRow.company || '投资企业'" description="投资主体" icon="el-icon-s-shop" />
            <el-step :title="drillRow.projectName ? drillRow.projectName.substring(0,8) : '目标企业'" description="被投资方" icon="el-icon-s-flag" />
          </el-steps>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="drillVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getInvestProjectList, getInvestStatistics, addInvestProject, updateInvestProject, deleteInvestProject, batchDeleteInvestProject } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'

const INVEST_TYPE_MAP = { EQUITY: '股权投资', DEBT: '债权投资', FUND: '基金投资', MIXED: '混合型', OTHER: '其他' }

export default {
  name: 'InvestPenetrationProject',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      statistics: {},
      multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, projectName: '', investType: '', projectStatus: '', isMainBiz: null },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      drillVisible: false,
      drillRow: null,
      companyOptions: [
        { label: '示例能源', value: 'HBNY' },
        { label: '示例科技', value: 'HBKJ' },
        { label: '示例金融', value: 'HBJR' },
        { label: '示例地产', value: 'HBDC' },
        { label: '示例物流', value: 'HBWL' },
      ],
      statusTagMap: { EXECUTING: '', COMPLETED: 'success', EXITED: 'info', PAUSED: 'warning' },
      statusTextMap: { EXECUTING: '执行中', COMPLETED: '已完成', EXITED: '已退出', PAUSED: '暂停' },
      rules: {
        projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        investType: [{ required: true, message: '请选择投资类型', trigger: 'change' }],
        companyId: [{ required: true, message: '请选择投资企业', trigger: 'change' }],
        investAmount: [{ required: true, message: '请输入投资金额', trigger: 'blur' }],
      },
    }
  },
  computed: {
    dialogTitle() {
      return { add: '新增投资项目', edit: '编辑投资项目', view: '查看投资项目' }[this.dialogType] || ''
    },
    summaryKpi() {
      const s = this.statistics
      // 优先使用后端统计数据，降级时从当前页列表计算
      if (s && s.totalCount != null) {
        return [
          { label: '项目总数', value: (s.totalCount || 0) + ' 个', color: this.ipPrimary },
          { label: '投资总额', value: ((s.totalAmount || 0) / 10000).toFixed(1) + '亿', color: this.ipAccent },
          { label: '执行中', value: (s.executingCount || 0) + ' 个', color: this.ipSecondary },
          { label: '非主业项目', value: (s.nonMainBizCount || 0) + ' 个', color: '#FA8C16' },
          { label: '平均收益率', value: (s.avgActualReturn || 0).toFixed(1) + '%', color: '#52C41A' },
          { label: '收益不达标', value: (s.underPerformCount || 0) + ' 个', color: '#FF4D4F' },
        ]
      }
      // 降级：从当前页数据计算（仅供参考）
      const d = this.list
      const len = d.length || 1
      return [
        { label: '项目总数', value: this.total + ' 个', color: this.ipPrimary },
        { label: '投资总额', value: (d.reduce((acc, i) => acc + (i.investAmount || 0), 0) / 10000).toFixed(1) + '亿', color: this.ipAccent },
        { label: '执行中', value: d.filter(i => i.projectStatus === 'EXECUTING').length + ' 个', color: this.ipSecondary },
        { label: '非主业项目', value: d.filter(i => !i.isMainBiz).length + ' 个', color: '#FA8C16' },
        { label: '平均收益率', value: (d.reduce((acc, i) => acc + (i.actualReturn || 0), 0) / len).toFixed(1) + '%', color: '#52C41A' },
        { label: '收益不达标', value: d.filter(i => i.actualReturn != null && i.actualReturn < i.expectedReturn * 0.8).length + ' 个', color: '#FF4D4F' },
      ]
    },
  },
  created() {
    this.fetchData()
    this.fetchStatistics()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = { ...this.queryForm }
        // 后端 isMainBiz 用 'Y'/'N'，null 表示不过滤
        if (params.isMainBiz !== null && params.isMainBiz !== '') {
          params.isMainBiz = params.isMainBiz ? 'Y' : 'N'
        } else {
          delete params.isMainBiz
        }
        const res = await getInvestProjectList(params)
        if (res.result === 200 && res.data) {
          this.list = (res.data.tlist || []).map(item => ({
            ...item,
            isMainBiz: item.isMainBiz === 'Y',
            investTypeLabel: INVEST_TYPE_MAP[item.investType] || item.investType,
            company: item.companyName || item.company,
          }))
          this.total = res.data.totalRecord || 0
        }
      } catch (e) {
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },
    async fetchStatistics() {
      try {
        const res = await getInvestStatistics()
        if (res.result === 200 && res.data) {
          this.statistics = res.data
        }
      } catch (e) { /* 统计接口失败不影响主流程 */ }
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.isMainBiz = null
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    handleSelectionChange(val) { this.multipleSelection = val },
    handleAdd() { this.dialogType = 'add'; this.form = { isMainBiz: true, projectStatus: 'EXECUTING', companyId: '' }; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row, companyId: row.companyId || '' }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row, companyId: row.companyId || '' }; this.dialogVisible = true },
    handleDrill(row) {
      // 跳转到穿透分析页面，传递 projectId 和企业标识
      this.$router.push({
        path: '/modelMonitor/Tzctfx',
        query: { projectId: row.projectId, company: row.companyId || row.company }
      })
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          // 提交前将 isMainBiz boolean 转为后端期望的 'Y'/'N'，映射 companyId → companyName
          const companyNameMap = { HBNY: '示例能源', HBKJ: '示例科技', HBJR: '示例金融', HBDC: '示例地产', HBWL: '示例物流' }
          const payload = {
            ...this.form,
            isMainBiz: this.form.isMainBiz ? 'Y' : 'N',
            companyId: this.form.companyId,
            companyName: companyNameMap[this.form.companyId] || this.form.companyId
          }
          if (this.dialogType === 'add') {
            const res = await addInvestProject(payload)
            if (res.result !== 200) { this.$message.error(res.msg || '新增失败'); return }
          } else {
            const res = await updateInvestProject(payload)
            if (res.result !== 200) { this.$message.error(res.msg || '修改失败'); return }
          }
          this.$message.success(this.dialogType === 'add' ? '新增成功' : '修改成功')
          this.dialogVisible = false
          this.fetchData()
          this.fetchStatistics()
        } catch (e) {
          this.$message.error(this.dialogType === 'add' ? '新增失败' : '修改失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该投资项目？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteInvestProject(row.projectId)
          if (res.result !== 200) { this.$message.error(res.msg || '删除失败'); return }
          this.$message.success('删除成功')
          this.fetchData()
          this.fetchStatistics()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(item => item.projectId)
      this.$confirm(`确认删除选中的 ${ids.length} 条记录？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await batchDeleteInvestProject({ ids })
          if (res.result !== 200) { this.$message.error(res.msg || '批量删除失败'); return }
          this.$message.success('批量删除成功')
          this.multipleSelection = []
          this.fetchData()
          this.fetchStatistics()
        } catch (e) {
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },
    handleExport() {
      if (!this.list || this.list.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      const headers = ['项目编号', '项目名称', '投资类型', '投资企业', '投资金额(万元)', '是否主业', '项目状态', '预期收益率(%)', '实际收益率(%)', '立项日期']
      const statusMap = { EXECUTING: '执行中', COMPLETED: '已完成', EXITED: '已退出', PAUSED: '暂停' }
      const rows = this.list.map(row => [
        row.projectId || '',
        row.projectName || '',
        row.investTypeLabel || '',
        row.company || row.companyName || '',
        row.investAmount || 0,
        row.isMainBiz ? '主业' : '非主业',
        statusMap[row.projectStatus] || row.projectStatus || '',
        row.expectedReturn || '',
        row.actualReturn !== null && row.actualReturn !== undefined ? row.actualReturn : '',
        row.approvalDate || ''
      ])
      // 生成CSV内容（带BOM头兼容Excel中文）
      let csv = '\uFEFF' + headers.join(',') + '\n'
      rows.forEach(r => {
        csv += r.map(cell => '"' + String(cell).replace(/"/g, '""') + '"').join(',') + '\n'
      })
      const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = '投资项目台账_' + new Date().toISOString().slice(0, 10) + '.csv'
      link.click()
      URL.revokeObjectURL(link.href)
      this.$message.success('导出成功')
    },
  },
}
</script>

<style lang="scss" scoped>
.invest-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px; color: #fff;
  &-left { display: flex; align-items: center; font-size: 16px; font-weight: 700; i { font-size: 22px; margin-right: 10px; color: var(--ip-accent, #FAAD14); } }
  &-desc { font-size: 13px; opacity: 0.8; }
}
.kpi-mini {
  background: #fff;
  border-left: 4px solid var(--ip-primary, #1A3A6B);
  border-radius: 6px;
  padding: 10px 14px;
  box-shadow: 0 1px 4px rgba(0,0,0,.06);
  .kpi-v { font-size: 18px; font-weight: 700; }
  .kpi-l { font-size: 12px; color: #888; margin-top: 2px; }
}
.search-card { margin-bottom: 0; border-left: 3px solid var(--ip-primary, #1A3A6B); }
.search-card .el-form-item { margin-bottom: 0; }
.amount-text { font-weight: 600; color: var(--ip-primary, #1A3A6B); }
::v-deep .el-table th { background: #f5f7ff; color: var(--ip-primary, #1A3A6B); }
::v-deep .el-card { border-radius: 8px; }
</style>
