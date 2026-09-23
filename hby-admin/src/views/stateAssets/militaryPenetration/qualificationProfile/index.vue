<template>
  <div class="military-page" :style="themeVars">
    <div class="page-header"><h2><i class="el-icon-document"></i> 资质档案管理</h2><p>军工企业保密资质与生产许可全档案管理</p></div>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item label="资质类型"><el-select v-model="queryForm.qualType" placeholder="请选择" clearable style="width:160px">
          <el-option label="武器装备科研生产许可" value="武器装备科研生产许可" /><el-option label="装备承制单位资格" value="装备承制单位资格" /><el-option label="保密资格" value="保密资格" /><el-option label="质量体系认证" value="质量体系认证" />
        </el-select></el-form-item>
        <el-form-item label="状态"><el-select v-model="queryForm.qualStatus" placeholder="请选择" clearable style="width:120px">
          <el-option label="有效" value="VALID" /><el-option label="待续期" value="PENDING_RENEW" /><el-option label="已过期" value="EXPIRED" /><el-option label="已吊销" value="REVOKED" />
        </el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px"><el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
      <el-table v-loading="loading" :data="list" border style="width:100%" size="small">
        <template slot="empty"><div style="padding:40px 0;text-align:center;color:#909399"><i class="el-icon-document" style="font-size:40px;display:block;margin-bottom:10px"></i><span>暂无资质档案数据</span></div></template>
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="资质名称" prop="qualName" min-width="160" show-overflow-tooltip />
        <el-table-column label="资质类型" prop="qualType" width="160" />
        <el-table-column label="资质编号" prop="qualNo" width="120" />
        <el-table-column label="发证机关" prop="issueAuthority" min-width="120" show-overflow-tooltip />
        <el-table-column label="发证日期" prop="issueDate" width="110" align="center" />
        <el-table-column label="到期日期" prop="expireDate" width="110" align="center" />
        <el-table-column label="密级" prop="secretLevel" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="{TOP_SECRET:'danger',SECRET:'warning',CONFIDENTIAL:'info',PUBLIC:'success'}[row.secretLevel]" size="mini">{{ {TOP_SECRET:'绝密',SECRET:'机密',CONFIDENTIAL:'秘密',PUBLIC:'公开'}[row.secretLevel] || row.secretLevel }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" prop="qualStatus" width="90" align="center">
          <template slot-scope="{row}"><el-tag :type="{VALID:'success',PENDING_RENEW:'warning',EXPIRED:'danger',REVOKED:'info'}[row.qualStatus]" size="mini">{{ {VALID:'有效',PENDING_RENEW:'待续期',EXPIRED:'已过期',REVOKED:'已吊销'}[row.qualStatus] || row.qualStatus }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize" layout="total,sizes,prev,pager,next,jumper" :total="total" @size-change="val=>{queryForm.pageSize=val;fetchData()}" @current-change="val=>{queryForm.pageNumber=val;fetchData()}" />
    </el-card>
    <el-dialog :title="{add:'新增资质',edit:'编辑资质',view:'查看资质'}[dialogType]" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="form" ref="form" label-width="100px" :rules="rules">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="资质名称" prop="qualName"><el-input v-model="form.qualName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="资质类型"><el-select v-model="form.qualType" :disabled="dialogType==='view'" style="width:100%"><el-option label="武器装备科研生产许可" value="武器装备科研生产许可" /><el-option label="装备承制单位资格" value="装备承制单位资格" /><el-option label="保密资格" value="保密资格" /><el-option label="质量体系认证" value="质量体系认证" /></el-select></el-form-item>
        <el-form-item label="资质编号"><el-input v-model="form.qualNo" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="发证机关"><el-input v-model="form.issueAuthority" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="发证日期"><el-date-picker v-model="form.issueDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="到期日期"><el-date-picker v-model="form.expireDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="密级"><el-select v-model="form.secretLevel" :disabled="dialogType==='view'" style="width:100%"><el-option label="绝密" value="TOP_SECRET" /><el-option label="机密" value="SECRET" /><el-option label="秘密" value="CONFIDENTIAL" /><el-option label="公开" value="PUBLIC" /></el-select></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.qualStatus" :disabled="dialogType==='view'" style="width:100%"><el-option label="有效" value="VALID" /><el-option label="待续期" value="PENDING_RENEW" /><el-option label="已过期" value="EXPIRED" /></el-select></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType!=='view'">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getQualificationList, addQualification, updateQualification, deleteQualification } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'QualificationProfile',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      queryForm: { companyName: '', qualType: '', qualStatus: '', pageNumber: 1, pageSize: 10 },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }], qualName: [{ required: true, message: '请输入资质名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getQualificationList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.queryForm = { companyName: '', qualType: '', qualStatus: '', pageNumber: 1, pageSize: 10 }; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addQualification : updateQualification)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该资质？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteQualification(row.qualId)
        if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.military-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:16px; padding:18px 24px; background:linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius:6px; color:#fff;
  h2 { font-size:20px; margin:0 0 4px; i{margin-right:8px;} } p { font-size:13px; opacity:0.85; margin:0; } }
.search-card { margin-bottom:0; ::v-deep .el-form-item { margin-bottom:0; } }
::v-deep .el-table th { background:var(--ip-light-bg, #EBF1FF) !important; color:var(--ip-secondary, #0050A0); font-weight:600; }
::v-deep .el-card { border-radius:6px; }
</style>

