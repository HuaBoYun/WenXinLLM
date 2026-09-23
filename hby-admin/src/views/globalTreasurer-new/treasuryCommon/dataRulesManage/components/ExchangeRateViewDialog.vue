<template>
  <el-dialog
    title="汇率详情"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="exchange-rate-detail" v-if="exchangeRateInfo">
      <el-card class="detail-card">
        <div slot="header">
          <span>基本信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="货币对">
            <div class="currency-pair">
              <span class="base-currency">{{ exchangeRateInfo.baseCurrency }}</span>
              <i class="el-icon-right"></i>
              <span class="target-currency">{{ exchangeRateInfo.targetCurrency }}</span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="汇率">
            <span class="rate-value">{{ exchangeRateInfo.exchangeRate }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="汇率类型">
            <el-tag :type="getRateTypeColor(exchangeRateInfo.rateType)">
              {{ getRateTypeText(exchangeRateInfo.rateType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="数据来源">
            <el-tag size="mini">{{ getSourceText(exchangeRateInfo.dataSource) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="生效日期">
            {{ exchangeRateInfo.effectiveDate }}
          </el-descriptions-item>
          <el-descriptions-item label="失效日期">
            {{ exchangeRateInfo.expireDate || '永久有效' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="exchangeRateInfo.status === 1 ? 'success' : 'danger'">
              {{ exchangeRateInfo.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="detail-card">
        <div slot="header">
          <span>汇率换算</span>
        </div>
        <div class="rate-converter">
          <el-row :gutter="20">
            <el-col :span="10">
              <el-input
                v-model="convertAmount"
                type="number"
                placeholder="输入金额"
                @input="handleConvert"
              >
                <template slot="append">{{ exchangeRateInfo.baseCurrency }}</template>
              </el-input>
            </el-col>
            <el-col :span="4" class="convert-arrow">
              <i class="el-icon-right"></i>
            </el-col>
            <el-col :span="10">
              <el-input
                v-model="convertedAmount"
                type="number"
                placeholder="换算结果"
                readonly
              >
                <template slot="append">{{ exchangeRateInfo.targetCurrency }}</template>
              </el-input>
            </el-col>
          </el-row>
          <div class="convert-formula" v-if="convertAmount && exchangeRateInfo.exchangeRate">
            <span>换算公式：{{ convertAmount }} × {{ exchangeRateInfo.exchangeRate }} = {{ convertedAmount }}</span>
          </div>
        </div>
      </el-card>

      <el-card class="detail-card" v-if="exchangeRateInfo.remark">
        <div slot="header">
          <span>备注信息</span>
        </div>
        <div class="remark-content">
          {{ exchangeRateInfo.remark }}
        </div>
      </el-card>

      <el-card class="detail-card">
        <div slot="header">
          <span>操作记录</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :timestamp="activity.timestamp"
            :type="activity.type"
          >
            {{ activity.content }}
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="handleEdit">编 辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExchangeRateViewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    exchangeRateData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      exchangeRateInfo: this.exchangeRateData,
      convertAmount: '',
      convertedAmount: '',
      activities: []
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initData()
      }
    },
    exchangeRateData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.exchangeRateInfo = { ...newVal }
          this.generateActivities()
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    initData() {
      this.convertAmount = ''
      this.convertedAmount = ''
      this.generateActivities()
    },
    generateActivities() {
      if (!this.exchangeRateInfo) return

      this.activities = [
        {
          content: '汇率配置创建',
          timestamp: this.exchangeRateInfo.createTime || '2024-01-01 10:00:00',
          type: 'primary'
        },
        {
          content: '汇率数据更新',
          timestamp: this.exchangeRateInfo.updateTime || '2024-09-25 15:30:00',
          type: 'success'
        }
      ]

      if (this.exchangeRateInfo.status === 1) {
        this.activities.push({
          content: '汇率配置启用',
          timestamp: '2024-09-25 16:00:00',
          type: 'success'
        })
      }

      this.activities.push({
        content: '汇率详情查看',
        timestamp: new Date().toLocaleString(),
        type: 'info'
      })
    },
    handleConvert() {
      if (this.convertAmount && this.exchangeRateInfo && this.exchangeRateInfo.exchangeRate) {
        this.convertedAmount = (parseFloat(this.convertAmount) * parseFloat(this.exchangeRateInfo.exchangeRate)).toFixed(6)
      } else {
        this.convertedAmount = ''
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    handleEdit() {
      this.$emit('edit', this.exchangeRateInfo)
      this.handleClose()
    },
    getRateTypeColor(type) {
      const colorMap = {
        'SPOT': 'success',
        'CASH': 'warning',
        'MIDDLE': 'info'
      }
      return colorMap[type] || 'info'
    },
    getRateTypeText(type) {
      const textMap = {
        'SPOT': '现汇',
        'CASH': '现钞',
        'MIDDLE': '中间价'
      }
      return textMap[type] || '未知'
    },
    getSourceText(source) {
      const textMap = {
        'MANUAL': '手工录入',
        'AUTO': '自动获取',
        'API': 'API接口'
      }
      return textMap[source] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.exchange-rate-detail {
  .detail-card {
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .currency-pair {
    display: flex;
    align-items: center;
    font-weight: 600;

    .base-currency {
      color: #409eff;
    }

    .target-currency {
      color: #67c23a;
    }

    i {
      margin: 0 8px;
      color: #909399;
    }
  }

  .rate-value {
    font-family: 'Monaco', 'Menlo', monospace;
    font-size: 16px;
    font-weight: 600;
    color: #e6a23c;
  }

  .rate-converter {
    .convert-arrow {
      text-align: center;
      line-height: 40px;
      color: #909399;
      font-size: 18px;
    }

    .convert-formula {
      margin-top: 15px;
      padding: 10px;
      background-color: #f8f9fa;
      border-radius: 4px;
      color: #606266;
      font-size: 14px;
    }
  }

  .remark-content {
    color: #606266;
    line-height: 1.6;
  }
}

.dialog-footer {
  text-align: right;
}
</style>