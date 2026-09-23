<template>
  <div class="cash-payment-container">
    <!-- 模拟数据提示 -->
    <MockDataNotice
      :show="useMockData"
      title="当前使用模拟数据"
      description="检测到网络连接问题，当前显示的是模拟数据。请检查网络连接后刷新页面获取真实数据。"
      type="warning"
      :closable="false"
    />

    <!-- 页面标题 -->
    <div class="page-header">
      <h2>付款管理</h2>
      <p>管理企业付款业务，包括付款申请、审批、执行和监控</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="付款单号" prop="paymentNumber">
          <el-input
            v-model="queryParams.paymentNumber"
            placeholder="请输入付款单号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="收款方" prop="payeeName">
          <el-input
            v-model="queryParams.payeeName"
            placeholder="请输入收款方名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="付款状态" prop="paymentStatus">
          <el-select v-model="queryParams.paymentStatus" placeholder="请选择付款状态" clearable>
            <el-option label="待审批" value="PENDING" />
            <el-option label="审批中" value="APPROVING" />
            <el-option label="待执行" value="APPROVED" />
            <el-option label="执行中" value="EXECUTING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已拒绝" value="REJECTED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="付款日期" prop="paymentDate">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增付款</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-check" size="mini" :disabled="multiple" @click="handleSubmit">提交审批</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-s-promotion" size="mini" :disabled="multiple" @click="handleExecute">执行付款</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table v-loading="loading" :data="paymentList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="付款单号" prop="paymentNumber" width="180" />
        <el-table-column label="付款日期" prop="paymentDate" width="120" />
        <el-table-column label="收款方" prop="payeeName" width="200" />
        <el-table-column label="付款金额" prop="paymentAmount" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.paymentAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currencyCode" width="80" />
        <el-table-column label="付款账户" prop="accountName" width="200" />
        <el-table-column label="付款状态" prop="paymentStatus" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.paymentStatus)">
              {{ getStatusText(scope.row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" />
        <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-if="scope.row.paymentStatus === 'PENDING'">修改</el-button>
            <el-button size="mini" type="text" @click="handleSubmit(scope.row)" v-if="scope.row.paymentStatus === 'PENDING'">提交</el-button>
            <el-button size="mini" type="text" @click="handleExecute(scope.row)" v-if="scope.row.paymentStatus === 'APPROVED'">执行</el-button>
            <el-dropdown @command="handleCommand" v-if="scope.row.paymentStatus !== 'CANCELLED'">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'cancel', row: scope.row}" v-if="['PENDING', 'APPROVING'].includes(scope.row.paymentStatus)">取消</el-dropdown-item>
                <el-dropdown-item :command="{action: 'reject', row: scope.row}" v-if="scope.row.paymentStatus === 'APPROVING'">拒绝</el-dropdown-item>
                <el-dropdown-item :command="{action: 'print', row: scope.row}">打印</el-dropdown-item>
                <el-dropdown-item :command="{action: 'export', row: scope.row}">导出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <el-pagination
      background
      :current-page="queryParams.pageNum"
      :layout="layout"
      :page-size="queryParams.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <!-- 添加或修改付款单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="付款日期" prop="paymentDate">
              <el-date-picker
                v-model="form.paymentDate"
                type="date"
                placeholder="选择付款日期"
                value-format="yyyy-MM-dd"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款账户" prop="accountId">
              <el-select v-model="form.accountId" placeholder="请选择付款账户" @change="handleAccountChange">
                <el-option
                  v-for="account in accountOptions"
                  :key="account.accountId"
                  :label="account.accountName"
                  :value="account.accountId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款方名称" prop="payeeName">
              <el-input v-model="form.payeeName" placeholder="请输入收款方名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款方账号" prop="payeeAccount">
              <el-input v-model="form.payeeAccount" placeholder="请输入收款方账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款方开户行" prop="payeeBank">
              <el-input v-model="form.payeeBank" placeholder="请输入收款方开户行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select v-model="form.paymentMethod" placeholder="请选择付款方式">
                <el-option label="网银转账" value="ONLINE_TRANSFER" />
                <el-option label="银企直联" value="BANK_DIRECT" />
                <el-option label="现金支付" value="CASH" />
                <el-option label="支票支付" value="CHECK" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="付款金额" prop="paymentAmount">
              <el-input v-model="form.paymentAmount" placeholder="请输入付款金额" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="form.currencyCode" placeholder="请选择币种">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="付款用途" prop="paymentPurpose">
          <el-input v-model="form.paymentPurpose" type="textarea" placeholder="请输入付款用途" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPaymentPage, getPayment, createPayment, updatePayment, deletePayment, submitPayment, executePayment } from '@/api/globalTreasurer/xjgl'
import { getAccountList } from '@/api/globalTreasurer/zhgl'
import globalTreasurerMixin from '@/mixins/globalTreasurerMixin'
import { mockDataGenerators } from '@/utils/mockData/globalTreasurerMockData'

export default {
  mixins: [globalTreasurerMixin],
  name: 'CashPaymentManagement',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 付款单表格数据
      paymentList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 账户选项
      accountOptions: [],
      // 分页布局
      layout: 'total, sizes, prev, pager, next, jumper',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        paymentNumber: null,
        payeeName: null,
        paymentStatus: null,
        startDate: null,
        endDate: null,
        orgId: this.$store.getters.orgId
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        paymentDate: [
          { required: true, message: '付款日期不能为空', trigger: 'blur' }
        ],
        accountId: [
          { required: true, message: '付款账户不能为空', trigger: 'change' }
        ],
        payeeName: [
          { required: true, message: '收款方名称不能为空', trigger: 'blur' }
        ],
        payeeAccount: [
          { required: true, message: '收款方账号不能为空', trigger: 'blur' }
        ],
        paymentAmount: [
          { required: true, message: '付款金额不能为空', trigger: 'blur' }
        ],
        currencyCode: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ],
        paymentMethod: [
          { required: true, message: '付款方式不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getAccountOptions()
  },
  methods: {
    /** 查询付款单列表 */
    async getList() {
      this.loading = true
      try {
        // 处理日期范围
        if (this.dateRange && this.dateRange.length === 2) {
          this.queryParams.startDate = this.dateRange[0]
          this.queryParams.endDate = this.dateRange[1]
        } else {
          this.queryParams.startDate = null
          this.queryParams.endDate = null
        }

        // 使用混入的通用API调用方法，包含错误处理和模拟数据回退
        const response = await this.callApiWithFallback(
          () => getPaymentPage(this.queryParams),
          mockDataGenerators.paymentManagement,
          '获取付款单列表失败'
        )

        // 支持多种数据结构格式
        if (response.data && response.data.records !== undefined) {
          // MyBatis Plus分页格式
          this.paymentList = response.data.records || []
          this.total = response.data.total || 0
        } else if (response.data && response.data.tlist !== undefined) {
          // PageInfo格式
          this.paymentList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else if (response.data && response.data.list !== undefined) {
          // 标准格式
          this.paymentList = response.data.list || []
          this.total = response.data.total || 0
        } else if (response.data && Array.isArray(response.data)) {
          // 数组格式
          this.paymentList = response.data || []
          this.total = response.data.length || 0
        } else {
          // 兜底处理
          this.paymentList = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取付款单列表失败:', error)
        this.paymentList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    /** 获取账户选项 */
    async getAccountOptions() {
      try {
        // 使用混入的通用API调用方法，包含错误处理和模拟数据回退
        const response = await this.callApiWithFallback(
          () => getAccountList({ orgId: this.$store.getters.orgId }),
          mockDataGenerators.accountList,
          '获取账户列表失败'
        )

        this.accountOptions = response.data || []
      } catch (error) {
        console.error('获取账户列表失败:', error)
        this.accountOptions = []
      }
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.paymentId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加付款单'
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const paymentId = row.paymentId || this.ids[0]
      getPayment(paymentId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.form = response.data
          this.open = true
          this.title = '修改付款单'
        }
      })
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.paymentId != null) {
            updatePayment(this.form).then(response => {
              const successCodes = [200, 0, '200', '0', '1', 1, 2]
              if (successCodes.includes(response.code)) {
                this.$message.success('修改成功')
                this.open = false
                this.getList()
              } else {
                this.$message.error(response.message || '修改失败')
              }
            })
          } else {
            createPayment(this.form).then(response => {
              const successCodes = [200, 0, '200', '0', '1', 1, 2]
              if (successCodes.includes(response.code)) {
                this.$message.success('新增成功')
                this.open = false
                this.getList()
              } else {
                this.$message.error(response.message || '新增失败')
              }
            })
          }
        }
      })
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const paymentIds = row.paymentId ? [row.paymentId] : this.ids
      this.$confirm('是否确认删除选中的付款单？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deletePayment({ ids: paymentIds })
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      }).catch(() => {})
    },

    /** 提交审批 */
    handleSubmit(row) {
      const paymentIds = row.paymentId ? [row.paymentId] : this.ids
      this.$confirm('是否确认提交选中的付款单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return submitPayment({ ids: paymentIds })
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('提交成功')
          this.getList()
        } else {
          this.$message.error(response.message || '提交失败')
        }
      }).catch(() => {})
    },

    /** 执行付款 */
    handleExecute(row) {
      const paymentIds = row.paymentId ? [row.paymentId] : this.ids
      this.$confirm('是否确认执行选中的付款单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return executePayment({ ids: paymentIds })
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('执行成功')
          this.getList()
        } else {
          this.$message.error(response.message || '执行失败')
        }
      }).catch(() => {})
    },

    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },

    /** 表单重置 */
    reset() {
      this.form = {
        paymentId: null,
        paymentNumber: null,
        paymentDate: null,
        accountId: null,
        payeeName: null,
        payeeAccount: null,
        payeeBank: null,
        paymentAmount: null,
        currencyCode: 'CNY',
        paymentMethod: 'ONLINE_TRANSFER',
        paymentPurpose: null,
        remark: null,
        orgId: this.$store.getters.orgId
      }
      this.resetForm('form')
    },

    /** 分页大小改变 */
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },

    /** 当前页改变 */
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },

    /** 格式化金额 */
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    /** 获取状态标签类型 */
    getStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'APPROVING': 'warning',
        'APPROVED': 'primary',
        'EXECUTING': 'warning',
        'COMPLETED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return tagMap[status] || 'info'
    },

    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'PENDING': '待审批',
        'APPROVING': '审批中',
        'APPROVED': '待执行',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'REJECTED': '已拒绝',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    /** 账户改变事件 */
    handleAccountChange(accountId) {
      const account = this.accountOptions.find(item => item.accountId === accountId)
      if (account) {
        this.form.currencyCode = account.currencyCode
      }
    },

    /** 查看详情 */
    handleView(row) {
      this.$message.info('查看功能待实现')
    },

    /** 导出操作 */
    handleExport() {
      this.$message.info('导出功能待实现')
    },

    /** 更多操作命令 */
    handleCommand(command) {
      switch (command.action) {
        case 'cancel':
          this.handleCancel(command.row)
          break
        case 'reject':
          this.handleReject(command.row)
          break
        case 'print':
          this.handlePrint(command.row)
          break
        case 'export':
          this.handleExportSingle(command.row)
          break
      }
    },

    /** 取消付款 */
    handleCancel(row) {
      this.$message.info('取消功能待实现')
    },

    /** 拒绝付款 */
    handleReject(row) {
      this.$message.info('拒绝功能待实现')
    },

    /** 打印付款单 */
    handlePrint(row) {
      this.$message.info('打印功能待实现')
    },

    /** 导出单个付款单 */
    handleExportSingle(row) {
      this.$message.info('导出功能待实现')
    }
  }
}
</script>

<style scoped>
.cash-payment-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
