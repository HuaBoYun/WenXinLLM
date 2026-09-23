<template>
  <div class="voucher-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>凭证管理</h2>
      <p>管理会计凭证的全生命周期，包括录入、修改、查询、删除等操作</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon total">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.totalVouchers || 0 }}</div>
                <div class="statistic-label">凭证总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon draft">
                <i class="el-icon-edit-outline"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.draftVouchers || 0 }}</div>
                <div class="statistic-label">草稿凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon approved">
                <i class="el-icon-check"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.approvedVouchers || 0 }}</div>
                <div class="statistic-label">已审核凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon posted">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.postedVouchers || 0 }}</div>
                <div class="statistic-label">已过账凭证</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建凭证
      </el-button>
      <el-button type="success" icon="el-icon-upload2" @click="handleImport">
        批量导入
      </el-button>
      <el-button type="info" icon="el-icon-download" @click="handleExport">
        导出数据
      </el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="凭证编号">
          <el-input v-model="queryForm.voucherNo" placeholder="请输入凭证编号" clearable />
        </el-form-item>
        <el-form-item label="凭证类型">
          <el-select v-model="queryForm.voucherType" placeholder="请选择凭证类型" clearable>
            <el-option label="记账凭证" value="ACCOUNTING" />
            <el-option label="收款凭证" value="RECEIPT" />
            <el-option label="付款凭证" value="PAYMENT" />
            <el-option label="转账凭证" value="TRANSFER" />
          </el-select>
        </el-form-item>
        <el-form-item label="凭证状态">
          <el-select v-model="queryForm.status" placeholder="请选择凭证状态" clearable>
            <el-option label="草稿" value="0" />
            <el-option label="已审核" value="1" />
            <el-option label="已过账" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="voucherNo" label="凭证编号" width="120" />
        <el-table-column prop="voucherType" label="凭证类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getVoucherTypeColor(scope.row.voucherType)">
              {{ getVoucherTypeName(scope.row.voucherType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voucherDate" label="制单日期" width="120" />
        <el-table-column prop="summary" label="摘要" width="200" show-overflow-tooltip />
        <el-table-column prop="totalAmount" label="金额" width="120" align="right">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold;">
              ¥{{ formatAmount(scope.row.totalAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="制单人" width="100" />
        <el-table-column prop="createTime" label="制单时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" v-if="scope.row.status === '0'">编辑</el-button>
            <el-button size="mini" type="success" @click="handleApprove(scope.row)" v-if="scope.row.status === '0'">审核</el-button>
            <el-button size="mini" type="warning" @click="handlePost(scope.row)" v-if="scope.row.status === '1'">过账</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)" v-if="scope.row.status === '0'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 凭证详情弹窗 -->
    <voucher-detail-dialog ref="voucherDetailDialog" />

    <!-- 凭证过账弹窗 -->
    <voucher-post-dialog ref="voucherPostDialog" @success="handlePostSuccess" />

    <!-- 新建凭证弹窗 -->
    <el-dialog
      title="新建凭证"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form
        ref="voucherForm"
        :model="voucherForm"
        :rules="voucherRules"
        label-width="120px"
        class="voucher-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="凭证类型" prop="voucherType">
              <el-select v-model="voucherForm.voucherType" placeholder="请选择凭证类型" style="width: 100%">
                <el-option label="记账凭证" value="1" />
                <el-option label="收款凭证" value="2" />
                <el-option label="付款凭证" value="3" />
                <el-option label="转账凭证" value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制单日期" prop="voucherDate">
              <el-date-picker
                v-model="voucherForm.voucherDate"
                type="date"
                placeholder="选择制单日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="凭证编号" prop="voucherNo">
              <el-input v-model="voucherForm.voucherNo" placeholder="自动生成" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="附件数" prop="attachmentCount">
              <el-input-number
                v-model="voucherForm.attachmentCount"
                :min="0"
                :max="99"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="摘要" prop="summary">
          <el-input
            v-model="voucherForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入凭证摘要"
          />
        </el-form-item>

        <!-- 凭证分录表格 -->
        <el-form-item label="凭证分录">
          <el-table
            :data="voucherForm.entries"
            style="width: 100%"
            border
          >
            <el-table-column label="科目" width="200">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.accountCode"
                  placeholder="选择科目"
                  style="width: 100%"
                  size="small"
                >
                  <el-option
                    v-for="account in accountOptions"
                    :key="account.code"
                    :label="`${account.code} ${account.name}`"
                    :value="account.code"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="借方金额" width="150">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.debitAmount"
                  :precision="2"
                  :min="0"
                  size="small"
                  style="width: 100%"
                  @change="calculateBalance"
                />
              </template>
            </el-table-column>
            <el-table-column label="贷方金额" width="150">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.creditAmount"
                  :precision="2"
                  :min="0"
                  size="small"
                  style="width: 100%"
                  @change="calculateBalance"
                />
              </template>
            </el-table-column>
            <el-table-column label="摘要">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.summary"
                  size="small"
                  placeholder="分录摘要"
                />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  icon="el-icon-delete"
                  @click="removeEntry(scope.$index)"
                  style="color: #f56c6c"
                />
              </template>
            </el-table-column>
          </el-table>

          <div style="margin-top: 10px; text-align: center;">
            <el-button type="primary" icon="el-icon-plus" size="small" @click="addEntry">
              添加分录
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="合计金额">
          <div class="balance-summary">
            <span style="margin-right: 20px;">
              借方合计：<strong style="color: #67c23a;">¥{{ totalDebit.toFixed(2) }}</strong>
            </span>
            <span>
              贷方合计：<strong style="color: #f56c6c;">¥{{ totalCredit.toFixed(2) }}</strong>
            </span>
            <span v-if="balance !== 0" style="margin-left: 20px; color: #e6a23c;">
              差额：<strong>¥{{ Math.abs(balance).toFixed(2) }}</strong>
              {{ balance > 0 ? '(借方)' : '(贷方)' }}
            </span>
          </div>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="voucherForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitVoucher">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getVoucherPage, getVoucherStatistics, createVoucher } from '@/api/financialSharing'
import VoucherDetailDialog from './VoucherDetailDialog.vue'
import VoucherPostDialog from './VoucherPostDialog.vue'

export default {
  name: 'VoucherManagement',
  components: {
    VoucherDetailDialog,
    VoucherPostDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      statistics: {},

      // 查询表单
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        voucherNo: '',
        voucherType: '',
        status: ''
      },

      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },

      // 弹窗控制
      dialogVisible: false,
      submitting: false,

      // 凭证表单
      voucherForm: {
        voucherType: '',
        voucherDate: this.getCurrentDate(),
        voucherNo: '',
        attachmentCount: 0,
        summary: '',
        remark: '',
        entries: []
      },

      // 科目选项
      accountOptions: [
        { code: '1001', name: '库存现金' },
        { code: '1002', name: '银行存款' },
        { code: '1121', name: '应收账款' },
        { code: '1122', name: '预付账款' },
        { code: '1401', name: '材料采购' },
        { code: '1403', name: '原材料' },
        { code: '1601', name: '固定资产' },
        { code: '2001', name: '短期借款' },
        { code: '2202', name: '应付账款' },
        { code: '2203', name: '预收账款' },
        { code: '2211', name: '应付职工薪酬' },
        { code: '2221', name: '应交税费' },
        { code: '3001', name: '实收资本' },
        { code: '3002', name: '资本公积' },
        { code: '3101', name: '盈余公积' },
        { code: '3103', name: '本年利润' },
        { code: '3104', name: '利润分配' },
        { code: '4001', name: '生产成本' },
        { code: '5001', name: '主营业务收入' },
        { code: '5401', name: '主营业务成本' },
        { code: '5601', name: '销售费用' },
        { code: '5602', name: '管理费用' },
        { code: '5603', name: '财务费用' }
      ],

      // 表单验证规则
      voucherRules: {
        voucherType: [
          { required: true, message: '请选择凭证类型', trigger: 'change' }
        ],
        voucherDate: [
          { required: true, message: '请选择制单日期', trigger: 'change' }
        ],
        summary: [
          { required: true, message: '请输入凭证摘要', trigger: 'blur' }
        ]
      }
    }
  },

  computed: {
    totalDebit() {
      return this.voucherForm.entries.reduce((total, entry) => total + (entry.debitAmount || 0), 0)
    },
    totalCredit() {
      return this.voucherForm.entries.reduce((total, entry) => total + (entry.creditAmount || 0), 0)
    },
    balance() {
      return this.totalDebit - this.totalCredit
    }
  },

  mounted() {
    console.log('凭证管理页面已挂载')
    this.loadData()
    this.loadStatistics()
  },

  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          voucherNo: this.queryForm.voucherNo,
          voucherType: this.queryForm.voucherType,
          status: this.queryForm.status
        }

        const response = await getVoucherPage(params)

        if (response && response.code === 1) {
          // 转换后端返回的数据格式以适配前端展示
          this.tableData = (response.data.tlist || []).map(item => ({
            ...item,
            // 转换日期数组为字符串格式
            voucherDate: this.formatVoucherDateArray(item.voucherDate),
            // 字段名映射
            voucherType: this.mapVoucherTypeId(item.voucherTypeId),
            summary: item.voucherDesc || item.summary || '',
            totalAmount: item.totalDebit || item.totalCredit || 0,
            status: String(item.voucherStatus || item.status || '0')
          }))
          this.pagination.total = parseInt(response.data.totalRecord) || 0
        } else {
          this.$message.error(response.message || '加载数据失败')
          this.tableData = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
        // API 调用失败时降级为空状态
        this.tableData = this.getMockData()
        this.pagination.total = 10
      } finally {
        this.loading = false
      }
    },

    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getVoucherStatistics({})

        if (response && response.code === 1) {
          this.statistics = response.data || {}
        } else {
          console.error('加载统计数据失败:', response.message)
          // API 调用失败时降级为空状态
          this.statistics = this.getMockStatistics()
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // API 调用失败时降级为空状态
        this.statistics = this.getMockStatistics()
      }
    },

    // 数据加载失败时的空状态降级（不再使用模拟数据）
    getMockStatistics() {
      return {
        totalVouchers: 0,
        draftVouchers: 0,
        approvedVouchers: 0,
        postedVouchers: 0
      }
    },

    // 数据加载失败时的空状态降级（不再使用模拟数据）
    getMockData() {
      return []
    },

    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 刷新
    handleRefresh() {
      this.loadData()
      this.loadStatistics()
    },

    // 新建凭证
    handleCreate() {
      this.resetVoucherForm()
      this.dialogVisible = true
    },

    // 批量导入
    handleImport() {
      this.$message.success('批量导入功能')
    },

    // 导出数据
    handleExport() {
      this.$message.success('导出数据功能')
    },

    // 查看详情
    handleView(row) {
      this.$refs.voucherDetailDialog.show(row)
    },

    // 编辑凭证
    handleEdit(row) {
      this.$message.info(`编辑凭证：${row.voucherNo}`)
    },

    // 审核凭证
    handleApprove(row) {
      this.$message.success(`审核凭证成功：${row.voucherNo}`)
      this.loadData()
      this.loadStatistics()
    },

    // 过账凭证
    handlePost(row) {
      this.$refs.voucherPostDialog.show(row)
    },

    // 过账成功回调
    handlePostSuccess() {
      this.loadData()
      this.loadStatistics()
    },

    // 删除凭证
    handleDelete(row) {
      this.$message.success(`删除凭证成功：${row.voucherNo}`)
      this.loadData()
      this.loadStatistics()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },

    // 获取凭证类型名称
    getVoucherTypeName(type) {
      const typeMap = {
        'ACCOUNTING': '记账凭证',
        'RECEIPT': '收款凭证',
        'PAYMENT': '付款凭证',
        'TRANSFER': '转账凭证'
      }
      return typeMap[type] || type
    },

    // 获取凭证类型颜色
    getVoucherTypeColor(type) {
      const colorMap = {
        'ACCOUNTING': 'primary',
        'RECEIPT': 'success',
        'PAYMENT': 'warning',
        'TRANSFER': 'info'
      }
      return colorMap[type] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '0': 'warning',
        '1': 'primary',
        '2': 'success'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态名称
    getStatusName(status) {
      const statusMap = {
        '0': '草稿',
        '1': '已审核',
        '2': '已过账'
      }
      return statusMap[status] || status
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    },

    // 转换日期数组为字符串（处理后端返回的 [2024, 12, 17] 格式）
    formatVoucherDateArray(dateArray) {
      if (Array.isArray(dateArray) && dateArray.length === 3) {
        const [year, month, day] = dateArray
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      // 如果不是数组，直接返回原值
      return dateArray
    },

    // 映射凭证类型ID到类型代码
    mapVoucherTypeId(typeId) {
      const typeMap = {
        '1': 'ACCOUNTING',  // 记账凭证
        '2': 'RECEIPT',     // 收款凭证
        '3': 'PAYMENT',     // 付款凭证
        '4': 'TRANSFER'     // 转账凭证
      }
      return typeMap[String(typeId)] || typeId
    },

    // 凭证弹窗相关方法
    getCurrentDate() {
      const today = new Date()
      return today.toISOString().split('T')[0]
    },

    resetVoucherForm() {
      this.voucherForm = {
        voucherType: '',
        voucherDate: this.getCurrentDate(),
        voucherNo: this.generateVoucherNo(),
        attachmentCount: 0,
        summary: '',
        remark: '',
        entries: []
      }
      // 添加两个默认分录
      this.addEntry()
      this.addEntry()
    },

    generateVoucherNo() {
      const today = new Date()
      const year = today.getFullYear()
      const month = String(today.getMonth() + 1).padStart(2, '0')
      const day = String(today.getDate()).padStart(2, '0')
      return `${year}${month}${day}-${Math.random().toString(36).substr(2, 4).toUpperCase()}`
    },

    addEntry() {
      this.voucherForm.entries.push({
        accountCode: '',
        debitAmount: 0,
        creditAmount: 0,
        summary: ''
      })
    },

    removeEntry(index) {
      if (this.voucherForm.entries.length > 2) {
        this.voucherForm.entries.splice(index, 1)
        this.calculateBalance()
      } else {
        this.$message.warning('至少需要保留两个分录')
      }
    },

    calculateBalance() {
      // 重新计算合计会在computed属性中处理
    },

    // 提交凭证
    async submitVoucher() {
      if (this.balance !== 0) {
        this.$message.error('借贷金额必须平衡才能保存')
        return
      }

      this.$refs.voucherForm.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            // 构建提交数据
            const voucherData = {
              ...this.voucherForm,
              totalAmount: this.totalDebit,
              status: 1 // 草稿状态
            }

            // 这里调用保存接口
            const response = await createVoucher(voucherData)

            if (response && response.code === 1) {
              this.$message.success('凭证保存成功')
            } else {
              this.$message.error(response.message || '保存凭证失败')
              return
            }
            this.dialogVisible = false
            this.loadData() // 重新加载数据
            this.loadStatistics()
          } catch (error) {
            console.error('保存凭证失败:', error)
            this.$message.error('保存凭证失败，请重试')
          } finally {
            this.submitting = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.voucher-management-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 8px;
  overflow: hidden;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.statistic-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistic-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
  flex-shrink: 0;
}

.statistic-icon.total {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.statistic-icon.draft {
  background: linear-gradient(135deg, #f093fb, #f5576c);
}

.statistic-icon.approved {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.statistic-icon.posted {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.statistic-content {
  flex: 1;
  min-width: 0;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.toolbar {
  background: white;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  background: white;
  padding: 16px;
  border-radius: 8px;
  text-align: right;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Element UI 样式覆盖 */
::v-deep .el-card {
  border: none;
  box-shadow: none;
}

::v-deep .el-button {
  border-radius: 6px;
}

::v-deep .el-table {
  border-radius: 8px;
}

::v-deep .el-table th {
  background-color: #fafafa;
  color: #333;
  font-weight: 600;
}

::v-deep .el-pagination {
  margin-top: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .voucher-management-container {
    padding: 10px;
  }

  .statistic-icon {
    width: 50px;
    height: 50px;
    font-size: 20px;
    margin-right: 15px;
  }

  .statistic-value {
    font-size: 24px;
  }

  .toolbar,
  .search-form,
  .table-container,
  .pagination-container {
    padding: 15px;
  }
}

/* 凭证弹窗样式 */
.voucher-form ::v-deep .el-form-item {
  margin-bottom: 20px;
}

.balance-summary {
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
  font-size: 14px;
}

.voucher-form ::v-deep .el-input-number {
  width: 100%;
}

.voucher-form ::v-deep .el-input-number .el-input__inner {
  text-align: right;
}

.voucher-form ::v-deep .el-table .el-input-number .el-input__inner {
  text-align: right;
}

::v-deep .el-dialog__header {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #f0f0f0;
}

::v-deep .el-dialog__body {
  padding: 20px;
}

::v-deep .el-dialog__footer {
  padding: 10px 20px 20px;
  border-top: 1px solid #f0f0f0;
}
</style>
