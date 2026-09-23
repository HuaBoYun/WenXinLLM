<template>
  <div class="app-container military-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-lock"></i><span>军品保密管理</span></div>
      <div class="page-header-desc">军工企业保密资质管理、检查记录与违规事件追踪</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable /></el-form-item>
        <el-form-item label="保密等级">
          <el-select v-model="queryForm.secretLevel" placeholder="请选择" clearable>
            <el-option label="绝密" value="TOP_SECRET" />
            <el-option label="机密" value="SECRET" />
            <el-option label="秘密" value="CONFIDENTIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查结果">
          <el-select v-model="queryForm.checkResult" placeholder="请选择" clearable>
            <el-option label="通过" value="PASS" />
            <el-option label="不通过" value="FAIL" />
            <el-option label="警告" value="WARNING" />
          </el-select>
        </el-form-item>
        <el-form-item label="整改状态">
          <el-select v-model="queryForm.rectificationStatus" placeholder="请选择" clearable>
            <el-option label="待整改" value="PENDING" />
            <el-option label="整改中" value="PROCESSING" />
            <el-option label="已完成" value="DONE" />
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
      </div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column prop="companyName" label="企业名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="secretLevel" label="保密等级" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.secretLevel === 'TOP_SECRET' ? 'danger' : row.secretLevel === 'SECRET' ? 'warning' : 'info'" size="small">{{ secretLevelMap[row.secretLevel] || row.secretLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkDate" label="检查日期" width="120" align="center" />
        <el-table-column prop="checkResult" label="检查结果" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.checkResult === 'PASS' ? 'success' : row.checkResult === 'FAIL' ? 'danger' : 'warning'" size="small">{{ checkResultMap[row.checkResult] || row.checkResult }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="violationCount" label="违规次数" width="90" align="center" />
        <el-table-column prop="rectificationStatus" label="整改状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.rectificationStatus === 'DONE' ? 'success' : row.rectificationStatus === 'PROCESSING' ? 'warning' : 'info'" size="small">{{ rectStatusMap[row.rectificationStatus] || row.rectificationStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkerName" label="检查人" width="100" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:12px;text-align:right" background layout="total, sizes, prev, pager, next, jumper" :total="total" :page-sizes="[10,20,50]" :page-size.sync="queryForm.pageSize" :current-page.sync="queryForm.pageNum" @size-change="getList" @current-change="getList" />
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="dialogType === 'view'">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" placeholder="请输入" /></el-form-item>
        <el-form-item label="保密等级" prop="secretLevel">
          <el-select v-model="form.secretLevel" placeholder="请选择" style="width:100%">
            <el-option label="绝密" value="TOP_SECRET" />
            <el-option label="机密" value="SECRET" />
            <el-option label="秘密" value="CONFIDENTIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查日期" prop="checkDate"><el-date-picker v-model="form.checkDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
        <el-form-item label="检查结果" prop="checkResult">
          <el-select v-model="form.checkResult" placeholder="请选择" style="width:100%">
            <el-option label="通过" value="PASS" />
            <el-option label="不通过" value="FAIL" />
            <el-option label="警告" value="WARNING" />
          </el-select>
        </el-form-item>
        <el-form-item label="违规次数" prop="violationCount"><el-input-number v-model="form.violationCount" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="违规描述" prop="violationDesc"><el-input v-model="form.violationDesc" type="textarea" :rows="3" placeholder="请输入" /></el-form-item>
        <el-form-item label="整改状态" prop="rectificationStatus">
          <el-select v-model="form.rectificationStatus" placeholder="请选择" style="width:100%">
            <el-option label="待整改" value="PENDING" />
            <el-option label="整改中" value="PROCESSING" />
            <el-option label="已完成" value="DONE" />
          </el-select>
        </el-form-item>
        <el-form-item label="整改期限" prop="rectificationDeadline"><el-date-picker v-model="form.rectificationDeadline" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
        <el-form-item label="检查人" prop="checkerName"><el-input v-model="form.checkerName" placeholder="请输入" /></el-form-item>
      </el-form>
      <span slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getSecurityList, addSecurity, updateSecurity, deleteSecurity } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'MilitarySecurityMgmt',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNum: 1, pageSize: 10, companyName: '', secretLevel: '', checkResult: '', rectificationStatus: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        secretLevel: [{ required: true, message: '请选择保密等级', trigger: 'change' }],
        checkDate: [{ required: true, message: '请选择检查日期', trigger: 'change' }],
        checkResult: [{ required: true, message: '请选择检查结果', trigger: 'change' }],
      },
      secretLevelMap: { TOP_SECRET: '绝密', SECRET: '机密', CONFIDENTIAL: '秘密' },
      checkResultMap: { PASS: '通过', FAIL: '不通过', WARNING: '警告' },
      rectStatusMap: { PENDING: '待整改', PROCESSING: '整改中', DONE: '已完成' },
    }
  },
  computed: {
    dialogTitle() {
      return { add: '新增保密记录', edit: '编辑保密记录', view: '查看保密记录' }[this.dialogType]
    },
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getSecurityList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNum = 1; this.getList() },
    resetQuery() {
      this.queryForm = { pageNum: 1, pageSize: 10, companyName: '', secretLevel: '', checkResult: '', rectificationStatus: '' }
      this.getList()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = { companyName: '', secretLevel: '', checkDate: '', checkResult: '', violationCount: 0, violationDesc: '', rectificationStatus: '', rectificationDeadline: '', checkerName: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.formRef && this.$refs.formRef.clearValidate() })
    },
    handleView(row) {
      this.dialogType = 'view'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该保密记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteSecurity(row.securityId)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.getList() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addSecurity : updateSecurity
          const res = await apiFn(this.form)
          if (res && res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.dialogVisible = false
            this.getList()
          } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.military-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { margin-bottom: 0; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>
