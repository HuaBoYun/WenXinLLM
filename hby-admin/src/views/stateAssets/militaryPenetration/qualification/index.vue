<template>
  <div class="app-container military-page" :style="themeVars">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-trophy"></i><span>资质管理</span></div>
      <div class="page-header-desc">管理军工企业资质证书、有效期监控与年审提醒</div>
    </div>

    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card" style="margin-bottom: 14px;">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width: 180px;" />
        </el-form-item>
        <el-form-item label="资质类型">
          <el-select v-model="queryForm.qualType" placeholder="请选择" clearable style="width: 180px;">
            <el-option label="武器装备科研生产许可" value="武器装备科研生产许可" />
            <el-option label="装备承制单位资格" value="装备承制单位资格" />
            <el-option label="保密资格" value="保密资格" />
            <el-option label="质量体系认证" value="质量体系认证" />
          </el-select>
        </el-form-item>
        <el-form-item label="资质状态">
          <el-select v-model="queryForm.qualStatus" placeholder="请选择" clearable style="width: 140px;">
            <el-option label="有效" value="VALID" />
            <el-option label="待续期" value="PENDING_RENEW" />
            <el-option label="已过期" value="EXPIRED" />
            <el-option label="已吊销" value="REVOKED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card shadow="never">
      <div slot="header" style="display: flex; align-items: center; justify-content: space-between;">
        <span style="font-weight: 600;">资质列表</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="资质名称" prop="qualName" min-width="140" show-overflow-tooltip />
        <el-table-column label="资质类型" prop="qualType" min-width="160" show-overflow-tooltip />
        <el-table-column label="资质编号" prop="qualNo" min-width="140" show-overflow-tooltip />
        <el-table-column label="发证机关" prop="issueAuthority" min-width="140" show-overflow-tooltip />
        <el-table-column label="发证日期" prop="issueDate" width="110" align="center" />
        <el-table-column label="到期日期" prop="expireDate" width="110" align="center" />
        <el-table-column label="密级" prop="secretLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="secretLevelTag(scope.row.secretLevel)" size="small">{{ secretLevelLabel(scope.row.secretLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="资质状态" prop="qualStatus" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTag(scope.row.qualStatus)" size="small">{{ statusLabel(scope.row.qualStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="即将到期" prop="isExpiring" width="90" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isExpiring" type="warning" size="small">是</el-tag>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C;" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top: 15px; text-align: right;"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑/查看弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="620px" :close-on-click-modal="false">
      <el-form ref="dialogForm" :model="dialogForm" :rules="dialogRules" label-width="100px" :disabled="dialogType === 'view'">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="dialogForm.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="资质名称" prop="qualName">
          <el-input v-model="dialogForm.qualName" placeholder="请输入资质名称" />
        </el-form-item>
        <el-form-item label="资质类型" prop="qualType">
          <el-select v-model="dialogForm.qualType" placeholder="请选择资质类型" style="width: 100%;">
            <el-option label="武器装备科研生产许可" value="武器装备科研生产许可" />
            <el-option label="装备承制单位资格" value="装备承制单位资格" />
            <el-option label="保密资格" value="保密资格" />
            <el-option label="质量体系认证" value="质量体系认证" />
          </el-select>
        </el-form-item>
        <el-form-item label="资质编号" prop="qualNo">
          <el-input v-model="dialogForm.qualNo" placeholder="请输入资质编号" />
        </el-form-item>
        <el-form-item label="发证机关" prop="issueAuthority">
          <el-input v-model="dialogForm.issueAuthority" placeholder="请输入发证机关" />
        </el-form-item>
        <el-form-item label="发证日期" prop="issueDate">
          <el-date-picker v-model="dialogForm.issueDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择发证日期" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="到期日期" prop="expireDate">
          <el-date-picker v-model="dialogForm.expireDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择到期日期" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="密级" prop="secretLevel">
          <el-select v-model="dialogForm.secretLevel" placeholder="请选择密级" style="width: 100%;">
            <el-option label="公开" value="PUBLIC" />
            <el-option label="秘密" value="CONFIDENTIAL" />
            <el-option label="机密" value="SECRET" />
            <el-option label="绝密" value="TOP_SECRET" />
          </el-select>
        </el-form-item>
        <el-form-item label="资质状态" prop="qualStatus">
          <el-select v-model="dialogForm.qualStatus" placeholder="请选择资质状态" style="width: 100%;">
            <el-option label="有效" value="VALID" />
            <el-option label="待续期" value="PENDING_RENEW" />
            <el-option label="已过期" value="EXPIRED" />
            <el-option label="已吊销" value="REVOKED" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getQualificationList, addQualification, updateQualification, deleteQualification, getQualificationStatistics } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'MilitaryQualification',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        companyName: '',
        qualType: '',
        qualStatus: '',
      },
      dialogVisible: false,
      dialogType: 'add', // add / edit / view
      dialogForm: {
        qualId: '',
        companyName: '',
        qualName: '',
        qualType: '',
        qualNo: '',
        issueAuthority: '',
        issueDate: '',
        expireDate: '',
        secretLevel: '',
        qualStatus: '',
      },
      dialogRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        qualName: [{ required: true, message: '请输入资质名称', trigger: 'blur' }],
        qualType: [{ required: true, message: '请选择资质类型', trigger: 'change' }],
        qualNo: [{ required: true, message: '请输入资质编号', trigger: 'blur' }],
        issueAuthority: [{ required: true, message: '请输入发证机关', trigger: 'blur' }],
        issueDate: [{ required: true, message: '请选择发证日期', trigger: 'change' }],
        expireDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }],
        secretLevel: [{ required: true, message: '请选择密级', trigger: 'change' }],
        qualStatus: [{ required: true, message: '请选择资质状态', trigger: 'change' }],
      },
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增资质', edit: '编辑资质', view: '查看资质' }
      return map[this.dialogType] || '资质详情'
    },
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getQualificationList(this.queryForm)
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
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleReset() {
      this.queryForm = { pageNumber: 1, pageSize: 10, companyName: '', qualType: '', qualStatus: '' }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.dialogForm = { qualId: '', companyName: '', qualName: '', qualType: '', qualNo: '', issueAuthority: '', issueDate: '', expireDate: '', secretLevel: '', qualStatus: '' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.dialogForm && this.$refs.dialogForm.clearValidate() })
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.dialogForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.dialogForm && this.$refs.dialogForm.clearValidate() })
    },
    handleView(row) {
      this.dialogType = 'view'
      this.dialogForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该资质记录？删除后不可恢复。', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteQualification(row.qualId)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.dialogForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addQualification : updateQualification
          const res = await apiFn(this.dialogForm)
          if (res && res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    statusTag(status) {
      const map = { VALID: 'success', PENDING_RENEW: 'warning', EXPIRED: 'danger', REVOKED: 'info' }
      return map[status] || 'info'
    },
    statusLabel(status) {
      const map = { VALID: '有效', PENDING_RENEW: '待续期', EXPIRED: '已过期', REVOKED: '已吊销' }
      return map[status] || status
    },
    secretLevelTag(level) {
      const map = { PUBLIC: '', CONFIDENTIAL: 'warning', SECRET: 'danger', TOP_SECRET: 'danger' }
      return map[level] || 'info'
    },
    secretLevelLabel(level) {
      const map = { PUBLIC: '公开', CONFIDENTIAL: '秘密', SECRET: '机密', TOP_SECRET: '绝密' }
      return map[level] || level
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
