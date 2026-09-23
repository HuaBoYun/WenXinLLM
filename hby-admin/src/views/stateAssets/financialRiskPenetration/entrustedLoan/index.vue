<template>
  <div class="fin-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document-checked"></i><span>委托贷款监控</span></div>
      <div class="page-header-desc">监控委托贷款借款方、逾期状态与还款跟踪</div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: s.color + '18', color: s.color }"><i :class="s.icon"></i></div>
            <div class="stat-info">
              <div class="stat-value">{{ s.value }}<span v-if="s.unit" class="stat-unit">{{ s.unit }}</span></div>
              <div class="stat-label">{{ s.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 逾期追踪警告 -->
    <el-alert v-if="overdueList.length > 0"
      :title="'当前存在 ' + overdueList.length + ' 笔逾期委托贷款，请及时处置！'"
      type="error" :closable="false" show-icon class="mb-16">
    </el-alert>

    <!-- 查询 -->
    <el-card shadow="never" class="mb-16">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="借款方">
          <el-input v-model="queryForm.borrowerName" placeholder="请输入借款方" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="贷款状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width:120px">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="逾期" value="OVERDUE" />
            <el-option label="已还清" value="REPAID" />
            <el-option label="违约" value="DEFAULT" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width:110px">
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>委托贷款台账</span>
        <span style="font-size:13px;color:#909399;margin-left:10px">（共 {{ total }} 条）</span>
      </div>
      <el-table v-loading="loading" :data="list" border stripe style="width:100%" :row-class-name="getRowClass">
        <el-table-column label="委托方" prop="lenderName" min-width="140" show-overflow-tooltip />
        <el-table-column label="借款方" prop="borrowerName" min-width="150" show-overflow-tooltip />
        <el-table-column label="贷款金额(万元)" prop="loanAmount" width="130" align="right">
          <template slot-scope="scope">
            <span :style="{fontWeight:'600',color:ipSecondary}">{{ formatAmount(scope.row.loanAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="利率(%)" prop="interestRate" width="80" align="center" />
        <el-table-column label="起始日期" prop="startDate" width="100" align="center" />
        <el-table-column label="到期日期" prop="endDate" width="100" align="center" />
        <el-table-column label="贷款用途" prop="loanPurpose" width="100" align="center" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.status)" size="mini">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="riskTagType(scope.row.riskLevel)" size="mini">
              {{ riskLabel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="关联方" prop="isRelatedParty" width="70" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isRelatedParty" type="warning" size="mini">是</el-tag>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#CF1322" @click="handleDelete(scope.row)">删除</el-button>
            <el-button v-if="scope.row.status === 'OVERDUE'" size="mini" type="text" style="color:#FA8C16" @click="handleUrge(scope.row)">催收</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:16px;text-align:right">
        <el-pagination background layout="total, sizes, prev, pager, next, jumper"
          :total="total" :page-size.sync="queryForm.pageSize" :current-page.sync="queryForm.pageNumber"
          :page-sizes="[10, 15, 20, 50]" @size-change="getList" @current-change="getList" />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="680px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="委托方" prop="lenderName">
              <el-input v-model="form.lenderName" placeholder="请输入委托方" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="借款方" prop="borrowerName">
              <el-input v-model="form.borrowerName" placeholder="请输入借款方" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="贷款金额" prop="loanAmount">
              <el-input-number v-model="form.loanAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="利率(%)" prop="interestRate">
              <el-input-number v-model="form.interestRate" :min="0" :max="100" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="起始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%" placeholder="选择日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%" placeholder="选择日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="贷款用途" prop="loanPurpose">
              <el-input v-model="form.loanPurpose" placeholder="请输入贷款用途" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="还款方式" prop="repaymentMethod">
              <el-select v-model="form.repaymentMethod" placeholder="请选择" style="width:100%">
                <el-option label="到期一次还本付息" value="LUMP_SUM" />
                <el-option label="按月付息到期还本" value="MONTHLY_INTEREST" />
                <el-option label="等额本息" value="EQUAL_INSTALLMENT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="担保措施">
              <el-input v-model="form.guaranteeMeasures" placeholder="请输入担保措施" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关联方交易">
              <el-switch v-model="form.isRelatedParty" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getEntrustedLoanList, addEntrustedLoan, updateEntrustedLoan, deleteEntrustedLoan, exportEntrustedLoan, urgeEntrustedLoan } from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'FinancialRiskEntrustedLoan',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, total: 0,
      queryForm: { borrowerName: '', status: '', riskLevel: '', pageNumber: 1, pageSize: 15 },
      statCards: [
        { label: '贷款总额', value: '0', unit: '万元', icon: 'el-icon-document-checked', color: '#0050A0' },
        { label: '正常贷款', value: '0', unit: '万元', icon: 'el-icon-check', color: '#52C41A' },
        { label: '逾期贷款', value: '0', unit: '万元', icon: 'el-icon-warning', color: '#CF1322' },
        { label: '逾期笔数', value: '0', unit: '笔', icon: 'el-icon-bell', color: '#FA8C16' }
      ],
      list: [],
      dialogVisible: false,
      dialogType: 'add',
      form: { lenderName: '', borrowerName: '', loanAmount: null, interestRate: null, startDate: '', endDate: '', loanPurpose: '', repaymentMethod: '', guaranteeMeasures: '', isRelatedParty: false, remark: '' },
      formRules: {
        lenderName: [{ required: true, message: '请输入委托方', trigger: 'blur' }],
        borrowerName: [{ required: true, message: '请输入借款方', trigger: 'blur' }],
        loanAmount: [{ required: true, message: '请输入贷款金额', trigger: 'blur' }]
      }
    }
  },
  computed: {
    overdueList() { return this.list.filter(r => r.status === 'OVERDUE') },
    dialogTitle() { return this.dialogType === 'add' ? '新增委托贷款' : '编辑委托贷款' }
  },
  created() {
    this.statCards[0].color = this.ipSecondary
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getEntrustedLoanList(this.queryForm).then(res => {
        if (res.data) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          this.computeStats()
        }
      }).catch(() => { this.$message.error('查询委托贷款失败') }).finally(() => { this.loading = false })
    },
    computeStats() {
      let totalAmt = 0, normalAmt = 0, overdueAmt = 0, overdueCount = 0
      this.list.forEach(r => {
        const amt = Number(r.loanAmount) || 0
        totalAmt += amt
        if (r.status === 'OVERDUE') { overdueAmt += amt; overdueCount++ }
        else if (r.status === 'ACTIVE') normalAmt += amt
      })
      this.statCards[0].value = totalAmt.toLocaleString()
      this.statCards[1].value = normalAmt.toLocaleString()
      this.statCards[2].value = overdueAmt.toLocaleString()
      this.statCards[3].value = String(overdueCount)
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.getList() },
    resetQuery() { this.queryForm = { borrowerName: '', status: '', riskLevel: '', pageNumber: 1, pageSize: 15 }; this.getList() },
    handleAdd() { this.dialogType = 'add'; this.form = { lenderName: '', borrowerName: '', loanAmount: null, interestRate: null, startDate: '', endDate: '', loanPurpose: '', repaymentMethod: '', guaranteeMeasures: '', isRelatedParty: false, remark: '' }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    handleDelete(row) {
      this.$confirm('确认删除该委托贷款记录？', '提示', { type: 'warning' }).then(() => {
        deleteEntrustedLoan(row.id).then(res => {
          if (res.result === 200) { this.$message.success('删除成功'); this.getList() }
          else this.$message.error(res.msg || '删除失败')
        }).catch(() => { this.$message.error('删除失败') })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        const api = this.dialogType === 'add' ? addEntrustedLoan : updateEntrustedLoan
        api(this.form).then(res => {
          if (res.result === 200) { this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功'); this.dialogVisible = false; this.getList() }
          else this.$message.error(res.msg || '操作失败')
        }).catch(() => { this.$message.error('操作失败') }).finally(() => { this.submitLoading = false })
      })
    },
    handleExport() {
      this.$message.info('正在导出数据...')
      exportEntrustedLoan(this.queryForm).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '委托贷款台账.xls'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => { this.$message.error('导出失败') })
    },
    handleUrge(row) {
      this.$confirm(`确认对「${row.borrowerName}」发送催收通知？`, '催收确认', { type: 'warning' }).then(() => {
        const loading = this.$loading({ lock: true, text: '正在发送催收通知...', background: 'rgba(0, 0, 0, 0.7)' })
        urgeEntrustedLoan({ id: row.id }).then(res => {
          loading.close()
          if (res.result === 200) {
            this.$message.success('催收通知已发送至：' + row.borrowerName)
            this.getList()
          } else {
            this.$message.error(res.msg || '催收失败')
          }
        }).catch(() => {
          loading.close()
          this.$message.error('催收请求失败')
        })
      }).catch(() => {})
    },
    formatAmount(val) { return val ? Number(val).toLocaleString() : '-' },
    statusTagType(s) { return { ACTIVE: 'success', OVERDUE: 'danger', REPAID: 'info', DEFAULT: 'danger' }[s] || 'info' },
    statusLabel(s) { return { ACTIVE: '正常', OVERDUE: '逾期', REPAID: '已还清', DEFAULT: '违约' }[s] || s },
    riskTagType(r) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[r] || 'info' },
    riskLabel(r) { return { HIGH: '高', MEDIUM: '中', LOW: '低' }[r] || r },
    getRowClass({ row }) { return row.status === 'OVERDUE' ? 'overdue-row' : '' }
  }
}
</script>

<style lang="scss" scoped>
.fin-page { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; }
.stat-icon { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 20px; font-weight: 700; color: #303133; line-height: 1; .stat-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; } }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-table .overdue-row { background: #FFF1F0 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
