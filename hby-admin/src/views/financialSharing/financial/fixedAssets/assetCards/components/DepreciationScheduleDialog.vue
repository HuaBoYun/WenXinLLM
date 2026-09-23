<template>
  <el-dialog
    title="折旧计划"
    :visible.sync="visible"
    width="1000px"
    @close="handleClose"
  >
    <div class="schedule-header">
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="资产编码">{{ assetInfo.assetCode }}</el-descriptions-item>
        <el-descriptions-item label="资产名称">{{ assetInfo.assetName }}</el-descriptions-item>
        <el-descriptions-item label="资产原值">{{ formatAmount(assetInfo.originalValue) }}</el-descriptions-item>
        <el-descriptions-item label="折旧方法">{{ assetInfo.depreciationMethod }}</el-descriptions-item>
        <el-descriptions-item label="使用年限">{{ assetInfo.usefulLife }}年</el-descriptions-item>
        <el-descriptions-item label="预计净残值">{{ formatAmount(assetInfo.residualValue) }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="schedule-table">
      <el-table
        :data="scheduleData"
        v-loading="loading"
        border
        stripe
        max-height="400"
      >
        <el-table-column type="index" label="期数" width="60" align="center" />
        <el-table-column prop="period" label="计提期间" width="120" align="center" />
        <el-table-column prop="depreciationAmount" label="折旧额" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.depreciationAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="accumulatedDepreciation" label="累计折旧" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.accumulatedDepreciation) }}
          </template>
        </el-table-column>
        <el-table-column prop="netBookValue" label="账面净值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.netBookValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="voucherNumber" label="凭证号" width="150" align="center" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      </el-table>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDepreciationSchedule } from '@/api/financialSharing/fixedAssets'

export default {
  name: 'DepreciationScheduleDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    assetInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      scheduleData: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.assetInfo.assetId) {
        this.loadSchedule()
      }
    }
  },
  methods: {
    async loadSchedule() {
      this.loading = true
      try {
        const response = await getDepreciationSchedule(this.assetInfo.assetId)
        if (response.code === 1) {
          this.scheduleData = response.data || []
        } else {
          this.$message.error(response.msg || '加载折旧计划失败')
        }
      } catch (error) {
        console.error('加载折旧计划失败：', error)
        this.$message.error('加载折旧计划失败')
      } finally {
        this.loading = false
      }
    },
    handleExport() {
      try {
        const data = this.scheduleData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '折旧计划表导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },
    getStatusType(status) {
      const typeMap = {
        'PENDING': 'info',
        'COMPLETED': 'success',
        'ADJUSTED': 'warning'
      }
      return typeMap[status] || 'default'
    },
    getStatusText(status) {
      const textMap = {
        'PENDING': '未计提',
        'COMPLETED': '已计提',
        'ADJUSTED': '已调整'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.schedule-header {
  margin-bottom: 20px;
}

.schedule-table {
  margin-bottom: 20px;
}
</style>

