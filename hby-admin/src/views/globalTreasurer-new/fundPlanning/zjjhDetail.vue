<template>
  <div class="app-container">
    <el-page-header @back="goBack" content="资金计划详情" />

    <el-card v-loading="loading" class="mt20">
      <div slot="header">
        <span>基本信息</span>
        <el-tag :type="statusTagType(detail.planStatus)" class="ml10" size="small">{{ statusLabel(detail.planStatus) }}</el-tag>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="计划编号">{{ detail.planNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划名称">{{ detail.planName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划类型">{{ planTypeLabel(detail.planType) }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detail.startDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detail.endDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="完成率">{{ detail.completionRate != null ? detail.completionRate + '%' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="预计收入">{{ formatAmount(detail.totalIncome) }}</el-descriptions-item>
        <el-descriptions-item label="预计支出">{{ formatAmount(detail.totalExpense) }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detail.createdByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createdTime | parseTime }}</el-descriptions-item>
        <el-descriptions-item label="更新人">{{ detail.updatedByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updatedTime | parseTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detail.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="3">{{ detail.description || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div class="mt20">
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getFundPlan } from '@/api/globalTreasurer/zjjh'
import { parseTime } from '@/utils'

const PLAN_STATUS_MAP = {
  DRAFT: { label: '草稿', type: 'info' },
  PENDING_APPROVAL: { label: '待审批', type: 'warning' },
  APPROVED: { label: '已审批', type: 'success' },
  EXECUTING: { label: '执行中', type: 'primary' },
  COMPLETED: { label: '已完成', type: 'success' },
  CANCELLED: { label: '已取消', type: 'danger' },
  REJECTED: { label: '已拒绝', type: 'danger' }
}

const PLAN_TYPE_MAP = {
  ANNUAL: '年度计划',
  QUARTERLY: '季度计划',
  MONTHLY: '月度计划',
  WEEKLY: '周计划',
  DAILY: '日计划'
}

export default {
  name: 'ZjjhDetail',
  filters: { parseTime },
  data() {
    return {
      loading: false,
      detail: {}
    }
  },
  created() {
    const planId = this.$route.query.planId
    if (planId) {
      this.fetchDetail(planId)
    } else {
      this.$message.error('缺少计划ID参数')
    }
  },
  methods: {
    fetchDetail(planId) {
      this.loading = true
      getFundPlan(planId).then(response => {
        if (response.code === 1) {
          this.detail = response.data || {}
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      }).catch(() => {
        this.$message.error('获取详情失败')
      }).finally(() => {
        this.loading = false
      })
    },
    goBack() {
      this.$router.back()
    },
    statusLabel(status) {
      return (PLAN_STATUS_MAP[status] || {}).label || status || '-'
    },
    statusTagType(status) {
      return (PLAN_STATUS_MAP[status] || {}).type || 'info'
    },
    planTypeLabel(type) {
      return PLAN_TYPE_MAP[type] || type || '-'
    },
    formatAmount(amount) {
      if (amount == null) return '-'
      return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(amount)
    }
  }
}
</script>

<style scoped>
.mt20 { margin-top: 20px; }
.ml10 { margin-left: 10px; }
</style>

