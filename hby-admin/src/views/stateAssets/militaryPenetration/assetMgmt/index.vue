<template>
  <div class="app-container military-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-box"></i><span>军品资产管理</span></div>
      <div class="page-header-desc">管理军品专用资产台账、使用状态与折旧分析</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable /></el-form-item>
        <el-form-item label="资产类型">
          <el-select v-model="queryForm.assetType" placeholder="请选择" clearable>
            <el-option label="设备" value="EQUIPMENT" />
            <el-option label="设施" value="FACILITY" />
            <el-option label="仪器" value="INSTRUMENT" />
            <el-option label="车辆" value="VEHICLE" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产状态">
          <el-select v-model="queryForm.assetStatus" placeholder="请选择" clearable>
            <el-option label="在用" value="IN_USE" />
            <el-option label="闲置" value="IDLE" />
            <el-option label="报废" value="SCRAPPED" />
            <el-option label="维修中" value="MAINTENANCE" />
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
        <el-table-column prop="assetCode" label="资产编码" width="130" align="center" />
        <el-table-column prop="assetName" label="资产名称" min-width="140" show-overflow-tooltip />
        <el-table-column prop="companyName" label="企业名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="assetType" label="资产类型" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.assetType === 'EQUIPMENT' ? '' : row.assetType === 'FACILITY' ? 'success' : row.assetType === 'INSTRUMENT' ? 'warning' : 'info'" size="small">{{ assetTypeMap[row.assetType] || row.assetType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="originalValue" label="原值(万元)" width="110" align="right" />
        <el-table-column prop="netValue" label="净值(万元)" width="110" align="right" />
        <el-table-column prop="depreciationRate" label="折旧率(%)" width="100" align="center" />
        <el-table-column prop="purchaseDate" label="购置日期" width="120" align="center" />
        <el-table-column prop="assetStatus" label="资产状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.assetStatus === 'IN_USE' ? 'success' : row.assetStatus === 'IDLE' ? 'warning' : row.assetStatus === 'SCRAPPED' ? 'info' : 'danger'" size="small">{{ assetStatusMap[row.assetStatus] || row.assetStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="secretLevel" label="密级" width="80" align="center">
          <template slot-scope="{ row }">
            <span>{{ secretLevelMap[row.secretLevel] || row.secretLevel }}</span>
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
        <el-form-item label="资产名称" prop="assetName"><el-input v-model="form.assetName" placeholder="请输入" /></el-form-item>
        <el-form-item label="资产编码" prop="assetCode"><el-input v-model="form.assetCode" placeholder="请输入" /></el-form-item>
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" placeholder="请输入" /></el-form-item>
        <el-form-item label="资产类型" prop="assetType">
          <el-select v-model="form.assetType" placeholder="请选择" style="width:100%">
            <el-option label="设备" value="EQUIPMENT" />
            <el-option label="设施" value="FACILITY" />
            <el-option label="仪器" value="INSTRUMENT" />
            <el-option label="车辆" value="VEHICLE" />
          </el-select>
        </el-form-item>
        <el-form-item label="原值(万元)" prop="originalValue"><el-input-number v-model="form.originalValue" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="净值(万元)" prop="netValue"><el-input-number v-model="form.netValue" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="折旧率(%)" prop="depreciationRate"><el-input-number v-model="form.depreciationRate" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="购置日期" prop="purchaseDate"><el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
        <el-form-item label="资产状态" prop="assetStatus">
          <el-select v-model="form.assetStatus" placeholder="请选择" style="width:100%">
            <el-option label="在用" value="IN_USE" />
            <el-option label="闲置" value="IDLE" />
            <el-option label="报废" value="SCRAPPED" />
            <el-option label="维修中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置" prop="location"><el-input v-model="form.location" placeholder="请输入" /></el-form-item>
        <el-form-item label="密级" prop="secretLevel">
          <el-select v-model="form.secretLevel" placeholder="请选择" style="width:100%">
            <el-option label="绝密" value="TOP_SECRET" />
            <el-option label="机密" value="SECRET" />
            <el-option label="秘密" value="CONFIDENTIAL" />
            <el-option label="非密" value="NONE" />
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
import { getAssetList, addAsset, updateAsset, deleteAsset } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'MilitaryAssetMgmt',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNum: 1, pageSize: 10, companyName: '', assetType: '', assetStatus: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
        assetCode: [{ required: true, message: '请输入资产编码', trigger: 'blur' }],
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        assetType: [{ required: true, message: '请选择资产类型', trigger: 'change' }],
        assetStatus: [{ required: true, message: '请选择资产状态', trigger: 'change' }],
      },
      assetTypeMap: { EQUIPMENT: '设备', FACILITY: '设施', INSTRUMENT: '仪器', VEHICLE: '车辆' },
      assetStatusMap: { IN_USE: '在用', IDLE: '闲置', SCRAPPED: '报废', MAINTENANCE: '维修中' },
      secretLevelMap: { TOP_SECRET: '绝密', SECRET: '机密', CONFIDENTIAL: '秘密', NONE: '非密' },
    }
  },
  computed: {
    dialogTitle() {
      return { add: '新增资产', edit: '编辑资产', view: '查看资产' }[this.dialogType]
    },
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getAssetList(this.queryForm)
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
      this.queryForm = { pageNum: 1, pageSize: 10, companyName: '', assetType: '', assetStatus: '' }
      this.getList()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = { assetName: '', assetCode: '', companyName: '', assetType: '', originalValue: 0, netValue: 0, depreciationRate: 0, purchaseDate: '', assetStatus: '', location: '', secretLevel: '' }
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
      this.$confirm('确认删除该资产记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteAsset(row.assetId)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.getList() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addAsset : updateAsset
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
