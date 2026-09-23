<template>
  <div class="app-container procurement-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-warning-outline"></i><span>供应链风险分析</span></div>
      <div class="page-header-desc">评估供应商集中度、替代方案覆盖率与中断风险</div>
    </div>
    <el-card shadow="never">
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="8"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">供应商集中度</div><div class="stat-value">{{ stats.concentration || '-' }}</div></div></el-card></el-col>
        <el-col :span="8"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">高风险供应商</div><div class="stat-value danger">{{ stats.highRiskCount || 0 }}</div></div></el-card></el-col>
        <el-col :span="8"><el-card shadow="hover"><div class="stat-item"><div class="stat-label">替代方案覆盖率</div><div class="stat-value">{{ stats.alternativeCoverage || '0%' }}</div></div></el-card></el-col>
      </el-row>
      <!-- 搜索区 -->
      <el-form :model="queryForm" :inline="true" size="small" style="margin-bottom: 10px">
        <el-form-item label="供应商名称">
          <el-input v-model="queryForm.supplierName" placeholder="请输入" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width: 100px">
            <el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <div style="margin-bottom: 10px"><el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="供应商" prop="supplierName" min-width="160" show-overflow-tooltip />
        <el-table-column label="采购占比(%)" prop="purchaseRatio" width="100" align="center" />
        <el-table-column label="风险等级" prop="riskLevel" width="80" align="center"><template slot-scope="scope"><el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[scope.row.riskLevel]" size="small">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[scope.row.riskLevel] || '-' }}</el-tag></template></el-table-column>
        <el-table-column label="替代方案" prop="hasAlternative" width="80" align="center"><template slot-scope="scope"><el-tag :type="scope.row.hasAlternative === 'Y' ? 'success' : 'danger'" size="small">{{ scope.row.hasAlternative === 'Y' ? '有' : '无' }}</el-tag></template></el-table-column>
        <el-table-column label="中断风险评估" prop="disruptionRisk" min-width="140" show-overflow-tooltip />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogType === 'add' ? '新增风险记录' : '编辑风险记录'" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="供应商名称" prop="supplierName"><el-input v-model="form.supplierName" /></el-form-item>
        <el-form-item label="采购占比(%)" prop="purchaseRatio"><el-input-number v-model="form.purchaseRatio" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="form.riskLevel" style="width:100%">
            <el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="替代方案">
          <el-radio-group v-model="form.hasAlternative">
            <el-radio label="Y">有</el-radio><el-radio label="N">无</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="中断风险评估"><el-input v-model="form.disruptionRisk" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getSupplyChainRiskList, addSupplyChainRisk, updateSupplyChainRisk, deleteSupplyChainRisk, getSupplyChainRiskStatistics } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'ProcurementSupplyChainRisk',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      stats: {},
      queryForm: { pageNumber: 1, pageSize: 10, supplierName: '', riskLevel: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      submitLoading: false,
      rules: {
        supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
      },
    }
  },
  created() { this.fetchData(); this.fetchStatistics() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getSupplyChainRiskList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
          this.computeStatsFromList()
        } else { this.list = []; this.total = 0 }
      } catch (e) {
        this.$message.error('获取风险列表失败')
        this.list = []; this.total = 0
      } finally { this.loading = false }
    },
    async fetchStatistics() {
      try {
        const res = await getSupplyChainRiskStatistics()
        if (res && res.result === 200 && res.data) {
          this.stats = res.data
        } else {
          this.computeStatsFromList()
        }
      } catch (e) { this.computeStatsFromList() }
    },
    computeStatsFromList() {
      if (!this.list.length) return
      const highRisk = this.list.filter(r => r.riskLevel === 'HIGH').length
      const withAlt = this.list.filter(r => r.hasAlternative === 'Y').length
      const topRatio = this.list.reduce((max, r) => Math.max(max, r.purchaseRatio || 0), 0)
      this.stats = {
        concentration: topRatio + '%',
        highRiskCount: highRisk,
        alternativeCoverage: this.list.length ? Math.round((withAlt / this.list.length) * 100) + '%' : '0%'
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, supplierName: '', riskLevel: '' }
      this.fetchData()
    },
    handleAdd() { this.dialogType = 'add'; this.form = { hasAlternative: 'N', riskLevel: '', purchaseRatio: 0 }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addSupplyChainRisk : updateSupplyChainRisk
          const res = await apiFn(this.form)
          if (res && res.result === 200) {
            this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData(); this.fetchStatistics()
          } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该风险记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteSupplyChainRisk(row.id)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData(); this.fetchStatistics() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
  },
}
</script>
<style scoped>.stat-item { text-align: center; padding: 10px; } .stat-label { color: #909399; font-size: 14px; } .stat-value { font-size: 28px; font-weight: bold; color: #303133; margin-top: 8px; } .stat-value.danger { color: #F56C6C; }</style>
<style lang="scss" scoped>
.procurement-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>