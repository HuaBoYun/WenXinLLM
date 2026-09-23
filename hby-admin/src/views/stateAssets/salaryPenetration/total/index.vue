<template>
  <div class="app-container">
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="年度" prop="reportYear">
          <el-date-picker v-model="queryForm.reportYear" type="year" value-format="yyyy" placeholder="选择年度" style="width: 120px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border @selection-change="val => multipleSelection = val" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="年度" prop="reportYear" width="80" align="center" />
        <el-table-column label="工资总额(万元)" prop="actualTotal" width="130" align="right" />
        <el-table-column label="人均薪酬(万元)" prop="avgSalary" width="130" align="right" />
        <el-table-column label="员工人数" prop="employeeCount" width="100" align="right" />
        <el-table-column label="预算金额(万元)" prop="budgetTotal" width="130" align="right" />
        <el-table-column label="预算执行率(%)" prop="laborCostRate" width="130" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.budgetTotal && scope.row.budgetTotal > 0 ? ((scope.row.actualTotal / scope.row.budgetTotal) * 100).toFixed(1) : '—' }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="dialogType = 'view'; form = { ...scope.row }; dialogVisible = true">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <el-dialog :title="{ add: '新增薪酬总额', edit: '编辑薪酬总额', view: '查看薪酬总额' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="年度" prop="reportYear"><el-date-picker v-model="form.reportYear" type="year" value-format="yyyy" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="工资总额(万元)"><el-input-number v-model="form.actualTotal" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="预算金额(万元)"><el-input-number v-model="form.budgetTotal" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="员工人数"><el-input-number v-model="form.employeeCount" :min="0" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getSalaryTotalList, addSalaryTotal, updateSalaryTotal, deleteSalaryTotal, batchDeleteSalaryTotal } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'
export default {
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
  },
  name: 'SalaryPenetrationTotal',
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', reportYear: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getSalaryTotalList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addSalaryTotal : updateSalaryTotal)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteSalaryTotal(row.salaryId); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.salaryId)
      this.$confirm(`确认删除 ${ids.length} 条？`, '提示', { type: 'warning' }).then(async () => {
        const res = await batchDeleteSalaryTotal(ids); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>
<style scoped>.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }</style>

