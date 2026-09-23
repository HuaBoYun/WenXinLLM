<template>
  <el-dialog
    title="账户详情"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <div class="account-detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="账户ID">
          {{ accountData.accountId }}
        </el-descriptions-item>
        <el-descriptions-item label="账户号码">
          {{ accountData.accountNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="账户名称">
          {{ accountData.accountName }}
        </el-descriptions-item>
        <el-descriptions-item label="账户英文名称">
          {{ accountData.accountNameEng || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="账户类型">
          <el-tag :type="getAccountTypeTag(accountData.accountType)">
            {{ getAccountTypeText(accountData.accountType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账户状态">
          <el-tag :type="getStatusTag(accountData.accountStatus)">
            {{ getStatusText(accountData.accountStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="银行编码">
          {{ accountData.bankCode }}
        </el-descriptions-item>
        <el-descriptions-item label="银行名称">
          {{ accountData.bankName }}
        </el-descriptions-item>
        <el-descriptions-item label="分行编码">
          {{ accountData.branchCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="分行名称">
          {{ accountData.branchName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="币种代码">
          {{ accountData.currencyCode }}
        </el-descriptions-item>
        <el-descriptions-item label="开户日期">
          {{ accountData.openDate || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="销户日期">
          {{ accountData.closeDate || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="是否默认账户">
          <el-tag v-if="accountData.isDefault === 1" type="success" size="mini">是</el-tag>
          <el-tag v-else type="info" size="mini">否</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否银企直联">
          <el-tag v-if="accountData.isDirectConnect === 1" type="success" size="mini">是</el-tag>
          <el-tag v-else type="info" size="mini">否</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="直联类型" v-if="accountData.isDirectConnect === 1">
          {{ getDirectConnectTypeText(accountData.directConnectType) }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 余额信息 -->
      <div class="balance-section">
        <h3>余额信息</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="balance-card">
              <div class="balance-label">账户余额</div>
              <div class="balance-amount">{{ formatAmount(accountData.balance) }}</div>
              <div class="balance-currency">{{ accountData.currencyCode }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="balance-card available">
              <div class="balance-label">可用余额</div>
              <div class="balance-amount">{{ formatAmount(accountData.availableBalance) }}</div>
              <div class="balance-currency">{{ accountData.currencyCode }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="balance-card frozen">
              <div class="balance-label">冻结余额</div>
              <div class="balance-amount">{{ formatAmount(accountData.frozenBalance) }}</div>
              <div class="balance-currency">{{ accountData.currencyCode }}</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 备注信息 -->
      <div class="remark-section" v-if="accountData.remark">
        <h3>备注信息</h3>
        <div class="remark-content">
          {{ accountData.remark }}
        </div>
      </div>

      <!-- 创建和更新信息 -->
      <div class="meta-section">
        <h3>操作信息</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(accountData.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="创建用户">
            {{ accountData.createUser || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(accountData.updateTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新用户">
            {{ accountData.updateUser || '-' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AccountDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    accountData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    /** 获取账户类型标签样式 */
    getAccountTypeTag(type) {
      const tagMap = {
        'BASIC': 'success',
        'GENERAL': 'primary',
        'SPECIAL': 'warning',
        'TEMPORARY': 'info'
      }
      return tagMap[type] || 'info'
    },
    /** 获取账户类型文本 */
    getAccountTypeText(type) {
      const textMap = {
        'BASIC': '基本账户',
        'GENERAL': '一般账户',
        'SPECIAL': '专用账户',
        'TEMPORARY': '临时账户'
      }
      return textMap[type] || type
    },
    /** 获取状态标签样式 */
    getStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'FROZEN': 'danger',
        'CLOSED': 'info'
      }
      return tagMap[status] || 'info'
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'INACTIVE': '非活跃',
        'FROZEN': '冻结',
        'CLOSED': '关闭'
      }
      return textMap[status] || status
    },
    /** 获取直联类型文本 */
    getDirectConnectTypeText(type) {
      const textMap = {
        'QUERY': '查询',
        'PAYMENT': '支付',
        'RECEIPT': '收款',
        'TRANSFER': '转账',
        'ALL': '全部'
      }
      return textMap[type] || type
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    /** 格式化日期时间 */
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.account-detail {
  padding: 20px 0;
}

.balance-section {
  margin-top: 30px;
}

.balance-section h3 {
  margin-bottom: 20px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.balance-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.balance-card.available {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.balance-card.frozen {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.balance-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.balance-amount {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.balance-currency {
  font-size: 12px;
  opacity: 0.8;
}

.remark-section {
  margin-top: 30px;
}

.remark-section h3 {
  margin-bottom: 15px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.remark-content {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  border-left: 4px solid #409eff;
  line-height: 1.6;
}

.meta-section {
  margin-top: 30px;
}

.meta-section h3 {
  margin-bottom: 15px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.dialog-footer {
  text-align: right;
}
</style>
