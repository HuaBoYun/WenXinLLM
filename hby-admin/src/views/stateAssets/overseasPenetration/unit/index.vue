<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-office-building"></i><span>境外单位管理</span></div>
      <div class="page-header-desc">管理境外子公司、分支机构基本信息与运营状态</div>
    </div>

    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="单位名称" prop="unitName">
          <el-input v-model="queryForm.unitName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="所在国家" prop="country">
          <el-input v-model="queryForm.country" placeholder="请输入" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="经营状态" prop="operationStatus">
          <el-select v-model="queryForm.operationStatus" placeholder="全部" clearable style="width: 120px">
            <el-option label="正常" value="NORMAL" />
            <el-option label="预警" value="WARNING" />
            <el-option label="异常" value="ABNORMAL" />
            <el-option label="已关闭" value="CLOSED" />
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
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="单位名称" prop="unitName" min-width="160" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="100" align="center" />
        <el-table-column label="所在城市" prop="city" width="100" align="center" />
        <el-table-column label="注册资本(万元)" prop="registeredCapital" width="130" align="right" />
        <el-table-column label="总资产(万元)" prop="totalAssets" width="130" align="right" />
        <el-table-column label="员工人数" prop="employeeCount" width="90" align="right" />
        <el-table-column label="业务范围" prop="businessScope" min-width="140" show-overflow-tooltip />
        <el-table-column label="经营状态" prop="operationStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.operationStatus)" size="small">{{ statusLabel(scope.row.operationStatus) }}</el-tag>
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
      <el-pagination background style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="val => { queryForm.pageSize = val; fetchData() }"
        @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>

    <el-dialog :title="{ add: '新增境外单位', edit: '编辑境外单位', view: '查看境外单位' }[dialogType]"
      :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="单位名称" prop="unitName"><el-input v-model="form.unitName" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在国家" prop="country"><el-input v-model="form.country" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在城市"><el-input v-model="form.city" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册资本(万)"><el-input-number v-model="form.registeredCapital" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总资产(万元)"><el-input-number v-model="form.totalAssets" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工人数"><el-input-number v-model="form.employeeCount" :min="0" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="业务范围"><el-input v-model="form.businessScope" :disabled="dialogType === 'view'" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经营状态">
              <el-select v-model="form.operationStatus" style="width: 100%" :disabled="dialogType === 'view'">
                <el-option label="正常" value="NORMAL" /><el-option label="预警" value="WARNING" /><el-option label="异常" value="ABNORMAL" /><el-option label="已关闭" value="CLOSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOverseasUnitList, addOverseasUnit, updateOverseasUnit, deleteOverseasUnit, batchDeleteOverseasUnit } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'OverseasPenetrationUnit',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, unitName: '', country: '', operationStatus: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: {
        unitName: [{ required: true, message: '请输入单位名称', trigger: 'blur' }],
        country: [{ required: true, message: '请输入所在国家', trigger: 'blur' }],
        registeredCapital: [{ required: true, message: '请输入注册资本', trigger: 'blur' }]
      }
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasUnitList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = { operationStatus: 'NORMAL' }; this.dialogVisible = true; this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate()) },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addOverseasUnit : updateOverseasUnit)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() }
          else { this.$message.error((res && res.msg) || '操作失败') }
        } catch (e) { this.$message.error('操作失败，请稍后重试') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除？', '提示', { type: 'warning' }).then(async () => {
        try { const res = await deleteOverseasUnit(row.unitId); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() } else { this.$message.error((res && res.msg) || '删除失败') } } catch (e) { this.$message.error('删除失败，请稍后重试') }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.unitId)
      this.$confirm(`确认删除 ${ids.length} 条？`, '提示', { type: 'warning' }).then(async () => {
        try { const res = await batchDeleteOverseasUnit(ids); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() } else { this.$message.error((res && res.msg) || '删除失败') } } catch (e) { this.$message.error('删除失败，请稍后重试') }
      }).catch(() => {})
    },
    statusTagType(s) { return { NORMAL: 'success', WARNING: 'warning', ABNORMAL: 'danger', CLOSED: 'info' }[s] || 'info' },
    statusLabel(s) { return { NORMAL: '正常', WARNING: '预警', ABNORMAL: '异常', CLOSED: '已关闭' }[s] || s }
  },
}
</script>

<style lang="scss" scoped>
.overseas-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-card { border-radius: 6px; }
.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }
</style>
