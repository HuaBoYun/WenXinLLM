<template>
  <div class="app-container property-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-document-copy"></i><span>产权登记管理</span></div>
      <div class="page-header-desc">管理国有产权登记信息与持股结构</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form ref="queryForm" :model="queryForm" inline>
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="产权类型" prop="rightType">
          <el-select v-model="queryForm.rightType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="国有独资" value="SOLE" />
            <el-option label="国有控股" value="HOLDING" />
            <el-option label="国有参股" value="PARTICIPATING" />
          </el-select>
        </el-form-item>
        <el-form-item label="登记状态" prop="registrationStatus">
          <el-select v-model="queryForm.registrationStatus" placeholder="请选择" clearable style="width: 150px">
            <el-option label="已登记" value="REGISTERED" />
            <el-option label="待登记" value="PENDING" />
            <el-option label="变更中" value="CHANGING" />
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
      <el-table v-loading="loading" :data="list" border @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="产权类型" prop="rightType" width="100" align="center">
          <template slot-scope="scope">
            {{ { SOLE: '国有独资', HOLDING: '国有控股', PARTICIPATING: '国有参股' }[scope.row.rightType] || scope.row.rightType || '—' }}
          </template>
        </el-table-column>
        <el-table-column label="持股比例(%)" prop="holdingRatio" width="120" align="right">
          <template slot-scope="scope">
            {{ scope.row.holdingRatio != null ? scope.row.holdingRatio : '—' }}
          </template>
        </el-table-column>
        <el-table-column label="注册资本(万元)" prop="registeredCapital" width="130" align="right" />
        <el-table-column label="登记状态" prop="registrationStatus" width="100" align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.registrationStatus">
              <el-tag :type="scope.row.registrationStatus === 'REGISTERED' ? 'success' : 'warning'" size="small">
                {{ { REGISTERED: '已登记', PENDING: '待登记', CHANGING: '变更中' }[scope.row.registrationStatus] || scope.row.registrationStatus }}
              </el-tag>
            </template>
            <span v-else>—</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入企业名称" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="产权类型" prop="rightType">
          <el-select v-model="form.rightType" placeholder="请选择" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option label="国有独资" value="SOLE" />
            <el-option label="国有控股" value="HOLDING" />
            <el-option label="国有参股" value="PARTICIPATING" />
          </el-select>
        </el-form-item>
        <el-form-item label="持股比例(%)" prop="holdingRatio">
          <el-input-number v-model="form.holdingRatio" :min="0" :max="100" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="注册资本(万元)" prop="registeredCapital">
          <el-input-number v-model="form.registeredCapital" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
        </el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPropertyRightList, addPropertyRight, updatePropertyRight, deletePropertyRight, batchDeletePropertyRight } from '@/api/stateAssets/propertyPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'PropertyPenetrationRight',
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', rightType: '', registrationStatus: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        rightType: [{ required: true, message: '请选择产权类型', trigger: 'change' }],
      },
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
    dialogTitle() { return { add: '新增产权登记', edit: '编辑产权登记', view: '查看产权登记' }[this.dialogType] || '' },
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getPropertyRightList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, companyName: '', rightType: '', registrationStatus: '' }
      this.fetchData()
    },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.fetchData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.fetchData() },
    handleSelectionChange(val) { this.multipleSelection = val },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addPropertyRight : updatePropertyRight)(this.form)
          if (res && res.result === 200) { this.$message.success(this.dialogType === 'add' ? '新增成功' : '修改成功'); this.dialogVisible = false; this.fetchData() }
          else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该产权登记？', '提示', { type: 'warning' }).then(async () => {
        const res = await deletePropertyRight(row.propertyId)
        if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
        else { this.$message.error(res.msg || '删除失败') }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.propertyId)
      this.$confirm(`确认删除选中的 ${ids.length} 条记录？`, '提示', { type: 'warning' }).then(async () => {
        const res = await batchDeletePropertyRight(ids)
        if (res && res.result === 200) { this.$message.success('批量删除成功'); this.fetchData() }
        else { this.$message.error(res.msg || '批量删除失败') }
      }).catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.property-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { margin-bottom: 0; border-left: 3px solid #52c41a; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: #f0fff0; }
::v-deep .el-card { border-radius: 6px; }
</style>