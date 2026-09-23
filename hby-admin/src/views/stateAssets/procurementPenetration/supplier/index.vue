<template>
  <div class="app-container procurement-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-user"></i><span>供应商管理</span></div>
      <div class="page-header-desc">管理供应商信息、信用评级与合作状态</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="queryForm.supplierName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="信用评级" prop="creditRating">
          <el-select v-model="queryForm.creditRating" placeholder="请选择" clearable style="width: 120px">
            <el-option label="AAA" value="AAA" /><el-option label="AA" value="AA" /><el-option label="A" value="A" /><el-option label="BBB" value="BBB" /><el-option label="BB" value="BB" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否黑名单" prop="isBlacklisted">
          <el-select v-model="queryForm.isBlacklisted" placeholder="请选择" clearable style="width: 120px">
            <el-option label="是" value="Y" /><el-option label="否" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px"><el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="供应商编码" prop="supplierCode" width="120" />
        <el-table-column label="供应商名称" prop="supplierName" min-width="160" show-overflow-tooltip />
        <el-table-column label="统一信用代码" prop="unifiedCreditCode" width="180" />
        <el-table-column label="行业" prop="industry" width="120" show-overflow-tooltip />
        <el-table-column label="信用评级" prop="creditRating" width="90" align="center" />
        <el-table-column label="累计交易额(万元)" prop="totalTradeAmount" width="140" align="right" />
        <el-table-column label="交易次数" prop="tradeCount" width="90" align="right" />
        <el-table-column label="黑名单" prop="isBlacklisted" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isBlacklisted === 'Y' ? 'danger' : 'success'" size="small">{{ scope.row.isBlacklisted === 'Y' ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="dialogType = 'view'; form = { ...scope.row }; dialogVisible = true">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <el-dialog :title="{ add: '新增供应商', edit: '编辑供应商', view: '查看供应商' }[dialogType]" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="130px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="供应商名称" prop="supplierName"><el-input v-model="form.supplierName" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="供应商编码"><el-input v-model="form.supplierCode" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="统一信用代码"><el-input v-model="form.unifiedCreditCode" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="信用评级">
            <el-select v-model="form.creditRating" :disabled="dialogType === 'view'" style="width: 100%">
              <el-option label="AAA" value="AAA" /><el-option label="AA" value="AA" /><el-option label="A" value="A" /><el-option label="BBB" value="BBB" /><el-option label="BB" value="BB" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="行业"><el-input v-model="form.industry" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="地区"><el-input v-model="form.region" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="资质信息"><el-input v-model="form.qualification" type="textarea" :rows="3" :disabled="dialogType === 'view'" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="是否黑名单">
            <el-radio-group v-model="form.isBlacklisted" :disabled="dialogType === 'view'">
              <el-radio label="Y">是</el-radio><el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item></el-col>
        </el-row>
        <el-form-item label="黑名单原因" v-if="form.isBlacklisted === 'Y'"><el-input v-model="form.blacklistReason" type="textarea" :rows="2" :disabled="dialogType === 'view'" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getSupplierList, addSupplier, updateSupplier, deleteSupplier } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'ProcurementPenetrationSupplier',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, supplierName: '', creditRating: '', isBlacklisted: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getSupplierList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, supplierName: '', creditRating: '', isBlacklisted: '' }
      this.fetchData()
    },
    handleDelete(row) {
      this.$confirm('确认删除该供应商？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteSupplier(row.id)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    handleAdd() { this.dialogType = 'add'; this.form = { isBlacklisted: 'N' }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addSupplier : updateSupplier)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
  },
}
</script>
<style scoped>.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }</style>
<style lang="scss" scoped>
.procurement-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>