<template>
  <div class="sal-total">
    <div class="sal-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">工资总额管理</div>
      <div class="banner-sub">工资总额台账管理 · 预算执行监控 · 超标预警 · 效益联动追踪</div>
    </div>

    <!-- 查询条件 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业">
          <el-select v-model="queryForm.companyName" clearable placeholder="全部企业" style="width:140px">
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告年度">
          <el-date-picker v-model="queryForm.reportYear" type="year" value-format="yyyy" clearable placeholder="全部年度" style="width:120px"></el-date-picker>
        </el-form-item>
        <el-form-item label="执行状态">
          <el-select v-model="queryForm.status" clearable placeholder="全部状态" style="width:120px">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="超预算" value="OVER_BUDGET"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- KPI汇总卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="k in kpiCards" :key="k.key">
        <div class="kpi-card">
          <div class="kpi-label">{{ k.label }}</div>
          <div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 工资总额台账列表 -->
    <el-card shadow="never" class="table-card">
      <div slot="header">
        <span class="card-title">工资总额台账</span>
        <div style="float:right">
          <el-button size="small" type="primary" icon="el-icon-plus" @click="openEdit(null)">新增记录</el-button>
          <el-button size="small" icon="el-icon-download" @click="handleExport">导出</el-button>
        </div>
      </div>
      <el-table :data="tableData" border stripe size="small" :row-class-name="rowClass">
        <el-table-column prop="companyName" label="企业名称" width="140"></el-table-column>
        <el-table-column prop="reportYear" label="年度" width="70" align="center"></el-table-column>
        <el-table-column prop="budgetTotal" label="核准总额（万元）" align="right">
          <template slot-scope="{ row }">{{ row.budgetTotal ? row.budgetTotal.toLocaleString() : '—' }}</template>
        </el-table-column>
        <el-table-column prop="actualTotal" label="实际总额（万元）" align="right">
          <template slot-scope="{ row }">
            <span :style="{ color: row.actualTotal > row.budgetTotal ? '#F5222D' : 'inherit', fontWeight: row.actualTotal > row.budgetTotal ? 700 : 400 }">
              {{ row.actualTotal.toLocaleString() }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="execRate" label="预算执行率" align="center">
          <template slot-scope="{ row }">
            <span :style="execRateStyle(row.execRate)">{{ row.execRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="employeeCount" label="在岗人数" align="center"></el-table-column>
        <el-table-column prop="avgSalary" label="人均薪酬（万元）" align="center"></el-table-column>
        <el-table-column prop="wageGrowthRate" label="工资增长率" align="center">
          <template slot-scope="{ row }">{{ row.wageGrowthRate }}%</template>
        </el-table-column>
        <el-table-column prop="linkCoeff" label="效益联动系数" align="center">
          <template slot-scope="{ row }">
            <span :style="linkCoeffStyle(row.linkCoeff)">{{ row.linkCoeff }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="row.status === 'OVER_BUDGET' ? 'danger' : row.status === 'REVIEWING' ? 'warning' : 'success'" size="mini">
              {{ { NORMAL: '正常', OVER_BUDGET: '超预算', REVIEWING: '待审核' }[row.status] }}
            </el-tag>
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
    <el-dialog :title="editForm.salaryId ? '编辑工资总额记录' : '新增工资总额记录'" :visible.sync="editVisible" width="580px">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="130px" size="small">
        <el-form-item label="企业" prop="companyName">
          <el-select v-model="editForm.companyName" placeholder="请选择企业" style="width:100%" filterable allow-create>
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告年度" prop="reportYear">
          <el-date-picker v-model="editForm.reportYear" type="year" value-format="yyyy" placeholder="选择年度" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="核准总额（万元）" prop="budgetTotal">
          <el-input-number v-model="editForm.budgetTotal" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="实际总额（万元）" prop="actualTotal">
          <el-input-number v-model="editForm.actualTotal" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="在岗员工人数" prop="employeeCount">
          <el-input-number v-model="editForm.employeeCount" :min="0" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="工资增长率（%）">
          <el-input-number v-model="editForm.wageGrowthRate" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="效益增长率（%）">
          <el-input-number v-model="editForm.profitGrowthRate" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </span>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="工资总额详情" :visible.sync="detailVisible" size="480px" direction="rtl">
      <div v-if="detailRow" style="padding: 20px">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="企业名称" :span="2">{{ detailRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="报告年度">{{ detailRow.reportYear }}</el-descriptions-item>
          <el-descriptions-item label="在岗人数">{{ detailRow.employeeCount }}人</el-descriptions-item>
          <el-descriptions-item label="核准总额">{{ detailRow.budgetTotal ? detailRow.budgetTotal.toLocaleString() : '—' }}万元</el-descriptions-item>
          <el-descriptions-item label="实际总额">
            <span :style="{ color: detailRow.actualTotal > detailRow.budgetTotal ? '#F5222D' : '#52C41A', fontWeight: 700 }">
              {{ detailRow.actualTotal.toLocaleString() }}万元
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="预算执行率">
            <span :style="{ color: detailRow.execRate > 100 ? '#F5222D' : '#52C41A', fontWeight: 700 }">{{ detailRow.execRate }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="人均薪酬">{{ detailRow.avgSalary || '—' }}万元</el-descriptions-item>
          <el-descriptions-item label="工资增长率">{{ detailRow.wageGrowthRate || '—' }}%</el-descriptions-item>
          <el-descriptions-item label="效益联动系数">
            <span :style="{ color: detailRow.linkCoeff < 0.8 ? '#FA8C16' : '#52C41A' }">{{ detailRow.linkCoeff || '—' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="执行状态">
            <el-tag :type="detailRow.status === 'OVER_BUDGET' ? 'danger' : 'success'" size="mini">
              {{ { NORMAL: '正常', OVER_BUDGET: '超预算', REVIEWING: '待审核' }[detailRow.status] || '正常' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getSalaryTotalList, saveSalaryTotal, exportSalaryTotal, deleteSalaryTotal } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'SalaryTotalList',
  data() {
    return {
      queryForm: { companyName: '', reportYear: '', status: '' },
      tableData: [],
      editVisible: false,
      editForm: {},
      editRules: {
        companyName: [{ required: true, message: '请选择企业', trigger: 'change' }],
        reportYear: [{ required: true, message: '请选择年度', trigger: 'change' }],
        budgetTotal: [{ required: true, message: '请填写核准总额', trigger: 'blur' }],
      },
      companyOptions: [],
      detailVisible: false,
      detailRow: null,
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
      const total = this.tableData.reduce((s, r) => s + (r.actualTotal || 0), 0)
      const approved = this.tableData.reduce((s, r) => s + (r.budgetTotal || 0), 0)
      const overCount = this.tableData.filter(r => r.execRate > 100).length
      const execRate = approved ? ((total / approved) * 100).toFixed(1) : 0
      return [
        { key: 'total', label: '集团工资总额（万元）', value: total.toLocaleString(), color: '#1677FF' },
        { key: 'approved', label: '核准总额（万元）', value: approved.toLocaleString(), color: '#0050A0' },
        { key: 'execRate', label: '集团预算执行率', value: execRate + '%', color: execRate > 100 ? '#F5222D' : '#52C41A' },
        { key: 'over', label: '超预算企业数', value: overCount + '家', color: overCount > 0 ? '#F5222D' : '#52C41A' },
      ]
    },
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await getSalaryTotalList({ ...this.queryForm, pageSize: 20 })
        if (res.data && res.data.tlist) {
          this.tableData = res.data.tlist.map(r => ({
            ...r,
            budgetTotal: Number(r.budgetTotal) || 0,
            actualTotal: Number(r.actualTotal) || 0,
            avgSalary: Number(r.avgSalary) || 0,
            wageGrowthRate: Number(r.wageGrowthRate) || 0,
            profitGrowthRate: Number(r.profitGrowthRate) || 0,
            employeeCount: r.employeeCount || 0,
            execRate: r.budgetTotal && Number(r.budgetTotal) > 0 ? ((Number(r.actualTotal) / Number(r.budgetTotal)) * 100).toFixed(1) : 0,
            linkCoeff: r.laborCostRate || 0,
            status: Number(r.actualTotal) > Number(r.budgetTotal) && Number(r.budgetTotal) > 0 ? 'OVER_BUDGET' : 'NORMAL',
          }))
          const companies = [...new Set(this.tableData.map(r => r.companyName).filter(Boolean))]
          if (companies.length) this.companyOptions = companies
        }
      } catch (e) {
        console.warn('加载工资总额数据失败', e)
      }
    },
    handleQuery() { this.loadData() },
    handleReset() {
      this.queryForm = { companyName: '', reportYear: '', status: '' }
      this.loadData()
    },
    rowClass({ row }) {
      if (row.execRate > 100) return 'row-over-budget'
      return ''
    },
    execRateStyle(v) {
      if (v > 100) return { color: '#F5222D', fontWeight: '700' }
      if (v > 95) return { color: '#FA8C16' }
      if (v < 80) return { color: '#FA8C16' }
      return { color: '#52C41A' }
    },
    linkCoeffStyle(v) {
      if (v < 0.6) return { color: '#F5222D', fontWeight: '700' }
      if (v < 0.8) return { color: '#FA8C16', fontWeight: '700' }
      if (v > 1.5) return { color: '#FA8C16', fontWeight: '700' }
      return { color: '#52C41A' }
    },
    openEdit(row) {
      if (row) {
        this.editForm = {
          salaryId: row.salaryId,
          companyName: row.companyName,
          reportYear: row.reportYear,
          budgetTotal: row.budgetTotal,
          actualTotal: row.actualTotal,
          employeeCount: row.employeeCount,
          wageGrowthRate: row.wageGrowthRate,
          profitGrowthRate: row.profitGrowthRate,
        }
      } else {
        this.editForm = { companyName: '', reportYear: '', budgetTotal: 0, actualTotal: 0, employeeCount: 0, wageGrowthRate: 0, profitGrowthRate: 0 }
      }
      this.editVisible = true
    },
    async submitEdit() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        try {
          await saveSalaryTotal(this.editForm)
          this.$message.success('保存成功')
          this.editVisible = false
          this.loadData()
        } catch (e) {
          this.$message.error('保存失败')
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          await deleteSalaryTotal(row.salaryId)
          this.$message.success('删除成功')
          this.loadData()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    viewDetail(row) {
      this.detailRow = row
      this.detailVisible = true
    },
    async handleExport() {
      try {
        const res = await exportSalaryTotal(this.queryForm)
        if (res.data && res.data.length) {
          const headers = ['企业名称', '年度', '核准总额(万元)', '实际总额(万元)', '预算执行率(%)', '在岗人数', '人均薪酬(万元)', '工资增长率(%)']
          const rows = res.data.map(r => [
            r.companyName, r.reportYear, r.budgetTotal, r.actualTotal,
            r.budgetTotal > 0 ? ((r.actualTotal / r.budgetTotal) * 100).toFixed(1) : 0,
            r.employeeCount, r.avgSalary, r.wageGrowthRate
          ])
          const csv = [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
          const blob = new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8;' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = `工资总额台账_${new Date().toISOString().slice(0, 10)}.csv`
          link.click()
          this.$message.success('导出成功')
        } else {
          this.$message.warning('暂无数据可导出')
        }
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
  },
}
</script>

<style scoped>
.sal-total { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; }
.query-card { margin-bottom: 12px; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 16px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 6px; }
.kpi-value { font-size: 22px; font-weight: 700; }
.table-card { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
::v-deep .row-over-budget td { background: #fff1f0 !important; }
</style>
