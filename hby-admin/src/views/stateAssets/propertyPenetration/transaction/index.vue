<template>
  <div class="app-container property-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-order"></i><span>产权交易管理</span></div>
      <div class="page-header-desc">管理产权交易记录与审批流程</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="交易类型" prop="transactionType">
          <el-select v-model="queryForm.transactionType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="转让" value="TRANSFER" />
            <el-option label="增资" value="CAPITAL_INCREASE" />
            <el-option label="减资" value="CAPITAL_DECREASE" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-select v-model="queryForm.approvalStatus" placeholder="请选择" clearable style="width: 150px">
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
            <el-option label="已完成" value="COMPLETED" />
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
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="交易类型" width="100" align="center">
          <template slot-scope="scope">
            {{ transactionTypeMap[scope.row.transactionType] || scope.row.transactionType }}
          </template>
        </el-table-column>
        <el-table-column label="交易金额(万元)" prop="transactionAmount" width="130" align="right" />
        <el-table-column label="交易对手" prop="counterparty" min-width="140" show-overflow-tooltip />
        <el-table-column label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="approvalTagType(scope.row.transactionStatus)" size="small">
              {{ approvalStatusMap[scope.row.transactionStatus] || scope.row.transactionStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="交易日期" prop="transactionDate" width="120" align="center" />
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

    <!-- 新增/编辑/查看弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false" @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="form" label-width="130px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="form.companyName" :disabled="dialogType === 'view'" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易类型" prop="transactionType">
              <el-select v-model="form.transactionType" :disabled="dialogType === 'view'" placeholder="请选择" style="width: 100%">
                <el-option label="转让" value="TRANSFER" />
                <el-option label="增资" value="CAPITAL_INCREASE" />
                <el-option label="减资" value="CAPITAL_DECREASE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易方式" prop="transactionMethod">
              <el-select v-model="form.transactionMethod" :disabled="dialogType === 'view'" placeholder="请选择" style="width: 100%">
                <el-option label="进场交易" value="EXCHANGE" />
                <el-option label="协议转让" value="AGREEMENT" />
                <el-option label="拍卖" value="AUCTION" />
                <el-option label="无偿划转" value="FREE_TRANSFER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易对手" prop="counterparty">
              <el-input v-model="form.counterparty" :disabled="dialogType === 'view'" placeholder="请输入交易对手" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易金额(万元)" prop="transactionAmount">
              <el-input-number v-model="form.transactionAmount" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估价值(万元)" prop="appraisalValue">
              <el-input-number v-model="form.appraisalValue" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易日期" prop="transactionDate">
              <el-date-picker v-model="form.transactionDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType === 'view'" placeholder="请选择交易日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否进场交易">
              <el-select v-model="form.isExchangeTraded" :disabled="dialogType === 'view'" placeholder="请选择" style="width: 100%">
                <el-option label="是" value="1" />
                <el-option label="否" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否有评估报告">
              <el-select v-model="form.hasAppraisal" :disabled="dialogType === 'view'" placeholder="请选择" style="width: 100%">
                <el-option label="是" value="1" />
                <el-option label="否" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注">
              <el-input v-model="form.remark" :disabled="dialogType === 'view'" placeholder="请输入备注" />
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
import { getPropertyTransactionList, addPropertyTransaction, updatePropertyTransaction, deletePropertyTransaction } from '@/api/stateAssets/propertyPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'PropertyPenetrationTransaction',
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
        transactionType: '',
        approvalStatus: '',
      },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      transactionTypeMap: {
        TRANSFER: '转让',
        CAPITAL_INCREASE: '增资',
        CAPITAL_DECREASE: '减资',
      },
      approvalStatusMap: {
        PENDING: '待审批',
        APPROVED: '已审批',
        REJECTED: '已驳回',
        COMPLETED: '已完成',
        IN_PROGRESS: '进行中',
      },
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        transactionType: [{ required: true, message: '请选择交易类型', trigger: 'change' }],
        transactionAmount: [{ required: true, message: '请输入交易金额', trigger: 'blur' }],
        counterparty: [{ required: true, message: '请输入交易对手', trigger: 'blur' }],
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
    dialogTitle() {
      const map = { add: '新增产权交易', edit: '编辑产权交易', view: '查看产权交易' }
      return map[this.dialogType] || ''
    },
  },
  created() {
    this.fetchData()
  },
  methods: {
    /** 查询列表 */
    async fetchData() {
      this.loading = true
      try {
        const res = await getPropertyTransactionList(this.queryForm)
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
    /** 查询 */
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    /** 分页 - 每页条数变化 */
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    /** 分页 - 当前页变化 */
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    /** 新增 */
    handleAdd() {
      this.dialogType = 'add'
      this.form = { isExchangeTraded: '1', hasAppraisal: '1' }
      this.dialogVisible = true
    },
    /** 查看 */
    handleView(row) {
      this.dialogType = 'view'
      this.form = { ...row }
      this.dialogVisible = true
    },
    /** 编辑 */
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
    },
    /** 弹窗关闭 */
    handleDialogClose() {
      this.$refs.form && this.$refs.form.resetFields()
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addPropertyTransaction : updatePropertyTransaction
          const res = await apiFn(this.form)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error((res && res.msg) || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    /** 删除 */
    handleDelete(row) {
      this.$confirm('确认删除该产权交易记录？删除后不可恢复。', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deletePropertyTransaction(row.transactionId)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error((res && res.msg) || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    /** 审批状态标签颜色 */
    approvalTagType(status) {
      const map = { COMPLETED: 'success', APPROVED: 'success', PENDING: 'info', IN_PROGRESS: 'warning', REJECTED: 'danger' }
      return map[status] || 'info'
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