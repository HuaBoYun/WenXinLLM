<template>
  <div class="voucher-form-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>{{ pageTitle }}</h2>
      <p>{{ pageDescription }}</p>
    </div>

    <!-- 凭证表单 -->
    <div class="form-container">
      <el-form :model="voucherForm" :rules="!isView ? formRules : {}" ref="voucherForm" label-width="120px" :disabled="isView">
        <!-- 基本信息 -->
        <div class="form-section">
          <h3>基本信息</h3>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="凭证编号" prop="voucherNo">
                <el-input v-model="voucherForm.voucherNo" placeholder="系统自动生成" readonly />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="凭证类型" prop="voucherType">
                <el-select v-model="voucherForm.voucherType" placeholder="请选择凭证类型" style="width: 100%">
                  <el-option label="记账凭证" value="ACCOUNTING" />
                  <el-option label="收款凭证" value="RECEIPT" />
                  <el-option label="付款凭证" value="PAYMENT" />
                  <el-option label="转账凭证" value="TRANSFER" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="制单日期" prop="voucherDate">
                <el-date-picker
                  v-model="voucherForm.voucherDate"
                  type="date"
                  placeholder="请选择制单日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="凭证摘要" prop="summary">
                <el-input v-model="voucherForm.summary" placeholder="请输入凭证摘要" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="附单据数" prop="attachmentCount">
                <el-input-number v-model="voucherForm.attachmentCount" :min="0" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 分录信息 -->
        <div class="form-section">
          <h3>分录信息</h3>
          <div class="entry-toolbar">
            <el-button type="primary" icon="el-icon-plus" @click="addEntry">添加分录</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="removeSelectedEntries" :disabled="selectedEntries.length === 0">
              删除分录
            </el-button>
          </div>

          <el-table :data="voucherForm.entries" border @selection-change="handleEntrySelection">
            <el-table-column type="selection" width="55" />
            <el-table-column label="科目代码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.accountCode" placeholder="请输入科目代码" @blur="handleAccountCodeBlur(scope.row)" />
              </template>
            </el-table-column>
            <el-table-column label="科目名称" width="200">
              <template slot-scope="scope">
                <el-input v-model="scope.row.accountName" placeholder="请输入科目名称" readonly />
              </template>
            </el-table-column>
            <el-table-column label="摘要" width="200">
              <template slot-scope="scope">
                <el-input v-model="scope.row.summary" placeholder="请输入摘要" />
              </template>
            </el-table-column>
            <el-table-column label="借方金额" width="150" align="right">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.debitAmount"
                  :precision="2"
                  :min="0"
                  @change="calculateTotal"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
            <el-table-column label="贷方金额" width="150" align="right">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.creditAmount"
                  :precision="2"
                  :min="0"
                  @change="calculateTotal"
                  style="width: 100%"
                />
              </template>
            </el-table-column>
          </el-table>

          <!-- 金额汇总 -->
          <div class="amount-summary">
            <el-row>
              <el-col :span="12">
                <div class="summary-item">
                  <span>借方合计：</span>
                  <span class="amount">¥{{ formatAmount(totalDebit) }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="summary-item">
                  <span>贷方合计：</span>
                  <span class="amount">¥{{ formatAmount(totalCredit) }}</span>
                </div>
              </el-col>
            </el-row>
            <div class="balance-status" :class="{ 'balanced': isBalanced }">
              {{ isBalanced ? '✓ 平衡' : '✗ 不平衡（差额：¥' + formatAmount(Math.abs(totalDebit - totalCredit)) + '）' }}
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="form-actions">
          <template v-if="isView">
            <el-button @click="handleBack">返回</el-button>
          </template>
          <template v-else>
            <el-button @click="handleCancel">取消</el-button>
            <el-button type="primary" @click="handleSaveDraft" :loading="saving">保存草稿</el-button>
            <el-button type="success" @click="handleSubmit" :loading="saving" :disabled="!isBalanced">提交凭证</el-button>
          </template>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { createVoucher, getVoucherById, updateVoucher } from '@/api/financialSharing/voucher'

export default {
  name: 'VoucherForm',
  data() {
    return {
      saving: false,
      isEdit: false,
      isView: false,
      mode: 'create', // create, edit, view
      selectedEntries: [],

      voucherForm: {
        voucherId: '',
        voucherNo: '',
        voucherType: 'ACCOUNTING',
        voucherDate: new Date().toISOString().split('T')[0],
        summary: '',
        attachmentCount: 0,
        status: '0',
        entries: []
      },

      formRules: {
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
    pageTitle() {
      if (this.isView) return '查看凭证'
      if (this.isEdit) return '编辑凭证'
      return '新建凭证'
    },

    pageDescription() {
      if (this.isView) return '查看会计凭证详细信息（只读模式）'
      if (this.isEdit) return '修改现有会计凭证信息'
      return '录入新的会计凭证'
    },

    totalDebit() {
      return this.voucherForm.entries.reduce((sum, entry) => sum + (entry.debitAmount || 0), 0)
    },

    totalCredit() {
      return this.voucherForm.entries.reduce((sum, entry) => sum + (entry.creditAmount || 0), 0)
    },

    isBalanced() {
      return Math.abs(this.totalDebit - this.totalCredit) < 0.01
    }
  },

  async mounted() {
    // 检查模式
    const voucherId = this.$route.params.voucherId
    const queryMode = this.$route.query.mode

    if (voucherId) {
      if (queryMode === 'view') {
        // 查看模式
        this.isView = true
        this.mode = 'view'
      } else {
        // 编辑模式
        this.isEdit = true
        this.mode = 'edit'
      }
      await this.loadVoucherData(voucherId)
    } else {
      // 新建模式
      this.mode = 'create'
      this.voucherForm.voucherNo = this.generateVoucherNo()
      // 添加默认分录行
      this.addEntry()
      this.addEntry()
    }
  },

  methods: {
    // 生成凭证编号
    generateVoucherNo() {
      const today = new Date()
      const year = today.getFullYear()
      const month = String(today.getMonth() + 1).padStart(2, '0')
      const day = String(today.getDate()).padStart(2, '0')
      const random = Math.floor(Math.random() * 9999).toString().padStart(4, '0')
      return `PZ${year}${month}${day}${random}`
    },

    // 加载凭证数据
    async loadVoucherData(voucherId) {
      try {
        const response = await getVoucherById(voucherId)
        if (response.code === 200) {
          this.voucherForm = {
            ...response.data,
            entries: response.data.entries || []
          }
        } else {
          this.$message.error('加载凭证数据失败：' + response.msg)
        }
      } catch (error) {
        console.error('加载凭证数据失败:', error)
        this.$message.error('加载凭证数据失败')
      }
    },

    // 添加分录
    addEntry() {
      this.voucherForm.entries.push({
        accountCode: '',
        accountName: '',
        summary: this.voucherForm.summary,
        debitAmount: 0,
        creditAmount: 0
      })
    },

    // 删除选中分录
    removeSelectedEntries() {
      if (this.selectedEntries.length === 0) return

      this.$confirm('确认删除选中的分录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const selectedIds = this.selectedEntries.map(entry => this.voucherForm.entries.indexOf(entry))
        this.voucherForm.entries = this.voucherForm.entries.filter((_, index) => !selectedIds.includes(index))
        this.selectedEntries = []
        this.calculateTotal()
      }).catch(() => {})
    },

    // 处理分录选择
    handleEntrySelection(selection) {
      this.selectedEntries = selection
    },

    // 处理科目代码失焦事件
    handleAccountCodeBlur(entry) {
      // 这里可以根据科目代码查询科目名称
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      if (entry.accountCode) {
        const accountMap = {
          '1001': '现金',
          '1002': '银行存款',
          '1121': '应收账款',
          '1401': '材料采购',
          '1601': '固定资产',
          '2001': '短期借款',
          '2202': '应付账款',
          '4001': '实收资本',
          '5001': '生产成本',
          '6001': '主营业务收入'
        }
        entry.accountName = accountMap[entry.accountCode] || ''
      }
    },

    // 计算总金额
    calculateTotal() {
      // 金额变化时自动计算
    },

    // 保存草稿
    async handleSaveDraft() {
      if (!this.validateForm()) return

      this.saving = true
      try {
        const voucherData = {
          ...this.voucherForm,
          status: '0',
          totalAmount: this.totalDebit
        }

        let response
        if (this.isEdit) {
          response = await updateVoucher(voucherData)
        } else {
          response = await createVoucher(voucherData)
        }

        if (response.code === 200) {
          this.$message.success('保存草稿成功')
          this.$router.push('/management/financial/accounting/voucher-management')
        } else {
          this.$message.error('保存失败：' + response.msg)
        }
      } catch (error) {
        console.error('保存草稿失败:', error)
        this.$message.error('保存草稿失败')
      } finally {
        this.saving = false
      }
    },

    // 提交凭证
    async handleSubmit() {
      if (!this.validateForm()) return

      if (!this.isBalanced) {
        this.$message.error('借贷不平衡，无法提交')
        return
      }

      if (this.voucherForm.entries.length < 2) {
        this.$message.error('至少需要两个分录才能提交凭证')
        return
      }

      this.saving = true
      try {
        const voucherData = {
          ...this.voucherForm,
          status: '1',
          totalAmount: this.totalDebit
        }

        let response
        if (this.isEdit) {
          response = await updateVoucher(voucherData)
        } else {
          response = await createVoucher(voucherData)
        }

        if (response.code === 200) {
          this.$message.success('凭证提交成功')
          this.$router.push('/management/financial/accounting/voucher-management')
        } else {
          this.$message.error('提交失败：' + response.msg)
        }
      } catch (error) {
        console.error('提交凭证失败:', error)
        this.$message.error('提交凭证失败')
      } finally {
        this.saving = false
      }
    },

    // 取消
    handleCancel() {
      this.$router.push('/financialSharing/platform/accounting/voucher-management')
    },

    // 返回列表
    handleBack() {
      this.$router.push('/financialSharing/platform/accounting/voucher-management')
    },

    // 表单验证
    validateForm() {
      this.$refs.voucherForm.validate((valid) => {
        if (!valid) {
          this.$message.error('请完善基本信息')
          return false
        }
      })

      // 验证分录
      if (this.voucherForm.entries.length === 0) {
        this.$message.error('请添加分录')
        return false
      }

      for (let entry of this.voucherForm.entries) {
        if (!entry.accountCode) {
          this.$message.error('请填写科目代码')
          return false
        }
        if (!entry.debitAmount && !entry.creditAmount) {
          this.$message.error('请填写金额')
          return false
        }
        if (entry.debitAmount && entry.creditAmount) {
          this.$message.error('同一分录不能同时填写借方和贷方金额')
          return false
        }
      }

      return true
    },

    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style scoped>
.voucher-form-container {
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
  color: #909399;
  font-size: 14px;
}

.form-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 30px;
}

.form-section h3 {
  margin: 0 0 20px 0;
  color: #303133;
  border-bottom: 2px solid #409eff;
  padding-bottom: 8px;
}

.entry-toolbar {
  margin-bottom: 10px;
}

.amount-summary {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.summary-item {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.amount {
  color: #409eff;
  margin-left: 10px;
}

.balance-status {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  color: #f56c6c;
  padding: 10px;
  border-radius: 4px;
  background: #fef0f0;
}

.balance-status.balanced {
  color: #67c23a;
  background: #f0f9ff;
}

.form-actions {
  text-align: center;
  margin-top: 30px;
}

.form-actions .el-button {
  margin: 0 10px;
  min-width: 120px;
}
</style>