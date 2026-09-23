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
        <el-table-column label="收款单号" prop="receiptNo" width="180" />
        <el-table-column label="收款日期" prop="receiptDate" width="120">
          <template slot-scope="scope">
            {{ formatDateCN(scope.row.receiptDate) }}
          </template>
        </el-table-column>
        <el-table-column label="付款方" prop="payerName" width="200" />
        <el-table-column label="收款金额" prop="receiptAmount" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.receiptAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="币种" prop="currencyCode" width="80">
          <template slot-scope="scope">
            {{ getCurrencyText(scope.row.currencyCode) }}
          </template>
        </el-table-column>
        <el-table-column label="收款账户" prop="accountName" width="200">
          <template slot-scope="scope">
            {{ getAccountName(scope.row.accountId) }}
          </template>
        </el-table-column>
        <el-table-column label="收款状态" prop="receiptStatus" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.receiptStatus)">
              {{ getStatusText(scope.row.receiptStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createdTime" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
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
                :disabled="isView"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款账户" prop="accountId">
              <el-select v-model="form.accountId" placeholder="请选择收款账户" @change="handleAccountChange" :disabled="isView">
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
              <el-input v-model="form.payerName" placeholder="请输入付款方名称" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方账号" prop="payerAccount">
              <el-input v-model="form.payerAccount" placeholder="请输入付款方账号" :disabled="isView" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款金额" prop="receiptAmount">
              <el-input v-model="form.receiptAmount" placeholder="请输入收款金额" type="number" :disabled="isView" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="form.currencyCode" placeholder="请选择币种" :disabled="isView">
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收款用途" prop="receiptPurpose">
          <el-input v-model="form.receiptPurpose" type="textarea" placeholder="请输入收款用途" :disabled="isView" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :disabled="isView" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isView">确 定</el-button>
        <el-button @click="cancel">{{ isView ? '关 闭' : '取 消' }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getReceiptPage,
  getReceipt,
  createReceipt,
  updateReceipt,
  deleteReceipt,
  batchDeleteReceipt,
  confirmReceipt,
  verifyReceipt,
  cancelReceipt,
  printReceipt,
  exportReceipt,
  exportReceiptSingle
} from '@/api/globalTreasurer/xjgl'
import { getAccountInfoPage } from '@/api/globalTreasurer/zhgl'
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
      // 是否为查看模式
      isView: false,
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
          this.total = parseInt(response.data.total) || 0
        } else if (response.data && response.data.tlist !== undefined) {
          // PageInfo格式
          this.receiptList = response.data.tlist || []
          this.total = parseInt(response.data.totalRecord) || 0
        } else if (response.data && response.data.list !== undefined) {
          // 标准格式
          this.receiptList = response.data.list || []
          this.total = parseInt(response.data.total) || 0
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
        console.log('正在获取账户选项...')

        // 调用全球司库账户信息接口
        // 注意：后端会从当前登录用户的session中获取orgId，前端不需要传递
        const response = await this.callApiWithFallback(
          () => getAccountInfoPage({
            pageNum: 1,
            pageSize: 1000 // 获取所有账户
            // 后端会自动使用当前用户的orgId，不需要前端传递
          }),
          mockDataGenerators.accountList,
          '获取账户列表失败'
        )

        console.log('API返回的原始响应:', response)

        // 支持多种数据结构格式
        let accounts = []
        if (response.data && response.data.records !== undefined) {
          // MyBatis Plus分页格式
          accounts = response.data.records || []
          console.log('使用 records 格式, 总数:', response.data.total)
        } else if (response.data && response.data.tlist !== undefined) {
          // PageInfo格式
          accounts = response.data.tlist || []
          console.log('使用 tlist 格式, 总数:', response.data.totalRecord)
        } else if (response.data && Array.isArray(response.data)) {
          // 数组格式（模拟数据）
          accounts = response.data || []
          console.log('使用数组格式, 总数:', accounts.length)
        }

        console.log('解析后的账户列表:', accounts)
        console.log('账户列表长度:', accounts.length)

        // TblGtAccountInfo 实体已经有 accountId 字段，直接使用
        // 将accountId转为字符串避免JavaScript大数字精度问题
        this.accountOptions = accounts.map(account => ({
          accountId: String(account.accountId),
          accountName: account.accountName,
          accountNumber: account.accountNumber,
          bankName: account.bankName,
          currencyCode: account.currencyCode,
          accountStatus: account.accountStatus || 'ACTIVE'
        }))

        console.log('收款账户选项数据:', this.accountOptions)
      } catch (error) {
        console.error('获取账户列表失败:', error)
        // 🔴 出错时使用模拟数据
        const mockAccounts = mockDataGenerators.accountList()
        this.accountOptions = mockAccounts.map(account => ({
          accountId: account.accountId,
          accountName: account.accountName,
          accountNumber: account.accountNumber,
          bankName: account.bankName,
          currencyCode: account.currencyCode,
          accountStatus: 'ACTIVE'
        }))
        console.log('使用模拟账户数据:', this.accountOptions)
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
      if (this.$refs.queryForm) {
        this.$refs.queryForm.resetFields()
      }
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
      console.log('点击新增按钮')
      this.reset()

      // 如果账户列表为空，重新加载
      if (!this.accountOptions || this.accountOptions.length === 0) {
        console.log('账户列表为空，正在重新加载...')
        this.getAccountOptions()
      }

      this.open = true
      this.isView = false
      this.title = '添加收款单'
      console.log('dialog状态:', this.open)
      console.log('当前账户选项数量:', this.accountOptions.length)
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const receiptId = row.receiptId || this.ids[0]
      getReceipt(receiptId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.form = response.data
          // 确保accountId是字符串类型
          if (this.form.accountId) {
            this.form.accountId = String(this.form.accountId)
          }
          // 转换receiptDate：时间戳转字符串格式
          if (this.form.receiptDate) {
            this.form.receiptDate = this.formatDate(this.form.receiptDate)
          }
          this.open = true
          this.isView = false
          this.title = '修改收款单'
        }
      })
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 转换数据格式
          const formData = { ...this.form }

          console.log('=== 提交前原始数据 ===')
          console.log('this.form:', this.form)
          console.log('formData:', formData)
          console.log('formData.accountId:', formData.accountId)
          console.log('formData.accountId类型:', typeof formData.accountId)

          // 转换日期格式：yyyy-MM-dd 字符串转时间戳（毫秒）
          if (formData.receiptDate && typeof formData.receiptDate === 'string') {
            // 将 yyyy-MM-dd 格式的字符串转换为时间戳
            const date = new Date(formData.receiptDate)
            formData.receiptDate = date.getTime()
          }

          // 转换accountId：字符串转数字
          if (formData.accountId && typeof formData.accountId === 'string') {
            console.log('accountId转换前:', formData.accountId)
            formData.accountId = parseInt(formData.accountId, 10)
            console.log('accountId转换后:', formData.accountId)
          }

          console.log('=== 最终提交数据 ===')
          console.log('formData:', formData)
          console.log('formData.accountId:', formData.accountId)

          if (this.form.receiptId != null) {
            updateReceipt(formData).then(response => {
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
            createReceipt(formData).then(response => {
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
        return batchDeleteReceipt({ ids: receiptIds })
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
        // 显示加载提示
        const loading = this.$loading({
          lock: true,
          text: '正在确认...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        // 批量确认：对每个收款单单独调用确认接口
        const promises = receiptIds.map(receiptId => {
          return confirmReceipt(receiptId).then(response => {
            const successCodes = [200, 0, '200', '0', '1', 1, 2]
            if (successCodes.includes(response.code)) {
              return { success: true, receiptId }
            } else {
              return { success: false, receiptId, message: response.message }
            }
          }).catch(error => {
            return { success: false, receiptId, message: error.message }
          })
        })

        return Promise.all(promises).then(results => {
          loading.close()

          const successCount = results.filter(r => r.success).length
          const failCount = results.length - successCount

          if (failCount === 0) {
            this.$message.success(`成功确认 ${successCount} 条收款单`)
            this.getList()
          } else if (successCount === 0) {
            this.$message.error('确认失败')
          } else {
            this.$message.warning(`成功 ${successCount} 条，失败 ${failCount} 条`)
            this.getList()
          }
        })
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
      this.isView = false
      // 重置表单验证状态
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
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

    /** 获取币种文本 */
    getCurrencyText(currencyCode) {
      const currencyMap = {
        'CNY': '人民币',
        'USD': '美元',
        'EUR': '欧元',
        'JPY': '日元',
        'HKD': '港币',
        'GBP': '英镑',
        'AUD': '澳元',
        'CAD': '加元'
      }
      return currencyMap[currencyCode] || currencyCode || '-'
    },

    /** 获取账户名称 */
    getAccountName(accountId) {
      if (!accountId) return '-'
      // 确保两边都是字符串类型进行比较
      const accountIdStr = String(accountId)
      const account = this.accountOptions.find(item => item.accountId === accountIdStr)
      return account ? account.accountName : '-'
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
      this.reset()
      const receiptId = row.receiptId
      getReceipt(receiptId).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.form = response.data
          // 确保accountId是字符串类型
          if (this.form.accountId) {
            this.form.accountId = String(this.form.accountId)
          }
          // 转换receiptDate：时间戳转字符串格式
          if (this.form.receiptDate) {
            this.form.receiptDate = this.formatDate(this.form.receiptDate)
          }
          this.open = true
          this.isView = true
          this.title = '查看收款单'
          // 查看模式禁用表单字段
          this.$nextTick(() => {
            if (this.$refs.form) {
              this.$refs.form.fields.forEach(field => {
                field.isDisabled = true
              })
            }
          })
        }
      }).catch(() => {
        this.$message.error('查询收款单详情失败')
      })
    },

    /** 核销操作 */
    handleVerify(row) {
      const receiptId = row.receiptId
      this.$confirm('是否确认核销该收款单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return verifyReceipt(receiptId, this.$store.getters.staffId)
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('核销成功')
          this.getList()
        } else {
          this.$message.error(response.message || '核销失败')
        }
      }).catch(() => {})
    },

    /** 导出操作 */
    handleExport() {
      this.$confirm('是否确认导出所有收款单数据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 导出所有数据，不分页
        const params = { ...this.queryParams }
        params.pageNum = 1
        params.pageSize = 100000

        // 显示加载提示
        const loading = this.$loading({
          lock: true,
          text: '正在导出...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        // 使用 API 方法导出
        exportReceipt(params).then(response => {
          loading.close()

          // 处理blob响应
          const blob = new Blob([response], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })

          // 创建下载链接
          const link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = `收款单数据_${new Date().getTime()}.xlsx`
          document.body.appendChild(link)
          link.click()

          // 清理
          document.body.removeChild(link)
          window.URL.revokeObjectURL(link.href)

          this.$message.success('导出成功')
        }).catch(error => {
          loading.close()
          console.error('导出失败:', error)
          this.$message.error('导出失败: ' + (error.message || '未知错误'))
        })
      }).catch(() => {})
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
      const receiptId = row.receiptId
      this.$confirm('是否确认取消该收款单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return cancelReceipt(receiptId)
      }).then(response => {
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          this.$message.success('取消成功')
          this.getList()
        } else {
          this.$message.error(response.message || '取消失败')
        }
      }).catch(() => {})
    },

    /** 打印收款单 */
    handlePrint(row) {
      const receiptId = row.receiptId
      printReceipt(receiptId).then(response => {
        // 创建新窗口打印
        const blob = new Blob([response], { type: 'application/pdf' })
        const url = window.URL.createObjectURL(blob)
        const printWindow = window.open(url, '_blank')
        if (printWindow) {
          printWindow.onload = () => {
            printWindow.print()
          }
        }
        this.$message.success('打印预览已打开')
      }).catch(() => {
        this.$message.error('打印失败')
      })
    },

    /** 导出单个收款单 */
    handleExportSingle(row) {
      const receiptId = row.receiptId

      // 显示加载提示
      const loading = this.$loading({
        lock: true,
        text: '正在导出...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      // 使用 API 方法导出
      exportReceiptSingle(receiptId).then(response => {
        loading.close()

        // 处理blob响应
        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })

        // 创建下载链接
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `收款单_${row.receiptNo || receiptId}.xlsx`
        document.body.appendChild(link)
        link.click()

        // 清理
        document.body.removeChild(link)
        window.URL.revokeObjectURL(link.href)

        this.$message.success('导出成功')
      }).catch(error => {
        loading.close()
        console.error('导出失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      })
    },

    /** 格式化日期时间为年月日格式 */
    formatDateTime(datetime) {
      if (!datetime) return '-'
      const date = new Date(datetime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}年${month}月${day}日 ${hour}:${minute}:${second}`
    },

    /** 格式化日期为 yyyy-MM-dd 格式（用于日期选择器） */
    formatDate(date) {
      if (!date) return null
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    /** 格式化日期为 yyyy年MM月dd日 格式（用于表格显示） */
    formatDateCN(date) {
      if (!date) return '-'
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}年${month}月${day}日`
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
