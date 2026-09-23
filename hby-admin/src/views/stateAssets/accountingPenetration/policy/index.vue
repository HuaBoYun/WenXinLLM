<template>
  <div class="app-container accounting-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-notebook-2"></i><span>会计政策管理</span></div>
      <div class="page-header-desc">管理企业会计政策一致性与变更审批</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="政策名称" prop="policyName">
          <el-input v-model="queryForm.policyName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="政策类型" prop="policyType">
          <el-select v-model="queryForm.policyType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="收入确认" value="REVENUE" /><el-option label="资产计量" value="ASSET" /><el-option label="折旧摊销" value="DEPRECIATION" /><el-option label="其他" value="OTHER" />
          </el-select>
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
        <el-table-column label="政策名称" prop="policyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="政策类型" prop="policyType" width="100" align="center" />
        <el-table-column label="适用企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="生效日期" prop="effectiveDate" width="120" align="center" />
        <el-table-column label="状态" prop="status" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'" size="small">{{ { ACTIVE: '生效', INACTIVE: '失效' }[scope.row.status] || scope.row.status }}</el-tag>
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
    <el-dialog :title="{ add: '新增会计政策', edit: '编辑会计政策', view: '查看会计政策' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="政策名称" prop="policyName"><el-input v-model="form.policyName" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="政策类型" prop="policyType">
          <el-select v-model="form.policyType" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option label="收入确认" value="REVENUE" /><el-option label="资产计量" value="ASSET" /><el-option label="折旧摊销" value="DEPRECIATION" /><el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="适用企业"><el-input v-model="form.companyName" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="政策内容"><el-input v-model="form.policyContent" type="textarea" :rows="4" :disabled="dialogType === 'view'" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getAccountingPolicyList, addAccountingPolicy, updateAccountingPolicy, deleteAccountingPolicy, batchDeleteAccountingPolicy } from '@/api/stateAssets/accountingPenetration'
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
  name: 'AccountingPenetrationPolicy',
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, policyName: '', policyType: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { policyName: [{ required: true, message: '请输入政策名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getAccountingPolicyList(this.queryForm)
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
          const res = await (this.dialogType === 'add' ? addAccountingPolicy : updateAccountingPolicy)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteAccountingPolicy(row.policyId); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.policyId)
      this.$confirm(`确认删除 ${ids.length} 条？`, '提示', { type: 'warning' }).then(async () => {
        const res = await batchDeleteAccountingPolicy(ids); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.accounting-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { margin-bottom: 0; border-left: 3px solid #13c2c2; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: #e6fffb; }
::v-deep .el-card { border-radius: 6px; }
</style>