<template>
  <div class="app-container finance-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-connection"></i><span>关联交易监控</span></div>
      <div class="page-header-desc">监控企业间关联交易行为与风险</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="交易名称" prop="transactionName">
          <el-input v-model="queryForm.transactionName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="关联方" prop="relatedParty">
          <el-input v-model="queryForm.relatedParty" placeholder="请输入" clearable style="width: 180px" />
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
        <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="关联方" prop="relatedParty" min-width="140" show-overflow-tooltip />
        <el-table-column label="交易名称" prop="transactionName" min-width="160" show-overflow-tooltip />
        <el-table-column label="交易类型" prop="transactionType" width="100" align="center">
          <template slot-scope="scope">
            {{ transactionTypeMap[scope.row.transactionType] || scope.row.transactionType || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="交易金额(万元)" prop="transactionAmount" width="130" align="right">
          <template slot-scope="scope">
            {{ scope.row.transactionAmount != null ? Number(scope.row.transactionAmount).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="关联关系" prop="relationType" width="120" align="center">
          <template slot-scope="scope">
            {{ relationTypeMap[scope.row.relationType] || scope.row.relationType || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="是否重大" prop="isMajor" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isMajor === '1' ? 'danger' : 'info'" size="small">
              {{ scope.row.isMajor === '1' ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="risk" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.risk === '高' ? 'danger' : scope.row.risk === '中' ? 'warning' : 'info'" size="small">
              {{ scope.row.risk || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="报告期" prop="period" width="100" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <!-- 新增/编辑/查看对话框 -->
    <el-dialog :title="{ add: '新增关联交易', edit: '编辑关联交易', view: '查看关联交易' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入企业名称" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="关联方" prop="partyName">
          <el-input v-model="form.partyName" placeholder="请输入关联方名称" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="交易名称" prop="transactionName">
          <el-input v-model="form.transactionName" placeholder="请输入交易名称" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="交易类型" prop="transactionType">
          <el-select v-model="form.transactionType" placeholder="请选择交易类型" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option v-for="item in transactionTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联关系" prop="relationType">
          <el-select v-model="form.relationType" placeholder="请选择关联关系" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option v-for="item in relationTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易金额(万元)" prop="transactionAmount">
          <el-input-number v-model="form.transactionAmount" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="报告期" prop="period">
          <el-date-picker v-model="form.period" type="month" value-format="yyyy-MM" placeholder="请选择报告期" :disabled="dialogType === 'view'" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否重大">
          <el-radio-group v-model="form.isMajor" :disabled="dialogType === 'view'">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
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
import { getRelatedTransactionList, addRelatedTransaction, updateRelatedTransaction, deleteRelatedTransaction } from '@/api/stateAssets/financePenetration'
import { mapGetters } from 'vuex'

// 交易类型中英文映射
const TRANSACTION_TYPE_MAP = {
  PURCHASE: '采购',
  SALE: '销售',
  SERVICE: '服务',
  LEASE: '租赁',
  LOAN: '借贷',
  GUARANTEE: '担保',
  TRANSFER: '转让',
  OTHER: '其他',
}

// 关联关系中英文映射
const RELATION_TYPE_MAP = {
  SUBSIDIARY: '子公司',
  ASSOCIATE: '联营企业',
  JOINT_VENTURE: '合营企业',
  KEY_MANAGEMENT: '关键管理人员',
  SHAREHOLDER: '股东',
  PARENT: '母公司',
  SIBLING: '兄弟公司',
  OTHER: '其他',
}

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
  name: 'FinancePenetrationRelatedTransaction',
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, transactionName: '', relatedParty: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: { companyName: '', partyName: '', transactionName: '', transactionType: '', relationType: '', transactionAmount: null, period: '', isMajor: '0' },
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        partyName: [{ required: true, message: '请输入关联方名称', trigger: 'blur' }],
        transactionName: [{ required: true, message: '请输入交易名称', trigger: 'blur' }],
        transactionType: [{ required: true, message: '请选择交易类型', trigger: 'change' }],
        relationType: [{ required: true, message: '请选择关联关系', trigger: 'change' }],
        period: [{ required: true, message: '请选择报告期', trigger: 'change' }],
      },
      transactionTypeMap: TRANSACTION_TYPE_MAP,
      relationTypeMap: RELATION_TYPE_MAP,
      transactionTypeOptions: Object.entries(TRANSACTION_TYPE_MAP).map(([value, label]) => ({ value, label })),
      relationTypeOptions: Object.entries(RELATION_TYPE_MAP).map(([value, label]) => ({ value, label })),
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getRelatedTransactionList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm = { pageNumber: 1, pageSize: 10, transactionName: '', relatedParty: '' }
      this.fetchData()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = { companyName: '', partyName: '', transactionName: '', transactionType: '', relationType: '', transactionAmount: null, period: '', isMajor: '0' }
      this.dialogVisible = true
    },
    handleView(row) {
      this.dialogType = 'view'
      this.form = {
        partyId: row.partyId,
        companyName: row.companyName || '',
        partyName: row.relatedParty || '',
        transactionName: row.transactionName || '',
        transactionType: row.transactionType || '',
        relationType: row.relationType || '',
        transactionAmount: row.transactionAmount,
        period: row.period || '',
        isMajor: row.isMajor || '0',
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = {
        partyId: row.partyId,
        companyName: row.companyName || '',
        partyName: row.relatedParty || '',
        transactionName: row.transactionName || '',
        transactionType: row.transactionType || '',
        relationType: row.relationType || '',
        transactionAmount: row.transactionAmount,
        period: row.period || '',
        isMajor: row.isMajor || '0',
      }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该关联交易记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteRelatedTransaction(row.partyId)
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
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const submitData = { ...this.form }
          const res = await (this.dialogType === 'add' ? addRelatedTransaction : updateRelatedTransaction)(submitData)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
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
  },
}
</script>
<style lang="scss" scoped>
.finance-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { margin-bottom: 0; border-left: 3px solid #722ed1; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: #f9f0ff; }
::v-deep .el-card { border-radius: 6px; }
</style>