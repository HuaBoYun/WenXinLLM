<template>
  <div class="fee-recharge-container">
    <!-- 余额卡片 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <div class="balance-section">
        <div class="balance-main">
          <div class="balance-label">当前余额</div>
          <div class="balance-value" :class="{ 'low-balance': balanceInfo.balance < 100 }">
            ¥ {{ balanceInfo.balance != null ? Number(balanceInfo.balance).toFixed(2) : '0.00' }}
          </div>
        </div>
        <div class="balance-stats">
          <div class="balance-stat-item">
            <span class="label">累计消费</span>
            <span class="value">¥ {{ balanceInfo.totalConsumed != null ? Number(balanceInfo.totalConsumed).toFixed(2) : '0.00' }}</span>
          </div>
          <div class="balance-stat-item">
            <span class="label">累计充值</span>
            <span class="value">¥ {{ balanceInfo.totalRecharged != null ? Number(balanceInfo.totalRecharged).toFixed(2) : '0.00' }}</span>
          </div>
          <div class="balance-stat-item">
            <span class="label">初始赠送</span>
            <span class="value">¥ {{ balanceInfo.initAmount != null ? Number(balanceInfo.initAmount).toFixed(2) : '0.00' }}</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 密钥充值 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <div slot="header"><span>密钥充值</span></div>
      <el-form :inline="true">
        <el-form-item label="充值密钥">
          <el-input v-model="licenseKey" placeholder="请输入充值密钥" style="width: 400px;" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="recharging" @click="handleRecharge">确认充值</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 充值记录 -->
    <el-card shadow="hover">
      <div slot="header"><span>充值记录</span></div>
      <el-table v-loading="recordsLoading" :data="records" border style="width: 100%;">
        <el-table-column prop="rechargeAmount" label="充值金额(元)" width="150" align="right" />
        <el-table-column label="充值时间" width="170" align="center">
          <template slot-scope="{ row }">{{ formatDate(row.useTime) }}</template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="120" align="center" />
        <el-table-column prop="remark" label="备注" min-width="200" />
      </el-table>
      <el-pagination
        style="margin-top: 15px; text-align: right;"
        @current-change="handlePageChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :total="recordsTotal"
        layout="total, prev, pager, next"
      />
    </el-card>
  </div>
</template>

<script>
import { queryFeeBalance, rechargeLicense, getLicenseRecords } from '@/api/setting/fee'

export default {
  name: 'FeeRecharge',
  data() {
    return {
      balanceInfo: { balance: 0, totalConsumed: 0, totalRecharged: 0, initAmount: 0 },
      licenseKey: '',
      recharging: false,
      records: [],
      recordsLoading: false,
      recordsTotal: 0,
      pageNum: 1,
      pageSize: 20,
    }
  },
  created() {
    this.fetchBalance()
    this.fetchRecords()
  },
  methods: {
    async fetchBalance() {
      try {
        const res = await queryFeeBalance({})
        if (res.code === 1 || res.code === 200) {
          this.balanceInfo = (res.data && res.data.balance) || {}
        }
      } catch (e) {
        console.error(e)
      }
    },
    async handleRecharge() {
      if (!this.licenseKey) {
        this.$message.warning('请输入充值密钥')
        return
      }
      this.$confirm('确认使用该密钥进行充值？', '充值确认', { type: 'warning' })
        .then(async () => {
          this.recharging = true
          try {
            const res = await rechargeLicense({ licenseKey: this.licenseKey })
            if (res.code === 1 || res.code === 200) {
              const data = res.data || {}
              this.$message.success('充值成功！充值金额: ¥' + (data.rechargeAmount || 0))
              this.licenseKey = ''
              this.fetchBalance()
              this.fetchRecords()
            } else {
              this.$message.error(res.msg || '充值失败')
            }
          } catch (e) {
            this.$message.error('充值失败')
          }
          this.recharging = false
        })
        .catch(() => {})
    },
    async fetchRecords() {
      this.recordsLoading = true
      try {
        const res = await getLicenseRecords({ pageNum: this.pageNum, pageSize: this.pageSize })
        if (res.code === 1 || res.code === 200) {
          const data = res.data || {}
          this.records = data.list || []
          this.recordsTotal = data.total || 0
        }
      } catch (e) {
        console.error(e)
      }
      this.recordsLoading = false
    },
    handlePageChange(val) {
      this.pageNum = val
      this.fetchRecords()
    },
    formatDate(val) {
      if (!val) return ''
      const d = new Date(val)
      const pad = (n) => (n < 10 ? '0' + n : n)
      return (
        d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) +
        ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds())
      )
    },
  },
}
</script>

<style lang="scss" scoped>
.fee-recharge-container {
  padding: 20px;
}
.balance-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.balance-main {
  text-align: center;
  min-width: 200px;
}
.balance-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.balance-value {
  font-size: 32px;
  font-weight: bold;
  color: #67c23a;
}
.balance-value.low-balance {
  color: #f56c6c;
}
.balance-stats {
  display: flex;
  gap: 40px;
}
.balance-stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.balance-stat-item .label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}
.balance-stat-item .value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
