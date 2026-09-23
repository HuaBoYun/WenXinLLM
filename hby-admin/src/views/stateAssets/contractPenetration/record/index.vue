<template>
  <div class="app-container contract-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + ' 0%, ' + themeColor + 'cc 100%)' }">
      <div class="page-header-left"><i class="el-icon-document"></i><span>合同台账</span></div>
      <div class="page-header-desc">管理合同签订、类型、金额与法律审核信息</div>
    </div>
    <el-card class="search-card" shadow="never" :style="{ borderLeft: '3px solid ' + themeColor }">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="queryForm.contractName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="合同类型" prop="contractType">
          <el-select v-model="queryForm.contractType" placeholder="请选择" clearable style="width: 130px">
            <el-option label="采购合同" value="PURCHASE" /><el-option label="销售合同" value="SALES" /><el-option label="工程合同" value="ENGINEERING" /><el-option label="服务合同" value="SERVICE" /><el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="合同状态" prop="contractStatus">
          <el-select v-model="queryForm.contractStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" /><el-option label="执行中" value="EXECUTING" /><el-option label="已完成" value="COMPLETED" /><el-option label="已终止" value="TERMINATED" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据来源" prop="dataSource">
          <el-select v-model="queryForm.dataSource" placeholder="请选择" clearable style="width: 120px">
            <el-option label="内部" value="INTERNAL" /><el-option label="外部" value="EXTERNAL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px" :style="{ '--table-header-bg': themeColorLight }">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border @selection-change="val => multipleSelection = val" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="合同编号" prop="contractCode" width="130" />
        <el-table-column label="合同名称" prop="contractName" min-width="160" show-overflow-tooltip />
        <el-table-column label="合同类型" prop="contractType" width="100" align="center" />
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right" />
        <el-table-column label="对方单位" prop="counterpartyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="签订日期" prop="signDate" width="110" align="center" />
        <el-table-column label="合同状态" prop="contractStatus" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ DRAFT: 'info', EXECUTING: 'warning', COMPLETED: 'success', TERMINATED: 'danger' }[scope.row.contractStatus]" size="small">
              {{ { DRAFT: '草稿', EXECUTING: '执行中', COMPLETED: '已完成', TERMINATED: '已终止' }[scope.row.contractStatus] || scope.row.contractStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="数据来源" prop="dataSource" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.dataSource === 'INTERNAL' ? '' : 'warning'" size="small">{{ scope.row.dataSource === 'INTERNAL' ? '内部' : '外部' }}</el-tag>
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
    <el-dialog :title="{ add: '新增合同记录', edit: '编辑合同记录', view: '查看合同记录' }[dialogType]" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合同名称" prop="contractName"><el-input v-model="form.contractName" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合同编号"><el-input v-model="form.contractCode" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合同类型">
            <el-select v-model="form.contractType" :disabled="dialogType === 'view'" style="width: 100%">
              <el-option label="采购合同" value="PURCHASE" /><el-option label="销售合同" value="SALES" /><el-option label="工程合同" value="ENGINEERING" /><el-option label="服务合同" value="SERVICE" /><el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合同金额(万元)"><el-input-number v-model="form.contractAmount" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="对方单位"><el-input v-model="form.counterpartyName" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="签订日期"><el-date-picker v-model="form.signDate" type="date" value-format="yyyy-MM-dd" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="是否重大合同">
            <el-radio-group v-model="form.isMajor" :disabled="dialogType === 'view'"><el-radio label="1">是</el-radio><el-radio label="0">否</el-radio></el-radio-group>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="法律审核">
            <el-radio-group v-model="form.hasLegalReview" :disabled="dialogType === 'view'"><el-radio label="1">已审核</el-radio><el-radio label="0">未审核</el-radio></el-radio-group>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合同状态">
            <el-select v-model="form.contractStatus" :disabled="dialogType === 'view'" style="width: 100%">
              <el-option label="草稿" value="DRAFT" /><el-option label="执行中" value="EXECUTING" /><el-option label="已完成" value="COMPLETED" /><el-option label="已终止" value="TERMINATED" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="数据来源">
            <el-select v-model="form.dataSource" :disabled="dialogType === 'view'" style="width: 100%">
              <el-option label="内部" value="INTERNAL" /><el-option label="外部" value="EXTERNAL" />
            </el-select>
          </el-form-item></el-col>
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
import { mapGetters } from 'vuex'
import { getContractRecordList, addContractRecord, updateContractRecord, deleteContractRecord, batchDeleteContractRecord } from '@/api/stateAssets/contractPenetration'
export default {
  name: 'ContractPenetrationRecord',
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
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, contractName: '', contractType: '', contractStatus: '', dataSource: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getContractRecordList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = { isMajor: '0', hasLegalReview: '0', dataSource: 'INTERNAL', contractStatus: 'DRAFT' }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addContractRecord : updateContractRecord)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteContractRecord(row.contractId); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.contractId)
      this.$confirm(`确认删除 ${ids.length} 条？`, '提示', { type: 'warning' }).then(async () => {
        const res = await batchDeleteContractRecord(ids); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>
<style lang="scss" scoped>
.contract-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { margin-bottom: 0; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: var(--table-header-bg, #e6f7ff); }
::v-deep .el-card { border-radius: 6px; }
</style>
