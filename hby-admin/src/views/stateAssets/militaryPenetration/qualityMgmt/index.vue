<template>
  <div class="app-container military-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-circle-check"></i><span>军品质量管理</span></div>
      <div class="page-header-desc">管理军品质量检测、质量问题追踪与质量体系审核</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable /></el-form-item>
        <el-form-item label="检验结果">
          <el-select v-model="queryForm.inspectionResult" placeholder="请选择" clearable>
            <el-option label="合格" value="QUALIFIED" />
            <el-option label="不合格" value="UNQUALIFIED" />
            <el-option label="待检测" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item label="产品名称"><el-input v-model="queryForm.productName" placeholder="请输入产品名称" clearable /></el-form-item>
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
        <el-table-column prop="companyName" label="企业名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="inspectionItem" label="检验项目" min-width="130" show-overflow-tooltip />
        <el-table-column prop="productName" label="产品名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="batchNo" label="批次号" width="120" align="center" />
        <el-table-column prop="inspectionDate" label="检验日期" width="120" align="center" />
        <el-table-column prop="inspectionResult" label="检验结果" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.inspectionResult === 'QUALIFIED' ? 'success' : row.inspectionResult === 'UNQUALIFIED' ? 'danger' : 'warning'" size="small">{{ inspectionResultMap[row.inspectionResult] || row.inspectionResult }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="issueCount" label="问题数量" width="90" align="center" />
        <el-table-column prop="inspectorName" label="检验员" width="100" align="center" />
        <el-table-column prop="rectificationStatus" label="整改状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.rectificationStatus === 'DONE' ? 'success' : row.rectificationStatus === 'PROCESSING' ? 'warning' : 'info'" size="small">{{ rectStatusMap[row.rectificationStatus] || row.rectificationStatus }}</el-tag>
          </template>
        </el-table-column>
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
        <el-form-item label="检验项目" prop="inspectionItem"><el-input v-model="form.inspectionItem" placeholder="请输入" /></el-form-item>
        <el-form-item label="产品名称" prop="productName"><el-input v-model="form.productName" placeholder="请输入" /></el-form-item>
        <el-form-item label="批次号" prop="batchNo"><el-input v-model="form.batchNo" placeholder="请输入" /></el-form-item>
        <el-form-item label="检验日期" prop="inspectionDate"><el-date-picker v-model="form.inspectionDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
        <el-form-item label="检验结果" prop="inspectionResult">
          <el-select v-model="form.inspectionResult" placeholder="请选择" style="width:100%">
            <el-option label="合格" value="QUALIFIED" />
            <el-option label="不合格" value="UNQUALIFIED" />
            <el-option label="待检测" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题数量" prop="issueCount"><el-input-number v-model="form.issueCount" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="问题描述" prop="issueDesc"><el-input v-model="form.issueDesc" type="textarea" :rows="3" placeholder="请输入" /></el-form-item>
        <el-form-item label="检验员" prop="inspectorName"><el-input v-model="form.inspectorName" placeholder="请输入" /></el-form-item>
        <el-form-item label="整改状态" prop="rectificationStatus">
          <el-select v-model="form.rectificationStatus" placeholder="请选择" style="width:100%">
            <el-option label="待整改" value="PENDING" />
            <el-option label="整改中" value="PROCESSING" />
            <el-option label="已完成" value="DONE" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getQualityList, addQuality, updateQuality, deleteQuality } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'MilitaryQualityMgmt',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNum: 1, pageSize: 10, companyName: '', inspectionResult: '', productName: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        inspectionItem: [{ required: true, message: '请输入检验项目', trigger: 'blur' }],
        productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
        inspectionDate: [{ required: true, message: '请选择检验日期', trigger: 'change' }],
        inspectionResult: [{ required: true, message: '请选择检验结果', trigger: 'change' }],
      },
      inspectionResultMap: { QUALIFIED: '合格', UNQUALIFIED: '不合格', PENDING: '待检测' },
      rectStatusMap: { PENDING: '待整改', PROCESSING: '整改中', DONE: '已完成' },
    }
  },
  computed: {
    dialogTitle() {
      return { add: '新增质量记录', edit: '编辑质量记录', view: '查看质量记录' }[this.dialogType]
    },
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getQualityList(this.queryForm)
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
      this.queryForm = { pageNum: 1, pageSize: 10, companyName: '', inspectionResult: '', productName: '' }
      this.getList()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = { companyName: '', inspectionItem: '', productName: '', batchNo: '', inspectionDate: '', inspectionResult: '', issueCount: 0, issueDesc: '', inspectorName: '', rectificationStatus: '' }
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
      this.$confirm('确认删除该质量记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteQuality(row.qualityId)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.getList() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addQuality : updateQuality
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
