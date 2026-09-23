<template>
  <el-dialog
    title="客户详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="detail-container">
      <!-- 基本信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-user"></i>
          基本信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="客户编码">{{ detailData.customerCode }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ detailData.customerName }}</el-descriptions-item>
          <el-descriptions-item label="客户分类">
            <el-tag :type="getCategoryType(detailData.customerCategory)" size="small">
              {{ getCategoryName(detailData.customerCategory) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="客户状态">
            <el-tag :type="getStatusType(detailData.customerStatus)" size="small">
              {{ getStatusName(detailData.customerStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="统一社会信用代码" :span="2">{{ detailData.creditCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="法定代表人">{{ detailData.legalRepresentative || '-' }}</el-descriptions-item>
          <el-descriptions-item label="注册地址">{{ detailData.registeredAddress || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 联系信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-phone"></i>
          联系信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="联系人">{{ detailData.contactPerson || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ detailData.contactPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="电子邮箱">{{ detailData.contactEmail || '-' }}</el-descriptions-item>
          <el-descriptions-item label="传真号码">{{ detailData.faxNumber || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系地址" :span="2">{{ detailData.contactAddress || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 信用信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-medal"></i>
          信用信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="信用等级">
            <el-rate v-model="creditLevelValue" disabled show-score text-color="#ff9900" />
          </el-descriptions-item>
          <el-descriptions-item label="信用额度">
            <span class="amount-text">¥ {{ formatAmount(detailData.creditLimit) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="收款方式">{{ getCollectionMethodName(detailData.collectionMethod) }}</el-descriptions-item>
          <el-descriptions-item label="账期(天)">{{ detailData.creditTerms || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 银行信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-bank-card"></i>
          银行信息
        </div>
        <el-descriptions :column="2" border size="medium">
          <el-descriptions-item label="开户银行">{{ detailData.bankName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="银行账号">{{ detailData.bankAccount || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 备注信息 -->
      <div class="detail-section">
        <div class="section-title">
          <i class="el-icon-edit-outline"></i>
          备注信息
        </div>
        <div class="remark-content">{{ detailData.remarks || '暂无备注' }}</div>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="handleEdit">编 辑</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'CustomerDetailDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      detailData: {}
    }
  },
  computed: {
    creditLevelValue() {
      const level = this.detailData.creditLevel
      if (level === 'AAA') return 5
      if (level === 'AA') return 4
      if (level === 'A') return 3
      if (level === 'BBB') return 2
      if (level === 'BB') return 1
      return 0
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.detailData = { ...row }
    },
    handleClose() {
      this.dialogVisible = false
      this.detailData = {}
    },
    handleEdit() {
      this.$emit('edit', this.detailData)
      this.handleClose()
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    getCategoryType(category) {
      const map = { 1: 'danger', 2: '', 3: 'warning', 4: 'info' }
      return map[category] || 'info'
    },
    getCategoryName(category) {
      const map = { 1: '重要客户', 2: '一般客户', 3: '潜在客户', 4: '其他客户' }
      return map[category] || '未知'
    },
    getStatusType(status) {
      const map = { 1: 'success', 2: 'warning', 3: 'danger' }
      return map[status] || 'info'
    },
    getStatusName(status) {
      const map = { 1: '正常', 2: '暂停', 3: '黑名单' }
      return map[status] || '未知'
    },
    getCollectionMethodName(method) {
      const map = { 1: '现金', 2: '银行转账', 3: '支票', 4: '承兑汇票', 5: '其他' }
      return map[method] || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-container {
  .detail-section {
    margin-bottom: 24px;

    &:last-child {
      margin-bottom: 0;
    }

    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .amount-text {
      color: #67c23a;
      font-weight: 600;
      font-size: 16px;
    }

    .remark-content {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 12px 16px;
      color: #606266;
      min-height: 60px;
      line-height: 1.6;
    }
  }
}

::v-deep .el-descriptions-item__label {
  width: 120px;
  font-weight: 500;
}
</style>
