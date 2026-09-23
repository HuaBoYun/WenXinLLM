<template>
  <div class="cash-receipt-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>收款管理</h2>
      <p>管理企业收款业务，包括收款申请、审批、确认和核销</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="收款单号" prop="receiptNumber">
          <el-input
            v-model="queryParams.receiptNumber"
            placeholder="请输入收款单号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="付款方" prop="payerName">
          <el-input
            v-model="queryParams.payerName"
            placeholder="请输入付款方名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="收款状态" prop="receiptStatus">
          <el-select v-model="queryParams.receiptStatus" placeholder="请选择收款状态" clearable>
            <el-option label="待确认" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已核销" value="VERIFIED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="收款日期" prop="receiptDate">
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
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">新增收款</el-button>
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
          <el-button type="info" icon="el-icon-check" size="mini" :disabled="multiple" @click="handleConfirm">确认收款</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table v-loading="loading" :data="receiptList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="收款单号" prop="receiptNumber" width="180" />
        <el-table-column label="收款日期" prop="receiptDate" width="120" />
        <el-table-column label="付款方" prop="payerName" width="200" />
        <el-table-column label="收款金额" prop="receiptAmount" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.receiptAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currencyCode" width="80" />
        <el-table-column label="收款账户" prop="accountName" width="200" />
        <el-table-column label="收款状态" prop="receiptStatus" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.receiptStatus)">
              {{ getStatusText(scope.row.receiptStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" />
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleUpdate(scope.row)" v-if="scope.row.receiptStatus === 'PENDING'">修改</el-button>
            <el-button size="mini" type="text" @click="handleConfirm(scope.row)" v-if="scope.row.receiptStatus === 'PENDING'">确认</el-button>
            <el-button size="mini" type="text" @click="handleVerify(scope.row)" v-if="scope.row.receiptStatus === 'CONFIRMED'">核销</el-button>
            <el-dropdown @command="handleCommand" v-if="scope.row.receiptStatus !== 'CANCELLED'">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'cancel', row: scope.row}" v-if="scope.row.receiptStatus === 'PENDING'">取消</el-dropdown-item>
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

    <!-- 添加或修改收款单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款日期" prop="receiptDate">
              <el-date-picker
                v-model="form.receiptDate"
                type="date"
                placeholder="选择收款日期"
                value-format="yyyy-MM-dd"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款账户" prop="accountId">
              <el-select v-model="form.accountId" placeholder="请选择收款账户" @change="handleAccountChange">
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
            <el-form-item label="付款方名称" prop="payerName">
              <el-input v-model="form.payerName" placeholder="请输入付款方名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方账号" prop="payerAccount">
              <el-input v-model="form.payerAccount" placeholder="请输入付款方账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款金额" prop="receiptAmount">
              <el-input v-model="form.receiptAmount" placeholder="请输入收款金额" type="number" />
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
        <el-form-item label="收款用途" prop="receiptPurpose">
          <el-input v-model="form.receiptPurpose" type="textarea" placeholder="请输入收款用途" />
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
import { getReceiptPage, getReceipt, createReceipt, updateReceipt, deleteReceipt, confirmReceipt } from '@/api/globalTreasurer/xjgl'
import { getAccountList } from '@/api/globalTreasurer/zhgl'
import globalTreasurerMixin from '@/mixins/globalTreasurerMixin'
import { mockDataGenerators } from '@/utils/mockData/globalTreasurerMockData'

export default {
  mixins: [globalTreasurerMixin],
  name: 'CashReceiptManagement',
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
      // 收款单表格数据
      receiptList: [],
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
        receiptNumber: null,
        payerName: null,
        receiptStatus: null,
        startDate: null,
        endDate: null,
        orgId: this.$store.getters.orgId
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        receiptDate: [
          { required: true, message: '收款日期不能为空', trigger: 'blur' }
        ],
        accountId: [
          { required: true, message: '收款账户不能为空', trigger: 'change' }
        ],
        payerName: [
          { required: true, message: '付款方名称不能为空', trigger: 'blur' }
        ],
        receiptAmount: [
          { required: true, message: '收款金额不能为空', trigger: 'blur' }
        ],
        currencyCode: [
          { required: true, message: '币种不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getAccountOptions()
  },
  methods: {
    /** 查询收款单列表 */
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
          () => getReceiptPage(this.queryParams),
          mockDataGenerators.receiptManagement,
          '获取收款单列表失败'
        )

        // 支持多种数据结构格式
        if (response.data && response.data.records !== undefined) {
          // MyBatis Plus分页格式
          this.receiptList = response.data.records || []
          this.total = response.data.total || 0
        } else if (response.data && response.data.tlist !== undefined) {
          // PageInfo格式
          this.receiptList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else if (response.data && response.data.list !== undefined) {
          // 标准格式
          this.receiptList = response.data.list || []
          this.total = response.data.total || 0
        } else if (response.data && Array.isArray(response.data)) {
          // 数组格式
          this.receiptList = response.data || []
          this.total = response.data.length || 0
        } else {
          // 兜底处理
          this.receiptList = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取收款单列表失败:', error)
        this.$message.error('获取数据失败，请检查网络连接')
        this.receiptList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    /** 获取账户选项 */
    async getAccountOptions() {
      try {
        const response = await getAccountList({ orgId: this.$store.getters.orgId })
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.accountOptions = response.data || []
        }
      } catch (error) {
        console.error('获取账户列表失败:', error)
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
      this.ids = selection.map(item => item.receiptId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加收款单'
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const receiptId = row.receiptId || this.ids[0]
      getReceipt(receiptId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.form = response.data
          this.open = true
          this.title = '修改收款单'
        }
      })
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.receiptId != null) {
            updateReceipt(this.form).then(response => {
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
            createReceipt(this.form).then(response => {
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
      const receiptIds = row.receiptId ? [row.receiptId] : this.ids
      this.$confirm('是否确认删除选中的收款单？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteReceipt({ ids: receiptIds })
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

    /** 确认收款 */
    handleConfirm(row) {
      const receiptIds = row.receiptId ? [row.receiptId] : this.ids
      this.$confirm('是否确认选中的收款单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return confirmReceipt({ ids: receiptIds })
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('确认成功')
          this.getList()
        } else {
          this.$message.error(response.message || '确认失败')
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
        receiptId: null,
        receiptNumber: null,
        receiptDate: null,
        accountId: null,
        payerName: null,
        payerAccount: null,
        receiptAmount: null,
        currencyCode: 'CNY',
        receiptPurpose: null,
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
        'PENDING': 'warning',
        'CONFIRMED': 'success',
        'VERIFIED': 'info',
        'CANCELLED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'PENDING': '待确认',
        'CONFIRMED': '已确认',
        'VERIFIED': '已核销',
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

    /** 核销操作 */
    handleVerify(row) {
      this.$message.info('核销功能待实现')
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
        case 'print':
          this.handlePrint(command.row)
          break
        case 'export':
          this.handleExportSingle(command.row)
          break
      }
    },

    /** 取消收款 */
    handleCancel(row) {
      this.$message.info('取消功能待实现')
    },

    /** 打印收款单 */
    handlePrint(row) {
      this.$message.info('打印功能待实现')
    },

    /** 导出单个收款单 */
    handleExportSingle(row) {
      this.$message.info('导出功能待实现')
    }
  }
}
</script>

<style scoped>
.cash-receipt-container {
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
